package com.search.coupon.agent.utils;

/**
 * 配置文件类
 */
public class ConfigUtils {
    public static final ConfigType configType = ConfigType.DEV;


    public static String getIp() {
        switch (configType) {
            case DEV:
                return "http://47.98.195.104";
            case CS:
                return "http://47.98.195.104";
            case UAT:
                return "http://59.110.172.203";
            case RELEASE:
                return "http://www.vxsousuo.com";
        }
        return "";
    }

    /**
     * H5单独部署地址
     * */
    public static String getH5Ip() {
        switch (configType) {
            case DEV:
                return "http://47.98.195.104";
            case CS:
                return "http://47.98.195.104";
            case UAT:
                return "http://59.110.172.203";
            case RELEASE:
                return "http://www.vxsousuo.com";
        }
        return "";
    }

    public static String getAES() {
        switch (configType) {
            case DEV:
            case CS:
            case UAT:
            case RELEASE:
                return "0123456789123456";
        }
        return "";
    }

    public static String getBucket() {
        switch (configType) {
            case DEV:
            case CS:
                return "iqianjindai";
            case UAT:
            case RELEASE:
                return "iqianjindai-prod";
        }
        return "";
    }

    public static String getEndPoint() {
        switch (configType) {
            case DEV:
            case CS:
                return "http://oss-cn-beijing.aliyuncs.com";
            case UAT:
            case RELEASE:
                return "http://resource.iqianjindai.com";
        }
        return "";
    }

    public static boolean getIsEncrypt() {
        switch (configType) {
            case DEV:
                return false;
            case CS:
            case UAT:
            case RELEASE:
                return false;
        }
        return true;
    }

    public static boolean isOpenLog() {
        switch (configType) {
            case DEV:
            case CS:
            case UAT:
                return true;
            case RELEASE:
                return false;
        }
        return true;
    }


    public enum ConfigType {
        DEV, CS, UAT, RELEASE
    }

}
