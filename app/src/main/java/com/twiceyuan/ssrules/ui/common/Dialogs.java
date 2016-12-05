package com.twiceyuan.ssrules.ui.common;

import android.app.Activity;
import android.support.v7.app.AlertDialog;

import com.twiceyuan.ssrules.model.AclItem;

import rx.functions.Action0;

/**
 * Created by twiceYuan on 04/12/2016.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */

public class Dialogs {

    public static void aclItemDialog(Activity activity, AclItem item, Action0 deleteCallback, Action0 editCallback) {
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setMessage(String.format("" +
                                "内容: %s\n" +
                                "类型: %s\n" +
                                "地址: %s\n" +
                                (item.getMask() == -1 ? "" : "掩码: " + item.getMask()),
                        item.content,
                        item.getTypeName(),
                        item.getHost()))
                .setPositiveButton("删除该规则", (dialogInterface, i) -> {
                    deleteCallback.call();
                })
                .setNeutralButton("编辑", (dialogInterface, i) -> {
                    editCallback.call();
                })
                .setNegativeButton("取消", null)
                .create();
        dialog.show();
    }

    public static void alertSave(Activity activity, Action0 saveCallback, Action0 discardCallback) {
        new AlertDialog.Builder(activity)
                .setMessage("当前未保存，是否保存后退出？")
                .setPositiveButton("保存", (dialogInterface, i) -> saveCallback.call())
                .setNegativeButton("舍弃", (dialogInterface, i) -> discardCallback.call())
                .create().show();
    }
}
