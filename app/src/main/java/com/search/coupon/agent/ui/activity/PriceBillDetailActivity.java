package com.search.coupon.agent.ui.activity;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.search.coupon.agent.R;
import com.search.coupon.agent.bean.PriceBillDetailBean;
import com.search.coupon.agent.common.BaseActivity;
import com.search.coupon.agent.network.RequestParams;
import com.search.coupon.agent.network.ResultData;
import com.search.coupon.agent.network.Task;
import com.search.coupon.agent.network.UrlConfig;
import com.search.coupon.agent.utils.DeviceUtils;
import com.search.coupon.agent.utils.StringUtils;
import com.search.coupon.agent.utils.UserManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;


public class PriceBillDetailActivity extends BaseActivity {

    private String inquiryCode;
    @BindView(R.id.tv_price_bill_detail_title)
    TextView tv_price_bill_detail_title;
    @BindView(R.id.tv_price_bill_detail_statustime)
    TextView tv_price_bill_detail_statustime;
    @BindView(R.id.tv_price_bill_detail_des)
    TextView tv_price_bill_detail_des;
    @BindView(R.id.ll_price_bill_detail_product)
    LinearLayout ll_price_bill_detail_product;
    @BindView(R.id.tv_price_bill_detail_estatename)
    TextView tv_price_bill_detail_estatename;
    @BindView(R.id.tv_price_bill_detail_time)
    TextView tv_price_bill_detail_time;
    @BindView(R.id.tv_price_bill_detail_city)
    TextView tv_price_bill_detail_city;
    @BindView(R.id.tv_price_bill_detail_house_property)
    TextView tv_price_bill_detail_house_property;
    @BindView(R.id.tv_price_bill_detail_address)
    TextView tv_price_bill_detail_address;
    @BindView(R.id.ll_price_bill_detail_1)
    LinearLayout ll_price_bill_detail_1;
    @BindView(R.id.ll_price_bill_detail_2)
    LinearLayout ll_price_bill_detail_2;

    @BindView(R.id.iv_house_pic1)
    ImageView iv_house_pic1;
    @BindView(R.id.iv_house_pic2)
    ImageView iv_house_pic2;
    @BindView(R.id.iv_house_pic3)
    ImageView iv_house_pic3;
    @BindView(R.id.tv_prefer_product)
    TextView tv_prefer_product;
    private String isRecProduct;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_price_bill_detail);
    }

    @Override
    public void dealLogicBeforeInitView() {
        Bundle bundle = getIntentData();
        inquiryCode = bundle.getString("inquiryCode");
        isRecProduct = bundle.getString("isRecProduct");
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        topBar.setTitle("询价订单详情");
    }

    @Override
    public void dealLogicAfterInitView() {
        RequestParams params = new RequestParams(UrlConfig.URL_PRICE_BILL_DETAIL);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("userId", UserManager.getUserId());
        params.add("inquiryCode", inquiryCode);
        startRequest(Task.PRICE_BILL_DETAIL, params, PriceBillDetailBean.class);
    }

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

    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);
        switch (tag) {
            case Task.PRICE_BILL_DETAIL:
                if (handlerRequestErr(data)) {
//                    toast(""+data.getBody());
                    PriceBillDetailBean priceBillDetailBean = (PriceBillDetailBean) data.getBody();
                    changeUi(priceBillDetailBean);
                }else{
                    tv_price_bill_detail_title.setText("----");
                    tv_price_bill_detail_statustime.setText("----------");
                    tv_price_bill_detail_des.setText("------------------------------------------------------------");
                    tv_price_bill_detail_estatename.setText(" - - - - - - -");
                    tv_price_bill_detail_time.setText(" - - - - - - -");
                    tv_price_bill_detail_city.setText(" - - - - - - -");
                    tv_price_bill_detail_house_property.setText(" - - - - - - -");
                    tv_price_bill_detail_address.setText(" - - - - - - -");
                }
                break;
            default:
                break;
        }
    }

    private void changeUi(PriceBillDetailBean priceBillDetailBean) {

        List<PriceBillDetailBean.PropertyCertBean> propertyCertUrlList = priceBillDetailBean.getPropertyCertUrl();
        List<PriceBillDetailBean.ProcessBean> processBeanList = priceBillDetailBean.getProcessList();

        //流程列表
        if (processBeanList != null && processBeanList.size() > 0) {
            PriceBillDetailBean.ProcessBean processBean = processBeanList.get(0);

            if (!StringUtils.isEmpty(processBean.getDealTime()) && processBean.getDealTime().contains(" ")) {
                String[] split = processBean.getDealTime().split(" ");
                tv_price_bill_detail_statustime.setText(split[0]);
            } else {
                tv_price_bill_detail_statustime.setText(processBean.getDealTime());
            }

            if ("1".equals(processBean.getProcess())) {                 //提交询价
                tv_price_bill_detail_title.setText("提交询价");
                tv_price_bill_detail_des.setText("您的询价订单提交成功，等待估值。");

            } else if ("2".equals(processBean.getProcess())) {            //房产估值

                if ("1".equals(processBean.getProcessState())) {      // 是否通过1是0否
                    tv_price_bill_detail_title.setText("房产估值");
                    if ("1".equals(isRecProduct)) {      //优选产品
                        tv_price_bill_detail_des.setText("询价估值成功，您的房产估值" + StringUtils.emptyDispose(priceBillDetailBean.getHousingValuation()) + "万元。授信借款"+StringUtils.emptyDispose(priceBillDetailBean.getMinCreditAmount())+"-"+ StringUtils.emptyDispose(priceBillDetailBean.getMaxCreditAmount()) + "万元，利率" + StringUtils.emptyDispose(priceBillDetailBean.getMinInterestRate()) + "-" + StringUtils.emptyDispose(priceBillDetailBean.getMaxInterestRate()) + "%，周期" + StringUtils.emptyDispose(priceBillDetailBean.getMinPeriod()) + "-" + StringUtils.emptyDispose(priceBillDetailBean.getMaxPeriod()) + "个月。");
                        ll_price_bill_detail_product.setVisibility(View.VISIBLE);
                        tv_prefer_product.setText("查看优选产品");
                        ll_price_bill_detail_product.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bd = new Bundle();
                                bd.putString("inquiryCode", priceBillDetailBean.getInquiryCode());
                                goPage(PreferenceProductActivity.class, bd);
                            }
                        });
                    }else{
                        tv_price_bill_detail_des.setText("询价估值成功，您的房产估值" + StringUtils.emptyDispose(priceBillDetailBean.getHousingValuation()) + "万元，暂时无法匹配到优质产品。");
                    }

                } else {

                    tv_price_bill_detail_title.setText("询价失败");
                    tv_price_bill_detail_des.setText(priceBillDetailBean.getReasonRemark());
                    tv_price_bill_detail_des.setTextColor(getResources().getColor(R.color.gesture_line_err));
                    ll_price_bill_detail_product.setVisibility(View.VISIBLE);
                    tv_prefer_product.setText("重新询价");
                    ll_price_bill_detail_product.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goPage(ConsultPriceActivity.class);
                            finish();
                        }
                    });
                }

            } else if ("3".equals(processBean.getProcess())) {         //客服服务

                tv_price_bill_detail_title.setText("客服服务");
                tv_price_bill_detail_des.setText("客服人员回访产品确认中，确认后将以短信和站内信的形式通知您，请您耐心等候。");

                if ("1".equals(isRecProduct)) {      //优选产品
                    ll_price_bill_detail_product.setVisibility(View.VISIBLE);
                    tv_prefer_product.setText("查看优选产品");
                    ll_price_bill_detail_product.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bd = new Bundle();
                            bd.putString("inquiryCode", priceBillDetailBean.getInquiryCode());
                            goPage(PreferenceProductActivity.class, bd);
                        }
                    });
                }

            } else if ("4".equals(processBean.getProcess())) {        //配置产品
                tv_price_bill_detail_title.setText("配置产品");
                tv_price_bill_detail_des.setText("根据您的需求已为您配置优质产品，请您及时发起借款申请");

                if ("2".equals(isRecProduct)) {      //推荐产品
                    ll_price_bill_detail_product.setVisibility(View.VISIBLE);
                    tv_prefer_product.setText("查看推荐产品");
                    ll_price_bill_detail_product.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bd = new Bundle();
                            bd.putString("inquiryCode", priceBillDetailBean.getInquiryCode());
                            goPage(RecommendProductActivity.class, bd);
                        }
                    });
                }
                if ("8".equals(priceBillDetailBean.getInquiryStatus())){
                    tv_price_bill_detail_title.setText("询价失败");
                    tv_price_bill_detail_des.setText(priceBillDetailBean.getReasonRemark());
                    tv_price_bill_detail_des.setTextColor(getResources().getColor(R.color.gesture_line_err));
                    ll_price_bill_detail_product.setVisibility(View.VISIBLE);
                    tv_prefer_product.setText("重新询价");
                    ll_price_bill_detail_product.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goPage(ConsultPriceActivity.class);
                            finish();
                        }
                    });
                }

            } else {
                return;
            }

        }

        //填充数据
        tv_price_bill_detail_estatename.setText(priceBillDetailBean.getEstateName());
        if (!StringUtils.isEmpty(priceBillDetailBean.getApplyTime()) && priceBillDetailBean.getApplyTime().contains(" ")) {
            String[] split = priceBillDetailBean.getApplyTime().split(" ");
            tv_price_bill_detail_time.setText(split[0]);
        } else {
            tv_price_bill_detail_time.setText(priceBillDetailBean.getApplyTime());
        }

        if (StringUtils.isEmpty(priceBillDetailBean.getCity())) {      //为空的情况
            ll_price_bill_detail_1.setVisibility(View.GONE);
            ll_price_bill_detail_2.setVisibility(View.GONE);
        } else {
            tv_price_bill_detail_city.setText(priceBillDetailBean.getCity());
            tv_price_bill_detail_address.setText(priceBillDetailBean.getCity() + priceBillDetailBean.getEstateName() + priceBillDetailBean.getBuildingName() + priceBillDetailBean.getHouseholdName());
            if ("1".equals(priceBillDetailBean.getPropertyType())){
                tv_price_bill_detail_house_property.setText("住宅");
            }else if ("2".equals(priceBillDetailBean.getPropertyType())){
                tv_price_bill_detail_house_property.setText("公寓");
            }else if ("3".equals(priceBillDetailBean.getPropertyType())){
                tv_price_bill_detail_house_property.setText("别墅");
            }else if ("4".equals(priceBillDetailBean.getPropertyType())){
                tv_price_bill_detail_house_property.setText("商业");
            }else if ("5".equals(priceBillDetailBean.getPropertyType())){
                tv_price_bill_detail_house_property.setText("办公");
            }else if ("6".equals(priceBillDetailBean.getPropertyType())){
                tv_price_bill_detail_house_property.setText("其他");
            }
        }

        //图片列表
        if (propertyCertUrlList != null) {
            for (int i = 0; i < propertyCertUrlList.size(); i++) {
                PriceBillDetailBean.PropertyCertBean housePicBean = propertyCertUrlList.get(i);
                if (housePicBean != null && !StringUtils.isEmpty(housePicBean.getUrl())) {
                    loadHousePic(housePicBean.getUrl(), i);
                }
            }
        }
    }


    private void loadHousePic(String retMsg, int index) {
        if(StringUtils.isEmpty(retMsg)) {
            Glide.with(this).load(R.drawable.icon_load_img_fail).into(getDisplayView(index));
            return;
        }
        byte[] stream = Base64.decode(retMsg, Base64.DEFAULT);
        String str = StringUtils.byteArrayToStr(stream);
        if(!str.contains("<!DOCTYPE")) {
//                    if(255 != stream.length) {
            Glide.with(this).load(stream).into(getDisplayView(index));
        } else {
            Glide.with(this).load(R.drawable.icon_load_img_fail).into(getDisplayView(index));
        }
    }

    private ImageView getDisplayView(int index) {
        ImageView iv_house = new ImageView(this);
        switch (index) {
            case 0:
                iv_house = iv_house_pic1;
                break;
            case 1:
                iv_house = iv_house_pic2;
                break;
            case 2:
                iv_house = iv_house_pic3;
                break;
        }
        return iv_house;
    }
}
