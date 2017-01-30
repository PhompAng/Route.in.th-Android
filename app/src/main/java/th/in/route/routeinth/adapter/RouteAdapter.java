package th.in.route.routeinth.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.List;
import java.util.Locale;

import th.in.route.routeinth.R;
import th.in.route.routeinth.ResultFragment;
import th.in.route.routeinth.StationMapActivity;
import th.in.route.routeinth.app.DistanceUtils;
import th.in.route.routeinth.app.StationUtils;
import th.in.route.routeinth.model.result.Route;
import th.in.route.routeinth.model.view.RouteItem;

/**
 * Created by Acer on 20/12/2559.
 */

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {

    private Context mContext;
    private List<RouteItem> routeItems;
    private ResultFragment fragment;
    private String nearestKey;
    private boolean isNavigate;

    public RouteAdapter(List<RouteItem> routeItems, Context mContext, ResultFragment resultFragment) {
        this.routeItems = routeItems;
        this.mContext = mContext;
        this.fragment = resultFragment;
        isNavigate = false;
    }

    private void hasFacilities(final ImageView holder, String key) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("facilities").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<String>> indicator = new GenericTypeIndicator<List<String>>(){};
                List<String> facilities = dataSnapshot.getValue(indicator);
                if (facilities.contains("elevator") || facilities.contains("ramp")) {
                    holder.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setNearestKey(String nearestKey) {
        this.nearestKey = nearestKey;
    }

    public void setNavigate(boolean isNavigate) {
        this.isNavigate = isNavigate;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView stationNameLabel;
        private ImageView resourceStationImage;
        private TextView viewHeadingLabel;
        private TextView viewCodeLabel;
        private ImageView showIcon;
        private ImageView infoLabel;
        private ConstraintLayout routeItem;
        ViewHolder(View itemView) {
            super(itemView);
            stationNameLabel = (TextView) itemView.findViewById(R.id.resultStationName);
            resourceStationImage = (ImageView) itemView.findViewById(R.id.resultStationImage);
            viewCodeLabel = (TextView) itemView.findViewById(R.id.viewCodeLabel);
            viewHeadingLabel = (TextView) itemView.findViewById(R.id.resultHeadingLabel);
            showIcon = (ImageView) itemView.findViewById(R.id.showIcon);
            infoLabel = (ImageView) itemView.findViewById(R.id.infoLabel);
            routeItem = (ConstraintLayout) itemView.findViewById(R.id.routeItem);
        }
    }

    @Override
    public RouteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RouteAdapter.ViewHolder holder, int position) {
        final RouteItem routeItem = routeItems.get(position);
        String routeName;
        String headingName;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        switch (preferences.getString("preference_lang", "en")) {
            case "en":
                routeName = routeItem.getRoute().name.en;
                headingName = routeItem.getRoute().heading.en;
                break;
            case "th":
                routeName = routeItem.getRoute().name.th;
                headingName = routeItem.getRoute().heading.th;
                break;
            default:
                routeName = routeItem.getRoute().name.en;
                headingName = routeItem.getRoute().heading.en;
                break;
        }
        holder.routeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("type", routeItem.getType());
                if (routeItem.getType().equals("station")) {
                    fragment.setIsShow(routeItem.getSystem(), false);
                } else if(routeItem.getType().equals("between")) {
                    fragment.setIsShow(routeItem.getSystem(), true);
                } else {
                    Intent intent = new Intent(mContext, StationMapActivity.class);
                    intent.putExtra("station", Parcels.wrap(StationUtils.getInstance().getStationFromKey(routeItem.getRoute().name.key)));
                    intent.putExtra("location", DistanceUtils.getInstance().getLocationFromKey(routeItem.getRoute().name.key));
                    mContext.startActivity(intent);
                }
            }
        });
        //set up color of each system
        int color;
        if (routeItem.getStationOf().equals("A")) {
            color = R.color.colorArl;
        } else if (routeItem.getRoute().line.en.equals("Sukhumvit")) {
            color = R.color.colorBtsSukhumvit;
        } else if (routeItem.getRoute().line.en.equals("Silom")) {
            color = R.color.colorBtsSilom;
        } else {
            color = R.color.colorMrt;
        }

        if (isNavigate && nearestKey != null) {
            if (!routeItem.getType().equals("between") && routeItem.getRoute().name.key.equals(nearestKey)) {
                holder.stationNameLabel.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                holder.viewHeadingLabel.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
            } else if (routeItem.getType().equals("between")) {
                boolean found = false;
                for (Route route: routeItem.getRoutes()) {
                    if (route.name.key.equals(nearestKey)) {
                        found = true;
                    }
                }

                if (found) {
                    holder.stationNameLabel.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                }
            } else {
                holder.stationNameLabel.setTextColor(ContextCompat.getColor(mContext, R.color.textSecondary));
                holder.viewHeadingLabel.setTextColor(ContextCompat.getColor(mContext, R.color.textSecondary));
            }
        }

//        GradientDrawable viewAllStationBg = (GradientDrawable) holder.viewAllStationLabel.getBackground();

        holder.infoLabel.setVisibility(View.GONE);
        //set code label and heading label
        if (!routeItem.getType().equals("between")) {
            holder.viewCodeLabel.setText(routeItem.getRoute().name.code);
            GradientDrawable labelBg = (GradientDrawable) holder.viewCodeLabel.getBackground();
            labelBg.setColorFilter(ContextCompat.getColor(mContext, color), PorterDuff.Mode.ADD);
            holder.viewCodeLabel.setVisibility(View.VISIBLE);
            holder.viewHeadingLabel.setText(String.format(Locale.getDefault(), mContext.getResources().getString(R.string.heading), headingName));
            holder.viewHeadingLabel.setVisibility(View.VISIBLE);
            hasFacilities(holder.infoLabel, routeItem.getRoute().name.key);
        } else {
            holder.viewCodeLabel.setVisibility(View.GONE);
            holder.viewHeadingLabel.setVisibility(View.GONE);
        }

        //each listview
        switch (routeItem.getType()) {
            case "siam":
                holder.stationNameLabel.setText(routeName);
                holder.resourceStationImage.setImageResource(R.drawable.route_one_between);
                holder.showIcon.setVisibility(View.GONE);
                holder.stationNameLabel.setTextSize(14);
                break;
            case "ori_one":
                holder.stationNameLabel.setText(routeName);
                holder.resourceStationImage.setImageResource(R.drawable.route_one_ori);
                holder.stationNameLabel.setTextSize(14);
                holder.showIcon.setVisibility(View.GONE);
                break;
            case "des_one":
                holder.stationNameLabel.setText(routeName);
                holder.resourceStationImage.setImageResource(R.drawable.route_one_des);
                holder.showIcon.setVisibility(View.GONE);
                holder.stationNameLabel.setTextSize(14);
                break;
            case "ori":
            case "start":
                if (routeItem.getType().equals("ori")) {
                    holder.resourceStationImage.setImageResource(R.drawable.route_ori);
                } else {
                    holder.resourceStationImage.setImageResource(R.drawable.route_start);
                }
                holder.stationNameLabel.setText(routeName);
                holder.showIcon.setVisibility(View.GONE);
                holder.stationNameLabel.setTextSize(14);
                break;
            case "between":
//                holder.stationNameLabel.setText(routeItem.getRoute().station_cnt + " " + mContext.getResources().getString(R.string.stations));
                holder.stationNameLabel.setText(mContext.getResources().getQuantityString(R.plurals.station, routeItem.getRoute().station_cnt, routeItem.getRoute().station_cnt));
                holder.stationNameLabel.setTextSize(12);
                holder.showIcon.setVisibility(View.VISIBLE);
//                viewAllStationBg.setColorFilter(ContextCompat.getColor(mContext, R.color.gray), PorterDuff.Mode.ADD);
                holder.viewCodeLabel.setVisibility(View.GONE);
                holder.resourceStationImage.setImageResource(R.drawable.route_between);
                break;
            default:
                switch (routeItem.getType()) {
                    case "des":
                        holder.resourceStationImage.setImageResource(R.drawable.route_des);
                        break;
                    case "end":
                        holder.resourceStationImage.setImageResource(R.drawable.route_end);
                        break;
                    case "station":
                        holder.resourceStationImage.setImageResource(R.drawable.route_station);
                        if (fragment.getIsShow(routeItem.getSystem())) {
//                            viewAllStationBg.setColorFilter(ContextCompat.getColor(mContext, R.color.gray), PorterDuff.Mode.ADD);
                            holder.showIcon.setVisibility(View.VISIBLE);
                            holder.stationNameLabel.setTextSize(14);
                        }
                        break;
                }
                holder.stationNameLabel.setText(routeName);
                holder.showIcon.setVisibility(View.GONE);
                holder.stationNameLabel.setTextSize(14);
                break;
        }
        holder.resourceStationImage.setColorFilter(ContextCompat.getColor(mContext, color));
    }

    @Override
    public int getItemCount() {
        return routeItems.size();
    }
}
