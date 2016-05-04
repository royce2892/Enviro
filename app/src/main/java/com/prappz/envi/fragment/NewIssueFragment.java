package com.prappz.envi.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.prappz.envi.R;
import com.prappz.envi.application.PreferenceManager;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by royce on 03-05-2016.
 */
public class NewIssueFragment extends Fragment {

    ImageView camera;
    TextView report;
    EditText desc;
    // TODO handle pic not take case and empty desc
    boolean photoTaken = false;
    Bitmap bitmap;


    public NewIssueFragment() {
    }

    public static NewIssueFragment newInstance() {
        final NewIssueFragment newIssueFragment = new NewIssueFragment();
        return newIssueFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_issue, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        camera = (ImageView) view.findViewById(R.id.issue);
        desc = (EditText) view.findViewById(R.id.place);
        report = (TextView) view.findViewById(R.id.report);

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
        if (mRequestFileIntent.resolveActivity(getActivity().getPackageManager()) != null) {
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

        Toast.makeText(getActivity(),"Reporting Issue , Please wait",Toast.LENGTH_LONG).show();
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

        issue.put("name", PreferenceManager.getInstance(getActivity()).getString("name"));
        issue.put("lat", PreferenceManager.getInstance(getActivity()).getString("LAT"));
        issue.put("long", PreferenceManager.getInstance(getActivity()).getString("LON"));
        issue.put("city", PreferenceManager.getInstance(getActivity()).getString("CITY"));
        issue.put("zip", PreferenceManager.getInstance(getActivity()).getString("ZIP"));
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
        Toast.makeText(getActivity(),"Issue reported",Toast.LENGTH_LONG).show();
        getActivity().onBackPressed();
    }

}