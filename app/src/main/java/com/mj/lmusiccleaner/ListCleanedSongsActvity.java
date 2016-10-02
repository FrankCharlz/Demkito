package com.mj.lmusiccleaner;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.mj.lmusiccleaner.utils.Utils;

import java.io.File;

/**
 * Created by Frank on 6/25/2016.
 * Mwamvua kaolewa..
 */
public class ListCleanedSongsActvity extends AppCompatActivity {

    private ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        File folder = Utils.getAppDirectory("demkito");
        File[] files = folder.listFiles();

        String[] names = new String[files.length];

        for (int i = 0; i < files.length; i++) {
            names[i] = files[i].getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, names);

        mListView = (ListView) findViewById(R.id.lv_cleaned_songs);
        mListView.setAdapter(adapter);



    }
}
