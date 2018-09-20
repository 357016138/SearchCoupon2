package com.search.coupon.agent.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

public class SpanUtils {
	public static SpannableStringBuilder spBuilder;
	public static SpannableString msp;

	/**
	 *
	 * @param context
	 * @param wholeStr
	 *            全部文字
	 * @param highlightStr
	 *            改变颜色的文字
	 * @param color
	 *            颜色
	 */
	public static SpannableStringBuilder getResult(Context context,
                                                   String wholeStr, String highlightStr, int color) {
		int start = 0, end = 0;
		if (wholeStr.contains(highlightStr)) {
			/*
			 * 返回highlightStr字符串wholeStr字符串中第一次出现处的索引。
			 */
			start = wholeStr.indexOf(highlightStr);
			end = start + highlightStr.length();
		}
		spBuilder = new SpannableStringBuilder(wholeStr);
		color = context.getResources().getColor(color);
		CharacterStyle charaStyle = new ForegroundColorSpan(color);
		spBuilder.setSpan(charaStyle, start, end,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		if (spBuilder != null) {
			return spBuilder;
		}
		return null;
	}

	/**
	 *
	 * @param context
	 * @param wholeStr
	 *            全部文字
	 * @param highlightStr
	 *            改变颜色的文字
	 * @param color
	 *            颜色
	 */
	public static SpannableString getElectricityText(Context context,
                                                     String wholeStr, String highlightStr, int color) {
		try {
			int start = 0, end = 0;
			if (wholeStr.contains(highlightStr)) {
				/*
				 * 返回highlightStr字符串wholeStr字符串中第一次出现处的索引。
				 */
				start = wholeStr.indexOf(highlightStr);
				end = start + highlightStr.length();
			}
			msp = new SpannableString(wholeStr);
			color = context.getResources().getColor(color);
			// 设置字体大小（绝对值,单位：像素）
			msp.setSpan(new AbsoluteSizeSpan(20, true), start, end,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//			msp.setSpan(new ForegroundColorSpan(context.getResources()
//					.getColor(R.color.text_green_color)), start, end,
//					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			msp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start,
					end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 正常
			return msp;
		} catch (Exception e) {
			return null;
		}
	}

	/***
	 * 获取点击Spannable
	 *
	 * @param context
	 * @param spBuilder
	 *            Spannable，可为空
	 * @param wholeStr
	 *            全部文字
	 * @param clickStr
	 *            点击文字
	 * @param clickSpan
	 *            点击事件
	 * @return
	 */
	public static SpannableStringBuilder getClickSpan(Context context,
                                                      SpannableStringBuilder spBuilder, String wholeStr, String clickStr,
                                                      ClickableSpan clickSpan) {
		try {
			int start = 0, end = 0;
			if (wholeStr.contains(clickStr)) {
				/*
				 * 返回highlightStr字符串wholeStr字符串中第一次出现处的索引。
				 */
				start = wholeStr.indexOf(clickStr);
				end = start + clickStr.length();
			}
			if (spBuilder == null) {
				spBuilder = new SpannableStringBuilder(wholeStr);
			}
			if (clickSpan == null) {
				return spBuilder;
			}
			spBuilder.setSpan(clickSpan, start, end,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		} catch (Exception e) {
			return new SpannableStringBuilder(wholeStr);
		}
		return spBuilder;
	}

	/***
	 * 获取颜色span
	 * @param context
	 * @param spBuilder
	 * @param wholeStr
	 * @param colorStr
	 * @param color
	 * @return
	 */
	public static SpannableStringBuilder getColorSpan(Context context,
                                                      SpannableStringBuilder spBuilder, String wholeStr, String colorStr,
                                                      int color) {
		try {
			int start = 0, end = 0;
			if (wholeStr.contains(colorStr)) {
				/*
				 * 返回highlightStr字符串wholeStr字符串中第一次出现处的索引。
				 */
				start = wholeStr.indexOf(colorStr);
				end = start + colorStr.length();
			}
			if (spBuilder == null) {
				spBuilder = new SpannableStringBuilder(wholeStr);
			}
			color = context.getResources().getColor(color);
			spBuilder.setSpan(new ForegroundColorSpan(color), start, end,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		} catch (Exception e) {
			return new SpannableStringBuilder(wholeStr);
		}
		return spBuilder;
	}

	/***
	 *
	 * @param context
	 * @param spBuilder
	 * @param wholeStr
	 *            全部文字
	 * @param sizeStr
	 *            改变大小文字
	 * @param size
	 *            文字大小，单位dp
	 * @return
	 */
	public static SpannableStringBuilder getSizeSpan(Context context,
                                                     SpannableStringBuilder spBuilder, String wholeStr, String sizeStr,
                                                     int size) {
		try {
			int start = 0, end = 0;
			if (wholeStr.contains(sizeStr)) {
				/*
				 * 返回highlightStr字符串wholeStr字符串中第一次出现处的索引。
				 */
				start = wholeStr.indexOf(sizeStr);
				end = start + sizeStr.length();
			}
			if (spBuilder == null) {
				spBuilder = new SpannableStringBuilder(wholeStr);
			}
			spBuilder.setSpan(new AbsoluteSizeSpan(size, true), start, end,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		} catch (Exception e) {
			return new SpannableStringBuilder(wholeStr);
		}
		return spBuilder;
	}

	/***
	 * 获取style span
	 *
	 * @param context
	 * @param spBuilder
	 * @param wholeStr
	 * @param styleStr
	 * @return
	 */
	public static SpannableStringBuilder getStyleSpan(Context context,
                                                      SpannableStringBuilder spBuilder, String wholeStr, String styleStr,
                                                      int style) {
		try {
			int start = 0, end = 0;
			if (wholeStr.contains(styleStr)) {
				/*
				 * 返回highlightStr字符串wholeStr字符串中第一次出现处的索引。
				 */
				start = wholeStr.indexOf(styleStr);
				end = start + styleStr.length();
			}
			if (spBuilder == null) {
				spBuilder = new SpannableStringBuilder(wholeStr);
			}
			spBuilder.setSpan(new StyleSpan(style), start, end,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		} catch (Exception e) {
			return new SpannableStringBuilder(wholeStr);
		}
		return spBuilder;
	}

	/***
	 * 获取span
	 * @param context
	 * @param spBuilder
	 * @param wholeStr
	 * @param strs 改变的数组，0改变颜色，1改变大小，2加粗，3倾斜
	 * @param color 颜色
	 * @param size 大小
	 * @return
	 */
	public static SpannableStringBuilder getSpan(Context context,
                                                 SpannableStringBuilder spBuilder, String wholeStr, String[] strs,
                                                 int color, int size) {
		if(strs==null||strs.length==0){
			return new SpannableStringBuilder(wholeStr);
		}
		try {
			if(strs.length>0&&!TextUtils.isEmpty(strs[0])){
				spBuilder=getColorSpan(context, spBuilder, wholeStr, strs[0], color);
			}
			if(strs.length>1&&!TextUtils.isEmpty(strs[1])){
				spBuilder=getSizeSpan(context, spBuilder, wholeStr, strs[1], size);
			}
			if(strs.length>2&&!TextUtils.isEmpty(strs[2])){
				spBuilder=getStyleSpan(context, spBuilder, wholeStr, strs[2], android.graphics.Typeface.BOLD);
			}
			if(strs.length>3&&!TextUtils.isEmpty(strs[3])){
				spBuilder=getStyleSpan(context, spBuilder, wholeStr, strs[2], android.graphics.Typeface.ITALIC);
			}
		} catch (Exception e) {
			return new SpannableStringBuilder(wholeStr);
		}
		return spBuilder;
	}


}
