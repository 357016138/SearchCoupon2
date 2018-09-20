package com.search.coupon.agent.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.search.coupon.agent.R;

/**
 * Created by Administrator on 2018/2/12 0012.
 *
 */

public class PasswordKeyboard extends LinearLayout implements View.OnClickListener {
    private PasswordKeyboardListener mKeyboardListener;
    private View mView;

    public PasswordKeyboard(Context context) {
//        super(context);
        this(context, null);
    }

    public PasswordKeyboard(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
        this(context, attrs, 0);
    }

    public PasswordKeyboard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mView = inflate(context, R.layout.layout_password_keyborard, this);
        initListener();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    public void setKeyboardListener(PasswordKeyboardListener listener){
        mKeyboardListener = listener;
    }

    private void initListener() {
        mView.findViewById(R.id.bt_1).setOnClickListener(this);
        mView.findViewById(R.id.bt_2).setOnClickListener(this);
        mView.findViewById(R.id.bt_3).setOnClickListener(this);
        mView.findViewById(R.id.bt_4).setOnClickListener(this);
        mView.findViewById(R.id.bt_5).setOnClickListener(this);
        mView.findViewById(R.id.bt_6).setOnClickListener(this);
        mView.findViewById(R.id.bt_7).setOnClickListener(this);
        mView.findViewById(R.id.bt_8).setOnClickListener(this);
        mView.findViewById(R.id.bt_9).setOnClickListener(this);
        mView.findViewById(R.id.bt_0).setOnClickListener(this);
        mView.findViewById(R.id.bt_delete).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_1:
            case R.id.bt_2:
            case R.id.bt_3:
            case R.id.bt_4:
            case R.id.bt_5:
            case R.id.bt_6:
            case R.id.bt_7:
            case R.id.bt_8:
            case R.id.bt_9:
            case R.id.bt_0:
                int number = Integer.valueOf(((Button)v).getText().toString());
                mKeyboardListener.addNum(number);
                break;
            case R.id.bt_delete:
                mKeyboardListener.deleteNum();
                break;
            default:
                break;
        }
    }

   public interface PasswordKeyboardListener {
        void addNum(int n);
        void deleteNum();
    }
}
