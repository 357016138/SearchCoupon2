package com.search.coupon.agent.utils;

import android.text.TextUtils;

import com.search.coupon.agent.bean.UserBean;
import com.search.coupon.agent.common.ShareData;


/**
 * 用户信息工具类
 * Created by fan on 2017/11/11.
 */
public class UserUtils {
    /**
     * 保存用户信息
     *
     * @param userBean
     */
    public static void saveLoginUserInfo(UserBean userBean) {
        if (userBean != null) {
            ShareData.setShareStringData(ShareData.USER_CITY, userBean.getCity());
            ShareData.setShareStringData(ShareData.USER_CITYCODE, userBean.getCityCode());
            ShareData.setShareStringData(ShareData.USER_INVITER, userBean.getInviter());
            ShareData.setShareStringData(ShareData.USER_ISPAYPASS, userBean.getIsPayPass());

            ShareData.setShareStringData(ShareData.USER_ID, userBean.getId());
            ShareData.setShareStringData(ShareData.USER_PHONE, userBean.getPhoneNumber());


            UserManager.clear();
        }
    }

    //是否登录
    public static boolean isLogin() {
        return !TextUtils.isEmpty(ShareData.getShareStringData(ShareData.USER_ID));
    }

    //退出登录
    public static void loginOut() {
        ShareData.setShareStringData(ShareData.USER_CITY, "");
        ShareData.setShareStringData(ShareData.USER_CITYCODE, "");
        ShareData.setShareStringData(ShareData.USER_INVITER, "");
        ShareData.setShareStringData(ShareData.USER_ISPAYPASS, "");

        ShareData.setShareStringData(ShareData.USER_ID, "");
        ShareData.setShareStringData(ShareData.USER_PHONE, "");
        ShareData.setShareStringData(ShareData.USER_COOKIE, "");
        UserManager.clear();
    }

    //清除手势密码
    public static void clearGesture() {
        ShareData.setShareStringData(ShareData.GESTURE_PATTERN, "");
        ShareData.setShareBooleanData(ShareData.GESTURE_IS_OPEN, false);
        ShareData.setShareIntData(ShareData.GESTURE_ERROR_NUM, 5);
    }
}
