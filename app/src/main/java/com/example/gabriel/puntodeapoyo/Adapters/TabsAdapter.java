package com.example.gabriel.puntodeapoyo.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.gabriel.puntodeapoyo.Fragments.ListFragment;
import com.example.gabriel.puntodeapoyo.Fragments.MapFragment;
import com.example.gabriel.puntodeapoyo.R;

/**
 * Created by gabii on 30/12/2017.
 */

public class TabsAdapter extends FragmentStatePagerAdapter {
    Context context;

    public TabsAdapter(android.support.v4.app.FragmentManager fm,Context context) {
        super(fm);
        this.context=context;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment;
        switch(i) {
            case 0:
                fragment = new MapFragment();
                break;
            case 1:
                fragment = new ListFragment();
                break;
            default:
                fragment = null;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.tab_map);
            case 1:
                return context.getString(R.string.tab_list);
        }
        return null;
    }
}
