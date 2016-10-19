package com.derek.doraemon.constants;

import android.os.Environment;

/**
 * Created by derek on 16/10/11.
 */
public class Constants {
    public static final String PET_FOLDER = Environment.getExternalStorageDirectory() + "/doraemon/";
    public static final String RECORDS_FOLDER = PET_FOLDER + "audio/";

    public static int PLATFORM_WECHAT = 1;
    public static int PLATFORM_FACEBOOK = 2;
    public static int PLATFORM_WEIBO = 3;
    public static int PLATFORM_TWITTER = 3;

    public static String APP_ID = "wx00e114a7310cf13b";
    public static String SECRET_ID = "b273cac568bad5425cfae3faafcf9703";
}
