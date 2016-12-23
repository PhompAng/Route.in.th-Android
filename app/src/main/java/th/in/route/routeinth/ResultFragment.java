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
        Log.wtf("result.object_route.get(position).name.th", result.object_route.get(0).name.th);
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

        routeAdapter = new RouteAdapter(result, getContext());
        linearLayoutManager = new LinearLayoutManager(getContext());
        routeRecycler.setHasFixedSize(true);
        routeRecycler.setLayoutManager(linearLayoutManager);
        routeRecycler.setAdapter(routeAdapter);

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

    private void getStationEachSystem(Result result){
        for (Route route: result.object_route){
            RouteItem routeItem = new RouteItem();
            if(routeItem)
        }
    }
}
