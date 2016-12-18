package th.in.route.routeinth.layout;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import th.in.route.routeinth.*;
import th.in.route.routeinth.Result;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnCalculateBtnPressedListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    private OnCalculateBtnPressedListener mListener;
    private Unbinder unbinder;
    @BindView(R.id.departSystem) Spinner departSystemSpinner;
    @BindView(R.id.departStation) Spinner departStationSpinner;
    @BindView(R.id.arriveSystem) Spinner arriveSystemSpinner;
    @BindView(R.id.arriveStation) Spinner arriveStationSpinner;
    @BindView(R.id.calculateButton) Button calculateButton;

    private List<POJOSystem> systems;
    private ArrayAdapter<Detail> departStationSpinnerAdapter;
    private List<Detail> departStationList;
    private ArrayAdapter<Detail> arriveStationSpinnerAdapter;
    private List<Detail> arriveStationList;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainFragment.
     */
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
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
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, v);

        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("http://103.253.134.235:8888/").build();

        APIServices apiServices = retrofit.create(APIServices.class);
        Call<List<POJOSystem>> call = apiServices.getSystem();
        call.enqueue(new Callback<List<POJOSystem>>() {
            @Override
            public void onResponse(Call<List<POJOSystem>> call, Response<List<POJOSystem>> response) {
                systems = response.body();

                ArrayAdapter<POJOSystem> systemSpinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, systems);
                departSystemSpinner.setAdapter(systemSpinnerAdapter);
                arriveSystemSpinner.setAdapter(systemSpinnerAdapter);

                departStationList = new ArrayList<>(systems.get(0).option.values());
                arriveStationList = new ArrayList<>(systems.get(0).option.values());

                departStationSpinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, departStationList);
                arriveStationSpinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, arriveStationList);
                departStationSpinner.setAdapter(departStationSpinnerAdapter);
                arriveStationSpinner.setAdapter(arriveStationSpinnerAdapter);
            }

            @Override
            public void onFailure(Call<List<POJOSystem>> call, Throwable t) {
                Log.v("Fail", t.getMessage());
            }
        });
        return v;
    }

    @OnItemSelected(R.id.departSystem)
    void onDepartItemSelected(int position) {
        departStationList.clear();
        departStationList.addAll(systems.get(position).option.values());
        departStationSpinnerAdapter.notifyDataSetChanged();
    }

    @OnItemSelected(R.id.arriveSystem)
    void onArriveItemSelected(int position) {
        arriveStationList.clear();
        arriveStationList.addAll(systems.get(position).option.values());
        arriveStationSpinnerAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.calculateButton)
    void onCalculateClicked(View v) {
        String departKey = ((Detail) departStationSpinner.getSelectedItem()).key;
        String arriveKey = ((Detail) arriveStationSpinner.getSelectedItem()).key;
        Input input = new Input();
        input.origin = departKey;
        input.destination = arriveKey;
        input.card_type_bts = "0";
        input.card_type_mrt = "0";
        input.card_type_arl = "0";

        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("http://103.253.134.235:8888/").build();

        APIServices apiServices = retrofit.create(APIServices.class);
        Call<Result> resultCall = apiServices.calculate(input);

        resultCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Result result = response.body();
                calculate(result);
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });

    }


    public void calculate(Result result) {
        if (mListener != null) {
            mListener.OnCalculateBtnPressed(result);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCalculateBtnPressedListener) {
            mListener = (OnCalculateBtnPressedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
    public interface OnCalculateBtnPressedListener {
        void OnCalculateBtnPressed(Result result);
    }
}
