package th.in.route.routeinth;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import th.in.route.routeinth.adapter.CardAdapter;
import th.in.route.routeinth.app.FirebaseUtils;
import th.in.route.routeinth.app.UIDUtils;
import th.in.route.routeinth.model.view.Card;
import th.in.route.routeinth.view.AddValueDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class CardFragment extends Fragment implements
        CardAdapter.ViewHolder.OnAddValue {

    Query query;
    ValueEventListener listener;

    private UIDUtils uidUtils;
    public static final int ADD_VALUE_DIALOG = 1;

    public CardFragment() {
        // Required empty public constructor
    }

    private Unbinder unbinder;
    private List<Card> cards;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cards = new ArrayList<>();
        getCardFromFirebase();

        uidUtils = new UIDUtils(getContext());
        ((MainActivity) getActivity()).tabVisibility(View.GONE);
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        cardAdapter = new CardAdapter(getContext(), cards, this);
        list.setHasFixedSize(true);
        list.setLayoutManager(layoutManager);
        list.setAdapter(cardAdapter);

        return v;
    }

    private void getCardFromFirebase(){
        UIDUtils uidUtils = new UIDUtils(getContext());
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        query = databaseReference.child("users").child(uidUtils.getUID()).child("cardMap");
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cards.clear();
                for(DataSnapshot cardData: dataSnapshot.getChildren()){
                    Card card = cardData.getValue(Card.class);
                    cards.add(card);
                }
                checkNumberOfCard();
                cardAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        query.addValueEventListener(listener);
    }

    public void checkNumberOfCard(){
        if (cards.size() == 3) {
            ((MainActivity) getActivity()).hideFab();
        } else {
            ((MainActivity) getActivity()).showFab();
        }
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
        query.removeEventListener(listener);
    }

    @Override
    public void addBalance(int position) {
        Card card = cards.get(position);
        DialogFragment dialogFragment = AddValueDialog.newInstance(card);
        dialogFragment.setTargetFragment(CardFragment.this, ADD_VALUE_DIALOG);
        dialogFragment.show(getFragmentManager(), "add");
    }

    @Override
    public boolean deleteCard(int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final Card card = cards.get(position);
        builder.setTitle(R.string.delete_card);
        builder.setMessage(String.format(Locale.getDefault(), getString(R.string.delete_card_text), card.getSystem()));
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseUtils.deleteCard(uidUtils.getUID(), card);
                Toast.makeText(getContext(), R.string.delete_success, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
        return true;
    }

    @Override
    public void editCard(int position) {
        Intent intent = new Intent(getContext(), EditCardActivity.class);
        intent.putExtra("card", Parcels.wrap(cards.get(position)));
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADD_VALUE_DIALOG:
                if (resultCode == Activity.RESULT_OK) {
                    Log.d("asd", data.toString());
                    FirebaseUtils.addValue(uidUtils.getUID(), (Card) Parcels.unwrap(data.getParcelableExtra(AddValueDialog.ARG_CARD)), data.getIntExtra("value", 0));
                    Toast.makeText(getContext(), R.string.value_added, Toast.LENGTH_SHORT).show();
                }
        }
    }
}
