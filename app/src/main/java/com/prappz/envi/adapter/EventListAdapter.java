package com.prappz.envi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.prappz.envi.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by royce on 07-05-2016.
 */
public class EventListAdapter extends BaseAdapter {

    LayoutInflater mLayoutInflater;
    List<ParseObject> event;
    String[] month = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public EventListAdapter(Context context, List<ParseObject> event) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.event = event;
    }

    @Override
    public int getCount() {
        return event.size();
    }

    @Override
    public Object getItem(int position) {
        return event.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {

            convertView = mLayoutInflater.inflate(R.layout.row_event, null);
            viewHolder = new ViewHolder();
            viewHolder.day = (TextView) convertView.findViewById(R.id.event_day);
            viewHolder.name = ((TextView) convertView.findViewById(R.id.event_title));
            viewHolder.month = ((TextView) convertView.findViewById(R.id.event_month));
            viewHolder.location = ((TextView) convertView.findViewById(R.id.event_place));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ParseObject m = event.get(position);

        try {
            viewHolder.day.setText(m.getString("day"));
            viewHolder.name.setText(m.getString("eventName"));
            viewHolder.location.setText(m.getString("place"));
            int mon = Integer.parseInt(m.getString("month"));
            viewHolder.month.setText(month[mon - 1]);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return convertView;
    }

    static class ViewHolder {
        TextView name, location, day, month;
    }


}
