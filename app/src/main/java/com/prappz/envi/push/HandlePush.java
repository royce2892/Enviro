
package com.prappz.envi.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.SaveCallback;
import com.prappz.envi.R;
import com.prappz.envi.activity.MainActivity;
import com.prappz.envi.application.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by RRaju on 6/10/2015.
 */

public class HandlePush extends ParsePushBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ParsePushBroadcastReceiver.ACTION_PUSH_RECEIVE)) {
            try {
                JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                analyzeJson(json, context);
            } catch (JSONException e) {
            }
        }
    }

    private void analyzeJson(final JSONObject json, final Context context) {
        try {
            int type = json.getInt("type");
            Log.i("****$$$$$$$$$******",json+"");
            //0 for ride request
            if (type == 0) {
                String content = json.getString("seats");
                String from = json.getString("from");
                String source = json.getString("source");
                String desti = json.getString("desti");
                String url = json.getString("url");
                String id = json.getString("id");
                createRequest(id, from, type, url, source, desti,content,json.getString("phone"));

                generate_notification(context, from + " sent a seat share request");
            }
            //1 for accept
            else if (type == 1) {
                String from = json.getString("from");
                String source = json.getString("source");
                String desti = json.getString("desti");
                String url = json.getString("url");
                String id = json.getString("id");

                createRequest(id, from, type, url, source, desti, null, json.getString("phone"));
                //createRequest(id, from + " accepted your share request for the ride you posted from " + source + " to " + desti, type, url, source, desti);
                generate_notification(context, from + " accepted your share request");
            }
            //2 for reject
            else if (type == 2) {
                String from = json.getString("from");
                String source = json.getString("source");
                String desti = json.getString("desti");
                String url = json.getString("url");
                String id = json.getString("id");
                createRequest(id, from, type, url, source, desti,null,json.getString("phone"));
                //createRequest(id, from + " rejected your share request for the ride you posted from " + source + " to " + desti, type, url, source, desti);
                generate_notification(context, from + " rejected your share request");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void createRequest(String id, String from,int type, String url, String source, String desti,String seats,String phon) {
        ParseObject a = ParseObject.create("Act");
        a.put("from", from);
        a.put("type", type);
        a.put("url", url);
        a.put("source", source);
        a.put("desti", desti);
        a.put("phone",phon);
        a.put("id", id);
        if (type == 0)
            a.put("status", "On hold");
        else if (type == 1)
            a.put("status", "Accepted");
        else if (type == 2)
            a.put("status", "Rejected");
        if(type == 0)
            a.put("seats",seats);
        a.pinInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.i("ACT", e + "");
            }
        });
    }

    private void generate_notification(Context context, String content) throws JSONException {
        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pintent = PendingIntent.getActivity(context, 0, i, 0);

        Notification notif = new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pintent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .build();

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notif);
    }

}

