package com.search.coupon.agent.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.search.coupon.agent.R;
import com.search.coupon.agent.bean.BindBankCardInfoBean;

import java.util.List;

/**
 * Created by RickBerg on 2018/2/25 0025.
 *
 */

public class BankCardListAdapter extends RecyclerView.Adapter {

    private static final int BANK_CARD_ITEM = R.layout.layout_bank_card_item_assert;
    private static final int ADD_BANK_CARD = R.layout.layout_add_bank_card_action;

    private Context mContext;
    private List<BindBankCardInfoBean> mCardList;

    public BankCardListAdapter(Context context, List<BindBankCardInfoBean> list){
        mContext = context;
        mCardList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case BANK_CARD_ITEM:
                View view = LayoutInflater.from(mContext).inflate(BANK_CARD_ITEM, parent, false);
                return new BankCardItemViewHolder(view);
            case ADD_BANK_CARD:
                View viewAdd = LayoutInflater.from(mContext).inflate(ADD_BANK_CARD, parent, false);
                return new AddBankCardActionViewHolder(mContext, viewAdd);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
       if (holder instanceof BankCardItemViewHolder){
           ((BankCardItemViewHolder)holder).setData(mContext, mCardList.get(position));
       }
    }

    @Override
    public int getItemCount() {
       return  mCardList.size() == 0 ? 1 : mCardList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mCardList.size()){
            return ADD_BANK_CARD;
        } else {
            return BANK_CARD_ITEM;
        }
    }

}
