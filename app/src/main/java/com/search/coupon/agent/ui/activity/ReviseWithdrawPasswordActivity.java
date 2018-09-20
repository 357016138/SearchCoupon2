package com.search.coupon.agent.ui.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.search.coupon.agent.R;
import com.search.coupon.agent.common.BaseActivity;
import com.search.coupon.agent.network.RequestParams;
import com.search.coupon.agent.network.ResultData;
import com.search.coupon.agent.network.Task;
import com.search.coupon.agent.network.UrlConfig;
import com.search.coupon.agent.utils.AESUtils;
import com.search.coupon.agent.utils.DeviceUtils;
import com.search.coupon.agent.utils.UserManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
/**
 * 修改提现密码页面
 * */
public class ReviseWithdrawPasswordActivity extends BaseActivity implements View.OnFocusChangeListener {
    @BindView(R.id.resetPassWord_oldPassword_input)
    EditText resetPassWord_oldPassword_input;
    @BindView(R.id.resetPassWord_password_input)
    EditText resetPassWord_password_input;
    @BindView(R.id.resetPassWord_ConfirmPassWord)
    EditText resetPassWord_ConfirmPassWord;
    @BindView(R.id.resetPassWord_button)
    TextView resetPassWord_button;
    @BindView(R.id.iv_del_oldpassword)
    ImageView iv_del_oldpassword;
    @BindView(R.id.iv_del_password)
    ImageView iv_del_password;
    @BindView(R.id.iv_del_confirmpassword)
    ImageView iv_del_confirmpassword;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_revise_withdraw_password);
    }

    @Override
    public void dealLogicBeforeInitView() {

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        topBar.setTitle("修改提现密码");
        topBar.setLineVisible(true);
        resetPassWord_oldPassword_input.addTextChangedListener(watcher);
        resetPassWord_password_input.addTextChangedListener(watcher);
        resetPassWord_ConfirmPassWord.addTextChangedListener(watcher);
        resetPassWord_oldPassword_input.setOnFocusChangeListener(this);
        resetPassWord_password_input.setOnFocusChangeListener(this);
        resetPassWord_ConfirmPassWord.setOnFocusChangeListener(this);

    }

    @Override
    public void dealLogicAfterInitView() {

    }

    @OnClick({R.id.resetPassWord_button, R.id.iv_del_oldpassword, R.id.iv_del_password, R.id.iv_del_confirmpassword})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.resetPassWord_button:
                getDataFromView();
                break;
            case R.id.iv_del_oldpassword:
                break;
            case R.id.iv_del_password:
                break;
            case R.id.iv_del_confirmpassword:
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

    private void getDataFromView() {

        String passWordOldStr = resetPassWord_oldPassword_input.getText().toString();
        String passWordStr = resetPassWord_password_input.getText().toString();
        String confirmPassWordStr = resetPassWord_ConfirmPassWord.getText().toString();

        if (passWordOldStr == null || passWordOldStr.trim().length() <= 0) {
            toast("请输入原密码");
            return;
        } else {
            if (passWordOldStr.trim().length() != 6) {
                toast("原密码必须为6位数字");
                return;
            }
        }

        if (passWordStr == null || passWordStr.trim().length() <= 0) {
            toast("请输入新密码");
            return;
        } else {
            if (passWordStr.trim().length() != 6) {
                toast("新密码必须为6位数字");
                return;
            }
        }

        if (confirmPassWordStr == null || confirmPassWordStr.trim().length() <= 0) {
            toast("请再次输入新密码");
            return;
        } else if (!passWordStr.equals(confirmPassWordStr)) {
            toast("两次输入的新密码不同");
            return;
        }

        submit(passWordOldStr, passWordStr);
    }

    private void submit(String passWordOldStr, String passWord) {

        RequestParams params = new RequestParams(UrlConfig.URL_UPDATE_PAY_PASSWORD);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("userId", UserManager.getUserId());
        params.add("passwordOld", AESUtils.aesEncryptStr(passWordOldStr, UrlConfig.KEY));
        params.add("passwordNew", AESUtils.aesEncryptStr(passWord, UrlConfig.KEY));
        startRequest(Task.UPDATE_PAY_PASSWORD, params, null);
    }

    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);
        switch (tag) {
            case Task.UPDATE_PAY_PASSWORD:
                if (handlerRequestErr(data)) {
                    toast("密码重置成功");
                    finish();
                }
                break;
        }
    }

    private boolean flag1 = false;
    private boolean flag2 = false;
    private boolean flag3 = false;
    /**
     * 监听所有EditText是否输入内容  然后判断高亮button
     * */
    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            flag1 = resetPassWord_oldPassword_input.getText().toString().trim().length() > 0;
            flag2 = resetPassWord_password_input.getText().toString().trim().length() > 0;
            flag3 = resetPassWord_ConfirmPassWord.getText().toString().trim().length() > 0;

            if (flag1 && oldPassWordHasFocus){
                iv_del_oldpassword.setVisibility(View.VISIBLE);
            }else {
                iv_del_oldpassword.setVisibility(View.GONE);
            }
            if (flag2 && passWordHasFocus){
                iv_del_password.setVisibility(View.VISIBLE);
            }else {
                iv_del_password.setVisibility(View.GONE);
            }
            if (flag3 && ConfirmPassWordHasFocus){
                iv_del_confirmpassword.setVisibility(View.VISIBLE);
            }else {
                iv_del_confirmpassword.setVisibility(View.GONE);
            }

            if (flag1 && flag2 && flag3) {
                resetPassWord_button.setEnabled(true);
                resetPassWord_button.setBackground(getResources().getDrawable(R.drawable.bg_login_button));
            } else {
                resetPassWord_button.setBackground(getResources().getDrawable(R.drawable.bg_button_disable));
                resetPassWord_button.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private boolean oldPassWordHasFocus = false;
    private boolean passWordHasFocus = false;
    private boolean ConfirmPassWordHasFocus = false;

    /**
     * EditText 焦点监听
     * */
    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.resetPassWord_oldPassword_input:      //旧密码焦点变化
                oldPassWordHasFocus = hasFocus;
                if (hasFocus && flag1){
                    iv_del_oldpassword.setVisibility(View.VISIBLE);
                }else{
                    iv_del_oldpassword.setVisibility(View.GONE);
                }
                break;
            case R.id.resetPassWord_password_input:        //新密码焦点变化
                passWordHasFocus = hasFocus;
                if (hasFocus && flag2){
                    iv_del_password.setVisibility(View.VISIBLE);
                }else{
                    iv_del_password.setVisibility(View.GONE);
                }
                break;
            case R.id.resetPassWord_ConfirmPassWord:      //确认新密码焦点变化
                ConfirmPassWordHasFocus = hasFocus;
                if (hasFocus && flag3){
                    iv_del_confirmpassword.setVisibility(View.VISIBLE);
                }else{
                    iv_del_confirmpassword.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

}
