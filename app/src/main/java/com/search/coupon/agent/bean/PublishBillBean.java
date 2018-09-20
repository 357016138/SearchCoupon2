package com.search.coupon.agent.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by song on 2018/3/7 0007.
 */

public class PublishBillBean implements Serializable {

/**
 * {"orderId":"dd1e339313d34141932f596d4f9f94cc",
 * "title":"背链家",
 * "description":"急用吗兔兔",
 * "imageUrl":"http://p5bahoihf.bkt.clouddn.com/Fo2mgV-OhwYqMqAPhA4xvUk6PjLh",
 * "updateDate":1523287822000,
 * "codeType":2,
 * "orderType":1}
 * */

    private String orderId;
    private String title;
    private String description;
    private String imageUrl;
    private long updateDate;
    private String codeType;
    private String orderType;




    //过时的 以后可以干掉
    private String busiCode;
    private String curPage;
    private String frontTransNo;
    private String interfaceNo;
    private String pageSize;
    private String retCode;
    private String retMsg;
    private String retTime;
    private String serverTransNo;
    private int totalPages;
    private String totalRows;
    private List<InquiryList> inquiryList;


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getBusiCode() {
        return busiCode;
    }

    public void setBusiCode(String busiCode) {
        this.busiCode = busiCode;
    }

    public String getCurPage() {
        return curPage;
    }

    public void setCurPage(String curPage) {
        this.curPage = curPage;
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

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
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

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public String getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(String totalRows) {
        this.totalRows = totalRows;
    }

    public List<InquiryList> getInquiryList() {
        return inquiryList;
    }

    public void setInquiryList(List<InquiryList> inquiryList) {
        this.inquiryList = inquiryList;
    }

    public static class InquiryList implements Serializable {

        private String estateKeyword;    //小区名称
        private String housingValuation; //房屋估值
        private String inquiryCode;    //询价编码
        private String inquiryStatus;  //询价状态
        private String inquiryTime;   //询价日期
        private String inquiryType;  //询价类型
        private String isRecProduct;  //是否推荐产品
        private String city;          //城市
        private String cityCode;        //城市码
        private String buildingName;     //楼栋号
        private String householdName;     //房间号
        private String remark;           //备注

        public String getEstateKeyword() {
            return estateKeyword;
        }

        public void setEstateKeyword(String estateKeyword) {
            this.estateKeyword = estateKeyword;
        }

        public String getHousingValuation() {
            return housingValuation;
        }

        public void setHousingValuation(String housingValuation) {
            this.housingValuation = housingValuation;
        }

        public String getInquiryCode() {
            return inquiryCode;
        }

        public void setInquiryCode(String inquiryCode) {
            this.inquiryCode = inquiryCode;
        }

        public String getInquiryStatus() {
            return inquiryStatus;
        }

        public void setInquiryStatus(String inquiryStatus) {
            this.inquiryStatus = inquiryStatus;
        }

        public String getInquiryTime() {
            return inquiryTime;
        }

        public void setInquiryTime(String inquiryTime) {
            this.inquiryTime = inquiryTime;
        }

        public String getInquiryType() {
            return inquiryType;
        }

        public void setInquiryType(String inquiryType) {
            this.inquiryType = inquiryType;
        }

        public String getIsRecProduct() {
            return isRecProduct;
        }

        public void setIsRecProduct(String isRecProduct) {
            this.isRecProduct = isRecProduct;
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

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        @Override
        public String toString() {
            return "InquiryList{" +
                    "estateKeyword='" + estateKeyword + '\'' +
                    ", housingValuation='" + housingValuation + '\'' +
                    ", inquiryCode='" + inquiryCode + '\'' +
                    ", inquiryStatus='" + inquiryStatus + '\'' +
                    ", inquiryTime='" + inquiryTime + '\'' +
                    ", inquiryType='" + inquiryType + '\'' +
                    ", isRecProduct='" + isRecProduct + '\'' +
                    '}';
        }
    }

}
