package com.prappz.envi.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.prappz.envi.R;
import com.squareup.picasso.Picasso;

/**
 * Created by royce on 12-05-2016.
 */
public class IssueActivity extends AppCompatActivity {

    TextView name, desc, share, close;
    ImageView issue;
    TextView toolbar;

    String s_name, s_desc, s_city, s_url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_issue);

        toolbar = (TextView) findViewById(R.id.ivLogo);

        issue = (ImageView) findViewById(R.id.issue);
        name = (TextView) findViewById(R.id.issue_reporter_name);
        desc = (TextView) findViewById(R.id.issue_desc);
        share = (TextView) findViewById(R.id.share);
        close = (TextView) findViewById(R.id.close);

        toolbar.setText("Issue #"+getIntent().getStringExtra("id"));

      /*  issue.putExtra("name", parseObject.getString("name"));
        issue.putExtra("city", parseObject.getString("city"));
        issue.putExtra("url", parseObject.getParseFile("image").getUrl());
        issue.putExtra("desc", parseObject.getString("desc"));
        issue.putExtra("zip", parseObject.getString("zip"));
        issue.putExtra("lat", parseObject.getString("lat"));
        issue.putExtra("long", parseObject.getString("long"));*/

        s_name = getIntent().getStringExtra("name");
        s_desc = getIntent().getStringExtra("desc");
        s_url = getIntent().getStringExtra("url");
        s_city = getIntent().getStringExtra("city");

        name.setText("Reported by " + s_name + " from " + s_city);
        desc.setText(s_desc);
        Picasso.with(this).load(s_url).into(issue);

    }
}
