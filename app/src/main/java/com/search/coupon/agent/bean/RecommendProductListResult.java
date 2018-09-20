package com.search.coupon.agent.bean;

import java.util.List;

/**
 * @author baipeng
 * @Title RecommendProductListResult
 * @Date 2018/3/3 14:50
 * @Description RecommendProductListResult.
 */
public class RecommendProductListResult {
    public List<ProductListBean> getIntoInfoUploadList() {
        return productList;
    }

    public void setIntoInfoUploadList(List<ProductListBean> productList) {
        this.productList = productList;
    }

    private List<ProductListBean> productList;

    public static class ProductListBean {
        /**
         * productCode : PTF151100109
         * productName : 超级抵押贷
         * maxCreditAmount/minCreditAmount : 200.00万元
         * maxInterestRate/minInterestRate  : 10.0-11.0
         * maxPeriod/minPeriod : 6-24月
         * productTag  : 利率低
         * loanPeriod : 48小时放款
         */

        private String productCode;
        private String productName;
        private String minCreditAmount;
        private String minInterestRate;
        private String minPeriod;
        private String maxCreditAmount;
        private String maxInterestRate;
        private String maxPeriod;
        private String productTag;
        private String loanPeriod;
        private boolean isCheck;

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }

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

        public String getMinCreditAmount() {
            return minCreditAmount;
        }

        public void setMinCreditAmount(String minCreditAmount) {
            this.minCreditAmount = minCreditAmount;
        }

        public String getMinInterestRate() {
            return minInterestRate;
        }

        public void setMinInterestRate(String minInterestRate) {
            this.minInterestRate = minInterestRate;
        }

        public String getMinPeriod() {
            return minPeriod;
        }

        public void setMinPeriod(String minPeriod) {
            this.minPeriod = minPeriod;
        }

        public String getMaxCreditAmount() {
            return maxCreditAmount;
        }

        public void setMaxCreditAmount(String maxCreditAmount) {
            this.maxCreditAmount = maxCreditAmount;
        }

        public String getMaxInterestRate() {
            return maxInterestRate;
        }

        public void setMaxInterestRate(String maxInterestRate) {
            this.maxInterestRate = maxInterestRate;
        }

        public String getMaxPeriod() {
            return maxPeriod;
        }

        public void setMaxPeriod(String maxPeriod) {
            this.maxPeriod = maxPeriod;
        }
    }
}
