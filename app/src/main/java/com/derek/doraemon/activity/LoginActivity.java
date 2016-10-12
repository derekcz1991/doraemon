package com.derek.doraemon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.derek.doraemon.MyApplication;
import com.derek.doraemon.R;
import com.derek.doraemon.constants.Constants;
import com.derek.doraemon.constants.SharePrefsConstants;
import com.derek.doraemon.model.Token;
import com.derek.doraemon.model.User;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.netapi.RequestCallback;
import com.derek.doraemon.netapi.Resp;
import com.derek.doraemon.utils.CommonUtils;
import com.derek.doraemon.utils.SharePreferenceHelper;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by derek on 16/7/12.
 */
public class LoginActivity extends BaseActivity {
    private String TAG = this.getClass().getSimpleName();

    // facebook
    private CallbackManager callbackManager;
    private String facebookUid;
    // twitter
    private TwitterAuthClient twitterAuthClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        //CommonUtils.showProgress(this, "登录中...");
    }

    @OnClick(R.id.wechatBtn)
    public void onWechatClick() {
        getToken();
    }

    @OnClick(R.id.fbBtn)
    public void onFacebookClick() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null) {
            callbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    facebookUid = loginResult.getAccessToken().getUserId();
                    SharePreferenceHelper.getInstance().put(SharePrefsConstants.PLATFORM, Constants.PLATFORM_FACEBOOK);
                    SharePreferenceHelper.getInstance().put(SharePrefsConstants.PLATFORM_USER_ID, facebookUid);
                    Log.d(TAG, "facebookUid = " + facebookUid);
                }

                @Override
                public void onCancel() {
                    Log.d(TAG, "facebook login cancel");
                }

                @Override
                public void onError(FacebookException exception) {
                    CommonUtils.toast("facebook登录失败, 请重试");
                    Log.d(TAG, "facebook onError", exception);
                }
            });
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        } else {
            facebookUid = accessToken.getUserId();
            Log.d(TAG, "facebookUid = " + facebookUid);
        }
    }

    @OnClick(R.id.twitterBtn)
    public void onTwitterClick() {
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        if (session == null) {
            getTwitterAuthClient().authorize(this, new com.twitter.sdk.android.core.Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    // The TwitterSession is also available through:
                    // Twitter.getInstance().core.getSessionManager().getActiveSession()
                    TwitterSession session = result.data;
                    // TODO: Remove toast and use the TwitterSession's userID
                    // with your app's user model
                    String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }

                @Override
                public void failure(TwitterException exception) {
                    Log.d("TwitterKit", "Login with Twitter failure", exception);
                }
            });
        } else {
            String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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

    private TwitterAuthClient getTwitterAuthClient() {
        if (twitterAuthClient == null) {
            synchronized (TwitterLoginButton.class) {
                if (twitterAuthClient == null) {
                    twitterAuthClient = new TwitterAuthClient();
                }
            }
        }
        return twitterAuthClient;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == getTwitterAuthClient().getRequestCode()) {
            getTwitterAuthClient().onActivityResult(requestCode, resultCode, data);
        }
    }

}
