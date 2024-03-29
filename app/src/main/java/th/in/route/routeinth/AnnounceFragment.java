package th.in.route.routeinth;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import th.in.route.routeinth.adapter.TweetPagerAdapter;
import th.in.route.routeinth.view.WrapContentViewPager;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link AnnounceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnnounceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Unbinder unbinder;
    @BindView(R.id.tweetPager)
    WrapContentViewPager tweetViewPager;
    private TweetPagerAdapter tweetPagerAdapter;
    public AnnounceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AnnounceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnnounceFragment newInstance(String param1, String param2) {
        AnnounceFragment fragment = new AnnounceFragment();
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
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity) getActivity()).hideFab();
        ((MainActivity) getActivity()).setFill(true);
        ((MainActivity) getActivity()).tabVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_announce, container, false);
        unbinder = ButterKnife.bind(this, v);
        tweetPagerAdapter = new TweetPagerAdapter(getChildFragmentManager());
        tweetViewPager.setAdapter(tweetPagerAdapter);

        ((MainActivity) getActivity()).setViewPager(tweetViewPager);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) getActivity()).setFill(false);
    }
}
