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
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.prappz.envi.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by royce on 14-05-2016.
 */
public class ClosedIssueAdapter extends BaseAdapter {

    private static final String TAG = "SLA";
    private Context context;
    private List<ParseObject> items;
    LayoutInflater mInflater;

    public ClosedIssueAdapter(Context context, List<ParseObject> items) {
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

            convertView = mInflater.inflate(R.layout.row_closed_issue, null);
            viewHolder = new ViewHolder();
            viewHolder.cover = (ImageView) convertView.findViewById(R.id.issue_image);
            viewHolder.name = ((TextView) convertView.findViewById(R.id.issue_name));
            viewHolder.close = ((TextView) convertView.findViewById(R.id.issue_close));

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ParseObject m = items.get(position);

        try {
            viewHolder.name.setText(m.getString("fromName") + " requested to close this issue");
            if (m.getBoolean("isActive"))
                viewHolder.close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeIssue(position);
                    }
                });
            else
                viewHolder.close.setText("ISSUE CLOSED");
            Picasso.with(context)
                    .load(m.getString("url"))
                    .placeholder(android.R.drawable.ic_menu_camera)
                    .error(android.R.drawable.ic_menu_camera)
                    .into(viewHolder.cover);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return convertView;
    }

    private void closeIssue(int position) {
        if (!items.get(position).getBoolean("isActive"))
            Toast.makeText(context, "Issue already closed", Toast.LENGTH_SHORT).show();
        else {
            Toast.makeText(context, "Closing issue, please wait", Toast.LENGTH_SHORT).show();
            final ParseObject object = items.get(position);
            object.put("isActive", false);
            object.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null)
                        makeIssueClosed(object.getString("eventId"));
                }
            });
        }
    }

    private void makeIssueClosed(String eventId) {
        ParseQuery<ParseObject> queery = ParseQuery.getQuery("Issue");
        queery.getInBackground(eventId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null && object != null) {
                    object.put("isClosed", true);
                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(context, "Issue closed", Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        });
    }

    static class ViewHolder {
        ImageView cover;
        TextView name, close;
    }


}
