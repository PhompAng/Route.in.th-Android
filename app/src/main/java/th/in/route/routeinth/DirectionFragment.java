package th.in.route.routeinth;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import th.in.route.routeinth.model.StationEvent;
import th.in.route.routeinth.view.StationChip;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DirectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DirectionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<StationEvent> stations;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_direction, container, false);
        unbinder = ButterKnife.bind(this, v);
        setStation();
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

    @OnClick(R.id.test)
    public void test() {
        Toast.makeText(getContext(), "yeah", Toast.LENGTH_SHORT).show();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void retrieveStation(StationEvent station) {
        if (!station.isStation()) {
            DistanceUtils distanceUtils = DistanceUtils.getInstancec();
            LatLng latLng = station.getPlace().getLatLng();
            Toast.makeText(getContext(), distanceUtils.getNearestStation(latLng.latitude, latLng.longitude), Toast.LENGTH_SHORT).show();
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
        } else {
            mDestination.setText(getString(R.string.select_destination_station));
            mDestinationChip.setVisibility(View.GONE);
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

}
