package com.search.coupon.agent.ui.fragment;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioGroup;

import com.search.coupon.agent.R;
import com.search.coupon.agent.adapter.LoanOrderPagerAdapter;
import com.search.coupon.agent.common.BaseFragment;
import com.search.coupon.agent.common.Constants;
import com.search.coupon.agent.service.MessageEvent;
import com.search.coupon.agent.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 贷款订单（全部、审核中、办理中、已放款、已取消）
 */
public class LoanBillFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener, View.OnTouchListener {

    private Unbinder unbinder;
    private ViewPager mViewPager;
    private RadioGroup mRadioGroup;
    private int currentItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loan_bill, container, false);
        initView(view);
        initData();
        return view;
    }

    /**
     * 初始化控件 用ButterKnife 简约
     */
    private void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        mViewPager = view.findViewById(R.id.vp_loan_order);
        List<Fragment> fragments = new ArrayList<>(5);
        fragments.add(new LoanOrderListFragment());
        fragments.add(new HandleFragment());
        fragments.add(new SignFragment());
        fragments.add(new AlreadyReleasedFragment());
        fragments.add(new HaveBeenCancelledFragment());
        mViewPager.setAdapter(new LoanOrderPagerAdapter(getFragmentManager(), fragments));
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.addOnPageChangeListener(this);
        mRadioGroup = view.findViewById(R.id.rg_tabs);
        mRadioGroup.setOnCheckedChangeListener(this);
        mRadioGroup.check(R.id.rb_all);
        mViewPager.setOnTouchListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {

    }

    @Override
    public void onClickEvent(View view) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentItem = position;
        switch (position) {
            case 0:
                mRadioGroup.check(R.id.rb_all);
                break;

            case 1:
                mRadioGroup.check(R.id.rb_progress);
                break;

            case 2:
                mRadioGroup.check(R.id.rb_sign);
                break;

            case 3:
                mRadioGroup.check(R.id.rb_got_money);
                break;

            case 4:
                mRadioGroup.check(R.id.rb_cancel);
                break;

            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_all:
                mViewPager.setCurrentItem(0);
                break;

            case R.id.rb_progress:
                mViewPager.setCurrentItem(1);
                break;

            case R.id.rb_sign:
                mViewPager.setCurrentItem(2);
                break;

            case R.id.rb_got_money:
                mViewPager.setCurrentItem(3);
                break;

            case R.id.rb_cancel:
                mViewPager.setCurrentItem(4);
                break;

            default:
                break;
        }
    }

    float startX;
    float startY;
    float endX;
    float endY;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;

            case MotionEvent.ACTION_UP:
                endX = event.getX();
                endY = event.getY();
                WindowManager windowManager = (WindowManager) getActivity().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                //获取屏幕的宽度
                Point size = new Point();
                windowManager.getDefaultDisplay().getSize(size);
                int width = size.x;
                LogUtils.e("currentItem=====" + currentItem + "startX=====" + startX + "endX=====" + endX + "width=====" + width);
                //首先要确定的是，是否到了最后一页，然后判断是否向左滑动，并且滑动距离是否符合，我这里的判断距离是屏幕宽度的4分之一（这里可以适当控制）
                if (currentItem == 0 && endX - startX > 0 && endX - startX >= (width / 5)) {
                    EventBus.getDefault().post(new MessageEvent(Constants.JUMP_TO_PRICE_LIST));
                }
                break;

        }
        return false;
    }


}

