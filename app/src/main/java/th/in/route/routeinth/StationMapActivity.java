package th.in.route.routeinth;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import th.in.route.routeinth.app.DpiUtils;
import th.in.route.routeinth.model.system.Station;

public class StationMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Location stationLocation;
    private Station station;
    Unbinder unbinder;
    BottomSheetBehavior behavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_map);
        unbinder = ButterKnife.bind(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        stationLocation = intent.getParcelableExtra("location");
        station = Parcels.unwrap(intent.getParcelableExtra("station"));

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
}
