package com.derek.doraemon.model;

/**
 * Created by derek on 16/10/15.
 */
public class WechatResp extends BaseModel {

    private String accessToken;
    private String openid;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
