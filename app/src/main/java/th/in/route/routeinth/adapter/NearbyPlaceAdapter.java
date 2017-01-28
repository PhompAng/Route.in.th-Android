package th.in.route.routeinth.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import fi.foyt.foursquare.api.entities.Location;
import th.in.route.routeinth.R;
import th.in.route.routeinth.model.foursquare.MyCompactVenue;

/**
 * Created by Acer on 26/1/2560.
 */

public class NearbyPlaceAdapter extends RecyclerView.Adapter<NearbyPlaceAdapter.ViewHolder>{

    private Context mContext;
    private List<MyCompactVenue> places;

    public NearbyPlaceAdapter(List<MyCompactVenue> places, Context mContext) {
        this.places = places;
        this.mContext = mContext;
    }

    @Override
    public NearbyPlaceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_nearby_place, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NearbyPlaceAdapter.ViewHolder holder, int position) {
        holder.nearbyName.setText(places.get(position).getName());
        Location location = places.get(position).getLocation();
        holder.nearbyAddress.setText(location.getAddress() + ", " + location.getCity());
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nearbyName;
        private TextView nearbyAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            nearbyName = (TextView) itemView.findViewById(R.id.nearbyName);
            nearbyAddress = (TextView) itemView.findViewById(R.id.nearbyAddress);
        }
    }
}
