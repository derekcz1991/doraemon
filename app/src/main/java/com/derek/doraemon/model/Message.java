package com.derek.doraemon.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by derek on 08/10/2016.
 */
public class Message extends BaseModel {

    private int id;
    private String content;
    @SerializedName("msg_from")
    private long msgFrom;
    @SerializedName("msg_to")
    private long msgTo;
    @SerializedName("msg_status")
    private int msgStatus;
    @SerializedName("del_status")
    private int delStatus;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("username")
    private String userName;
    @SerializedName("avatar")
    private String avatarUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getMsgFrom() {
        return msgFrom;
    }

    public void setMsgFrom(long msgFrom) {
        this.msgFrom = msgFrom;
    }

    public long getMsgTo() {
        return msgTo;
    }

    public void setMsgTo(long msgTo) {
        this.msgTo = msgTo;
    }

    public int getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(int msgStatus) {
        this.msgStatus = msgStatus;
    }

    public int getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(int delStatus) {
        this.delStatus = delStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
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
}
