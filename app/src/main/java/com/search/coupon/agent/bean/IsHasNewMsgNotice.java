package com.search.coupon.agent.bean;

import java.io.Serializable;

/**
 * @author baipeng
 * @Title IsHasNewMsgNotice
 * @Date 2018/3/14 14:22
 * @Description IsHasNewMsgNotice.
 */
public class IsHasNewMsgNotice implements Serializable {
    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    private String isRead;
}
