package com.derek.doraemon;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.derek.doraemon.constants.Constants;
import com.derek.doraemon.constants.SharePrefsConstants;
import com.derek.doraemon.utils.SharePreferenceHelper;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import java.io.File;

import io.fabric.sdk.android.Fabric;

/**
 * Created by derek on 2016/9/28.
 */
public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        SharePreferenceHelper.getInstance()
            .init(SharePrefsConstants.SHARE_PREFS_NAME, this.getApplicationContext());

        // facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        // twitter
        TwitterAuthConfig authConfig = new TwitterAuthConfig("foJvwgdJreZtPkfKOXiQBV9nJ", "ZFiugQJDKidG6eYMFJh3h6aVvhGAnTNfSBX449u1q6CN9BXZoP");
        Fabric.with(this, new TwitterCore(authConfig));

        // baidu map
        SDKInitializer.initialize(getApplicationContext());

        //
        File dir = new File(Constants.RECORDS_FOLDER);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static Context getContext() {
        return mInstance;
    }
}
