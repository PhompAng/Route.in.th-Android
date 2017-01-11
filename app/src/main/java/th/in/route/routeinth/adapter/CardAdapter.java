package th.in.route.routeinth.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.in.route.routeinth.R;
import th.in.route.routeinth.app.FirebaseUtils;
import th.in.route.routeinth.app.UIDUtils;
import th.in.route.routeinth.model.view.Card;

import static java.security.AccessController.getContext;

/**
 * Created by phompang on 1/3/2017 AD.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private Context context;
    private List<Card> cards;
    private ViewHolder.OnAddValue listener;

    public CardAdapter(Context context, List<Card> cards, ViewHolder.OnAddValue listener) {
        this.context = context;
        this.cards = cards;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_card, parent, false);
        return new ViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Card card = cards.get(position);
        Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier(card.getSystem().toLowerCase() + "_" + unCapitalize(card.getType()), "drawable", context.getPackageName());
        Glide.with(context).load(resourceId).fitCenter().centerCrop().into(holder.img);
        holder.name.setText(card.getName());
        holder.number.setText(card.getNumber());
        holder.balance.setText(String.format(Locale.getDefault(), "%d Baht", card.getBalance()));
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        OnAddValue listener;

        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.number)
        TextView number;
        @BindView(R.id.balance)
        TextView balance;
        @BindView(R.id.add_balance)
        Button addBalance;
        ViewHolder(View itemView, OnAddValue listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;

            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
            addBalance.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                switch (v.getId()) {
                    case R.id.add_balance:
                        listener.addBalance(getAdapterPosition());
                        break;
                    default:
                        listener.editCard(getAdapterPosition());
                }

            }
        }

        @Override
        public boolean onLongClick(View v) {
            return listener != null && listener.deleteCard(getAdapterPosition());
        }

        public interface OnAddValue {
            void addBalance(int position);
            boolean deleteCard(int position);
            void editCard(int position);
        }
    }

    private String unCapitalize(final String line) {
        return Character.toLowerCase(line.charAt(0)) + line.substring(1);
    }
}
