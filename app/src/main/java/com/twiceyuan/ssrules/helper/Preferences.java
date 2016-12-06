package com.twiceyuan.ssrules.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by twiceYuan on 04/12/2016.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
public class Preferences {

    private static SharedPreferences sDefaultPreference;

    public static void init(Context context) {
        sDefaultPreference = context.getSharedPreferences("default", Context.MODE_PRIVATE);
    }

    public static Set<String> getSetting(Key key, Set<String> defaultValue) {
        SharedPreferences preferences = sDefaultPreference;
        return preferences.getStringSet(String.valueOf(key.ordinal()), defaultValue);
    }

    public static void putSetting(Key key, boolean value) {
        SharedPreferences preferences = sDefaultPreference;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(String.valueOf(key.ordinal()), value);
        editor.apply();
    }

    public static void putSetting(Key key, int value) {
        SharedPreferences preferences = sDefaultPreference;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(String.valueOf(key.ordinal()), value);
        editor.apply();
    }

    public static void putSetting(Key key, long value) {
        SharedPreferences preferences = sDefaultPreference;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(String.valueOf(key.ordinal()), value);
        editor.apply();
    }

    public static void putSetting(Key key, float value) {
        SharedPreferences preferences = sDefaultPreference;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(String.valueOf(key.ordinal()), value);
        editor.apply();
    }

    public static void putSetting(Key key, String value) {
        SharedPreferences preferences = sDefaultPreference;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(String.valueOf(key.ordinal()), value);
        editor.apply();
    }

    public static void putSetting(Key key, Set<String> value) {
        SharedPreferences preferences = sDefaultPreference;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(String.valueOf(key.ordinal()), value);
        editor.apply();
    }

    public static boolean getSetting(Key key, boolean defaultValue) {
        SharedPreferences preferences = sDefaultPreference;
        return preferences.getBoolean(String.valueOf(key.ordinal()), defaultValue);
    }

    public static int getSetting(Key key, int defaultValue) {
        SharedPreferences preferences = sDefaultPreference;
        return preferences.getInt(String.valueOf(key.ordinal()), defaultValue);
    }

    public static long getSetting(Key key, long defaultValue) {
        SharedPreferences preferences = sDefaultPreference;
        return preferences.getLong(String.valueOf(key.ordinal()), defaultValue);
    }

    public static float getSetting(Key key, float defaultValue) {
        SharedPreferences preferences = sDefaultPreference;
        return preferences.getFloat(String.valueOf(key.ordinal()), defaultValue);
    }

    public static String getSetting(Key key, String defaultValue) {
        SharedPreferences preferences = sDefaultPreference;
        return preferences.getString(String.valueOf(key.ordinal()), defaultValue);
    }

    public enum Key {
        /**
         * 是否第一次启动 - boolean
         */
        FIRST_LAUNCH,

        /**
         * 偏好文件
         */
        LAST_FILE,

        /**
         * 偏好类型
         */
        LAST_TYPE,
    }
}
