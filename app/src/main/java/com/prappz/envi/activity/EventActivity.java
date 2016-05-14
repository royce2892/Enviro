package com.prappz.envi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.prappz.envi.R;
import com.prappz.envi.adapter.AttListAdapter;
import com.prappz.envi.application.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by royce on 14-05-2016.
 */
public class EventActivity extends AppCompatActivity {

    String id, name;
    ParseObject event;
    ListView attListView;
    AttListAdapter attListAdapter;
    Toolbar toolbar;
    List<String> attendees = new ArrayList<>();
    boolean isAttending = false, isHosting = false;
    TextView day, month, desc, place, ivLogo, host, time, rsvp, empty, att;
    String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    String share;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_event);
        name = PreferenceManager.getInstance(this).getString("name");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        attListView = (ListView) findViewById(R.id.att_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivLogo = (TextView) findViewById(R.id.ivLogo);
        day = (TextView) findViewById(R.id.event_day);
        month = (TextView) findViewById(R.id.event_month);
        desc = (TextView) findViewById(R.id.event_desc);
        place = (TextView) findViewById(R.id.event_place);
        host = (TextView) findViewById(R.id.event_host);
        time = (TextView) findViewById(R.id.event_time);
        att = (TextView) findViewById(R.id.event_att_text);
        rsvp = (TextView) findViewById(R.id.rsvp);
        empty = (TextView) findViewById(R.id.empty);
        rsvp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptEvent();
            }
        });


        id = getIntent().getStringExtra("id");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        query.getInBackground(id, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null && object != null)
                    handleData(object);

            }
        });

    }

    private void acceptEvent() {

        if (isHosting) {
            Toast.makeText(this, "You are hosting this event", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isAttending) {
            attendees.add(name);
            event.put("attendees", attendees);
            event.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(EventActivity.this, "Successfully send ATTEND RSVP", Toast.LENGTH_SHORT).show();
                        rsvp.setText("CANCEL RSVP");
                        isAttending = true;
                        handleAddName(true);
                    } else {
                        Toast.makeText(EventActivity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                    return;
                }
            });
        } else {
            attendees.remove(name);
            event.put("attendees", attendees);
            event.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(EventActivity.this, "Successfully send CANCEL RSVP", Toast.LENGTH_SHORT).show();
                        rsvp.setText("ATTEND");
                        isAttending = false;
                        handleAddName(false);
                    } else {
                        Toast.makeText(EventActivity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                    return;
                }
            });
        }
    }

    private void handleAddName(boolean b) {
        // name added change adapter dynamically
        if (b) {
            if (empty.getVisibility() == View.VISIBLE) {
                empty.setVisibility(View.GONE);
                att.setVisibility(View.VISIBLE);
            }
            attListAdapter.add(name);
        } else {
            if (attendees.size() == 0 && empty.getVisibility() == View.GONE) {
                empty.setVisibility(View.VISIBLE);
                att.setVisibility(View.GONE);
            }
            attListAdapter.remove(name);
        }
    }

    private void handleData(ParseObject object) {

        event = object;
        isAttending = PreferenceManager.getInstance(this).getBoolean(id);


        day.setText(object.getString("day"));
        isHosting = object.getString("userName").contentEquals(name);
        int mon = Integer.parseInt(object.getString("month"));
        month.setText(months[mon - 1]);
        desc.setText(object.getString("desc"));
        place.setText(object.getString("place"));
        ivLogo.setText(object.getString("eventName"));
        host.setText("Hosted by " + object.getString("userName"));
        time.setText("Event starts at " + object.getString("time"));
        share = object.getString("eventName") + " is an event hosted by " +
                object.getString("userName") + " at " + object.getString("place")
                + ". It starts at " + object.getString("time")
                + " on " + day.getText().toString() + " " + month.getText().toString()
                + " and you can RSVP from the Enviro app ." +
                "Download now from https://play.google.com/store/apps/details?id=com.prappz.envi";
        if (object.get("attendees") != null)
            attendees = (List<String>) object.get("attendees");
        isAttending = attendees.contains(name);
        if (isAttending)
            rsvp.setText("CANCEL RSVP");
        attListAdapter = new AttListAdapter(this, attendees);
        attListView.setAdapter(attListAdapter);
        if (attendees.size() != 0) {

            empty.setVisibility(View.GONE);
            att.setVisibility(View.VISIBLE);
        } else {
            empty.setVisibility(View.VISIBLE);
            att.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_push_down_in, R.anim.activity_push_down_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        else if (item.getItemId() == R.id.share) {
            shareIssue();
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareIssue() {
        Toast.makeText(this, "SHARE", Toast.LENGTH_SHORT).show();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, share);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Share Event"));
    }
}
