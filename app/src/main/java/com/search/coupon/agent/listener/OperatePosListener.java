package com.search.coupon.agent.listener;

/**
 * @author baipeng
 * @Title OperatePosListener
 * @Date 2018/3/3 15:33
 * @Description 回调接口.
 */
public interface OperatePosListener {
    /**
     * @param operateType   操作类型 自定义
     * @param position      position
     */
    void operatePos(String operateType, int position);
}
