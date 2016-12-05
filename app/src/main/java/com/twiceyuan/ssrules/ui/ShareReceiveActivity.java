package com.twiceyuan.ssrules.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.twiceyuan.autofinder.AutoFinder;
import com.twiceyuan.ssrules.R;
import com.twiceyuan.ssrules.constants.Filters;
import com.twiceyuan.ssrules.model.AclFile;
import com.twiceyuan.ssrules.ui.constract.CanBack;
import com.twiceyuan.ssrules.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by twiceYuan on 05/12/2016.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 * <p>
 * 接收分享结果添加到规则
 */
public class ShareReceiveActivity extends BaseActivity implements CanBack {

    private static final int MENU_SUBMIT = 1;

    MaterialEditText et_host;
    MaterialEditText et_rule;
    AppCompatSpinner sp_file;
    AppCompatSpinner sp_type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_receive);

        AutoFinder.find(this);

        initViews();

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            }
        } else {
            Utils.toast("没有解析到 URL");
            finish();
        }
    }

    private void initViews() {
        et_host.addTextChangedListener(new TextWatcher() {
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
                et_rule.setText(text);
            }
        });

        List<AclFile> files = Utils.getAllAclFiles();

        List<String> fileNames = Observable.from(files)
                .map(file -> file.fileName)
                .toList().toBlocking().first();

        sp_file.setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, fileNames));
        sp_file.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Utils.readFile(files.get(position).filePath).subscribe(strings -> {
                    List<String> typeList = new ArrayList<>();
                    for (int i = 0; i < strings.size(); i++) {
                        if (strings.get(i).startsWith("[")) {
                            typeList.add(strings.get(i));
                        }
                    }
                    sp_type.setAdapter(new ArrayAdapter<>(ShareReceiveActivity.this, R.layout.support_simple_spinner_dropdown_item, typeList));
                    sp_type.setVisibility(View.VISIBLE);
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_file.setSelection(0);
    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null && Filters.URL.matcher(sharedText).matches()) {
            String host = Uri.parse(sharedText).getHost();
            et_host.setText(host);
        } else {
            Utils.toast("没有解析到 URL");
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, MENU_SUBMIT, 0, "添加")
                .setIcon(R.drawable.ic_submit_white_24dp)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == MENU_SUBMIT) {

            if (!checkValidate()) {
                return true;
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 检查数据合法性
     */
    private boolean checkValidate() {
        // TODO: 05/12/2016
        return false;
    }
}
