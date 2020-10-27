package com.david.core.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import androidx.arch.core.util.Function;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Locale;

/**
 * author: Ling Lin
 * created on: 2017/7/28 9:29
 * email: 10525677@qq.com
 * description:
 */

public class FileUtil {

    public static String readTextFileFromAssets(String fileName) throws IOException {
        AssetManager assetManager = ContextUtil.getApplicationContext().getAssets();
        InputStream inputStream = assetManager.open(fileName);
        InputStreamReader inputReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputReader);

        StringBuilder result = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line).append("\t");
        }

        bufferedReader.close();
        inputReader.close();
        inputStream.close();

        return result.toString();
    }

    public static void readSensorData(String fileName, int[] buffer, Function<Integer, Integer> func) {
        try {
            AssetManager assetManager = ContextUtil.getApplicationContext().getAssets();
            InputStream inputStream = assetManager.open(fileName);

            int index = 0;
            int sourceData;

            while ((sourceData = inputStream.read()) != -1) {
                int data = func.apply(sourceData);
                buffer[index] = data;
                buffer[index + buffer.length / 2] = data;
                index++;
            }

            inputStream.close();
        } catch (Throwable e) {
            LoggerUtil.e(e);
        }
    }

    public static void setLocalLanguage(Context context, Locale locale) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, metrics);
    }

    public static boolean makeDirectory(String directory) {
        File file = new File(directory);
        if (!file.exists()) {
            return file.mkdirs();
        }
        return true;
    }

    public static File[] listSortedFile(String path, long startTime, long endTime) {
        FileFilter fileFilter = pathname -> {
            long fileTime = pathname.lastModified();
            return fileTime >= startTime && fileTime < endTime;
        };
        File file = new File(path);
        File[] files = file.listFiles(fileFilter);

        Arrays.sort(files, (o1, o2) -> {
            long diff = o1.lastModified() - o2.lastModified();
            if (diff > 0)
                return -1;
            else if (diff == 0)
                return 0;
            else
                return 1;
        });
        return files;
    }
}