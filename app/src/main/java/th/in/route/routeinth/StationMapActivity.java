package th.in.route.routeinth;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import th.in.route.routeinth.adapter.viewholder.FacilityAdapter;
import th.in.route.routeinth.app.DpiUtils;
import th.in.route.routeinth.model.place.PlaceResponse;
import th.in.route.routeinth.model.place.Results;
import th.in.route.routeinth.model.result.Result;
import th.in.route.routeinth.model.system.Station;
import th.in.route.routeinth.services.GooglePlaceService;

public class StationMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Location stationLocation;
    private Station station;
    private Unbinder unbinder;
    private BottomSheetBehavior behavior;
    private ArrayList<String> facilities;
    private FacilityAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private List<Results> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_map);
        unbinder = ButterKnife.bind(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        facilities = new ArrayList<>();
        results = new ArrayList<>();

        Intent intent = getIntent();
        stationLocation = intent.getParcelableExtra("location");
        station = Parcels.unwrap(intent.getParcelableExtra("station"));
        getFacilities();
        getNearbyPlace();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.facilityRecycler);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new FacilityAdapter(getApplicationContext(), facilities);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng stationLatLong = new LatLng(stationLocation.getLatitude(), stationLocation.getLongitude());
        // Add a marker in Sydney, Australia, and move the camera.
        mMap.moveCamera(CameraUpdateFactory.newLatLng(stationLatLong));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));

    }

    public void showStationDetail(){
//        View v = LayoutInflater.from(StationMapActivity.this).inflate(R.layout.bottom_station_detail, null);
        View mBottomSheetLayout = findViewById(R.id.bottom_statin_detail);
        behavior = BottomSheetBehavior.from(mBottomSheetLayout);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

    }

    private void getFacilities(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference fireDatabaseReference = firebaseDatabase.getReference();
        fireDatabaseReference.child("facilities").child(station.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                facilities.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    facilities.add(snapshot.getValue(String.class));
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getNearbyPlace() {
        th.in.route.routeinth.model.place.Location location = new th.in.route.routeinth.model.place.Location();
        location.setLat(stationLocation.getLatitude());
        location.setLng(stationLocation.getLongitude());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GooglePlaceService service = retrofit.create(GooglePlaceService.class);
        service.getNearbyPlace(location)
                .subscribeOn(Schedulers.newThread())
                .doOnNext(new Action1<PlaceResponse>() {
                    @Override
                    public void call(PlaceResponse response) {
                        results = response.getResults();
                    }
                })
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("DistanceUtils", throwable.getMessage());
                    }
                })
                .subscribe();
    }

}
