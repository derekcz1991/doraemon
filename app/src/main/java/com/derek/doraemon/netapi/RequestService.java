package com.derek.doraemon.netapi;

import com.derek.doraemon.model.Token;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by derek on 2016/9/28.
 */
public interface RequestService {

    // ------------------------------- account -------------------------------------
    @FormUrlEncoded
    @POST("v1/register")
    Call<Resp> register(@Field("username") String userName,
                        @Field("email") String email,
                        @Field("password") String password,
                        @Field("pwd_check") String pwdCheck);

    @FormUrlEncoded
    @POST("oauth/access_token")
    Call<Token> getToken(@Field("username") String userName, // 用户邮箱
                         @Field("password") String password,
                         @Field("client_id") int clientId, // 客户端id
                         @Field("client_secret") String clientSecret, // 客户端密钥
                         @Field("grant_type") String grantType); // 授权类型

    @FormUrlEncoded
    @POST("v1/login")
    Call<Resp> login(@Query("access_token") String token,
                     @Field("access_token") String accessToken,
                     @Field("email") String email,
                     @Field("password") String password);


    // ------------------------------- user -------------------------------------
    @FormUrlEncoded
    @POST("v1/complete")
    Call<Resp> completeUserInfo(@Query("access_token") String token,
                                @Field("access_token") String accessToken,
                                @Field("sex") String sex, // 1-男 2-女   (必填)
                                @Field("pet_type") String petType, // 宠物类型 (非必填)
                                @Field("pet_breed") String petBreed, // 宠物品种 (非必填)
                                @Field("pet_name") String petName, // 宠物名称  (非必填)
                                @Field("pet_age") String petAge, // 宠物年龄  (非必填)
                                @Field("nickname") String nickname, // 昵称      必填
                                @Field("profession") String profession,// 职业     (非必填)
                                @Field("constellation") String constellation, // 星座     (非必填)
                                @Field("intro") String intro); // 介绍      必填

    @GET("v1/user/detail")
    Call<Resp> getUserDetail(@Query("access_token") String token,
                             @Query("uid") String uid);

    @GET("v1/post/star/1")
    Call<Resp> getStarUserList(@Query("access_token") String token,
                               @Query("uid") String uid);

    @FormUrlEncoded
    @POST("v1/locate")
    Call<Resp> locate(@Query("access_token") String token,
                      @Field("access_token") String accessToken,
                      @Field("uid") String uid,
                      @Field("long") double longitude,
                      @Field("lat") double latitude);

    // ------------------------------- common -------------------------------------
    @FormUrlEncoded
    @POST("v1/post/like")
    Call<Resp> star(@Query("access_token") String token,
                    @Field("access_token") String accessToken,
                    @Field("type") String type, // 点赞类型  1-寄养 2-朋友圈 3-公益
                    @Field("uid") String uid,
                    @Field("like_id") String likeId);// 对应的帖子id (朋友圈id,公益id)

    @FormUrlEncoded
    @POST("v1/post/comment")
    Call<Resp> comment(@Query("access_token") String token,
                       @Field("access_token") String accessToken,
                       @Field("type") String type, // 点赞类型  1-寄养 2-朋友圈 3-公益
                       @Field("uid") String uid, @Field("post_id") String postId, // 帖子id
                       @Field("comment") String content);

    @FormUrlEncoded
    @POST("v1/post/collect")
    Call<Resp> collect(@Query("access_token") String token,
                       @Field("access_token") String accessToken,
                       @Field("type") String type, // 点赞类型  1-寄养 2-朋友圈 3-公益
                       @Field("uid") String uid,
                       @Field("post_id") String postId); // 帖子id

    @GET("v1/collection")
    Call<Resp> getMyFavList(@Query("access_token") String token,
                            @Query("uid") String uid,
                            @Query("type") String type);

    @GET("v1/collect/cancel")
    Call<Resp> cancelCollection(@Query("access_token") String token,
                                @Query("id") String id);


    // ------------------------------- post -------------------------------------
    @FormUrlEncoded
    @POST("v1/post/list")
    Call<Resp> getHostList(@Query("access_token") String token,
                           @Field("access_token") String accessToken,
                           @Field("uid") String uid);

    @GET("v1/my/post")
    Call<Resp> getMyPostList(@Query("access_token") String token,
                             @Query("uid") String uid);

    @GET("v1/post/delete")
    Call<Resp> deletePost(@Query("access_token") String token,
                          @Query("id") String postId);

    @Multipart
    @POST("v1/post/upload")
    Call<Resp> uploadPostPhoto(@Query("access_token") String token,
                               @Part("access_token") RequestBody accessToken,
                               @Part("uid") RequestBody uid,
                               @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("v1/post/add")
    Call<Resp> publishPost(@Query("access_token") String token,
                           @Field("access_token") String accessToken,
                           @Field("uid") String uid,
                           @Field("content") String content,
                           @Field("photoUrl") String photoUrl);

    // ------------------------------- welfare -------------------------------------
    @FormUrlEncoded
    @POST("v1/welfare/list")
    Call<Resp> getWelfareList(@Query("access_token") String token,
                              @Field("access_token") String accessToken,
                              @Field("uid") String uid);

    @GET("v1/my/welfare")
    Call<Resp> getMyWelfareList(@Query("access_token") String token,
                                @Query("uid") String uid);

    @GET("v1/welfare/delete")
    Call<Resp> deleteWelfare(@Query("access_token") String token,
                             @Query("id") String welfareId);

    @Multipart
    @POST("v1/welfare/upload")
    Call<Resp> uploadWelfarePhoto(@Query("access_token") String token,
                                  @Part("access_token") RequestBody accessToken,
                                  @Part("uid") RequestBody uid,
                                  @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("v1/welfare/add")
    Call<Resp> publishWelfare(@Query("access_token") String token,
                              @Field("access_token") String accessToken,
                              @Field("uid") String uid,
                              @Field("content") String content,
                              @Field("kind") String kind,
                              @Field("photoUrl") String photoUrl);


    // ------------------------------- moment -------------------------------------
    @FormUrlEncoded
    @POST("v1/moment/list")
    Call<Resp> getMomentList(@Query("access_token") String token,
                             @Field("access_token") String accessToken,
                             @Field("uid") String uid);

    @GET("v1/my/moment")
    Call<Resp> getMyMomentList(@Query("access_token") String token,
                               @Query("uid") String uid);

    @GET("v1/moment/delete")
    Call<Resp> deleteMoment(@Query("access_token") String token,
                            @Query("id") String momentId);

    @Multipart
    @POST("v1/moment/upload")
    Call<Resp> uploadMomentPhoto(@Query("access_token") String token,
                                  @Part("access_token") RequestBody accessToken,
                                  @Part("uid") RequestBody uid,
                                  @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("v1/moment/add")
    Call<Resp> publishMoment(@Query("access_token") String token,
                              @Field("access_token") String accessToken,
                              @Field("uid") String uid,
                              @Field("content") String content,
                              @Field("photoUrl") String photoUrl);


    // ------------------------------- nearby -------------------------------------
    @GET("v1/walk/list/1")
    Call<Resp> getNearbyList(@Query("access_token") String token,
                             @Query("access_token") String accessToken,
                             @Query("uid") String uid);

    // ------------------------------- letter -------------------------------------
    @FormUrlEncoded
    @POST("v1/letter/list")
    Call<Resp> getLetterList(@Query("access_token") String token,
                             @Field("access_token") String accessToken,
                             @Field("uid") String uid);

    @FormUrlEncoded
    @POST("v1/letter/chat")
    Call<Resp> getChatList(@Query("access_token") String token,
                           @Field("access_token") String accessToken,
                           @Field("uid") String uid,
                           @Field("chatterId") String chatterId);

    @FormUrlEncoded
    @POST("v1/letter/send")
    Call<Resp> sendChat(@Query("access_token") String token,
                        @Field("access_token") String accessToken,
                        @Field("from") String fromUid,
                        @Field("to") String toUid,
                        @Field("content") String content);
}
