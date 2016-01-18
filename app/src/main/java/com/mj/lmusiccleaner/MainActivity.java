package com.mj.lmusiccleaner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.mj.lmusiccleaner.utils.Paths;
import com.mj.lmusiccleaner.utils.Utils;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int RQ_CODE = 2;
    private ImageView btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        final Intent intent = getIntent();
        if (intent.getAction().contains("MAIN")) {
            //started from the menu...
            //showInstructions();
            Utils.log("Started from the menu");
        } else {
            //started by sharing
            Utils.log("Started by sharing");
            processIntent(intent);
        }

    }

    private void initViews() {
        btnAdd =(ImageView)findViewById(R.id.imgv_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("audio/*");
                startActivityForResult(intent, RQ_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RQ_CODE && intent != null) {
                processIntent(intent);
            }
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    private void processIntent(Intent intent) {
        String path = Paths.getPath(this, intent);
        if (path == null) {
            Utils.log("Couldn't find path, process terminating");
            return;
        }

        if (!path.endsWith("mp3")) {
            Utils.log("Not an mp3 file");
            return;
        }

        File file = new File(path);
        Utils.log(file.getAbsolutePath());

        Song song = new Song(this, file);
        song.solve();
        Utils.log("soluble : "+song.isItSoluble()+"\ncut frame : "+song.getCutFrame());


    }
}
