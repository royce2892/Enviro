package com.prappz.envi.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.prappz.envi.R;
import com.prappz.envi.application.PreferenceManager;

/**
 * Created by royce on 07-05-2016.
 */
public class NewEventActivity extends AppCompatActivity {

    EditText etTitle, etDesc, etDate, etTime, etPlace;
    TextView post;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_event);


        etPlace = (EditText) findViewById(R.id.etPlace);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etDesc = (EditText) findViewById(R.id.etDesc);
        etDate = (EditText) findViewById(R.id.etDate);
        etTime = (EditText) findViewById(R.id.etTime);

        post = (TextView) findViewById(R.id.post_event);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postEvent();
            }
        });
    }

    private void postEvent() {

        if (etPlace.getText().toString().trim().replaceAll(" ", "").contentEquals("")) {
            Toast.makeText(this, "Please enter a valid place", Toast.LENGTH_SHORT).show();
            return;
        } else if (etTitle.getText().toString().trim().replaceAll(" ", "").contentEquals("")) {
            Toast.makeText(this, "Please enter a valid title", Toast.LENGTH_SHORT).show();
            return;
        } else if (etDesc.getText().toString().trim().replaceAll(" ", "").contentEquals("")) {
            Toast.makeText(this, "Please enter a valid description", Toast.LENGTH_SHORT).show();
            return;
        } else if (etDate.getText().toString().length() != 5) {
            Toast.makeText(this, "Please enter a valid date in MM/DD format", Toast.LENGTH_SHORT).show();
            return;
        } else if (etTime.getText().toString().length() != 5) {
            Toast.makeText(this, "Please enter a valid time in HH:MM format", Toast.LENGTH_SHORT).show();
            return;
        } else {

            String day = etDate.getText().toString().substring(3);
            String month = etDate.getText().toString().substring(0, 2);
            char c = etDate.getText().toString().charAt(2);

            try {
                int mon = Integer.parseInt(month);
                int da = Integer.parseInt(day);
                if (c != '/' && c != '-' && c != '.') {
                    Toast.makeText(this, "Please enter a valid date in MM/DD format", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mon < 1 || mon > 12) {
                    Toast.makeText(this, "Please enter a month between 1 - 12 ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (da < 1 || da > 31) {
                    Toast.makeText(this, "Please enter a day between 1 - 31 ", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException ex) {
                Toast.makeText(this, "Please enter a valid date in MM/DD format ", Toast.LENGTH_SHORT).show();
                return;
            }


            post.setClickable(false);
            Toast.makeText(this, "Creating event ...", Toast.LENGTH_SHORT).show();

            ParseObject event = ParseObject.create("Event");
            event.put("eventName", etTitle.getText().toString());
            event.put("place", etPlace.getText().toString());
            event.put("time", etTime.getText().toString());
            event.put("day", etDate.getText().toString().substring(3));
            event.put("month", etDate.getText().toString().substring(0, 2));
            event.put("desc", etDesc.getText().toString());
            event.put("userName", PreferenceManager.getInstance(this).getString("name"));
            event.put("zip", PreferenceManager.getInstance(this).getString("ZIP"));
            event.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(NewEventActivity.this, "Event saved...", Toast.LENGTH_SHORT).show();
                        NewEventActivity.this.onBackPressed();
                    } else {
                        Toast.makeText(NewEventActivity.this, "Event save failed due to reason " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        post.setClickable(true);
                    }
                }
            });
        }


    }

}
