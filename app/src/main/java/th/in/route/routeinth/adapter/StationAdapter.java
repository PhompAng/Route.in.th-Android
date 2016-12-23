package th.in.route.routeinth.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;

import java.util.List;

import th.in.route.routeinth.R;
import th.in.route.routeinth.adapter.viewholder.StationViewHolder;
import th.in.route.routeinth.adapter.viewholder.SystemViewHolder;
import th.in.route.routeinth.model.system.RailSystem;
import th.in.route.routeinth.model.system.Station;

/**
 * Created by phompang on 12/22/2016 AD.
 */

public class StationAdapter extends ExpandableRecyclerAdapter<RailSystem, Station, SystemViewHolder, StationViewHolder> {

    private StationViewHolder.OnStationClickListener listener;
    private LayoutInflater mInflater;
    private List<RailSystem> systems;

    /**
     * Primary constructor. Sets up {@link #mParentList} and {@link #mFlatItemList}.
     * <p>
     * Any changes to {@link #mParentList} should be made on the original instance, and notified via
     * {@link #notifyParentInserted(int)}
     * {@link #notifyParentRemoved(int)}
     * {@link #notifyParentChanged(int)}
     * {@link #notifyParentRangeInserted(int, int)}
     * {@link #notifyChildInserted(int, int)}
     * {@link #notifyChildRemoved(int, int)}
     * {@link #notifyChildChanged(int, int)}
     * methods and not the notify methods of RecyclerView.Adapter.
     *
     * @param parentList List of all parents to be displayed in the RecyclerView that this
     *                   adapter is linked to
     */
    public StationAdapter(Context context, @NonNull List<RailSystem> parentList, StationViewHolder.OnStationClickListener listener) {
        super(parentList);
        mInflater = LayoutInflater.from(context);
        systems = parentList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SystemViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View v = mInflater.inflate(R.layout.layout_system_viewholder, parentViewGroup, false);
        return new SystemViewHolder(v);
    }

    @NonNull
    @Override
    public StationViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View v = mInflater.inflate(R.layout.layout_station_viewholder, childViewGroup, false);
        return new StationViewHolder(v, listener);
    }

    @Override
    public void onBindParentViewHolder(@NonNull SystemViewHolder parentViewHolder, int parentPosition, @NonNull RailSystem parent) {
        parentViewHolder.bind(parent);
    }

    @Override
    public void onBindChildViewHolder(@NonNull StationViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull Station child) {
        childViewHolder.bind(child.getTh());
    }
}
