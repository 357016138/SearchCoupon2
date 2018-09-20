package com.search.coupon.agent.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.search.coupon.agent.R;
import com.search.coupon.agent.common.BaseActivity;
import com.search.coupon.agent.network.UrlConfig;
import com.search.coupon.agent.utils.DeviceUtils;
import com.search.coupon.agent.utils.UserManager;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author baipeng
 * @Title ReportApplySuccessActivity
 * @Date 2018/2/28 9:42
 * @Description ReportApplySuccessActivity.
 */
public class ReportApplySuccessActivity extends BaseActivity {
    @BindView(R.id.btn_detail)
    TextView btn_detail;
    @BindView(R.id.btn_return)
    TextView btn_return;
    private String orderNum;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_report_apply_success);
    }

    @Override
    public void dealLogicBeforeInitView() {
        Bundle bundle = getIntentData();
        orderNum = bundle.getString("orderNum");
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        topBar.setTitle("报单成功");
        topBar.setLineVisible(true);
    }

    @Override
    public void dealLogicAfterInitView() {

    }

    @OnClick({R.id.btn_return, R.id.btn_detail})
    @Override
    public void onClickEvent(View view) {

        switch (view.getId()) {
            case R.id.btn_return:
                Bundle bd = new Bundle();
                bd.putInt(MainActivity.CURRENT_POSITION, 0);
                bd.putBoolean(MainActivity.IS_CLEAR_FRAGMENT, true);
                goPage(MainActivity.class, bd);
                break;
            case R.id.btn_detail:
                if (!isLogin()) return;
                goWebPage("贷款订单详情", String.format(Locale.US, UrlConfig.URL_LOAN_ORDER_DETAIL,
                        DeviceUtils.getDeviceUniqueId(this), orderNum, UserManager.getUserId()));
                this.finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void OnTopLeftClick() {
        Bundle bd = new Bundle();
        bd.putInt(MainActivity.CURRENT_POSITION, 1);
        bd.putBoolean(MainActivity.IS_CLEAR_FRAGMENT, true);
        goPage(MainActivity.class, bd);
    }

    @Override
    public void OnTopRightClick() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
