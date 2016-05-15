package com.prappz.envi.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.prappz.envi.R;
import com.prappz.envi.utils.CircleTransformation;
import com.prappz.envi.utils.CropSquareTransformation;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by royce on 04-05-2016.
 */
public class IssueListAdapter extends BaseAdapter {

    private static final String TAG = "SLA";
    LayoutInflater mInflater;
    DisplayMetrics displaymetrics;
    private Context context;
    private List<ParseObject> items;

    public IssueListAdapter(Context context, List<ParseObject> items) {
        this.context = context;
        this.items = items;
        mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
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
            viewHolder.userimage = (ImageView) convertView.findViewById(R.id.user_image);
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

            try {
                Log.i("MM OBJECT ***", m.getString("pic") + "*********************************");
                Picasso.with(context)
                        .load(m.getString("pic"))
                        .placeholder(android.R.color.darker_gray)
                        .transform(new CircleTransformation())
                        .error(android.R.color.darker_gray)
                        .into(viewHolder.userimage);
            } catch (Exception e) {

            }

            Picasso.with(context)
                    .load(m.getParseFile("image").getUrl())
                    .placeholder(android.R.drawable.ic_menu_camera)
                    .error(android.R.drawable.ic_menu_camera)
                    .resize(displaymetrics.widthPixels, displaymetrics.widthPixels)
                    .transform(new CropSquareTransformation())
                    .into(viewHolder.cover);

        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView cover, userimage;
        TextView name, desc, location;
    }


}