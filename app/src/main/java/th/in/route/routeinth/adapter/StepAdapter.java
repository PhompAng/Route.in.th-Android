package th.in.route.routeinth.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.googledirection.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.in.route.routeinth.R;

/**
 * Created by phompang on 1/31/2017 AD.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {

    private Context context;
    private List<Step> stepList;

    public StepAdapter(Context context, List<Step> stepList) {
        this.context = context;
        this.stepList = stepList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_step, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Step step = stepList.get(position);
        switch (step.getTravelMode()) {
            case "WALKING":
                holder.travel_mode.setImageResource(R.drawable.ic_directions_walk_black_24dp);
                holder.shortName.setVisibility(View.INVISIBLE);
                break;
            case "TRANSIT":
                holder.travel_mode.setImageResource(R.drawable.ic_directions_bus_black_24dp);
                holder.shortName.setText(step.getTransitDetail().getLine().getShortName());
                break;
            default:
                break;
        }
        holder.instruction.setText(step.getHtmlInstruction() + " (" + step.getDistance().getText() + ")");
    }

    @Override
    public int getItemCount() {
        return stepList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.travel_mode)
        ImageView travel_mode;
        @BindView(R.id.instruction)
        TextView instruction;
        @BindView(R.id.short_name)
        TextView shortName;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
