package com.prappz.envi.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Rraju on 7/25/2015.
 */
public class PreferenceManager {

    SharedPreferences preferences;
    private static PreferenceManager sInstance;

    public PreferenceManager(Context context) {
        preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
    }

    public static synchronized PreferenceManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferenceManager(context);
        }
        return sInstance;
    }

    public void put(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void put(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void put(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void putSilentLatLong(String key, String lat, String lng) {

        put(key + "lat", lat);
        put(key + "lng", lng);
    }

    public String getString(String key) {
        return preferences.getString(key, "default");
    }

    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    public int getInt(String key) {
        return preferences.getInt(key, 0);
    }

    public void putToSilentify(String value) {

        SharedPreferences.Editor editor = preferences.edit();
        Set<String> silentify = preferences.getStringSet("silentify_set", null);
        if (silentify == null)
            silentify = new HashSet<String>();

        silentify.add(value);
        editor.putStringSet("silentify_set", silentify);
        editor.commit();

    }

    public Set<String> getSilentify() {

        return preferences.getStringSet("silentify_set", null);
    }

    public void removeFromSilentify(String key) {

        Log.i("SMARTY-REMOVAL", key);
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> silentify = preferences.getStringSet("silentify_set", null);

        silentify.remove(key);

        editor.putStringSet("silentify_set", silentify);
        editor.remove(key + "lat");
        editor.remove(key + "lng");
        editor.remove(key + "enabled");
        editor.commit();
    }


    public void increment(String key) {
        int curr = getInt(key);
        put(key, ++curr);
    }

}
