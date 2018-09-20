package com.search.coupon.agent.network;

import java.io.Serializable;

/**
 * Created by ____ Bye丶 on 2017/3/23.
 */
public class ResultData implements Serializable {
    private String rspCode = "";
    private String rspMsg = "";
    private String header = "";
    private Object body;

    public String getRspCode() {
        return rspCode;
    }

    public void setRspCode(String rspCode) {
        this.rspCode = rspCode;
    }

    public String getRspMsg() {
        return rspMsg;
    }

    public void setRspMsg(String rspMsg) {
        this.rspMsg = rspMsg;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
