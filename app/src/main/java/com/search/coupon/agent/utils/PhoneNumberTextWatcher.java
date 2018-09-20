package com.search.coupon.agent.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by song on 2018/2/27 0027.
 * 手机号自动分隔的TextWatcher       身份证号，银行卡号的分隔可以根据这个类改改规则
 *
 */

public class PhoneNumberTextWatcher implements TextWatcher{

    EditText editText;
    int lastContentLength = 0;
    boolean isDelete = false;

    public PhoneNumberTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        StringBuffer sb = new StringBuffer(s);
        //是否为输入状态还是删除状态
        isDelete = s.length() > lastContentLength ? false : true;

        //输入状态  输入是第4，第9位，这时需要插入空格
        if(!isDelete&& (s.length() == 4||s.length() == 9)){
            if(s.length() == 4) {
                sb.insert(3, " ");
            }else {
                sb.insert(8, " ");
            }
            setContent(sb);
        }

        //删除状态  删除的位置到4，9时，剔除空格
        if (isDelete && (s.length() == 4 || s.length() == 9)) {
            sb.deleteCharAt(sb.length() - 1);
            setContent(sb);
        }
        //默认反显的情况
        if (s.length() == 11 && !sb.toString().contains(" ")){
            sb.insert(3, " ");
            sb.insert(8, " ");
            setContent(sb);
        }
        lastContentLength = sb.length();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 添加或删除空格EditText的设置
     *
     * @param sb
     */
    private void setContent(StringBuffer sb) {
        editText.setText(sb.toString());
        //移动光标到最后面
        editText.setSelection(sb.length());
    }
}