package th.in.route.routeinth;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import th.in.route.routeinth.model.system.POJOSystem;
import th.in.route.routeinth.model.system.RailSystem;
import th.in.route.routeinth.model.system.RailSystemMapper;
import th.in.route.routeinth.services.APIServices;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StationSelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StationSelectFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public StationSelectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StationSelectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StationSelectFragment newInstance(String param1, String param2) {
        StationSelectFragment fragment = new StationSelectFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
        mStationAdapter = new StationAdapter(getContext(), new ArrayList<RailSystem>());
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

}
