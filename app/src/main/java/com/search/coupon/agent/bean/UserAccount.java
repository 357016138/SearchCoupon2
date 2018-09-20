package com.search.coupon.agent.bean;

/**
 * Created by RickBerg on 2018/3/12 0012.
 *
 */

public class UserAccount {
    private double accountBalance;
    private double amountToReturn;
    private double forzenAmount;
    private double totalReturnFee;

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public double getAmountToReturn() {
        return amountToReturn;
    }

    public void setAmountToReturn(double amountToReturn) {
        this.amountToReturn = amountToReturn;
    }

    public double getForzenAmount() {
        return forzenAmount;
    }

    public void setForzenAmount(double forzenAmount) {
        this.forzenAmount = forzenAmount;
    }

    public double getTotalReturnFee() {
        return totalReturnFee;
    }

    public void setTotalReturnFee(double totalReturnFee) {
        this.totalReturnFee = totalReturnFee;
    }
}
