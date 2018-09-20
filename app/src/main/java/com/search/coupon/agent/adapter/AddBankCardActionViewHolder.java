package com.search.coupon.agent.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.search.coupon.agent.R;
import com.search.coupon.agent.ui.activity.BindBankCardActivity;

/**
 * Created by RickBerg on 2018/2/25 0025.
 *
 */
public class AddBankCardActionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private LinearLayout mLlAdd;
    private Context mContext;

    public AddBankCardActionViewHolder(Context context, View itemView) {
        super(itemView);
        mContext = context;
        mLlAdd = itemView.findViewById(R.id.ll_add_bank_card);
        mLlAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_add_bank_card:
                Intent intent = new Intent(mContext, BindBankCardActivity.class);
                mContext.startActivity(intent);
                break;
        }
    }


}
