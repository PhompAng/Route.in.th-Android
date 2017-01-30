package th.in.route.routeinth.adapter.viewholder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;

import th.in.route.routeinth.R;
import th.in.route.routeinth.model.system.Station;
import th.in.route.routeinth.view.StationChip;

/**
 * Created by phompang on 12/22/2016 AD.
 */

public class StationViewHolder extends ChildViewHolder implements View.OnClickListener {
    private OnStationClickListener listener;
    private TextView name;
    private StationChip chip;
    public StationViewHolder(View itemView, OnStationClickListener listener) {
        super(itemView);
        this.name = (TextView) itemView.findViewById(R.id.name);
        this.chip = (StationChip) itemView.findViewById(R.id.chip);
        this.listener = listener;
        itemView.setOnClickListener(this);
    }

    public void bind(@NonNull String name, @NonNull Station station) {
        this.name.setText(name);
        this.chip.setStation(station);
    }

    public interface OnStationClickListener {
        void onClick(int parentPosition, int childPosition);
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(getParentAdapterPosition(), getChildAdapterPosition());
        }
    }
}
