package com.search.coupon.agent.bean;

import java.util.List;

/**
 * Created by song on 2018/3/2 0002.
 */

public class OpenCityBean {

    private String busiCode;
    private String frontTransNo;
    private String interfaceNo;
    private String retCode;
    private String retMsg;
    private String retTime;
    private String serverTransNo;
    private List<CityBean> cityList;


    public String getBusiCode() {
        return busiCode;
    }

    public void setBusiCode(String busiCode) {
        this.busiCode = busiCode;
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

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
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

    public List<CityBean> getCityList() {
        return cityList;
    }

    public void setCityList(List<CityBean> cityList) {
        this.cityList = cityList;
    }

    public static class CityBean {
        /**
         * city : 城市名
         * cityCode  城市code
         */
        private String city;
        private String cityCode;

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

        @Override
        public String toString() {
            return "ProvinceBean{" +
                    "city='" + city + '\'' +
                    ", cityCode='" + cityCode + '\'' +
                    '}';
        }
    }



}
