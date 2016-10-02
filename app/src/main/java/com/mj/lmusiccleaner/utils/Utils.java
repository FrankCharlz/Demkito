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

    private static final String APP_FOLDER_NAME = "demkito";

    public static File getAppDirectory() {
        File file = new File(Environment.getExternalStorageDirectory(), APP_FOLDER_NAME);
        if (!file.mkdirs()) {
            Utils.log("Directory not created, maybe it exists");
        }
        return file;
    }

    public static void log(String str) {
        Log.e("demkito", str);
    }
}
