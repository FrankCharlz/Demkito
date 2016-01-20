package com.mj.lmusiccleaner.music;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.mj.lmusiccleaner.DetailsActivity;
import com.mj.lmusiccleaner.R;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Handler;

import at.markushi.ui.CircleButton;

public class Player implements
        View.OnClickListener,
        View.OnTouchListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener {

    private final AppCompatActivity parentActivity;
    private final Drawable playDrawable, pauseDrawable;
    private CircleButton btnPlayPause;
    private EditText edUrl;
    private SeekBar seekBar;

    private MediaPlayer mediaPlayer;
    private android.os.Handler handler = new android.os.Handler();

    private int song_length = 1;
    private File song;

    public Player(AppCompatActivity parentActivity) {
        this.parentActivity = parentActivity;
        initView();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            playDrawable = parentActivity.getDrawable(R.drawable.ic_action_play_arrow);
            pauseDrawable = parentActivity.getDrawable(R.drawable.ic_action_pause);
        } else  {
            playDrawable = parentActivity.getResources().getDrawable(R.drawable.ic_action_play_arrow);
            pauseDrawable = parentActivity.getResources().getDrawable(R.drawable.ic_action_pause);
        }


    }

    private void initView() {
        btnPlayPause = (CircleButton) parentActivity.findViewById(R.id.btn_play_pause);
        seekBar = (SeekBar) parentActivity.findViewById(R.id.seekbar);

        btnPlayPause.setOnClickListener(this);
        seekBar.setMax(100);
        seekBar.setOnTouchListener(this);



    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int progress) {
        seekBar.setSecondaryProgress(progress);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_play_pause) { playOrPause(); }
    }

    private void playOrPause() {

        /***
         * TODO: Something is fishy here.
         */
        try {
            mediaPlayer.setDataSource(song.getAbsolutePath());
            mediaPlayer.prepare();
            song_length = mediaPlayer.getDuration();

        } catch (Exception e) {
            Log.e("----007", "Data src fail : "+ e.getLocalizedMessage());
            e.printStackTrace();
        }

        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            btnPlayPause.setImageDrawable(pauseDrawable);

        } else {
            mediaPlayer.pause();
            btnPlayPause.setImageDrawable(playDrawable);
        }

        seekBarPrimaryProgressUpdate();
    }

    private void seekBarPrimaryProgressUpdate() {
        int progress = (int)((float)100*mediaPlayer.getCurrentPosition()/song_length);
        seekBar.setProgress(progress);

        if (mediaPlayer.isPlaying()) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    seekBarPrimaryProgressUpdate(); //calls itself evry 1 second
                }
            };
            handler.postDelayed(r, 1000);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        btnPlayPause.setImageDrawable(playDrawable);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view.getId() == R.id.seekbar) {
            if (mediaPlayer.isPlaying()) {
                int song_pos =(int)(((float)seekBar.getProgress()/100)*song_length);
                mediaPlayer.seekTo(song_pos);
            }
        }

        return false;
    }

    public void setSong(File song) {
        this.song = song;
    }
}
