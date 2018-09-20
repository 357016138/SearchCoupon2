package com.search.coupon.agent.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.search.coupon.agent.R;
import com.search.coupon.agent.common.BaseFragment;
import com.search.coupon.agent.ui.activity.PublishWechatGroupActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 应用
 * Created by song on 2018/1/30.
 */
public class PublishFragment extends BaseFragment {

    Unbinder unbinder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_publish, container, false);
        initView(view);
        initData();

        return view;
    }

    private void initView(View view) {
        //一定要解绑 在onDestroyView里
        unbinder = ButterKnife.bind(this,view);
    }

    private void initData() {

    }

    @OnClick({R.id.ll_publish_1,R.id.ll_publish_2})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.ll_publish_1:
                if (!isLogin()) return;
                goPage(PublishWechatGroupActivity.class);
                break;
            case R.id.ll_publish_2:
                toast("敬请期待");
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



}
