package com.search.coupon.agent.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.search.coupon.agent.R;
import com.search.coupon.agent.common.BaseActivity;
import com.search.coupon.agent.common.ShareData;
import com.search.coupon.agent.utils.DateUtils;
import com.search.coupon.agent.utils.UserUtils;
import com.search.coupon.agent.utils.UtilTools;
import com.search.coupon.agent.view.LockPatternView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.search.coupon.agent.common.Keys.KEY_GESTURE_TYPE;
import static com.search.coupon.agent.common.Keys.KEY_LOGIN_CAN_BACK;
import static com.search.coupon.agent.common.ShareData.GESTURE_PATTERN;

/**
 * 解锁界面
 * */
public class GestureUnlockActivity extends BaseActivity implements LockPatternView.OnPatternListener{
    private final int MAX_ERR_NUM = 6;
    public static final String TYPE_VERIFY = "0";
    public static final String TYPE_CLOSE = "1";
    public static final String TYPE_RESET = "2";

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.gesture_lpv)
    LockPatternView gestureLpv;
    @BindView(R.id.tv_pwd_login)
    TextView tvPwdLogin;
    @BindView(R.id.tv_forget_set)
    TextView tvForgetSet;
    @BindView(R.id.ll_bottom)
    LinearLayout ll_bottom;

    //操作类型
    private String type;
    //错误动画
    private TranslateAnimation translateAnim;
    //手势密码
    private List<LockPatternView.Cell> setPattern;
    //错误次数
    private int errorNum;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_gesture_unlock);
    }

    @Override
    public void dealLogicBeforeInitView() {
        type = getBundleStr(KEY_GESTURE_TYPE);
        setPattern = LockPatternView.stringToPattern(ShareData.getShareStringData(GESTURE_PATTERN));
        errorNum = ShareData.getShareIntData(ShareData.GESTURE_ERROR_NUM);
        setCheckLock(false);
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        gestureLpv.setOnPatternListener(this);
        setTopbar();
        tvName.setText(DateUtils.getCurrentMMdd());
    }

    @Override
    public void dealLogicAfterInitView() {
        switch (type) {
            case TYPE_VERIFY:
                tvTip.setVisibility(View.VISIBLE);
                ll_bottom.setVisibility(View.VISIBLE);
                break;
            case TYPE_CLOSE:
            case TYPE_RESET:
                tvTip.setVisibility(View.VISIBLE);
                tvTip.setTextColor(getResources().getColor(R.color.color_3889FF));
                tvTip.setText("请绘制原手势密码");
                break;
        }
    }

    private void setTopbar() {
        switch (type) {
            case TYPE_VERIFY:
                topBar.setVisibility(View.GONE);
                break;
            case TYPE_CLOSE:
            case TYPE_RESET:
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
    @OnClick({R.id.tv_pwd_login, R.id.tv_forget_set})
    @Override
    public void onClickEvent(View view) {

            switch (view.getId()) {
                case R.id.tv_pwd_login:
                case R.id.tv_forget_set:
                    goPwdLogin();
                    break;
            }
    }

    private void goPwdLogin() {
        UserUtils.loginOut();
        UserUtils.clearGesture();
        app.removeAllActivity();
        Bundle bd = new Bundle();
        bd.putBoolean(KEY_LOGIN_CAN_BACK, false);
        goPage(LoginActivity.class, bd);
        this.finish();
    }

    /**
     * 绘制手势密码的回调方法
     * */
    @Override
    public void onPatternStart() {

    }
    /**
     * 绘制手势密码的回调方法
     * */
    @Override
    public void onPatternCleared() {

    }
    /**
     * 绘制手势密码的回调方法
     * */
    @Override
    public void onPatternCellAdded(List<LockPatternView.Cell> pattern) {

    }

    /**
     * 绘制手势密码的回调方法，应该是绘制完的
     * */
    @Override
    public void onPatternDetected(List<LockPatternView.Cell> pattern) {

        if (!pattern.equals(setPattern)) {
            //手势错误，设置错误类型，增加错误次数
            gestureLpv.setDisplayMode(LockPatternView.DisplayMode.Wrong);
            gestureLpv.clearPatternDelay();
            errorNum--;
            ShareData.setShareIntData(ShareData.GESTURE_ERROR_NUM, errorNum);
            if (errorNum >= 1) {
                setTipsErr("手势绘制错误" + (MAX_ERR_NUM - errorNum) + "次,您还有" + errorNum + "次机会", true);
            } else {
                setTipsErr("即将转为密码登录", true);
                goPwdLogin();
            }
            return;
        } else {
            unLockSuccess();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //如果是解锁模式，拦截返回事件，进入桌面
        boolean isVerify = TYPE_VERIFY.equals(type);
        if (isVerify && keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setTipsErr(String str, boolean isSahke) {
        tvTip.setTextColor(getResources().getColor(R.color.gesture_tip_err));
        tvTip.setText(str);
        if (isSahke) {
            startShakeAnim(tvTip);
        }
    }

    private void startShakeAnim(TextView textView) {
        if (translateAnim == null) {
            translateAnim = new TranslateAnimation(0, UtilTools.dp2px(this, 5f), 0, 0);
            translateAnim.setDuration(400);
            translateAnim.setInterpolator(this, R.anim.shake_interpolator);
        }
        if (!translateAnim.hasEnded()) {
            translateAnim.cancel();
        }
        textView.startAnimation(translateAnim);
    }

    private void unLockSuccess() {
        ShareData.setShareIntData(ShareData.GESTURE_ERROR_NUM, 5);
        ShareData.setShareLongData(ShareData.GESTURE_TIME, 0L);
        switch (type) {
            case TYPE_CLOSE:
                UserUtils.clearGesture();
                break;
            case TYPE_RESET:
                Bundle bd = new Bundle();
                bd.putString(KEY_GESTURE_TYPE, GestureSetActivity.TYPE_RESET);
                goPage(GestureSetActivity.class, bd);
                break;
        }
        this.finish();
    }
}
