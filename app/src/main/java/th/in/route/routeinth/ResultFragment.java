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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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
    private ArrayList<RouteItem> routeItems;
    private ArrayList<Boolean> isShow;
    private ArrayList<ArrayList<RouteItem>> subRoutes;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_result, container, false);
        unbinder = ButterKnife.bind(this, v);

        resultOrigin.setText(this.result.origin.th);
        resultDestination.setText(this.result.destination.th);
        resultTripFareTotal.setText(String.format("%d บาท", this.result.fare.total));
        if(this.result.fare.BTS != 0){
            resultBTSFare.setVisibility(View.VISIBLE);
            resultBTSFare.setText(String.format("BTS    %d บาท", this.result.fare.BTS));
        }
        if(this.result.fare.MRT != 0){
            resultMRTFare.setVisibility(View.VISIBLE);
            resultMRTFare.setText(String.format("MRT    %d บาท",  this.result.fare.MRT));
        }
        if(this.result.fare.ARL != 0){
            resultARLFare.setVisibility(View.VISIBLE);
            resultARLFare.setText(String.format("ARL    %d บาท", this.result.fare.ARL));
        }

        routeItems = new ArrayList<>();
        isShow = new ArrayList<>();

        routeAdapter = new RouteAdapter(routeItems, getContext(), ResultFragment.this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        routeRecycler.setHasFixedSize(true);
        routeRecycler.setLayoutManager(linearLayoutManager);
        routeRecycler.setAdapter(routeAdapter);

//        getStationEachSystem(result);
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

//    private void getStationEachSystem(Result result){
//        List<Route> routes = result.object_route;
//        routeItems.clear();
//        for (int i=0; i < result.object_route.size(); i++){
//            RouteItem routeItem = new RouteItem();
//            if(routes.get(i).station_cnt != 0){
//                routeItem.setStationOf(routes.get(i).code.charAt(0)+"");
//                routeItem.setType("start");
//                Log.d("start", routeItem.getType() + routeItem.getStationOf());
//                routeItem.setRoute(routes.get(i));
//                routeItems.add(routeItem);
//
//                RouteItem routeBetween = new RouteItem();
//                routeBetween.setType("between");
//                routeBetween.setStationOf(routes.get(i).code.charAt(0)+"");
//                routeBetween.setRoute(routes.get(i));
//                routeItems.add(routeBetween);
//                i+=routes.get(i).station_cnt-1;
//            }
//// else if(routes.get(i).station_cnt == 0){
////                routeItem.setType("one");
////                routeItem.setStationOf(routes.get(i).code.charAt(0)+"");
////                routeItem.setRoute(routes.get(i));
////                routeItems.add(routeItem);
////                i+=1;
////            }
//                else{
//                Log.d("aaaaaaaaa", routes.get(i).name.th);
//                routeItem.setType("end");
//                routeItem.setStationOf(routes.get(i).code.charAt(0)+"");
//                routeItem.setRoute(routes.get(i));
//                routeItems.add(routeItem);
//            }
//        }
//        routeAdapter.notifyDataSetChanged();
//
//    }

    public void getStation(){
        List<Route> routes = result.object_route;
        subRoutes = new ArrayList<>();
        routeItems.clear();
        Character now = 'N';
        Character code = 'C';
        int system = 0;
        for (int i=0; i<routes.size(); i++){
            RouteItem routeItem = new RouteItem();
            routeItem.setStationOf(routes.get(i).code.charAt(0)+"");
            routeItem.setRoute(routes.get(i));
            routeItem.setSystem(system);
            if(routes.get(i).code.charAt(0) != now) {
                if (i == 0) {
                    routeItem.setType("ori");
                } else {
                    routeItem.setType("start");
                }
                if(flag == 0){
                    isShow.add(false);
                }
                now = routes.get(i).code.charAt(0);
                routeItems.add(routeItem);
                if (isShow.get(system) == false){
                    RouteItem routeBetween = new RouteItem();
                    routeBetween.setRoute(routes.get(i));
                    routeBetween.setStationOf(routes.get(i).code.charAt(0)+"");
                    routeBetween.setType("between");
                    routeBetween.setSystem(system);
                    i+=routes.get(i).station_cnt-1;
                    routeItems.add(routeBetween);
                }
            }else if(i == routes.size()-1 || routes.get(i).code.charAt(0) != routes.get(i+1).code.charAt(0)){
                if(i == routes.size()-1){
                    routeItem.setType("des");
                }else{
                    routeItem.setType("end");
                }
                system += 1;
                routeItems.add(routeItem);
                subRoutes.add(routeItems);
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
        }
        flag = 1;
        routeAdapter.notifyDataSetChanged();
        Log.wtf("subRoute", routeItems.toString());
    }

//    public void getAllStationOfEachSystem(boolean arlFlag, boolean btsFlag, boolean mrtFlag){
//        List<Route> routes = result.object_route;
//        Character system = 'N';
//        routeItems.clear();
//        for (int i=0; i < result.object_route.size(); i++){
//            RouteItem routeItem = new RouteItem();
//            if(routes.get(i).station_cnt != 0){
//                routeItem.setStationOf(routes.get(i).code.charAt(0)+"");
//                routeItem.setType("start");
//                Log.d("start", routeItem.getType() + routeItem.getStationOf());
//                routeItem.setRoute(routes.get(i));
//                routeItems.add(routeItem);
//                system = routeItem.getStationOf().charAt(0);
//                if((routeItem.getStationOf().equals("A") && arlFlag == false) ||
//                        (routeItem.getStationOf().equals("B") && btsFlag == false) ||
//                        (routeItem.getStationOf().equals("M") && mrtFlag == false)){
//                    RouteItem routeBetween = new RouteItem();
//                    routeBetween.setType("between");
//                    routeBetween.setSystem(system);
//                    routeBetween.setStationOf(routes.get(i).code.charAt(0)+"");
//                    routeBetween.setRoute(routes.get(i));
//                    routeItems.add(routeBetween);
//                    i+=routes.get(i).station_cnt-1;
//                }
//                i+=1;
//            }else if((routes.get(i+1).code.charAt(0) != system) && (i < result.object_route.size()-1)){
//                routeItem.setType("end");
//                routeItem.setStationOf(routes.get(i).code.charAt(0)+"");
//                routeItem.setRoute(routes.get(i));
//                routeItems.add(routeItem);
//            }
//// else if(routes.get(i).station_cnt == 0){
////                routeItem.setType("one");
////                routeItem.setStationOf(routes.get(i).code.charAt(0)+"");
////                routeItem.setRoute(routes.get(i));
////                routeItems.add(routeItem);
////                i+=1;
////            }
//            else{
//                Log.d("aaaaaaaaa", routes.get(i).name.th);
//                routeItem.setType("station");
//                routeItem.setStationOf(routes.get(i).code.charAt(0)+"");
//                routeItem.setRoute(routes.get(i));
//                routeItems.add(routeItem);
//                i+=1;
//            }
//        }
//        routeAdapter.notifyDataSetChanged();
//
//    }

    public void setIsShow(int system, Boolean show){
        this.isShow.set(system, show);
        for (boolean a: isShow){
            Log.d("aaaaa", String.valueOf(a));
        }
        Log.d("flag", String.valueOf(flag));
        getStation();
    }

    public  boolean getIsShow(int position){
        return isShow.get(position);
    }

}


