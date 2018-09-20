package com.search.coupon.agent.network;

import okhttp3.Call;

/**
 * Created by ____ Bye丶 on 2017/3/23.
 */
public interface ResponseListener {
    public void onRefresh(Call call, int tag, ResultData data);
}
