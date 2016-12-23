package th.in.route.routeinth.adapter.viewholder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;

import th.in.route.routeinth.R;

/**
 * Created by phompang on 12/22/2016 AD.
 */

public class StationViewHolder extends ChildViewHolder implements View.OnClickListener {
    private OnStationClickListener listener;
    private TextView name;
    public StationViewHolder(View itemView, OnStationClickListener listener) {
        super(itemView);
        this.name = (TextView) itemView.findViewById(R.id.name);
        this.listener = listener;
        itemView.setOnClickListener(this);
    }

    public void bind(@NonNull String name) {
        this.name.setText(name);
    }

    public interface OnStationClickListener {
        void onClick(int parentPosition, int childPosition);
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(getParentAdapterPosition() ,getChildAdapterPosition());
        }
    }
}
