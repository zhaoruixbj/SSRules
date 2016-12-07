package com.twiceyuan.ssrules.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.twiceyuan.autofinder.AutoFinder;
import com.twiceyuan.ssrules.BuildConfig;
import com.twiceyuan.ssrules.R;
import com.twiceyuan.ssrules.ui.constract.CanBack;

/**
 * Created by twiceYuan on 06/12/2016.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */

public class AboutActivity extends BaseActivity implements CanBack {

    TextView tv_other_info;
    TextView tv_app_info;

    public static void start(Context context) {
        Intent starter = new Intent(context, AboutActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        AutoFinder.find(this);

        tv_app_info.setText(String.format("%s %s(%s)",
                getString(R.string.app_name),
                BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE));

        tv_other_info.setText("" +
                "如果发生了任何意外\n" +
                "可通过清除 Shadowsocks 的应用数据恢复\n" +
                "\n" +
                "项目地址\n" +
                "https://github.com/twiceyuan/SSRules");
    }
}
