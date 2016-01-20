package com.mj.lmusiccleaner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mj.lmusiccleaner.utils.Paths;
import com.mj.lmusiccleaner.utils.Prefs;
import com.mj.lmusiccleaner.utils.Utils;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int RQ_CODE = 2;
    public static final String PATH_TO_DETAILS = "8HJk";
    private ImageView btnAdd;
    private TextView tvIns, tvSongs, tvBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        doStats();

        String instructions = "click the button above<br/>" +
                "&mdash;OR&mdash;<br/>" +
                "<b>browse</b> to the song <b>click share</b><br/>"+
                "then select <b>L Music Cleaner</b>";

        tvIns.setText(Html.fromHtml(instructions));


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

    private void doStats() {
        int cleanedSongs = Prefs.getCleanedSongs(this);
        int cleanedBytes = Prefs.getCleanedBytes(this);

        float megabytes = cleanedBytes/1024.0f/1024.0f;
        String megabytes_str = String.format("%.1f", megabytes);

        SpannableString statsSongs = new SpannableString(cleanedSongs +  "songs\ncleaned");
        SpannableString statsBytes = new SpannableString(megabytes_str +"MB\nads dumped");

        RelativeSizeSpan big_style = new RelativeSizeSpan(3.6f);

        statsSongs.setSpan(big_style, 0, (""+cleanedSongs).length(), 0);
        statsBytes.setSpan(big_style, 0, (""+megabytes_str).length(), 0);

        tvSongs.setText(statsSongs);
        tvBytes.setText(statsBytes);
    }

    private void initViews() {
        tvSongs = (TextView) findViewById(R.id.tv_stats_songs);
        tvBytes = (TextView) findViewById(R.id.tv_stats_bytes);
        tvIns = (TextView) findViewById(R.id.tv_ins);

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

        //proceed to start DetailsActivity
        Intent toDetailsIntent = new Intent(this, DetailsActivity.class);
        toDetailsIntent.putExtra(MainActivity.PATH_TO_DETAILS, path);
        startActivity(toDetailsIntent);

    }
}
