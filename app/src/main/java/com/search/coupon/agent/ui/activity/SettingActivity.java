package com.search.coupon.agent.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.search.coupon.agent.R;
import com.search.coupon.agent.bean.IsPayPasswordBean;
import com.search.coupon.agent.common.BaseActivity;
import com.search.coupon.agent.common.Constants;
import com.search.coupon.agent.common.ShareData;
import com.search.coupon.agent.network.ResultData;
import com.search.coupon.agent.network.Task;
import com.search.coupon.agent.service.MessageEvent;
import com.search.coupon.agent.utils.DeviceUtils;
import com.search.coupon.agent.utils.UserUtils;
import com.search.coupon.agent.view.SwitchButton;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.search.coupon.agent.common.Keys.KEY_GESTURE_TYPE;
import static com.search.coupon.agent.common.Keys.KEY_H5_RESULT_CODE;

/**
 * 设置页面
 */
public class SettingActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.tv_reset_login)
    TextView tvResetLogin;
    @BindView(R.id.btn_gesture)
    SwitchButton btnGesture;
    @BindView(R.id.tv_reset_gesture)
    TextView tvResetGesture;
    @BindView(R.id.tv_revise_withdraw)
    TextView tv_revise_withdraw;
    @BindView(R.id.tv_reset_withdraw)
    TextView tv_reset_withdraw;
    @BindView(R.id.tv_version)
    TextView tv_version;

    @BindView(R.id.btn_sign_out)
    Button btnSignOut;
    @BindView(R.id.btn_gestureTV)
    TextView btn_gestureTV;      //手势 打开或关闭状态

    private int showMarketSource;
    private boolean isCheck;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_set);
    }

    @Override
    public void dealLogicBeforeInitView() {

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        topBar.setTitle("设置");

    }

    @Override
    public void dealLogicAfterInitView() {

        tv_version.setText("版本号 V" + DeviceUtils.getCurrentAppVersionName(this));

    }

    @Override
    public void OnTopLeftClick() {
        finish();
    }

    @Override
    public void OnTopRightClick() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        setSwitchButton();
    }

    @OnClick({R.id.tv_reset_login, R.id.tv_reset_gesture, R.id.tv_reset_withdraw, R.id.tv_revise_withdraw, R.id.btn_sign_out, R.id.btn_gestureTV})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.tv_reset_login:
                Intent intent = new Intent(SettingActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
                break;

            case R.id.tv_reset_gesture:
                resetgesture();
                break;

            case R.id.tv_revise_withdraw:     //修改提现密码
                goPage(SetWithdrawPasswordActivity.class);
                break;

            case R.id.tv_reset_withdraw:     //重置提现密码
                goPage(SetWithdrawPasswordActivity.class);
                break;

            case R.id.btn_sign_out:
                loginOut();
                break;

            case R.id.btn_gestureTV:
                String marketSourceStr = DeviceUtils.getCurrentAppMarketSource(this);
                showMarketSource += 1;
                if (showMarketSource % 10 == 0) {
                    Toast.makeText(this, marketSourceStr, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void setSwitchButton() {
        boolean isLock = ShareData.getShareBooleanData(ShareData.GESTURE_IS_OPEN);
        btnGesture.setChecked(isLock);
        isCheck = isLock;
        btnGesture.setOnCheckedChangeListener(this);

        if (isLock)
            btn_gestureTV.setText("手势密码: 开启");
        else
            btn_gestureTV.setText("手势密码: 关闭");
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isCheck == isChecked) return;
        Bundle bd = new Bundle();
        if (isChecked) {
            bd.putString(KEY_GESTURE_TYPE, GestureSetActivity.TYPE_OPEN);
            goPage(GestureSetActivity.class, bd);
        } else {
            bd.putString(KEY_GESTURE_TYPE, GestureUnlockActivity.TYPE_CLOSE);
            goPage(GestureUnlockActivity.class, bd);
        }
    }

    private void resetgesture() {
        boolean isOpen = ShareData.getShareBooleanData(ShareData.GESTURE_IS_OPEN);
        if (!isOpen) {
            toast("请先开启手势密码");
            return;
        }
        Bundle bd = new Bundle();
        bd.putString(KEY_GESTURE_TYPE, GestureUnlockActivity.TYPE_RESET);
        goPage(GestureUnlockActivity.class, bd);

    }

    private void loginOut() {
        UserUtils.loginOut();
        finish();
        EventBus.getDefault().post(new MessageEvent(Constants.GET_NEW_MSG));
        EventBus.getDefault().post(new MessageEvent(Constants.GET_REFRESH_ORDER_LIST));
    }

//    /**
//     * 退出登录
//     * */
//    private void checkTokenValidity() {
//        RequestParams params = new RequestParams(UrlConfig.URL_LOGIN_OUT);
//        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
//        params.add("userId", ShareData.getShareStringData(ShareData.USER_ID));
//        startRequest(Task.LOGIN_OUT, params, null, false);
//    }


    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);
        switch (tag) {
            case Task.LOGIN_OUT:
                if (handlerRequestErr(data)) {
                    UserUtils.loginOut();
                    Bundle bd = new Bundle();

                    goPage(MainActivity.class, bd);
                }
                break;

            case Task.WHETHER_SET_PAY_PASSWORD:
                if (handlerRequestErr(data)) {

                    IsPayPasswordBean isPayPasswordBean = (IsPayPasswordBean) data.getBody();
                    if ("0".equals(isPayPasswordBean.getIsPayPass())) {
                        tv_reset_withdraw.setVisibility(View.GONE);
                    } else if ("1".equals(isPayPasswordBean.getIsPayPass())) {
                        tv_revise_withdraw.setVisibility(View.GONE);
                    }

                }
                break;

            default:
                break;


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.RESET_BILL_PASSWORD_REQUEST_CODE) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                String resultCod = bundle.getString(KEY_H5_RESULT_CODE);
                if (resultCod.equals("1")) {
                    Toast.makeText(this, "密码已重置", Toast.LENGTH_SHORT).show();
                } else if (resultCod.equals("0")) {
                    Toast.makeText(this, "密码修改失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


}
