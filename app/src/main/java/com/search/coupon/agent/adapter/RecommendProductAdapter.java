package com.search.coupon.agent.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.search.coupon.agent.R;
import com.search.coupon.agent.bean.RecommendProductListResult;
import com.search.coupon.agent.common.Constants;
import com.search.coupon.agent.listener.OperatePosListener;
import com.search.coupon.agent.utils.SpanUtils;
import com.search.coupon.agent.utils.StringUtils;

import java.util.ArrayList;

/**
 * @author baipeng
 * @Title RecommendProductAdapter
 * @Date 2018/2/26 16:32
 * @Description RecommendProductAdapter.
 */
public class RecommendProductAdapter extends RecyclerView.Adapter<RecommendProductAdapter.MyViewHolder> {

    private Context context;
    private static OperatePosListener listener;
    private ArrayList<RecommendProductListResult.ProductListBean> mProductList = new ArrayList<>();

    public RecommendProductAdapter(Context context, ArrayList<RecommendProductListResult.ProductListBean> dataList) {
        this.context = context;
        this.mProductList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_recommend_product, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        if (viewHolder == null) {
            return;
        }

        RecommendProductListResult.ProductListBean item = mProductList.get(position);
        viewHolder.item_product_name.setText(StringUtils.emptyDispose(item.getProductName()));
        String productTag = item.getProductTag();
        if(StringUtils.isEmpty(productTag)) {
            viewHolder.item_product_tag1.setVisibility(View.GONE);
            viewHolder.item_product_tag2.setVisibility(View.GONE);
        } else if(productTag.contains(",")) {
            String[] products = productTag.split(",");
            viewHolder.item_product_tag1.setText(Constants.getProductTag(products[0]));
            viewHolder.item_product_tag2.setText(Constants.getProductTag(products[1]));
            viewHolder.item_product_tag1.setVisibility(View.VISIBLE);
            viewHolder.item_product_tag2.setVisibility(View.VISIBLE);
        } else {
            viewHolder.item_product_tag2.setText(Constants.getProductTag(productTag));
            viewHolder.item_product_tag1.setVisibility(View.GONE);
            viewHolder.item_product_tag2.setVisibility(View.VISIBLE);
        }
        viewHolder.item_loan_amount.setText(SpanUtils.getSizeSpan(context, null, StringUtils.emptyDispose(item.getMaxCreditAmount()) + "万元", "万元", 12));
        viewHolder.item_loan_period.setText(StringUtils.emptyDispose(item.getLoanPeriod()));
        viewHolder.item_loan_rate.setText("月利率" + StringUtils.emptyDispose(item.getMinInterestRate()) + "-" + StringUtils.emptyDispose(item.getMaxInterestRate()) + "%");
        viewHolder.item_loan_cycle.setText("周期" + StringUtils.emptyDispose(item.getMinPeriod()) + "-" + StringUtils.emptyDispose(item.getMaxPeriod()) + "月");
        if(item.isCheck())
            viewHolder.item_checkbox.setImageResource(R.drawable.ic_check);
        else
            viewHolder.item_checkbox.setImageResource(R.drawable.ic_uncheck);
        viewHolder.rl_price_bill_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.operatePos("1", position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rl_price_bill_item;
        ImageView item_checkbox;
        TextView item_product_name;
        TextView item_product_tag1;
        TextView item_product_tag2;
        TextView item_loan_amount;
        TextView item_loan_period;
        TextView item_loan_rate;
        TextView item_loan_cycle;

        public MyViewHolder(View itemView) {
            super(itemView);
            rl_price_bill_item = itemView.findViewById(R.id.rl_price_bill_item);
            item_product_name = itemView.findViewById(R.id.item_product_name);
            item_product_tag1 = itemView.findViewById(R.id.item_product_tag1);
            item_product_tag2 = itemView.findViewById(R.id.item_product_tag2);
            item_loan_amount = itemView.findViewById(R.id.item_loan_amount);
            item_loan_period = itemView.findViewById(R.id.item_loan_period);
            item_loan_rate = itemView.findViewById(R.id.item_loan_rate);
            item_loan_cycle = itemView.findViewById(R.id.item_loan_cycle);
            item_checkbox = itemView.findViewById(R.id.item_checkbox);
        }
    }

    public void setOperateListener(OperatePosListener listener) {
        this.listener = listener;
    }
}
