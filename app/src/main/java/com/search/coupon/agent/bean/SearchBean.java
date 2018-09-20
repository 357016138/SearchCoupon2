package com.search.coupon.agent.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/4/7.
 */

public class SearchBean implements Serializable {

    private int totalCount;    //总数目
    private List<ProductBean> groups;

    /**
     *  "groupInfoId": 3,
     "provinceId": 1,
     "cityId": 40,
     "parentCategory": 1,
     "category": 20,
     "uniqueId": "fb47093524a24a76bc237a679cf456bf",
     "coverImage": "Fg18AkbIDyB_QYegczxTugQVD4ct",
     "groupName": "发布微信群测试搜索功能",
     "tags": "测试|搜索|微信群",
     "description": "这是一个微信群搜索测试发布,为了试验搜索功能是否可用,各位请勿加入."
     *
     * */
    public static class ProductBean implements Serializable {

        private String groupInfoId;
        private String provinceId;
        private String cityId;
        private String parentCategory;
        private String category;
        private String uniqueId;
        private String coverImage;
        private String groupName;
        private String tags;
        private String description;

        public String getGroupInfoId() {
            return groupInfoId;
        }

        public void setGroupInfoId(String groupInfoId) {
            this.groupInfoId = groupInfoId;
        }

        public String getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(String provinceId) {
            this.provinceId = provinceId;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
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

        public String getUniqueId() {
            return uniqueId;
        }

        public void setUniqueId(String uniqueId) {
            this.uniqueId = uniqueId;
        }

        public String getCoverImage() {
            return coverImage;
        }

        public void setCoverImage(String coverImage) {
            this.coverImage = coverImage;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }


    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<ProductBean> getGroups() {
        return groups;
    }

    public void setGroups(List<ProductBean> groups) {
        this.groups = groups;
    }
}
