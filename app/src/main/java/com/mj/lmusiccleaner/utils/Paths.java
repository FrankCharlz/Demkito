package com.mj.lmusiccleaner.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.mj.lmusiccleaner.MainActivity;

/**
 * Created by Frank on 1/15/2016.
 * Find file paths from the intent
 * --uri way
 * --stream way
 */
public class Paths {


    public static final String FOLDER_NAME = "L Music Cleaner/";

    public static String getPathFromMediaStoreQuery(Context context, Uri uri) {
        Utils.log("Finding path MEDIA QUERY METHOD");
        Cursor cursor = null;
        try {
            String[] projections = {MediaStore.Audio.Media.DATA};
            cursor = context.getContentResolver().query(uri, projections, null, null, null);
            assert cursor != null;
            int index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(index);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String getPathFromUri(Intent intent) {
        Utils.log("Finding path URI METHOD");
        Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (uri == null) return  null;
        return  uri.getPath();
    }

    public static String getPath(Context context, Intent intent) {
        String path = getPathFromUri(intent);
        if (path == null) path = getPathFromMediaStoreQuery(context, intent.getData());
        if (path == null) Utils.log("Failed to get path");
        else Utils.log(path);
        return path;
    }
}
