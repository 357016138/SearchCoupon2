package com.search.coupon.agent.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.search.coupon.agent.R;
import com.search.coupon.agent.network.HandlerRequestErr;
import com.search.coupon.agent.network.JyHandler;
import com.search.coupon.agent.network.RequestParams;
import com.search.coupon.agent.network.ResponseListener;
import com.search.coupon.agent.network.ResultData;
import com.search.coupon.agent.ui.activity.CommonWebActivity;
import com.search.coupon.agent.ui.activity.GestureUnlockActivity;
import com.search.coupon.agent.ui.activity.LoginActivity;
import com.search.coupon.agent.utils.EasyPermissions;
import com.search.coupon.agent.utils.NetUtils;
import com.search.coupon.agent.utils.StatusBarUtil;
import com.search.coupon.agent.utils.UMStatisticsUtils;
import com.search.coupon.agent.utils.UserUtils;
import com.search.coupon.agent.view.LoadingDialog;
import com.search.coupon.agent.view.TopBar;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;

import static com.search.coupon.agent.common.Keys.KEY_GESTURE_TYPE;
import static com.search.coupon.agent.common.Keys.KEY_IS_POST_URL;
import static com.search.coupon.agent.common.Keys.KEY_PARAM_MAP;
import static com.search.coupon.agent.common.Keys.KEY_TOPBAR_TITLE;
import static com.search.coupon.agent.common.Keys.KEY_WEB_JSON;
import static com.search.coupon.agent.common.Keys.KEY_WEB_URL;

/**
 * Created by song on 2018/1/17 0017.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener, ResponseListener, TopBar.OnTopbarClickListener, EasyPermissions.PermissionCallbacks {

    public BaseApplication app;
    private Toast toast;
    private LoadingDialog dialog;
    private ArrayList<Call> calls;
    private BasePageSet basePageSet;
    protected TopBar topBar;
    private LinearLayout ltBaseMain, ltBaseDefaultPage;
    private View noNetWorkView, noDataView;
    private Button btnBaseSetNetWork;
    //程序离开后进入手势密码的时间5min，可以根据需求调节
    private final long OUT_TIME = 300000;
    //是否触发解锁页面
    private boolean isCheckLock = true;
    protected static final int RC_PERM = 123;
    private CheckPermListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = BaseApplication.getApplication();
        //去掉状态栏空间并 修改状态栏颜色为透明  ，内部有方法可以更改状态栏字体颜色(在最下面)
        StatusBarUtil.setTranslucentForImageView(this, 0);
        StatusBarUtil.setStatusBarFontIconDark(this,true);

        setContentLayout();
        dealLogicBeforeInitView();
        initView();
        dealLogicAfterInitView();
    }

    @Override
    public void onClick(View v) {
        onClickEvent(v);
    }

    /**
     * 设置布局文件  setContentView(R.layout.activity_main);
     */
    public abstract void setContentLayout();

    /**
     * 初始化控件之前需要处理的逻辑，例如接收传过来的数据
     */
    public abstract void dealLogicBeforeInitView();

    /**
     * 初始化控件 可以用注解的方式
     */
    public abstract void initView();

    /**
     * 初始化控件之后需要处理的业务，例如从网络或缓存加载数据
     */
    public abstract void dealLogicAfterInitView();

    /**
     * onClick方法的封装，在此方法中处理点击
     */
    abstract public void onClickEvent(View view);


    /**
     * 设置TopBar 即标题栏  默认是有标题栏的  不用标题栏可以传这个参数： NO_TOPBAR_DEFAULT_PAGE
     */
    public void setContentView(int layoutResID) {
        setContentView(layoutResID, BasePageSet.CONTAIN_TOPBAR_DEFAULT_PAGE);
    }

    /**
     * 上面方法的重载
     */
    public void setContentView(int layoutResID, BasePageSet basePageSet) {
        this.basePageSet = basePageSet;
        switch (basePageSet) {
            case NO_TOPBAR_DEFAULT_PAGE:        //传这个值为没有标题栏的情况
                super.setContentView(layoutResID);
                break;
            case CONTAIN_TOPBAR:
                setContainTopBar(layoutResID);
                break;
            case CONTAIN_TOPBAR_DEFAULT_PAGE:    //默认的情况
                setContainTopBarDefaultPage(layoutResID);
                break;

        }
    }

    /**
     * 初始化topBar一些参数和样式 默认返回键是可见的 如有需要可以设置隐藏 topBar.setLeftVisible(false);
     */
    private void setContainTopBar(int layoutResID) {
        super.setContentView(R.layout.activity_base);
        topBar = findViewById(R.id.topbar_base);
        topBar.setLeftVisible(true);
        topBar.setOnTopbarClickListener(this);
        ltBaseMain = findViewById(R.id.lt_base_main);
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        ltBaseMain.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * 初始化一些特殊情况的布局（无网络时）
     */
    private void setContainTopBarDefaultPage(int layoutResID) {
        setContainTopBar(layoutResID);
        ltBaseDefaultPage = findViewById(R.id.lt_base_default_page);
        noNetWorkView = LayoutInflater.from(this).inflate(R.layout.layout_no_network, null);
        btnBaseSetNetWork = noNetWorkView.findViewById(R.id.btn_base_set_network);
        btnBaseSetNetWork.setOnClickListener(v -> {
            startActivity(new Intent(Settings.ACTION_SETTINGS));
        });
        noDataView = LayoutInflater.from(this).inflate(R.layout.layout_no_data, null);
        noDataView.setOnClickListener(v -> {
//            onNoDataClick();
        });
        setUiNoNetWork();
    }

    /**
     * 设置无网络页面
     */
    private void setUiNoNetWork() {
        if (NetUtils.isOpenNetwork(this)) return;
        if (ltBaseDefaultPage.getChildCount() > 0) {
            ltBaseDefaultPage.removeAllViews();
        }
        ltBaseDefaultPage.addView(noNetWorkView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        showDefaultLayout();
    }

    /**
     * 设置白色标题栏
     */
    protected void setTopBarWhite() {
        topBar.setBackground(R.color.white);
        topBar.setTitleColor(ContextCompat.getColor(this, R.color.color_3F3F3F));
        topBar.setLeftTextColor(ContextCompat.getColor(this, R.color.color_3F3F3F));
        topBar.setRightTextColor(ContextCompat.getColor(this, R.color.color_3F3F3F));
        topBar.setLeftImage(R.drawable.ic_back_black);
        topBar.setLineVisible(true);
    }

    /**
     * 展示缺省页面
     */
    protected void showDefaultLayout() {
        ltBaseMain.setVisibility(View.GONE);
        ltBaseDefaultPage.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
        UMStatisticsUtils.onActivityResume(this, this.getClass().getSimpleName());
        /**
         * 判断是否触发手势密码
         */
        setLockShow();
        if(isCheckLock && !BaseApplication.isRun)
            BaseApplication.isRun = true;
        long gestureTime = ShareData.getShareLongData(ShareData.GESTURE_TIME);
        if(gestureTime != 0)
            ShareData.setShareLongData(ShareData.GESTURE_TIME, 0L);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
        UMStatisticsUtils.onActivityPause(this, this.getClass().getSimpleName());
    }

    /**
     * 显示Toast
     *
     * @param info 显示的内容
     */
    public void toast(String info) {
        try {
            if (toast != null) {
                toast.cancel();
                toast = null;
            }
            toast = Toast.makeText(app, info, Toast.LENGTH_SHORT);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示加载dialog
     */
    protected void showDialog() {
        try {
            if (dialog == null) {
                dialog = new LoadingDialog(this);
                dialog.setCanceledOnTouchOutside(false);
            }
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void showDialog(String title) {
        try {
            if (dialog == null) {
                dialog = new LoadingDialog(this);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setLoadingTitle(title);
            }
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭加载dialog
     */
    public void dissDialog() {
        if (dialog != null) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 》》》》》》》》》》》》》》》》网络请求封装区域》》》》》》》》》》》》》》》》》》》》》》
     */

    protected JyHandler startRequest(int flag, RequestParams params, Type type) {
        return startRequest(flag, params, type, true);
    }

    protected JyHandler startRequest(int flag, RequestParams params, Type type, boolean showDialog) {
        if (!(this instanceof ResponseListener)) {
            return null;
        }
        JyHandler handler = new JyHandler(this, type);
        Call call = handler.start(flag, params, this, showDialog);
        addCall(call);
        return handler;
    }

    public boolean handlerRequestErr(ResultData data) {
        return HandlerRequestErr.handlerRequestErr(this, data);
    }

    public boolean handlerRequestErr(ResultData data, boolean isTips) {
        return HandlerRequestErr.handlerRequestErr(this, data, isTips);
    }

    private void addCall(Call call) {
        if (call == null) {
            return;
        }
        if (calls == null) {
            calls = new ArrayList<>();
        }
        calls.add(call);
    }

    private void cancelCall(Call call) {
        if (calls != null && calls.size() != 0) {
            if (call != null) {
                if (!call.isCanceled()) {
                    call.cancel();
                }
                calls.remove(call);
            } else {
                for (Call cal : calls) {
                    if (cal != null) {
                        cal.cancel();
                    }
                }
            }
            calls.clear();
        }
    }

    /**
     * @Description: 网络请求返回值的回调方法
     * @params call
     * @params tag
     * @params data
     */
    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        cancelCall(call);
    }

    @Override
    protected void onDestroy() {
        cancelCall(null);
        super.onDestroy();
    }
    /**
     * 》》》》》》》》》》》》》》》》网络请求封装区域结束》》》》》》》》》》》》》》》》》》》
     * */


    /***
     * 跳转到指定Activity页面
     *
     * @param clas 指定页面
     */
    public void goPage(Class<? extends Activity> clas) {
        goPage(clas, null);
    }

    /***
     * 跳转到指定Activity页面
     *
     * @param clas 指定页面
     * @param data 传入数据
     */
    protected void goPage(Class<? extends Activity> clas, Bundle data) {
        goPage(clas, data, -1);
    }

    /***
     * 跳转到指定Activity页面
     *
     * @param clas        指定页面
     * @param data        传入数据
     * @param requestCode 请求值
     */
    protected void goPage(Class<? extends Activity> clas, Bundle data, int requestCode) {
        if (clas == null) {
            return;
        }
        Intent intent = new Intent(this, clas);
        if (data != null) {
            intent.putExtra(Keys.KEY_DATA, data);
        }
        startActivityForResult(intent, requestCode);
    }

    /***
     * 跳转到Webview页面
     *
     * @param title 标题名称
     * @param url 网址
     */
    protected void goWebPage(String title, String url) {
        goWebPage(title, url, null);
    }

    /***
     * 跳转到Webview页面
     *
     * @param title 标题名称
     * @param url 网址
     * @param jsonObj 部分json参数
     */
    protected void goWebPage(String title, String url, JSONObject jsonObj) {
        Bundle bd = new Bundle();
        bd.putString(KEY_TOPBAR_TITLE, title);
        bd.putString(KEY_WEB_URL, url);
        if (jsonObj != null) bd.putString(KEY_WEB_JSON, jsonObj.toString());
        goPage(CommonWebActivity.class, bd);
    }

    /***
     * 跳转到Webview页面
     *
     * @param title 标题名称
     * @param url 网址
     * @param paramMap post需要的参数
     */
    protected void goWebPageForPost(int requestCode, String title, String url, HashMap<String, String> paramMap) {
        Bundle bd = new Bundle();
        bd.putString(KEY_TOPBAR_TITLE, title);
        bd.putString(KEY_WEB_URL, url);
        bd.putBoolean(KEY_IS_POST_URL, true);
        bd.putSerializable(KEY_PARAM_MAP, paramMap);
        goPage(CommonWebActivity.class, bd, requestCode);
    }

    /***
     * Activity跳转后 获取String传入值，如果为空返回空字符串
     *
     * @param key
     * @return
     */
    protected String getBundleStr(String key) {
        Bundle bundle = getIntent().getBundleExtra(Keys.KEY_DATA);
        if (bundle == null) {
            return "";
        }
        return bundle.getString(key, "");
    }

    /***
     * 获取intent传入值
     *
     * @return bundle
     */
    protected Bundle getIntentData() {
        Bundle bundle = getIntent().getBundleExtra(Keys.KEY_DATA);
        if (bundle == null) {
            bundle = new Bundle();
        }
        return bundle;
    }

    /**
     * 判断是否登录，没有登录自动跳转到登录页面
     **/
    protected boolean isLogin() {
        boolean isLogin = UserUtils.isLogin();
        if (!isLogin) goPage(LoginActivity.class);
        return isLogin;
    }

    //设置当前页面是否触发手势密码
    public void setCheckLock(boolean checkLock) {
        isCheckLock = checkLock;
    }

    private void setLockShow() {
        if (!UserUtils.isLogin()) return;
        if (!isCheckLock) return;
        boolean isOpenLock = ShareData.getShareBooleanData(ShareData.GESTURE_IS_OPEN);
        if (!isOpenLock) return;
        if (!BaseApplication.isRun) {
            Bundle bd = new Bundle();
            bd.putString(KEY_GESTURE_TYPE, GestureUnlockActivity.TYPE_VERIFY);
            goPage(GestureUnlockActivity.class, bd);
            return;
        }
        long currentTime = System.currentTimeMillis();
        long lastTime = ShareData.getShareLongData(ShareData.GESTURE_TIME);
        if (lastTime == 0L) return;
        if ((currentTime - lastTime) >= OUT_TIME) {
            Bundle bd = new Bundle();
            bd.putString(KEY_GESTURE_TYPE, GestureUnlockActivity.TYPE_VERIFY);
            goPage(GestureUnlockActivity.class, bd);
        }
    }


    public enum BasePageSet {
        /**
         * 无Topbar和缺省页面配置，将会调用系统的setContentView方法
         */
        NO_TOPBAR_DEFAULT_PAGE,
        /**
         * 仅包含头部TopBar
         */
        CONTAIN_TOPBAR,
        /**
         * 包含TopBar和默认缺省页面
         */
        CONTAIN_TOPBAR_DEFAULT_PAGE
    }

    /**
     * 点击EditText外的空白处隐藏软键盘
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    public interface CheckPermListener {
        //权限通过后的回调方法
        void superPermission();
    }

    public void checkPermission(CheckPermListener listener, int resString, String... mPerms) {
        mListener = listener;
        if (EasyPermissions.hasPermissions(this, mPerms)) {
            if (mListener != null)
                mListener.superPermission();
        } else {
            EasyPermissions.requestPermissions(this, getString(resString),
                    RC_PERM, mPerms);
        }
    }

    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //同意了某些权限可能不是全部
    }

    @Override
    public void onPermissionsAllGranted() {
        if (mListener != null)
            mListener.superPermission();//同意了全部权限的回调
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

        EasyPermissions.checkDeniedPermissionsNeverAskAgain(this,
                getString(R.string.perm_tip), R.string.setting, R.string.cancel, null, perms);
    }

}
