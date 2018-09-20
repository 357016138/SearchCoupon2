package com.search.coupon.agent.bean;

/**
 * Created by Administrator on 2018/4/1.
 *
 * 查询用户微币的实体类
 */

public class CoinBean {

    /**
     * "id":2,"userId":2,"vcoin":9999999999999,"updateDate":1522591299000,"codetype":0
     * */
    private String id;
    private String userId;
    private String vcoin;
    private String updateDate;
    private String codetype;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVcoin() {
        return vcoin;
    }

    public void setVcoin(String vcoin) {
        this.vcoin = vcoin;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getCodetype() {
        return codetype;
    }

    public void setCodetype(String codetype) {
        this.codetype = codetype;
    }
}
