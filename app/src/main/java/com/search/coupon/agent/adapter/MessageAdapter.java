package com.search.coupon.agent.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.search.coupon.agent.R;
import com.search.coupon.agent.bean.MessageBean;
import com.search.coupon.agent.listener.OperateListener;
import com.search.coupon.agent.utils.DateUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/4/24.
 */

public class MessageAdapter extends RecyclerView.Adapter {

    private Context context;
    private OperateListener listener;
    private List<MessageBean> list;

    public MessageAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<MessageBean> list) {
        this.list = list;
    }

    public List<MessageBean> getData() {
        return list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message_list, parent, false);
        MessageAdapter.MyViewHolder myViewHolder = new MessageAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageAdapter.MyViewHolder myViewHolder = (MessageAdapter.MyViewHolder) holder;
        MessageBean messageBean = list.get(position);
        if (messageBean != null) {
            //数据绑定
            String context = messageBean.getReview_context();         //描述
            long time = messageBean.getCreate_time();               //time

            myViewHolder.tv_des.setText(transport(context));
            myViewHolder.tv_time.setText(DateUtils.formatDate(time));

        }
    }

    @Override
    public int getItemCount() {
        return list == null? 0 : list.size();
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_des;
        private TextView tv_time;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_des = itemView.findViewById(R.id.tv_des);
            tv_time = itemView.findViewById(R.id.tv_time);
        }
    }

    public void setOperateListener(OperateListener listener) {
        this.listener = listener;
    }


    /**
     *w文字排版不整齐的问题
     * */
    public String transport(String inputStr)
    {
        char arr[] = inputStr.toCharArray();
        for(int i=0;i<arr.length;i++)
        {
            if(arr[i] == ' ')
            {
                arr[i]='\u3000';
            }
            else if (arr[i] < '\177')
            {
                arr[i] = (char) (arr[i] + 65248);
            }

        }
        return new String(arr);
    }
}
