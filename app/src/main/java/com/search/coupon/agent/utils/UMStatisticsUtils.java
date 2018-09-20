package com.search.coupon.agent.utils;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.search.coupon.agent.ui.activity.CommonWebActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 友盟统计工具类
 * Created by fan on 2018/1/12.
 */
public class UMStatisticsUtils {
    /**
     * Activity中的onResume()方法调用
     * @param context 上下文
     * @param className 记录页面路径的唯一值,可传递调用此方法的Activity的类名,根据各个项目不同传递规则传递
     */
    public static void onActivityResume(Context context, String className) {
        MobclickAgent.onResume(context);
        if (context != null && !context.getClass().getName().equals(CommonWebActivity.class.getName())) {
            MobclickAgent.onPageStart(className);
        }
    }

    /**
     * Activity中的onPause()方法调用
     * @param context 上下文
     * @param className 记录页面路径的唯一值,需要和onActivityResume()方法的值一一对应,
     *                  可传递调用此方法的Activity的类名,根据各个项目不同传递规则传递
     */
    public static void onActivityPause(Context context, String className) {
        MobclickAgent.onPause(context);
        if (context != null && !context.getClass().getName().equals(CommonWebActivity.class.getName())) {
            MobclickAgent.onPageEnd(className);
        }
    }

    /**
     * Fragment中的onResume()方法调用
     * @param fragment 调用此方法的Fragment的实例
     * @param className 记录页面路径的唯一值,可传递调用此方法的Fragment的类名,根据各个项目不同传递规则传递
     */
    public static void onFragmentResume(Fragment fragment, String className) {
        if (fragment != null) {
            MobclickAgent.onPageStart(className);
        }
    }

    /**
     * Fragment中的onPause()方法调用
     * @param fragment 调用此方法的Fragment的实例
     * @param className 记录页面路径的唯一值,onFragmentResume()方法的值一一对应,
     *                  可传递调用此方法的Fragment的类名,根据各个项目不同传递规则传递
     */
    public static void onFragmentPause(Fragment fragment, String className) {
        if (fragment != null) {
            MobclickAgent.onPageEnd(className);
        }
    }


}
