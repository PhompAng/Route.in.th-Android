package th.in.route.routeinth.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.List;

import th.in.route.routeinth.R;
import th.in.route.routeinth.ResultFragment;
import th.in.route.routeinth.StationMapActivity;
import th.in.route.routeinth.app.DistanceUtils;
import th.in.route.routeinth.app.StationUtils;
import th.in.route.routeinth.model.result.Route;
import th.in.route.routeinth.model.view.RouteItem;

import static java.security.AccessController.getContext;

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
        holder.routeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("type", routeItem.getType());
                if(routeItem.getType().equals("station")){
                    fragment.setIsShow(routeItem.getSystem(), false);
                }else if(routeItem.getType().equals("between")){
                    fragment.setIsShow(routeItem.getSystem(), true);
                }else {
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
                holder.infoLabel.setVisibility(View.VISIBLE);
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
                holder.infoLabel.setVisibility(View.GONE);
            } else {
                holder.infoLabel.setVisibility(View.VISIBLE);
                holder.stationNameLabel.setTextColor(ContextCompat.getColor(mContext, R.color.textSecondary));
                holder.viewHeadingLabel.setTextColor(ContextCompat.getColor(mContext, R.color.textSecondary));
            }
        }

//        GradientDrawable viewAllStationBg = (GradientDrawable) holder.viewAllStationLabel.getBackground();

        //set code label and heading label
        if (!routeItem.getType().equals("between")) {
            holder.viewCodeLabel.setText(routeItem.getRoute().name.code);
            GradientDrawable labelBg = (GradientDrawable) holder.viewCodeLabel.getBackground();
            labelBg.setColorFilter(ContextCompat.getColor(mContext, color), PorterDuff.Mode.ADD);
            holder.viewCodeLabel.setVisibility(View.VISIBLE);
            holder.viewHeadingLabel.setText("Heading:  " + routeItem.getRoute().heading.en);
            holder.viewHeadingLabel.setVisibility(View.VISIBLE);
//            holder.infoLabel.setVisibility(View.VISIBLE);
        } else {
            holder.viewCodeLabel.setVisibility(View.GONE);
            holder.viewHeadingLabel.setVisibility(View.GONE);
//            holder.infoLabel.setVisibility(View.GONE);
        }

        //each listview
        switch (routeItem.getType()) {
            case "siam":
                holder.stationNameLabel.setText(routeItem.getRoute().name.en);
                holder.resourceStationImage.setImageResource(R.drawable.route_one_between);
                holder.showIcon.setVisibility(View.GONE);
                holder.stationNameLabel.setTextSize(14);
                break;
            case "ori_one":
                holder.stationNameLabel.setText(routeItem.getRoute().name.en);
                holder.resourceStationImage.setImageResource(R.drawable.route_one_ori);
                holder.stationNameLabel.setTextSize(14);
                holder.showIcon.setVisibility(View.GONE);
                break;
            case "des_one":
                holder.stationNameLabel.setText(routeItem.getRoute().name.en);
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
                holder.stationNameLabel.setText(routeItem.getRoute().name.en);
                holder.showIcon.setVisibility(View.GONE);
                holder.stationNameLabel.setTextSize(14);
                break;
            case "between":
                holder.stationNameLabel.setText(routeItem.getRoute().station_cnt + " stations");
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
                holder.stationNameLabel.setText(routeItem.getRoute().name.en);
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
