package com.prappz.envi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.prappz.envi.R;
import com.prappz.envi.activity.EventActivity;
import com.prappz.envi.activity.NewEventActivity;
import com.prappz.envi.adapter.EventListAdapter;

import java.util.List;

/**
 * Created by royce on 05-05-2016.
 */
public class EventFragment extends Fragment {

    FloatingActionButton floatingActionButton;
    ListView listView;
    EventListAdapter mEventListAdapter;
    List<ParseObject> events;

    public EventFragment() {
    }

    public static EventFragment newInstance() {
        final EventFragment eventFragment = new EventFragment();
        return eventFragment;
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
                getActivity().startActivity(new Intent(getActivity(), NewEventActivity.class));
                getActivity().overridePendingTransition(R.anim.activity_push_up_in,R.anim.activity_push_up_out);
            }
        });

        listView = (ListView) view.findViewById(R.id.event_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(),EventActivity.class).putExtra("id",events.get(position).getObjectId()));
                getActivity().overridePendingTransition(R.anim.activity_push_up_in,R.anim.activity_push_up_out);
            }
        });
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
                    events = objects;
                    mEventListAdapter = new EventListAdapter(getActivity(), objects);
                    listView.setAdapter(mEventListAdapter);
                }
            }
        });
    }

}
