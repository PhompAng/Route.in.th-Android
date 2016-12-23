package th.in.route.routeinth.adapter.viewholder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;

import th.in.route.routeinth.R;

/**
 * Created by phompang on 12/22/2016 AD.
 */

public class StationViewHolder extends ChildViewHolder {
    private TextView name;
    public StationViewHolder(View itemView) {
        super(itemView);
        this.name = (TextView) itemView.findViewById(R.id.name);
    }

    public void bind(@NonNull String name) {
        this.name.setText(name);
    }
}
