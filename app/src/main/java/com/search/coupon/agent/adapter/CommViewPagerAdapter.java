package com.search.coupon.agent.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by dell on 2016/11/28.
 *
 */
public class CommViewPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<Fragment> fragmentList;

    public CommViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        this.fragmentList = list;
    }

    @Override
    public Fragment getItem(int arg0) {
        return fragmentList.get(arg0);
    }

    @Override
    public int getCount() {
        return fragmentList.size();

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";

    }
}
