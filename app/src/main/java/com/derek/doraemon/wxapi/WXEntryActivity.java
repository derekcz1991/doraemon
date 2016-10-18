package com.derek.doraemon.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.derek.doraemon.activity.LoginActivity;
import com.derek.doraemon.utils.CommonUtils;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IWXAPI api = WXAPIFactory.createWXAPI(this, "APP_ID");
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {

        if (resp instanceof SendAuth.Resp) {
            SendAuth.Resp newResp = (SendAuth.Resp) resp;

            //获取微信传回的code
            if (newResp.errCode == 0) {
                String code = newResp.code;
                LoginActivity.wechatCode = code;
            } else {
                CommonUtils.toast("微信登录失败");
            }
            finish();
        }

    }

}