package com.prappz.envi.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.prappz.envi.R;
import com.prappz.envi.application.PreferenceManager;

import java.io.ByteArrayOutputStream;

/**
 * Created by royce on 03-05-2016.
 */
public class NewIssueActivity extends AppCompatActivity {

    ImageView camera;
    TextView report;
    EditText desc;
    // TODO handle pic not take case and empty desc
    boolean photoTaken = false;
    Bitmap bitmap;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_issue);


        camera = (ImageView) findViewById(R.id.issue);
        desc = (EditText) findViewById(R.id.place);
        report = (TextView) findViewById(R.id.report);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportPhoto();
            }
        });
    }


    private void reportPhoto() {

        addImageToParse();
    }

    private void takePhoto() {

        Intent mRequestFileIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (mRequestFileIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(mRequestFileIntent, 34);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            // for image from gallery
            if (requestCode == 34) {
                Bundle extras = data.getExtras();
                bitmap = (Bitmap) extras.get("data");
                camera.setImageBitmap(bitmap);
                //    mGridAdapter.addItem(bitmapList.get(requestCode - 20));

            }
        }
    }

    private void addImageToParse() {

        Toast.makeText(this, "Reporting Issue , Please wait", Toast.LENGTH_LONG).show();
        ParseObject issue = ParseObject.create("Issue");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG, 20, stream);
        byte[] image = stream.toByteArray();

        ParseFile parseFile = new ParseFile("image.png", image);
        parseFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i("IMAGE", "ADDED");
                } else
                    Log.i("IMAGE", "ex" + e.getMessage());

            }
        });

        issue.put("name", PreferenceManager.getInstance(this).getString("name"));
        issue.put("lat", PreferenceManager.getInstance(this).getString("LAT"));
        issue.put("long", PreferenceManager.getInstance(this).getString("LON"));
        issue.put("city", PreferenceManager.getInstance(this).getString("CITY"));
        issue.put("zip", PreferenceManager.getInstance(this).getString("ZIP"));
        issue.put("image", parseFile);
        issue.put("desc", desc.getText().toString());
        issue.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    goBack();
                    Log.i("IMAGE", "SAVED");
                } else
                    Log.i("IMAGE", "ex" + e.getMessage());
            }
        });
    }

    private void goBack() {
        Toast.makeText(this, "Issue reported", Toast.LENGTH_LONG).show();
        onBackPressed();
    }

}