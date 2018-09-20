package com.search.coupon.agent.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.search.coupon.agent.R;

public class FixedRatioImageView extends AppCompatImageView {

    private float ratio = -1f;

    public FixedRatioImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FixedRatioImageView);
        ratio = a.getFloat(R.styleable.FixedRatioImageView_ratio, -1f);
        a.recycle();
    }

    public FixedRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FixedRatioImageView);
        ratio = a.getFloat(R.styleable.FixedRatioImageView_ratio, -1f);
        a.recycle();
    }

    public FixedRatioImageView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // For simple implementation, or internal size is always 0.
        // We depend on the container to specify the layout size of
        // our view. We can't really know what it is since we will be
        // adding and removing different arbitrary views and do not
        // want the layout to change as this happens.

        if (ratio == -1) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int currentWidth = getMeasuredWidth();
            int currentHeight = (int) (((float) currentWidth) * ratio);
            setMeasuredDimension(currentWidth, currentHeight);
        }

    }
}
