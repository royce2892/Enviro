package com.prappz.envi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prappz.envi.R;

/**
 * Created by royce on 05-05-2016.
 */
public class NearbyFragment extends Fragment {

    private FragmentTabHost mTabHost;

    public NearbyFragment() {
    }

    public static NearbyFragment newInstance() {
        final NearbyFragment nearbyFragment = new NearbyFragment();
        return nearbyFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_nearby, container, false);

        mTabHost = (FragmentTabHost) root.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("chats").setIndicator("Chats"),
                ChatFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("members").setIndicator("Members"),
                MembersFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("events").setIndicator("Events"),
                EventFragment.class, null);

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
