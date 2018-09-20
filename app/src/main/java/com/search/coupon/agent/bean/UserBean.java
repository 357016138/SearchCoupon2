package com.search.coupon.agent.bean;

/**
 * 用户信息
 * Created by fan on 2018/1/22.
 */
public class UserBean {

//"busiCode":"HLAB02","city":"北京市(市辖区)","cityCode":"110100","frontTransNo":"20180303142820853",
// "interfaceNo":"5002","inviter":"","phone":"15311436473","retCode":"0000","retMsg":"登录成功",
// "retTime":"2018-03-03 14:28:20","serverTransNo":"50021725065002939583","userId":"9"

    /**
     * busiCode :
     * city :
     * cityCode :
     * frontTransNo :
     * interfaceNo :
     * inviter :
     * phone :
     * retTime :
     * serverTransNo:
     * userId:
     */
//    json:{"data":{"id":2,"phoneNumber":15311436473,"username":null,"password":null,"codeType":0,"updateDate":null},"success":true,"message":"登录成功!"}
    private String busiCode;
    private String city;
    private String cityCode;
    private String frontTransNo;
    private String interfaceNo;
    private String inviter;
    private String phone;
    private String retTime;
    private String serverTransNo;
    private String userId;
    private String isPayPass;

    private String id;
    private String phoneNumber;
    private String username;
    private String password;
    private String codeType;
    private String updateDate;

    public String getBusiCode() {
        return busiCode;
    }

    public void setBusiCode(String busiCode) {
        this.busiCode = busiCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getFrontTransNo() {
        return frontTransNo;
    }

    public void setFrontTransNo(String frontTransNo) {
        this.frontTransNo = frontTransNo;
    }

    public String getInterfaceNo() {
        return interfaceNo;
    }

    public void setInterfaceNo(String interfaceNo) {
        this.interfaceNo = interfaceNo;
    }

    public String getInviter() {
        return inviter;
    }

    public void setInviter(String inviter) {
        this.inviter = inviter;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRetTime() {
        return retTime;
    }

    public void setRetTime(String retTime) {
        this.retTime = retTime;
    }

    public String getServerTransNo() {
        return serverTransNo;
    }

    public void setServerTransNo(String serverTransNo) {
        this.serverTransNo = serverTransNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIsPayPass() {
        return isPayPass;
    }

    public void setIsPayPass(String isPayPass) {
        this.isPayPass = isPayPass;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}
