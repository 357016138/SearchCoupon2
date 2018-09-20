package com.search.coupon.agent.service;


/**
 * Created by RickBerg on 2018/3/13 0013.
 * eventBus 消息传递服务类
 */

public class MessageEvent {
    private int tag;
    private Object obj;

    public MessageEvent(int tag){
        this.tag = tag;
    }

    public MessageEvent(int tag, Object obj){
        this.tag = tag;
        this.obj = obj;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
