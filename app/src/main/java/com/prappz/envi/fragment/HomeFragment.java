package com.prappz.envi.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.prappz.envi.ClickedView;
import com.prappz.envi.R;
import com.prappz.envi.adapter.IssueListAdapter;

import java.util.List;

/**
 * Created by royce on 03-05-2016.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    ClickedView clickedView;
    RelativeLayout newIssue, leaderboard, nearby;
    ListView issueListView;
    IssueListAdapter issueListAdapter;


    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        final HomeFragment homeFragment = new HomeFragment();
        return homeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newIssue = (RelativeLayout) view.findViewById(R.id.new_fish_layout);
        leaderboard = (RelativeLayout) view.findViewById(R.id.lead_layout);
        nearby = (RelativeLayout) view.findViewById(R.id.nearby_layout);
        issueListView = (ListView) view.findViewById(R.id.issue_list);

        loadIssues();
        newIssue.setOnClickListener(this);
        leaderboard.setOnClickListener(this);
        nearby.setOnClickListener(this);
    }

    private void loadIssues() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Issue");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() != 0)
                    setList(objects);

            }
        });

    }

    private void setList(List<ParseObject> objects) {

        issueListAdapter = new IssueListAdapter(getActivity(), objects);
        issueListView.setAdapter(issueListAdapter);
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

    @Override
    public void onClick(View v) {
        clickedView.clicked(v.getId());
    }
}
