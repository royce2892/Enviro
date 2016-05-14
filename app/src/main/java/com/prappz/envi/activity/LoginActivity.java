package com.prappz.envi.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.prappz.envi.R;
import com.prappz.envi.application.PreferenceManager;
import com.prappz.envi.application.TrnqlAppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by nithin on 17-11-2015.
 */
public class LoginActivity extends Activity {

    private static final String TAG = "Login";
    ProgressDialog progressDialog;
    Button mFb;
    LinearLayout intro;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (PreferenceManager.getInstance(this).getBoolean(TrnqlAppConstants.HAS_SIGNED_UP))
            skipToMain();
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        mFb = (Button) findViewById(R.id.fb);
        intro = (LinearLayout) findViewById(R.id.intro);

        /*float fbIconScale = 2.0F;
        Drawable drawable = this.getResources().getDrawable(
                com.facebook.R.drawable.com_facebook_button_icon);
        drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * fbIconScale),
                (int) (drawable.getIntrinsicHeight() * fbIconScale));
        mFb.setCompoundDrawables(drawable, null, null, null);
        mFb.setCompoundDrawablePadding(this.getResources().
                getDimensionPixelSize(R.dimen.fb_margin_override_textpadding));
        mFb.setPadding(mFb.getResources().getDimensionPixelSize(R.dimen.fb_margin_override_lr), mFb.getResources().getDimensionPixelSize(R.dimen.fb_margin_override_top),
                mFb.getResources().getDimensionPixelSize(R.dimen.fb_margin_override_lr),
                this.getResources().getDimensionPixelSize(
                        R.dimen.fb_margin_override_bottom));*/

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    public void onLoginClick(View v) {
        progressDialog = ProgressDialog.show(LoginActivity.this, "", "Logging in...", true);

        List<String> permissions = Arrays.asList("public_profile", "email");

        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                progressDialog.dismiss();
                if (user == null) {
                    Log.i(TAG, "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.i(TAG, "User signed up and logged in through Facebook!");
                    makeMeRequest();
                } else {
                    Log.i(TAG, "User logged in through Facebook!");
                    makeMeRequest();

                }
            }
        });
    }

    private void makeMeRequest() {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Please wait while we log you in");
        dialog.show();

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                        if (jsonObject != null) {
                            JSONObject userProfile = new JSONObject();
                            Log.i(TAG, jsonObject + "");

                            try {
                                saveAndCreateOnParse(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                userProfile.put("facebookId", jsonObject.getLong("id"));
                                userProfile.put("name", jsonObject.getString("name"));

                                if (jsonObject.getString("gender") != null)
                                    userProfile.put("gender", jsonObject.getString("gender"));

                                if (jsonObject.getString("email") != null)
                                    userProfile.put("email", jsonObject.getString("email"));

                                // Save the user profile info in a user property
                                ParseUser currentUser = ParseUser.getCurrentUser();
                                currentUser.put("profile", userProfile);
                                currentUser.saveInBackground();

                                // Show the user info
                                //updateViewsWithProfileInfo();
                            } catch (JSONException e) {
                                Log.i(TAG,
                                        "Error parsing returned user data. " + e);
                            }
                        } else if (graphResponse.getError() != null) {
                            switch (graphResponse.getError().getCategory()) {
                                case LOGIN_RECOVERABLE:
                                    Log.i(TAG,
                                            "Authentication error: " + graphResponse.getError());
                                    break;

                                case TRANSIENT:
                                    Log.i(TAG,
                                            "Transient error. Try again. " + graphResponse.getError());
                                    break;

                                case OTHER:
                                    Log.i(TAG,
                                            "Some other error: " + graphResponse.getError());
                                    break;
                            }
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,gender,name,picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void saveAndCreateOnParse(JSONObject jsonObject) throws JSONException {

        String id, email = null, name, url, gender = null;

        id = String.valueOf(jsonObject.getLong("id"));

        ParsePush.subscribeInBackground("p" + id);
        name = jsonObject.getString("name");

        if (jsonObject.getString("gender") != null)
            gender = jsonObject.getString("gender");

        if (jsonObject.getString("email") != null)
            email = jsonObject.getString("email");

        ParseUser user = ParseUser.getCurrentUser();
        user.put("id", id);
        user.put("name", name);
        user.put("score", 300);

        if (email != null)
            user.put("email", email);
        if (gender != null)
            user.put("gender", gender);
        user.put("url", jsonObject.getJSONObject("picture").getJSONObject("data").getString("url").replaceAll("\\\\", ""));

        PreferenceManager.getInstance(this).put("name", name);
        PreferenceManager.getInstance(this).put("id", id);
        PreferenceManager.getInstance(this).put("url", jsonObject.getJSONObject("picture").getJSONObject("data").getString("url").replaceAll("\\\\", ""));

        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.i(TAG, e + "");
                if (e == null)
                    PreferenceManager.getInstance(LoginActivity.this).put(TrnqlAppConstants.HAS_SIGNED_UP, true);
                skipToMain();
            }
        });

    }

    private void skipToMain() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
      }


}
