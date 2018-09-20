package com.search.coupon.agent.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.webkit.MimeTypeMap;


import com.search.coupon.agent.R;
import com.search.coupon.agent.bean.VersionBean;
import com.search.coupon.agent.network.download.OkHttpDownListener;
import com.search.coupon.agent.network.download.OkHttpDownLoad;
import com.search.coupon.agent.utils.FileUtils;
import com.search.coupon.agent.utils.LogUtils;
import com.search.coupon.agent.utils.UtilTools;

import java.io.File;

/**
 * Created by yangwei on 2017/12/1.
 */
public class DownloadService extends IntentService {
    //定义notification ID
    private static final int NO_3 = 0x3;
    private String apkPath;
    private VersionBean versionBean;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public DownloadService() {
        super("com.jieyue.wechat.search.service.DownloadService");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        if (intent != null && intent.getExtras() != null) {
            versionBean = (VersionBean) intent.getExtras().getSerializable("key_version");
        }
        if (versionBean == null) {
            versionBean = new VersionBean();
        }
        startDownLoad();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    private void startDownLoad() {
        apkPath = FileUtils.APK_PATH + versionBean.getNewAppVersion() + ".apk";
        File file = new File(apkPath);
        if (file.exists()) {
            openFile(file);
            return;
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "");
        builder.setTicker("微信搜索正在下载");
        builder.setContentTitle("下载");
        builder.setContentText("正在下载");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setProgress(100, 0, false);
        notificationManager.notify(NO_3, builder.build());


        OkHttpDownLoad.getInstance().startDownload(versionBean.getAppURL(), FileUtils.APK_PATH, versionBean.getNewAppVersion() + ".apk", new OkHttpDownListener() {
            @Override
            public void onFailure() {
                builder.setContentText("下载失败");
                notificationManager.notify(NO_3, builder.build());
            }

            @Override
            public void onProcess(int progress) {
                if (progress == 100) {
                    builder.setContentTitle("下载完成");
                    builder.setContentText("正在安装");
                } else {
                    builder.setContentText("正在下载" + progress + "%");
                    builder.setProgress(100, progress, false);
                    LogUtils.e("Test", "---------" + progress);
                }
                notificationManager.notify(NO_3, builder.build());
            }

            @Override
            public void onSuccess() {
                notificationManager.cancel(NO_3);
                openFile(new File(apkPath));
            }
        });
    }

    public void openFile(File var0) {
        Intent var2 = new Intent();
        var2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        var2.setAction("android.intent.action.VIEW");
        String var3 = getMIMEType(var0);
        //判断版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            var2.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri apkUri = FileProvider.getUriForFile(this, "com.jieyue.wechat.search.fileprovider", var0);
            var2.setDataAndType(apkUri, var3);
        } else {
            var2.setDataAndType(Uri.fromFile(var0), var3);
        }
        try {
            this.startActivity(var2);
        } catch (Exception var5) {
            var5.printStackTrace();
            UtilTools.toast(this, "没有找到打开此类文件的程序");
        }
    }

    public String getMIMEType(File var0) {
        String var1 = "";
        String var2 = var0.getName();
        String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
        var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        return var1;
    }

}
