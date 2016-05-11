package com.prappz.envi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class NewEventFragment extends Fragment {

    EditText etTitle, etDesc, etDate, etTime, etPlace;
    TextView post;

    public NewEventFragment() {
    }

    public static NewEventFragment newInstance() {
        final NewEventFragment newEventFragment = new NewEventFragment();
        return newEventFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_new_event, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etPlace = (EditText) view.findViewById(R.id.etPlace);
        etTitle = (EditText) view.findViewById(R.id.etTitle);
        etDesc = (EditText) view.findViewById(R.id.etDesc);
        etDate = (EditText) view.findViewById(R.id.etDate);
        etTime = (EditText) view.findViewById(R.id.etTime);

        post = (TextView) view.findViewById(R.id.post_event);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postEvent();
            }
        });
    }

    private void postEvent() {

        if (etPlace.getText().toString().trim().replaceAll(" ", "").contentEquals("")) {
            Toast.makeText(getActivity(), "Please enter a valid place", Toast.LENGTH_SHORT).show();
            return;
        } else if (etTitle.getText().toString().trim().replaceAll(" ", "").contentEquals("")) {
            Toast.makeText(getActivity(), "Please enter a valid title", Toast.LENGTH_SHORT).show();
            return;
        } else if (etDesc.getText().toString().trim().replaceAll(" ", "").contentEquals("")) {
            Toast.makeText(getActivity(), "Please enter a valid description", Toast.LENGTH_SHORT).show();
            return;
        } else if (etDate.getText().toString().length() != 5) {
            Toast.makeText(getActivity(), "Please enter a valid date in MM/DD format", Toast.LENGTH_SHORT).show();
            return;
        } else if (etTime.getText().toString().length() != 5) {
            Toast.makeText(getActivity(), "Please enter a valid time in HH:MM format", Toast.LENGTH_SHORT).show();
            return;
        } else {

            String day = etDate.getText().toString().substring(3);
            String month = etDate.getText().toString().substring(0, 2);
            char c = etDate.getText().toString().charAt(2);

            try {
                int mon = Integer.parseInt(month);
                int da = Integer.parseInt(day);
                if (c != '/' && c != '-' && c != '.') {
                    Toast.makeText(getActivity(), "Please enter a valid date in MM/DD format", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mon < 1 || mon > 12) {
                    Toast.makeText(getActivity(), "Please enter a month between 1 - 12 ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (da < 1 || da > 31) {
                    Toast.makeText(getActivity(), "Please enter a day between 1 - 31 ", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException ex) {
                Toast.makeText(getActivity(), "Please enter a valid date in MM/DD format ", Toast.LENGTH_SHORT).show();
                return;
            }


            post.setClickable(false);
            Toast.makeText(getActivity(), "Creating event ...", Toast.LENGTH_SHORT).show();

            ParseObject event = ParseObject.create("Event");
            event.put("eventName", etTitle.getText().toString());
            event.put("place", etPlace.getText().toString());
            event.put("time", etTime.getText().toString());
            event.put("day", etDate.getText().toString().substring(3));
            event.put("month", etDate.getText().toString().substring(0, 2));
            event.put("desc", etDesc.getText().toString());
            event.put("userName", PreferenceManager.getInstance(getActivity()).getString("name"));
            event.put("zip", PreferenceManager.getInstance(getActivity()).getString("ZIP"));
            event.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(getActivity(), "Event saved...", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    } else {
                        Toast.makeText(getActivity(), "Event save failed due to reason " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        post.setClickable(true);
                    }
                }
            });
        }


    }

}
