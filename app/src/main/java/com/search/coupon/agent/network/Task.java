package com.search.coupon.agent.network;

/**
 * 网络请求任务ID,发起请求时携带 ，在回调方法中根据此id判断是哪个请求的回调
 * Created by yangwei on 2017/11/6.
 */

public interface Task {

    //注册
    int REGISTER = 0X0000;
    //登录
    int LOGIN = 0X0001;
    //请求上传图片权限
    int REQEUST_OSS_PREMISSION = 0X0003;
    //提交基本信息
    int QUERY_BASIC_PERSON_INFO = 0X0004;
    //注册
    int SIGN_UP = 0X0005;
    //注册获取验证码
    int SIGN_UP_CODE = 0X0006;
    //询价订单
    int PRICE_BILL = 0X0007;
    //忘记密码
    int FORGET_PASSWORD = 0X0008;
    //保存地址信息
    int SAVE_ADRESS = 0X0009;
    //我的页面
    int MINE_INFO = 0X0010;
    //修改用户密码
    int MEND_PASSWORD = 0X0012;
    //退出登录
    int LOGIN_OUT = 0X0013;
    //保存用户邮箱
    int SAVE_USER_EMAIL = 0X0014;
    //版本更新
    int NEW_VERSION = 0X0015;
    //获取银行卡列表信息
    int BIND_BANK_CARD_INFO = 0X0016;
    //获取银行信息
    int QUERY_BANK_INFO = 0X0017;
    //发布微信群信息
    int PUBLISH_WECHAT_GROUP = 0X0017;
    //图片上传接口
    int UPLOAD_IMAGE = 0X0018;
    //获得省级区域列表
    int GET_PROVINCE_LIST = 0X0019;
    //获得省级区域列表
    int GET_CITY_LIST = 0X0020;
    //获取用户微币数量
    int GET_TINY_COIN_NUM = 0X0021;
    //获取订单详情
    int GET_ORDER_DES = 0X0022;
    //用微币支付
    int PAY_BY_COIN = 0X0023;
    //获取分类类目
    int GET_CATEGORY = 0X0024;
    //获取搜索列表
    int GET_SEARCH_LIST = 0X0025;
    //获取首页最新列表
    int GET_NEW_DATA_LIST = 0X0026;
    //获取详情页数据
    int PRODUCT_DETAIL = 0X0027;
    //增加商品浏览量
    int ADD_LOOK_COUNT = 0X0028;
    //获取banner信息
    int BANNER_DATA = 0X0029;
    //账户微币充值
    int RECHARGE_ORDER = 0X0030;
    //刷新订单
    int REFRESH_ORDER = 0X0031;
    //删除订单
    int DELETE_ORDER = 0X0032;
    //获取消息列表
    int MESSAGE_LIST = 0X0033;
    //获取消息列表
    int UPDATE_GROUP = 0X0034;
    //意见反馈
    int SUGGESTION_BACK = 0X0035;
    //检查Token是否到期
    int CHECK_TOKEN_VALIDITY = 0X0036;











    //绑定银行卡
    int CHECK_BANK_CARD = 0X0018;
    //首页基本数据查询
    int APP_BASE_DATA = 0X0019;
    //获取开通城市列表
    int OPEN_CITY = 0X0020;
    //验证短信验证码
    int CHECKSMCODE = 0X0021;

    int AUTO_ADAPTER_BANK = 0X0022;
    //货款订单列表
    int Loan_Order_List=0X0023;

    //返费账户信息查询
    int QUERY_RETURN_FEE_ACCOUNT = 0X0026;

    //影响上传
    int UPLOAD_ZIP_FILE = 0X0024;
    //人工询价
    int ARTIFICIAL_ASK_PRICE = 0X0025;
    //询价订单详情
    int PRICE_BILL_DETAIL = 0X0026;
    //是否设置支付密码
    int WHETHER_SET_PAY_PASSWORD = 0X0027;
    //验证支付密码
    int VERIFICATION_PASSWORD = 0X0028;

    //推荐产品列表
    int RECOMMEND_PRODUCT = 0X0029;
    //推荐产品列表
    int PREFERENCE_PRODUCT = 0X0030;
    //报单
    int SUBMIT_APPLY_FORM = 0X0031;
    //设置支付密码
    int SET_PAY_PASSWORD =  0X0032;
    //修改支付密码
    int UPDATE_PAY_PASSWORD =  0X0033;

    //首页是否有新消息、公告查询
    int APP_MSG_DATA = 0X0034;

    int APPLY_FOR_DEPOSIT = 0X0060;
}
