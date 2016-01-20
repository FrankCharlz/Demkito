package com.mj.lmusiccleaner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.mj.lmusiccleaner.music.Player;
import com.mj.lmusiccleaner.music.Song;
import com.mj.lmusiccleaner.utils.DopeTextView;
import com.mj.lmusiccleaner.utils.Utils;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.File;
import java.io.IOException;

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


        Toast.makeText(this, "Details activity", Toast.LENGTH_SHORT).show();

        //String path = getIntent().getStringExtra(MainActivity.PATH_TO_DETAILS);

        String path = "/storage/sdcard0/Music/test3.mp3";

        original_file = new File(path);
        Utils.log(original_file.getAbsolutePath());

        song = new Song(this, original_file);
        song.solve();
        Utils.log("soluble : " + song.isItSoluble() + "\ncut frame : " + song.getCutFrame());

        String details = "Song name : "+original_file.getName().substring(0, original_file.getName().length() - 4) +
                "\nCut point : "+ song.getCutFrame() +
                "\nCut second : "+ String.format("%.2f", song.getCutSecond()*1.0f/1000) +
                "\n\n";

        player = new Player(this);
        player.setSong(original_file);
        player.setCutAtSecond(song.getCutSecond());

        tvDetails.setText(details);
        Toast.makeText(this, details, Toast.LENGTH_SHORT).show();


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
        if (player != null) {
            player.kill();
        }

        try {
            song.clean();

            File clean_file = song.getCleanFile();
            player.setSong(clean_file);
            player.setCutAtSecond(0);
            player.refresh();

            //player.setCutAtSecond(Integer.MAX_VALUE);

        } catch (Exception e) {
            Utils.log("An error occured could not remove ads.");
            Utils.log(e.getLocalizedMessage());
            e.printStackTrace();

            player.setSong(original_file);
            player.setCutAtSecond(song.getCutSecond());

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
