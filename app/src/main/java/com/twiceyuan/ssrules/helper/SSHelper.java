package com.twiceyuan.ssrules.helper;

import com.chrisplus.rootmanager.RootManager;

/**
 * Created by twiceYuan on 08/12/2016.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
public class SSHelper {

    public static void restartShadowsocks() {
        RootManager.getInstance().runCommand("kill -9 $(pidof ss-local)");
    }
}
