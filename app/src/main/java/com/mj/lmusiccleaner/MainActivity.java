package com.mj.lmusiccleaner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mj.lmusiccleaner.utils.Paths;
import com.mj.lmusiccleaner.utils.Prefs;
import com.mj.lmusiccleaner.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private static final int RQ_CODE = 2;
    public static final String TO_DETAILS = "8HJk";
    private ImageView btnAdd;
    private TextView tvIns, tvSongs, tvBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        doStats();

        String appname = getResources().getString(R.string.app_name);

        String instructions = "click the + button above<br/>" +
                "&mdash;OR&mdash;<br/>" +
                "<b>browse</b> your <b>file manager</b> for the song, <b>click share</b>"+
                " then select <b><u>"+appname+"</u></b>";

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
        float cleanedBytes = Prefs.getCleanedBytes(this);

        String megabytes_str = String.format("%.1f", cleanedBytes);

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
            toast("Sorry, we couldn\'t process the selected file");
            return;
        }

        if (!path.endsWith("mp3")) {
            Utils.log("Not an mp3 file");
            toast("Sorry, the selected file is not an mp3 file.");
            return;
        }

        //proceed to start DetailsActivity
        Intent toDetailsIntent = new Intent(this, DetailsActivity.class);
        toDetailsIntent.putExtra(MainActivity.TO_DETAILS, path);
        startActivity(toDetailsIntent);

    }

    private void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        doStats();
    }

    public void openCleanedSongsActivity(View view) {
        Toast.makeText(this, "Still working on something, coming in full package soon!", Toast.LENGTH_LONG).show();
        //startActivity(new Intent(this, ListCleanedSongsActvity.class));
    }
}
