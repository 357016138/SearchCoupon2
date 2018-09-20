package com.search.coupon.agent.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.search.coupon.agent.R;
import com.search.coupon.agent.common.BaseActivity;
import com.search.coupon.agent.common.Constants;
import com.search.coupon.agent.network.RequestParams;
import com.search.coupon.agent.network.ResultData;
import com.search.coupon.agent.network.Task;
import com.search.coupon.agent.network.UrlConfig;
import com.search.coupon.agent.utils.DeviceUtils;
import com.search.coupon.agent.utils.PhoneNumberTextWatcher;
import com.search.coupon.agent.utils.StringUtils;
import com.search.coupon.agent.utils.UserManager;
import com.search.coupon.agent.utils.UserUtils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 设置/重置提现密码页面
 */
public class SetWithdrawPasswordActivity extends BaseActivity implements View.OnFocusChangeListener {

    @BindView(R.id.forgetPassWord_uerName)
    EditText forgetPassWord_uerName;
    @BindView(R.id.forgetPassWord_code_input)
    EditText forgetPassWord_code_input;
    @BindView(R.id.forgetPassWord_obtainCode)
    TextView forgetPassWord_obtainCode;
    @BindView(R.id.forgetPassWord_button)
    TextView forgetPassWord_button;
    @BindView(R.id.iv_del_username)
    ImageView iv_del_username;
    @BindView(R.id.iv_del_obtainCode)
    ImageView iv_del_obtainCode;
    private boolean isTimeing = false;//是否是倒计时状态
    private String userNameStr;
    private String codeStr;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_set_withdraw_password);
    }

    @Override
    public void dealLogicBeforeInitView() {
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        topBar.setTitle("设置提现密码");
        if ("1".equals(UserManager.getIsPayPass())) {
            topBar.setTitle("修改提现密码");
        }
        topBar.setLineVisible(true);
        forgetPassWord_uerName.addTextChangedListener(new PhoneNumberTextWatcher(forgetPassWord_uerName));
        forgetPassWord_uerName.addTextChangedListener(watcher);
        forgetPassWord_code_input.addTextChangedListener(watcher);
        forgetPassWord_uerName.setOnFocusChangeListener(this);
        forgetPassWord_code_input.setOnFocusChangeListener(this);
        forgetPassWord_obtainCode.setOnClickListener(null);
    }

    @Override
    public void dealLogicAfterInitView() {

        if (UserUtils.isLogin() && !StringUtils.isEmpty(UserManager.getPhone())) {
            forgetPassWord_uerName.setText(UserManager.getPhone());
            forgetPassWord_uerName.setFocusable(false);
        }

    }

    @OnClick({R.id.forgetPassWord_obtainCode, R.id.forgetPassWord_button, R.id.iv_del_username, R.id.iv_del_obtainCode})
    @Override
    public void onClickEvent(View view) {

        switch (view.getId()) {
            case R.id.iv_del_username:
                forgetPassWord_uerName.setText("");
                iv_del_username.setVisibility(View.GONE);
                break;
            case R.id.iv_del_obtainCode:
                forgetPassWord_code_input.setText("");
                iv_del_obtainCode.setVisibility(View.GONE);
                break;
            case R.id.forgetPassWord_obtainCode:
                obtainCode();
                break;
            case R.id.forgetPassWord_button:
                userNameStr = forgetPassWord_uerName.getText().toString().replace(" ", "");
                codeStr = forgetPassWord_code_input.getText().toString();
                if (userNameStr == null || userNameStr.trim().length() == 0) {
                    toast("请输入手机号码");
                    return;
                } else if (userNameStr.trim().length() != 11) {
                    toast("请输入11位数字手机号码");
                    return;
                } else {
                    String startElement = userNameStr.substring(0, 1);
                    if (!startElement.equals("1")) {
                        toast("手机号码不正确");
                        return;
                    }
                }
                if (codeStr == null || codeStr.trim().length() <= 0) {
                    toast("请输入验证码");
                    return;
                }
                checkSMCode(userNameStr, codeStr);
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

    private void obtainCode() {

        String userNameStr = forgetPassWord_uerName.getText().toString().replace(" ", "");
        if (userNameStr == null || userNameStr.trim().length() == 0) {
            toast("请输入手机号码");
            return;
        } else if (userNameStr.trim().length() != 11) {
            toast("手机号码不正确");
            return;
        } else {
            String startElement = userNameStr.substring(0, 1);
            if (!startElement.equals("1")) {
                toast("手机号码不正确");
                return;
            }
        }
        getCode(userNameStr);
    }

    private void getCode(String name) {
        RequestParams params = new RequestParams(UrlConfig.URL_SIGN_IN_CODE);
        params.add("phone", name);
        params.add("codeType", "D");//发送类型 A-注册，B-密码找回，C-绑卡 D设置支付密码
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        startRequest(Task.SIGN_UP_CODE, params, null);

    }

    /**
     * 验证短信验证码
     */
    private void checkSMCode(String phone, String smCode) {
        RequestParams params = new RequestParams(UrlConfig.URL_CHECKSMCODE);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("phone", phone);
        params.add("codeType", "D");//发送类型 A-注册，B-密码找回，C-绑卡 D设置支付密码
        params.add("smCode", smCode);
        startRequest(Task.CHECKSMCODE, params, null);
    }

    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);

        if (data != null) {
            switch (tag) {
                case Task.SIGN_UP_CODE:
                    if (handlerRequestErr(data)) {
                        obtianCodeMothed();
                    }
                    break;
                case Task.CHECKSMCODE:
                    if (handlerRequestErr(data)) {
                        Bundle bd = new Bundle();
                        bd.putString("userNameS", userNameStr);
                        bd.putString("codeStr", codeStr);
                        goPage(SetWithdrawPassword2Activity.class, bd, Constants.FLAG_FORGET_PASSWORD);
                    }
                    break;
                default:
                    break;

            }
        }
    }

    private int codeTime = 60;

    private boolean obtianCodeMothed() {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                msg.arg1 = codeTime;
                handler.sendMessage(msg);
                if (codeTime >= 0) {
                    codeTime -= 1;
                }
                if (codeTime == -1) {
                    codeTime = 60;
                    timer.cancel();
                }
            }
        }, 0, 1000);

        return true;
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg != null) {
                int tag = msg.what;
                int addMsg = msg.arg1;
                if (addMsg > 0) {
                    forgetPassWord_obtainCode.setText("" + addMsg + "s");
                    isTimeing = true;
                    forgetPassWord_obtainCode.setTextColor(getResources().getColor(R.color.color_A2A2A2));
                    forgetPassWord_obtainCode.setOnClickListener(null);
                } else {
                    forgetPassWord_obtainCode.setText("获取验证码");
                    isTimeing = false;
                    setObtainCodeColor();
                }
            }
            return false;
        }
    });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.FLAG_FORGET_PASSWORD:
                    setResult(RESULT_OK, data);
                    this.finish();
                    break;
            }
        }
    }

    private boolean flag1 = false;
    private boolean flag2 = false;
    /**
     * 监听所有EditText是否输入内容  然后判断高亮button
     */
    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            boolean flag1 = forgetPassWord_uerName.getText().toString().trim().length() > 0;
            boolean flag2 = forgetPassWord_code_input.getText().toString().trim().length() > 0;
            /**
             * 根据手机号情况设置获取验证码按钮颜色
             * */
            setObtainCodeColor();

            if (flag1 && uerNameHasFocus) {
                iv_del_username.setVisibility(View.VISIBLE);
            }
            if (flag2 && codeFocus) {
                iv_del_obtainCode.setVisibility(View.VISIBLE);
            }

            if (flag1 && flag2) {
                forgetPassWord_button.setEnabled(true);
                forgetPassWord_button.setBackground(getResources().getDrawable(R.drawable.bg_login_button));
            } else {
                forgetPassWord_button.setBackground(getResources().getDrawable(R.drawable.bg_button_disable));
                forgetPassWord_button.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void setObtainCodeColor() {

        int length = forgetPassWord_uerName.getText().toString().replace(" ", "").length();
        if (length == 11 && !isTimeing) {
            forgetPassWord_obtainCode.setTextColor(getResources().getColor(R.color.color_3889FF));
            forgetPassWord_obtainCode.setOnClickListener(this);
        } else {
            forgetPassWord_obtainCode.setTextColor(getResources().getColor(R.color.color_A2A2A2));
            forgetPassWord_obtainCode.setOnClickListener(null);
        }

    }

    private boolean uerNameHasFocus = false;
    private boolean codeFocus = false;

    /**
     * EditText 焦点监听
     */
    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.forgetPassWord_uerName:      //手机号焦点变化
                uerNameHasFocus = hasFocus;
                if (hasFocus && flag1) {
                    iv_del_username.setVisibility(View.VISIBLE);
                } else {
                    iv_del_username.setVisibility(View.GONE);
                }
                break;
            case R.id.forgetPassWord_code_input:      //密码焦点变化
                codeFocus = hasFocus;
                if (hasFocus && flag2) {
                    iv_del_obtainCode.setVisibility(View.VISIBLE);
                } else {
                    iv_del_obtainCode.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

}
