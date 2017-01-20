package th.in.route.routeinth;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import th.in.route.routeinth.adapter.StationAdapter;
import th.in.route.routeinth.adapter.viewholder.StationViewHolder;
import th.in.route.routeinth.app.StationUtils;
import th.in.route.routeinth.model.StationEvent;
import th.in.route.routeinth.model.system.RailSystem;
import th.in.route.routeinth.model.system.Station;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StationSelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StationSelectFragment extends Fragment implements
        StationViewHolder.OnStationClickListener,
        FloatingSearchView.OnHomeActionClickListener,
        FloatingSearchView.OnQueryChangeListener,
        FloatingSearchView.OnMenuItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "type";
    private static final int PLACE_PICKER_REQUEST = 1;

    private int type;


    public StationSelectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param type Parameter 1.
     * @return A new instance of fragment StationSelectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StationSelectFragment newInstance(int type) {
        StationSelectFragment fragment = new StationSelectFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            type = getArguments().getInt(ARG_PARAM1);
        }

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    private Unbinder unbinder;
    private List<RailSystem> systems = new ArrayList<>();
    private StationAdapter mStationAdapter;
    @BindView(R.id.floating_search_view)
    FloatingSearchView mSearchView;
    @BindView(R.id.list)
    RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_station_select, container, false);
        unbinder = ButterKnife.bind(this, v);

        ((MainActivity) getActivity()).hideFab();
        ((MainActivity) getActivity()).tabVisibility(View.GONE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mStationAdapter = new StationAdapter(getContext(), new ArrayList<RailSystem>(), this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mStationAdapter);

        mSearchView.setOnHomeActionClickListener(this);
        mSearchView.setOnQueryChangeListener(this);
        mSearchView.setOnMenuItemClickListener(this);

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
        EventBus.getDefault().postSticky(new StationEvent(station, type, true));
        getFragmentManager().popBackStack();
    }

    @Override
    public void onHomeClicked() {
        getFragmentManager().popBackStack();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(getContext(), data);
//                String toastMsg = String.format("Place: %s", place.getName());
//                Toast.makeText(getContext(), toastMsg, Toast.LENGTH_LONG).show();
                EventBus.getDefault().postSticky(new StationEvent(place, type, false));
                getFragmentManager().popBackStack();
            }
        }
    }

    @Override
    public void onActionMenuItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_landmark) {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            try {
                startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }
    }
}
