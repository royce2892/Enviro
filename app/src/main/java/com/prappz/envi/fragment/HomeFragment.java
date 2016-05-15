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
import com.prappz.envi.activity.IssueActivity;
import com.prappz.envi.activity.NewIssueActivity;
import com.prappz.envi.adapter.IssueListAdapter;

import java.util.List;

/**
 * Created by royce on 03-05-2016.
 */
public class HomeFragment extends Fragment {

    ListView issueListView;
    IssueListAdapter issueListAdapter;
    FloatingActionButton floatingActionButton;

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
        issueListView = (ListView) view.findViewById(R.id.issue_list);

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), NewIssueActivity.class));
                getActivity().overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_scale);
            }
        });

        loadIssues();
    }

    private void loadIssues() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Issue");
        query.whereNotEqualTo("isClosed", true);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() != 0)
                    setList(objects);

            }
        });

    }

    private void setList(final List<ParseObject> objects) {

        issueListAdapter = new IssueListAdapter(getActivity(), objects);
        issueListView.setAdapter(issueListAdapter);

        issueListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gotoIssue(objects.get(position));
            }
        });
    }

    private void gotoIssue(ParseObject parseObject) {

        Intent issue = new Intent(getActivity(), IssueActivity.class);
        issue.putExtra("name", parseObject.getString("name"));
        issue.putExtra("city", parseObject.getString("city"));
        issue.putExtra("url", parseObject.getParseFile("image").getUrl());
        issue.putExtra("desc", parseObject.getString("desc"));
        issue.putExtra("zip", parseObject.getString("zip"));
        issue.putExtra("lat", parseObject.getString("lat"));
        issue.putExtra("long", parseObject.getString("long"));
        issue.putExtra("user_pic", parseObject.getString("pic"));
        issue.putExtra("id", parseObject.getObjectId());
        getActivity().startActivity(issue);
        getActivity().overridePendingTransition(R.anim.activity_push_up_in, R.anim.activity_push_up_out);
    }

}
