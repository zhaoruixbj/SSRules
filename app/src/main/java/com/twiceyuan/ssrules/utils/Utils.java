package com.twiceyuan.ssrules.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.widget.Toast;

import com.twiceyuan.ssrules.App;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by twiceYuan on 02/12/2016.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 * <p>
 * 通用工具类
 */
public class Utils {

    /**
     * Application Context Toast
     *
     * @param message 需要提示的消息
     */
    public static void toast(String message) {
        Toast.makeText(App.get(), message, Toast.LENGTH_SHORT).show();
    }

    public static String readClipBoard() {
        try {
            ClipboardManager cm = (ClipboardManager) App.get().getSystemService(CLIPBOARD_SERVICE);
            ClipData cd = cm.getPrimaryClip();
            return cd.getItemAt(0).getText().toString();
        } catch (Exception ignored) {
            return "";
        }
    }
}
