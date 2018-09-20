package com.search.coupon.agent.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.search.coupon.agent.R;
import com.search.coupon.agent.bean.BindBankCardInfoBean;
import com.search.coupon.agent.network.UrlConfig;

/**
 * Created by RickBerg on 2018/2/25 0025.
 *
 */
public class BankCardItemViewHolder extends RecyclerView.ViewHolder{
    private TextView mTvBankName;
    private TextView mTvBankCardNumber;
    private ImageView mIvIcon;

    //private final static String URL_ICON = "http://172.18.101.14/appStore/img/bank/";
    public BankCardItemViewHolder(View itemView) {
        super(itemView);
        mTvBankName = itemView.findViewById(R.id.tv_bank_name);
        mTvBankCardNumber = itemView.findViewById(R.id.tv_card_number);
        mIvIcon = itemView.findViewById(R.id.iv_bank_logo);
    }
    public void setData(Context context, BindBankCardInfoBean bankCard){
        mTvBankName.setText(bankCard.getOpenAccBank());
        String cardNum = bankCard.getBankCardNo();
        mTvBankCardNumber.setText("**** **** **** " + cardNum.substring(cardNum.length() - 4, cardNum.length()));
        Glide.with(context).load(UrlConfig.URL_BANK_ICON + bankCard.getBankCode() + ".png").into(mIvIcon);
    }
}
