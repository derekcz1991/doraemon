package com.derek.doraemon.utils;

import android.widget.Toast;
import com.derek.doraemon.MyApplication;

/**
 * Created by derek on 2016/9/29.
 */
public class CommonUtils {

    public static void toast(String msg) {
        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
