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

import com.futuremind.recyclerviewfastscroll.FastScroller;
import com.twiceyuan.autofinder.AutoFinder;
import com.twiceyuan.ssrules.R;
import com.twiceyuan.ssrules.helper.AclHelper;
import com.twiceyuan.ssrules.model.AclFile;
import com.twiceyuan.ssrules.model.AclItem;
import com.twiceyuan.ssrules.ui.adapters.AclLineHolder;
import com.twiceyuan.ssrules.ui.adapters.SectionAdapter;
import com.twiceyuan.ssrules.ui.common.Dialogs;
import com.twiceyuan.ssrules.ui.constract.CanBack;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by twiceYuan on 02/12/2016.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 *
 * 规则详情界面
 */
public class AclDetailActivity extends BaseActivity implements CanBack {

    private static final String EXTRA_FILE = "file";

    RecyclerView         rv_detail;
    FastScroller         scroller;
    FloatingActionButton fab;

    AclFile      file;
    List<String> typeList;

    SectionAdapter<String, AclLineHolder> mAdapter;

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
                mAdapter.remove(aclItem.content);
                mAdapter.notifyDataSetChanged();
                AclHelper.saveToFile(this, mAdapter.getData(), file.filePath);
            });
        });

        scroller.setRecyclerView(rv_detail);

        fab.setOnClickListener(view -> Dialogs.newAcl(this, mAdapter.getData(), typeList, (item, type) -> {
            int position = mAdapter.getData().indexOf(type) + 1;
            position = position < 0 ? 0 : position;
            mAdapter.getData().add(position, item.content);
            mAdapter.notifyDataSetChanged();
            AclHelper.saveToFile(this, mAdapter.getData(), file.filePath);
        }));
    }

    public void loadFileContent() {
        AclHelper.readFile(file.filePath)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(aclItems -> {
                    mAdapter.clear();
                    mAdapter.addAll(aclItems);
                    mAdapter.notifyDataSetChanged();
                    typeList = AclHelper.getTypes(aclItems);
                });
    }
}
