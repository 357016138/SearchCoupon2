package com.search.coupon.agent.view.iosspinner;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.search.coupon.agent.R;

/**
 * @author baipeng
 * @Title SpinnerPop
 * @Date 2018/2/9 17:17
 * @Description SpinnerPop.
 */
public class SpinnerPop {
    private Context mContext;

    public void showPopupWindow(){
        if(!popupWindow.isShowing()){
            popupWindow.showAtLocation(((Activity) mContext).getWindow().getDecorView(),
                    Gravity.BOTTOM, 0, 0);
        }
    }

    private PopupWindow popupWindow;
    public SpinnerPop(Context context, BaseSpinnerAdapter adapter, AdapterView.OnItemClickListener listener) {
        // TODO Auto-generated method stub
        // 获取自定义布局文件pop.xml的视图
        this.mContext = context;
        View popupWindow_view = ((Activity) mContext).getLayoutInflater().inflate(R.layout.pop_spinner, null, false);
        ListView listView = (ListView) popupWindow_view.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(listener);
        listView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
                // TODO Auto-generated method stub
                return false;
            }});

        popupWindow = new PopupWindow(popupWindow_view,
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
        ColorDrawable dw = new ColorDrawable(-00000);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.setOutsideTouchable(true);
        popupWindow_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (popupWindow != null) {
                    if (popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                }
                return false;
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
    }

    public void hidePopupWindow() {
        // TODO Auto-generated method stub
        if (popupWindow != null) {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        }
    }
}
