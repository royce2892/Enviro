package com.prappz.envi.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.prappz.envi.ClickedView;
import com.prappz.envi.R;
import com.prappz.envi.application.PreferenceManager;
import com.prappz.envi.fragment.HomeFragment;
import com.prappz.envi.fragment.LeaderboardFragment;
import com.prappz.envi.fragment.NearbyFragment;
import com.prappz.envi.fragment.NewEventFragment;
import com.prappz.envi.fragment.NewIssueFragment;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ClickedView, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    FragmentManager fragmentManager;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // toolbar = (Toolbar) findViewById(R.id.toolBar);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        HomeFragment fragment = HomeFragment.newInstance();
        fragmentTransaction.add(R.id.frame, fragment);
        fragmentTransaction.addToBackStack("none");
        fragmentTransaction.commit();

        requestPermission();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        20);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void clicked(int id) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (id) {
            case R.id.new_fish_layout:
                NewIssueFragment newIssueFragment = NewIssueFragment.newInstance();
                fragmentTransaction.add(R.id.frame, newIssueFragment);
                break;
            case R.id.lead_layout:
                LeaderboardFragment leaderboardFragment = LeaderboardFragment.newInstance();
                fragmentTransaction.add(R.id.frame, leaderboardFragment);
                break;
            case R.id.nearby_layout:
                NearbyFragment nearbyFragment = NearbyFragment.newInstance();
                fragmentTransaction.add(R.id.frame, nearbyFragment);
                break;
            case R.id.fab:
                NewEventFragment newEventFragment = NewEventFragment.newInstance();
                fragmentTransaction.replace(R.id.frame, newEventFragment);
                break;
        }

        fragmentTransaction.addToBackStack("none");
        fragmentTransaction.commit();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            /*if (mLastLocation != null) {
        //        Log.i("ADDRESS", String.valueOf(mLastLocation.getLatitude()));
         //       Log.i("ADDRESS", String.valueOf(mLastLocation.getLongitude()));

            } else
                Log.i("ADDRESS", "Null");*/

            return;
        } else {
            //       Log.i("ADDRESS", "Permission not there");
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                //         Log.i("ADDRESS", String.valueOf(mLastLocation.getLatitude()));
                //         Log.i("ADDRESS", String.valueOf(mLastLocation.getLongitude()));
                onUpdateLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            } /*else
                Log.i("ADDRESS", "Null");*/
        }


    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("ADDRESS", "Connected suspended" + i + "");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("ADDRESS", "failed" + connectionResult.getErrorMessage());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 20: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {

                }
                return;
            }
        }
    }


    private void onUpdateLocation(final double latitude, final double longitude) {
        PreferenceManager.getInstance(this).put("LAT", latitude + "");
        PreferenceManager.getInstance(this).put("LON", longitude + "");

        new AsyncTask<Void, Void, List<Address>>() {
            @Override
            protected List<Address> doInBackground(Void... params) {
                Geocoder coder = new Geocoder(MainActivity.this);
                try {
                    List<Address> addresses = coder.getFromLocation(latitude, longitude, 1);
                    int nAddressCnt = addresses.size();
                    if (nAddressCnt > 0) {
                        return addresses;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Address> addresses) {
                if (addresses != null)
                    getAddress(addresses);
                super.onPostExecute(addresses);
            }
        }.execute();
    }

    private void getAddress(List<Address> addresses) {
        String Address1 = "";
        String Address2 = "";
        String Country = "";
        String Zip = "";
        String State = "";
        String City = "";


        Address1 = addresses.get(0).getAddressLine(0);
        Address2 = addresses.get(0).getAddressLine(1);
        City = addresses.get(0).getLocality();
        State = addresses.get(0).getAdminArea();
        Country = addresses.get(0).getCountryName();
        Zip = addresses.get(0).getPostalCode();

        Log.i("ADDRESS", Address1 + " ");
        Log.i("ADDRESS", Address2 + " ");
        Log.i("ADDRESS", Zip + " ");
        Log.i("ADDRESS", City + " ");
        Log.i("ADDRESS", State + " ");
        Log.i("ADDRESS", Country + " ");

        PreferenceManager.getInstance(this).put("ZIP", Zip);
        PreferenceManager.getInstance(this).put("CITY", City);
    }


}
