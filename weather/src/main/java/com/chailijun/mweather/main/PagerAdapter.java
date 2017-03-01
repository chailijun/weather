package com.chailijun.mweather.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


public class PagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;

    public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    public void setNewData(List<Fragment> mFragments) {
        this.mFragments = mFragments;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {

        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

}
