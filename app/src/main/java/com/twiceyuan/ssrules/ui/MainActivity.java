package com.twiceyuan.ssrules.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.chrisplus.rootmanager.RootManager;
import com.chrisplus.rootmanager.container.Result;
import com.twiceyuan.autofinder.AutoFinder;
import com.twiceyuan.ssrules.R;
import com.twiceyuan.ssrules.constants.Path;

public class MainActivity extends AppCompatActivity {

    TextView tv_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AutoFinder.find(this);

        RootManager rootManager = RootManager.getInstance();

        boolean obtainPermission = rootManager.obtainPermission();

        if (obtainPermission) {
            Result result = rootManager.runCommand("ls " + Path.SS_PATH + "/*.acl");
            String message = result.getMessage();
            if (message.contains("No such file or directory")) {
                tv_check.setText(R.string.not_installed);
            } else {
                AclListActivity.start(this);
                finish();
            }
        } else {
            tv_check.setText(R.string.need_root);
        }
    }
}
