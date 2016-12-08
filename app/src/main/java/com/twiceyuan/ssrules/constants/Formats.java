package com.twiceyuan.ssrules.constants;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.text.TextUtils;

import java.text.SimpleDateFormat;

/**
 * Created by twiceYuan on 08/12/2016.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */

public class Formats {

    public static boolean isURL(String url) {
        if (url == null) return false;
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        try {
            Uri uri = Uri.parse(url);
            String host = uri.getHost();
            return !TextUtils.isEmpty(host);
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * 文件日期格式
     */
    @SuppressLint("SimpleDateFormat") public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
}
