package com.prappz.envi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.prappz.envi.R;
import com.prappz.envi.application.PreferenceManager;

import java.util.List;

/**
 * Created by royce on 05-05-2016.
 */
public class MessageListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<ParseObject> messageList;
    private String selfName;

    public MessageListAdapter(List<ParseObject> messageList, Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.messageList = messageList;
        selfName = PreferenceManager.getInstance(context).getString("name");
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getString("from").contentEquals(selfName))
            return 1;
        else
            return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        int direction = getItemViewType(position);
        if (convertView == null) {
            int res = 0;
            if (direction == 1)
                res = R.layout.row_sent;
            else
                res = R.layout.row_received;
            convertView = mInflater.inflate(res, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.txtView_sender_name);
            viewHolder.content = (TextView) convertView.findViewById(R.id.txtView_message);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ParseObject message = messageList.get(position);
        viewHolder.content.setText(message.getString("content"));
        viewHolder.name.setText(message.getString("from"));
//        time.setText(message.getTime().substring(0, 3) + ", " + message.getTime().substring(11, 16));
        return convertView;
    }

    static class ViewHolder {
        TextView name, content;
    }

    public void add(ParseObject message) {
        messageList.add(message);
        notifyDataSetChanged();
    }
}