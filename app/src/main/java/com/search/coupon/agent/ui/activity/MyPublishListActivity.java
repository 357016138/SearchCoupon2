package com.search.coupon.agent.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.search.coupon.agent.R;
import com.search.coupon.agent.adapter.PriceBillPagerAdapter;
import com.search.coupon.agent.common.BaseActivity;
import com.search.coupon.agent.ui.fragment.BillAllFragment;
import com.search.coupon.agent.ui.fragment.BillFailFragment;
import com.search.coupon.agent.ui.fragment.BillProgressFragment;
import com.search.coupon.agent.ui.fragment.BillUnpaidFragment;
import com.search.coupon.agent.ui.fragment.BillCompleteFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyPublishListActivity extends BaseActivity {

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
    @BindView(R.id.tv_fail)
    TextView tv_fail;


    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private BillAllFragment allFragment;                  // 全部
    private BillUnpaidFragment unpaidFragment;           // 待支付
    private BillProgressFragment progressFragment;      // 审核中
    private BillCompleteFragment completeFragment;     // 审核通过
    private BillFailFragment    failFragment;        // 审核未通过


    public static final int ALL = 0;
    public static final int PROGRESS = 1;
    public static final int COMPLETE = 2;
    public static final int STOP = 3;
    public static final int FAIL = 4;
    private int currentItem;


    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_my_publish_list);
    }

    @Override
    public void dealLogicBeforeInitView() {

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        topBar.setTitle("我的发布");
        topBar.setLineVisible(true);

        allFragment = new BillAllFragment();                     //全部
        unpaidFragment = new BillUnpaidFragment();               //待支付
        progressFragment = new BillProgressFragment();            //审核中
        completeFragment = new BillCompleteFragment();            //审核通过
        failFragment = new BillFailFragment();                  //审核未通过

        mFragments.add(allFragment);
        mFragments.add(unpaidFragment);
        mFragments.add(progressFragment);
        mFragments.add(completeFragment);
        mFragments.add(failFragment);

        /**
         * 初始化Adapter
         */
        PriceBillPagerAdapter mAdapter = new PriceBillPagerAdapter(getSupportFragmentManager(), mFragments);
        viewpager.setOffscreenPageLimit(4);                               // 使ViewPager至少保持两个页面不被销毁
        viewpager.setAdapter(mAdapter);
        viewpager.setOnPageChangeListener(new MyOnPageChangeListener()); // 设置页面滑动监听
//        viewpager.setOnTouchListener(this);



    }

    @Override
    public void dealLogicAfterInitView() {

    }

    @OnClick({R.id.tv_all, R.id.tv_progress, R.id.tv_complete, R.id.tv_stop,R.id.tv_fail})
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
            case R.id.tv_fail:
                viewpager.setCurrentItem(FAIL);
                setTextColor(FAIL);
                break;

            default:
                break;
        }
    }

    @Override
    public void OnTopLeftClick() {
              finish();
    }

    @Override
    public void OnTopRightClick() {

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
                tv_fail.setTextColor(getResources().getColor(R.color.color_8A97B0));
                break;

            case PROGRESS:
                tv_all.setTextColor(getResources().getColor(R.color.color_8A97B0));
                tv_progress.setTextColor(getResources().getColor(R.color.color_3F3F3F));
                tv_complete.setTextColor(getResources().getColor(R.color.color_8A97B0));
                tv_stop.setTextColor(getResources().getColor(R.color.color_8A97B0));
                tv_fail.setTextColor(getResources().getColor(R.color.color_8A97B0));
                break;

            case COMPLETE:
                tv_all.setTextColor(getResources().getColor(R.color.color_8A97B0));
                tv_progress.setTextColor(getResources().getColor(R.color.color_8A97B0));
                tv_complete.setTextColor(getResources().getColor(R.color.color_3F3F3F));
                tv_stop.setTextColor(getResources().getColor(R.color.color_8A97B0));
                tv_fail.setTextColor(getResources().getColor(R.color.color_8A97B0));
                break;

            case STOP:
                tv_all.setTextColor(getResources().getColor(R.color.color_8A97B0));
                tv_progress.setTextColor(getResources().getColor(R.color.color_8A97B0));
                tv_complete.setTextColor(getResources().getColor(R.color.color_8A97B0));
                tv_stop.setTextColor(getResources().getColor(R.color.color_3F3F3F));
                tv_fail.setTextColor(getResources().getColor(R.color.color_8A97B0));
                break;
            case FAIL:
                tv_all.setTextColor(getResources().getColor(R.color.color_8A97B0));
                tv_progress.setTextColor(getResources().getColor(R.color.color_8A97B0));
                tv_complete.setTextColor(getResources().getColor(R.color.color_8A97B0));
                tv_stop.setTextColor(getResources().getColor(R.color.color_8A97B0));
                tv_fail.setTextColor(getResources().getColor(R.color.color_3F3F3F));
                break;

            default:
                break;

        }

    }
}
