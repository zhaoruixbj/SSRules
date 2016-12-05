package com.twiceyuan.ssrules.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.twiceyuan.autofinder.AutoFinder;
import com.twiceyuan.commonadapter.library.adapter.CommonAdapter;
import com.twiceyuan.ssrules.R;
import com.twiceyuan.ssrules.ui.adapters.AclFileHolder;
import com.twiceyuan.ssrules.model.AclFile;
import com.twiceyuan.ssrules.utils.Utils;
import com.twiceyuan.ssrules.widget.VerticalItemDecoration;

import java.util.List;

/**
 * Created by twiceYuan on 02/12/2016.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
public class AclListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout refreshLayout;
    RecyclerView       rv_acls;

    private CommonAdapter<AclFile, AclFileHolder> mAdapter;

    public static void start(Context context) {
        Intent starter = new Intent(context, AclListActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acls);

        AutoFinder.find(this);

        rv_acls.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv_acls.addItemDecoration(new VerticalItemDecoration(Utils.getPx(4)));
        rv_acls.setAdapter(mAdapter = new CommonAdapter<>(this, AclFileHolder.class));

        mAdapter.setOnItemClickListener((position, file) -> AclDetailActivity.start(AclListActivity.this, file));

        refreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.post(this::onRefresh);
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        List<AclFile> files = Utils.getAllAclFiles();

        mAdapter.clear();
        mAdapter.addAll(files);
        mAdapter.notifyDataSetChanged();

        new Handler(Looper.getMainLooper()).postDelayed(() -> refreshLayout.setRefreshing(false), 500);
    }
}
