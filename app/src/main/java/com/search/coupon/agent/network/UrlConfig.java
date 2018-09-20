package com.search.coupon.agent.network;


import com.search.coupon.agent.utils.ConfigUtils;

/**
 * URL配置
 */
public interface UrlConfig {

    String KEY = "abcdnnnnnn123456";

    String IP = ConfigUtils.getIp();
    String H5_IP = ConfigUtils.getH5Ip();

    //AES KEY
    String AES_SECRET = ConfigUtils.getAES();
    //bucket
    String BUCKET = ConfigUtils.getBucket();
    //endpoint
    String ENDPOINT = ConfigUtils.getEndPoint();

    String URL = IP ;

    //注册
    String URL_REGISTER = URL + "/user/register.json";
    //登录
    String URL_LOGIN = URL + "/user/login.json";
    //版本更新接口
    String URL_GET_NEW_VERSION = URL + "/version/new.json";
    //获取验证码（注册、密码找回、绑卡、设置支付密码）
    String URL_SIGN_IN_CODE = URL + "/user/registerCode.json";
    //忘记密码(重置)
    String URL_FORGET_PASSWORD = URL + "/user/forget/password.json";
    //修改密码
    String URL_UPDATE_PASSWORD = URL + "/user/update/password.json";
    //图片上传接口
    String URL_UPLOAD_IMAGE = URL + "/upload/qiuniuupload.json";
    //发布微信群信息
    String URL_PUBLISH_WECHAT_GROUP = URL + "/upload/groupInfo.json";
    //获得省级区域列表
    String URL_GET_PROVINCE_LIST = URL + "/area/province.json";
    //获得市级区域列表
    String URL_GET_CITY_LIST = URL + "/area/city.json";
    //获取用户微币数量
    String URL_GET_TINY_COIN_NUM = URL + "/vcoin/get.json";
    //获取订单详情
    String URL_GET_ORDER_DES = URL + "/order/getByOrderId.json";
    //用微币支付
    String URL_PAY_BY_COIN = URL + "/order/pay/self.json";
    //获取分类类目
    String URL_GET_CATEGORY = URL + "/category/all.json";
    //获取搜索列表
    String URL_GET_SEARCH_LIST = URL + "/search/group.json";
    //获取首页最新列表
    String URL_GET_NEW_DATA_LIST = URL + "/search/group/new.json";
    //获取详情页数据
    String URL_PRODUCT_DETAIL = URL + "/group/get/id.json";
    //增加商品浏览量
    String URL_ADD_LOOK_COUNT = URL + "/exter/add/lookCount.json";
    //获取banner信息
    String URL_BANNER_DATA = URL + "/banner/get.json";
    //查询发布订单列表
    String URL_PRICE_BILL = URL + "/order/show/all.json";
    //账户微币充值
    String URL_RECHARGE_ORDER = URL + "/vcoin/order.json";
    //支付回调地址
    String URL_PAY_NOTIFY = URL + "/order/pay/notify.json";
    //刷新订单
    String URL_REFRESH_ORDER = URL + "/order/refresh/group.json";
    //删除订单
    String URL_DELETE_ORDER = URL + "/group/edit/delete.json";
    //获取消息列表
    String URL_MESSAGE_LIST = URL + "/message/get/all.json";
    //修改微信群信息
    String URL_UPDATE_GROUP = URL + "/group/edit/update.json";
    //意见反馈
    String URL_SUGGESTION_BACK = URL + "/opinion/add.json";
    //检查Token是否到期
    String URL_CHECK_TOKEN_VALIDITY = URL + "/user/token/check.json";





    //我的消息列表
    String URL_MSG_LIST = H5_IP + "/#/msgCenter?pageType=0&pid=%s&userId=%s";

    //系统公告列表
    String URL_NOTICE_LIST = H5_IP + "/#/msgCenter?pageType=1&pid=%s&userId=%s";

    //贷款攻略
    String URL_LOAN_RAIDERS = H5_IP + "/#/loanRaiders";

    //贷款试算
    String URL_LOAN_CALCULATION = H5_IP + "/#/loanCalculation";

    //佣金试算
    String URL_COMMISION_CALCULATION = H5_IP + "/#/commisionCalculation";

    //帮助中心
    String URL_HELP_CENTER = H5_IP + "/#/helpCenter";

    //联系我们
    String URL_CONTACT_US = H5_IP + "/#/contactUs";

    //关于我们
    String URL_ABOUT_US = H5_IP + "/#/aboutUs";

    //我要吐槽
    String URL_SUGGEST = H5_IP + "/#/suggest?pid=%s&userId=%s";

    //贷款订单详情
    String URL_LOAN_ORDER_DETAIL = H5_IP + "/#/loanDetail?pid=%s&orderNum=%s&userId=%s";

    //用户注册协议
    String H5_USER_REGISTER_PROTOCAL = H5_IP + "/#/userServiceProtocol";

    //获取银行卡图标
    String URL_BANK_ICON = H5_IP + "/appStore/img/bank/";

    //查询银行卡列表信息
    String URL_QUERY_BIND_BANK_CARD_INFO = URL + "/loanApp/rest/queryBankCard/v1/";

    //查询开户行列表信息
    String URL_QUERY_BANK_CARD_LIST = URL + "/loanApp/rest/queryBankList/v1/";

    //自动适配银行卡
    String URL_AUTO_ADAPTER_BANK = URL + "/loanApp/rest/judgeBankCardNoToBank/v1/";

    //获取开户行
    String URL_OPEN_BANK = URL + "/appDebitCard/selectBank/v1";

    //银行卡绑定
    String URL_BIND_BANK_CARD = URL + "/loanApp/rest/bindingBankCard/v1/";

    //获取开通城市列表
    String URL_OPEN_CITY = URL + "/loanApp/rest/findCityList/v1/";

    //验证手机验证码
    String URL_CHECKSMCODE = URL + "/loanApp/rest/checkSMCode/v1/";

    //首页基本数据查询接口
    String URL_GET_APP_BASE_DATA = URL + "/loanApp/rest/queryApptplatformdata/v1/";

    //首页是否有新消息、公告查询接口
    String URL_GET_MSG_DATA = URL + "/loanApp/rest/queryNoticeIsRead/v1/";

    //影响上传接口
    String URL_UPLOAD_ZIP_FILE = URL + "/loanApp/rest/uploadFileChannelZip/v1/";

    //人工询价接口
    String URL_ARTIFICIAL_ASK_PRICE = URL + "/loanApp/rest/queryArtificialInquiry/v1/";

    //货款订单列表
    String URL_LOAN_ORDER_LIST = URL + "/loanApp/rest/queryLoanOrderList/v1/";

    //是否设置支付密码
    String URL_WHETHER_SET_PAYMENT_PASSWORD = URL + "/loanApp/rest/whetherSetUpPaymentPassword/v1/";

    //验证支付密码
    String URL_VERIFICATION_PASSWORD = URL + "/loanApp/rest/verifyPayment/v1/";

    //设置支付密码
    String URL_SET_PAY_PASSWORD = URL + "/loanApp/rest/setPayment/v1/";

    //修改支付密码
    String URL_UPDATE_PAY_PASSWORD = URL + "/loanApp/rest/updatePayment/v1/";

    //询价订单详情
    String URL_PRICE_BILL_DETAIL = URL + "/loanApp/rest/queryInquiryOrderInfo/v1/";

    //推荐产品列表
    String URL_RECOMMEND_PRODUCT = URL + "/loanApp/rest/queryProductList/v1/";

    //优选产品列表
    String URL_PREFERENCE_PRODUCT = URL + "/loanApp/rest/queryByConditionProductList/v1/";

    //报单接口
    String URL_SUBMIT_APPLY_FORM = URL + "/loanApp/rest/saveDeclarationForm/v1/";

    //返费账户信息查询
    String URL_QUERY_RETURN_FEE_ACCOUNT = URL + "/loanApp/rest/queryReturnFeeAccount/v1/";

    // 提现申请
    String URL_APPLY_FOR_DEPOSIT = URL + "/loanApp/rest/saveReturnFeeAccountApply/v1/";












}
