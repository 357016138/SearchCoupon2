package com.search.coupon.agent.common;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.search.coupon.agent.R;
import com.search.coupon.agent.network.HandlerRequestErr;
import com.search.coupon.agent.network.JyHandler;
import com.search.coupon.agent.network.RequestParams;
import com.search.coupon.agent.network.ResponseListener;
import com.search.coupon.agent.network.ResultData;
import com.search.coupon.agent.ui.activity.CommonWebActivity;
import com.search.coupon.agent.ui.activity.LoginActivity;
import com.search.coupon.agent.utils.EasyPermissions;
import com.search.coupon.agent.utils.UMStatisticsUtils;
import com.search.coupon.agent.utils.UserUtils;
import com.search.coupon.agent.view.LoadingDialog;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

import static com.search.coupon.agent.common.Keys.KEY_IS_POST_URL;
import static com.search.coupon.agent.common.Keys.KEY_PARAM_MAP;
import static com.search.coupon.agent.common.Keys.KEY_TOPBAR_TITLE;
import static com.search.coupon.agent.common.Keys.KEY_WEB_JSON;
import static com.search.coupon.agent.common.Keys.KEY_WEB_URL;

/**
 * Created by song on 2018/1/18 0018.
 */

public abstract class BaseFragment extends Fragment implements ResponseListener, View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private BaseApplication app;
    private Toast toast;
    private LoadingDialog dialog;
    private ArrayList<Call> calls;
    protected static final int RC_PERM = 123;
    private CheckPermListener mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = BaseApplication.getApplication();

    }

    @Override
    public void onResume() {
        super.onResume();
        UMStatisticsUtils.onFragmentResume(this, this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        UMStatisticsUtils.onFragmentPause(this, this.getClass().getSimpleName());
    }

    @Override
    public void onClick(View v) {
        onClickEvent(v);
    }
    /**
     * onClick方法的封装，在此方法中处理点击
     */
    abstract public void onClickEvent(View view);

    /**
     * 显示Toast
     * @param info  显示的内容
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
                dialog = new LoadingDialog(getActivity());
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
                dialog = new LoadingDialog(getActivity());
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

    protected JyHandler startRequest(int flag, RequestParams params, Type type) {
        return startRequest(flag, params, type, true);
    }

    protected JyHandler startRequest(int flag, RequestParams params, Type type, boolean showDialog) {
        if (!(this instanceof ResponseListener)) {
            return null;
        }
        JyHandler handler = new JyHandler(getActivity(), type);
        Call call = handler.start(flag, params, this, showDialog);
        addCall(call);
        return handler;
    }

    public boolean handlerRequestErr(ResultData data) {
        return HandlerRequestErr.handlerRequestErr(getActivity(), data);
    }

    public boolean handlerRequestErr(ResultData data, boolean isTips) {
        return HandlerRequestErr.handlerRequestErr(getActivity(), data, isTips);
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

    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        cancelCall(call);
    }
    /***
     * 跳转到指定页面
     *
     * @param clas 指定页面
     */
    public void goPage(Class<? extends Activity> clas) {
        goPage(clas, null);
    }

    /***
     * 跳转到指定页面
     *
     * @param clas 指定页面
     * @param data 传入数据
     */
    protected void goPage(Class<? extends Activity> clas, Bundle data) {
        goPage(clas, data, -1);
    }

    /***
     * 跳转到指定页面
     *
     * @param clas        指定页面
     * @param data        传入数据
     * @param requestCode 请求值
     */
    protected void goPage(Class<? extends Activity> clas, Bundle data, int requestCode) {
        if (clas == null || getActivity() == null) {
            return;
        }
        Intent intent = new Intent(getActivity(), clas);
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

    /**
     * 是否登录，没有登录自动跳转到登录页面
     **/
    protected boolean isLogin() {
        boolean isLogin = UserUtils.isLogin();
        if (!isLogin) goPage(LoginActivity.class);
        return isLogin;
    }

    public interface CheckPermListener {
        //权限通过后的回调方法
        void superPermission();
    }

    public void checkPermission(CheckPermListener listener, int resString, String... mPerms) {
        mListener = listener;
        if (EasyPermissions.hasPermissions(getActivity(), mPerms)) {
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
