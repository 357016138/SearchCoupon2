package com.search.coupon.agent.bean;

import java.io.Serializable;

/**
 * @author baipeng
 * @Title ProductCodeBean
 * @Date 2018/3/10 13:14
 * @Description ProductCodeBean.
 */
public class ProductCodeBean implements Serializable {
    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    private String productCode;
}
