package com.search.coupon.agent.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.search.coupon.agent.R;
import com.search.coupon.agent.bean.BindBankCardInfoBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的银行卡适配器
 * Created by fan on 2017/11/26.
 */
@Deprecated
public class MyBankCardListAdapter extends BaseAdapter {
    private static final int TOP_INDEX = 0X0001;
    private static final int DATA_INDEX = 0X0002;
    private static final int BOTTOM_INDEX = 0X0003;

    private Context context;
    private List<BindBankCardInfoBean> lists;
    private boolean isEdit;//是否开启编辑

    public MyBankCardListAdapter(Context context, List<BindBankCardInfoBean> list) {
        this.context = context;
        this.lists = list;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_bank_card_data, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        setData(viewHolder, position);
        return convertView;

    }

    private void setData(ViewHolder holder, int position) {
        BindBankCardInfoBean bean = lists.get(position);
        //设置银行卡背景色
        String[] colorsStr;
        if (!TextUtils.isEmpty(bean.getBankCardColor()) && bean.getBankCardColor().contains(",")) {
            colorsStr = bean.getBankCardColor().split(",");
        } else {
            colorsStr = new String[]{bean.getBankCardColor()};
        }
        int[] colors = new int[colorsStr.length];
        for (int i = 0; i < colorsStr.length; i++) {
            colors[i] = Color.parseColor(colorsStr[i]);
        }
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TL_BR, colors);
        gd.setCornerRadius(18);
        holder.rlCard.setBackgroundDrawable(gd);
        //加载银行图片
        //Glide.with(context).load(UrlConfig.IMAGE_URL + "/" + bean.getBankCardIconUrl()).into(holder.ivBankIcon);
        //设置银行卡信息
        holder.tvBankName.setText(bean.getBank());
        holder.tvCardType.setText(bean.getBankType());
        holder.tvCardNo.setText("**** **** **** " + bean.getCardNo().substring(bean.getCardNo().length() - 4));
//        if ("1".equals(bean.getIsDefaultCard())) {//1是默认卡
        holder.tvDefault.setText("默认卡");

    }

    static class ViewHolder {
        @BindView(R.id.vBlank)
        View vBlank;
        @BindView(R.id.ivBankIcon)
        ImageView ivBankIcon;
        @BindView(R.id.tvBankName)
        TextView tvBankName;
        @BindView(R.id.tvDefault)
        TextView tvDefault;
        @BindView(R.id.tvCardType)
        TextView tvCardType;
        @BindView(R.id.tvCardNo)
        TextView tvCardNo;
        @BindView(R.id.rlCard)
        RelativeLayout rlCard;
        @BindView(R.id.rbDefault)
        RadioButton rbDefault;
        @BindView(R.id.llItem)
        LinearLayout llItem;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
