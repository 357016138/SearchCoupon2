package com.search.coupon.agent.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by song on 2018/3/7 0007.
 */

public class PriceBillDetailBean implements Serializable {

    private String inquiryCode;           //询价订单编号
    private String applyTime;             //申请时间
    private String cityCode;               //所在城市
    private String estateName;            //小区名称
    private String propertyType;          //房产种类
    private String buildingName;          //楼栋名称
    private String householdName;          //房号名称
    private String city;                  //城市名称
    private String housingValuation;      //房产估值
    private String maxCreditAmount;      //最大授信额度
    private String minCreditAmount;      //最小授信额度
    private String maxInterestRate;      //最大利率
    private String minInterestRate;      //最小利率
    private String maxPeriod;            //最大周期
    private String minPeriod;            //最小周期
    private String inquiryStatus;         //询价状态
    private String province;              //省
    private String area;                 //区
    private String reasonRemark;            //失败原因备注 失败时不为空


    private List<ProcessBean> processList;         //流程集合
    private List<PropertyCertBean> propertyCertUrl;         //房产证照片集合


    public String getInquiryCode() {
        return inquiryCode;
    }

    public void setInquiryCode(String inquiryCode) {
        this.inquiryCode = inquiryCode;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getEstateName() {
        return estateName;
    }

    public void setEstateName(String estateName) {
        this.estateName = estateName;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getHouseholdName() {
        return householdName;
    }

    public void setHouseholdName(String householdName) {
        this.householdName = householdName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHousingValuation() {
        return housingValuation;
    }

    public void setHousingValuation(String housingValuation) {
        this.housingValuation = housingValuation;
    }

    public String getMaxCreditAmount() {
        return maxCreditAmount;
    }

    public void setMaxCreditAmount(String maxCreditAmount) {
        this.maxCreditAmount = maxCreditAmount;
    }

    public String getMinCreditAmount() {
        return minCreditAmount;
    }

    public void setMinCreditAmount(String minCreditAmount) {
        this.minCreditAmount = minCreditAmount;
    }

    public String getMaxInterestRate() {
        return maxInterestRate;
    }

    public void setMaxInterestRate(String maxInterestRate) {
        this.maxInterestRate = maxInterestRate;
    }

    public String getMinInterestRate() {
        return minInterestRate;
    }

    public void setMinInterestRate(String minInterestRate) {
        this.minInterestRate = minInterestRate;
    }

    public String getMaxPeriod() {
        return maxPeriod;
    }

    public void setMaxPeriod(String maxPeriod) {
        this.maxPeriod = maxPeriod;
    }

    public String getMinPeriod() {
        return minPeriod;
    }

    public void setMinPeriod(String minPeriod) {
        this.minPeriod = minPeriod;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getReasonRemark() {
        return reasonRemark;
    }

    public void setReasonRemark(String reasonRemark) {
        this.reasonRemark = reasonRemark;
    }

    public String getInquiryStatus() {
        return inquiryStatus;
    }

    public void setInquiryStatus(String inquiryStatus) {
        this.inquiryStatus = inquiryStatus;
    }

    public List<ProcessBean> getProcessList() {
        return processList;
    }

    public void setProcessList(List<ProcessBean> processList) {
        this.processList = processList;
    }

    public List<PropertyCertBean> getPropertyCertUrl() {
        return propertyCertUrl;
    }

    public void setPropertyCertUrl(List<PropertyCertBean> propertyCertUrl) {
        this.propertyCertUrl = propertyCertUrl;
    }

    public class ProcessBean{

        private String process;                  //当前步骤
        private String processState;            //当前步骤状态
        private String dealTime;                //处理时间
        private String remark;                  //备注
        private String isRecProduct;           //是否推荐产品

        public String getProcess() {
            return process;
        }

        public void setProcess(String process) {
            this.process = process;
        }

        public String getProcessState() {
            return processState;
        }

        public void setProcessState(String processState) {
            this.processState = processState;
        }

        public String getDealTime() {
            return dealTime;
        }

        public void setDealTime(String dealTime) {
            this.dealTime = dealTime;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getIsRecProduct() {
            return isRecProduct;
        }

        public void setIsRecProduct(String isRecProduct) {
            this.isRecProduct = isRecProduct;
        }
    }


    public class PropertyCertBean{
        private String url;                 //照片链接

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}
