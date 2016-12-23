package th.in.route.routeinth;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import th.in.route.routeinth.adapter.StationAdapter;
import th.in.route.routeinth.adapter.viewholder.StationViewHolder;
import th.in.route.routeinth.model.StationEvent;
import th.in.route.routeinth.model.system.POJOSystem;
import th.in.route.routeinth.model.system.RailSystem;
import th.in.route.routeinth.model.system.RailSystemMapper;
import th.in.route.routeinth.model.system.Station;
import th.in.route.routeinth.services.APIServices;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StationSelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StationSelectFragment extends Fragment implements StationViewHolder.OnStationClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "type";

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
    }

    private Unbinder unbinder;
    private StationAdapter mStationAdapter;
    @BindView(R.id.list)
    RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_station_select, container, false);
        unbinder = ButterKnife.bind(this, v);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mStationAdapter = new StationAdapter(getContext(), new ArrayList<RailSystem>(), this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mStationAdapter);

        retrieveStations();

        return v;
    }

    private void retrieveStations() {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://103.253.134.235:8888/").build();

        APIServices apiServices = retrofit.create(APIServices.class);
        Observable<List<POJOSystem>> observable = apiServices.getSystem();
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new RailSystemMapper())
                .doOnNext(new Action1<List<RailSystem>>() {
                    @Override
                    public void call(List<RailSystem> railSystems) {
                        mStationAdapter.setParentList(railSystems, false);
                    }
                })
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .subscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(int parentPosition, int childPosition) {
        Station station = mStationAdapter.getParentList().get(parentPosition).getChildList().get(childPosition);
        EventBus.getDefault().postSticky(new StationEvent(station, type));
        getFragmentManager().popBackStack();
    }
}
