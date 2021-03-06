package com.derek.doraemon.netapi;

import com.derek.doraemon.model.WechatResp;
import com.derek.doraemon.model.WechatUserinfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by derek on 16/10/15.
 */
public interface OtherRequestService {

    @GET("sns/oauth2/access_token")
    Call<WechatResp> getWechatToken(@Query("appid") String appid,
                                    @Query("secret") String secret,
                                    @Query("code") String code,
                                    @Query("grant_type") String grantType);

    @GET("sns/userinfo")
    Call<WechatUserinfo> getWechatUserInfo(@Query("access_token") String accessToken,
                                           @Query("openid") String openid);
}
