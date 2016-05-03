package com.musicplayer.collection.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.musicplayer.collection.android.R;
import com.musicplayer.collection.android.adapter.SongListViewAdapter;
import com.musicplayer.collection.android.model.SongsInfoDto;
import com.musicplayer.collection.android.service.MusicPlayerService;

public class PlayListActivity extends BaseActivity {
    // Songs list
    private ListView listView;
    private SongListViewAdapter playerListViewAdapter;
    private TextView songNameTextView;
    private ImageView songImageView;
    private ImageView playPauseImageView;
    private ImageView nextImageView;
    private ImageView previousImageView;
    private SongsInfoDto mSongsInfoDto;
    private int songIndex;
    private MusicPlayerService mMusicPlayerService;
    public static int pos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist);
        mMusicPlayerService = MusicPlayerActivity.getInstanse().musicPlayerService;
        mSongsInfoDto = SongsInfoDto.getInstance();

        initView();
        initListener();
        initData();
    }

    @Override
    public void initView() {
        listView = (ListView) findViewById(R.id.list);
        View view = (View) findViewById(R.id.footer_player);
        songNameTextView = (TextView) view.findViewById(R.id.footersongName);
        songImageView = (ImageView) view.findViewById(R.id.footer_song_image);
        playPauseImageView = (ImageView) view.findViewById(R.id.footer_play_button);
        nextImageView = (ImageView) view.findViewById(R.id.footer_next_button);
        previousImageView = (ImageView) view.findViewById(R.id.footer_previous_button);

    }

    @Override
    public void initData() {

        playerListViewAdapter = new SongListViewAdapter(
                PlayListActivity.this);
        listView.setAdapter(playerListViewAdapter);
        listView.setFastScrollAlwaysVisible(true);
        playerListViewAdapter.notifyDataSetChanged();

        playSongFromList(MusicPlayerService.currentSongIndex);

    }

    @Override
    public void initListener() {
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting listitem index
                songIndex = position;
                pos = songIndex;
                playerListViewAdapter.notifyDataSetChanged();
                MusicPlayerActivity.getInstanse().playSong(songIndex);
                playSongFromList(songIndex);

                MusicPlayerService.currentSongIndex = songIndex;

            }
        });

        songImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Starting new intent
                Intent in = new Intent(PlayListActivity.this,
                        MusicPlayerActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                // Sending songIndex to PlayerActivity
                in.putExtra("songIndex", songIndex);
                setResult(100, in);
               // startActivity(in);
                // Closing PlayListView
                finish();
            }
        });

        nextImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMusicPlayerService != null) {
                    mMusicPlayerService.playNext();
                    playSongFromList(MusicPlayerService.currentSongIndex);
                }
            }
        });

        previousImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMusicPlayerService != null) {
                    mMusicPlayerService.playPrev();
                    playSongFromList(MusicPlayerService.currentSongIndex);
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

    }


}
