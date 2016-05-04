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
import com.prappz.envi.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by royce on 04-05-2016.
 */
public class IssueListAdapter extends BaseAdapter {

    private static final String TAG = "SLA";
    private Context context;
    private List<ParseObject> items;
    LayoutInflater mInflater;

    public IssueListAdapter(Context context, List<ParseObject> items) {
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
    public ParseObject getItem(int position) {
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

            convertView = mInflater.inflate(R.layout.row_issue, null);
            viewHolder = new ViewHolder();
            viewHolder.cover = (ImageView) convertView.findViewById(R.id.issue_image);
            viewHolder.name = ((TextView) convertView.findViewById(R.id.issue_name));
            viewHolder.desc = ((TextView) convertView.findViewById(R.id.issue_desc));
            viewHolder.location = ((TextView) convertView.findViewById(R.id.issue_place));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ParseObject m = items.get(position);

        try {
            viewHolder.desc.setText(m.getString("desc"));
            viewHolder.name.setText(m.getString("name"));
            viewHolder.location.setText(m.getString("city"));
//            viewHolder.title.setText("Reported from " + m.getString("city") + " by " + m.getString("name"));
            Picasso.with(context)
                    .load(m.getParseFile("image").getUrl())
                    .placeholder(android.R.drawable.ic_menu_camera)
                    .error(android.R.drawable.ic_menu_camera)
                    .into(viewHolder.cover);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView cover;
        TextView name, desc,location;
    }


}