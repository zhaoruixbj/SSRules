package com.twiceyuan.ssrules;

import android.app.Application;

import com.twiceyuan.ssrules.helper.Preferences;

/**
 * Created by twiceYuan on 04/12/2016.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */

public class App extends Application {

    private static App sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

        Preferences.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        sInstance = null;
    }

    public static App get() {
        return sInstance;
    }
}
