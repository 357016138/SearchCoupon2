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
import com.search.coupon.agent.bean.SearchBean;
import com.search.coupon.agent.listener.OperateListener;

import java.util.List;

/**
 * Created by Administrator on 2018/4/7.
 */

public class SearchAdapter extends RecyclerView.Adapter {

    private Context context;
    private OperateListener listener;
    private List<SearchBean.ProductBean> list;
    private int flag = 0;

    public SearchAdapter(Context context,int flag) {
        this.context = context;
        this.flag = flag;
    }

    public void setData(List<SearchBean.ProductBean> list) {
        this.list = list;
    }

    public List<SearchBean.ProductBean> getData() {
        return list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_list, parent, false);
        SearchAdapter.MyViewHolder myViewHolder = new SearchAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SearchAdapter.MyViewHolder myViewHolder = (SearchAdapter.MyViewHolder) holder;
        SearchBean.ProductBean productBean = list.get(position);
        if (productBean != null) {
            //数据绑定
              String coverImage = productBean.getCoverImage();            //图片链接
              String groupName = productBean.getGroupName();              //标题
              String description = productBean.getDescription();         //描述
              String tags = productBean.getTags();                       //tag

              Glide.with(context).load(coverImage).into(myViewHolder.iv_cover);
              myViewHolder.tv_title.setText(groupName);
              myViewHolder.tv_des.setText(description);
              myViewHolder.tv_tags.setText(tags);

            myViewHolder.rl_price_bill_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.operate("1", productBean.getUniqueId());
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
        private TextView tv_des;
        private TextView tv_tags;
        private RelativeLayout rl_price_bill_item;

        public MyViewHolder(View itemView) {
            super(itemView);
              iv_cover = itemView.findViewById(R.id.iv_cover);
              tv_title = itemView.findViewById(R.id.tv_title);
              tv_des = itemView.findViewById(R.id.tv_des);
              tv_tags = itemView.findViewById(R.id.tv_tags);
              rl_price_bill_item = itemView.findViewById(R.id.rl_price_bill_item);


        }

    }

    public void setOperateListener(OperateListener listener) {
        this.listener = listener;
    }
}
