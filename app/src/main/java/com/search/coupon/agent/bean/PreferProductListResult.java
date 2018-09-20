package com.search.coupon.agent.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author baipeng
 * @Title PreferProductListResult
 * @Date 2018/3/7 19:50
 * @Description PreferProductListResult.
 */
public class PreferProductListResult implements Serializable {
    private String curPage;
    private String pageSize;
    private String totalPages;
    private String totalRows;
    private List<PreferProductListResult.ProductList> productList;

    public String getCurPage() {
        return curPage;
    }

    public void setCurPage(String curPage) {
        this.curPage = curPage;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(String totalPages) {
        this.totalPages = totalPages;
    }

    public String getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(String totalRows) {
        this.totalRows = totalRows;
    }

    public List<PreferProductListResult.ProductList> getProductList() {
        return productList;
    }

    public void setProductList(List<PreferProductListResult.ProductList> inquiryList) {
        this.productList = inquiryList;
    }

    public static class ProductList implements Serializable {
        private String productCode;
        private String productName;
        private String creditAmount;
        private String minRate;

        public String getMinRate() {
            return minRate;
        }

        public void setMinRate(String minRate) {
            this.minRate = minRate;
        }

        public String getMaxRate() {
            return maxRate;
        }

        public void setMaxRate(String maxRate) {
            this.maxRate = maxRate;
        }

        private String maxRate;
        private String period;
        private String productTag;
        private String loanPeriod;
        private String version;
        private String housingValuation;

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getCreditAmount() {
            return creditAmount;
        }

        public void setCreditAmount(String creditAmount) {
            this.creditAmount = creditAmount;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public String getProductTag() {
            return productTag;
        }

        public void setProductTag(String productTag) {
            this.productTag = productTag;
        }

        public String getLoanPeriod() {
            return loanPeriod;
        }

        public void setLoanPeriod(String loanPeriod) {
            this.loanPeriod = loanPeriod;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getHousingValuation() {
            return housingValuation;
        }

        public void setHousingValuation(String housingValuation) {
            this.housingValuation = housingValuation;
        }
    }
}
