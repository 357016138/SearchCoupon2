package com.search.coupon.agent.bean;

import java.io.Serializable;

/**
 * @author baipeng
 * @Title AppBaseDataBean
 * @Date 2018/3/2 14:38
 * @Description AppBaseDataBean.
 */
public class AppBaseDataBean implements Serializable {
    /**
     * accumulatedBorrower :累计借款人
     * limitLoan :贷款总额
     * customerManagers :客户经理数
     * limitBrokerage :佣金总额
     * highestLoan :最高贷款
     */
    private String accumulatedBorrower;
    private String limitLoan;
    private String customerManagers;
    private String limitBrokerage;
    private String highestLoan;

    public String getAccumulatedBorrower() {
        return accumulatedBorrower;
    }

    public void setAccumulatedBorrower(String accumulatedBorrower) {
        this.accumulatedBorrower = accumulatedBorrower;
    }

    public String getLimitLoan() {
        return limitLoan;
    }

    public void setLimitLoan(String limitLoan) {
        this.limitLoan = limitLoan;
    }

    public String getCustomerManagers() {
        return customerManagers;
    }

    public void setCustomerManagers(String customerManagers) {
        this.customerManagers = customerManagers;
    }

    public String getLimitBrokerage() {
        return limitBrokerage;
    }

    public void setLimitBrokerage(String limitBrokerage) {
        this.limitBrokerage = limitBrokerage;
    }

    public String getHighestLoan() {
        return highestLoan;
    }

    public void setHighestLoan(String highestLoan) {
        this.highestLoan = highestLoan;
    }
}
