package com.mj.lmusiccleaner.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by Frank on 1/13/2016.
 *
 * Shiit
 */
public class Utils {

    public static File getAppDirectory(String folder_name) {
        File file = new File(Environment.getExternalStorageDirectory(), folder_name);
        if (!file.mkdirs()) {
            Utils.log("Directory not created, maybe it exists");
        }
        return file;
    }

    public static void log(String str) {
        Log.e("007m", str);
    }
}
