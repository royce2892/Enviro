package com.prappz.envi.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.prappz.envi.R;
import com.prappz.envi.adapter.ClosedIssueAdapter;
import com.prappz.envi.adapter.EventListAdapter;
import com.prappz.envi.application.PreferenceManager;

import java.util.List;

/**
 * Created by royce on 14-05-2016.
 */
public class ClosedActivity extends AppCompatActivity {

    ListView listView;
    ClosedIssueAdapter closedIssueAdapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_closed);
        listView = (ListView) findViewById(R.id.issue_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setData();
    }

    private void setData() {
        ParseQuery<ParseObject> userParseQuery = ParseQuery.getQuery("Close");
        userParseQuery.whereEqualTo("userName", PreferenceManager.getInstance(this).getString("name"));
//        userParseQuery.whereEqualTo("zip", PreferenceManager.getInstance(getActivity()).getString("ZIP"));
        // todo add the above code to restrict messages by ZIP CODE
        userParseQuery.setLimit(20);
        userParseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    closedIssueAdapter = new ClosedIssueAdapter(ClosedActivity.this, objects);
                    listView.setAdapter(closedIssueAdapter);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_push_down_in, R.anim.activity_push_down_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
