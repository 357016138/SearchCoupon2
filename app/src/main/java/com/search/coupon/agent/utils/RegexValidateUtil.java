package com.search.coupon.agent.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @Description：正则表达式验证输入格式
 * @author: cj
 * @date: 2017/3/15 20:45
 */
public class RegexValidateUtil {
    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        boolean flag;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 验证手机号码
     *
     * @param mobileNumber
     * @return
     */
    public static boolean checkMobileNumber(String mobileNumber) {
//        boolean flag = true;
//        try {
//            //Pattern regex = Pattern.compile("^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
//            //Pattern regex = Pattern.compile("^(((13[0-9])|(14[5,7])|(15([0-3]|[5-9]))|(17[0,6,7,8])|(18[0,0-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
//            Pattern regex = Pattern.compile("^(((13[0-9])|(14[5,7])|(15([0-3]|[5-9]))|(17[0,0-9])|(18[0,0-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
//            Matcher matcher = regex.matcher(mobileNumber);
//            flag = matcher.matches();
//        } catch (Exception e) {
//            flag = false;
//        }
//        return flag;

        if (mobileNumber.trim().length() == 11) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 只允许字母、数字和汉字
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String stringFilter(String str) throws PatternSyntaxException {
        String regEx = "[^a-zA-Z0-9\u4E00-\u9FA5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 验证支付密码（只允许输入6-12位数字或字母）
     *
     * @param payPassword
     * @return
     */
    public static boolean checkPayPwd(String payPassword) {
        boolean flag;
        try {
            Pattern regex = Pattern.compile("[0-9A-Za-z]{6,12}");
            Matcher matcher = regex.matcher(payPassword);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 验证登录密码（只允许输入8-16位数字+字母组合）
     *
     * @param psd
     * @return
     */
    public static boolean checkPwd(String psd) {
        boolean flag;
        try {
            Pattern regex = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$");
            Matcher matcher = regex.matcher(psd);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 验证输入的名字是否为“中文”或者是否包含“·”
     */
    public static boolean isLegalName(String name){
        if (name.contains("·") || name.contains("•")){
            if (name.matches("^[\\u4e00-\\u9fa5]+[·•][\\u4e00-\\u9fa5]+$")){
                return true;
            }else {
                return false;
            }
        }else {
            if (name.matches("^[\\u4e00-\\u9fa5]+$")){
                return true;
            }else {
                return false;
            }
        }
    }


}
