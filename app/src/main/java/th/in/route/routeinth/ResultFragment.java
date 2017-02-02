package th.in.route.routeinth;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ezhome.rxfirebase2.database.RxFirebaseDatabase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Subscriber;
import th.in.route.routeinth.adapter.RouteAdapter;
import th.in.route.routeinth.app.DatabaseUtils;
import th.in.route.routeinth.app.DistanceUtils;
import th.in.route.routeinth.app.FacilitiesUtils;
import th.in.route.routeinth.app.FirebaseUtils;
import th.in.route.routeinth.app.UIDUtils;
import th.in.route.routeinth.model.StationEvent;
import th.in.route.routeinth.model.User;
import th.in.route.routeinth.model.result.Result;
import th.in.route.routeinth.model.result.Route;
import th.in.route.routeinth.model.view.Card;
import th.in.route.routeinth.model.view.RouteItem;
import th.in.route.routeinth.services.BackgroundLocationService;
import th.in.route.routeinth.services.LocationReceiver;
import th.in.route.routeinth.view.StationChip;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ResultFragment.OnTest} interface
 * to handle interaction events.
 * Use the {@link ResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final int REQUEST_CODE_ASK_PERMISSIONS = 8000;

    private OnTest mListener;

    private Unbinder unbinder;
    private ProgressDialog progressDialog;
//    @BindView(R.id.resultOrigin) TextView resultOrigin;
//    @BindView(R.id.resultDestination) TextView resultDestination;
    @BindView(R.id.origin)
    TextView mOrigin;
    @BindView(R.id.destination)
    TextView mDestination;
    @BindView(R.id.origin_station_chip)
    StationChip mOriginChip;
    @BindView(R.id.destination_station_chip)
    StationChip mDestinationChip;
    @BindView(R.id.resultTripFareTotal) TextView resultTripFareTotal;
    @BindView(R.id.bts_station_cnt) TextView btsStationCnt;
    @BindView(R.id.resultBTSFare) TextView resultBTSFare;
    @BindView(R.id.mrt_station_cnt) TextView mrtStationCnt;
    @BindView(R.id.resultMRTFare) TextView resultMRTFare;
    @BindView(R.id.arl_station_cnt) TextView arlStationCnt;
    @BindView(R.id.resultARLFare) TextView resultARLFare;
    @BindView(R.id.routeRecycler) RecyclerView routeRecycler;
    @BindView(R.id.calculate) Button navigate;
    @BindView(R.id.pay) Button pay;

    RouteAdapter routeAdapter;
    LinearLayoutManager linearLayoutManager;
    private Result result;
    private List<StationEvent> stations;
    private List<RouteItem> routeItems;
    private List<Boolean> isShow;
    private Map<String, Integer> stationCnt;
    private Map<String, Card> cardMap;
    private int flag = 0;
    private String lang;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private NotificationManager mNotificationManager;

    private boolean navigating = false;

    public ResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ResultFragment.
     */
    public static ResultFragment newInstance() {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        setRetainInstance(true);
        setHasOptionsMenu(true);

        setLocationRequest();

        showProgressDialog();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mNotificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        routeItems = new ArrayList<>();
        isShow = new ArrayList<>();

        stationCnt = new HashMap<>();
        stationCnt.put("bts", 0);
        stationCnt.put("mrt", 0);
        stationCnt.put("arl", 0);

        calculateStationCnt();

        if (savedInstanceState != null) {
            isShow = toList(savedInstanceState.getBooleanArray("isShow"));
        }

        UIDUtils uidUtils = new UIDUtils(getContext());
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        lang = preferences.getString("preference_lang", "en");
        DatabaseReference reference = DatabaseUtils.getDatabase().getReference();
        RxFirebaseDatabase.getInstance().observeSingleValue(reference.child("users").child(uidUtils.getUID()))
                .subscribe(new Subscriber<DataSnapshot>() {
                    @Override
                    public void onCompleted() {
                        ResultFragment.this.hideProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("ResultFragment", e.getMessage());
                        ResultFragment.this.hideProgressDialog();
                    }

                    @Override
                    public void onNext(DataSnapshot dataSnapshot) {
                        cardMap = dataSnapshot.getValue(User.class).getCardMap();
                        setPay();
                    }
                });
    }

    private void calculateStationCnt() {
        for (String station: result.route) {
            if (station.charAt(0) == 'A') {
                stationCnt.put("arl", stationCnt.get("arl")+1);
            } else if (station.charAt(0) == 'B') {
                stationCnt.put("bts", stationCnt.get("bts")+1);
            } else {
                stationCnt.put("mrt", stationCnt.get("mrt")+1);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_result2, container, false);
        unbinder = ButterKnife.bind(this, v);

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).hideFab();
        ((MainActivity) getActivity()).tabVisibility(View.GONE);

        v.findViewById(R.id.swap).setVisibility(View.INVISIBLE);
        navigate.setVisibility(View.VISIBLE);
        navigate.setText(getString(R.string.navigate));
        setStation();
        resultTripFareTotal.setText(String.format(Locale.getDefault(), "%d", this.result.fare.total));
        if(this.result.fare.BTS != 0){
            btsStationCnt.setVisibility(View.VISIBLE);
            resultBTSFare.setVisibility(View.VISIBLE);
//            btsStationCnt.setText(String.format(Locale.getDefault(), "BTS %d "+ getResources().getString(R.string.stations) +" (%s)", this.stationCnt.get("bts"), this.result.card_type_bts.en));
            btsStationCnt.setText(getResources().getQuantityString(R.plurals.station_count, this.stationCnt.get("bts"), "BTS", this.stationCnt.get("bts"), this.result.card_type_bts.en));
            resultBTSFare.setText(String.format(Locale.getDefault(), getString(R.string.n_baht), this.result.fare.BTS));
        }
        if(this.result.fare.MRT != 0){
            mrtStationCnt.setVisibility(View.VISIBLE);
            resultMRTFare.setVisibility(View.VISIBLE);
            mrtStationCnt.setText(getResources().getQuantityString(R.plurals.station_count, this.stationCnt.get("mrt"), "MRT", this.stationCnt.get("mrt"), this.result.card_type_mrt.en));
            resultMRTFare.setText(String.format(Locale.getDefault(), getString(R.string.n_baht),  this.result.fare.MRT));
        }
        if(this.result.fare.ARL != 0){
            arlStationCnt.setVisibility(View.VISIBLE);
            resultARLFare.setVisibility(View.VISIBLE);
            arlStationCnt.setText(getResources().getQuantityString(R.plurals.station_count, this.stationCnt.get("arl"), "ARL", this.stationCnt.get("arl"), this.result.card_type_arl.en));
            resultARLFare.setText(String.format(Locale.getDefault(), getString(R.string.n_baht), this.result.fare.ARL));
        }

        routeAdapter = new RouteAdapter(routeItems, getContext(), FacilitiesUtils.getInstance().getFacilitiesMap(), ResultFragment.this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        routeRecycler.setHasFixedSize(true);
        routeRecycler.setLayoutManager(linearLayoutManager);
        routeRecycler.setAdapter(routeAdapter);

        getStation();

        return v;
    }

    private void setPay() {
        boolean isShow = false;
        for (Map.Entry<String, Card> entry: cardMap.entrySet()) {
            switch (entry.getKey()) {
                case "BTS":
                    if (entry.getValue().getType().equals(result.card_type_bts.en)) {
                        isShow = true;
                    }
                    break;
                case "MRT":
                    if (entry.getValue().getType().equals(result.card_type_mrt.en)) {
                        isShow = true;
                    }
                    break;
                case "ARL":
                    if (entry.getValue().getType().equals(result.card_type_arl.en)) {
                        isShow = true;
                    }
                    break;
            }
        }

        if (result.fare.total == 0) {
            isShow = false;
        }

        if (!isShow) {
            pay.setVisibility(View.GONE);
        }
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onTest();
        }
    }

    @OnClick(R.id.calculate)
    public void navigate() {
        navigate.setText(navigating ? R.string.navigate:R.string.stop_navigation);
        navigating = !navigating;
        Intent intent = new Intent(getActivity(), BackgroundLocationService.class);
        if (navigating) {
            intent.putStringArrayListExtra("route", new ArrayList<>(result.route));
            intent.putExtra("bts_same_line", result.BTS_same_line);
            getActivity().startService(intent);
        } else {
            getActivity().stopService(intent);
            mNotificationManager.cancel(LocationReceiver.notifyID);
        }
        routeAdapter.setNavigate(navigating);
        routeAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.pay)
    public void pay() {
        int btsFare = result.fare.BTS;
        int mrtFare = result.fare.MRT;
        int arlFare = result.fare.ARL;

        UIDUtils uidUtils = new UIDUtils(getContext());

        FirebaseUtils.pay(uidUtils.getUID(), btsFare, mrtFare, arlFare);

        Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
        pay.setText(R.string.paid);
        pay.setEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStack();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTest) {
            mListener = (OnTest) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTest");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBooleanArray("isShow", toPrimitiveArray(isShow));
    }

    private boolean[] toPrimitiveArray(final List<Boolean> booleanList) {
        final boolean[] primitives = new boolean[booleanList.size()];
        int index = 0;
        for (Boolean object : booleanList) {
            primitives[index++] = object;
        }
        return primitives;
    }

    private List<Boolean> toList(final boolean[] booleanArray) {
        List<Boolean> booleanList = new ArrayList<>();
        for (boolean b: booleanArray) {
            booleanList.add(b);
        }
        return booleanList;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    onConnected(null);
                } else {
                    Toast.makeText(getContext(), "Need Location Permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public void setStations(List<StationEvent> stations) {
        this.stations = stations;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnTest {
        void onTest();
    }

    public void getStation(){
        List<Route> routes = result.object_route;
        routeItems.clear();
        Character now = 'N';
        int system = 0;
        for (int i=0; i<routes.size(); i++) {
            RouteItem routeItem = new RouteItem();
            routeItem.setStationOf(routes.get(i).code.charAt(0)+"");
            routeItem.setRoute(routes.get(i));
            routeItem.setSystem(system);
            if (routes.get(i).station_cnt == 0) {
                if (i == 0) {
                    routeItem.setType("ori_one");
                } else {
                    routeItem.setType("des_one");
                }
                if(flag == 0){
                    isShow.add(false);
                }
                system += 1;
                routeItems.add(routeItem);
            } else if (routes.get(i).code.charAt(0) != now || routes.get(i).station_cnt > 0) {
                if (i == 0) {
                    routeItem.setType("ori");
                } else if(routes.get(i).code.equals("BCEN") && result.BTS_same_line == 0) {
                    system+=1;
                    routeItem.setType("siam");
                    routeItem.setSystem(system);
                } else {
                    routeItem.setType("start");
                }
                if (flag == 0) {
                    isShow.add(false);
                }
                now = routes.get(i).code.charAt(0);
                routeItems.add(routeItem);
                if (!isShow.isEmpty() && !isShow.get(system)) {
                    if (result.BTS_same_line == 0 && routes.get(i+1).code.equals("BCEN") && routes.get(i).station_cnt > 1) {
                        RouteItem routeBetween = new RouteItem();
                        routeBetween.setStationOf(routes.get(i).code.charAt(0)+"");
                        routeBetween.setType("between");
                        routeBetween.setSystem(system);
                        ArrayList<Route> routeInIt = new ArrayList<>();
                        for (int j = i+1; j<=i+routes.get(i).station_cnt-2; j++){
                            routeInIt.add(routes.get(j));
                        }
                        routeBetween.setRoutes(routeInIt);
                        i+=routes.get(i).station_cnt-2;
                        routeItems.add(routeBetween);
                    } else if(routes.get(i).station_cnt > 1) {
                        RouteItem routeBetween = new RouteItem();
                        routeBetween.setRoute(routes.get(i));
                        routeBetween.setStationOf(routes.get(i).code.charAt(0)+"");
                        routeBetween.setType("between");
                        routeBetween.setSystem(system);
                        ArrayList<Route> routeInIt = new ArrayList<>();
                        for (int j = i+1; j<=i+routes.get(i).station_cnt-1; j++){
                            routeInIt.add(routes.get(j));
                        }
                        routeBetween.setRoutes(routeInIt);
                        i+=routes.get(i).station_cnt-1;
                        routeItems.add(routeBetween);
                    }
                }
            } else if(i == routes.size()-1 || routes.get(i).code.charAt(0) != routes.get(i+1).code.charAt(0)) {
                if (i == routes.size()-1) {
                    routeItem.setType("des");
                } else {
                    routeItem.setType("end");
                }
                system += 1;
                routeItems.add(routeItem);
            } else if(routes.get(i).code.charAt(0) == now && isShow.get(system)) {
                routeItem.setType("station");
                routeItems.add(routeItem);
            } else if(routes.get(i).station_cnt > 1) {
                RouteItem routeBetween = new RouteItem();
                routeBetween.setStationOf(routes.get(i).code.charAt(0)+"");
                routeBetween.setType("between");
                routeBetween.setSystem(system);
                ArrayList<Route> routeInIt = new ArrayList<>();
                for (int j = i+1; j<=i+routes.get(i).station_cnt-1; j++){
                    routeInIt.add(routes.get(j));
                }
                routeBetween.setRoutes(routeInIt);
                i+=routes.get(i).station_cnt-1;
                routeItems.add(routeBetween);
            }
        }
        flag = 1;
        routeAdapter.notifyDataSetChanged();
    }

    public void setIsShow(int system, Boolean show){
        this.isShow.set(system, show);
        getStation();
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
                textView.setText(stations.get(i).toString(lang));
                chip.setVisibility(View.VISIBLE);
                if (stations.get(i).isStation()) {
                    chip.setStation(stations.get(i).getStation());
                } else {
                    chip.setVisibility(View.GONE);
                }
            } else {
                textView.setText(getString(s));
                chip.setVisibility(View.GONE);
            }
        }
    }

    public boolean getIsShow(int position){
        return isShow.get(position);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        setLocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            handleNewLocation(mLastLocation);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.w("onConnectionSuspended", i + "");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("onConnectionFailed", connectionResult.getErrorMessage());
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        handleNewLocation(location);
    }

    private void handleNewLocation(Location location) {
        String nearestKey = DistanceUtils.getInstance().getNearestStation(location.getLatitude(), location.getLongitude());
        routeAdapter.setNearestKey(nearestKey);
        routeAdapter.notifyDataSetChanged();
    }

    private void setLocationRequest() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
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
}




