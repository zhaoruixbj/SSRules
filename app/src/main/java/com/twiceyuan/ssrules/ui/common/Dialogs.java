package com.twiceyuan.ssrules.ui.common;

import android.app.Activity;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;

import com.twiceyuan.autofinder.AutoFinder;
import com.twiceyuan.ssrules.R;
import com.twiceyuan.ssrules.constants.Filters;
import com.twiceyuan.ssrules.constants.Formats;
import com.twiceyuan.ssrules.helper.ViewHelper;
import com.twiceyuan.ssrules.model.AclItem;
import com.twiceyuan.ssrules.utils.Utils;
import com.twiceyuan.ssrules.widget.EditorDialogHolder;

import java.util.List;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action2;

/**
 * Created by twiceYuan on 04/12/2016.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */

public class Dialogs {

    public static void aclItemDialog(Activity activity, AclItem item, Action0 deleteCallback) {
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setMessage(String.format("" +
                                "内容: %s\n" +
                                "类型: %s\n" +
                                "地址: %s\n" +
                                (item.getMask() == -1 ? "" : "掩码: " + item.getMask()),
                        item.content,
                        item.getTypeName(),
                        item.getHost()))
                .setPositiveButton("删除该规则", (dialogInterface, i) -> deleteCallback.call())
                .setNegativeButton("取消", null)
                .create();
        dialog.show();
    }

    /**
     * 编辑 ACL 并且返回的值作用到适配器中（保存时由适配器生成）
     *
     * @param activity     activity
     * @param editCallback 编辑完成的回调，第一个代表编辑完的对象，第二个代表编辑完之后的规则放在哪个规则下面
     *                     主要是方便新建模式时，决定该规则属于哪个属性下。
     */
    public static void newAcl(Activity activity, List<String> ruleList, List<String> typeList, Action2<AclItem, String> editCallback) {

        AclItem item = new AclItem("");

        View rootView = View.inflate(activity, R.layout.dialog_edit_acl, null);

        EditorDialogHolder holder = new EditorDialogHolder();
        AutoFinder.find(holder, rootView);

        holder.et_host.setText(item.getHost());
        holder.et_rule.setText(item.content);

        final String[] selectedType = {""};

        if (typeList != null && typeList.size() != 0) {
            selectedType[0] = typeList.get(0);
            List<String> typeName = Observable.from(typeList)
                    .map(Filters.TYPE::get)
                    .toList().toBlocking().first();

            holder.spinner.setAdapter(new ArrayAdapter<>(activity,
                    R.layout.support_simple_spinner_dropdown_item,
                    typeName));
            ViewHelper.spinnerSelect(holder.spinner, position -> selectedType[0] = typeList.get(position));
        } else {
            holder.spinner.setVisibility(View.GONE);
        }

        ViewHelper.watchText(holder.et_host, s -> {
            if (!Formats.isURL(s)) {
                return;
            }
            String text = "(.*\\.)?" + s.replaceAll("\\.", "\\\\.");
            holder.et_rule.setText(text);
        });

        final AclItem selectedItem = item;

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setView(rootView)
                .setPositiveButton("确定", null)
                .setNeutralButton("从粘贴板识别", null)
                .setNegativeButton("取消", null)
                .setCancelable(false)
                .create();

        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(view -> {
            // 获取 剪切板数据
            String clipString = Utils.readClipBoard();
            if (Formats.isURL(clipString)) {
                holder.et_host.setText(Uri.parse(clipString).getHost());
            } else {
                Utils.toast("剪切板里好像不是 URL 哦？");
            }
        });

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(view -> {
            String host = holder.et_host.getText().toString();

            if (!host.startsWith("http://") && !host.startsWith("https://")) {
                host = "http://" + host;
            }

            if (!Formats.isURL(host) || holder.et_rule.length() == 0) {
                Utils.toast("请输入正确的域名，例如 google.com");
                return;
            }

            if (ruleList.contains(holder.et_rule.getText().toString())) {
                Utils.toast("该规则已存在");
                return;
            }

            selectedItem.content = holder.et_rule.getText().toString();
            editCallback.call(selectedItem, selectedType[0]);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        });
    }
}
