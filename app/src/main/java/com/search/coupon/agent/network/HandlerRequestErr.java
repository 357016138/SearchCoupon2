package com.search.coupon.agent.network;

import android.content.Context;
import android.text.TextUtils;

import com.search.coupon.agent.utils.UtilTools;


/***
 * 网络请求返回信息处理
 */
public class HandlerRequestErr {
    /***
     * 判断网络请求是否成功及错误处理，错误信息会提示
     *
     * @param context
     * @param data
     * @return code为200或0000返回true，否则返回false
     */
    public static boolean handlerRequestErr(Context context, ResultData data) {
        return handlerRequestErr(context, data, true);
    }

    /***
     * 判断网络请求是否成功及错误处理
     *
     * @param context
     * @param data
     * @param isTips
     *            是否提示错误信息
     * @return code为200或0000返回true，否则返回false
     */
    public static boolean handlerRequestErr(Context context, ResultData data, boolean isTips) {
        String rspCode = data.getRspCode();
        String rspMsg = data.getRspMsg();
        if (ErrorCode.SUCCESS.equals(rspCode)) {
            return true;
        }
        if (context == null || !isTips) {
            return false;
        }
        UtilTools.toast(context, TextUtils.isEmpty(rspMsg) ? getLocalErrMsg(context, rspCode) : rspMsg);
        return false;
    }

    /***
     * 获取常规网络错误提示
     *
     * @param state
     * @return
     */
    public static String getLocalErrMsg(Context context, String state) {
        String msg = "";
        if (ErrorCode.SOCKET_TIME_OUT.equals(state)) {
            msg = "服务器响应超时";
        } else if (ErrorCode.CONNECT_ERR.equals(state)) {
            msg = "服务器连接失败";
        } else {
            msg = "数据请求失败，请稍后重试";
        }
        return msg;
    }
}
