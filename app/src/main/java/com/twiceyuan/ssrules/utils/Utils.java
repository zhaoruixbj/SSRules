package com.twiceyuan.ssrules.utils;

import android.annotation.SuppressLint;
import android.widget.Toast;

import com.chrisplus.rootmanager.RootManager;
import com.twiceyuan.ssrules.App;
import com.twiceyuan.ssrules.constants.ACLs;
import com.twiceyuan.ssrules.func.Callback;
import com.twiceyuan.ssrules.model.AclFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
                .runCommand("ls -il /data/data/com.github.shadowsocks/*.acl")
                .getMessage();

        String[] lines = result.split("\n");
        for (String line : lines) {
            line = line.replaceAll("  ", " ");
            line = line.replaceAll("  ", " ");
            String[] words = line.split(" ");

            String fileName = matchFileName(words[8]);
            if (fileName == null) {
                continue;
            }

            AclFile file = new AclFile();
            file.fileName = fileName;
            file.filePath = words[8];
            file.fileSize = Long.parseLong(words[5]);
            file.lastUpdate = parseTime(words[6] + " " + words[7]);
            aclFiles.add(file);
        }
        return aclFiles;
    }

    private static void runCommandSync(String command, Callback<String> resultCallback) {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"su", "-c", command});
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder resultBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                resultBuilder.append(line);
            }
            if (resultBuilder.toString().length() != 0) {
                resultCallback.call(resultBuilder.toString());
                reader.close();
                inputStream.close();
                return;
            }
            InputStream errorStream = process.getErrorStream();
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
            StringBuilder errorBuilder = new StringBuilder();
            while ((line = errorReader.readLine()) != null) {
                errorBuilder.append(line);
            }
            resultCallback.call(errorBuilder.toString());
        } catch (IOException e) {
            resultCallback.call(e.getMessage());
        }
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
}
