package com.search.coupon.agent.ui.activity;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.search.coupon.agent.R;
import com.search.coupon.agent.bean.DataBean;
import com.search.coupon.agent.bean.OpenCityBean;
import com.search.coupon.agent.bean.UserBean;
import com.search.coupon.agent.common.BaseActivity;
import com.search.coupon.agent.network.RequestParams;
import com.search.coupon.agent.network.ResultData;
import com.search.coupon.agent.network.Task;
import com.search.coupon.agent.network.UrlConfig;
import com.search.coupon.agent.utils.CodeUtils;
import com.search.coupon.agent.utils.DeviceUtils;
import com.search.coupon.agent.utils.Md5Util;
import com.search.coupon.agent.utils.PhoneNumberTextWatcher;
import com.search.coupon.agent.utils.StringUtils;
import com.search.coupon.agent.utils.UtilTools;
import com.search.coupon.agent.view.wheelview.adapter.ArrayWheelAdapter;
import com.search.coupon.agent.view.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by song on 2018/1/31 0017.
 * 注册的界面
 */
public class RegistActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener,View.OnFocusChangeListener {

    @BindView(R.id.signup_uerName)
    EditText signup_uerName;
    @BindView(R.id.signup_password_input)
    EditText signup_password_input;
    @BindView(R.id.signup_code_input)
    EditText signup_code_input;
    @BindView(R.id.signup_city_input)
    TextView signup_city_input;
    @BindView(R.id.signup_showPassWord)
    CheckBox signup_showPassWord;
    @BindView(R.id.signup_obtainCode)
    TextView signup_obtainCode;
    @BindView(R.id.signup_signupButton)
    TextView signup_signupButton;
    @BindView(R.id.signup_belowFiv)
    TextView signup_belowFiv;
    @BindView(R.id.signup_inviter_input)
    EditText signup_inviter_input;
    @BindView(R.id.signup_go_login)
    LinearLayout signup_go_login;
    @BindView(R.id.iv_del_username)
    ImageView iv_del_username;
    @BindView(R.id.iv_del_obtainCode)
    ImageView iv_del_obtainCode;
    @BindView(R.id.iv_del_PassWord)
    ImageView iv_del_PassWord;
    @BindView(R.id.iv_del_inviter)
    ImageView iv_del_inviter;
    @BindView(R.id.iv_pic_code)
    ImageView iv_pic_code;
    @BindView(R.id.et_pic_code)
    EditText et_pic_code;
    @BindView(R.id.ll_btn)
    LinearLayout ll_btn;

    private String picCode="";   //图片验证码
    private String phoneNum;//记录获取验证码的手机号和注册的手机号对比验证。
    private String userNameS;
    private String passwordS;
    private boolean isAgree = true;//是否同意注册协议；同意：true；不同意：false;
    private List<OpenCityBean.CityBean> cityList;
    private String cityCode;
    private boolean isTimeing = false;//是否是倒计时状态
    private String acceptCodeStr;   //接收到的短信验证码


    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_regist);
    }

    @Override
    public void dealLogicBeforeInitView() {

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        signup_showPassWord.setOnCheckedChangeListener(this);
        topBar.setTitle("注册");
        topBar.setLineVisible(true);
        signup_uerName.addTextChangedListener(new PhoneNumberTextWatcher(signup_uerName));
        signup_inviter_input.addTextChangedListener(new PhoneNumberTextWatcher(signup_inviter_input));
        signup_uerName.addTextChangedListener(watcher);
        signup_code_input.addTextChangedListener(watcher);
        signup_password_input.addTextChangedListener(watcher);
        signup_inviter_input.addTextChangedListener(watcher);

        signup_uerName.setOnFocusChangeListener(this);
        signup_code_input.setOnFocusChangeListener(this);
        signup_password_input.setOnFocusChangeListener(this);
        signup_inviter_input.setOnFocusChangeListener(this);
        signup_obtainCode.setOnClickListener(null);
        signup_signupButton.setEnabled(false);
    }

    @Override
    public void dealLogicAfterInitView() {
        createPicCode();
    }

    @Override
    public void OnTopLeftClick() {
        finish();
    }

    @Override
    public void OnTopRightClick() {

    }

    @OnClick({R.id.iv_pic_code,R.id.signup_obtainCode, R.id.signup_signupButton, R.id.signup_belowFiv, R.id.signup_go_login, R.id.rl_choses_city, R.id.iv_del_username, R.id.iv_del_obtainCode, R.id.iv_del_PassWord, R.id.iv_del_inviter})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.signup_obtainCode:
                if (codeTime == 60){
                    String mPicCode = et_pic_code.getText().toString().replace(" ","");
                    if (StringUtils.isEmpty(mPicCode)||!picCode.equals(mPicCode)) {
                        toast("请输入正确图片验证码");
                        return;
                    }
                    obtainCode();
                }
                break;
            case R.id.iv_pic_code:
                createPicCode();     //生成图片验证码
                break;
            case R.id.signup_signupButton:
                signUpMethod();
                break;
            case R.id.signup_belowFiv:
                goWebPage("用户服务协议", UrlConfig.H5_USER_REGISTER_PROTOCAL);
                break;
            case R.id.signup_go_login:
                goPage(LoginActivity.class);
                finish();
                break;
            case R.id.rl_choses_city:    //选择城市
                getOpenCity();
                break;
            case R.id.iv_del_username:
                signup_uerName.setText("");
                break;
            case R.id.iv_del_obtainCode:
                signup_code_input.setText("");
                break;
            case R.id.iv_del_PassWord:
                signup_password_input.setText("");
                break;
            case R.id.iv_del_inviter:
                signup_inviter_input.setText("");
                break;
            default:
                break;
        }
    }
    /**
     * 创建图片验证码
     * */
    private void createPicCode(){
        Bitmap bp = CodeUtils.getInstance().createBitmap();
        iv_pic_code.setImageBitmap(bp);
        picCode = CodeUtils.getInstance().getCode();
    }

    /**
     * 获取城市列表
     * */
    private void getOpenCity() {

        RequestParams params = new RequestParams(UrlConfig.URL_OPEN_CITY);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("userId", DeviceUtils.getDeviceUniqueId(this));
        startRequest(Task.OPEN_CITY, params,OpenCityBean.class);

    }

    private void obtainCode() {
        String userNameStr = signup_uerName.getText().toString().replace(" ","");
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
        phoneNum = userNameStr;
        getCode(userNameStr);
    }

    private void getCode(String phoneStr) {

        RequestParams params = new RequestParams(UrlConfig.URL_SIGN_IN_CODE);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("phoneNumber", phoneStr);
        params.add("sendType", 0);
        startRequest(Task.SIGN_UP_CODE, params, DataBean.class);
    }

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

    private int codeTime = 60;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg != null) {
                int tag = msg.what;
                int addMsg = msg.arg1;

                if (addMsg > 0) {
                    signup_obtainCode.setText("" + addMsg + "s");
                    isTimeing = true;
                    signup_obtainCode.setTextColor(getResources().getColor(R.color.color_A2A2A2));
                    signup_obtainCode.setOnClickListener(null);
                } else {
                    signup_obtainCode.setText("重新获取验证码");
                    isTimeing = false;
                    setObtainCodeColor();
                }

            }
            return false;
        }
    });

    //点击注册按钮发起网络请求
    private void signUpMethod() {
        //获取输入框文本 有空格的去掉空格
        String userNameStr = signup_uerName.getText().toString().replace(" ","");
        String passWordStr = signup_password_input.getText().toString();
        String codeStr = signup_code_input.getText().toString();
        String inviterStr = signup_inviter_input.getText().toString().replace(" ","");
        //判断手机号是否正确
        if (userNameStr == null || userNameStr.trim().length() == 0) {
            toast("请输入帐号");
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
        if (!userNameStr.equals(phoneNum)) {
            toast("手机号码与验证码手机号不同");
            return;
        }
        //判断是否未输密码 和密码格式是否正确
        if (passWordStr == null || passWordStr.trim().length() <= 0) {
            toast("请输入密码");
            return;
        } else {
            boolean isTrue = UtilTools.checkPassWordInput(passWordStr);
            if (!isTrue) {
                toast("请输入6-12位字母+数字密码");
                return;
            }
        }
        //判断验证码是否为空
        if (codeStr == null || codeStr.trim().length() <= 0) {
            toast("请输入验证码");
            return;
        }
        if (inviterStr.equals(userNameStr)) {
            toast("邀请码可以为空,但不能填写自己");
            return;
        }
        //发起网络请求
        if (acceptCodeStr.equals(codeStr)){
            RequestParams params = new RequestParams(UrlConfig.URL_REGISTER);
            params.add("pid", DeviceUtils.getDeviceUniqueId(this));
            params.add("phoneNumber", userNameStr);
            params.add("password", Md5Util.MD5(passWordStr));
            params.add("invitationCode", inviterStr);
            startRequest(Task.REGISTER, params, UserBean.class);
        }
    }

    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);
        if (data != null) {
            switch (tag) {
                case Task.REGISTER:
                    if (handlerRequestErr(data)) {
//                        UserBean userBean = (UserBean) data.getBody();
//                        UserUtils.saveLoginUserInfo(userBean);
//                        ShareData.setShareStringData(ShareData.USER_COOKIE, data.getHeader());
//                        ShareData.setShareStringData(ShareData.LAST_ACCOUNT, signup_uerName.getText().toString());
//                        EventBus.getDefault().post(new MessageEvent(Constants.GET_NEW_MSG));
                        finish();
                    }
                    break;
                case Task.SIGN_UP_CODE:
                    if (handlerRequestErr(data)) {
                        DataBean dataBean = (DataBean) data.getBody();
                        acceptCodeStr = dataBean.getData();
                        toast(data.getRspMsg());
                        obtianCodeMothed();
                    }
                    break;
                case Task.OPEN_CITY:
                    if (handlerRequestErr(data)) {
                        OpenCityBean openCityBean = (OpenCityBean) data.getBody();
                        if (openCityBean != null)
                        cityList = openCityBean.getCityList();
                        List<String> mCityNameList = new ArrayList<>();
                        String[] array = new String[cityList.size()];
                        // List转换成数组
                        for (int i = 0; i < cityList.size(); i++) {
                            mCityNameList.add(cityList.get(i).getCity());
                        }
                        initDialog(mCityNameList);

                    }
                    break;
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (buttonView.getId() == R.id.signup_showPassWord)
            if (isChecked) {
                signup_password_input.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                signup_password_input.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
    }

    /**
     * 选择城市弹出框
     */
    private void initDialog(List<String> list) {
        final Dialog selectStoreDialog = new Dialog(this, R.style.bottom_dialog);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_regist_choose, null);
        final WheelView wv = view.findViewById(R.id.wv_stores);

        ArrayWheelAdapter adapter = new ArrayWheelAdapter(this);
        adapter.setData(list);
        wv.setWheelAdapter(adapter);
        if (list.size() >= 5) {
            wv.setWheelSize(5);
            wv.setSelection(3);
        } else {
            wv.setWheelSize(3);
            wv.setSelection(2);
        }

        wv.setDrawSelectorOnTop(true);
        wv.setSkin(WheelView.Skin.Holo);
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.holoBorderColor = getResources().getColor(R.color.color_CCCCCC);
        style.textSize = 24;
        style.selectedTextColor = getResources().getColor(R.color.color_374953);
        style.textColor = getResources().getColor(R.color.color_6E757F);
        wv.setStyle(style);
        selectStoreDialog.setContentView(view);
        view.findViewById(R.id.bt_cancel).setOnClickListener(v -> selectStoreDialog.dismiss());

        view.findViewById(R.id.bt_conform).setOnClickListener(v -> {
            int position = wv.getCurrentPosition();
            signup_city_input.setText(list.get(position));
            cityCode = cityList.get(position).getCityCode();
            selectStoreDialog.dismiss();
        });
        DisplayMetrics dm = new DisplayMetrics();
        int height = dm.heightPixels;
        Window window = selectStoreDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.bottom_dialog_window_style);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.x = 0;
        lp.y = height;
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        selectStoreDialog.setCanceledOnTouchOutside(false);
        selectStoreDialog.show();
    }


    private boolean flag1 = false;
    private boolean flag2 = false;
    private boolean flag3 = false;
    private boolean flag4 = false;
    /**
     * 监听所有EditText是否输入内容  然后判断高亮button
     * */
    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            flag1 = signup_uerName.getText().toString().trim().length() > 0;
            flag2 = signup_code_input.getText().toString().trim().length() > 0;
            flag3 = signup_password_input.getText().toString().trim().length() > 0;
            flag4 = signup_inviter_input.getText().toString().trim().length() > 0;

            /**
             * 根据手机号情况设置获取验证码按钮颜色
             * */
            setObtainCodeColor();

            if (flag1 && flag2&& flag3 ) {
                signup_signupButton.setEnabled(true);
                signup_signupButton.setBackground(getResources().getDrawable(R.drawable.bg_login_button));
                ll_btn.setBackground(getResources().getDrawable(R.drawable.bg_loan_pic_shadow));
            } else {
                signup_signupButton.setBackground(getResources().getDrawable(R.drawable.bg_button_disable));
                signup_signupButton.setEnabled(false);
                ll_btn.setBackground(null);
            }

            if (flag1 && uerNameHasFocus){
                iv_del_username.setVisibility(View.VISIBLE);
            }else {
                iv_del_username.setVisibility(View.GONE);
            }
            if (flag2 && codeFocus){
                iv_del_obtainCode.setVisibility(View.VISIBLE);
            }else {
                iv_del_obtainCode.setVisibility(View.GONE);
            }
            if (flag3 && passWordHasFocus){
                iv_del_PassWord.setVisibility(View.VISIBLE);
            }else {
                iv_del_PassWord.setVisibility(View.GONE);
            }
            if (flag4 && inviterHasFocus){
                iv_del_inviter.setVisibility(View.VISIBLE);
            }else {
                iv_del_inviter.setVisibility(View.GONE);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void setObtainCodeColor(){

        int length = signup_uerName.getText().toString().replace(" ","").length();
        if (length == 11&&!isTimeing){
            signup_obtainCode.setTextColor(getResources().getColor(R.color.color_3889FF));
            signup_obtainCode.setOnClickListener(this);
        }else{
            signup_obtainCode.setTextColor(getResources().getColor(R.color.color_A2A2A2));
            signup_obtainCode.setOnClickListener(null);
        }
    }

    private boolean uerNameHasFocus = false;
    private boolean codeFocus = false;
    private boolean passWordHasFocus = false;
    private boolean inviterHasFocus = false;

    /**
     * EditText 焦点监听
     * */
    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.signup_uerName:      //手机号焦点变化
                uerNameHasFocus = hasFocus;
                if (hasFocus && flag1){
                    iv_del_username.setVisibility(View.VISIBLE);
                }else{
                    iv_del_username.setVisibility(View.GONE);
                }
                break;
            case R.id.signup_code_input:      //验证码焦点变化
                codeFocus = hasFocus;
                if (hasFocus && flag2){
                    iv_del_obtainCode.setVisibility(View.VISIBLE);
                }else{
                    iv_del_obtainCode.setVisibility(View.GONE);
                }
                break;
            case R.id.signup_password_input:      //密码焦点变化
                passWordHasFocus = hasFocus;
                if (hasFocus && flag3){
                    iv_del_PassWord.setVisibility(View.VISIBLE);
                }else{
                    iv_del_PassWord.setVisibility(View.GONE);
                }
                break;
            case R.id.signup_inviter_input:      //邀请人焦点变化
                inviterHasFocus = hasFocus;
                if (hasFocus && flag4){
                    iv_del_inviter.setVisibility(View.VISIBLE);
                }else{
                    iv_del_inviter.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }
}
