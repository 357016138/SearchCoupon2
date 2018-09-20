package com.search.coupon.agent.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.view.View;

import com.search.coupon.agent.utils.DensityUtil;

/**
 * Created by RickBerg on 2018/3/1 0001.
 *
 */

public class BankCardListItemDivider extends ItemDecoration{
    private int mDividerHeight;
    private Context mContext;

    public BankCardListItemDivider(Context context, int height){
        mDividerHeight = height;
        mContext = context;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top = DensityUtil.dip2px(mContext, mDividerHeight);
    }
}
