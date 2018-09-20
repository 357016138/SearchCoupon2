package com.search.coupon.agent.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.search.coupon.agent.R;
import com.search.coupon.agent.bean.PreferProductListResult;
import com.search.coupon.agent.common.Constants;
import com.search.coupon.agent.listener.OperateListener;
import com.search.coupon.agent.utils.SpanUtils;
import com.search.coupon.agent.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author baipeng
 * @Title PreferenceProductAdapter
 * @Date 2018/2/28 17:15
 * @Description PreferenceProductAdapter.
 */
public class
PreferenceProductAdapter extends RecyclerView.Adapter<PreferenceProductAdapter.MyViewHolder> {

    private Context context;
    private static OperateListener listener;
    private List<PreferProductListResult.ProductList> mProductList = new ArrayList<>();

    public PreferenceProductAdapter(Context context, List<PreferProductListResult.ProductList> dataList) {

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

        PreferProductListResult.ProductList item = mProductList.get(position);
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
        viewHolder.item_loan_amount.setText(SpanUtils.getSizeSpan(context, null, StringUtils.emptyDispose(item.getCreditAmount()) + "万元", "万元", 12));
        viewHolder.item_loan_period.setText(StringUtils.emptyDispose(item.getLoanPeriod()));
        viewHolder.item_loan_rate.setText("月利率" + StringUtils.emptyDispose(item.getMinRate()) + "-" + StringUtils.emptyDispose(item.getMaxRate()) + "%");
        String period = item.getPeriod();
        if(period.contains(",")) {
            String[] products = period.split(",");
            viewHolder.item_loan_cycle.setText("周期" + StringUtils.emptyDispose(products[0]) + "-" + StringUtils.emptyDispose(products[products.length - 1]) + "月");
        } else {
            viewHolder.item_loan_cycle.setText("周期" + StringUtils.emptyDispose(period) + "月");
        }
        viewHolder.item_checkbox.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView item_product_name;
        TextView item_product_tag1;
        TextView item_product_tag2;
        TextView item_loan_amount;
        TextView item_loan_period;
        TextView item_loan_rate;
        TextView item_loan_cycle;
        ImageView item_checkbox;

        public MyViewHolder(View itemView) {
            super(itemView);
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

    public void setOperateListener(OperateListener listener) {
        this.listener = listener;
    }
}
