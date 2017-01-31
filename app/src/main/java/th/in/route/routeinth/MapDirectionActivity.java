package th.in.route.routeinth;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransitMode;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.in.route.routeinth.adapter.StepAdapter;

public class MapDirectionActivity extends AppCompatActivity implements OnMapReadyCallback, DirectionCallback {

    private GoogleMap mMap;
    private LatLng origin;
    private LatLng destination;

    @BindView(R.id.list)
    RecyclerView list;
    private StepAdapter stepAdapter;
    private List<Step> stepList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_direction);
        ButterKnife.bind(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        origin = intent.getParcelableExtra("origin");
        destination = intent.getParcelableExtra("destination");

        stepList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        stepAdapter = new StepAdapter(this, stepList);
        list.setHasFixedSize(true);
        list.setLayoutManager(layoutManager);
        list.setAdapter(stepAdapter);

        GoogleDirection.withServerKey("AIzaSyCCItC0aFhqEKphc5NuOnhWEmK2BRLJnqM")
                .from(origin)
                .to(destination)
                .transportMode(TransportMode.TRANSIT)
                .transitMode(TransitMode.BUS)
                .execute(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        Snackbar.make(findViewById(R.id.map_direction), "Success with status : " + direction.getStatus(), Snackbar.LENGTH_SHORT).show();
        if (direction.isOK()) {
            ArrayList<LatLng> sectionPositionList = direction.getRouteList().get(0).getLegList().get(0).getSectionPoint();
            for (LatLng position : sectionPositionList) {
                mMap.addMarker(new MarkerOptions().position(position));
            }

            stepList.addAll(direction.getRouteList().get(0).getLegList().get(0).getStepList());
            stepAdapter.notifyDataSetChanged();
            ArrayList<PolylineOptions> polylineOptionList = DirectionConverter.createTransitPolyline(this, stepList, 5, Color.RED, 3, Color.BLUE);
            for (PolylineOptions polylineOption : polylineOptionList) {
                mMap.addPolyline(polylineOption);
            }
        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
