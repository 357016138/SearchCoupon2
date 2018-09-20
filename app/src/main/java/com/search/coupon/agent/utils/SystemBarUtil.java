package com.search.coupon.agent.utils;

import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

/**
 * 作者：sql on 2015/12/8 11:35
 * 邮箱：357016138@qq.com
 * 说明：状态栏与标题栏颜色一体化的工具类
 */
public class SystemBarUtil {

    /**
     * 设置状态栏背景状态
     */
    public static void setTranslucentStatus(Activity activity,int color)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            Window win = activity.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(color);//状态栏无背景
    }
}
