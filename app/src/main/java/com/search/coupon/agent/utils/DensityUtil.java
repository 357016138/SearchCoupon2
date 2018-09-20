package com.search.coupon.agent.utils;

import android.content.Context;

public class DensityUtil {

	/**
	 * 根据手机的分辨率dp转成px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率px(像素) 转成dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率sp转成px(像素)
	 */
	public static int sp2px(Context context, float sp) {
		final float scale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (sp * scale + 0.5f);
	}

	/**
	 * 获取屏幕宽度
	 */
	public static int getWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	/**
	 * 获取屏幕高度
	 */
	public static int getHeight(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}
}