package com.search.coupon.agent.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.search.coupon.agent.R;


/**
 * Created by yangwei on 2017/11/3.
 */
public class TopBar extends LinearLayout implements View.OnClickListener {
    private Context context;
    private LinearLayout rltTop;
    //左右两侧图标
    private ImageView ivLeft, ivRight;
    //左右两侧文字
    private TextView tvLeft, tvRight;
    //左右两侧layout
    private LinearLayout ltLeft, ltRight;
    //title文本框
    private TextView tvTitle;
    //底部线
    private View vLine;
    private OnTopbarClickListener onTopbarClickListener;
    //标题，左侧文字，右侧文字
    private String title, leftText, rightText;
    //左侧图标，右侧图标，整体背景
    private Drawable leftImage, rightImage, layoutDrawable;
    //左侧文字颜色，右侧文字颜色，标题文字颜色
    private int leftTextColor = -1, rightTextColor = -1, titleTextColor = -1;
    //是否显示左侧操作按钮或文字，是否显示右侧操作按钮或文字
    private boolean isShowLeft = false, isShowRight = false;
    //是否显示底部线
    private boolean isShowLine = false;

    public TopBar(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public TopBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray styleable = context.obtainStyledAttributes(attrs, R.styleable.TopBar);
        if (styleable != null) {
            title = styleable.getString(R.styleable.TopBar_topbarTile);
            leftText = styleable.getString(R.styleable.TopBar_topbarLeftText);
            rightText = styleable.getString(R.styleable.TopBar_topbarRightText);
            leftImage = styleable.getDrawable(R.styleable.TopBar_topbarLeftImage);
            rightImage = styleable.getDrawable(R.styleable.TopBar_topbarRightImage);
            layoutDrawable = styleable.getDrawable(R.styleable.TopBar_topbarBackground);
            titleTextColor = styleable.getColor(R.styleable.TopBar_topbarTitleTextColor, context.getResources().getColor(R.color.white));
            leftTextColor = styleable.getColor(R.styleable.TopBar_topbarLeftTextColor, context.getResources().getColor(R.color.white));
            rightTextColor = styleable.getColor(R.styleable.TopBar_topbarRightTextColor, context.getResources().getColor(R.color.white));
            isShowLeft = styleable.getBoolean(R.styleable.TopBar_topbarIsShowLeft, false);
            isShowRight = styleable.getBoolean(R.styleable.TopBar_topbarIsShowRight, false);
            isShowLine = styleable.getBoolean(R.styleable.TopBar_topbarIsShowLine, false);
            styleable.recycle();
        }
        init();
    }

    private void init() {
        LayoutInflater.from(context).inflate(R.layout.layout_topbar, this);
        rltTop = (LinearLayout) findViewById(R.id.rlt_top);

        ltLeft = findViewById(R.id.lt_top_left);

        ltRight = findViewById(R.id.lt_top_right);

        ivLeft = (ImageView) findViewById(R.id.iv_top_left);
        ivLeft.setOnClickListener(this);

        ivRight = (ImageView) findViewById(R.id.iv_top_right);
        ivRight.setOnClickListener(this);

        tvTitle = (TextView) findViewById(R.id.tv_top_title);

        tvLeft = findViewById(R.id.tv_top_left);
        tvLeft.setOnClickListener(this);

        tvRight = findViewById(R.id.tv_top_right);
        tvRight.setOnClickListener(this);

        vLine = findViewById(R.id.v_line);

        tvTitle.setText(TextUtils.isEmpty(title) ? "" : title);
        if (!TextUtils.isEmpty(leftText)) {
            tvLeft.setText(leftText);
            tvLeft.setVisibility(VISIBLE);
            ivLeft.setVisibility(GONE);
        }
        if (!TextUtils.isEmpty(rightText)) {
            tvRight.setText(rightText);
            tvRight.setVisibility(VISIBLE);
            ivRight.setVisibility(GONE);
        }
        if (leftImage != null) {
            ivLeft.setImageDrawable(leftImage);
            ivLeft.setVisibility(VISIBLE);
            tvLeft.setVisibility(GONE);
        }
        if (rightImage != null) {
            ivRight.setImageDrawable(rightImage);
            ivRight.setVisibility(VISIBLE);
            tvRight.setVisibility(GONE);
        }
        tvLeft.setTextColor(leftTextColor == -1 ? getResources().getColor(R.color.color_3F3F3F) : leftTextColor);
        tvRight.setTextColor(rightTextColor == -1 ? getResources().getColor(R.color.color_3F3F3F) : rightTextColor);
        tvTitle.setTextColor(titleTextColor == -1 ? getResources().getColor(R.color.color_3F3F3F) : titleTextColor);
        if (layoutDrawable != null) rltTop.setBackgroundDrawable(layoutDrawable);
        ltLeft.setVisibility(isShowLeft ? VISIBLE : INVISIBLE);
        ltRight.setVisibility(isShowRight ? VISIBLE : INVISIBLE);
        vLine.setVisibility(isShowLine ? VISIBLE : GONE);
    }

    public void setLeftVisible(boolean isShow) {
        ltLeft.setVisibility(isShow ? VISIBLE : INVISIBLE);
    }

    public void setRightVisible(boolean isShow) {
        ltRight.setVisibility(isShow ? VISIBLE : INVISIBLE);
    }

    public void setTitleTextSize(int size) {
        tvTitle.setTextSize(size);
    }

    public void setBackground(int resid) {
        rltTop.setBackgroundResource(resid);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public String getTitle() {
        return tvTitle.getText().toString();
    }

    public void setLeftImage(int id) {
        ivLeft.setImageDrawable(context.getResources().getDrawable(id));
        ivLeft.setVisibility(VISIBLE);
        tvLeft.setVisibility(GONE);
    }

    public void setRightImage(int id) {
        ivRight.setImageDrawable(context.getResources().getDrawable(id));
        ivRight.setVisibility(VISIBLE);
        tvRight.setVisibility(GONE);
    }

    public void setLeftText(String leftText) {
        tvLeft.setText(leftText);
        tvLeft.setVisibility(VISIBLE);
        ivLeft.setVisibility(GONE);
    }

    public void setRightText(String rightText) {
        tvRight.setText(rightText);
        tvRight.setVisibility(VISIBLE);
        ivRight.setVisibility(GONE);
    }

    public void setTitleColor(int color) {
        tvTitle.setTextColor(color);
    }

    public void setLeftTextColor(int color) {
        tvLeft.setTextColor(color);
    }

    public void setRightTextColor(int color) {
        tvRight.setTextColor(color);
    }

    public void setLineVisible(boolean isShow) {
        vLine.setVisibility(isShow ? VISIBLE : GONE);
    }

    public View getContentView() {
        return rltTop;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_top_left:
            case R.id.tv_top_left:
                if (onTopbarClickListener != null) {
                    onTopbarClickListener.OnTopLeftClick();
                }
                break;
            case R.id.iv_top_right:
            case R.id.tv_top_right:
                if (onTopbarClickListener != null) {
                    onTopbarClickListener.OnTopRightClick();
                }
                break;
        }
    }

    public void setOnTopbarClickListener(OnTopbarClickListener onTopbarClickListener) {
        this.onTopbarClickListener = onTopbarClickListener;
    }

    public interface OnTopbarClickListener {
        void OnTopLeftClick();

        void OnTopRightClick();
    }

}
