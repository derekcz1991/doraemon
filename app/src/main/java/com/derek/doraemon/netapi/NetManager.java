package com.derek.doraemon.netapi;

import com.derek.doraemon.constants.Constants;
import com.derek.doraemon.model.Token;
import com.derek.doraemon.model.WechatResp;
import com.derek.doraemon.model.WechatUserinfo;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by derek on 2016/9/28.
 */
public class NetManager {
    private final static String host = "http://pet.tazine.com";
    private static NetManager instance;
    private static RequestService service;
    private static OtherRequestService otherService;
    private String token;
    private long uid;

    private NetManager() {
    }

    public static NetManager getInstance() {
        if (instance == null) {
            instance = new NetManager();
            Retrofit retrofit = new Retrofit.Builder().baseUrl(host)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
            service = retrofit.create(RequestService.class);

            Retrofit otherRetrofit = new Retrofit.Builder().baseUrl("https://api.weixin.qq.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
            otherService = otherRetrofit.create(OtherRequestService.class);
        }
        return instance;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUid(long uid) {
        //this.uid = 26;
        this.uid = uid;
    }

    public long getUid() {
        return uid;
    }

    public String getHost() {
        return host;
    }

    public Call<Resp> register() {
        return service.register("derek", "derekcz1991@gmail.com", "123", "123");
    }

    public Call<Token> getToken() {
        return service.getToken("derekcz1991@gmail.com", "123", 1, "duh2i3h1oh21ihjk32",
            "password");
    }

    public Call<Resp> login(String email, String password) {
        return service.login(token, token, "derekcz1991@gmail.com", "123");
    }

    public Call<Resp> thirdPartyLogin(String platform, String openId) {
        return service.thirdPartyLogin(platform, openId);
    }

    public Call<Resp> completeUserInfo(String sex, String petType, String petBreed, String petName,
                                       String petAge, String nickName, String profession, String constellation, String intro) {
        return service.completeUserInfo(token, token, String.valueOf(uid), sex, petType, petBreed, petName, petAge,
            nickName, profession, constellation, intro);
    }

    public Call<Resp> getHostList(int sort, String city) {
        return service.getHostList(token, token, String.valueOf(uid), sort, city);
    }

    public Call<Resp> getStarUser() {
        return service.getStarUserList(token, String.valueOf(uid));
    }

    public Call<Resp> star(String type, long likeId) {
        return service.star(token, token, type, String.valueOf(uid), String.valueOf(likeId));
    }

    public Call<Resp> comment(String type, long postId, String content) {
        return service.comment(token, token, type, String.valueOf(uid), String.valueOf(postId),
            content);
    }

    public Call<Resp> collect(int type, long postId) {
        return service.collect(token, token, type, String.valueOf(uid), String.valueOf(postId));
    }

    public Call<Resp> uploadPhoto(MultipartBody.Part body, int type) {
        if (type == 0) {
            return service.uploadPostPhoto(token,
                RequestBody.create(MediaType.parse("multipart/form-data"), token),
                RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(uid)), body);
        } else if (type == 1) {
            return service.uploadWelfarePhoto(token,
                RequestBody.create(MediaType.parse("multipart/form-data"), token),
                RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(uid)), body);
        } else {
            return service.uploadMomentPhoto(token,
                RequestBody.create(MediaType.parse("multipart/form-data"), token),
                RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(uid)), body);
        }
    }

    public Call<Resp> post(String content, String photoUrl, int type, int kind) {
        if (type == 0) {
            return service.publishPost(token, token, String.valueOf(uid), content, photoUrl);
        } else if (type == 1) {
            return service.publishWelfare(token, token, String.valueOf(uid), content, String.valueOf(kind), photoUrl);
        } else {
            return service.publishMoment(token, token, String.valueOf(uid), content, photoUrl);
        }
    }

    public Call<Resp> getUserDetail(long uid) {
        return service.getUserDetail(token, String.valueOf(uid));
    }

    public Call<Resp> getLetterList() {
        return service.getLetterList(token, token, String.valueOf(uid));
    }

    public Call<Resp> getChatList(long chatterId) {
        return service.getChatList(token, token, String.valueOf(uid), String.valueOf(chatterId));
    }

    public Call<Resp> sendChat(long toUid, String content) {
        return service.sendChat(token, token, String.valueOf(uid), String.valueOf(toUid), content);
    }

    public Call<Resp> getWelfareList(int sort, String city) {
        return service.getWelfareList(token, token, String.valueOf(uid), sort, city);
    }

    public Call<Resp> getMomentList(int kind) {
        return service.getMomentList(token, token, String.valueOf(uid), kind);
    }

    public Call<Resp> getMyPostList() {
        return service.getMyPostList(token, String.valueOf(uid));
    }

    public Call<Resp> getMyWelfareList() {
        return service.getMyWelfareList(token, String.valueOf(uid));
    }

    public Call<Resp> getMyMomentList() {
        return service.getMyMomentList(token, String.valueOf(uid));
    }

    public Call<Resp> getMyFavList(int type) {
        return service.getMyFavList(token, String.valueOf(uid), String.valueOf(type));
    }

    public Call<Resp> getNearbyList() {
        return service.getNearbyList(token, token, String.valueOf(uid));
    }

    public Call<Resp> deletePublishedItem(int type, long id) {
        switch (type) {
            case 1:
                return service.deletePost(token, String.valueOf(id));
            case 2:
                return service.deleteWelfare(token, String.valueOf(id));
            case 3:
                return service.deleteMoment(token, String.valueOf(id));
        }
        return null;
    }

    public Call<Resp> cancelCollection(long id) {
        return service.cancelCollection(token, String.valueOf(id));
    }

    public Call<Resp> locate(double[] location) {
        return service.locate(token, token, String.valueOf(uid), location[1], location[0]);
    }

    public Call<Resp> uploadAvatar(MultipartBody.Part body) {
        return service.uploadAvatar(token,
            RequestBody.create(MediaType.parse("multipart/form-data"), token),
            RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(uid)), body);
    }

    public Call<WechatResp> oauthWechat(String code, String grantType) {
        return otherService.getWechatToken(Constants.APP_ID, Constants.SECRET_ID, code, grantType);
    }

    public Call<WechatUserinfo> getWechatUserInfo(String accessToken, String openId) {
        return otherService.getWechatUserInfo(accessToken, openId);
    }

    public Call<Resp> uploadAudio(File file, long hostUid) {
        // create RequestBody instance from file
        RequestBody requestFile =
            RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part audio =
            MultipartBody.Part.createFormData("audio", file.getName(), requestFile);

        return service.uploadAudio(token,
            RequestBody.create(MediaType.parse("multipart/form-data"), token),
            RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(uid)),
            RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(hostUid)), audio);
    }

    public Call<Resp> getAudioList() {
        return service.getAudioList(token, token, String.valueOf(uid));
    }

    public Call<ResponseBody> getAudio(String audioName) {
        return service.getAudio("/uploads/audio/" + audioName);
    }

    public Call<Resp> feedback(String feedback) {
        return service.feedback(token, token, String.valueOf(uid), feedback);
    }

    public Call<Resp> evaluate(long hostUid, int grade, String comment) {
        return service.evaluate(token, token, String.valueOf(uid), String.valueOf(hostUid), grade, comment);
    }

    public Call<Resp> findHostPost(String keyword) {
        return service.findHostList(token, token, keyword);
    }

    public Call<Resp> findWelfarePost(String keyword) {
        return service.findWelfareList(token, token, keyword);
    }

    public Call<Resp> getCommentList(long id, int type) {
        return service.getCommentList(token, id, type);
    }

    public Call<Resp> getItemDetail(int type, long id) {
        if (type == 1) {
            return service.getPostDetail(token, id);
        } else {
            return service.getWelfareDetail(token, id);
        }
    }
}
