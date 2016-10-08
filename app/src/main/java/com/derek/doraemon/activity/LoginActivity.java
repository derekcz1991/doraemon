package com.derek.doraemon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.derek.doraemon.MyApplication;
import com.derek.doraemon.R;
import com.derek.doraemon.constants.SharePrefsConstants;
import com.derek.doraemon.model.Token;
import com.derek.doraemon.model.User;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.netapi.RequestCallback;
import com.derek.doraemon.netapi.Resp;
import com.derek.doraemon.utils.SharePreferenceHelper;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by derek on 16/7/12.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.wechatBtn).setOnClickListener(this);
        findViewById(R.id.fbBtn).setOnClickListener(this);
        findViewById(R.id.wbBtn).setOnClickListener(this);
        findViewById(R.id.twBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wechatBtn:
                getToken();
                break;
            case R.id.fbBtn:
            case R.id.wbBtn:
            case R.id.twBtn:
                register();
                break;
        }
    }

    private void getToken() {
        NetManager.getInstance().getToken().enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response != null && response.body() != null) {
                    Token token = response.body();
                    Log.d("RequestCallback", token.toString());
                    SharePreferenceHelper.getInstance()
                        .put(SharePrefsConstants.TOKEN, token.getAccessToken());
                    NetManager.getInstance().setToken(token.getAccessToken());
                    login("", "");
                } else {
                    Toast.makeText(MyApplication.getContext(), "获取token失败", Toast.LENGTH_SHORT)
                        .show();
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.d("RequestCallback", call.request().url().toString(), t);
                Toast.makeText(MyApplication.getContext(), "网络失败请重试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void login(String email, String password) {
        NetManager.getInstance()
            .login(email, password)
            .enqueue(new RequestCallback(new RequestCallback.Callback() {
                @Override
                public void success(Resp resp) {
                    Gson gson = new Gson();
                    User user = gson.fromJson(gson.toJsonTree(resp.getData()), User.class);
                    NetManager.getInstance().setUid(user.getId());
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                }

                @Override
                public boolean fail(Resp resp) {
                    return false;
                }
            }));
    }

    private void register() {
        NetManager.getInstance()
            .register()
            .enqueue(new RequestCallback(new RequestCallback.Callback() {
                @Override
                public void success(Resp resp) {

                }

                @Override
                public boolean fail(Resp resp) {
                    return false;
                }
            }));
    }
}
