package com.musicplayer.collection.android.activity;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.musicplayer.collection.android.R;
import com.musicplayer.collection.android.model.SongsInfoDto;
import com.musicplayer.collection.android.service.MusicPlayerService;
import com.musicplayer.collection.android.service.MusicPlayerService.MusicPlayerBinder;
import com.musicplayer.collection.android.utils.SongsManager;
import com.musicplayer.collection.android.utils.Utilities;

import java.io.File;

public class MusicPlayerActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener {

    private ImageButton btnPlay;
    private ImageButton btnForward;
    private ImageButton btnBackward;
    private ImageButton btnNext;
    private ImageButton btnPrevious;
    private ImageButton btnPlaylist;
    private ImageButton btnRepeat;
    private ImageButton btnShuffle;
    private SeekBar songProgressBar;
    private TextView songTitleLabel;
    private TextView songCurrentDurationLabel;
    private TextView songTotalDurationLabel;
    private ImageView songImage;
    // Media Player
    //private MediaPlayer mp;
    // Handler to update UI timer, progress bar etc,.
    private Handler mHandler = new Handler();
    ;
    private SongsManager songManager;
    private Utilities utils;
    public static MusicPlayerActivity musicPlayerActivity;
    //public int currentSongIndex = 0;
    public boolean isShuffle = false;
    public boolean isRepeat = false;
    private SongsInfoDto songsInfoDto;
    private boolean isPlayingFirstTime = false;
    public MusicPlayerService musicPlayerService;
    private Intent playIntent;
    //binding
    private boolean musicBound = false;
//    public static final String MEDIA_PATH = Environment
//            .getExternalStorageDirectory().getPath() + "/songs/";

    public static final String MEDIA_PATH = new String(Environment.getExternalStorageDirectory().getAbsolutePath());

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        musicPlayerActivity = this;

        // All player buttons
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnForward = (ImageButton) findViewById(R.id.btnForward);
        btnBackward = (ImageButton) findViewById(R.id.btnBackward);
        btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
        btnPlaylist = (ImageButton) findViewById(R.id.btnPlaylist);
        btnRepeat = (ImageButton) findViewById(R.id.btnRepeat);
        btnShuffle = (ImageButton) findViewById(R.id.btnShuffle);
        songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
        songTitleLabel = (TextView) findViewById(R.id.songTitle);
        songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
        songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);
        songImage = (ImageView) findViewById(R.id.songImage);

        File file = new File(MEDIA_PATH);
        if (!file.exists()) {
            try {
                file.mkdir();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // downloadedFile.createNewFile();
        }
        // Mediaplayer
        //  mp = new MediaPlayer();

        songManager = new SongsManager();
        utils = new Utilities();

        // Listeners
        songProgressBar.setOnSeekBarChangeListener(this); // Important
        songProgressBar.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        // mp.setOnCompletionListener(this); // Important
        // Getting all songs list
        // songsList = songManager.getPlayList();
        songsInfoDto = SongsInfoDto.getInstance();
        getImageFromDevice(0);
        // By default play first song
        // playSong(0);

        /**
         * Play button click event
         * plays a song and changes button to pause image
         * pauses a song and changes button to play image
         * */
        btnPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                // check for already playing
                if (musicPlayerService.isPlayingMood()) {
                    if (musicPlayerService.player != null) {
                        musicPlayerService.pausePlayer();
                        // Changing button image to play button
                        btnPlay.setImageResource(R.drawable.btn_play);
                    }
                } else {

                    // Resume song
                    if (musicPlayerService.player != null) {
                        musicPlayerService.goStart();

                        // Changing button image to pause button
                        btnPlay.setImageResource(R.drawable.btn_pause);
                    }
                }

            }
        });

        /**
         * Forward button click event
         * Forwards song specified seconds
         * */
        btnForward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (musicPlayerService != null) {
                    musicPlayerService.playForward();
                }
            }
        });

        /**
         * Backward button click event
         * Backward song to specified seconds
         * */
        btnBackward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (musicPlayerService != null) {
                    musicPlayerService.playBackward();
                }

            }
        });

        /**
         * Next button click event
         * Plays next song by taking currentSongIndex + 1
         * */
        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (musicPlayerService != null) {
                    musicPlayerService.playNext();
                }

            }
        });

        /**
         * Back button click event
         * Plays previous song by currentSongIndex - 1
         * */
        btnPrevious.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (musicPlayerService != null) {
                    musicPlayerService.playPrev();
                }

            }
        });

        /**
         * Button Click event for Repeat button
         * Enables repeat flag to true
         * */
        btnRepeat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (isRepeat) {
                    isRepeat = false;
                    Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
                    btnRepeat.setImageResource(R.drawable.btn_repeat);
                } else {
                    // make repeat to true
                    isRepeat = true;
                    Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isShuffle = false;
                    btnRepeat.setImageResource(R.drawable.btn_repeat_focused);
                    btnShuffle.setImageResource(R.drawable.btn_shuffle);
                }
            }
        });

        /**
         * Button Click event for Shuffle button
         * Enables shuffle flag to true
         * */
        btnShuffle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (isShuffle) {
                    isShuffle = false;
                    Toast.makeText(getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
                    btnShuffle.setImageResource(R.drawable.btn_shuffle);
                } else {
                    // make repeat to true
                    isShuffle = true;
                    Toast.makeText(getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isRepeat = false;
                    btnShuffle.setImageResource(R.drawable.btn_shuffle_focused);
                    btnRepeat.setImageResource(R.drawable.btn_repeat);
                }
            }
        });

        /**
         * Button Click event for Play list click event
         * Launches list activity which displays list of songs
         * */
        btnPlaylist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(MusicPlayerActivity.this, MainActivity.class);
                i.putExtra("currentsongIndex", MusicPlayerService.currentSongIndex);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
                //startActivityForResult(i,100);
            }
        });


        songImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent in = new Intent(MusicPlayerActivity.this,
                        SongInfoActivity.class);
                in.putExtra("currentsongIndex", MusicPlayerService.currentSongIndex);
                startActivity(in);

                return false;
            }
        });

    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }


    public static MusicPlayerActivity getInstanse() {

        return musicPlayerActivity;
    }

    /**
     * Receiving song index from playlist view
     * and play the song
     */
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            MusicPlayerService.currentSongIndex = data.getExtras().getInt("songIndex");

            if (musicPlayerService.isPlayingMood()) {
                if (musicPlayerService.player != null) {
                    // Changing button image to play button
                    btnPlay.setImageResource(R.drawable.btn_pause);
                }
            } else {

                // Resume song
                if (musicPlayerService.player != null) {
                    // Changing button image to pause button
                    btnPlay.setImageResource(R.drawable.btn_play);
                }
            }
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        System.out.println("onNewIntent call ");


        if (musicPlayerService.isPlayingMood()) {
            if (musicPlayerService.player != null) {
                // Changing button image to play button
                btnPlay.setImageResource(R.drawable.btn_pause);
            }
        } else {

            // Resume song
            if (musicPlayerService.player != null) {
                // Changing button image to pause button
                btnPlay.setImageResource(R.drawable.btn_play);
            }
        }

    }

    /**
     * Function to play a song
     *
     * @param songIndex - index of song
     */
    public void playSong(int songIndex) {

        System.out.println("playSong call");
        // Play song
        try {
            if (musicPlayerService != null) {
                musicPlayerService.playSong(songIndex);
            }
            // Displaying Song title
            songTitleLabel.setText(songsInfoDto.getSongNameArray().get(songIndex));
            songTitleLabel.setSelected(true);
            // Changing Button Image to pause image
            if (isPlayingFirstTime) {
                btnPlay.setImageResource(R.drawable.btn_pause);
                musicPlayerService.goStart();
            } else {
                btnPlay.setImageResource(R.drawable.btn_play);
                isPlayingFirstTime = true;
            }

            // set Progress bar values
            songProgressBar.setProgress(0);
            songProgressBar.setMax(100);

            getImageFromDevice(songIndex);

            // Updating progress bar
            updateProgressBar();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Update timer on seekbar
     */
    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = musicPlayerService.getDuration();
            long currentDuration = musicPlayerService.getPostion();

            // Displaying Total Duration time
            songTotalDurationLabel.setText("" + utils.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            songCurrentDurationLabel.setText("" + utils.milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            songProgressBar.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    /**
     *
     * */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

    }

    /**
     * When user starts moving the progress handler
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // remove message Handler from updating progress bar
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    /**
     * When user stops moving the progress hanlder
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = musicPlayerService.getDuration();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        musicPlayerService.seek(currentPosition);

        // update timer progress again
        updateProgressBar();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mUpdateTimeTask);
        unbindService(musicConnection);
        if (playIntent != null) {
            stopService(playIntent);
        }

        finish();

    }


    public void getImageFromDevice(int index) {

        try {
            songImage.setImageBitmap(songsInfoDto.getSongImageBitmapArray().get(index));
            songTitleLabel.setText(songsInfoDto.getSongNameArray().get(index));
            songTitleLabel.setSelected(true);

        } catch (Exception e) {

        }

    }


    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicPlayerBinder binder = (MusicPlayerBinder) service;
            //get service
            musicPlayerService = binder.getService();
            //pass list
            musicBound = true;
            playSong(0);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicPlayerService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(MusicPlayerActivity.this,
                ChoosePlayerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}