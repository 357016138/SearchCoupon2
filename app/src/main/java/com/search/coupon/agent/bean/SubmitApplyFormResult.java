package com.search.coupon.agent.bean;

import java.io.Serializable;

/**
 * @author baipeng
 * @Title SubmitApplyFormResult
 * @Date 2018/3/9 17:25
 * @Description SubmitApplyFormResult.
 */
public class SubmitApplyFormResult implements Serializable {
    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    private String orderNum;
}
