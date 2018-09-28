package com.search.coupon.agent.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;

import com.search.coupon.agent.R;
import com.search.coupon.agent.bean.VersionBean;
import com.search.coupon.agent.common.BaseActivity;
import com.search.coupon.agent.common.ShareData;
import com.search.coupon.agent.network.RequestParams;
import com.search.coupon.agent.network.ResultData;
import com.search.coupon.agent.network.Task;
import com.search.coupon.agent.network.UrlConfig;
import com.search.coupon.agent.service.DownloadService;
import com.search.coupon.agent.utils.DeviceUtils;
import com.search.coupon.agent.utils.DiaLogUtils;
import com.search.coupon.agent.utils.UserUtils;
import com.search.coupon.agent.view.DownloadDialog;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by song on 2018/1/17 0017.
 */
public class StartActivity extends BaseActivity {

    private static final int REQUEST_CODE_UNKNOWN_APP = 100;
    private final int KEY_GET_NEW_VERSION = 1;
    private final int KEY_TO_MAIN = 2;
    private final int KEY_UPLOAD_POSITION = 3;

    private final String KEY_NO_UPDATE = "0";
    private final String KEY_CAN_UPDATE = "1";
    private final String KEY_FORCE_UPDATE = "2";

    private WeakHandler mHandler;
    private VersionBean versionBean;

    public static class WeakHandler extends Handler {
        private WeakReference<StartActivity> refreence;

        public WeakHandler(StartActivity startActivity) {
            refreence = new WeakReference<>(startActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            refreence.get().handleResult(msg);
        }
    }

    private void handleResult(Message msg) {
        switch (msg.what) {
            case KEY_GET_NEW_VERSION:
                getNewVersion();
                break;

            case KEY_TO_MAIN:
                nextPage();
                break;

            default:
                break;
        }

    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_start, BasePageSet.NO_TOPBAR_DEFAULT_PAGE);
    }

    @Override
    public void dealLogicBeforeInitView() {
        setCheckLock(false);
        mHandler = new WeakHandler(this);
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        /**

         * 初始化PaySdk(context请传入当前Activity对象(如：MainActivity.this))

         * 第一个参数:是您在trPay后面应用的appkey（需要先提交应用资料(若应用未上线，需上传测试APK文件)，审核通过后appkey生效）

         * 第二个参数:是您的渠道，一般是baidu,360,xiaomi等

         */

//        TrPay.getInstance(StartActivity.this).initPaySdk("48b8f3bf366148a9aa0ba89e2b5d3ec1","baidu");
    }

    @Override
    public void dealLogicAfterInitView() {

        if (UserUtils.isLogin()){
            //检测token是否过期
            checkTokenValidity();
        }else{
            getNewVersion();   //查看是否有新版本
        }
    }


    @Override
    public void OnTopLeftClick() {

    }

    @Override
    public void OnTopRightClick() {

    }

    @Override
    public void onClickEvent(View view) {

    }
    /**
     * 查看是否有新版本
     * */
    private void getNewVersion() {
        RequestParams params = new RequestParams(UrlConfig.URL_GET_NEW_VERSION);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("appVersion", DeviceUtils.getCurrentAppVersionCode(this));
        params.add("operatSystem", "android");
        startRequest(Task.NEW_VERSION, params, VersionBean.class, false);
    }

    private void nextPage() {
        if (TextUtils.isEmpty(ShareData.getShareStringData(ShareData.IS_OPEN_GUIDE))) {
            goPage(GuideActivity.class);
        } else {
            goPage(MainActivity.class);
        }
        finish();
    }
    /**
     * 检查Token是否到期
     * */
    private void checkTokenValidity() {
        RequestParams params = new RequestParams(UrlConfig.URL_CHECK_TOKEN_VALIDITY);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("userId", ShareData.getShareStringData(ShareData.USER_ID));
        startRequest(Task.CHECK_TOKEN_VALIDITY, params, null, false);
    }



    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);
        switch (tag) {
            case Task.NEW_VERSION:
                if (handlerRequestErr(data)) {
                    versionBean = (VersionBean) data.getBody();
                    if (versionBean != null) {
                        setUpdateTips(versionBean);
                    }
                } else {
                    mHandler.sendEmptyMessage(KEY_TO_MAIN);
                }
                break;
            case Task.CHECK_TOKEN_VALIDITY:
                if (handlerRequestErr(data)) {
                    //Token未过期  查看是否有新版本
                    getNewVersion();
                } else {
                    //Token过期,清理本地登录状态
                    if ("0000".equals(data.getRspMsg())){
                        UserUtils.loginOut();
                    }
                    //查看是否有新版本
                    getNewVersion();
                }
                break;
            default:
                break;

        }
    }

    private void setUpdateTips(VersionBean versionBean) {
        switch (versionBean.getForceState()) {
            case KEY_NO_UPDATE:
                mHandler.sendEmptyMessage(KEY_TO_MAIN);
                break;
            case KEY_CAN_UPDATE:
                showUpdate(versionBean, false);
                break;
            case KEY_FORCE_UPDATE:
                showUpdate(versionBean, true);
                break;
        }
    }

    private void showUpdate(VersionBean versionBean, boolean isForce) {
        DownloadDialog dialog = new DownloadDialog(this);
        dialog.setContent("版本号:" + versionBean.getNewAppVersion() + "\n更新内容:\n" + versionBean.getVersionContent());
        dialog.setCancelText(isForce ? "退出" : "取消");
        dialog.setOnDownLoadClickListener(new DownloadDialog.OnDownLoadClickListener() {
            @Override
            public void onLeftClick() {
                if (isForce) {
                    dialog.dismiss();
                    app.exitSystem();
                } else {
                    dialog.dismiss();
                    mHandler.sendEmptyMessage(KEY_TO_MAIN);
                }
            }

            @Override
            public void onRightClick() {
                //请求权限
                checkPermission(new CheckPermListener(){
                    @Override
                    public void superPermission() {
                        dialog.dismiss();
                        downLoad(versionBean);
                        toast( "后台下载中");
                    }
                }, R.string.ask_again, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        });
        dialog.show();
    }



    private void downLoad(VersionBean versionBean) {

            // Android 8.0 以上版本先获取是否有安装未知来源应用的权限
            boolean b = true;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                b = getPackageManager().canRequestPackageInstalls();
            }
            if (b) {
                Intent intent = new Intent(this, DownloadService.class);
                intent.putExtra("key_version", versionBean);
                startService(intent);
                if (!"2".equals(versionBean.getForceState())){
                    nextPage();
                }

            } else {
                DiaLogUtils diaLogUtils = DiaLogUtils.creatDiaLog(this);
                diaLogUtils.setContent("安装应用需要打开未知来源权限，请去设置中开启权限");
                diaLogUtils.setSureButton("确认", v -> {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:com.jieyue.wechat.search"));
                    startActivityForResult(intent, REQUEST_CODE_UNKNOWN_APP);
                    diaLogUtils.destroyDialog();
                });
                diaLogUtils.setCancelButton("取消", v -> {
                    diaLogUtils.destroyDialog();
                });
                diaLogUtils.showDialog();
            }
    }

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_UNKNOWN_APP) {
            downLoad(versionBean);
        }
    }

}
