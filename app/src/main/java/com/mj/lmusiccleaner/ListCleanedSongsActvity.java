package com.mj.lmusiccleaner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
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
        setContentView(R.layout.activity_cleaned_songs);

        File folder = Utils.getAppDirectory();
        File[] files = folder.listFiles();

        String[] names = new String[files.length];
        String name;
        for (int i = 0; i < files.length; i++) {
            name = files[i].getName();
            if (name.length() > 4) name = name.substring(0, name.length() - 4);
            names[i] = name;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, names);

        mListView = (ListView) findViewById(R.id.lv_cleaned_songs);
        mListView.setAdapter(adapter);



    }
}
