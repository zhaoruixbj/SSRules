package com.twiceyuan.ssrules.ui.common;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.twiceyuan.autofinder.AutoFinder;
import com.twiceyuan.ssrules.R;
import com.twiceyuan.ssrules.constants.Filters;
import com.twiceyuan.ssrules.model.AclItem;
import com.twiceyuan.ssrules.widget.EditorDialogHolder;

import java.util.List;

import rx.functions.Action0;
import rx.functions.Action2;

import static android.content.Context.CLIPBOARD_SERVICE;

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
                .setPositiveButton("删除该规则", (dialogInterface, i) -> deleteCallback.call())
                .setNeutralButton("编辑", (dialogInterface, i) -> editCallback.call())
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

    /**
     * 编辑 ACL 并且返回的值作用到适配器中（保存时由适配器生成）
     *
     * @param activity     activity
     * @param item         需要编辑的 ACL item
     * @param editCallback 编辑完成的回调，第一个代表编辑完的对象，第二个代表编辑完之后的规则放在哪个规则下面
     *                     主要是方便新建模式时，决定该规则属于哪个属性下。
     */
    public static void editAcl(Activity activity,
                               AclItem item,
                               List<String> typeList,
                               Action2<AclItem, String> editCallback) {

        if (item == null) {
            // 新建模式
            item = new AclItem("");
        } else {
            // 更新模式
        }

        View rootView = View.inflate(activity, R.layout.dialog_edit_acl, null);

        EditorDialogHolder holder = new EditorDialogHolder();
        AutoFinder.find(holder, rootView);

        holder.et_host.setText(item.getHost());
        holder.et_rule.setText(item.content);
        holder.spinner.setAdapter(new ArrayAdapter<>(activity, R.layout.support_simple_spinner_dropdown_item, typeList));

        final String[] selectedType = {typeList.get(0)};

        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedType[0] = (String) adapterView.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        holder.et_host.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    return;
                }

                String text = "(.*\\.)?" + editable.toString().replaceAll("\\.", "\\\\.");
                holder.et_rule.setText(text);
            }
        });

        final AclItem selectedItem = item;

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setView(rootView)
                .setPositiveButton("确定", (d, i) -> {
                    selectedItem.content = holder.et_rule.getText().toString();
                    editCallback.call(selectedItem, selectedType[0]);
                })
                .setNeutralButton("从粘贴板识别", null)
                .setNegativeButton("取消", null)
                .setCancelable(false)
                .create();

        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(view -> {
            // 获取 剪切板数据
            ClipboardManager cm = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
            ClipData cd = cm.getPrimaryClip();
            String clipString = cd.getItemAt(0).getText().toString();
            if (Filters.URL.matcher(clipString).matches()) {
                holder.et_host.setText(Uri.parse(clipString).getHost());
            }
        });
    }
}
