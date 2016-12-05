package com.twiceyuan.ssrules.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.chrisplus.rootmanager.RootManager;
import com.futuremind.recyclerviewfastscroll.FastScroller;
import com.twiceyuan.autofinder.AutoFinder;
import com.twiceyuan.ssrules.R;
import com.twiceyuan.ssrules.constants.Path;
import com.twiceyuan.ssrules.model.AclFile;
import com.twiceyuan.ssrules.model.AclItem;
import com.twiceyuan.ssrules.ui.adapters.AclLineHolder;
import com.twiceyuan.ssrules.ui.adapters.SectionAdapter;
import com.twiceyuan.ssrules.ui.common.Dialogs;
import com.twiceyuan.ssrules.ui.constract.CanBack;
import com.twiceyuan.ssrules.utils.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by twiceYuan on 02/12/2016.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
public class AclDetailActivity extends BaseActivity implements CanBack {

    private static final String EXTRA_FILE = "file";

    RecyclerView         rv_detail;
    FastScroller         scroller;
    FloatingActionButton fab;

    AclFile file;
    List<String> typeList;

    SectionAdapter<String, AclLineHolder> mAdapter;

    // 文件是否发生了改变。编辑 ACL、删除规则都会导致该标志的变化
    private boolean isChanged = false;

    public static void start(Context context, AclFile file) {
        Intent starter = new Intent(context, AclDetailActivity.class);
        starter.putExtra(EXTRA_FILE, file);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        AutoFinder.find(this);

        file = (AclFile) getIntent().getSerializableExtra(EXTRA_FILE);

        ActionBar toolbar = getSupportActionBar();

        if (toolbar != null) {
            toolbar.setTitle(R.string.title_acl_detail);
            toolbar.setSubtitle(file.fileName);
        }

        setupViews();
        loadFileContent();
    }

    private void setupViews() {
        rv_detail.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv_detail.setAdapter(mAdapter = new SectionAdapter<String, AclLineHolder>(this, AclLineHolder.class) {

            @Override
            public String getSectionTitle(int position) {
                String item = mAdapter.getItem(position);
                if (TextUtils.isEmpty(item)) {
                    return "";
                }
                return String.valueOf(item.replace("(.*\\.)?", "").charAt(0));
            }
        });

        mAdapter.setOnItemClickListener((position, s) -> {
            if (s == null || s.length() == 0 || s.startsWith("[") || s.startsWith("#") || s.startsWith(" ")) {
                return;
            }

            AclItem aclItem = new AclItem(s);
            Dialogs.aclItemDialog(this, aclItem, () -> {
                isChanged = true;
                mAdapter.remove(aclItem.content);
                mAdapter.notifyDataSetChanged();
            }, () -> {
                Utils.toast("没实现！");
                // Dialogs.editAcl(aclItem);
            });
        });

        scroller.setRecyclerView(rv_detail);

        fab.setOnClickListener(view -> Dialogs.editAcl(this, null, typeList, (item, type) -> {
            int position = mAdapter.getData().indexOf(type) + 1;
            mAdapter.getData().add(position, item.content);
            mAdapter.notifyDataSetChanged();

            isChanged = true;
        }));
    }

    public void loadFileContent() {
        Utils.readFile(file.filePath)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(s -> {
                    mAdapter.clear();
                    mAdapter.addAll(s);
                    mAdapter.notifyDataSetChanged();

                    typeList = Observable.from(s)
                            .filter(line -> line.startsWith("["))
                            .toList().toBlocking().first();
                });
    }

    @Override
    public void onBackPressed() {
        if (isChanged) {
            Dialogs.alertSave(this, () -> {
                saveToFile();
                super.onBackPressed();
            }, super::onBackPressed);
        } else {
            super.onBackPressed();
        }
    }

    private void saveToFile() {
        List<String> data = mAdapter.getData();
        File tempFile = new File(getExternalCacheDir(), "tmp.acl");
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile)));
            //noinspection Convert2streamapi
            for (String line : data) {
                writer.write(line + "\n");
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        RootManager manager = RootManager.getInstance();

        if (manager.remount(Path.SS_PATH, "rw")) {
            manager.runCommand("cat " + file.filePath + " > " + file.filePath + ".bak");
            manager.runCommand("cat " + tempFile.getAbsolutePath() + " > " + file.filePath);
            manager.runCommand("chmod a-w " + file.filePath);
        }
        Utils.toast("保存完成");
        Utils.restartShadowsocks();
    }
}
