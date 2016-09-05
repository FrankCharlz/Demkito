package com.mj.lmusiccleaner;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mj.lmusiccleaner.music.Player;
import com.mj.lmusiccleaner.music.Song;
import com.mj.lmusiccleaner.utils.DopeTextView;
import com.mj.lmusiccleaner.utils.Utils;
import com.mj.lmusiccleaner.utils.Prefs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;

/**
 * Created by Frank on 1/19/2016.
 *
 * In God we trust
 */
public class DetailsActivity extends AppCompatActivity {

    private DopeTextView tvDetails;
    private Player player;
    private DopeTextView btnRemoveAds;
    private Song song;
    private File original_file;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        initViews();

        String path = getIntent().getStringExtra(MainActivity.TO_DETAILS);

        //String path = "/storage/sdcard0/Music/test3.mp3";

        original_file = new File(path);
        Utils.log(original_file.getAbsolutePath());

        song = new Song(this, original_file);
        song.solve();
        Utils.log("soluble : " + song.isItSoluble() + "\ncut frame : " + song.getCutFrame());

        String details = "Song name : "+original_file.getName().substring(0, original_file.getName().length() - 4) +
                "\nDirty seconds : "+ String.format("%.2f", song.getCutSecond()*1.0f/1000) +
                "\n\n";

        player = new Player(this);
        player.setSong(original_file);
        player.setCutAtSecond(song.getCutSecond());

        tvDetails.setText(details);


    }

    private void initViews() {
        tvDetails = (DopeTextView) findViewById(R.id.tv_song_details);
        btnRemoveAds = (DopeTextView) findViewById(R.id.btnRemoveAds);

        btnRemoveAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeAds();
            }
        });

    }

    private void removeAds() {

        try {
            player.kill();

            song.clean();

            File clean_file = song.getCleanFile();
            player.setSong(clean_file);
            player.setCutAtSecond(0);

            float uchafu = original_file.length() - clean_file.length(); //bytes
            uchafu = uchafu / 1024.0f / 1024.0f;

            Prefs.incrementCleanedBytes(this, uchafu);
            Prefs.incrementCleanedSongs(this);

            tvDetails.setText(R.string.ads_removed_message);
            btnRemoveAds.setVisibility(View.GONE);

            FileOutputStream fos = openFileOutput("_cleaned_songs_db_", Context.MODE_APPEND);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(clean_file.getAbsolutePath().trim());
            bw.newLine();
            bw.flush();
            bw.close();

        } catch (Exception e) {
            //// TODO: 5/22/2016 laminate this...
            Utils.log("An error occured could not remove ads.");
            Utils.log(e.getLocalizedMessage());
            e.printStackTrace();

            player.setSong(original_file);
            player.setCutAtSecond(song.getCutSecond());

            tvDetails.setText(R.string.ads_removing_failed_message);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (player != null) {
            player.kill();
        }
    }
}
