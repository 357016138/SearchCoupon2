package com.search.coupon.agent.common;

import android.content.Context;

import com.search.coupon.agent.utils.LogUtils;


/**
 * Created by song on 2018/1/17 0017.
 */

public class UncatchExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private static UncatchExceptionHandler INSTANCE = new UncatchExceptionHandler();
    private Context mContext;

    private UncatchExceptionHandler() {
    }

    public static UncatchExceptionHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            //自己处理
//            try {//延迟3秒杀进程
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//            }
            BaseApplication.getApplication().exitSystem();
            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(1);
        }
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return true;
        }
//        new Thread(() -> {
//            Looper.prepare();
//            UtilTools.toast(mContext, "程序崩溃了，请提BUG");
//            Looper.loop();
//        }).start();
        // 使用Toast来显示异常信息
        LogUtils.e("以下异常信息导致程序崩溃:\n");
        LogUtils.e(ex);
        return true;
    }
}
