package com.search.coupon.agent.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.search.coupon.agent.R;
import com.search.coupon.agent.common.BaseActivity;
import com.search.coupon.agent.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author baipeng
 * @Title AskPriceResultActivity
 * @Date 2018/2/28 17:21
 * @Description AskPriceResultActivity.
 */
public class AskPriceResultActivity extends BaseActivity {
    @BindView(R.id.btn_detail)
    TextView btn_detail;
    @BindView(R.id.btn_return)
    TextView btn_return;
    @BindView(R.id.tv_house_name)
    TextView tv_house_name;
    private String estateName, inquiryCode;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_ask_price_result);
    }

    @Override
    public void dealLogicBeforeInitView() {
        Bundle bundle = getIntentData();
        estateName = bundle.getString("estate_name");
        inquiryCode = bundle.getString("inquiryCode");
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        topBar.setTitle("询价结果");
        topBar.setLineVisible(true);
    }

    @Override
    public void dealLogicAfterInitView() {
        if(!StringUtils.isEmpty(estateName))
            tv_house_name.setText(estateName);
    }

    @OnClick({R.id.btn_return, R.id.btn_detail})
    @Override
    public void onClickEvent(View view) {
        Bundle bd = new Bundle();
        switch (view.getId()) {
            case R.id.btn_return:

                goPage(MainActivity.class, bd);
                this.finish();
                break;
            case R.id.btn_detail:
                bd.putString("inquiryCode", inquiryCode);
                goPage(PriceBillDetailActivity.class, bd);
                this.finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void OnTopLeftClick() {
        Bundle bd = new Bundle();

        goPage(MainActivity.class, bd);
        this.finish();
    }

    @Override
    public void OnTopRightClick() {

    }
}
