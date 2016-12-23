package th.in.route.routeinth.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import th.in.route.routeinth.R;
import th.in.route.routeinth.ResultFragment;
import th.in.route.routeinth.model.result.Result;

/**
 * Created by Acer on 20/12/2559.
 */

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {

    Context mContext;
    Result result;

    public RouteAdapter(Result result, Context mContext) {
        this.result = result;
        this.mContext = mContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView stationNameLabel;
        private ImageView resourceStationImage;
        private LinearLayout routeItem;
        public ViewHolder(View itemView) {
            super(itemView);
            stationNameLabel = (TextView) itemView.findViewById(R.id.resultStationName);
            resourceStationImage = (ImageView) itemView.findViewById(R.id.resultStationImage);
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
    public void onBindViewHolder(RouteAdapter.ViewHolder holder, int position) {
        holder.stationNameLabel.setText(result.object_route.get(position).name.th);
        if(position == 0 && result.object_route.get(position).name.th.charAt(0) == 'A'){
            holder.resourceStationImage.setImageResource(R.drawable.arl_ori);
            holder.resourceStationImage.setColorFilter(R.color.colorPrimaryDark);
        }else if (position == result.object_route.size()-1 && result.object_route.get(position).name.th.charAt(0) == 'A'){
            holder.resourceStationImage.setImageResource(R.drawable.arl_des);
        }else {
            holder.resourceStationImage.setImageResource(R.drawable.arl_between);
        }
    }

    @Override
    public int getItemCount() {
        return result.object_route.size();
    }
}
