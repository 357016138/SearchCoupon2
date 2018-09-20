package com.search.coupon.agent.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.search.coupon.agent.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by song on 2018/5/8 0008.
 *
 * 通用两个按钮的Dialog ，可以显示标题，内容和图片
 *
 * 想显示那个就掉用显示的方法
 *
 * CommonWebActivity 显示二维码有实例
 *
 */

public class CommonDialog extends Dialog {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.iv_image)
    ImageView iv_image;

    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_ok)
    TextView tvOk;
    @BindView(R.id.v_view)
    View vView;

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public CommonDialog(@NonNull Context context) {
        super(context, R.style.style_common_dialog);
        setContentView(R.layout.layout_common_dialog);
        ButterKnife.bind(this);
        setCancelable(false);
        Display display = this.getWindow().getWindowManager().getDefaultDisplay();
        this.getWindow().setLayout((int) (display.getWidth() * 0.80), LinearLayout.LayoutParams.WRAP_CONTENT);
        this.getWindow().setGravity(Gravity.CENTER);
    }

    /**
     * 设置标题和主内容
     * */
    public void setContent(String content,String title) {
        tvContent.setText(content);
        tvTitle.setText(title);
    }
    /**
     * 设置图片资源,Id资源类型
     * */
    public void setImageUrl(int url) {
        iv_image.setImageResource(url);
    }
    /**
     * 设置图片资源,Bitmap资源
     * */
    public void setImageUrl(Bitmap bp) {
        iv_image.setImageBitmap(bp);
    }
    /**
     * 设置左边按钮文字内容
     * */
    public void setLeftText(String content) {
        tvCancel.setText(content);
    }
    /**
     * 设置右边按钮文字内容
     * */
    public void setRightText(String content) {
        tvOk.setText(content);
    }

    /**
     * 设置标题显示
     * */
    public void setTitleVisibility() {
        tvTitle.setVisibility(View.VISIBLE);
    }
    /**
     * 设置主内容显示
     * */
    public void setContentVisibility() {
        tvContent.setVisibility(View.VISIBLE);
    }
    /**
     * 设置图片显示
     * */
    public void setImageVisibility() {
        iv_image.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.tv_cancel, R.id.tv_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                if (onClickListener != null) {
                    onClickListener.onLeftClick();
                }
                break;

            case R.id.tv_ok:
                if (onClickListener != null) {
                    onClickListener.onRightClick();
                }
                break;
        }
    }

    public interface OnClickListener {
        void onLeftClick();
        void onRightClick();
    }
}
