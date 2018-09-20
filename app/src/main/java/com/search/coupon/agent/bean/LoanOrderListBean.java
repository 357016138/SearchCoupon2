package com.search.coupon.agent.bean;

import java.util.List;

/**
 * Created by jyg on 2018/3/12.
 */

public class LoanOrderListBean {

        /**
         * busiCode : HLAB25
         * curPage : 1
         * frontTransNo : 20180313162225210
         * interfaceNo : 5025
         * loanList : [{"borrowerName":"滕南珍","creditAmount":50,"declarationDate":"2018-03-13","loanState":"01","orderNum":"BMD257886561957588567","period":"3","productName":"一路e贷"},{"borrowerName":"毕韩韩","creditAmount":50,"declarationDate":"2018-03-12","loanState":"01","orderNum":"BMD251126642844852673","period":"3","productName":"一路e贷"},{"borrowerName":"柳仁涛","creditAmount":50,"declarationDate":"2018-03-12","loanState":"01","orderNum":"BMD251018092767049350","period":"3","productName":"一路e贷"},{"borrowerName":"柳仁涛","creditAmount":50,"declarationDate":"2018-03-12","loanState":"01","orderNum":"BMD251003633154451889","period":"3","productName":"一路e贷"}]
         * pageSize : 15
         * retCode : 0000
         * retMsg : 查询成功
         * retTime : 2018-03-13 16:22:25
         * serverTransNo : 5025259590931339317859
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
             * borrowerName : 滕南珍
             * creditAmount : 50.0
             * declarationDate : 2018-03-13
             * loanState : 01
             * orderNum : BMD257886561957588567
             * period : 3
             * productName : 一路e贷
             */

            private String borrowerName;
            private String creditAmount;
            private String declarationDate;
            private String loanState;
            private String orderNum;
            private String period;
            private String productName;
            private String interestRate;
            private String creditAmountAudit;
            private String interestRateAudit;
            private String periodAudit;

            public String getCreditAmountAudit() {
                return creditAmountAudit;
            }

            public void setCreditAmountAudit(String creditAmountAudit) {
                this.creditAmountAudit = creditAmountAudit;
            }

            public String getInterestRateAudit() {
                return interestRateAudit;
            }

            public void setInterestRateAudit(String interestRateAudit) {
                this.interestRateAudit = interestRateAudit;
            }

            public String getPeriodAudit() {
                return periodAudit;
            }

            public void setPeriodAudit(String periodAudit) {
                this.periodAudit = periodAudit;
            }

            public String getInterestRate() {
                return interestRate;
            }

            public void setInterestRate(String interestRate) {
                this.interestRate = interestRate;
            }



            public String getBorrowerName() {
                return borrowerName;
            }

            public void setBorrowerName(String borrowerName) {
                this.borrowerName = borrowerName;
            }

            public String getCreditAmount() {
                return creditAmount;
            }

            public void setCreditAmount(String creditAmount) {
                this.creditAmount = creditAmount;
            }

            public String getDeclarationDate() {
                return declarationDate;
            }

            public void setDeclarationDate(String declarationDate) {
                this.declarationDate = declarationDate;
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

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }
        }

}
