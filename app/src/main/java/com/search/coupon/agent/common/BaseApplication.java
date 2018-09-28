package com.search.coupon.agent.common;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import java.util.LinkedList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by song on 2018/1/17 0017.
 * 自定义Application
 */

public class BaseApplication extends Application {
    public static BaseApplication baseApplication;
    //LinkedList 装Activity
    private List<Activity> activityList = new LinkedList<Activity>();
    //记录activity状态
    public int activityCount = 0;
    public static boolean isRun = false;

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;

        ShareData.init(this);          //SharedPreferences 初始化

        JPushInterface.setDebugMode(true);      //极光推送debug模式开关
        JPushInterface.init(this);    //极光推送初始化

        UMConfigure.init(this,"5aa62bd98f4a9d5862000174","umeng",UMConfigure.DEVICE_TYPE_PHONE,"");//友盟分享初始化
        PlatformConfig.setWeixin("wxeee314e33dd5d218", "8fc040a682c9f752cca36b320b01766b");     //微信分享設置
        PlatformConfig.setQQZone("1106786821", "iBv2TRuBuTTjHhlY");                             //QQ分享設置

        registerActivityLifecycleCallbacks(new MyActivityLifecycleCallbacks());
        UncatchExceptionHandler.getInstance().init(this);


    }

    public static BaseApplication getApplication() {
        return baseApplication;
    }

    /**
     * 遍历所有Activity并finish
     *
     * @since 2013-7-24 gaobingbing
     */
    public void exitSystem() {
        //程序切换到后台,开始记录离开程序的时间,然后再打开判断是否需要指纹解锁
        recordGestureTime();
        for (Activity activity : activityList) {
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }
        System.exit(0);
    }

    private void recordGestureTime() {
        boolean isOpen = ShareData.getShareBooleanData(ShareData.GESTURE_IS_OPEN);
        if (!isOpen) return;
        ShareData.setShareLongData(ShareData.GESTURE_TIME, System.currentTimeMillis());
    }


    private class MyActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            if (!activityList.contains(activity)) {
                activityList.add(activity);
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {
            if (activityCount == 0) {
                //程序切换到前台
            }
            activityCount++;
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            activityCount--;
            if (activityCount == 0) {
                //程序切换到后台,开始记录离开程序的时间
                recordGestureTime();
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            if (activityList.contains(activity)) {
                activityList.remove(activity);
            }
        }
    }

    //清除栈内所有activity
    public void removeAllActivity() {
        for (Activity activity : activityList) {
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }
    }


}
