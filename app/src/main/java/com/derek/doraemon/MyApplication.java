package com.derek.doraemon;

import android.app.Application;
import android.content.Context;

import com.derek.doraemon.constants.SharePrefsConstants;
import com.derek.doraemon.utils.SharePreferenceHelper;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

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

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    public static Context getContext() {
        return mInstance;
    }
}
