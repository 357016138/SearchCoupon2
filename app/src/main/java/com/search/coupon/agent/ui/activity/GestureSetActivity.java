package com.search.coupon.agent.ui.activity;

import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.search.coupon.agent.R;
import com.search.coupon.agent.common.BaseActivity;
import com.search.coupon.agent.common.ShareData;
import com.search.coupon.agent.utils.UtilTools;
import com.search.coupon.agent.view.LockIndicator;
import com.search.coupon.agent.view.LockPatternView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.search.coupon.agent.common.Keys.KEY_GESTURE_TYPE;

/**
 * Created by song on 2018/1/31 0017.
 * 手势设置的界面
 * */
public class GestureSetActivity extends BaseActivity implements LockPatternView.OnPatternListener{
    private final int SEP_1 = 1;//第一次设置手势密码
    private final int SEP_2 = 2;//第二次设置

    public static final String TYPE_LOGIN = "0";
    public static final String TYPE_OPEN = "1";
    public static final String TYPE_RESET = "2";

    @BindView(R.id.gesture_li)
    LockIndicator gestureLi;
    @BindView(R.id.gesture_tv)
    TextView gestureTv;
    @BindView(R.id.gesture_lpv)
    LockPatternView gestureLpv;
    @BindView(R.id.tv_reset_gesture)
    TextView tvResetGesture;
    @BindView(R.id.tv_gesture_tip)
    TextView tvGestureTip;

    private int currentSep = SEP_1;
    private TranslateAnimation translateAnim;
    private List<LockPatternView.Cell> setPattern;
    //操作类型
    private String type;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_gesture_set);
    }

    @Override
    public void dealLogicBeforeInitView() {
        type = getBundleStr(KEY_GESTURE_TYPE);
        setCheckLock(false);

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        gestureLpv.setSettingModel(LockPatternView.SettingModel.Setting);
        gestureLpv.setOnPatternListener(this);
        initTipState();
    }

    @Override
    public void dealLogicAfterInitView() {
        setTopbar();
    }

    @OnClick({R.id.tv_reset_gesture})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.tv_reset_gesture:
                resetGesture();
                break;
        }
    }

    @Override
    public void OnTopLeftClick() {
        finish();
    }

    @Override
    public void OnTopRightClick() {
        switch (type) {
            case TYPE_LOGIN:
                goPage(MainActivity.class);
                break;
        }
    }

    private void setTopbar() {
        topBar.setTitle("");
        switch (type) {
            case TYPE_LOGIN:
                topBar.setLeftVisible(false);
                topBar.setRightText("跳过");
                topBar.setRightVisible(true);
                break;
            case TYPE_RESET:
            case TYPE_OPEN:
                topBar.setLeftVisible(true);
                break;
        }
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
        if (currentSep == SEP_1) {
            //第一次设置，判断手势数量是否少于4个
            tvGestureTip.setVisibility(View.INVISIBLE);
            if (pattern.size() < LockPatternView.MIN_LOCK_PATTERN_SIZE) {
                gestureLpv.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                gestureLpv.clearPatternDelay();
                setTipsErr("为了您的帐户安全，请至少绘制4个圆点", true);
                return;
            }
            if (setPattern == null) {
                setPattern = new ArrayList<>();
            }
            setPattern.clear();
            setPattern.addAll(pattern);
            updateView();
            currentSep = SEP_2;
            tvResetGesture.setVisibility(View.VISIBLE);
        } else {
            if (!pattern.equals(setPattern)) {
                gestureLpv.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                gestureLpv.clearPatternDelay();
                setTipsErr("与上次绘制不一致，请重新绘制", true);
                return;
            }
            setSuccess(pattern);
        }
    }

    private void setTipsErr(String str, boolean isSahke) {
        gestureTv.setVisibility(View.VISIBLE);
        gestureTv.setTextColor(getResources().getColor(R.color.gesture_tip_err));
        gestureTv.setText(str);
        if (isSahke) {
            startShakeAnim(gestureTv);
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

    private void updateView() {
        gestureLi.setPath(setPattern);
        gestureTv.setVisibility(View.VISIBLE);
        gestureTv.setTextColor(getResources().getColor(R.color.color_3889FF));
        gestureTv.setText("请再次绘制手势密码");
        gestureLpv.clearPattern();
    }

    private void setSuccess(List<LockPatternView.Cell> pattern) {
        gestureTv.setVisibility(View.VISIBLE);
        gestureTv.setTextColor(getResources().getColor(R.color.color_3889FF));
        gestureTv.setText("手势密码绘制成功");
        ShareData.setShareBooleanData(ShareData.GESTURE_IS_OPEN, true);
        ShareData.setShareIntData(ShareData.GESTURE_ERROR_NUM, 6);
        ShareData.setShareStringData(ShareData.GESTURE_PATTERN, LockPatternView.patternToString(pattern));
        ShareData.setShareLongData(ShareData.GESTURE_TIME, 0L);
        toast("手势密码绘制成功");
        if (type.equals(TYPE_LOGIN)) {
            goPage(MainActivity.class);
        }
        this.finish();
    }

    private void resetGesture() {
        setPattern.clear();
        initTipState();
        tvGestureTip.setVisibility(View.VISIBLE);
        tvResetGesture.setVisibility(View.INVISIBLE);
        currentSep = SEP_1;
        gestureLi.setPath(null);
    }

    private void initTipState() {
        switch (type) {
            case TYPE_OPEN:
            case TYPE_LOGIN:
                gestureTv.setVisibility(View.INVISIBLE);
                break;
            case TYPE_RESET:
                gestureTv.setVisibility(View.VISIBLE);
                gestureTv.setTextColor(getResources().getColor(R.color.color_3889FF));
                gestureTv.setText("请绘制新手势密码");
                break;
        }
    }
}
