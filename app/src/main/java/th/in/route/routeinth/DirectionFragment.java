package th.in.route.routeinth;


import android.app.AlertDialog;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import th.in.route.routeinth.app.DistanceUtils;
import th.in.route.routeinth.app.StationUtils;
import th.in.route.routeinth.model.StationEvent;
import th.in.route.routeinth.model.system.Station;
import th.in.route.routeinth.view.StationChip;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DirectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DirectionFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<StationEvent> stations;
    private StationUtils stationUtils;

    public DirectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DirectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DirectionFragment newInstance(String param1, String param2) {
        DirectionFragment fragment = new DirectionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setRetainInstance(true);
        stationUtils = StationUtils.getInstance();
        stations = new ArrayList<>();
        stations.add(null);
        stations.add(null);
    }

    private Unbinder unbinder;

    @BindView(R.id.origin)
    TextView mOrigin;
    @BindView(R.id.destination)
    TextView mDestination;
    @BindView(R.id.origin_station_chip)
    StationChip mOriginChip;
    @BindView(R.id.destination_station_chip)
    StationChip mDestinationChip;
    @BindView(R.id.calculate)
    Button mCalculate;
    @BindView(R.id.map_img)
    ImageView map_img;
    @BindView(R.id.map)
    ConstraintLayout map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_direction, container, false);
        unbinder = ButterKnife.bind(this, v);
        setStation();

        Glide.with(this).load(R.drawable.map).into(map_img);

        setPinsTransparent();

        for (int i=1; i<map.getChildCount();i++) {
            map.getChildAt(i).setOnClickListener(this);
        }
        return v;
    }

    @OnClick(R.id.origin)
    public void selectOrigin() {
        StationSelectFragment stationSelectFragment = StationSelectFragment.newInstance(0);
        getFragmentManager().beginTransaction().replace(R.id.flContent, stationSelectFragment).addToBackStack(null).commit();
    }

    @OnClick(R.id.destination)
    public void selectDestination() {
        StationSelectFragment stationSelectFragment = StationSelectFragment.newInstance(1);
        getFragmentManager().beginTransaction().replace(R.id.flContent, stationSelectFragment).addToBackStack(null).commit();
    }

    @OnClick(R.id.swap)
    public void swap() {
        Collections.swap(stations, 0, 1);
        setStation();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void retrieveStation(StationEvent station) {
        if (!station.isStation()) {
            DistanceUtils distanceUtils = DistanceUtils.getInstance();
            LatLng latLng = station.getPlace().getLatLng();
            Station s = stationUtils.getStationFromKey(distanceUtils.getNearestStation(latLng.latitude, latLng.longitude));
            station.setStation(s);
        }
        stations.set(station.getType(), station);
        setStation();
    }

    //TODO Refactor
    private void setStation() {
        if (stations.get(0) != null) {
            mOrigin.setText(stations.get(0).toString());
            mOriginChip.setVisibility(View.VISIBLE);
            if (stations.get(0).isStation()) {
                mOriginChip.setStation(stations.get(0).getStation());
            } else {
                mOriginChip.setVisibility(View.GONE);
            }
            setPinColor(stations.get(0));
        } else {
            mOrigin.setText(getString(R.string.select_origin_station));
            mOriginChip.setVisibility(View.GONE);
        }

        if (stations.get(1) != null) {
            mDestination.setText(stations.get(1).toString());
            mDestinationChip.setVisibility(View.VISIBLE);
            if (stations.get(1).isStation()) {
                mDestinationChip.setStation(stations.get(1).getStation());
            } else {
                mDestinationChip.setVisibility(View.GONE);
            }
            setPinColor(stations.get(1));
        } else {
            mDestination.setText(getString(R.string.select_destination_station));
            mDestinationChip.setVisibility(View.GONE);
        }

        if (stations.get(0) != null && stations.get(1) != null) {
            if (!stations.get(0).equals(stations.get(1))) {
                mCalculate.setVisibility(View.VISIBLE);
            } else {
                mCalculate.setVisibility(View.GONE);
            }
        } else {
            mCalculate.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(), v.getTag().toString(), Toast.LENGTH_SHORT).show();
        setPinsTransparent();
        Station station = stationUtils.getStationFromKey(v.getTag().toString());
        showSelectDialog(station);
    }

    private void showSelectDialog(final Station station) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_station_select, null);
        builder.setTitle("Select As")
                .setView(dialogView)
                .create();
        final AlertDialog dialog = builder.show();

        dialogView.findViewById(R.id.origin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stations.set(0, new StationEvent(station, 0, true));
                setStation();
                dialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.destination).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stations.set(1, new StationEvent(station, 1, true));
                setStation();
                dialog.dismiss();
            }
        });
    }

    private void setPinsTransparent() {
        for (int i=1; i<map.getChildCount(); i++) {
            View v = map.getChildAt(i);
            if (v.getTag() != null) {
                ((GradientDrawable) v.getBackground()).setColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
            }
        }
    }

    private void setPinColor(StationEvent stationEvent) {
        for (int i=1; i<map.getChildCount(); i++) {
            View v = map.getChildAt(i);
            if (v.getTag() != null && v.getTag().toString().equals(stationEvent.getStation().getKey())) {
                if (stationEvent.getType() == 0) {
                    ((GradientDrawable) v.getBackground()).setColor(ContextCompat.getColor(getContext(), R.color.colorBts));
                } else {
                    ((GradientDrawable) v.getBackground()).setColor(ContextCompat.getColor(getContext(), R.color.colorMrt));
                }
            }
        }
    }
}
