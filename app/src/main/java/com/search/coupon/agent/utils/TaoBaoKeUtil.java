/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package com.search.coupon.agent.utils;

import com.search.coupon.agent.utils.compress.MD5Util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 淘宝客工具类
 * http://open.taobao.com/api.htm?docId=24515&docType=2
 */
public class TaoBaoKeUtil {


  /**
   * 简化版,使用MD5生成签名 对所有API请求参数（包括公共参数和业务参数，但除去sign参数和byte[]类型的参数），根据参数名称的ASCII码表的顺序排序。如：foo:1, bar:2,
   * foo_bar:3, foobar:4排序后的顺序是bar:2, foo:1, foo_bar:3, foobar:4。 将排序好的参数名和参数值拼装在一起，根据上面的示例得到的结果为：bar2foo1foo_bar3foobar4。
   * 把拼装好的字符串采用utf-8编码，使用签名算法对编码后的字节流进行摘要。如果使用MD5算法，则需要在拼装的字符串前后加上app的secret后，再进行摘要，如：md5(secret+bar2foo1foo_bar3foobar4+secret)；如果使用HMAC_MD5算法，则需要用app的secret初始化摘要算法后，再进行摘要，如：hmac_md5(bar2foo1foo_bar3foobar4)。
   * 将摘要得到的字节流结果使用十六进制表示，如：hex(“helloworld”.getBytes(“utf-8”)) = “68656C6C6F776F726C64”
   */
  public static String signTopRequest(Map<String, Object> params, String secret) {
    try {
      // 第一步：检查参数是否已经排序
      String[] keys = params.keySet().toArray(new String[0]);
      Arrays.sort(keys);
      // 第二步：把所有参数名和参数值串在一起
      StringBuilder query = new StringBuilder();
      query.append(secret);
      for (String key : keys) {
        String value = (String) params.get(key);
        if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(value)) {
          query.append(key).append(value);
        }
      }
      // 第三步：使用MD5加密
      query.append(secret);
      return MD5Util.MD5Encryption(query.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * 获取通用参数的Map
   */
  public static Map<String, String> getCommonParams(String methodName) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    HashMap<String, String> params = new HashMap<>();
    params.put("method", methodName);
    params.put("app_key", TaoBaoConstants.APP_KEY);
    params.put("sign_method", "md5");
    params.put("timestamp", dateFormat.format(new Date()));
    params.put("format", "json");
    params.put("v", "2.0");
    params.put("simplify", "false");
    return params;
  }


}
