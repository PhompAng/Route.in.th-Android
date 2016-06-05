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
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import th.in.route.routeinth.APIServices;
import th.in.route.routeinth.Detail;
import th.in.route.routeinth.POJOSystem;
import th.in.route.routeinth.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Main.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Main#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Main extends Fragment {
    private OnFragmentInteractionListener mListener;
    private Unbinder unbinder;
    @BindView(R.id.departSystem) Spinner departSystemSpinner;
    @BindView(R.id.departStation) Spinner departStationSpinner;

    private List<POJOSystem> systems;

    public Main() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Main.
     */
    public static Main newInstance() {
        Main fragment = new Main();
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

        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("http://route.in.th/api/public/").build();

        APIServices apiServices = retrofit.create(APIServices.class);
        Call<List<POJOSystem>> call = apiServices.getSystem();
        call.enqueue(new Callback<List<POJOSystem>>() {
            @Override
            public void onResponse(Call<List<POJOSystem>> call, Response<List<POJOSystem>> response) {
                systems = response.body();
                ArrayList<String> systemList = new ArrayList<>();
                for (POJOSystem s: systems) {
                    systemList.add(s.name);
                }

                ArrayAdapter<String> systemSpinnerAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, systemList);
                departSystemSpinner.setAdapter(systemSpinnerAdapter);

                ArrayList<String> stationList = new ArrayList<>();
                for (Detail detail: systems.get(0).option.values()) {
                    stationList.add(detail.th);
                }

                ArrayAdapter<String> stationSpinnerAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, stationList);
                departStationSpinner.setAdapter(stationSpinnerAdapter);
            }

            @Override
            public void onFailure(Call<List<POJOSystem>> call, Throwable t) {
                Log.v("Fail", t.getMessage());
            }
        });
        return v;
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction();
    }
}
