package com.search.coupon.agent.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.search.coupon.agent.R;
import com.search.coupon.agent.adapter.BizPhotoWallAdapter;
import com.search.coupon.agent.common.BaseActivity;
import com.search.coupon.agent.common.Constants;
import com.search.coupon.agent.log.JYLogUtils;
import com.search.coupon.agent.view.HackyViewPager;

import java.util.ArrayList;


public class ImageWallActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    public static final String TAG = "ImageWallActivity";
    private TextView indexTxt;
    private TextView sumTxt;
    private HackyViewPager mViewPager;
    private ImageView mIVBack;
    private ArrayList<String> mData;
    private int index = 0;

    private void getLastBundle() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                index = bundle.getInt(Constants.IMAGE_WALL_INDEX);
                mData = (ArrayList<String>) bundle.getSerializable(Constants.IMAGE_WALL_DATA);
            } else {
                JYLogUtils.d(TAG, "没有获取到数据无法查看大图！");
                finish();
            }
        }
    }

    @Override
    public void initView() {
        indexTxt = (TextView) findViewById(R.id.image_walll_index);
        sumTxt = (TextView) findViewById(R.id.image_walll_sum);

        mViewPager = (HackyViewPager) findViewById(R.id.vp_photo_wall);
        mIVBack = (ImageView) findViewById(R.id.iv_back);
    }

    private void initData() {
        indexTxt.setText(String.valueOf(index + 1));
        sumTxt.setText(String.valueOf(mData.size()));

        if (mData != null) {
            BizPhotoWallAdapter adapter = new BizPhotoWallAdapter(this.getSupportFragmentManager(), mData);
            mViewPager.setAdapter(adapter);
        }
        mViewPager.setCurrentItem(index, false);
    }

    private void initListener() {
        mViewPager.addOnPageChangeListener(this);
        mIVBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        indexTxt.setText(String.valueOf(position + 1));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        if (mViewPager != null) {
            mViewPager.removeOnPageChangeListener(this);
        }
        super.onDestroy();
    }

    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
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
        initData();
        initListener();
    }

    @Override
    public void dealLogicBeforeInitView() {
        getLastBundle();
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_image_wall, BasePageSet.NO_TOPBAR_DEFAULT_PAGE);
    }
}
