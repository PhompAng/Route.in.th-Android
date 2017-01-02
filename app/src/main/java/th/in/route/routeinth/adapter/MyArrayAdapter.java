package th.in.route.routeinth.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by phompang on 12/30/2016 AD.
 */

public class MyArrayAdapter<T> extends ArrayAdapter<T> {
    private Context context;
    private int resource;
    private List<T> list;

    public MyArrayAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.list = objects;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return setUp(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return setUp(position, convertView, parent);
    }

    private View setUp(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
            holder = new ViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText(list.get(position).toString());
        convertView.setTag(holder);

        return convertView;
    }

    private class ViewHolder {
        TextView text;

        public ViewHolder(View itemView) {
            text = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}
