package com.search.coupon.agent.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.search.coupon.agent.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yangwei on 2017/12/7.
 */
public class DownloadDialog extends Dialog {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_ok)
    TextView tvOk;

    private OnDownLoadClickListener onDownLoadClickListener;

    public void setOnDownLoadClickListener(OnDownLoadClickListener onDownLoadClickListener) {
        this.onDownLoadClickListener = onDownLoadClickListener;
    }

    public DownloadDialog(@NonNull Context context) {
        super(context, R.style.style_common_dialog);
        setContentView(R.layout.layout_download_dialog);
        ButterKnife.bind(this);
        setCancelable(false);
        Display display = this.getWindow().getWindowManager().getDefaultDisplay();
        this.getWindow().setLayout((int) (display.getWidth() * 0.85), WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void setContent(String content) {
        tvContent.setText(content);
    }

    public void setCancelText(String text) {
        tvCancel.setText(text);
    }

    @OnClick({R.id.tv_cancel, R.id.tv_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                if (onDownLoadClickListener != null) {
                    onDownLoadClickListener.onLeftClick();
                }
                break;

            case R.id.tv_ok:
                if (onDownLoadClickListener != null) {
                    onDownLoadClickListener.onRightClick();
                }
                break;
        }
    }

    public interface OnDownLoadClickListener {
        void onLeftClick();

        void onRightClick();
    }
}
