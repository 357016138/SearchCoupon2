package com.search.coupon.agent.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.search.coupon.agent.R;
import com.search.coupon.agent.common.BaseFragment;
import com.search.coupon.agent.network.UrlConfig;
import com.search.coupon.agent.utils.DeviceUtils;
import com.search.coupon.agent.utils.UserManager;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author baipeng
 * @Title NoticeListFragment
 * @Date 2018/3/6 11:36
 * @Description NoticeListFragment.
 */
public class NoticeListFragment extends BaseFragment {
    Unbinder unbinder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_webview, container, false);
        initView(view);
        initData();
        return view;
    }

    /**
     * 初始化控件
     * */
    private void initView(View view) {
        //一定要解绑 在onDestroyView里
        unbinder = ButterKnife.bind(this,view);
    }

    /**
     * 初始化数据
     * */
    private void initData() {
        String url = String.format(Locale.US, UrlConfig.URL_NOTICE_LIST,
                DeviceUtils.getDeviceUniqueId(getActivity()),
                UserManager.getUserId());
    }

    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
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
