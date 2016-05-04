package com.prappz.envi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.prappz.envi.R;
import com.prappz.envi.adapter.LeadListAdapter;

import java.util.List;

/**
 * Created by royce on 04-05-2016.
 */
public class LeaderboardFragment extends Fragment {

    ListView listView;
    LeadListAdapter leadListAdapter;
    public LeaderboardFragment() {
    }

    public static LeaderboardFragment newInstance() {
        final LeaderboardFragment leaderboardFragment = new LeaderboardFragment();
        return leaderboardFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_leaderboard,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (ListView) view.findViewById(R.id.lead_list);
        setData();
    }

    private void setData() {

        ParseQuery<ParseUser> userParseQuery = ParseUser.getQuery();
        userParseQuery.addDescendingOrder("score");
        userParseQuery.setLimit(20);
        userParseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null) {
                    leadListAdapter = new LeadListAdapter(getActivity(),objects);
                    listView.setAdapter(leadListAdapter);
                }
            }
        });
    }
}
