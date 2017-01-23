package th.in.route.routeinth;


import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arlib.floatingsearchview.FloatingSearchView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import th.in.route.routeinth.adapter.StationAdapter;
import th.in.route.routeinth.adapter.viewholder.StationViewHolder;
import th.in.route.routeinth.app.DistanceUtils;
import th.in.route.routeinth.app.StationUtils;
import th.in.route.routeinth.model.system.RailSystem;
import th.in.route.routeinth.model.system.Station;


/**
 * A simple {@link Fragment} subclass.
 */
public class InformationFragment extends Fragment
        implements StationViewHolder.OnStationClickListener,
        FloatingSearchView.OnQueryChangeListener {

    public InformationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        ((MainActivity) getActivity()).hideFab();

        locationMap = DistanceUtils.getInstance().getLocationMap();
    }

    private Unbinder unbinder;
    private List<RailSystem> systems = new ArrayList<>();
    private Map<String, Location> locationMap;
    private StationAdapter mStationAdapter;
    @BindView(R.id.floating_search_view)
    FloatingSearchView mSearchView;
    @BindView(R.id.list)
    RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_information, container, false);
        unbinder = ButterKnife.bind(this, v);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mStationAdapter = new StationAdapter(getContext(), new ArrayList<RailSystem>(), this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mStationAdapter);

        mSearchView.setOnQueryChangeListener(this);
        systems.addAll(StationUtils.getInstance().getSystems());
        mStationAdapter.setParentList(systems, false);

        return v;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(int parentPosition, int childPosition) {
        Station station = mStationAdapter.getParentList().get(parentPosition).getChildList().get(childPosition);
        Intent intent = new Intent(getContext(), StationMapActivity.class);
        intent.putExtra("station", Parcels.wrap(station));
        intent.putExtra("location", locationMap.get(station.getKey()));
        startActivity(intent);
    }

    @Override
    public void onSearchTextChanged(String oldQuery, String newQuery) {
        if (newQuery.isEmpty()) {
            mStationAdapter.setParentList(systems, true);
            return;
        }
        List<RailSystem> newList = new ArrayList<>();
        for (RailSystem railSystem: systems) {
            List<Station> stations = new ArrayList<>();
            for (Station station: railSystem.getChildList()) {
                if (station.getEn().contains(newQuery)) {
                    stations.add(station);
                }
            }
            if (stations.size() > 0) {
                RailSystem system = new RailSystem(railSystem.getTitle(), stations);
                newList.add(system);
            }
        }
        mStationAdapter.setParentList(newList, true);
        mStationAdapter.expandAllParents();
    }
}
