package com.search.coupon.agent.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.bj.paysdk.domain.TrPayResult;
import com.base.bj.paysdk.listener.PayResultListener;
import com.base.bj.paysdk.utils.TrPay;
import com.search.coupon.agent.R;
import com.search.coupon.agent.bean.CoinBean;
import com.search.coupon.agent.bean.OrderBean;
import com.search.coupon.agent.common.BaseActivity;
import com.search.coupon.agent.common.ShareData;
import com.search.coupon.agent.network.RequestParams;
import com.search.coupon.agent.network.ResultData;
import com.search.coupon.agent.network.Task;
import com.search.coupon.agent.network.UrlConfig;
import com.search.coupon.agent.utils.DeviceUtils;
import com.search.coupon.agent.utils.StringUtils;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.search.coupon.agent.network.UrlConfig.URL_PAY_NOTIFY;

public class PayActivity extends BaseActivity {


    @BindView(R.id.tv_money_num)
    TextView tv_money_num;
    @BindView(R.id.tv_coin_num)
    TextView tv_coin_num;
    @BindView(R.id.rl_payment_alipay)
    RelativeLayout rl_payment_alipay;
    @BindView(R.id.rl_payment_wechat)
    RelativeLayout rl_payment_wechat;
    @BindView(R.id.rl_payment_tiny_coin)
    RelativeLayout rl_payment_tiny_coin;

    @BindView(R.id.cb_pay_1)
    CheckBox cb_pay_1;                //支付宝支付
    @BindView(R.id.cb_pay_2)
    CheckBox cb_pay_2;                //微信支付
    @BindView(R.id.cb_pay_3)
    CheckBox cb_pay_3;                //微币支付


    @BindView(R.id.ll_btn)
    LinearLayout ll_btn;
    @BindView(R.id.btn_submit)
    TextView btn_submit;

    private static final String NOTIFYURL = URL_PAY_NOTIFY;

    private static final String RECHARGE = "recharge";    //从充值过来的路径
    private static final String REFRESH = "refresh";    //从刷新过来的

    private String orderId;
    private OrderBean orderBean;
    private String path;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_pay);
    }

    @Override
    public void dealLogicBeforeInitView() {
        Bundle bundle = getIntentData();
        orderId = bundle.getString("orderId");
        path = bundle.getString("path");
        if (StringUtils.isEmpty(orderId)){
            toast("请获取正确的订单信息");
            return;
        }

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        topBar.setTitle("选择支付方式");
        topBar.setLineVisible(true);
    }

    @Override
    public void dealLogicAfterInitView() {

        if ("recharge".equals(path)){
            rl_payment_tiny_coin.setVisibility(View.GONE);    //从充值页过来的
        }else{
            getTinyCoinNum();      //  从微信群发布页 或者刷新页 过来的
        }
        getOrderDes();
    }



    @OnClick({R.id.rl_payment_alipay, R.id.rl_payment_wechat,R.id.rl_payment_tiny_coin, R.id.btn_submit})
    @Override
    public void onClickEvent(View view) {
        Intent intent =null;
        switch (view.getId()) {
            case R.id.rl_payment_alipay:                  //支付宝支付

                cb_pay_1.setChecked(true);
                cb_pay_2.setChecked(false);
                cb_pay_3.setChecked(false);

                break;
            case R.id.rl_payment_wechat:                 //微信支付
                cb_pay_1.setChecked(false);
                cb_pay_2.setChecked(true);
                cb_pay_3.setChecked(false);
                break;
            case R.id.rl_payment_tiny_coin:              //微币支付

                cb_pay_1.setChecked(false);
                cb_pay_2.setChecked(false);
                cb_pay_3.setChecked(true);
                break;
            case R.id.btn_submit:                 //提交

                if (orderBean == null){
                    toast("非法方式");
                    return;
                }
                if (cb_pay_1.isChecked()){           //支付宝支付
                    goToPayByAliPay(orderBean.getOrderName(),orderId,orderBean.getPayMoney(),"0",NOTIFYURL,ShareData.getShareStringData(ShareData.USER_ID));
                }else if (cb_pay_2.isChecked()){     //微信支付
                    goToPayByWechat(orderBean.getOrderName(),orderId,orderBean.getPayMoney(),"0",NOTIFYURL,ShareData.getShareStringData(ShareData.USER_ID));
                }else if (cb_pay_3.isChecked()){     //微币支付
                    goToPayByCoin();
                }else{
                    return;
                }

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
     * 查询微币数量
     * */
    private void getTinyCoinNum() {
        RequestParams params = new RequestParams(UrlConfig.URL_GET_TINY_COIN_NUM);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("userId", ShareData.getShareStringData(ShareData.USER_ID));
        startRequest(Task.GET_TINY_COIN_NUM, params, CoinBean.class);

    }

    /***
     * 获取订单详情
     * */
    private void getOrderDes() {
        RequestParams params = new RequestParams(UrlConfig.URL_GET_ORDER_DES);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("userId", ShareData.getShareStringData(ShareData.USER_ID));
        params.add("orderId", orderId);
        startRequest(Task.GET_ORDER_DES, params, OrderBean.class);

    }
    /***
     * 用微币支付
     * */
    private void goToPayByCoin() {

       /**
        * userId  Long	是	用户ID
         orderId  String	是	订单id
        payAmount int  是	支付微币金额 单位分  小于等于0 写0
        couponId String 否	优惠券id
        appkey   String 是	软件的appkey  Trpay给的值  安卓 48b8f3bf366148a9aa0ba89e2b5d3ec1
        version String 是	版本，固定值    1.0
        payType int  是	   支付方式固定值 4    说明 1支付宝2微信3银联  4 微币支付  5优惠券支付
        orderName String 否	商品名称    发布微信群
        payTime String  否	支付时间  2016-08-12 22:10:32
        * */

        RequestParams params = new RequestParams(UrlConfig.URL_PAY_BY_COIN);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("userId", ShareData.getShareStringData(ShareData.USER_ID));
        params.add("orderId", orderBean.getOrderId());
        params.add("payAmount", orderBean.getPayMoney());
        params.add("couponId", "");
        params.add("appkey", "48b8f3bf366148a9aa0ba89e2b5d3ec1");
        params.add("version", "1.0");
        params.add("payType", 4);
        params.add("orderName", orderBean.getOrderName());
        params.add("payTime", System.currentTimeMillis());
        startRequest(Task.PAY_BY_COIN, params, OrderBean.class);

    }


    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);
        switch (tag) {
            case Task.GET_TINY_COIN_NUM:
                if (handlerRequestErr(data)) {
                    //获取微币数量
                    CoinBean coinBean = (CoinBean) data.getBody();
                    tv_coin_num.setText("可用个数:"+coinBean.getVcoin());

                }
                break;
            case Task.GET_ORDER_DES:
                if (handlerRequestErr(data)) {
                    orderBean = (OrderBean) data.getBody();
                    float payMoney = (float)orderBean.getPayMoney()/100;
                    tv_money_num.setText("¥"+formatString(payMoney));
                }
                break;
            case Task.PAY_BY_COIN:
                if (handlerRequestErr(data)) {
                    if (StringUtils.isEmpty(path)){     //从微信群发布页 过来的
                        Bundle bd = new Bundle();
                        bd.putString("orderId",orderId);
                        goPage(PayResultActivity.class,bd);
                    }
                    finish();
                }else {
                    toast(data.getRspMsg());
                }
                break;
            default:
                break;
        }
    }

    public String formatString(float data) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(data);
    }


    /**
     * 2.发起支付宝支付
     * @param tradename       商品名称
     * @param outtradeno      商户系统订单号(商户系统内唯一)
     * @param amount          商品价格（单位：分。如1.5元传150）
     * @param backparams      商户系统回调参数
     * @param notifyurl       商户系统回调地址
     * @param userid          商户系统用户ID(如：trpay@52yszd.com，商户系统内唯一)*/
    public void goToPayByAliPay(String tradename,String outtradeno,long amount,String backparams,String notifyurl,String userid){

        TrPay.getInstance(PayActivity.this).callAlipay(tradename, outtradeno, amount, backparams, notifyurl, userid, new PayResultListener() {
            /**
             * 支付完成回调
             * @param context        上下文
             * @param outtradeno   商户系统订单号
             * @param resultCode   支付状态(RESULT_CODE_SUCC：支付成功、RESULT_CODE_FAIL：支付失败)
             * @param resultString  支付结果
             * @param payType      支付类型（1：支付宝 2：微信 3：银联）
             * @param amount       支付金额
             * @param tradename   商品名称
             */
            @Override
            public void onPayFinish(Context context, String outtradeno, int resultCode, String resultString, int payType, Long                                                           amount, String tradename) {
                if (resultCode == TrPayResult.RESULT_CODE_SUCC.getId()) {
                    Bundle bd = new Bundle();
                    bd.putString("orderId",orderId);
                    if (StringUtils.isEmpty(path)){
                        goPage(PayResultActivity.class,bd);
                    }else {
                        toast("支付宝支付成功");
                    }
                    finish();
                    //支付成功逻辑处理
                } else if (resultCode == TrPayResult.RESULT_CODE_FAIL.getId()) {
                    //支付失败逻辑处理
                    toast("支付宝支付未成功");
                }
            }
        });


    }
    /**
     * 3.发起微信支付
     * @param tradename   商品名称
     * @param outtradeno   商户系统订单号(商户系统内唯一)
     * @param amount        商品价格（单位：分。如1.5元传150）
     * @param backparams 商户系统回调参数
     * @param notifyurl       商户系统回调地址
     * @param userid          商户系统用户ID(如：trpay@52yszd.com，商户系统内唯一)
     */

    public void goToPayByWechat(String tradename,String outtradeno,long amount,String backparams,String notifyurl,String userid){

        TrPay.getInstance(PayActivity.this).callWxPay(tradename, outtradeno, amount, backparams, notifyurl, userid, new PayResultListener() {
            /**
             * 支付完成回调
             * @param context        上下文
             * @param outtradeno   商户系统订单号
             * @param resultCode   支付状态(RESULT_CODE_SUCC：支付成功、RESULT_CODE_FAIL：支付失败)
             * @param resultString  支付结果
             * @param payType      支付类型（1：支付宝 2：微信 3：银联）
             * @param amount       支付金额
             * @param tradename   商品名称
             */
            @Override
            public void onPayFinish(Context context, String outtradeno, int resultCode, String resultString, int payType, Long                                                           amount, String tradename) {
                if (resultCode == TrPayResult.RESULT_CODE_SUCC.getId()) {
                    //支付成功逻辑处理
                    Bundle bd = new Bundle();
                    bd.putString("orderId",orderId);
                    if (StringUtils.isEmpty(path)){
                        goPage(PayResultActivity.class,bd);
                    }else {
                        toast("微信支付成功");
                    }
                    finish();
                } else if (resultCode == TrPayResult.RESULT_CODE_FAIL.getId()) {
                    //支付失败逻辑处理
                    toast("微信支付未成功");
                }
            }
        });

    }


}
