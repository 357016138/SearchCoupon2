package com.search.coupon.agent.view;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.search.coupon.agent.R;
import com.search.coupon.agent.utils.StringUtils;


/**
 * Created by ____ Bye丶 on 2017/6/1.
 */

public class LoadingDialog extends Dialog {
    private TextView tv_title;
    public LoadingDialog(Context context) {
        super(context, R.style.style_common_dialog);
        init();
    }

    private void init() {
        setContentView(R.layout.layout_loading);
        setCanceledOnTouchOutside(false);
        tv_title = findViewById(R.id.tv_title);
    }

    public void setLoadingTitle(String title) {
        if(!StringUtils.isEmpty(title))
            tv_title.setText(title);
    }
}
