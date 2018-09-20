package com.search.coupon.agent.bean;

import java.util.List;

/**
 * Created by jyg on 2018/3/20.
 */

public class Lo {

        /**
         * busiCode : HLAB25
         * curPage : 1
         * frontTransNo : 20180320181829803
         * interfaceNo : 5025
         * loanList : [{"borrowerName":"电话活动经费金发金发金发恒大华府话费","creditAmount":630,"creditAmountAudit":620,"declarationDate":"2018-03-20","interestRate":9.7,"interestRateAudit":7.8,"loanState":"09","orderNum":"BMD320488142383004577","period":"100","periodAudit":"10","productName":"中鑫达房抵贷"},{"borrowerName":"话费家福建福建大姐姐大","creditAmount":450,"creditAmountAudit":440,"declarationDate":"2018-03-20","interestRate":7.4,"interestRateAudit":6.7,"loanState":"09","orderNum":"BMD320431604471257740","period":"90","periodAudit":"10","productName":"中鑫达房抵贷"},{"borrowerName":"恒大华府华东交大","creditAmount":700,"creditAmountAudit":690,"declarationDate":"2018-03-20","interestRate":6.7,"interestRateAudit":4.5,"loanState":"09","orderNum":"BMD319690118642673764","period":"60","periodAudit":"9","productName":"晋诚贷2号"},{"borrowerName":"回到家经典款","creditAmount":480,"creditAmountAudit":470,"declarationDate":"2018-03-20","interestRate":2.79,"interestRateAudit":7.1,"loanState":"09","orderNum":"BMD319613459609321760","period":"60","periodAudit":"10","productName":"江苏银行信用贷"}]
         * pageSize : 15
         * retCode : 0000
         * retMsg : 查询成功
         * retTime : 2018-03-20 18:18:29
         * serverTransNo : 5025320768297391821723
         * totalPages : 1
         * totalRows : 4
         */

        private String busiCode;
        private int curPage;
        private String frontTransNo;
        private String interfaceNo;
        private int pageSize;
        private String retCode;
        private String retMsg;
        private String retTime;
        private String serverTransNo;
        private int totalPages;
        private int totalRows;
        private List<LoanListBean> loanList;

        public String getBusiCode() {
            return busiCode;
        }

        public void setBusiCode(String busiCode) {
            this.busiCode = busiCode;
        }

        public int getCurPage() {
            return curPage;
        }

        public void setCurPage(int curPage) {
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

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
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

        public int getTotalRows() {
            return totalRows;
        }

        public void setTotalRows(int totalRows) {
            this.totalRows = totalRows;
        }

        public List<LoanListBean> getLoanList() {
            return loanList;
        }

        public void setLoanList(List<LoanListBean> loanList) {
            this.loanList = loanList;
        }

        public static class LoanListBean {
            /**
             * borrowerName : 电话活动经费金发金发金发恒大华府话费
             * creditAmount : 630.0
             * creditAmountAudit : 620.0
             * declarationDate : 2018-03-20
             * interestRate : 9.7
             * interestRateAudit : 7.8
             * loanState : 09
             * orderNum : BMD320488142383004577
             * period : 100
             * periodAudit : 10
             * productName : 中鑫达房抵贷
             */

            private String borrowerName;
            private double creditAmount;
            private double creditAmountAudit;
            private String declarationDate;
            private double interestRate;
            private double interestRateAudit;
            private String loanState;
            private String orderNum;
            private String period;
            private String periodAudit;
            private String productName;

            public String getBorrowerName() {
                return borrowerName;
            }

            public void setBorrowerName(String borrowerName) {
                this.borrowerName = borrowerName;
            }

            public double getCreditAmount() {
                return creditAmount;
            }

            public void setCreditAmount(double creditAmount) {
                this.creditAmount = creditAmount;
            }

            public double getCreditAmountAudit() {
                return creditAmountAudit;
            }

            public void setCreditAmountAudit(double creditAmountAudit) {
                this.creditAmountAudit = creditAmountAudit;
            }

            public String getDeclarationDate() {
                return declarationDate;
            }

            public void setDeclarationDate(String declarationDate) {
                this.declarationDate = declarationDate;
            }

            public double getInterestRate() {
                return interestRate;
            }

            public void setInterestRate(double interestRate) {
                this.interestRate = interestRate;
            }

            public double getInterestRateAudit() {
                return interestRateAudit;
            }

            public void setInterestRateAudit(double interestRateAudit) {
                this.interestRateAudit = interestRateAudit;
            }

            public String getLoanState() {
                return loanState;
            }

            public void setLoanState(String loanState) {
                this.loanState = loanState;
            }

            public String getOrderNum() {
                return orderNum;
            }

            public void setOrderNum(String orderNum) {
                this.orderNum = orderNum;
            }

            public String getPeriod() {
                return period;
            }

            public void setPeriod(String period) {
                this.period = period;
            }

            public String getPeriodAudit() {
                return periodAudit;
            }

            public void setPeriodAudit(String periodAudit) {
                this.periodAudit = periodAudit;
            }

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }
        }
    }

