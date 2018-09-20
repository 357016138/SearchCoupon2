package com.search.coupon.agent.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.search.coupon.agent.R;
import com.search.coupon.agent.common.BaseApplication;


/**
 * Created by dell on 2017/11/6.
 */
public class DiaLogUtils extends Dialog {

    private Context context;
    private static DiaLogUtils diaLogUtils = null;
    private static int layoutID;

    private static class ClassHolder {
//        private static final DiaLogUtils INSTANCE =
    }

    static DiaLogUtils getInstance(Context mContext) {

        if (diaLogUtils == null) {
            diaLogUtils = new DiaLogUtils(mContext);
        }

        return diaLogUtils;
    }

    private TextView titleTV;
    private View lineView;
    private TextView contentTV;
    private TextView cancelTV;
    private TextView sureTV;
    private ImageView windowDialog_close;
    private View windowDialog_buttonView;
    private View windowDialog_buttonLayView;

    protected DiaLogUtils(Context context) {
        super(context);
        this.context = context;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉Dialog的标题栏
        setContentView(R.layout.window_dialog);
        initView();
        Display display = this.getWindow().getWindowManager().getDefaultDisplay();
        this.getWindow().setLayout((int) (display.getWidth() * 0.85), WindowManager.LayoutParams.WRAP_CONTENT);
    }


    private void initView() {

        titleTV = findViewById(R.id.windowDialog_title);
        lineView = findViewById(R.id.windowDialog_LF);
        contentTV = findViewById(R.id.windowDialog_content);
        sureTV = findViewById(R.id.windowDialog_sure);
        cancelTV = findViewById(R.id.windowDialog_cancel);
        windowDialog_close = findViewById(R.id.windowDialog_close);
        windowDialog_buttonView = findViewById(R.id.windowDialog_buttonView);
        windowDialog_buttonLayView = findViewById(R.id.windowDialog_buttonLayView);
    }


    public static DiaLogUtils creatDiaLog(Context context) {

        if (diaLogUtils != null) {
            if (diaLogUtils.isShowing()) {
                diaLogUtils.dismiss();
            }
            diaLogUtils = null;
        }
        if (context == null) {
            context = BaseApplication.getApplication();
        }
        diaLogUtils = getInstance(context);
        diaLogUtils.setCanceledOnTouchOutside(false);
        return diaLogUtils;
    }

    public static DiaLogUtils creatDiaLog(Context context, int layoutId) {

        if (layoutId > 0) {
            layoutID = layoutId;
        } else {
            layoutID = 0;
        }
        creatDiaLog(context);

        return diaLogUtils;
    }

    public void showDialog() {

        if (diaLogUtils != null) {

            if (!diaLogUtils.isShowing()) {
                if (context instanceof Activity && !((Activity) context).isFinishing()) {
                    diaLogUtils.show();
                } else {
                    diaLogUtils = null;
                }
            }
        }//end if (diaLogUtils != null)
    }

    public DiaLogUtils setTitle(String title, int gravity) {

        if (title != null && title.trim().length() > 0) {
            titleTV.setVisibility(View.VISIBLE);
            lineView.setVisibility(View.VISIBLE);
            titleTV.setText(title);
            if (gravity == Gravity.LEFT) {
                titleTV.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            } else if (gravity == Gravity.CENTER) {
                titleTV.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
            } else if (gravity == Gravity.RIGHT) {
                titleTV.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            }

        } else {
            lineView.setVisibility(View.GONE);
            titleTV.setVisibility(View.GONE);
        }
        return diaLogUtils;
    }

    public DiaLogUtils setContent(String content) {

        if (content != null && content.trim().length() > 0) {
            contentTV.setVisibility(View.VISIBLE);
            contentTV.setText(content);
        } else {
            contentTV.setVisibility(View.GONE);
        }

        return diaLogUtils;
    }

    public DiaLogUtils setContent(String content, int gravity) {

        if (content != null && content.trim().length() > 0) {
            contentTV.setVisibility(View.VISIBLE);
            contentTV.setText(content);

        } else {
            contentTV.setVisibility(View.GONE);
        }

        return diaLogUtils;
    }

    public DiaLogUtils setSureButton(String buttonTxt, View.OnClickListener clickListener) {

        if (buttonTxt != null && buttonTxt.trim().length() > 0) {
            sureTV.setText(buttonTxt);
        }

        if (clickListener != null) {
            sureTV.setOnClickListener(clickListener);
        } else {
            sureTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (diaLogUtils != null) {
                        diaLogUtils.dismiss();
                    }
                }
            });
        }
        windowDialog_buttonLayView.setVisibility(View.VISIBLE);
        sureTV.setVisibility(View.VISIBLE);

        return diaLogUtils;
    }

    public DiaLogUtils setCancelButton(String buttonTxt, View.OnClickListener clickListener) {

        if (buttonTxt != null && buttonTxt.trim().length() > 0) {
            cancelTV.setText(buttonTxt);
        }

        if (clickListener != null) {
            cancelTV.setOnClickListener(clickListener);
        } else {
            cancelTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (diaLogUtils != null) {
                        diaLogUtils.dismiss();
                    }
                }
            });
        }
        windowDialog_buttonLayView.setVisibility(View.VISIBLE);
        windowDialog_buttonView.setVisibility(View.VISIBLE);
        cancelTV.setVisibility(View.VISIBLE);
        return diaLogUtils;
    }

    public DiaLogUtils setCloseButton(View.OnClickListener clickListener) {

        windowDialog_close.setVisibility(View.VISIBLE);

        if (clickListener != null) {
            windowDialog_close.setOnClickListener(clickListener);
        } else {
            windowDialog_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (diaLogUtils != null) {
                        diaLogUtils.dismiss();
                    }
                }
            });
        }
        windowDialog_buttonView.setVisibility(View.VISIBLE);
        return diaLogUtils;
    }

    public void destroyDialog() {

        if (diaLogUtils != null) {
            if (diaLogUtils.isShowing()) {
                diaLogUtils.dismiss();
            }

            diaLogUtils = null;
        }
    }

}
