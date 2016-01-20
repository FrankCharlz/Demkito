package com.mj.lmusiccleaner.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.mj.lmusiccleaner.MainActivity;

/**
 * Created by Frank on 1/19/2016.
 */
public class Prefs {

    private static final String PREFS_FILE = "your_data";
    private static final String CLEANED_SONGS = "n7jbn";
    private static final String CLEANED_BYTES = "7jF0";

    public static int getCleanedSongs(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        return preferences.getInt(CLEANED_SONGS, 0);
    }

    public static void incrementCleanedSongs(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        int cn = preferences.getInt(CLEANED_SONGS, 0);
        preferences.edit().putInt(CLEANED_SONGS, cn + 1).apply();
    }

    public static int getCleanedBytes(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        return preferences.getInt(CLEANED_BYTES, 0);
    }

    public static void incrementCleanedBytes(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        int cn = preferences.getInt(CLEANED_BYTES, 0);
        preferences.edit().putInt(CLEANED_BYTES, cn + 1).apply();
    }
}
