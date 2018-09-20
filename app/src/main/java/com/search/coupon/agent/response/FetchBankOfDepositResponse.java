package com.search.coupon.agent.response;

import com.google.gson.annotations.SerializedName;

import com.search.coupon.agent.bean.BankOfDeposit;

import java.util.List;

/**
 * Created by RickBerg on 2018/3/5 0005.
 */

public class FetchBankOfDepositResponse {

    @SerializedName("appTBankDTOList")
    private List<BankOfDeposit> list;

    public List<BankOfDeposit> getList() {
        return list;
    }

    public void setList(List<BankOfDeposit> mList) {
        this.list = mList;
    }
}
