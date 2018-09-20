package com.search.coupon.agent.log;

import android.util.Log;

import com.search.coupon.agent.BuildConfig;

/**
 * @author baipeng
 * @Title JYLogUtils
 * @Date 2017/9/8 14:09
 * @Description 通用JYLogUtils.
 */
public class JYLogUtils {

	private static boolean logOpen = BuildConfig.DEBUG;

	public static void v(String TAG, String msg) {
		if (logOpen) {
			Log.v(String.valueOf(TAG), String.valueOf(msg));
		}
	}
	
	public static boolean loggerIsOpen(){
		return logOpen;
	}

	public static void v(String TAG, String msg, Throwable t) {
		if (logOpen) {
			Log.v(String.valueOf(TAG), String.valueOf(msg), t);
		}
	}

	public static void d(String TAG, String msg) {
		if (logOpen) {
			Log.d(String.valueOf(TAG), String.valueOf(msg));
		}
	}

	public static void d(String TAG, String msg, Throwable t) {
		if (logOpen) {
			Log.d(String.valueOf(TAG), String.valueOf(msg), t);
		}
	}
	
	public static void i(String TAG, String msg) {
		if (logOpen) {
			Log.i(String.valueOf(TAG), String.valueOf(msg));
		}
	}

	public static void i(String TAG, String msg, Throwable t) {
		if (logOpen) {
			Log.i(String.valueOf(TAG), String.valueOf(msg), t);
		}
	}
	
	public static void w(String TAG, String msg) {
		if (logOpen) {
			Log.w(String.valueOf(TAG), String.valueOf(msg));
		}
	}

	public static void w(String TAG, String msg, Throwable t) {
		if (logOpen) {
			Log.w(String.valueOf(TAG), String.valueOf(msg), t);
		}
	}
	
	public static void e(String TAG, String msg) {
		if (logOpen) {
			Log.e(String.valueOf(TAG), String.valueOf(msg));
		}
	}

	public static void e(String TAG, String msg, Throwable t) {
		if (logOpen) {
			Log.e(String.valueOf(TAG), String.valueOf(msg), t);
		}
	}
}
