package th.in.route.routeinth;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import th.in.route.routeinth.adapter.TweetAdapter;
import th.in.route.routeinth.model.view.Tweet;


/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceAlertsFragment extends Fragment {


    public ServiceAlertsFragment() {
        // Required empty public constructor
    }

    private Unbinder unbinder;
    private List<Tweet> tweets;
    private TweetAdapter adapter;
    private DatabaseReference reference;
    private Query query;
    private ValueEventListener listener;

    @BindView(R.id.list)
    RecyclerView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_service_alerts, container, false);
        unbinder = ButterKnife.bind(this, v);
        tweets = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        adapter = new TweetAdapter(getContext(), tweets);

        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);

        reference = FirebaseDatabase.getInstance().getReference();
        query = reference.child("service_alerts").limitToLast(100);
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tweets.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    tweets.add(snapshot.getValue(Tweet.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        query.addValueEventListener(listener);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        query.removeEventListener(listener);
        unbinder.unbind();
    }


}
