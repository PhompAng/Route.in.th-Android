package th.in.route.routeinth;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.ButterKnife;
import th.in.route.routeinth.adapter.NearbyPlaceAdapter;
import th.in.route.routeinth.adapter.viewholder.FacilityAdapter;
import th.in.route.routeinth.app.FoursquareUtils;
import th.in.route.routeinth.model.foursquare.MyCompactVenue;
import th.in.route.routeinth.model.system.Station;

public class StationMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Location stationLocation;
    private Station station;
    private BottomSheetBehavior behavior;
    private ArrayList<String> facilities;
    private FacilityAdapter adapter;
    private NearbyPlaceAdapter nearbyPlaceAdapter;
    private List<MyCompactVenue> mVenues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_map);
        ButterKnife.bind(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        facilities = new ArrayList<>();
        mVenues = new ArrayList<>();

        Intent intent = getIntent();
        stationLocation = intent.getParcelableExtra("location");
        station = Parcels.unwrap(intent.getParcelableExtra("station"));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.facilityRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        adapter = new FacilityAdapter(getApplicationContext(), facilities);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        RecyclerView nearbyRecycler = (RecyclerView) findViewById(R.id.nearbyRecycler);
        LinearLayoutManager nearbyLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        nearbyPlaceAdapter = new NearbyPlaceAdapter(mVenues, getApplicationContext());
        nearbyRecycler.setHasFixedSize(true);
        nearbyRecycler.setLayoutManager(nearbyLinearLayoutManager);
        nearbyRecycler.setAdapter(nearbyPlaceAdapter);

        getFacilities();
        getNearbyPlace();
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
        String ll = String.valueOf(stationLocation.getLatitude()) + "," + String.valueOf(stationLocation.getLongitude());
        try {
            mVenues.clear();
            mVenues.addAll(FoursquareUtils.venuesSearch(ll));
            nearbyPlaceAdapter.notifyDataSetChanged();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//    private void getNearbyPlace() {
//        th.in.route.routeinth.model.place.Location location = new th.in.route.routeinth.model.place.Location();
//        location.setLat(stationLocation.getLatitude());
//        location.setLng(stationLocation.getLongitude());
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://maps.googleapis.com/maps/")
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        GooglePlaceService service = retrofit.create(GooglePlaceService.class);
//        service.getNearbyPlace(location)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnNext(new Action1<PlaceResponse>() {
//                    @Override
//                    public void call(PlaceResponse response) {
//                        places.addAll(response.getResults());
//                        nearbyPlaceAdapter.notifyDataSetChanged();
//                    }
//                })
//                .doOnError(new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        Log.e("DistanceUtils", throwable.getMessage());
//                    }
//                })
//                .subscribe();
//    }

}
