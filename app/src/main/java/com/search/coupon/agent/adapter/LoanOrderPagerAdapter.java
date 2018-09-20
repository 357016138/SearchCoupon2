package com.search.coupon.agent.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


/**
 * Created by RickBerg on 2018/2/26 0026.
 *
 */

public class LoanOrderPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;

    public LoanOrderPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        mFragments = list;
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
