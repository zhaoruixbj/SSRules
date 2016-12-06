package com.twiceyuan.ssrules.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.chrisplus.rootmanager.RootManager;
import com.twiceyuan.ssrules.App;
import com.twiceyuan.ssrules.constants.ACLs;
import com.twiceyuan.ssrules.constants.Filters;
import com.twiceyuan.ssrules.constants.Path;
import com.twiceyuan.ssrules.helper.Preferences;
import com.twiceyuan.ssrules.model.AclFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

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

    /**
     * 获得 ACL 文件中所有的 Type（一般只有一种）
     *
     * @param aclContent 全部文件内容
     * @return 文件中所有的规则类型
     */
    public static List<String> getTypes(List<String> aclContent) {
        List<String> types = new ArrayList<>();
        //noinspection Convert2streamapi
        for (String line : aclContent) {
            String trim = line.trim();
            if (Filters.TYPE.keySet().contains(trim)) {
                types.add(trim);
            }
        }
        return types;
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

    /**
     * 添加一条规则
     *
     * @param aclContent 规则内容
     * @param filePath   规则文件路径
     * @param typeAnchor 规则类型（作为插入锚点）
     */
    public static Observable<Void> insertAcl(Context context, String aclContent, String filePath, String typeAnchor) {
        return Observable.create(subscriber -> {
            readFile(filePath).subscribe(strings -> {
                strings = new ArrayList<>(strings);
                int anchor = strings.indexOf(typeAnchor) + 1;
                strings.add(anchor, aclContent);
                saveToFile(context, strings, filePath);

                // 保存最后一次记录
                Preferences.putSetting(Preferences.Key.LAST_FILE, filePath);
                Preferences.putSetting(Preferences.Key.LAST_TYPE, typeAnchor);

                subscriber.onNext(null);
                subscriber.onCompleted();
            });
        });
    }

    public static void saveToFile(Context context, List<String> data, String filePath) {

        File tempFile = new File(context.getExternalCacheDir(), "tmp.acl");
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
            manager.runCommand("cat " + filePath + " > " + filePath + ".bak");
            manager.runCommand("cat " + tempFile.getAbsolutePath() + " > " + filePath);
            manager.runCommand("chmod a-w " + filePath);
        }
        Utils.toast("保存完成");
        Utils.restartShadowsocks();
    }

    public static <T, R> void setDefaultOrFirst(Spinner spinner, R defaultValue, Func1<T, R> converter) {
        SpinnerAdapter adapter = spinner.getAdapter();
        int count = adapter.getCount();

        boolean isHaveDefault = false;
        for (int i = 0; i < count; i++) {
            //noinspection unchecked
            T item = (T) adapter.getItem(i);
            R compare = converter.call(item);
            if (compare.equals(defaultValue)) {
                spinner.setSelection(i);
                isHaveDefault = true;
                break;
            }
        }
        if (!isHaveDefault && count > 0) {
            spinner.setSelection(0);
        }
    }
}
