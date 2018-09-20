package com.search.coupon.agent.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.search.coupon.agent.R;
import com.search.coupon.agent.bean.PublishBillBean;
import com.search.coupon.agent.listener.OperateListener;
import com.search.coupon.agent.utils.DateUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/4/19.
 * 发布订单列表的适配器
 *
 */

public class PublishBillAdapter extends RecyclerView.Adapter {

    private Context context;
    private OperateListener listener;
    private List<PublishBillBean> list;
    private int flag = 0;

    public PublishBillAdapter(Context context,int flag) {
        this.context = context;
        this.flag = flag;
    }

    public void setData(List<PublishBillBean> list) {
        this.list = list;
    }

    public List<PublishBillBean> getData() {
        return list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_public_bill, parent, false);
        PublishBillAdapter.MyViewHolder myViewHolder = new PublishBillAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PublishBillAdapter.MyViewHolder myViewHolder = (PublishBillAdapter.MyViewHolder) holder;
        PublishBillBean publishBillBean = list.get(position);
        if (publishBillBean != null) {

            //数据绑定
            String coverImage = publishBillBean.getImageUrl();               //图片链接
            String title = publishBillBean.getTitle();                      //标题
            long updateDate = publishBillBean.getUpdateDate();             //时间
            String codeType = publishBillBean.getCodeType();               //状态  待支付，审核中，审核完成，审核失败

            Glide.with(context).load(coverImage).into(myViewHolder.iv_cover);
            myViewHolder.tv_title.setText(title);
            myViewHolder.tv_time.setText( DateUtils.formatDate(updateDate));

            if ("0".equals(codeType)){
                myViewHolder.tv_tags.setText("待支付");
            }else if ("1".equals(codeType)){
                myViewHolder.tv_tags.setText("审核中");
            }else if ("2".equals(codeType)){
                myViewHolder.tv_tags.setText("审核通过");
            }else if ("3".equals(codeType)){
                myViewHolder.tv_tags.setText("审核不通过");
            }else{
                myViewHolder.tv_tags.setText(codeType);
            }

            myViewHolder.rl_price_bill_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.operate("1", publishBillBean);
                }
            });


            myViewHolder.tv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.operate("2", publishBillBean);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return list == null? 0 : list.size();
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {


        private ImageView iv_cover;
        private TextView tv_title;
        private TextView tv_time;
        private TextView tv_tags;
        private TextView tv_edit;
        private RelativeLayout rl_price_bill_item;

        public MyViewHolder(View itemView) {
            super(itemView);
            rl_price_bill_item = itemView.findViewById(R.id.rl_price_bill_item);
            iv_cover = itemView.findViewById(R.id.iv_cover);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_tags = itemView.findViewById(R.id.tv_tags);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_edit = itemView.findViewById(R.id.tv_edit);



        }

    }

    public void setOperateListener(OperateListener listener) {
        this.listener = listener;
    }
}
