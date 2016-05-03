package com.musicplayer.collection.android.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.musicplayer.collection.android.R;
import com.musicplayer.collection.android.fragment.SongsListFragment;
import com.musicplayer.collection.android.fragment.TabFragment;
import com.musicplayer.collection.android.interfac.InformationInterface;
import com.musicplayer.collection.android.model.SongsInfoDto;
import com.musicplayer.collection.android.service.MusicPlayerService;
import com.musicplayer.collection.android.utils.Utilities;

/**
 * Created by gauravkumar.singh on 5/2/2016.
 */
public class MainActivity extends BaseActivity implements SongsListFragment.SongIndexInterface, SeekBar.OnSeekBarChangeListener {
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    public MusicPlayerService mMusicPlayerService;
    private Intent playIntent;
    private boolean musicBound = false;
    private TextView songNameTextView;
    private ImageView songImageView;
    private ImageView playPauseImageView;
    private ImageView nextImageView;
    private ImageView previousImageView;
    private SongsInfoDto mSongsInfoDto;
    private int songIndex;
    private SeekBar seekProgressBar;
    private Handler mHandler = new Handler();
    private Utilities utils;
    public InformationInterface informationInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mMusicPlayerService = MusicPlayerActivity.getInstanse().musicPlayerService;
        mSongsInfoDto = SongsInfoDto.getInstance();
        initView();
        initListener();
        initData();

        registerReceiver(broadcastReceiver, new IntentFilter(
                MusicPlayerService.BROADCAST_PLAY_MUSIC));
    }

    @Override
    public void initView() {
        /**
         *Setup the DrawerLayout and NavigationView
         */

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff);
        seekProgressBar = (SeekBar) findViewById(R.id.seekProgressBar);
        View view = (View) findViewById(R.id.footer_player);
        songNameTextView = (TextView) view.findViewById(R.id.footersongName);
        songImageView = (ImageView) view.findViewById(R.id.footer_song_image);
        playPauseImageView = (ImageView) view.findViewById(R.id.footer_play_button);
        nextImageView = (ImageView) view.findViewById(R.id.footer_next_button);
        previousImageView = (ImageView) view.findViewById(R.id.footer_previous_button);
    }

    @Override
    public void initData() {
        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */
        utils = new Utilities();

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();


        playSongFromList(MusicPlayerService.currentSongIndex);
    }

    @Override
    public void initListener() {
        /**
         * Setup click events on the Navigation View Items.
         */
        seekProgressBar.setOnSeekBarChangeListener(this);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();

                if (menuItem.getItemId() == R.id.nav_item_inbox) {
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();

                }

                if (menuItem.getItemId() == R.id.nav_item_sent) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
                }

                if (menuItem.getItemId() == R.id.nav_item_draft) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
                }

                return false;
            }

        });

        /**
         * Setup Drawer Toggle of the Toolbar
         */

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name,
                R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

        songImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Starting new intent
                Intent in = new Intent(MainActivity.this,
                        MusicPlayerActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                // Sending songIndex to PlayerActivity
                // in.putExtra("songIndex", songIndex);
                // setResult(100, in);
                startActivity(in);
                // Closing PlayListView
                // finish();
            }
        });

        nextImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMusicPlayerService != null) {
                    mMusicPlayerService.playNext();
                    playSongFromList(MusicPlayerService.currentSongIndex);
                    informationInterface.getData(MusicPlayerService.currentSongIndex);
                }
            }
        });

        previousImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMusicPlayerService != null) {
                    mMusicPlayerService.playPrev();
                    playSongFromList(MusicPlayerService.currentSongIndex);
                    informationInterface.getData(MusicPlayerService.currentSongIndex);
                }
            }
        });

        playPauseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check for already playing
                if (mMusicPlayerService.isPlayingMood()) {
                    if (mMusicPlayerService.player != null) {
                        mMusicPlayerService.pausePlayer();
                        // Changing button image to play button
                        playPauseImageView.setImageResource(R.drawable.play_button);
                    }
                } else {

                    // Resume song
                    if (mMusicPlayerService.player != null) {
                        mMusicPlayerService.goStart();

                        // Changing button image to pause button
                        playPauseImageView.setImageResource(R.drawable.pause_button);
                    }
                }
            }
        });
    }

    public void playSongFromList(int index) {

        if (!mMusicPlayerService.isPlayingMood()) {
            if (mMusicPlayerService.player != null) {
                MusicPlayerActivity.getInstanse().playSong(index);
            }
        }

        songNameTextView.setText(mSongsInfoDto.getSongNameArray().get(index));
        songNameTextView.setSelected(true);
        songImageView.setImageBitmap(mSongsInfoDto.getSongImageBitmapArray().get(index));
        playPauseImageView.setImageResource(R.drawable.pause_button);

        if (mMusicPlayerService.isPlayingMood()) {
            if (mMusicPlayerService.player != null) {
                // Changing button image to play button
                playPauseImageView.setImageResource(R.drawable.pause_button);
            }
        } else {

            // Resume song
            if (mMusicPlayerService.player != null) {
                // Changing button image to pause button
                playPauseImageView.setImageResource(R.drawable.play_button);
            }
        }

        seekProgressBar.setProgress(0);
        seekProgressBar.setMax(100);
        updateProgressBar();
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
            long totalDuration = mMusicPlayerService.getDuration();
            long currentDuration = mMusicPlayerService.getPostion();

            // Updating progress bar
            int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            seekProgressBar.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void setSongIndex(int index) {
        System.out.println("index here MAIN : " + index);
        if (mMusicPlayerService != null) {
            MusicPlayerActivity.getInstanse().playSong(index);
        }
        playSongFromList(index);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //       mHandler.removeCallbacks(mUpdateTimeTask);
//        int totalDuration = mMusicPlayerService.getDuration();
//        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);
//
//        // forward or backward to certain seconds
//        mMusicPlayerService.seek(currentPosition);
//
//        // update timer progress again
//        updateProgressBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mUpdateTimeTask);
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        playSongFromList(MusicPlayerService.currentSongIndex);


    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            playSongFromList(MusicPlayerService.currentSongIndex);
            informationInterface.getData(MusicPlayerService.currentSongIndex);       
        }
    };


}