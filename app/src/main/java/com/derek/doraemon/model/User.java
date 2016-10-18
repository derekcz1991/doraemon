package com.derek.doraemon.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by derek on 2016/9/29.
 */
public class User extends BaseModel {
    private long id;
    @SerializedName("username")
    private String userName;
    @SerializedName("nick_name")
    private String nickName;
    private String constellation;
    private String profession;
    private String email;
    private String intro;
    @SerializedName("last_login")
    private String lastLogin;
    @SerializedName("is_first")
    private boolean isFirst;

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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }
}
