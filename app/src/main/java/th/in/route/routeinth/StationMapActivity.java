package th.in.route.routeinth;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.parceler.Parcels;

import th.in.route.routeinth.model.system.Station;

public class StationMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Location stationLocation;
    private Station station;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_map);

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
}
