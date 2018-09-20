package com.search.coupon.agent.listener;


/***
 * list里面有点击事件的回调接口
 * 
 * 相应的activity或fragment实现此接口 ,并把this传给 adapter 
 * 
 * */

public interface OperateListener {
	
    /**
     * @param operateType   操作类型 自定义
     * @param bean          可以回传实体类
     */

    public void operate(String operateType, Object bean);

}
