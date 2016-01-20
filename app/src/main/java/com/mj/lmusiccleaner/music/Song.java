package com.mj.lmusiccleaner.music;

import android.content.Context;
import android.os.Environment;

import com.mj.lmusiccleaner.cheapgoogle.CheapMP3;
import com.mj.lmusiccleaner.utils.Paths;
import com.mj.lmusiccleaner.utils.Prefs;
import com.mj.lmusiccleaner.utils.Utils;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Frank on 1/18/2016.
 */


public class Song {

    //one frame is 26.12 millisecond long
    //inspecting from 3rd to 15th second for the lowest volume frame
    //hence 500 frames shall be inspected
    private static final  int cut_0 = (int)(2000/26) ;
    private static final  int cut_1 = (int)(10000/26) ;
    private final Context context;

    private CheapMP3 cheap_mp3;
    private File song_file;
    private int cut_frame = 0;
    private File clean_song_file;
    private boolean solution_exists = false;

    public Song(Context context, File infile) {
        this.context = context;
        this.cheap_mp3 = new CheapMP3();
        this.song_file = infile;
        this.clean_song_file = new File(Utils.getOurDirectory(Paths.FOLDER_NAME).getPath()+"_"+infile.getName());
    }

    public int getCutFrame() {
        return cut_frame;
    }

    public boolean isItSoluble() {
        return solution_exists;
    }

    public void solve() {
        try {
            cheap_mp3.ReadFile(song_file);
            int[] frame_volumes = Arrays.copyOfRange(cheap_mp3.getFrameGains(), cut_0, cut_1);

            //get several smaller volume frames
            //find the one closest to the song starting position..
            //I take 5 motherfucker....
            int[] smaller_volume_frames = getFewSmallestVolumes(frame_volumes, 5);
            Arrays.sort(smaller_volume_frames);

            int  index_of__rightmost_min = smaller_volume_frames[smaller_volume_frames.length-1];
            cut_frame = index_of__rightmost_min + cut_0;

            solution_exists = (cut_frame > cut_0 && cut_frame < cut_1);

            Utils.log("Cut frame is solved at : "+cut_frame);


        } catch (IOException e) {
            Utils.log("IO exception during solving");
            e.printStackTrace();
        }

    }


    public void clean() throws IOException, InvalidDataException, NotSupportedException, UnsupportedTagException {
        String temp_fname = "m"+new Random().nextInt(1993);
        File temp = File.createTempFile(temp_fname, "dm", context.getCacheDir());

        cheap_mp3.WriteFile(temp, cut_frame, cheap_mp3.getNumFrames() - cut_frame);
        doIDE3(temp);

    }

    private void doIDE3(File ftemp) throws IOException, NotSupportedException, InvalidDataException, UnsupportedTagException {
        Mp3File mp3in = new Mp3File(song_file.getAbsolutePath());
        Mp3File mp3out = new Mp3File(ftemp.getAbsolutePath());

        if (mp3in.hasId3v2Tag())
        {
            Utils.log("It has id3v2 tag");
            ID3v2 id3v2 = mp3in.getId3v2Tag();
            id3v2.setComment("Ads removed by L Music Cleaner");
            mp3out.setId3v2Tag(id3v2);
        }
        else if (mp3in.hasId3v1Tag())
        {
            Utils.log("It has id3v1 tag");
            ID3v1 id3v1 = mp3in.getId3v1Tag();
            id3v1.setComment("Ads removed by L Music Cleaner");
            mp3out.setId3v1Tag(id3v1);
        }
        else
        {
            Utils.log("Has no id3 tag");
        }
        mp3out.save(clean_song_file.getAbsolutePath());

    }

    private int[] getFewSmallestVolumes(int[] arr, int limit) {
        int res[] = new int[limit];
        int ns = getNextSmaller(arr, 0);
        res[0] = ns;

        for (int i = 1; i < limit; i++) {
            ns = getNextSmaller(arr, arr[ns]);
            res[i] = ns;
        }
        return res;
    }

    private int getNextSmaller(int[] arr, int previous) {
        int min = Integer.MAX_VALUE, index = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < min && arr[i] > previous) {
                min = arr[i];
                index = i;
            }
        }
        return index;
    }

    public int getNumOfFrames() {
        return cheap_mp3.getNumFrames();
    }

    public int getCutSecond() {
        return (int) (getCutFrame() * 26.12f);
    }

    public File getCleanFile() {
        return clean_song_file;
    }
}
