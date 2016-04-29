package com.musicplayer.collection.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.musicplayer.collection.android.R;
import com.musicplayer.collection.android.model.SongsInfoDto;

/**
 * Created by gauravkumar.singh on 4/22/2016.
 */
public class SongInfoActivity extends Activity {

    private TextView songName;
    private TextView albumName;
    private TextView artistName;
    private TextView releaseYear;
    private ImageView songImage;
    private SongsInfoDto songsInfoDto;
    private int currentSongIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_info);

        songImage = (ImageView) findViewById(R.id.songImage);
        songName = (TextView) findViewById(R.id.songName);
        albumName = (TextView) findViewById(R.id.albumName);
        artistName = (TextView) findViewById(R.id.artistName);
        releaseYear = (TextView) findViewById(R.id.releaseYear);

        songsInfoDto = SongsInfoDto.getInstance();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentSongIndex = bundle.getInt("currentsongIndex");
        }


        songImage.setImageBitmap(songsInfoDto.getSongImageBitmapArray().get(currentSongIndex));
        songName.setText(songsInfoDto.getSongNameArray().get(currentSongIndex));
        albumName.setText(songsInfoDto.getAlbumNameArray().get(currentSongIndex));
        artistName.setText(songsInfoDto.getArtistNameArray().get(currentSongIndex));
        if (songsInfoDto.getYearArray().get(currentSongIndex) != null) {
            releaseYear.setText(songsInfoDto.getYearArray().get(currentSongIndex));
        } else {
            //releaseYear.setVisibility(View.GONE);
        }
    }

}
