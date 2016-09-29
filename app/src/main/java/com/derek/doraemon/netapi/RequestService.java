package com.derek.doraemon.netapi;

import com.derek.doraemon.model.Token;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by derek on 2016/9/28.
 */
public interface RequestService {
    @FormUrlEncoded
    @POST("v1/register")
    Call<Resp> register(@Field("username") String userName, @Field("email") String email,
        @Field("password") String password, @Field("pwd_check") String pwdCheck);

    @FormUrlEncoded
    @POST("oauth/access_token")
    Call<Token> getToken(@Field("username") String userName, // 用户邮箱
        @Field("password") String password, @Field("client_id") int clientId, // 客户端id
        @Field("client_secret") String clientSecret, // 客户端密钥
        @Field("grant_type") String grantType); // 授权类型

    @FormUrlEncoded
    @POST("v1/login")
    Call<Resp> login(@Query("access_token") String token, @Field("access_token") String accessToken,
        @Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("v1/complete")
    Call<Resp> completeUserInfo(@Query("access_token") String token,
        @Field("access_token") String accessToken, @Field("sex") String sex,
        // 1-男 2-女   (必填)
        @Field("pet_type") String petType, // 宠物类型 (非必填)
        @Field("pet_breed") String petBreed, // 宠物品种 (非必填)
        @Field("pet_name") String petName, // 宠物名称  (非必填)
        @Field("pet_age") String petAge, // 宠物年龄  (非必填)
        @Field("nickname") String nickname, // 昵称      必填
        @Field("profession") String profession,// 职业     (非必填)
        @Field("constellation") String constellation, // 星座     (非必填)
        @Field("intro") String intro); // 介绍      必填

    @FormUrlEncoded
    @POST("v1/post/list")
    Call<Resp> getHostList(@Query("access_token") String token,
        @Field("access_token") String accessToken, @Field("uid") String id);
}
