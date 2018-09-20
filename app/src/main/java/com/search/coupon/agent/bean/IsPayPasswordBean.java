package com.search.coupon.agent.bean;

import java.io.Serializable;

/**
 * Created by song on 2018/3/10 0010.
 */

public class IsPayPasswordBean implements Serializable {

    private String isPayPass;   //是否设置支付密码 0未设置  1已设置

    public String getIsPayPass() {
        return isPayPass;
    }

    public void setIsPayPass(String isPayPass) {
        this.isPayPass = isPayPass;
    }
}
