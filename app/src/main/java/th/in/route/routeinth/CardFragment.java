package th.in.route.routeinth;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import th.in.route.routeinth.adapter.CardAdapter;
import th.in.route.routeinth.model.view.Card;


/**
 * A simple {@link Fragment} subclass.
 */
public class CardFragment extends Fragment {

    public CardFragment() {
        // Required empty public constructor
    }

    private Unbinder unbinder;
    private List<Card> cards;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCards();
    }

    private void setCards() {
        cards = new ArrayList<>();
        Card c1 = new Card();
        c1.setName("BTS Student Card");
        c1.setNumber("1234567890");
        c1.setBalance(500);
        Card c2 = new Card();
        c2.setName("BTS Student Card 2");
        c2.setNumber("487923874");
        c2.setBalance(222);
        Card c3 = new Card();
        c3.setName("BTS Student Card 3");
        c3.setNumber("1298378912");
        c3.setBalance(12);
        cards.add(c1);
        cards.add(c2);
        cards.add(c3);

    }

    @BindView(R.id.list)
    RecyclerView list;
    private CardAdapter cardAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_card, container, false);
        unbinder = ButterKnife.bind(this, v);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((MainActivity) getActivity()).showFab();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        cardAdapter = new CardAdapter(getContext(), cards);
        list.setHasFixedSize(true);
        list.setLayoutManager(layoutManager);
        list.setAdapter(cardAdapter);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
//        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
//        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
