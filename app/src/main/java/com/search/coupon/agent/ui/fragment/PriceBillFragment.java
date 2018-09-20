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
import android.widget.TextView;

import com.search.coupon.agent.R;
import com.search.coupon.agent.adapter.PriceBillPagerAdapter;
import com.search.coupon.agent.common.BaseFragment;
import com.search.coupon.agent.common.Constants;
import com.search.coupon.agent.service.MessageEvent;
import com.search.coupon.agent.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 询价订单（全部、询价中、询价完成、询价终止）
 * Created by song on 2018/2/11
 */
public class PriceBillFragment extends BaseFragment implements View.OnTouchListener {

    private Unbinder unbinder;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tv_all)
    TextView tv_all;
    @BindView(R.id.tv_progress)
    TextView tv_progress;
    @BindView(R.id.tv_complete)
    TextView tv_complete;
    @BindView(R.id.tv_stop)
    TextView tv_stop;

    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private BillAllFragment allFragment; // 全部
    private BillUnpaidFragment progressFragment; // 询价中
    private BillProgressFragment completeFragment; // 询价完成
    private BillCompleteFragment stopFragment; // 询价终止

    public static final int ALL = 0;
    public static final int PROGRESS = 1;
    public static final int COMPLETE = 2;
    public static final int STOP = 3;
    private int currentItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_price_bill, container, false);
        initView(view);
        initData();
        return view;
    }

    /**
     * 初始化控件 用ButterKnife 简约
     */
    private void initView(View view) {
        //一定要解绑 在onDestroyView里
        unbinder = ButterKnife.bind(this, view);

        allFragment = new BillAllFragment();                    //全部
        progressFragment = new BillUnpaidFragment();          //待支付
        completeFragment = new BillProgressFragment();        //审核中
        stopFragment = new BillCompleteFragment();          //审核完成

        mFragments.add(allFragment);
        mFragments.add(progressFragment);
        mFragments.add(completeFragment);
        mFragments.add(stopFragment);

        /**
         * 初始化Adapter
         */
        PriceBillPagerAdapter mAdapter = new PriceBillPagerAdapter(getActivity().getSupportFragmentManager(), mFragments);
        viewpager.setOffscreenPageLimit(3);                               // 使ViewPager至少保持两个页面不被销毁
        viewpager.setAdapter(mAdapter);
        viewpager.setOnPageChangeListener(new MyOnPageChangeListener()); // 设置页面滑动监听
        viewpager.setOnTouchListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // tvTitle.setText("账单");

    }

    @OnClick({R.id.tv_all, R.id.tv_progress, R.id.tv_complete, R.id.tv_stop})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.tv_all:
                viewpager.setCurrentItem(ALL);
                setTextColor(ALL);
                break;

            case R.id.tv_progress:
                viewpager.setCurrentItem(PROGRESS);
                setTextColor(PROGRESS);
                break;

            case R.id.tv_complete:
                viewpager.setCurrentItem(COMPLETE);
                setTextColor(COMPLETE);
                break;

            case R.id.tv_stop:
                viewpager.setCurrentItem(STOP);
                setTextColor(STOP);
                break;

            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /***
     * ViewPager 滑动页面监听
     */
    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int state) {
            // ViewPager滑动状态改变的回调
        }

        @Override
        public void onPageScrolled(int index, float offset, int offsetPx) {
            // ViewPager滑动时的回调

        }

        @Override
        public void onPageSelected(int index) {
            // ViewPager页面被选中的回调
            setTextColor(index);
            currentItem = index;
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
                if (currentItem == 3 && startX - endX > 0 && startX - endX >= (width / 5)) {
                    LogUtils.e("进入了触摸");
                    EventBus.getDefault().post(new MessageEvent(Constants.JUMP_TO_LOAN_LIST));
                }
                break;

        }
        return false;
    }

    /***
     * 设置字体颜色
     */
    public void setTextColor(int index) {
        switch (index) {
            case ALL:
                tv_all.setTextColor(getResources().getColor(R.color.color_3F3F3F));
                tv_progress.setTextColor(getResources().getColor(R.color.color_8A97B0));
                tv_complete.setTextColor(getResources().getColor(R.color.color_8A97B0));
                tv_stop.setTextColor(getResources().getColor(R.color.color_8A97B0));
                break;

            case PROGRESS:
                tv_all.setTextColor(getResources().getColor(R.color.color_8A97B0));
                tv_progress.setTextColor(getResources().getColor(R.color.color_3F3F3F));
                tv_complete.setTextColor(getResources().getColor(R.color.color_8A97B0));
                tv_stop.setTextColor(getResources().getColor(R.color.color_8A97B0));
                break;

            case COMPLETE:
                tv_all.setTextColor(getResources().getColor(R.color.color_8A97B0));
                tv_progress.setTextColor(getResources().getColor(R.color.color_8A97B0));
                tv_complete.setTextColor(getResources().getColor(R.color.color_3F3F3F));
                tv_stop.setTextColor(getResources().getColor(R.color.color_8A97B0));
                break;

            case STOP:
                tv_all.setTextColor(getResources().getColor(R.color.color_8A97B0));
                tv_progress.setTextColor(getResources().getColor(R.color.color_8A97B0));
                tv_complete.setTextColor(getResources().getColor(R.color.color_8A97B0));
                tv_stop.setTextColor(getResources().getColor(R.color.color_3F3F3F));
                break;

            default:
                break;

        }

    }

}
