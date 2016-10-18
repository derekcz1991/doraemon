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
import com.derek.doraemon.model.ThirdParty;
import com.derek.doraemon.model.Token;
import com.derek.doraemon.model.User;
import com.derek.doraemon.model.WechatResp;
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
import com.google.gson.Gson;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.Arrays;

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
    public static String wechatCode;

    // facebook
    private CallbackManager callbackManager;
    private String facebookUid;
    // twitter
    private TwitterAuthClient twitterAuthClient;
    // wechat
    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        //CommonUtils.showProgress(this, "登录中...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wechatCode != null) {
            NetManager.getInstance().oauthWechat(wechatCode, "authorization_code").enqueue(new Callback<WechatResp>() {
                @Override
                public void onResponse(Call<WechatResp> call, Response<WechatResp> response) {
                    WechatResp wechatResp = response.body();
                    Log.d("wechat", response.toString());
                    thirdPartyLogin("1", wechatResp.getOpenid());
                }

                @Override
                public void onFailure(Call<WechatResp> call, Throwable t) {

                }
            });

            /*new RequestCallback(new RequestCallback.Callback() {
                    @Override
                    public void success(Resp resp) {
                        Gson gson = new Gson();
                        WechatResp wechatResp = gson.fromJson(gson.toJsonTree(resp.getData()), WechatResp.class);
                        thirdPartyLogin("1", wechatResp.getOpenid());
                    }

                    @Override
                    public boolean fail(Resp resp) {
                        return false;
                    }
                }));*/
            wechatCode = null;
        }
    }

    @OnClick(R.id.wechatBtn)
    public void onWechatClick() {
        //api注册
        if (api == null) {
            api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
            api.registerApp(Constants.APP_ID);
        }

        SendAuth.Req req = new SendAuth.Req();

        //授权读取用户信息
        req.scope = "snsapi_userinfo";
        //自定义信息
        req.state = "wechat_sdk_demo_test";
        //向微信发送请求
        api.sendReq(req);
    }

    @OnClick(R.id.wbBtn)
    public void onWeiboClick() {
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
                    Log.d(TAG, "facebookUid = " + facebookUid);
                    thirdPartyLogin("2", facebookUid);
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
            thirdPartyLogin("2", facebookUid);
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
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    thirdPartyLogin("3", String.valueOf(session.getUserId()));
                }

                @Override
                public void failure(TwitterException exception) {
                    Log.d("TwitterKit", "Login with Twitter failure", exception);
                }
            });
        } else {
            String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            thirdPartyLogin("3", String.valueOf(session.getUserId()));
        }
    }

    private void thirdPartyLogin(final String platform, final String openId) {
        NetManager.getInstance()
            .thirdPartyLogin(platform, openId)
            .enqueue(new RequestCallback(new RequestCallback.Callback() {
                @Override
                public void success(Resp resp) {
                    Gson gson = new Gson();
                    ThirdParty thirdParty = gson.fromJson(gson.toJsonTree(resp.getData()), ThirdParty.class);
                    NetManager.getInstance().setToken(thirdParty.getAccessToken());
                    NetManager.getInstance().setUid(thirdParty.getUser().getId());
                    if (thirdParty.getUser().isFirst()) {
                        startActivity(new Intent(LoginActivity.this, CompleteInfoActivity.class));
                    } else {
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    }

                    SharePreferenceHelper.getInstance().put(SharePrefsConstants.IS_LOGIN, true);
                    SharePreferenceHelper.getInstance().put(SharePrefsConstants.PLATFORM, platform);
                    SharePreferenceHelper.getInstance().put(SharePrefsConstants.PLATFORM_USER_ID, openId);

                    finish();
                }

                @Override
                public boolean fail(Resp resp) {
                    return false;
                }
            }));
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
