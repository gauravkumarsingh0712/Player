package com.musicplayer.collection.android.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

import com.musicplayer.collection.android.activity.MusicPlayerActivity;
import com.musicplayer.collection.android.fragment.SongsListFragment;
import com.musicplayer.collection.android.model.SongsInfoDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by gauravkumar.singh on 4/29/2016.
 */
public class MusicPlayerService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    //binder
    public final IBinder musicServiceBind = new MusicPlayerBinder();

    //media player
    public MediaPlayer player;

    private SongsInfoDto songsInfoDto;

    public ArrayList<String> songsList = new ArrayList<String>();

    public static int currentSongIndex = 0;

    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000; // 5000 milliseconds
    public static final String BROADCAST_PLAY_MUSIC = "play_music";
    private Intent playMusicBroadcast;


    @Override
    public void onCreate() {
        super.onCreate();

        //create player
        player = new MediaPlayer();

        songsInfoDto = SongsInfoDto.getInstance();
        songsList = songsInfoDto.getSongPathArray();

        playMusicBroadcast = new Intent(BROADCAST_PLAY_MUSIC);

        //initialize
        initMusicPlayer();
    }

    public void initMusicPlayer() {
        //set player properties
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //set listeners
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);

    }

    public void playSong(int index) {
        // Play song
        try {
            player.reset();
            if(songsInfoDto.isStartPlayList())
            {
                player.setDataSource(songsInfoDto.getAddToPlayListArray().get(index));
            }else
            {
                player.setDataSource(songsList.get(index));
            }
            player.prepare();


        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicServiceBind;
    }

    //binder
    public class MusicPlayerBinder extends Binder {
        public MusicPlayerService getService() {
            return MusicPlayerService.this;
        }
    }

    //release resources when unbind
    @Override
    public boolean onUnbind(Intent intent) {
//        player.stop();
//        player.release();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
// check for repeat is ON or OFF
        if (MusicPlayerActivity.getInstanse().isRepeat) {
            // repeat is on play same song again
            MusicPlayerActivity.getInstanse().playSong(currentSongIndex);

        } else if (MusicPlayerActivity.getInstanse().isShuffle) {
            // shuffle is on - play a random song
            Random rand = new Random();
            currentSongIndex = rand.nextInt((songsList.size() - 1) - 0 + 1) + 0;
            MusicPlayerActivity.getInstanse().playSong(currentSongIndex);
        } else {
            // no repeat or shuffle ON - play next song
            if (currentSongIndex < (songsList.size() - 1)) {
                MusicPlayerActivity.getInstanse().playSong(currentSongIndex + 1);
                currentSongIndex = currentSongIndex + 1;
            } else {
                // play first song
                MusicPlayerActivity.getInstanse().playSong(0);
                currentSongIndex = 0;
            }
        }

        SongsListFragment.pos = currentSongIndex;

        sendBroadcast(playMusicBroadcast);
       // MusicPlayerActivity.getInstanse().getImageFromDevice(currentSongIndex);


    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        player.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    //playback methods
    public int getPostion() {
        return player.getCurrentPosition();
    }

    public int getDuration() {
        return player.getDuration();
    }

    public boolean isPlayingMood() {
        return player.isPlaying();
    }

    public void pausePlayer() {
        player.pause();
    }

    public void seek(int posn) {
        player.seekTo(posn);
    }

    public void goStart() {
        player.start();
    }

    public void release() {
        player.release();
    }

    //skip to previous track
    public void playPrev() {
        if (currentSongIndex > 0) {
            MusicPlayerActivity.getInstanse().playSong(currentSongIndex - 1);
            currentSongIndex = currentSongIndex - 1;
        } else {
            // play last song
            MusicPlayerActivity.getInstanse().playSong(songsList.size() - 1);
            currentSongIndex = songsList.size() - 1;
        }

       // MusicPlayerActivity.getInstanse().getImageFromDevice(currentSongIndex);
    }

    //skip to next
    public void playNext() {
        // check if next song is there or not
        if (currentSongIndex < (songsList.size() - 1)) {
            MusicPlayerActivity.getInstanse().playSong(currentSongIndex + 1);
            currentSongIndex = currentSongIndex + 1;
        } else {
            // play first song
            MusicPlayerActivity.getInstanse().playSong(0);
            currentSongIndex = 0;
        }
       // MusicPlayerActivity.getInstanse().getImageFromDevice(currentSongIndex);
    }

    public void playForward()
    {
        // get current song position
        int currentPosition = getPostion();
        // check if seekForward time is lesser than song duration
        if (currentPosition + seekForwardTime <= getDuration()) {
            // forward song
            seek(currentPosition + seekForwardTime);
        } else {
            // forward to end position
            seek(getDuration());
        }
    }

    public void playBackward()
    {
        // get current song position
        int currentPosition = getPostion();
        // check if seekBackward time is greater than 0 sec
        if (currentPosition - seekBackwardTime >= 0) {
            // forward song
            seek(currentPosition - seekBackwardTime);
        } else {
            // backward to starting position
            seek(0);
        }
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
//        player.stop();
//        player.release();
    }

    //toggle shuffle
    public void setShuffle() {

    }

}
