package com.twiceyuan.ssrules.utils;

import android.annotation.SuppressLint;
import android.widget.Toast;

import com.chrisplus.rootmanager.RootManager;
import com.twiceyuan.ssrules.App;
import com.twiceyuan.ssrules.constants.ACLs;
import com.twiceyuan.ssrules.constants.Path;
import com.twiceyuan.ssrules.model.AclFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by twiceYuan on 02/12/2016.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */

public class Utils {

    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static List<AclFile> getAllAclFiles() {
        List<AclFile> aclFiles = new ArrayList<>();

        String result = RootManager.getInstance()
                .runCommand("cd " + Path.SS_PATH + "&& ls -il *.acl")
                .getMessage();

        String[] lines = result.split("\n");
        for (String line : lines) {
            String[] words = line.split(" +");
            String fileName = matchFileName(words[8]);
            if (fileName == null) {
                continue;
            }

            AclFile file = new AclFile();
            file.fileName = fileName;
            file.filePath = Path.SS_PATH + "/" + words[8];
            file.fileSize = Long.parseLong(words[5]);
            file.lastUpdate = parseTime(words[6] + " " + words[7]);
            aclFiles.add(file);
        }
        return aclFiles;
    }

    private static long parseTime(String source) {
        try {
            return mDateFormat.parse(source).getTime();
        } catch (ParseException e) {
            return 0L;
        }
    }

    private static String matchFileName(String filePath) {
        for (String fileName : ACLs.fileNameMap.keySet()) {
            if (filePath.contains(fileName)) {
                return ACLs.fileNameMap.get(fileName);
            }
        }
        return null;
    }

    public static Observable<List<String>> readFile(final String filePath) {
        return Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                RootManager manager = RootManager.getInstance();
                if (manager.remount(Path.SS_PATH, "rw")) {
                    String[] split = manager.runCommand("cat " + filePath).getMessage().split("\n");
                    subscriber.onNext(Arrays.asList(split));
                }
            }
        });
    }

    public static int getPx(float dpValue) {
        final float scale = App.get().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int getDp(float pxValue) {
        final float scale = App.get().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static void toast(String message) {
        Toast.makeText(App.get(), message, Toast.LENGTH_SHORT).show();
    }

    public static void restartShadowsocks() {
        RootManager.getInstance().runCommand("kill -9 $(pidof ss-local)");
    }
}
