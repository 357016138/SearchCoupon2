package com.search.coupon.agent.bean;

/**
 * Created by Administrator on 2018/4/1.
 *
 * 查询发布订单详情
 *
 */

public class OrderBean {

    /**
     data.id	      long	   订单自增id
     data.orderId     string	   订单uuid
     data.orderName   string	订单名称
     data.orderType   int	订单类型
     data.userId      long	用户id
     data.orginMoney  long	支付原价(单位分)
     data.payMoney   long	实际要支付的价格
     data.status     int	 支付状态 1.未支付2.支付成功.3支付失败
     data.payType    int	支付渠道  1支付宝2微信3银联 4微币 5优惠券抵扣

     * */
    private String id;
    private String orderId;
    private String orderName;
    private String orderType;
    private String userId;
    private long   orginMoney;
    private long   payMoney;
    private String status;
    private String payType;
    private String appKey;
    private String updateDate;
    private String codeType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getOrginMoney() {
        return orginMoney;
    }

    public void setOrginMoney(long orginMoney) {
        this.orginMoney = orginMoney;
    }

    public long getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(long payMoney) {
        this.payMoney = payMoney;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }
}
