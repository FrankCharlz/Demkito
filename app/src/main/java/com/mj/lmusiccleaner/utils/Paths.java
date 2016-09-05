package com.mj.lmusiccleaner.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import com.mj.lmusiccleaner.MainActivity;

import java.io.File;

/**
 * Created by Frank on 1/15/2016.
 * Find file paths from the intent
 * --uri way
 * --stream way
 */
public class Paths {


    public static final String FOLDER_NAME = "/Demkito/";

    public static String getPathFromMediaStoreQuery(Context context, Uri uri) {
        Utils.log("Finding path MEDIA QUERY METHOD : "+uri.toString());
        //Utils.log("mime type : "+context.getContentResolver().getType(uri));
        Cursor cursor = null;
        try {
            String[] projections = {MediaStore.Audio.Media.DATA};
            cursor = context.getContentResolver().query(uri, projections, null, null, null);
            int index;
            if (cursor != null) {
                index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(index);
            }
            return null;
        } catch (Exception e){
            e.printStackTrace();
            Utils.log("error when querying : "+e.getMessage());
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String getPathFromUri(Uri uri) {
        Utils.log("Finding path URI METHOD");
        if (uri == null) return null;
        File file = new File(uri.getPath());
        if (!file.exists()) return null;
        return  file.getAbsolutePath();
    }

    private static  Uri getUri(Intent intent){
        Uri uri = intent.getData();
        if (uri == null) uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        return  uri;
    }

    public static String getPath(Context context, Intent intent) {
        Uri uri = getUri(intent);
        if (uri == null) return null;
        String path = getPathFromUri(uri);
        if (path == null) path = getPathFromMediaStoreQuery(context, uri);
        if (path == null) {
            Utils.log("Failed to get path .:)");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) path = SO.getPath(context, intent.getData());
            Utils.log("Path from SO for > kitkat :)" + path);
        }
        else Utils.log(path);
        return path;
    }
}
