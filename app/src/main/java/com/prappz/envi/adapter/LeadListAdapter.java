package com.prappz.envi.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.prappz.envi.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by royce on 04-05-2016.
 */
public class LeadListAdapter extends BaseAdapter {

    private static final String TAG = "SLA";
    private Context context;
    private List<ParseUser> items;
    LayoutInflater mInflater;

    public LeadListAdapter(Context context, List<ParseUser> items) {
        this.context = context;
        this.items = items;
        mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public ParseUser getItem(int position) {
        return items.get(position);
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

            convertView = mInflater.inflate(R.layout.row_leaderboard, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.cover = (ImageView) convertView.findViewById(R.id.row_lead_image);
            viewHolder.name = ((TextView) convertView.findViewById(R.id.row_lead_name));
            viewHolder.score = ((TextView) convertView.findViewById(R.id.row_lead_score));
            viewHolder.location = ((TextView) convertView.findViewById(R.id.row_lead_place));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ParseUser m = items.get(position);

        try {
            viewHolder.score.setText(String.valueOf(m.getInt("score")));
            viewHolder.name.setText(m.getString("name"));
            viewHolder.location.setText(m.getString("city"));
//            viewHolder.title.setText("Reported from " + m.getString("city") + " by " + m.getString("name"));
            Picasso.with(context)
                    .load(m.getString("url"))
                    .placeholder(R.drawable.ic_person_black_24dp)
                    .error(R.drawable.ic_person_black_24dp)
                    .into(viewHolder.cover);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView cover;
        TextView name, score, location;
    }


}