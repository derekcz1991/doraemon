package com.derek.doraemon.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by derek on 2016/9/29.
 */
public class StarUser extends BaseModel {

    private long id;
    @SerializedName("username") private String userName;
    @SerializedName("avatar") private String avatarUrl;
    @SerializedName("recommend") private int recommendNum;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getRecommendNum() {
        return recommendNum;
    }

    public void setRecommendNum(int recommendNum) {
        this.recommendNum = recommendNum;
    }
}
