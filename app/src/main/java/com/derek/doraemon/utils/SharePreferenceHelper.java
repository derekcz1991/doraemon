package com.derek.doraemon.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kendy on 15/9/17.
 */
public final class SharePreferenceHelper {

    private static SharePreferenceHelper mInstance = new SharePreferenceHelper();
    private SharedPreferences sps;
    private SharedPreferences.Editor editor;

    private SharePreferenceHelper() {

    }

    public void init(String sharedPrefName, Context context) {
        sps = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
        editor = sps.edit();
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static SharePreferenceHelper getInstance() {
        return mInstance;
    }

    /**
     * Put.
     *
     * @param key the key
     * @param v the v
     */
    public void put(String key, String v) {
        editor.putString(key, v);
        editor.commit();
    }

    /**
     * Put.
     *
     * @param key the key
     * @param v the v
     */
    public void put(String key, int v) {
        editor.putInt(key, v);
        editor.commit();
    }

    /**
     * Put.
     *
     * @param key the key
     * @param v the v
     */
    public void put(String key, boolean v) {
        editor.putBoolean(key, v);
        editor.commit();
    }

    /**
     * Put.
     *
     * @param key the key
     * @param v the v
     */
    public void put(String key, float v) {
        editor.putFloat(key, v);
        editor.commit();
    }

    /**
     * Put.
     *
     * @param key the key
     * @param v the v
     */
    public void put(String key, long v) {
        editor.putLong(key, v);
        editor.commit();
    }

    /**
     * Get int.
     *
     * @param key the key
     * @param v the v
     * @return the int
     */
    public int get(String key, int v) {
        return sps.getInt(key, v);
    }

    /**
     * Get string.
     *
     * @param key the key
     * @param v the v
     * @return the string
     */
    public String get(String key, String v) {
        return sps.getString(key, v);
    }

    /**
     * Get boolean.
     *
     * @param key the key
     * @param v the v
     * @return the boolean
     */
    public boolean get(String key, boolean v) {
        return sps.getBoolean(key, v);
    }

    /**
     * Get float.
     *
     * @param key the key
     * @param v the v
     * @return the float
     */
    public float get(String key, float v) {
        return sps.getFloat(key, v);
    }

    /**
     * Get long.
     *
     * @param key the key
     * @param v the v
     * @return the long
     */
    public long get(String key, long v) {
        return sps.getLong(key, v);
    }

    /**
     * Remove.
     *
     * @param key the key
     */
    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }
}
