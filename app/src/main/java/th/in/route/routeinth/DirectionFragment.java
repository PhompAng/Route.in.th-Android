package th.in.route.routeinth;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ezhome.rxfirebase2.database.RxFirebaseDatabase;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import th.in.route.routeinth.app.DatabaseUtils;
import th.in.route.routeinth.app.DistanceUtils;
import th.in.route.routeinth.app.StationUtils;
import th.in.route.routeinth.app.UIDUtils;
import th.in.route.routeinth.model.StationEvent;
import th.in.route.routeinth.model.User;
import th.in.route.routeinth.model.result.Input;
import th.in.route.routeinth.model.result.Result;
import th.in.route.routeinth.model.system.Station;
import th.in.route.routeinth.model.view.Card;
import th.in.route.routeinth.services.APIServices;
import th.in.route.routeinth.view.StationChip;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DirectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DirectionFragment extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, th.in.route.routeinth.view.RadioGroup.OnCheckedChangeListener {
    private OnCalculate mListener;
    
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<StationEvent> stations;
    private StationUtils stationUtils;
    private Map<String, Card> defaultCardMap;
    private Map<String, Card> cardMap;

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
        setHasOptionsMenu(true);
        showProgressDialog();
        stationUtils = StationUtils.getInstance();
        UIDUtils uidUtils = new UIDUtils(getContext());
        DatabaseReference reference = DatabaseUtils.getDatabase().getReference();
        RxFirebaseDatabase.getInstance().observeSingleValue(reference.child("users").child(uidUtils.getUID()))
                .subscribe(new Subscriber<DataSnapshot>() {
                    @Override
                    public void onCompleted() {
                        DirectionFragment.this.hideProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("DirectionFragment", e.getMessage());
                        DirectionFragment.this.hideProgressDialog();
                    }

                    @Override
                    public void onNext(DataSnapshot dataSnapshot) {
                        cardMap = dataSnapshot.getValue(User.class).getCardMap();
                        defaultCardMap = dataSnapshot.getValue(User.class).getCardMap();
                    }
                });
//        cardMap = UserUtils.getInstance().getUser().getCardMap();
        stations = new ArrayList<>();
        stations.add(null);
        stations.add(null);
        ((MainActivity) getActivity()).tabVisibility(View.GONE);
    }

    private Unbinder unbinder;
    private ProgressDialog progressDialog;

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

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((MainActivity) getActivity()).hideFab();
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

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Loading User Data");
            progressDialog.setIndeterminate(true);
        }
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.direction_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
//                showFareSetting();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showFareSetting() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.bottom_card_setting, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(v);

        final RadioGroup btsGroup = (RadioGroup) v.findViewById(R.id.bts_group);
        final th.in.route.routeinth.view.RadioGroup mrtGroup = (th.in.route.routeinth.view.RadioGroup) v.findViewById(R.id.mrt_group);
        final RadioGroup arlGroup = (RadioGroup) v.findViewById(R.id.arl_group);
        btsGroup.setOnCheckedChangeListener(this);
        mrtGroup.setOnCheckedChangeListener(this);
        arlGroup.setOnCheckedChangeListener(this);

        setCardCheck(btsGroup, arlGroup, mrtGroup, cardMap);

        v.findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCardCheck(btsGroup, arlGroup, mrtGroup, defaultCardMap);
            }
        });

        bottomSheetDialog.show();
    }

    private void setCardCheck(RadioGroup btsGroup, RadioGroup arlGroup, th.in.route.routeinth.view.RadioGroup mrtGroup, Map<String, Card> map) {
        ((RadioButton) btsGroup.findViewWithTag(Integer.toString(getOrDefault(map, "BTS").getIntType()))).setChecked(true);
        ((RadioButton) mrtGroup.findViewWithTag(Integer.toString(getOrDefault(map, "MRT").getIntType()))).setChecked(true);
        ((RadioButton) arlGroup.findViewWithTag(Integer.toString(getOrDefault(map, "ARL").getIntType()))).setChecked(true);
    }

    private Card getOrDefault(Map<String, Card> map, String key) {
        if (map.containsKey(key)) {
            return map.get(key);
        } else {
            return new Card(key, 0);
        }
    }

    private void setStation() {
        for (int i=0;i<2;i++) {
            TextView textView;
            StationChip chip;
            int s;
            if (i == 0) {
                textView = mOrigin;
                chip = mOriginChip;
                s = R.string.select_origin_station;
            } else {
                textView = mDestination;
                chip = mDestinationChip;
                s = R.string.select_destination_station;
            }
            if (stations.get(i) != null) {
                textView.setText(stations.get(i).toString());
                chip.setVisibility(View.VISIBLE);
                if (stations.get(i).isStation()) {
                    chip.setStation(stations.get(i).getStation());
                } else {
                    chip.setVisibility(View.GONE);
                }
                setPinColor(stations.get(i));
            } else {
                textView.setText(getString(s));
                chip.setVisibility(View.GONE);
            }
        }

        setCalculate();
    }

    @OnClick(R.id.calculate)
    void onCalculateClicked(View v) {
        String departKey = stations.get(0).getStation().getKey();
        String arriveKey = stations.get(1).getStation().getKey();
        final Input input = new Input();
        input.origin = departKey;
        input.destination = arriveKey;
        input.card_type_bts = "0";
        input.card_type_mrt = "0";
        input.card_type_arl = "0";
        if (cardMap != null) {
            for (Map.Entry<String, Card> entry: cardMap.entrySet()) {
                switch (entry.getKey()) {
                    case "BTS":
                        input.card_type_bts = Integer.toString(entry.getValue().getIntType());
                        break;
                    case "MRT":
                        input.card_type_mrt = Integer.toString(entry.getValue().getIntType());
                        break;
                    case "ARL":
                        input.card_type_arl = Integer.toString(entry.getValue().getIntType());
                        break;
                }
            }
        }
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://103.253.134.235:8888/")
                .build();
        APIServices apiServices = retrofit.create(APIServices.class);
        apiServices.calculate(input)
        .subscribeOn(Schedulers.newThread())
        .doOnNext(new Action1<Result>() {
            @Override
            public void call(Result result) {
                calculate(result);
            }
        }).doOnError(new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e("calculate", throwable.getMessage());
                Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        .subscribe();
    }

    private void calculate(Result result) {
        if (mListener != null) {
            mListener.OnCalculateBtnPressed(result, stations);
        }
    }

    private void setCalculate() {
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCalculate) {
            mListener = (OnCalculate) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTest");
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideProgressDialog();
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        String system = (String) group.getTag();
        int type = Integer.parseInt((String) group.findViewById(checkedId).getTag());
        cardMap.put(system, new Card(system, type));
    }

    @Override
    public void onCheckedChanged(th.in.route.routeinth.view.RadioGroup group, @IdRes int checkedId) {
        String system = (String) group.getTag();
        int type = Integer.parseInt((String) group.findViewById(checkedId).getTag());
        cardMap.put(system, new Card(system, type));
    }

    public interface OnCalculate {
        void OnCalculateBtnPressed(Result result, List<StationEvent> stationEvents);
    }
}
