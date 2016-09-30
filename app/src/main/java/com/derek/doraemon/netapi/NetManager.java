package com.derek.doraemon.netapi;

import com.derek.doraemon.model.Token;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by derek on 2016/9/28.
 */
public class NetManager {
    private final static String host = "http://pet.tazine.com";
    private static NetManager instance;
    private static RequestService service;
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
        }
        return instance;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUid(long uid) {
        this.uid = uid;
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

    public Call<Resp> completeUserInfo(String sex, String petType, String petBreed, String petName,
        String petAge, String nickName, String profession, String constellation, String intro) {
        return service.completeUserInfo(token, token, sex, petType, petBreed, petName, petAge,
            nickName, profession, constellation, intro);
    }

    public Call<Resp> getHostList() {
        return service.getHostList(token, token, String.valueOf(uid));
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

    public Call<Resp> collect(String type, long postId) {
        return service.collect(token, token, type, String.valueOf(uid), String.valueOf(postId));
    }
}
