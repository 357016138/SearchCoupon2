package com.search.coupon.agent.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.search.coupon.agent.R;
import com.search.coupon.agent.bean.LoanOrder;

/**
 * Created by RickBerg on 2018/2/26 0026.
 *
 */

public class LoanOrderItemViewHolder extends RecyclerView.ViewHolder {

    private TextView mTvProductName;
    private TextView mTvLoanAmount;
    private TextView mTvLoanPeriod;
    private TextView mTvRate;
    private TextView mTvLoanUser;
    private TextView mTvDate;

    public LoanOrderItemViewHolder(View itemView) {
        super(itemView);
        mTvProductName = itemView.findViewById(R.id.tv_product_name);
        mTvLoanAmount = itemView.findViewById(R.id.tv_loan_amount);
        mTvLoanPeriod = itemView.findViewById(R.id.tv_loan_period);
        mTvRate = itemView.findViewById(R.id.tv_interest_rate);
        mTvLoanUser = itemView.findViewById(R.id.tv_loan_user);
        mTvDate = itemView.findViewById(R.id.tv_order_date);
    }

    public void bindData(LoanOrder loanOrder){

    }
}
