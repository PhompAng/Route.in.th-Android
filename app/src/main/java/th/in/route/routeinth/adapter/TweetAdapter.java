package th.in.route.routeinth.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.in.route.routeinth.R;
import th.in.route.routeinth.model.view.Tweet;

/**
 * Created by phompang on 2/1/2017 AD.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {
    private Context context;
    private List<Tweet> tweets;

    public TweetAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_tweet, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);
        if (tweet.getUser().getProfile_image_url() != null) {
            Glide.with(context).load(tweet.getUser().getProfile_image_url()).centerCrop().into(holder.img);
        } else {
            Glide.clear(holder.img);
        }
        holder.name.setText(tweet.getUser().getName());
        holder.screen_name.setText(String.format("@%s", tweet.getUser().getScreen_name()));
        String time = (new SimpleDateFormat("MM/dd/yy", Locale.getDefault())).format(new Date(tweet.getTimestamp_ms()));
        holder.time.setText(time);
        holder.text.setText(tweet.getText());
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    @Override
    public long getItemId(int position) {
        return tweets.get(position).getId();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.screen_name)
        TextView screen_name;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.text)
        TextView text;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
