package th.in.route.routeinth.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import th.in.route.routeinth.R;

/**
 * Created by Acer on 25/1/2560.
 */

public class FacilityAdapter extends RecyclerView.Adapter<FacilityAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<String> facilities;
    public FacilityAdapter(Context mContext, ArrayList<String> facilities) {
        this.mContext = mContext;
        this.facilities = facilities;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.facility_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Resources resources = mContext.getResources();
        final int resourceId = resources.getIdentifier(facilities.get(position), "drawable", mContext.getPackageName());
        holder.image.setImageResource(resourceId);
    }

    @Override
    public int getItemCount() {
        return facilities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.facilityItem);
        }
    }
}
