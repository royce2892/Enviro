package com.prappz.envi.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.prappz.envi.ClickedView;
import com.prappz.envi.R;
import com.prappz.envi.adapter.EventListAdapter;
import com.prappz.envi.adapter.MemberListAdapter;
import com.prappz.envi.adapter.MessageListAdapter;
import com.prappz.envi.application.PreferenceManager;

import java.util.List;

/**
 * Created by royce on 05-05-2016.
 */
public class EventFragment extends Fragment {

    FloatingActionButton floatingActionButton;
    ClickedView clickedView;
    ListView listView;
    EventListAdapter mEventListAdapter;

    public EventFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_event, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedView.clicked(v.getId());
            }
        });

        listView = (ListView) view.findViewById(R.id.event_list);
        setData();

    }

    private void setData() {
        ParseQuery<ParseObject> userParseQuery = ParseQuery.getQuery("Event");
//        userParseQuery.whereEqualTo("zip", PreferenceManager.getInstance(getActivity()).getString("ZIP"));
        // todo add the above code to restrict messages by ZIP CODE
        userParseQuery.setLimit(20);
        userParseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    mEventListAdapter = new EventListAdapter(getActivity(),objects);
                    listView.setAdapter(mEventListAdapter);
                }
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            clickedView = (ClickedView) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
}
