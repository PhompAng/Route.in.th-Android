package th.in.route.routeinth.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import th.in.route.routeinth.MainActivity;
import th.in.route.routeinth.R;
import th.in.route.routeinth.ResultFragment;
import th.in.route.routeinth.model.result.Result;
import th.in.route.routeinth.model.result.Route;
import th.in.route.routeinth.model.view.RouteItem;

/**
 * Created by Acer on 20/12/2559.
 */

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {

    Context mContext;
    List<RouteItem> routeItems;
    ResultFragment fragment;

    public RouteAdapter(List<RouteItem> routeItems, Context mContext, ResultFragment resultFragment) {
        this.routeItems = routeItems;
        this.mContext = mContext;
        this.fragment = resultFragment;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView stationNameLabel;
        private ImageView resourceStationImage;
        private TextView viewAllStationLabel;
        private TextView viewCodeLabel;
        private LinearLayout routeItem;
        public ViewHolder(View itemView) {
            super(itemView);
            stationNameLabel = (TextView) itemView.findViewById(R.id.resultStationName);
            resourceStationImage = (ImageView) itemView.findViewById(R.id.resultStationImage);
            viewAllStationLabel = (TextView) itemView.findViewById(R.id.viewAllStationLabel);
            viewCodeLabel = (TextView) itemView.findViewById(R.id.viewCodeLabel);
            routeItem = (LinearLayout) itemView.findViewById(R.id.routeItem);
        }
    }

    @Override
    public RouteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(final RouteAdapter.ViewHolder holder, final int position) {

        //set up color of each system
        int color;
        if(routeItems.get(position).getStationOf().equals("A")){
            color = R.color.colorArl;
        }else if(routeItems.get(position).getStationOf().equals("B")){
            color = R.color.colorBts;
        }else {
            color = R.color.colorMrt;
        }

        GradientDrawable viewAllStationbg = (GradientDrawable) holder.viewAllStationLabel.getBackground();

        //set code label
        if (routeItems.get(position).getType() != "between"){
            holder.viewCodeLabel.setText(routeItems.get(position).getRoute().code);
            GradientDrawable labelBg = (GradientDrawable) holder.viewCodeLabel.getBackground();
            labelBg.setColorFilter(ContextCompat.getColor(mContext, color), PorterDuff.Mode.ADD);
            holder.viewCodeLabel.setVisibility(View.VISIBLE);
        }else {
            holder.viewCodeLabel.setVisibility(View.GONE);
        }

        //each listview
        if(routeItems.get(position).getType() == "ori"){
            holder.stationNameLabel.setText(routeItems.get(position).getRoute().name.th);
            holder.resourceStationImage.setImageResource(R.drawable.route_ori);
            holder.viewAllStationLabel.setVisibility(View.GONE);
            holder.stationNameLabel.setTextSize(14);
            if(fragment.getIsShow(routeItems.get(position).getSystem())){
                Log.wtf("bbbbbb", routeItems.get(position).getSystem() + "");
                holder.viewAllStationLabel.setText("HIDE All STATIONS");
                holder.viewAllStationLabel.setVisibility(View.VISIBLE);
                holder.stationNameLabel.setTextSize(14);
                holder.routeItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fragment.setIsShow(routeItems.get(position).getSystem(), false);
                    }
                });
            }
        }else if(routeItems.get(position).getType() == "des"){
            holder.stationNameLabel.setText(routeItems.get(position).getRoute().name.th);
            holder.resourceStationImage.setImageResource(R.drawable.route_des);
            holder.viewAllStationLabel.setVisibility(View.GONE);
            holder.stationNameLabel.setTextSize(14);
        } else if(routeItems.get(position).getType().equals("station")){
            holder.stationNameLabel.setText(routeItems.get(position).getRoute().name.th);
            holder.resourceStationImage.setImageResource(R.drawable.route_station);
            holder.viewAllStationLabel.setVisibility(View.GONE);
            holder.stationNameLabel.setTextSize(14);
        }
        else if(routeItems.get(position).getType().equals("start")){
            holder.stationNameLabel.setText(routeItems.get(position).getRoute().name.th);
            holder.resourceStationImage.setImageResource(R.drawable.route_start);
            holder.viewAllStationLabel.setVisibility(View.GONE);
            holder.stationNameLabel.setTextSize(14);
            if(fragment.getIsShow(routeItems.get(position).getSystem())){
                Log.wtf("cccc", routeItems.get(position).getSystem() + "");
                holder.viewAllStationLabel.setText("HIDE All STATIONS");
                viewAllStationbg.setColorFilter(ContextCompat.getColor(mContext, R.color.gray), PorterDuff.Mode.ADD);
                holder.viewAllStationLabel.setVisibility(View.VISIBLE);
                holder.stationNameLabel.setTextSize(14);
                holder.routeItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fragment.setIsShow(routeItems.get(position).getSystem(), false);
                    }
                });
            }
        }else if (routeItems.get(position).getType().equals("end")){
            holder.stationNameLabel.setText(routeItems.get(position).getRoute().name.th);
            holder.resourceStationImage.setImageResource(R.drawable.route_end);
            holder.viewAllStationLabel.setVisibility(View.GONE);
            holder.stationNameLabel.setTextSize(14);
        }else if(routeItems.get(position).getType().equals("between")){
            holder.stationNameLabel.setText(routeItems.get(position).getRoute().station_cnt + " สถานี");
            holder.stationNameLabel.setTextSize(10);
            holder.viewAllStationLabel.setVisibility(View.VISIBLE);
            holder.viewAllStationLabel.setText("VIEW ALL STATIONS");
            viewAllStationbg.setColorFilter(ContextCompat.getColor(mContext, R.color.gray), PorterDuff.Mode.ADD);
            holder.viewCodeLabel.setVisibility(View.GONE);
            holder.resourceStationImage.setImageResource(R.drawable.route_between);
            holder.routeItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragment.setIsShow(routeItems.get(position).getSystem(), true);
                }
            });
        }else {
            Log.d("out of case", "TRUE");
        }
        holder.resourceStationImage.setColorFilter(ContextCompat.getColor(mContext, color));
    }

    @Override
    public int getItemCount() {
        return routeItems.size();
    }
}
