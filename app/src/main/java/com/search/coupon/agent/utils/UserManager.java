package com.search.coupon.agent.utils;

import android.text.TextUtils;

import com.search.coupon.agent.common.ShareData;


/**
 * 用户信息管理类
 * Created by fan on 2017/11/11.
 */
public class UserManager {

    private static String city; // 城市名称
    private static String cityCode; // 城市编码
    private static String inviter; // 邀请人
    private static String phone; // 手机号
    private static String userId; // 用户ID
    private static String isPayPass; // 是否设置支付密码（0 未设置；1 已设置）




    public static String getCity() {
        if (TextUtils.isEmpty(city)) {
            city = ShareData.getShareStringData(ShareData.USER_CITY);
        }
        return city;
    }

    public static String getCityCode() {
        if (TextUtils.isEmpty(cityCode)) {
            cityCode = ShareData.getShareStringData(ShareData.USER_CITYCODE);
        }
        return cityCode;
    }

    public static String getInviter() {
        if (TextUtils.isEmpty(inviter)) {
            inviter = ShareData.getShareStringData(ShareData.USER_INVITER);
        }
        return inviter;
    }

    public static String getPhone() {
        if (TextUtils.isEmpty(phone)) {
            phone = ShareData.getShareStringData(ShareData.USER_PHONE);
        }
        return phone;
    }

    public static String getUserId() {
        if (TextUtils.isEmpty(userId)) {
            userId = ShareData.getShareStringData(ShareData.USER_ID);
        }
        return userId;
    }

    public static String getIsPayPass() {
        isPayPass = ShareData.getShareStringData(ShareData.USER_ISPAYPASS);
        return isPayPass;
    }

    public static void clear() {
        //busiCode = "";
        city = "";
        cityCode = "";
        //frontTransNo = "";
        //interfaceNo = "";
        inviter = "";
        phone = "";
        //retTime = "";
        //serverTransNo = "";
        userId = "";
        isPayPass = "";
    }
}
