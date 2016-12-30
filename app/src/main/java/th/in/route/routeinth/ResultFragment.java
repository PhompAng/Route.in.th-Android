package th.in.route.routeinth;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import th.in.route.routeinth.adapter.RouteAdapter;
import th.in.route.routeinth.model.result.Result;
import th.in.route.routeinth.model.result.Route;
import th.in.route.routeinth.model.view.RouteItem;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ResultFragment.OnTest} interface
 * to handle interaction events.
 * Use the {@link ResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultFragment extends Fragment {
    private OnTest mListener;

    private Unbinder unbinder;
    @BindView(R.id.resultOrigin) TextView resultOrigin;
    @BindView(R.id.resultDestination) TextView resultDestination;
    @BindView(R.id.resultTripFareTotal) TextView resultTripFareTotal;
    @BindView(R.id.resultBTSFare) TextView resultBTSFare;
    @BindView(R.id.resultMRTFare) TextView resultMRTFare;
    @BindView(R.id.resultARLFare) TextView resultARLFare;
    @BindView(R.id.routeRecycler) RecyclerView routeRecycler;
    RouteAdapter routeAdapter;
    LinearLayoutManager linearLayoutManager;
    private Result result;
    private List<RouteItem> routeItems;
    private List<Boolean> isShow;
    private int flag = 0;

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

        routeItems = new ArrayList<>();
        isShow = new ArrayList<>();

        if (savedInstanceState != null) {
            isShow = toList(savedInstanceState.getBooleanArray("isShow"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_result, container, false);
        unbinder = ButterKnife.bind(this, v);

        resultOrigin.setText(this.result.origin.th);
        resultDestination.setText(this.result.destination.th);
        resultTripFareTotal.setText(String.format(Locale.getDefault(), "%d บาท", this.result.fare.total));
        if(this.result.fare.BTS != 0){
            resultBTSFare.setVisibility(View.VISIBLE);
            resultBTSFare.setText(String.format(Locale.getDefault(), "BTS    %d บาท", this.result.fare.BTS));
        }
        if(this.result.fare.MRT != 0){
            resultMRTFare.setVisibility(View.VISIBLE);
            resultMRTFare.setText(String.format(Locale.getDefault(), "MRT    %d บาท",  this.result.fare.MRT));
        }
        if(this.result.fare.ARL != 0){
            resultARLFare.setVisibility(View.VISIBLE);
            resultARLFare.setText(String.format(Locale.getDefault(), "ARL    %d บาท", this.result.fare.ARL));
        }

        routeAdapter = new RouteAdapter(routeItems, getContext(), ResultFragment.this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        routeRecycler.setHasFixedSize(true);
        routeRecycler.setLayoutManager(linearLayoutManager);
        routeRecycler.setAdapter(routeAdapter);

        getStation();

        return v;
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onTest();
        }
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setResult(Result result) {
        this.result = result;
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
        for (int i=0; i<routes.size(); i++){
            Log.wtf("route", routes.get(i).code);
            RouteItem routeItem = new RouteItem();
            routeItem.setStationOf(routes.get(i).code.charAt(0)+"");
            routeItem.setRoute(routes.get(i));
            routeItem.setSystem(system);
            if(routes.get(i).station_cnt == 0){
                if(i == 0){
                    routeItem.setType("ori_one");
                }else {
                    routeItem.setType("des_one");
                }
                if(flag == 0){
                    isShow.add(false);
                }
                system += 1;
                routeItems.add(routeItem);
            }else
            if(routes.get(i).code.charAt(0) != now || routes.get(i).station_cnt > 0) {
                Log.wtf("agggggggg", routes.get(i).code);
                if (i == 0) {
                    routeItem.setType("ori");
                }else if(routes.get(i).code.equals("BCEN") && result.BTS_same_line == 0){
                    system+=1;
                    routeItem.setType("siam");
                    routeItem.setSystem(system);
                } else {
                    routeItem.setType("start");
                }
                if(flag == 0){
                    isShow.add(false);
                }
                Log.wtf("isshow", isShow.toString());
                now = routes.get(i).code.charAt(0);
                routeItems.add(routeItem);
                if (!isShow.isEmpty() && !isShow.get(system)){
                    if(result.BTS_same_line == 0 && routes.get(i+1).code.equals("BCEN")){
                        RouteItem routeBetween = new RouteItem();
                        routeBetween.setRoute(routes.get(i));
                        routeBetween.setStationOf(routes.get(i).code.charAt(0)+"");
                        routeBetween.setType("between");
                        routeBetween.setSystem(system);
                        i+=routes.get(i).station_cnt-2;
                        routeItems.add(routeBetween);
                    }else {
                        Log.wtf("BCEN", "BCEN3");
                        RouteItem routeBetween = new RouteItem();
                        routeBetween.setRoute(routes.get(i));
                        routeBetween.setStationOf(routes.get(i).code.charAt(0)+"");
                        routeBetween.setType("between");
                        routeBetween.setSystem(system);
                        i+=routes.get(i).station_cnt-1;
                        routeItems.add(routeBetween);
                    }
                }
            }else if(i == routes.size()-1 || routes.get(i).code.charAt(0) != routes.get(i+1).code.charAt(0)){
                if(i == routes.size()-1){
                    routeItem.setType("des");
                }else{
                    routeItem.setType("end");
                }
                system += 1;
                routeItems.add(routeItem);
            }else if(routes.get(i).code.charAt(0) == now && isShow.get(system)){
                routeItem.setType("station");
                routeItems.add(routeItem);
            } else {
                RouteItem routeBetween = new RouteItem();
                routeBetween.setRoute(routes.get(i));
                routeBetween.setStationOf(routes.get(i).code.charAt(0)+"");
                routeBetween.setType("between");
                routeBetween.setSystem(system);
                i+=routes.get(i).station_cnt-1;
                routeItems.add(routeBetween);
                Log.wtf("subRoute", routeItem.getSystem()+"");
            }
//            Log.wtf(">>>>>", routes.get(i).code + routeItem.getType());
        }
        flag = 1;
        routeAdapter.notifyDataSetChanged();
        Log.wtf("subRoute", routeItems.toString());
    }

    public void setIsShow(int system, Boolean show){
        this.isShow.set(system, show);
        for (boolean a: isShow){
            Log.d("aaaaa", String.valueOf(a));
        }
        Log.d("flag", String.valueOf(flag));
        getStation();
    }

    public boolean getIsShow(int position){
        return isShow.get(position);
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putParcelableArrayList("isShow", isShow);
//    }
}




