package com.search.coupon.agent.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.search.coupon.agent.R;
import com.search.coupon.agent.common.BaseFragment;
import com.search.coupon.agent.common.Constants;
import com.search.coupon.agent.service.MessageEvent;
import com.search.coupon.agent.utils.UserUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 订单（询价订单、贷款订单）
 * Created by song on 2018/1/30.
 */
public class BillFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.ll_price_bill)
    LinearLayout ll_price_bill;
    @BindView(R.id.ll_loan_bill)
    LinearLayout ll_loan_bill;
    @BindView(R.id.tv_price_bill)
    TextView tv_price_bill;
    @BindView(R.id.tv_loan_bill)
    TextView tv_loan_bill;
    @BindView(R.id.view_price_bill)
    View view_price_bill;
    @BindView(R.id.view_loan_bill)
    View view_loan_bill;
    @BindView(R.id.fl_bill)
    FrameLayout fl_bill;

    private Fragment currentFragment = new Fragment();
    private Fragment priceBillFragment = new PriceBillFragment();
    private Fragment loanBillFragment = new LoanBillFragment();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill, container, false);
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
        EventBus.getDefault().register(this);

    }

    /**
     * 初始化数据
     */
    private void initData() {
        switchFragment(priceBillFragment).commit();
    }

    @OnClick({R.id.ll_price_bill, R.id.ll_loan_bill})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.ll_price_bill:     //询价订单
                switchFragment(priceBillFragment).commit();
                tv_price_bill.setTextColor(getResources().getColor(R.color.color_3F3F3F));
                view_price_bill.setVisibility(View.VISIBLE);
                tv_loan_bill.setTextColor(getResources().getColor(R.color.color_8A97B0));
                view_loan_bill.setVisibility(View.INVISIBLE);
                break;

            case R.id.ll_loan_bill:     //贷款订单
                switchFragment(loanBillFragment).commit();
                tv_price_bill.setTextColor(getResources().getColor(R.color.color_8A97B0));
                view_price_bill.setVisibility(View.INVISIBLE);
                tv_loan_bill.setTextColor(getResources().getColor(R.color.color_3F3F3F));
                view_loan_bill.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    private FragmentTransaction switchFragment(Fragment targetFragment) {

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        if (!targetFragment.isAdded()) {
            //第一次使用switchFragment()时currentFragment为null，所以要判断一下
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.fl_bill, targetFragment, targetFragment.getClass().getName());
        } else {
            transaction.hide(currentFragment).show(targetFragment);
        }
        currentFragment = targetFragment;
        return transaction;

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.getTag() == Constants.JUMP_TO_LOAN_LIST) {
            if (UserUtils.isLogin()){
                switchFragment(loanBillFragment).commit();
                tv_price_bill.setTextColor(getResources().getColor(R.color.color_8A97B0));
                view_price_bill.setVisibility(View.INVISIBLE);
                tv_loan_bill.setTextColor(getResources().getColor(R.color.color_3F3F3F));
                view_loan_bill.setVisibility(View.VISIBLE);
            }
        }else if (event.getTag() == Constants.JUMP_TO_PRICE_LIST){
            if (UserUtils.isLogin()){
                switchFragment(priceBillFragment).commit();
                tv_price_bill.setTextColor(getResources().getColor(R.color.color_3F3F3F));
                view_price_bill.setVisibility(View.VISIBLE);
                tv_loan_bill.setTextColor(getResources().getColor(R.color.color_8A97B0));
                view_loan_bill.setVisibility(View.INVISIBLE);
            }
        }
    }
}
