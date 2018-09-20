package com.search.coupon.agent.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.search.coupon.agent.R;
import com.search.coupon.agent.adapter.CommViewPagerAdapter;
import com.search.coupon.agent.common.BaseActivity;
import com.search.coupon.agent.listener.CustomTabEntity;
import com.search.coupon.agent.listener.OnTabSelectListener;
import com.search.coupon.agent.listener.TabEntity;
import com.search.coupon.agent.ui.fragment.ArtificialAskFragment;
import com.search.coupon.agent.view.CommonTabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author baipeng
 * @Title ConsultPriceActivity
 * @Date 2018/2/9 14:58
 * @Description 询价.
 */
public class ConsultPriceActivity extends BaseActivity {
    @BindView(R.id.ll_back)
    View llBack;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    @BindView(R.id.tl_4)
    CommonTabLayout mTabLayout_4;
    private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
//    private ArtificialAskFragment quicklyFragment; //极速询价
    private ArtificialAskFragment artificialFragment; //人工询价
    private int sub_tab = 0;
//    private String[] mTitles = {"极速询价", "人工询价"};
    private String[] mTitles = {"我要询价"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    @OnClick({R.id.ll_back})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            default:
                break;

        }
    }

    @Override
    public void OnTopLeftClick() {
    }

    @Override
    public void OnTopRightClick() {
    }

    @Override
    public void dealLogicAfterInitView() {
        llBack.setOnClickListener(this);
        mTabLayout_4.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout_4.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void dealLogicBeforeInitView() {

    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_ask_price, BasePageSet.NO_TOPBAR_DEFAULT_PAGE);
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        initPageAdapter();
    }

    private void initPageAdapter() {
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i]));
        }
        mTabLayout_4.setTabData(mTabEntities);
//        quicklyFragment = new ArtificialAskFragment();
        artificialFragment = new ArtificialAskFragment();
//        fragmentList.add(quicklyFragment);
        fragmentList.add(artificialFragment);
        mViewPager.setOffscreenPageLimit(2);

        CommViewPagerAdapter adapter = new CommViewPagerAdapter(this.getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(sub_tab);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
