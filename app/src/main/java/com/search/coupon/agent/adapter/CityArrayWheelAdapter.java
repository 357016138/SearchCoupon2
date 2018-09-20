package com.search.coupon.agent.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.search.coupon.agent.bean.ProvinceBean;
import com.search.coupon.agent.view.wheelview.adapter.BaseWheelAdapter;
import com.search.coupon.agent.view.wheelview.widget.WheelItem;

/**
 * Created by song on 2018/4/2 0002.
 */

public class CityArrayWheelAdapter<T> extends BaseWheelAdapter<T> {
    private Context mContext;

    public CityArrayWheelAdapter(Context context) {
        mContext = context;
    }

    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new WheelItem(mContext);
        }
        WheelItem wheelItem = (WheelItem) convertView;
        T item = getItem(position);
        if (wheelItem instanceof CharSequence) {
            wheelItem.setText((CharSequence) item);
        } else {
            ProvinceBean.CityBean bean =(ProvinceBean.CityBean)item;
            wheelItem.setText(bean.getAreaName());
        }
        return convertView;
    }


}