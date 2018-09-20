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
 * Created by song on 2018/2/25 0025.
 */

public class OneButtonDialog extends Dialog {

    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_ok)
    TextView tvOk;
    private DownloadDialog.OnDownLoadClickListener onDownLoadClickListener;

    public void setOnDownLoadClickListener(DownloadDialog.OnDownLoadClickListener onDownLoadClickListener) {
        this.onDownLoadClickListener = onDownLoadClickListener;
    }

    public OneButtonDialog(@NonNull Context context) {
        super(context, R.style.style_common_dialog);
        setContentView(R.layout.layout_onebutton_dialog);
        ButterKnife.bind(this);
        setCancelable(false);
        Display display = this.getWindow().getWindowManager().getDefaultDisplay();
        this.getWindow().setLayout((int) (display.getWidth() * 0.85), WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void setContent(String content) {
        tvContent.setText(content);
    }

    public void setOkText(String text) {
        tvOk.setText(text);
    }

    @OnClick({R.id.tv_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_ok:
                if (onDownLoadClickListener != null) {
                    onDownLoadClickListener.onRightClick();
                }
                break;
            default:
                break;
        }
    }

}
