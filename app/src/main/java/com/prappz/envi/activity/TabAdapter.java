package com.prappz.envi.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.prappz.envi.fragment.ChatFragment;
import com.prappz.envi.fragment.EventFragment;
import com.prappz.envi.fragment.HomeFragment;
import com.prappz.envi.fragment.LeaderboardFragment;
import com.prappz.envi.fragment.NearbyFragment;

/**
 * Created by nithin on 11/5/16.
 */
public class TabAdapter extends FragmentPagerAdapter {

    private SparseArray<Fragment> array = new SparseArray<>();

    public TabAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        Log.i("location", "getitem of tabadapter");
        Fragment item = null;
        switch (arg0) {
            case 0:
                item = HomeFragment.newInstance();
                break;
            case 1:
                item = EventFragment.newInstance();
                break;
            case 2:
                item = ChatFragment.newInstance();
                break;
            case 3:
                item = LeaderboardFragment.newInstance();
                break;
            default:
                item = null;
                break;
        }
        return item;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // TODO Auto-generated method stub
        String title = null;
        switch (position) {
            case 0:
                title = "Issue";
                break;
            case 1:
                title = "Event";
                break;
            case 2:
                title = "Chat";
                break;
            case 3:
                title = "Top";
                break;

            default:
                title = "Ripple";
                break;
        }
        return title;
    }

    @Override
    public Object instantiateItem(ViewGroup arg0, int arg1) {
        // TODO Auto-generated method stub
        Fragment fragment = (Fragment) super.instantiateItem(arg0, arg1);
        array.put(arg1, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        array.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getFragment(int position) {
        Log.i("position", "getfragment called");
        return array.get(position);
    }
}