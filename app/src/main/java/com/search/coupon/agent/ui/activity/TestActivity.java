package com.search.coupon.agent.ui.activity;

import android.view.View;

 import com.search.coupon.agent.R;
import com.search.coupon.agent.common.BaseActivity;

import butterknife.ButterKnife;

public class TestActivity extends BaseActivity {

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_test);
    }

    @Override
    public void dealLogicBeforeInitView() {

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        topBar.setTitle("充值");
        topBar.setLineVisible(true);
    }

    @Override
    public void dealLogicAfterInitView() {

    }
    //    @OnClick({R.id.iv_pic_code,R.id.signup_obtainCode})
    @Override
    public void onClickEvent(View view) {

    }

    @Override
    public void OnTopLeftClick() {
        finish();
    }

    @Override
    public void OnTopRightClick() {

    }
}
