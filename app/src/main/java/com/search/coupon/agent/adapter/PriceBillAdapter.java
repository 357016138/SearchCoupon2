package com.search.coupon.agent.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.search.coupon.agent.R;
import com.search.coupon.agent.bean.PublishBillBean;
import com.search.coupon.agent.listener.OperateListener;
import com.search.coupon.agent.utils.SpanUtils;
import com.search.coupon.agent.utils.StringUtils;

import java.util.List;


/**
 * Created by song on 2018/2/25 0025.
 */

public class PriceBillAdapter extends RecyclerView.Adapter {

    private Context context;
    private OperateListener listener;
    private List<PublishBillBean.InquiryList> list;
    private int flag = 0;

    public PriceBillAdapter(Context context,int flag) {
        this.context = context;
        this.flag = flag;
    }

    public void setData(List<PublishBillBean.InquiryList> list) {
        this.list = list;
    }

    public List<PublishBillBean.InquiryList> getData() {
        return list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_price_bill, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        PublishBillBean.InquiryList inquiryListBean = list.get(position);
        if (inquiryListBean != null) {
            //数据绑定
            String inquiryStatus = inquiryListBean.getInquiryStatus();      //询价状态
            String estateKeyword = inquiryListBean.getEstateKeyword();      //小区名称
            String housingValuation = inquiryListBean.getHousingValuation();//房屋估值
            String inquiryTime = inquiryListBean.getInquiryTime();         //询价日期
            String isRecProduct = inquiryListBean.getIsRecProduct();      //是否推荐产品
            String city = inquiryListBean.getCity();                     //城市
            String buildingName = inquiryListBean.getBuildingName();      //楼栋号
            String householdName = inquiryListBean.getHouseholdName();      //房间号
            String remark = inquiryListBean.getRemark();                   //备注

            if ("1".equals(inquiryStatus)){           //询价中
                myViewHolder.tv_price_bill_item_title.setText("估值中");
                myViewHolder.tv_price_bill_item_status.setText("询价中");
                myViewHolder.tv_price_bill_item_status.setTextColor(context.getResources().getColor(R.color.color_FF924C));
                myViewHolder.tv_price_bill_item_status.setBackground(context.getResources().getDrawable(R.drawable.textview_border_orange));
                myViewHolder.tv_price_bill_item_des_3.setText(SpanUtils.getColorSpan(context, null, "预计3个工作日内出具评估结果", "3个工作日内", R.color.color_FF924C));
                myViewHolder.tv_price_bill_item_des_3.setVisibility(View.VISIBLE);
                myViewHolder.tv_price_bill_item_des_1.setVisibility(View.GONE);
                myViewHolder.tv_price_bill_item_des_2.setVisibility(View.GONE);
                myViewHolder.ll_price_bill_item_look_more.setVisibility(View.GONE);

            }else if ("2".equals(inquiryStatus)){    //询价完成
                myViewHolder.tv_price_bill_item_title.setText("房产估值");
                myViewHolder.tv_price_bill_item_status.setText("询价完成");
                myViewHolder.tv_price_bill_item_status.setTextColor(context.getResources().getColor(R.color.color_24BD48));
                myViewHolder.tv_price_bill_item_status.setBackground(context.getResources().getDrawable(R.drawable.textview_border_blue));
                myViewHolder.tv_price_bill_item_des_1.setText(SpanUtils.getSizeSpan(context,null,StringUtils.emptyDispose(housingValuation)+"万元","万元",12));
                myViewHolder.tv_price_bill_item_des_1.setVisibility(View.VISIBLE);
                myViewHolder.tv_price_bill_item_des_2.setVisibility(View.GONE);
                myViewHolder.tv_price_bill_item_des_3.setVisibility(View.GONE);
                myViewHolder.ll_price_bill_item_look_more.setVisibility(View.GONE);

                if (!StringUtils.isEmpty(isRecProduct)){
                    if ("1".equals(isRecProduct)){                 //优选产品
                        myViewHolder.ll_price_bill_item_look_more.setVisibility(View.VISIBLE);
                        myViewHolder.tv_price_bill_item_look_more.setText("查看优选产品");
                        myViewHolder.ll_price_bill_item_look_more.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                listener.operate("3",inquiryListBean);
                            }
                        });

                    }else if ("2".equals(isRecProduct)){             //推荐产品
                        myViewHolder.ll_price_bill_item_look_more.setVisibility(View.VISIBLE);
                        myViewHolder.tv_price_bill_item_look_more.setText("查看推荐产品");
                        myViewHolder.ll_price_bill_item_look_more.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                listener.operate("2",inquiryListBean);
                            }
                        });
                    }
                }
            }else if ("3".equals(inquiryStatus)){   //询价终止
                myViewHolder.tv_price_bill_item_title.setText("无法完成房产估值");
                myViewHolder.tv_price_bill_item_status.setText("询价终止");
                myViewHolder.tv_price_bill_item_status.setTextColor(context.getResources().getColor(R.color.color_959595));
                myViewHolder.tv_price_bill_item_status.setBackground(context.getResources().getDrawable(R.drawable.textview_border_gray));
                myViewHolder.tv_price_bill_item_des_2.setText(remark);
                myViewHolder.tv_price_bill_item_des_2.setVisibility(View.VISIBLE);
                myViewHolder.tv_price_bill_item_des_1.setVisibility(View.GONE);
                myViewHolder.tv_price_bill_item_des_3.setVisibility(View.GONE);
                myViewHolder.ll_price_bill_item_look_more.setVisibility(View.GONE);
            }



            if (city!=null&&buildingName!=null&&householdName!=null){
                myViewHolder.tv_price_bill_item_address.setText(city+estateKeyword+buildingName+householdName);
            }else{
                myViewHolder.tv_price_bill_item_address.setText(estateKeyword);
            }
            if (flag==1||flag==2||flag==3)
                myViewHolder.tv_price_bill_item_status.setVisibility(View.GONE);

            myViewHolder.tv_price_bill_item_time.setText(inquiryTime);
            myViewHolder.rl_price_bill_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.operate("1",inquiryListBean);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list == null? 0 : list.size();
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rl_price_bill_item;
        private LinearLayout ll_price_bill_item_look_more;
        private TextView tv_price_bill_item_look_more;
        private TextView tv_price_bill_item_title;
        private TextView tv_price_bill_item_status;
        private TextView tv_price_bill_item_des_1;
        private TextView tv_price_bill_item_des_2;
        private TextView tv_price_bill_item_des_3;
        private TextView tv_price_bill_item_address;
        private TextView tv_price_bill_item_time;

        public MyViewHolder(View itemView) {
            super(itemView);
            rl_price_bill_item = itemView.findViewById(R.id.rl_price_bill_item);
            ll_price_bill_item_look_more = itemView.findViewById(R.id.ll_price_bill_item_look_more);
            tv_price_bill_item_look_more = itemView.findViewById(R.id.tv_price_bill_item_look_more);
            tv_price_bill_item_title = itemView.findViewById(R.id.tv_price_bill_item_title);
            tv_price_bill_item_status = itemView.findViewById(R.id.tv_price_bill_item_status);
            tv_price_bill_item_des_1 = itemView.findViewById(R.id.tv_price_bill_item_des_1);
            tv_price_bill_item_des_2 = itemView.findViewById(R.id.tv_price_bill_item_des_2);
            tv_price_bill_item_des_3 = itemView.findViewById(R.id.tv_price_bill_item_des_3);
            tv_price_bill_item_address = itemView.findViewById(R.id.tv_price_bill_item_address);
            tv_price_bill_item_time = itemView.findViewById(R.id.tv_price_bill_item_time);

        }

    }

    public void setOperateListener(OperateListener listener) {
        this.listener = listener;
    }
}
