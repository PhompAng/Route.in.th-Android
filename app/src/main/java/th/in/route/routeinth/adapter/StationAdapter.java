package th.in.route.routeinth.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

import th.in.route.routeinth.R;
import th.in.route.routeinth.adapter.viewholder.StationViewHolder;
import th.in.route.routeinth.adapter.viewholder.SystemViewHolder;
import th.in.route.routeinth.model.system.Station;

/**
 * Created by phompang on 12/22/2016 AD.
 */

public class StationAdapter extends ExpandableRecyclerViewAdapter<SystemViewHolder, StationViewHolder> {
    public StationAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public SystemViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_system_viewholder, parent, false);
        return new SystemViewHolder(v);
    }

    @Override
    public StationViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_station_viewholder, parent, false);
        return new StationViewHolder(v);
    }

    @Override
    public void onBindChildViewHolder(StationViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        Station station = (Station) group.getItems().get(childIndex);
        holder.setName(station.getEn());
    }

    @Override
    public void onBindGroupViewHolder(SystemViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setTitle(group);
    }
}
