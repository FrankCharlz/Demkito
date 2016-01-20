package com.mj.lmusiccleaner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mj.lmusiccleaner.music.Player;
import com.mj.lmusiccleaner.music.Song;
import com.mj.lmusiccleaner.utils.DopeTextView;
import com.mj.lmusiccleaner.utils.Utils;

import java.io.File;

/**
 * Created by Frank on 1/19/2016.
 *
 * In God we trust
 */
public class DetailsActivity extends AppCompatActivity {

    private DopeTextView tvDetails;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        initViews();


        Toast.makeText(this, "Details activity", Toast.LENGTH_SHORT).show();

        String path = getIntent().getStringExtra(MainActivity.PATH_TO_DETAILS);


        File file = new File(path);
        Utils.log(file.getAbsolutePath());

        Song song = new Song(this, file);
        song.solve();
        Utils.log("soluble : " + song.isItSoluble() + "\ncut frame : " + song.getCutFrame());

        String details = "Song name : "+file.getName().substring(0, file.getName().length()-4) +
                        "\nCut point : "+ song.getCutFrame() +
                        "\n\n";

        Player player = new Player(this);
        player.setSong(file);

        tvDetails.setText(details);
        Toast.makeText(this, details, Toast.LENGTH_SHORT).show();


    }

    private void initViews() {
        tvDetails = (DopeTextView) findViewById(R.id.tv_song_details);

    }
}
