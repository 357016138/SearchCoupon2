package com.search.coupon.agent.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.search.coupon.agent.R;
import com.search.coupon.agent.bean.DataBean;
import com.search.coupon.agent.common.BaseActivity;
import com.search.coupon.agent.common.ShareData;
import com.search.coupon.agent.network.RequestParams;
import com.search.coupon.agent.network.ResultData;
import com.search.coupon.agent.network.Task;
import com.search.coupon.agent.network.UrlConfig;
import com.search.coupon.agent.utils.DeviceUtils;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


/**
 * Created by song on 2018/4/11 0017.
 * 充值的界面
 */
public class RechargeActivity extends BaseActivity {

    @BindView(R.id.ll_1)
    LinearLayout ll_1;
    @BindView(R.id.ll_2)
    LinearLayout ll_2;
    @BindView(R.id.ll_3)
    LinearLayout ll_3;
    @BindView(R.id.ll_4)
    LinearLayout ll_4;
    @BindView(R.id.ll_5)
    LinearLayout ll_5;
    @BindView(R.id.ll_6)
    LinearLayout ll_6;
    @BindView(R.id.tv_1_1)
    TextView tv_1_1;
    @BindView(R.id.tv_1_2)
    TextView tv_1_2;
    @BindView(R.id.tv_2_1)
    TextView tv_2_1;
    @BindView(R.id.tv_2_2)
    TextView tv_2_2;
    @BindView(R.id.tv_3_1)
    TextView tv_3_1;
    @BindView(R.id.tv_3_2)
    TextView tv_3_2;
    @BindView(R.id.tv_4_1)
    TextView tv_4_1;
    @BindView(R.id.tv_4_2)
    TextView tv_4_2;
    @BindView(R.id.tv_5_1)
    TextView tv_5_1;
    @BindView(R.id.tv_5_2)
    TextView tv_5_2;
    @BindView(R.id.tv_6_1)
    TextView tv_6_1;
    @BindView(R.id.tv_6_2)
    TextView tv_6_2;
    private String rechargeMenoy = "600";


    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_recharge);
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
        ll_1.setBackground(getResources().getDrawable(R.drawable.bg_yellow));
    }
    @OnClick({R.id.ll_1,R.id.ll_2,R.id.ll_3,R.id.ll_4,R.id.ll_5,R.id.ll_6,R.id.btn_submit})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.ll_1:
                setLinearLayoutBackground();
                ll_1.setBackground(getResources().getDrawable(R.drawable.bg_yellow));
                tv_1_1.setTextColor(getResources().getColor(R.color.white));
                tv_1_2.setTextColor(getResources().getColor(R.color.white));
                rechargeMenoy = "600";
                break;
            case R.id.ll_2:
                setLinearLayoutBackground();
                ll_2.setBackground(getResources().getDrawable(R.drawable.bg_yellow));
                tv_2_1.setTextColor(getResources().getColor(R.color.white));
                tv_2_2.setTextColor(getResources().getColor(R.color.white));
                rechargeMenoy = "1200";
                break;
            case R.id.ll_3:
                setLinearLayoutBackground();
                ll_3.setBackground(getResources().getDrawable(R.drawable.bg_yellow));
                tv_3_1.setTextColor(getResources().getColor(R.color.white));
                tv_3_2.setTextColor(getResources().getColor(R.color.white));
                rechargeMenoy = "3000";
                break;
            case R.id.ll_4:
                setLinearLayoutBackground();
                ll_4.setBackground(getResources().getDrawable(R.drawable.bg_yellow));
                tv_4_1.setTextColor(getResources().getColor(R.color.white));
                tv_4_2.setTextColor(getResources().getColor(R.color.white));
                rechargeMenoy = "5000";
                break;
            case R.id.ll_5:
                setLinearLayoutBackground();
                ll_5.setBackground(getResources().getDrawable(R.drawable.bg_yellow));
                tv_5_1.setTextColor(getResources().getColor(R.color.white));
                tv_5_2.setTextColor(getResources().getColor(R.color.white));
                rechargeMenoy = "10000";
                break;
            case R.id.ll_6:
                setLinearLayoutBackground();
                ll_6.setBackground(getResources().getDrawable(R.drawable.bg_yellow));
                tv_6_1.setTextColor(getResources().getColor(R.color.white));
                tv_6_2.setTextColor(getResources().getColor(R.color.white));
                rechargeMenoy = "20000";
                break;
            case R.id.btn_submit:
                createOrder();
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

    public void createOrder(){
        RequestParams params = new RequestParams(UrlConfig.URL_RECHARGE_ORDER);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("userId", ShareData.getShareStringData(ShareData.USER_ID));
        params.add("rechargeMenoy", rechargeMenoy);
        startRequest(Task.RECHARGE_ORDER, params, DataBean.class);
    }


    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);
        switch (tag) {
            case Task.RECHARGE_ORDER:
                if (handlerRequestErr(data)) {
                    //创建订单完成 去支付
                    DataBean dataBean = (DataBean) data.getBody();
                    String orderId = dataBean.getData();
                    Bundle bd = new Bundle();
                    bd.putString("orderId", orderId);
                    bd.putString("path", "recharge");
                    goPage(PayActivity.class, bd);
                    finish();

                }
                break;

            default:
                break;

        }
    }



    //背景色初始化
    public void setLinearLayoutBackground(){
        ll_1.setBackground(getResources().getDrawable(R.drawable.bg_loading_dialog));
        ll_2.setBackground(getResources().getDrawable(R.drawable.bg_loading_dialog));
        ll_3.setBackground(getResources().getDrawable(R.drawable.bg_loading_dialog));
        ll_4.setBackground(getResources().getDrawable(R.drawable.bg_loading_dialog));
        ll_5.setBackground(getResources().getDrawable(R.drawable.bg_loading_dialog));
        ll_6.setBackground(getResources().getDrawable(R.drawable.bg_loading_dialog));
        tv_1_1.setTextColor(getResources().getColor(R.color.color_FF934C));
        tv_1_2.setTextColor(getResources().getColor(R.color.color_999999));
        tv_2_1.setTextColor(getResources().getColor(R.color.color_FF934C));
        tv_2_2.setTextColor(getResources().getColor(R.color.color_999999));
        tv_3_1.setTextColor(getResources().getColor(R.color.color_FF934C));
        tv_3_2.setTextColor(getResources().getColor(R.color.color_999999));
        tv_4_1.setTextColor(getResources().getColor(R.color.color_FF934C));
        tv_4_2.setTextColor(getResources().getColor(R.color.color_999999));
        tv_5_1.setTextColor(getResources().getColor(R.color.color_FF934C));
        tv_5_2.setTextColor(getResources().getColor(R.color.color_999999));
        tv_6_1.setTextColor(getResources().getColor(R.color.color_FF934C));
        tv_6_2.setTextColor(getResources().getColor(R.color.color_999999));
    }






}
