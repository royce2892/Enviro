package com.prappz.envi.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.prappz.envi.R;
import com.prappz.envi.application.PreferenceManager;
import com.prappz.envi.utils.CircleTransformation;
import com.squareup.picasso.Picasso;

/**
 * Created by royce on 12-05-2016.
 */
public class IssueActivity extends AppCompatActivity {

    TextView name, desc, map, close, ivLogo, loc;
    ImageView issue,userPic;
    Toolbar toolbar;

    String s_name, s_desc, s_city, s_url, lat, lon, id, usr_pic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_issue);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivLogo = (TextView) findViewById(R.id.ivLogo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        issue = (ImageView) findViewById(R.id.issue);
        name = (TextView) findViewById(R.id.issue_name);
        loc = (TextView) findViewById(R.id.issue_place);
        desc = (TextView) findViewById(R.id.issue_desc);
        map = (TextView) findViewById(R.id.map);
        close = (TextView) findViewById(R.id.close);
        userPic = (ImageView) findViewById(R.id.user_image);
        ivLogo.setText("Issue #" + getIntent().getStringExtra("id"));


      /*  issue.putExtra("name", parseObject.getString("name"));
        issue.putExtra("city", parseObject.getString("city"));
        issue.putExtra("url", parseObject.getParseFile("image").getUrl());
        issue.putExtra("desc", parseObject.getString("desc"));
        issue.putExtra("zip", parseObject.getString("zip"));
        issue.putExtra("lat", parseObject.getString("lat"));
        issue.putExtra("long", parseObject.getString("long"));*/

        s_name = getIntent().getStringExtra("name");
        usr_pic = getIntent().getStringExtra("user_pic");
        s_desc = getIntent().getStringExtra("desc");
        s_url = getIntent().getStringExtra("url");
        s_city = getIntent().getStringExtra("city");
        lat = getIntent().getStringExtra("lat");
        lon = getIntent().getStringExtra("long");
        id = getIntent().getStringExtra("id");

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMap();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeIssue();
            }


        });
        loc.setText(s_city);
        name.setText(s_name);
        desc.setText(s_desc);
        Picasso.with(this).load(s_url).into(issue);
        Picasso.with(this)
                .load(usr_pic)
                .placeholder(android.R.color.darker_gray)
                .transform(new CircleTransformation())
                .error(android.R.color.darker_gray)
                .into(userPic);

    }

    private void closeIssue() {

        if (s_name.contentEquals(PreferenceManager.getInstance(this).getString("name")))
            askConfiDialog();
        else {
            ParseObject close = ParseObject.create("Close");
            close.put("eventId", id);
            close.put("userName", s_name);
            close.put("url", s_url);
            close.put("isActive", true);
            close.put("fromName", PreferenceManager.getInstance(this).getString("name"));
            close.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(IssueActivity.this, "Request sent to " + s_name + " to verify and close the issue", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void askConfiDialog() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Issue");
        query.getInBackground(id, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null && object != null) {
                    object.put("isClosed", true);
                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(IssueActivity.this, "Issue closed, you can re-open from closed issues", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void gotoMap() {
        Uri gmmIntentUri = Uri.parse("geo:" + lat + "," + lon);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
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
        //  Toast.makeText(this, "SHARE", Toast.LENGTH_SHORT).show();
        // Get access to the URI for the bitmap
       /* Drawable mDrawable = issue.getDrawable();
        Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();

        String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                mBitmap, "Image Description", null);

        Uri uri = Uri.parse(path);
        if (uri != null) {*/
        // Construct a ShareIntent with link to image
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "An issue was reported by " + s_name + " from " + s_city + " via Enviro App .Please find the image" +
                " at " + s_url);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Share Issue"));

    }
}
