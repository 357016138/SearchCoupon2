package com.search.coupon.agent.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/2.
 */

public class CategoryBean {

    /**
     * {"id":1,"categoryName":"兴趣","parentId":0,"twoLevel":[{"categoryName":"旅行","parentId":1,"id":16}
     */

    private String id;
    private String categoryName;
    private String parentId;
    private List<TwoLevelBean> twoLevel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<TwoLevelBean> getTwoLevel() {
        return twoLevel;
    }

    public void setTwoLevel(List<TwoLevelBean> twoLevel) {
        this.twoLevel = twoLevel;
    }

    public class TwoLevelBean{

        private String categoryName;
        private String parentId;
        private String id;

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

}
