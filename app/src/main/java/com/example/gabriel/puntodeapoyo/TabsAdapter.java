package com.example.gabriel.puntodeapoyo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.gabriel.puntodeapoyo.Fragments.MapFragment;

public class TabsAdapter extends FragmentPagerAdapter{

    public TabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position){
            case 0:
                fragment= new MapFragment();
                break;
                default:
                    fragment=null;
                    break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
