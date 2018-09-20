package com.search.coupon.agent.ui.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.search.coupon.agent.R;
import com.search.coupon.agent.bean.BindBankCardInfoBean;
import com.search.coupon.agent.bean.CoinBean;
import com.search.coupon.agent.common.BaseFragment;
import com.search.coupon.agent.common.ShareData;
import com.search.coupon.agent.network.RequestParams;
import com.search.coupon.agent.network.ResultData;
import com.search.coupon.agent.network.Task;
import com.search.coupon.agent.network.UrlConfig;
import com.search.coupon.agent.response.BankCardListResponse;
import com.search.coupon.agent.response.WhetherSetPaymentPasswordResponse;
import com.search.coupon.agent.ui.activity.AboutUsActivity;
import com.search.coupon.agent.ui.activity.BindBankCardActivity;
import com.search.coupon.agent.ui.activity.ContactUsActivity;
import com.search.coupon.agent.ui.activity.LoginActivity;
import com.search.coupon.agent.ui.activity.MyPublishListActivity;
import com.search.coupon.agent.ui.activity.RechargeActivity;
import com.search.coupon.agent.ui.activity.SetWithdrawPasswordActivity;
import com.search.coupon.agent.ui.activity.SettingActivity;
import com.search.coupon.agent.ui.activity.SuggestionActivity;
import com.search.coupon.agent.ui.activity.WithdrawDepositActivity;
import com.search.coupon.agent.utils.DeviceUtils;
import com.search.coupon.agent.utils.LogUtils;
import com.search.coupon.agent.utils.StringUtils;
import com.search.coupon.agent.utils.UserUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 我的
 * Created by song on 2018/1/30.
 */
public class MineFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.iv_setting)
    ImageView iv_setting;
    @BindView(R.id.iv_mine_header)
    ImageView iv_mine_header;
    @BindView(R.id.btn_sign_out)
    Button btn_sign_out;
    @BindView(R.id.ll_mine_logined)
    LinearLayout ll_mine_logined;
    @BindView(R.id.ll_mine_login)
    LinearLayout ll_mine_login;
    @BindView(R.id.bt_manage_publish)
    TextView bt_manage_publish;
    @BindView(R.id.ll_mine_4)
    LinearLayout ll_mine_4;
    @BindView(R.id.ll_mine_5)
    LinearLayout ll_mine_5;
    @BindView(R.id.ll_mine_6)
    LinearLayout ll_mine_6;
    @BindView(R.id.ll_mine_7)
    LinearLayout ll_mine_7;
    @BindView(R.id.ll_mine_8)
    LinearLayout ll_mine_8;
    @BindView(R.id.ll_mine_9)
    LinearLayout ll_mine_9;
    @BindView(R.id.ll_mine_10)
    LinearLayout ll_mine_10;
    @BindView(R.id.ll_mine_11)
    LinearLayout ll_mine_11;
    @BindView(R.id.ll_mine_12)
    LinearLayout ll_mine_12;
    @BindView(R.id.ll_mine_13)
    LinearLayout ll_mine_13;
    @BindView(R.id.ll_mine_14)
    LinearLayout ll_mine_14;
    @BindView(R.id.mine_tv_capital)
    TextView mine_tv_capital;
    @BindView(R.id.mine_iv_show)
    ImageView mine_iv_show;
    @BindView(R.id.tv_mine_phone)
    TextView tv_mine_phone;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private CoinBean coinBean;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    private void initView(View view) {

        //一定要解绑 在onDestroyView里
        unbinder = ButterKnife.bind(this, view);
        refreshLayout.isEnableLoadmore();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (UserUtils.isLogin()){
                    //TODO   刷新用户账户金额信息
                    refreshLayout.finishRefresh();
                    getTinyCoinNum();
                }else{
                    refreshLayout.finishRefresh();
                }

            }
        });
    }

    private void initData() {
        //登录状态
        if (UserUtils.isLogin()) {
            iv_mine_header.setVisibility(View.VISIBLE);
            ll_mine_logined.setVisibility(View.VISIBLE);
            ll_mine_login.setVisibility(View.GONE);
            String phone = ShareData.getShareStringData(ShareData.USER_PHONE);
            tv_mine_phone.setText(StringUtils.getUserName(phone));
            getTinyCoinNum();
        } else {
            iv_mine_header.setVisibility(View.GONE);
            ll_mine_logined.setVisibility(View.GONE);
            ll_mine_login.setVisibility(View.VISIBLE);
        }
    }


    @OnClick({R.id.iv_setting, R.id.btn_sign_out, R.id.mine_tv_capital,R.id.bt_manage_publish, R.id.mine_iv_show,R.id.ll_mine_1,R.id.ll_mine_2,R.id.ll_mine_3,R.id.ll_mine_4, R.id.ll_mine_5, R.id.ll_mine_6, R.id.ll_mine_7, R.id.ll_mine_8, R.id.ll_mine_9, R.id.ll_mine_10, R.id.ll_mine_11, R.id.ll_mine_12, R.id.ll_mine_13, R.id.ll_mine_14})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.iv_setting:             //设置
                if (!isLogin()) return;
                goPage(SettingActivity.class);
                break;
            case R.id.btn_sign_out:          //登录
                if (!isLogin()) return;
                goPage(LoginActivity.class);
                break;
            case R.id.bt_manage_publish:        //我的发布
                if (!isLogin()) return;
                goPage(MyPublishListActivity.class);
                break;
            case R.id.mine_iv_show:        //显示与隐藏资金
                if (!isLogin()) return;
                if (ShareData.getShareBooleanData(ShareData.SHOW_AMOUNT)){
                    ShareData.setShareBooleanData(ShareData.SHOW_AMOUNT, false);
                } else {
                    ShareData.setShareBooleanData(ShareData.SHOW_AMOUNT, true);
                }
                if (!ShareData.getShareBooleanData(ShareData.SHOW_AMOUNT)) {
                    mine_tv_capital.setText(coinBean.getVcoin());
                    mine_iv_show.setImageResource(R.drawable.icon_mine_2_2);
                } else {
                    mine_tv_capital.setText("****");
                    mine_iv_show.setImageResource(R.drawable.icon_mine_2_1);
                }
                break;

            case R.id.ll_mine_1:        //充值
                if (!isLogin()) return;
                goPage(RechargeActivity.class);
                break;

            case R.id.ll_mine_2:        //联系客服
                goPage(ContactUsActivity.class);
                break;

            case R.id.ll_mine_3:        //我要吐槽
                if (!isLogin()) return;
                goPage(SuggestionActivity.class);
                break;

            case R.id.ll_mine_4:        //关于我们
                goPage(AboutUsActivity.class);
                break;
            case R.id.ll_mine_5:      //返佣管理

                break;
            case R.id.ll_mine_6:      //账单
                toast("敬请期待");
                break;

            case R.id.ll_mine_7:     //银行卡
                toast("敬请期待");
                break;

            case R.id.ll_mine_8:    //邀请好友
                toast("敬请期待");
                break;

            case R.id.ll_mine_9:     //业绩榜单
                toast("敬请期待");
                break;

            case R.id.ll_mine_10:    //客户经理
                toast("敬请期待");
                break;

            case R.id.ll_mine_11:     //帮助中心
                goWebPage("帮助中心", UrlConfig.URL_HELP_CENTER);
                break;

            case R.id.ll_mine_12:     //关于我们
//                goWebPage("关于我们", UrlConfig.URL_ABOUT_US);
                break;

            case R.id.ll_mine_13:    //联系我们
//                goWebPage("联系我们", UrlConfig.URL_CONTACT_US);
                break;

            case R.id.ll_mine_14:   //我要吐槽
//                goWebPage("我要吐槽", String.format(Locale.US, UrlConfig.URL_SUGGEST,
//                        DeviceUtils.getDeviceUniqueId(getActivity()),
//                        UserManager.getUserId()));
                break;

            default:
                break;

        }
    }


    /***
     * 查询微币数量
     * */
    private void getTinyCoinNum() {
        RequestParams params = new RequestParams(UrlConfig.URL_GET_TINY_COIN_NUM);
        params.add("pid", DeviceUtils.getDeviceUniqueId(getActivity()));
        params.add("userId", ShareData.getShareStringData(ShareData.USER_ID));
        startRequest(Task.GET_TINY_COIN_NUM, params, CoinBean.class);

    }

    private void isSetPayPassword() {
        RequestParams params = new RequestParams(UrlConfig.URL_WHETHER_SET_PAYMENT_PASSWORD);
        params.add("pid", DeviceUtils.getDeviceUniqueId(getActivity()));
        params.add("userId", ShareData.getShareStringData(ShareData.USER_ID));
        startRequest(Task.WHETHER_SET_PAY_PASSWORD, params, WhetherSetPaymentPasswordResponse.class, true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
//        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            //相当于Fragment的onPause
            LogUtils.e("界面不可见");
        } else {
            // 相当于Fragment的onResume
            LogUtils.e("界面可见");
        }
        super.onHiddenChanged(hidden);
    }

    private void showVerificationDialog(String tip, int tag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dialog_no_bg);
        View myView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_is_can_withdraw_deposit_layout, null);
        builder.setView(myView);
        TextView tvTip = myView.findViewById(R.id.tv_tip);
        tvTip.setText(tip);
        myView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        final Dialog dialog = builder.create();
        myView.findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        myView.findViewById(R.id.bt_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (tag) {
                    case 1:
                        goPage(BindBankCardActivity.class);
                        break;

                    case 2:
                        goPage(SetWithdrawPasswordActivity.class);
                        break;
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);
        switch (tag) {
            case Task.BIND_BANK_CARD_INFO:
                if (handlerRequestErr(data)) {
                    BankCardListResponse response = (BankCardListResponse) data.getBody();
                    List<BindBankCardInfoBean> list = response.getList();
                    if (list != null && list.size() > 0) {
                        isSetPayPassword();
                    } else {
                        showVerificationDialog("您需要先绑定银行卡才可提现", 1);
                    }
                }
                break;

            case Task.WHETHER_SET_PAY_PASSWORD:
                if (handlerRequestErr(data)) {
                    WhetherSetPaymentPasswordResponse response = (WhetherSetPaymentPasswordResponse) data.getBody();
                    if (response.getIsPayPass().equals("1")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("balanceOfAccount", mine_tv_capital.getText().toString());
                        goPage(WithdrawDepositActivity.class, bundle);
                    } else {
                        showVerificationDialog("您需要先设置支付密码才可提现", 2);
                    }
                }
                break;

            case Task.GET_TINY_COIN_NUM:
                refreshLayout.finishRefresh();
                if (handlerRequestErr(data)){
                    //获取微币数量
                    coinBean = (CoinBean) data.getBody();
                    if (!ShareData.getShareBooleanData(ShareData.SHOW_AMOUNT)){
                        mine_tv_capital.setText(coinBean.getVcoin());
                    } else {
                        mine_tv_capital.setText("****");
                    }
                }
                break;
        }
    }

    public String formatString(double data) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(data);
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(MessageEvent event){
//        if (event.getTag() == Constants.GO_WITHDRAW_DEPOSIT){
//            isSetPayPassword();
//        }
//    }

}
