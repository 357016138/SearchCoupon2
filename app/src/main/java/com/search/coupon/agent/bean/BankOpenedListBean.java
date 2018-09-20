package com.search.coupon.agent.bean;

/**
 * Created by dell on 2017/11/13.
 */
public class BankOpenedListBean {

    private String bankCode;
    private String bankName;
    private String singleAmount;
    private String SingleDayLimit;

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getSingleAmount() {
        return singleAmount;
    }

    public void setSingleAmount(String singleAmount) {
        this.singleAmount = singleAmount;
    }

    public String getSingleDayLimit() {
        return SingleDayLimit;
    }

    public void setSingleDayLimit(String singleDayLimit) {
        this.SingleDayLimit = singleDayLimit;
    }

}
