package com.search.coupon.agent.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 设备相关
 * Created by yangwei on 2017/11/9.
 */

public class DeviceUtils {
    /**
     * 获取当前程序版本名
     *
     * @param context 上下文
     * @return 当前程序版本名
     */
    public static String getCurrentAppVersionName(Context context) {
        String versionName = "";
        try {
            versionName = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取当前程序版本数值
     *
     * @param context 上下文
     * @return 当前程序版本数值
     */
    public static String getCurrentAppVersionCode(Context context) {
        String verCode = "";
        try {
            verCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return verCode;
    }

    /**
     * 当要使用权限却被拒绝时提示跳到设置页面允许权限
     *
     * @param context
     */
    public static void openPermissionSetting(final Context context) {
        DiaLogUtils dialog = DiaLogUtils.creatDiaLog(context);
        dialog.setTitle("提示", Gravity.LEFT)
                .setContent("当前应用缺少必要权限\n打开应用进行权限设置")
                .setSureButton("确定", v -> {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.fromParts("package", context.getPackageName(), null));
                    context.startActivity(intent);
                    dialog.destroyDialog();
                })
                .showDialog();
    }

    /**
     * @param context
     * @return
     * @describe 获取设备唯一标识
     */
    public static String getDeviceUniqueId(Context context) {
        String deviceId = "";
        if (null != context) {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            /**
             * 唯一编号（IMEI, MEID, ESN, IMSI） 缺点 Android设备要具有电话功能 其工作不是很可靠 序列号
             * 当其工作时，该值保留了设备的重置信息（“恢复出厂设置”），从而可以消除当客户删除自己设备上的信息，并把设备转另一个人时发生的错误。
             */
            if (tm != null) {
                try {
                    deviceId = tm.getDeviceId();
                    if (!TextUtils.isEmpty(deviceId)) {
                        return deviceId;
                    }
                } catch (Exception e) {

                }
                try {
                    deviceId = tm.getSubscriberId();
                    if (!TextUtils.isEmpty(deviceId)) {
                        return deviceId;
                    }
                } catch (Exception e) {

                }
            }

            WifiManager wifi = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            deviceId = info.getMacAddress();
            if (!TextUtils.isEmpty(deviceId)) {
                return deviceId;
            }
        }
        // 序列号 缺点序列号无法在所有Android设备上使用
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            deviceId = (String) (get.invoke(c, "ro.serialno", "unknown"));
            if (!TextUtils.isEmpty(deviceId)) {
                return deviceId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * ANDROID_ID 缺点 对于Android 2.2（“Froyo”）之前的设备不是100％的可靠
         * 此外，在主流制造商的畅销手机中至少存在一个众所周知的错误，每一个实例都具有相同的ANDROID_ID。
         */
        if (null != context) {
            deviceId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            if (!TextUtils.isEmpty(deviceId)
                    && !"9774d56d682e549c".equals(deviceId)) {
                return deviceId;
            }
        }

//        if (TextUtils.isEmpty(deviceId)) {
//            deviceId = PrefUtils.getPrefString(App.Instance(), SPConstant.IMEI, "");
//            if (!TextUtils.isEmpty(deviceId)) {
//                return deviceId;
//            } else {
//                final SecureRandom random = new SecureRandom();
//                deviceId = new BigInteger(64, random).toString(16);
//                PrefUtils.setPrefString(App.Instance(), SPConstant.IMEI, deviceId);
//                return deviceId;
//            }
//        }
        return deviceId;
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    /**
     * 隐藏输入软键盘
     */
    public static void hideInputKeyBoad(Activity activity) {

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }
    /**
     *获取设备手机MAC信息，需要申请权限："android.permission.ACCESS_WIFI_STATE"
     *
     * @param context
     *
     * @return Returns WiFi MAC, 格式例如："68:3E:34:A5:5F:C1"
     */
    public static String getMac(Context context) {
        String INVALID_MAC = "00:00:00:00:00:00";
        Pattern MAC_PATTERN = Pattern.compile("^([0-9A-F]{2}:){5}([0-9A-F]{2})$");
        String mac = null;
        //Android M 以上系统返回默认的MAC 地址
        String defaultMacAddress = "02:00:00:00:00:00";
        try {
            //Android M 上获取 WiFi Mac
            if(Build.VERSION.SDK_INT >=23){
                try {
                    List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
                    if(all == null || all.size() <= 0){
                        return defaultMacAddress;
                    }
                    for (NetworkInterface nif : all) {
                        if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
                        byte[] macBytes = nif.getHardwareAddress();
                        if (macBytes == null) {
                            return "";
                        }
                        StringBuilder res1 = new StringBuilder();
                        for (byte b : macBytes) {
                            res1.append(String.format("%02X:",b));
                        }
                        if (res1.length() > 0) {
                            res1.deleteCharAt(res1.length() - 1);
                        }
                        mac = res1.toString().toUpperCase().trim();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                return (!(mac == null || "".equals(mac.trim())) ? mac : defaultMacAddress);
            }else{
                if (context.checkCallingOrSelfPermission(
                        android.Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {
                    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    if (wifiManager.isWifiEnabled()) {
                        WifiInfo info = wifiManager.getConnectionInfo();
                        if (info != null) {
                            mac = info.getMacAddress();
                            if (mac != null) {
                                mac = mac.toUpperCase().trim();
                                if (INVALID_MAC.equals(mac)
                                        || !MAC_PATTERN.matcher(mac).matches()) {
                                    mac = null;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return mac;
    }

    /**
     *获取设备WiFi MAC信息，需要申请权限："android.permission.ACCESS_WIFI_STATE"
     *
     * @param context
     *
     * @return Returns WiFi MAC, 格式例如："68:3E:34:A5:5F:C1"
     */
    public static String getWiFiMac(Context context) {
        String mac = null;
        if (context.checkCallingOrSelfPermission(
                android.Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (wifiManager.isWifiEnabled()) {
                WifiInfo info = wifiManager.getConnectionInfo();
                if (info != null) {
                    mac = info.getMacAddress();
                    if (mac != null) {
                        mac = mac.toUpperCase().trim();
                    }
                }
            }
        }
        return mac;
    }

    /**
     *获取SIM IMSI 信息，需要申请权限："android.permission.READ_PHONE_STATE"
     *
     * @param context
     *
     * @return Returns SIM IMSI
     *
     * List size为2，卡槽1的SIM IMSI对应List[0],卡槽2的SIM IMSI对应List[1]
     * List size为1，卡槽的SIM IMSI对应List[0]
     * 如果卡槽位置对应没有SIM卡，对应值为空字符串""
     * 如果是List size为0，没有权限获取IMSI信息
     */
    public static List<String> getSimSubscriberId(Context context) {
        List<String> imsis = new ArrayList<>();
        if (context == null) {
            return imsis;
        }
        try {
            if (Build.VERSION.SDK_INT >=22) {
                TelephonyManager tm = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
                SubscriptionManager sm = (SubscriptionManager) context.getSystemService(
                        Context.TELEPHONY_SUBSCRIPTION_SERVICE);
                for (int index=0; index < 2; index ++) {
                    SubscriptionInfo info = sm.getActiveSubscriptionInfoForSimSlotIndex(index);
                    if (info != null) {
                        int subId = info.getSubscriptionId();
                        // IMSI
                        Method method = tm.getClass().getMethod("getSubscriberId", int.class);
                        method.setAccessible(true);
                        Object ret = method.invoke(tm, subId);
                        imsis.add(index, ret == null ? "" : (String) ret);
                    }
                }
                return imsis;
            } else {
                TelephonyManager telephonyManager1 = (TelephonyManager) context
                        .getSystemService("phone1");
                if (telephonyManager1 != null) {
                    String imsi1 = telephonyManager1.getSubscriberId();
                    imsis.add(0, imsi1 == null ? "" : imsi1);
                }
                TelephonyManager telephonyManager2 = (TelephonyManager) context
                        .getSystemService("phone2");
                if (telephonyManager1 != null) {
                    String imsi2 = telephonyManager2.getSubscriberId();
                    imsis.add(1, imsi2 == null ? "" : imsi2);
                }
                return imsis;
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return imsis;
    }
    //获取ip地址
    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    /**
     * 获取当前市场渠道
     *
     * @param context 上下文
     * @return 当前程序版本数值
     */
    public static String getCurrentAppMarketSource(Context context) {

        String msg = "";
        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            msg = info.metaData.getString("UMENG_CHANNEL");
        } catch (Exception e) {
            msg = "";
        }
        return msg;
    }
}
