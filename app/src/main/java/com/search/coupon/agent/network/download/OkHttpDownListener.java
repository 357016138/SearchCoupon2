package com.search.coupon.agent.network.download;

/**
 * Created by ____ Bye丶 on 2016/9/21.
 */

public interface OkHttpDownListener {
    public void onFailure();
    public void onProcess(int progress);
    public void onSuccess();
}
