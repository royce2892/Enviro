package com.prappz.envi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.ParseObject;
import com.prappz.envi.R;

import java.util.List;

/**
 * Created by royce on 14-05-2016.
 */
public class AttListAdapter extends BaseAdapter {

    LayoutInflater mLayoutInflater;
    List<String> members;

    public AttListAdapter(Context context, List<String> event) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.members = event;
    }

    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public Object getItem(int position) {
        return members.get(position);
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

            convertView = mLayoutInflater.inflate(R.layout.row_member, null);
            viewHolder = new ViewHolder();
            viewHolder.name = ((TextView) convertView.findViewById(R.id.row_member_name));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.name.setText((String) getItem(position));

        return convertView;
    }

    public void add(String name) {
        if (!members.contains(name))
            members.add(name);
        notifyDataSetChanged();
    }

    public void remove(String name) {
        if (members.contains(name))
            members.remove(name);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView name;
    }


}
