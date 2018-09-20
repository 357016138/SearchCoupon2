package com.search.coupon.agent.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.search.coupon.agent.R;
import com.search.coupon.agent.bean.LoanOrderListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by RickBerg on 2018/2/26 0026.
 */

public class LoanOrderListAdapter extends RecyclerView.Adapter<LoanOrderListAdapter.MyViewHolder> {



    private Context mContext;
    private List<LoanOrderListBean.LoanListBean> mList;
    private View view;

    public LoanOrderListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<LoanOrderListBean.LoanListBean> mList) {
        this.mList = mList;
    }

    public List<LoanOrderListBean.LoanListBean> getData() {
        return mList;
    }

    //点击事件
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    //set
    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.loan_order_item, null);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tvProductName.setText(mList.get(position).getProductName());
        holder.tvLoanAmount.setText(mList.get(position).getCreditAmount() + "万元");
        holder.tvInterestRate.setText(mList.get(position).getInterestRate()+"%");
        holder.tvLoanPeriod.setText(mList.get(position).getPeriod() + "个月");
        holder.tvLoanUser.setText(mList.get(position).getBorrowerName());
        holder.tvOrderDate.setText(mList.get(position).getDeclarationDate());
        if ("01".equals(mList.get(position).getLoanState())) {
            holder.tvOrderState.setText("待下户");
            holder.tvLoanAmountLabel.setText("申请金额");
            holder.tvInterestRateLabel.setText("申请利率");
            holder.tvLoanPeriodLabel.setText("申请周期");
            holder.tvOrderState.setTextColor(mContext.getResources().getColor(R.color.color_3889F));
            holder.tvOrderState.setBackground(mContext.getResources().getDrawable(R.drawable.textview_border_blues));
        } else if ("02".equals(mList.get(position).getLoanState())) {
            holder.tvOrderState.setText("下户中");
            holder.tvLoanAmountLabel.setText("申请金额");
            holder.tvInterestRateLabel.setText("申请利率");
            holder.tvLoanPeriodLabel.setText("申请周期");
            holder.tvOrderState.setTextColor(mContext.getResources().getColor(R.color.color_3889F));
            holder.tvOrderState.setBackground(mContext.getResources().getDrawable(R.drawable.textview_border_blues));
        } else if ("03".equals(mList.get(position).getLoanState())) {
            holder.tvOrderState.setText("下户不通过");
            holder.tvLoanAmountLabel.setText("申请金额");
            holder.tvInterestRateLabel.setText("申请利率");
            holder.tvLoanPeriodLabel.setText("申请周期");
            holder.tvOrderState.setTextColor(mContext.getResources().getColor(R.color.color_959595));
            holder.tvOrderState.setBackground(mContext.getResources().getDrawable(R.drawable.textview_border_gray));
        } else if ("04".equals(mList.get(position).getLoanState())) {
            holder.tvOrderState.setText("审批中");
            holder.tvLoanAmountLabel.setText("申请金额");
            holder.tvInterestRateLabel.setText("申请利率");
            holder.tvLoanPeriodLabel.setText("申请周期");
            holder.tvOrderState.setTextColor(mContext.getResources().getColor(R.color.color_3889F));
            holder.tvOrderState.setBackground(mContext.getResources().getDrawable(R.drawable.textview_border_blues));
        }else if ("05".equals(mList.get(position).getLoanState())) {
            holder.tvOrderState.setText("审批拒绝");
            holder.tvLoanAmountLabel.setText("申请金额");
            holder.tvInterestRateLabel.setText("申请利率");
            holder.tvLoanPeriodLabel.setText("申请周期");
            holder.tvOrderState.setTextColor(mContext.getResources().getColor(R.color.color_959595));
            holder.tvOrderState.setBackground(mContext.getResources().getDrawable(R.drawable.textview_border_gray));
        }else if ("06".equals(mList.get(position).getLoanState())) {
            holder.tvOrderState.setText("签约中");
            holder.tvLoanAmountLabel.setText("申请金额");
            holder.tvInterestRateLabel.setText("申请利率");
            holder.tvLoanPeriodLabel.setText("申请周期");
            holder.tvOrderState.setTextColor(mContext.getResources().getColor(R.color.color_FF924C));
            holder.tvOrderState.setBackground(mContext.getResources().getDrawable(R.drawable.textview_border_orange));
        }else if ("07".equals(mList.get(position).getLoanState())) {
            holder.tvOrderState.setText("签约拒绝");
            holder.tvLoanAmountLabel.setText("申请金额");
            holder.tvInterestRateLabel.setText("申请利率");
            holder.tvLoanPeriodLabel.setText("申请周期");
            holder.tvOrderState.setTextColor(mContext.getResources().getColor(R.color.color_959595));
            holder.tvOrderState.setBackground(mContext.getResources().getDrawable(R.drawable.textview_border_gray));
        }else if ("08".equals(mList.get(position).getLoanState())) {
            holder.tvOrderState.setText("放款中");
            holder.tvLoanAmountLabel.setText("申请金额");
            holder.tvInterestRateLabel.setText("申请利率");
            holder.tvLoanPeriodLabel.setText("申请周期");
            holder.tvOrderState.setTextColor(mContext.getResources().getColor(R.color.color_FF924C));
            holder.tvOrderState.setBackground(mContext.getResources().getDrawable(R.drawable.textview_border_orange));
        }else if ("09".equals(mList.get(position).getLoanState())) {
            holder.tvOrderState.setText("已放款");
            holder.tvLoanAmountLabel.setText("审批金额");
            holder.tvInterestRateLabel.setText("审批利率");
            holder.tvLoanPeriodLabel.setText("审批周期");
            holder.tvLoanAmount.setText(mList.get(position).getCreditAmountAudit() + "万元");
            holder.tvInterestRate.setText(mList.get(position).getInterestRateAudit()+"%");
            holder.tvLoanPeriod.setText(mList.get(position).getPeriodAudit()+ "个月");
            holder.tvOrderState.setTextColor(mContext.getResources().getColor(R.color.color_24BD48));
            holder.tvOrderState.setBackground(mContext.getResources().getDrawable(R.drawable.textview_border_blue));
        }else if ("10".equals(mList.get(position).getLoanState())) {
            holder.tvOrderState.setText("放款拒绝");
            holder.tvLoanAmountLabel.setText("申请金额");
            holder.tvInterestRateLabel.setText("申请利率");
            holder.tvLoanPeriodLabel.setText("申请周期");
            holder.tvOrderState.setTextColor(mContext.getResources().getColor(R.color.color_959595));
            holder.tvOrderState.setBackground(mContext.getResources().getDrawable(R.drawable.textview_border_gray));
        }else if ("11".equals(mList.get(position).getLoanState())) {
            holder.tvOrderState.setText("客户放弃");
            holder.tvLoanAmountLabel.setText("申请金额");
            holder.tvInterestRateLabel.setText("申请利率");
            holder.tvLoanPeriodLabel.setText("申请周期");
            holder.tvOrderState.setTextColor(mContext.getResources().getColor(R.color.color_959595));
            holder.tvOrderState.setBackground(mContext.getResources().getDrawable(R.drawable.textview_border_gray));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getLayoutPosition();
                mOnItemClickLitener.onItemClick(holder.itemView, pos);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_product_name)
        TextView tvProductName;
        @BindView(R.id.tv_order_state)
        TextView tvOrderState;
        @BindView(R.id.tv_loan_amount)
        TextView tvLoanAmount;
        @BindView(R.id.tv_interest_rate)
        TextView tvInterestRate;
        @BindView(R.id.tv_loan_period)
        TextView tvLoanPeriod;
        @BindView(R.id.tv_loan_user)
        TextView tvLoanUser;
        @BindView(R.id.tv_order_date)
        TextView tvOrderDate;
        @BindView(R.id.tv_loan_amount_label)
        TextView tvLoanAmountLabel;
        @BindView(R.id.tv_interest_rate_label)
        TextView tvInterestRateLabel;
        @BindView(R.id.tv_loan_period_label)
        TextView tvLoanPeriodLabel;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
