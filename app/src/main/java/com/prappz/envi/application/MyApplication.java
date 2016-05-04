package com.prappz.envi.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by RRaju on 7/22/2015.
 */
public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "uKinqzEilP0fHIRDScMKZHuXATsJo9KG9Iv8PvXa", "C2SqyKV1pHQyS7SNEjAfNr0q9aR0LpvzN8j1K69E");
        ParseFacebookUtils.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseUser.enableAutomaticUser();

        if (PreferenceManager.getInstance(this).getBoolean(TrnqlAppConstants.HAS_SIGNED_UP))
            ParsePush.subscribeInBackground("p"+PreferenceManager.getInstance(this).getString("id"), new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    Log.i("PUSH", e + "");
                }
            });

    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
