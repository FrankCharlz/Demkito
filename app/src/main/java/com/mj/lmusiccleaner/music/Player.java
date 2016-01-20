package com.mj.lmusiccleaner.music;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.mj.lmusiccleaner.R;

import java.io.File;

import at.markushi.ui.CircleButton;

public class Player implements
        View.OnTouchListener,
        MediaPlayer.OnCompletionListener{

    private static final int SEEK_BAR_X_SCALE = 5;
    private static final long THREAD_LOOP_SECONDS = 200;
    private final AppCompatActivity parentActivity;
    private Drawable playDrawable, pauseDrawable;
    private ColorDrawable color_red, color_green;
    private CircleButton btnPlayPause;
    private SeekBar seekBar;

    private MediaPlayer mediaPlayer;
    private android.os.Handler handler = new android.os.Handler();

    private int song_length = 1;
    private File song;
    private int cutAtSecond;
    private LinearLayout playerContainer;
    private Runnable progressRunnable;

    public Player(AppCompatActivity parentActivity) {
        this.parentActivity = parentActivity;
        initView();
        initResources();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);


    }

    private  void initResources() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            playDrawable = parentActivity.getDrawable(R.drawable.ic_action_play_arrow);
            pauseDrawable = parentActivity.getDrawable(R.drawable.ic_action_pause);
        } else  {
            playDrawable = parentActivity.getResources().getDrawable(R.drawable.ic_action_play_arrow);
            pauseDrawable = parentActivity.getResources().getDrawable(R.drawable.ic_action_pause);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color_green = new ColorDrawable(parentActivity.getColor(R.color.green));
            color_red = new ColorDrawable(parentActivity.getColor(R.color.red));
        } else  {
            color_green = new ColorDrawable(parentActivity.getResources().getColor(R.color.green));
            color_red = new ColorDrawable(parentActivity.getResources().getColor(R.color.red));
        }


    }

    private void initView() {
        seekBar = (SeekBar) parentActivity.findViewById(R.id.seekbar);
        seekBar.setMax(100);
        seekBar.setOnTouchListener(this);

        playerContainer = (LinearLayout)parentActivity.findViewById(R.id.player_container);

        btnPlayPause = (CircleButton) parentActivity.findViewById(R.id.btn_play_pause);
        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playOrPause();
            }
        });

    }


    private void playOrPause() {

        /***
         * TODO: Something is fishy here.
         */
        try {
            mediaPlayer.setDataSource(song.getAbsolutePath());
            mediaPlayer.prepare();
            song_length = mediaPlayer.getDuration();
            song_length = song_length/SEEK_BAR_X_SCALE; //to see progress clearer to the cut point

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
        if (mediaPlayer.isPlaying()) {

            int progress = (int)((float)100*mediaPlayer.getCurrentPosition()/song_length);
            seekBar.setProgress(progress);
            progressColors(progress);

            progressRunnable = new Runnable() {
                @Override
                public void run() {
                    seekBarPrimaryProgressUpdate(); //calls itself evry 1 second
                }
            };
            handler.postDelayed(progressRunnable, THREAD_LOOP_SECONDS);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void progressColors(int progress) {
        if (cutAtSecond > 0) {
            if (mediaPlayer.getCurrentPosition() > cutAtSecond) {
                playerContainer.setBackground(color_green);
            } else {
                playerContainer.setBackground(color_red);
            }
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

    public void kill() {
        handler.removeCallbacks(progressRunnable); //stopping the thread...
        if (mediaPlayer.isPlaying()) {
            seekBar.setProgress(0);
            mediaPlayer.pause();
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    public void setCutAtSecond(int cutAtSecond) {
        this.cutAtSecond = cutAtSecond;
    }

    public void refresh() {
        //does nothing..
    }
}
