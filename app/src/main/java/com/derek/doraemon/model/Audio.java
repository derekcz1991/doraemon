package com.derek.doraemon.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by derek on 16/10/20.
 */
public class Audio extends BaseModel {

    private long id;
    @SerializedName("apply_userid")
    private long applyUserId;
    @SerializedName("audio_url")
    private String audioUrl;
    @SerializedName("apply_status")
    private String applyStatus;
    @SerializedName("username")
    private String userName;
    @SerializedName("avatar")
    private String avatarUrl;
    private String status;

    public Audio() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(long applyUserId) {
        this.applyUserId = applyUserId;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(String applyStatus) {
        this.applyStatus = applyStatus;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
