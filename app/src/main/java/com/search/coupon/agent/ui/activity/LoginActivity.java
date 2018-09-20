package com.search.coupon.agent.ui.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.search.coupon.agent.R;
import com.search.coupon.agent.bean.UserBean;
import com.search.coupon.agent.common.BaseActivity;
import com.search.coupon.agent.common.Constants;
import com.search.coupon.agent.common.ShareData;
import com.search.coupon.agent.network.RequestParams;
import com.search.coupon.agent.network.ResultData;
import com.search.coupon.agent.network.Task;
import com.search.coupon.agent.network.UrlConfig;
import com.search.coupon.agent.service.MessageEvent;
import com.search.coupon.agent.utils.DeviceUtils;
import com.search.coupon.agent.utils.Md5Util;
import com.search.coupon.agent.utils.PhoneNumberTextWatcher;
import com.search.coupon.agent.utils.UserUtils;
import com.search.coupon.agent.utils.UtilTools;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.search.coupon.agent.common.Keys.KEY_LOGIN_CAN_BACK;

/**
 * Created by song on 2018/1/17 0017.
 */
public class LoginActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener,View.OnFocusChangeListener {

    @BindView(R.id.login_uerName)
    EditText login_uerName;
    @BindView(R.id.login_password_input)
    EditText login_password_input;
    @BindView(R.id.login_showPassWord)
    CheckBox login_showPassWord;
    @BindView(R.id.login_sign)
    TextView login_sign;
    @BindView(R.id.login_forgetPassword)
    TextView login_forgetPassword;
    @BindView(R.id.login_loginButton)
    TextView login_loginButton;
    @BindView(R.id.ll_btn)
    LinearLayout ll_btn;
    @BindView(R.id.iv_del_username)
    ImageView iv_del_username;
    @BindView(R.id.iv_del_password)
    ImageView iv_del_password;
    private String userNaS;
    private String passWoS;
    private boolean isCanBack;
    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_login);
    }

    @Override
    public void dealLogicBeforeInitView() {
        isCanBack = getIntentData().getBoolean(KEY_LOGIN_CAN_BACK, true);
        userNaS = getBundleStr("UserName");
        passWoS = getBundleStr("passWord");

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        login_showPassWord.setOnCheckedChangeListener(this);
        topBar.setLeftImage(R.drawable.icon_login_close);
        topBar.setLineVisible(false);
        if (!isCanBack) topBar.setLeftVisible(false);
        login_uerName.addTextChangedListener(new PhoneNumberTextWatcher(login_uerName));
        login_uerName.addTextChangedListener(watcher);
        login_password_input.addTextChangedListener(watcher);
        login_uerName.setOnFocusChangeListener(this);
        login_password_input.setOnFocusChangeListener(this);
    }

    @Override
    public void dealLogicAfterInitView() {

        if (!TextUtils.isEmpty(userNaS)) {
            login_uerName.setText(userNaS);
        } else {
            userNaS = ShareData.getShareStringData(ShareData.LAST_ACCOUNT);
            login_uerName.setText(userNaS);
        }
        if (passWoS != null) {
            login_password_input.setText(passWoS);
        }
    }
    @OnClick({R.id.login_sign, R.id.login_forgetPassword,R.id.login_loginButton, R.id.iv_del_username, R.id.iv_del_password})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.iv_del_username:      //删除手机号
                login_uerName.setText("");
                break;
            case R.id.iv_del_password:      //删除密码
                login_password_input.setText("");
                break;
            case R.id.login_sign:      //注册
                goPage(RegistActivity.class);
//                finish();
            break;
            case R.id.login_forgetPassword:   //忘记密码
                goPage(ForgetPasswordActivity.class);
            break;
            case R.id.login_loginButton:    //登录
                loginMothed();
            break;
            default:
                break;
        }
    }
    @Override
    public void OnTopLeftClick() {
        if (isCanBack) finish();

    }

    @Override
    public void OnTopRightClick() {

    }

    /**
     * 是否明文密码的回调方法
     * */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            login_password_input.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            login_password_input.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    private void loginMothed() {
        String userNameStr = login_uerName.getText().toString().replace(" ","");
        String passWordStr = login_password_input.getText().toString().trim();

        if (userNameStr == null || userNameStr.length() == 0) {
            toast("请输入帐号");
            return;
        } else if (userNameStr.length() != 11) {
            toast("手机号码不正确");
            return;
        } else {
            String startElement = userNameStr.substring(0, 1);
            if (!startElement.equals("1")) {
                toast("手机号码不正确");
                return;
            }
        }
        if (passWordStr == null || passWordStr.length() == 0) {
            toast("请输入密码");
            return;
        } else {
            boolean isTrue = UtilTools.checkPassWordInput(passWordStr);
            if (!isTrue) {
                toast("请输入6-12位字母+数字密码");
                return;
            }
        }

        RequestParams params = new RequestParams(UrlConfig.URL_LOGIN);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("phoneNumber", userNameStr);
        params.add("password", Md5Util.MD5(passWordStr));
        startRequest(Task.LOGIN, params, UserBean.class);
    }

    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);
        switch (tag) {
            case Task.LOGIN:
                if (handlerRequestErr(data)) {
                    UserBean userBean = (UserBean) data.getBody();
                    UserUtils.saveLoginUserInfo(userBean);
                    ShareData.setShareStringData(ShareData.USER_COOKIE, data.getHeader());
                    ShareData.setShareStringData(ShareData.LAST_ACCOUNT, login_uerName.getText().toString());
                    EventBus.getDefault().post(new MessageEvent(Constants.GET_NEW_MSG));
                    finish();

                }
                break;
            default:
                break;
        }
    }


    private boolean flag1 = false;
    private boolean flag2 = false;
    /**
     * 监听所有EditText是否输入内容  然后判断高亮登录按钮，并可点击
     * */
    private TextWatcher watcher = new TextWatcher() {
        @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {

          }
          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {
              //每次有字符输入获取两个EditText是否有文字状态
              flag1 = login_uerName.getText().toString().trim().length() > 0;
              flag2 = login_password_input.getText().toString().trim().length() > 0;
              if (flag1 && uerNameHasFocus){
                  iv_del_username.setVisibility(View.VISIBLE);
              }else {
                  iv_del_username.setVisibility(View.GONE);
              }
              if (flag2 && passWordHasFocus){
                  iv_del_password.setVisibility(View.VISIBLE);
              }else {
                  iv_del_password.setVisibility(View.GONE);
              }
              //如果都有文字，高亮登录按钮 并设置可点击
              if (flag1 && flag2 ) {
                  login_loginButton.setEnabled(true);
                  login_loginButton.setBackground(getResources().getDrawable(R.drawable.bg_login_button));
                  ll_btn.setBackground(getResources().getDrawable(R.drawable.bg_loan_pic_shadow));
              } else {
                  login_loginButton.setEnabled(false);
                  login_loginButton.setBackground(getResources().getDrawable(R.drawable.bg_button_disable));
                  ll_btn.setBackground(null);
              }
          }
         @Override
         public void afterTextChanged(Editable s) {
         }
     };


    private boolean uerNameHasFocus = false;
    private boolean passWordHasFocus = false;

    /**
     * EditText 焦点监听
     * */
    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.login_uerName:      //手机号焦点变化
                uerNameHasFocus = hasFocus;
                if (hasFocus && flag1){
                    iv_del_username.setVisibility(View.VISIBLE);
                }else{
                    iv_del_username.setVisibility(View.GONE);
                }
                break;
            case R.id.login_password_input:      //密码焦点变化
                passWordHasFocus = hasFocus;
                if (hasFocus && flag2){
                    iv_del_password.setVisibility(View.VISIBLE);
                }else{
                    iv_del_password.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }
}
