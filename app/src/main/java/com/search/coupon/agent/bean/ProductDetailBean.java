package com.search.coupon.agent.bean;

import java.io.Serializable;

/**
 * @author song
 */
public class ProductDetailBean implements Serializable {

    /**
     *   "userId": 1,
     "groupInfoId": 3,
     "uniqueId": "fb47093524a24a76bc237a679cf456bf",
     "groupName": "发布微信群测试搜索功能",
     "groupImage": "http://p5bahoihf.bkt.clouddn.com/Fg18AkbIDyB_QYegczxTugQVD4ct",
     "coverImage": "http://p5bahoihf.bkt.clouddn.com/Fg18AkbIDyB_QYegczxTugQVD4ct",
     "userWechat": "tjggtlx",
     "tags": "测试|搜索|微信群",
     "description": "这是一个微信群搜索测试发布,为了试验搜索功能是否可用,各位请勿加入.",
     "codeType": 2,
     "updateDate": 1522997899000,
     "province": "北京市",
     "city": "丰台区",
     "parentCategory": "兴趣",
     "category": "文学",
     "lookCount": 15
     * */
    private String userId;                    //发布者id
    private String groupInfoId;                //微信群自增id
    private String uniqueId;                 //唯一uuid
    private String groupName;               //群名称
    private String groupImage;               // 群二维码地址
    private String coverImage;              //封面地址
    private String userWechat;             //微信号
    private String tags;                   //标签
    private String description;             //描述
    private String codeType;               //标示位
    private long updateDate;              //最后变更时间时间戳
    private String province;                //一级区域名称
    private String city;                    // 二级区域名称
    private String parentCategory;          //一级类目名称
    private String category;             //二级类目名称
    private String lookCount;             //浏览量

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupInfoId() {
        return groupInfoId;
    }

    public void setGroupInfoId(String groupInfoId) {
        this.groupInfoId = groupInfoId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getUserWechat() {
        return userWechat;
    }

    public void setUserWechat(String userWechat) {
        this.userWechat = userWechat;
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

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getLookCount() {
        return lookCount;
    }

    public void setLookCount(String lookCount) {
        this.lookCount = lookCount;
    }
}
