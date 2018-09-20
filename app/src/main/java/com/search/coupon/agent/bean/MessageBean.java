package com.search.coupon.agent.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/24.
 * 消息列表的bean
 */

public class MessageBean implements Serializable {

    /**
     * "id": 8,
     "userId": 1,
     "orderId": "dd7901641ec64c829d9dcec709809f95",
     "parentCategory": 1,
     "category": 18,
     "review_status": "1",
     "review_context": "恭喜您提交的微信群订单：dd7901641ec64c829d9dcec709809f95，已通过审核",
     "create_time": 1524478771000,
     "read_flag": "0",
     "del_flag": "0"
     * */

    private String id;
    private String userId;
    private String parentCategory;
    private String category;
    private String review_status;
    private String review_context;
    private long create_time;
    private String read_flag;
    private String del_flag;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(String parentCategory) {
        this.parentCategory = parentCategory;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getReview_status() {
        return review_status;
    }

    public void setReview_status(String review_status) {
        this.review_status = review_status;
    }

    public String getReview_context() {
        return review_context;
    }

    public void setReview_context(String review_context) {
        this.review_context = review_context;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getRead_flag() {
        return read_flag;
    }

    public void setRead_flag(String read_flag) {
        this.read_flag = read_flag;
    }

    public String getDel_flag() {
        return del_flag;
    }

    public void setDel_flag(String del_flag) {
        this.del_flag = del_flag;
    }
}
