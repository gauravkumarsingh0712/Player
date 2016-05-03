package com.musicplayer.collection.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ProgressBar;

import com.musicplayer.collection.android.activity.ChoosePlayerActivity;
import com.musicplayer.collection.android.model.SongsInfoDto;

import java.util.ArrayList;

/**
 * Created by gauravkumar.singh on 4/27/2016.
 */
public class SplashActivity extends Activity {


    private MediaMetadataRetriever metaRetriver;
    private byte[] art;
    private final String[] STAR = {"*"};
    private Cursor cursor;
    private SongsInfoDto songsInfoDto;
    private ArrayList<String> fullsongpath = new ArrayList<>();
    private ArrayList<Bitmap> bitmapArray = new ArrayList<>();
    private ArrayList<String> songNameList = new ArrayList<>();
    private ArrayList<String> albumNameArrayList = new ArrayList<>();
    private ArrayList<String> artistNameArrayList = new ArrayList<>();
    private ArrayList<String> yearArrayList = new ArrayList<>();
    private ProgressBar progressBar;
    private AsyncTask<Void, Void, Void> loadDataAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_activity);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.circle_progress_bar_lower));


        songsInfoDto = SongsInfoDto.getInstance();

        loadDataFromSDCard();
    }


    private void loadDataFromSDCard() {
        loadDataAsyncTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar.setVisibility(View.VISIBLE);
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//
//                            Intent in = new Intent(SplashActivity.this,
//                                    ChoosePlayerActivity.class);
//                            startActivity(in);
//                            finish();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, 3000);


            }

            @Override
            protected Void doInBackground(Void... params) {

                getAllSongs();

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent in = new Intent(SplashActivity.this,
                        ChoosePlayerActivity.class);
                startActivity(in);
                progressBar.setVisibility(View.GONE);
                finish();
            }

        };
        loadDataAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    @SuppressLint("NewApi")
    public void getImageFromDevice(int index) {

        metaRetriver = new MediaMetadataRetriever();

        metaRetriver.setDataSource(fullsongpath.get(index));

        try {
            art = metaRetriver.getEmbeddedPicture();

            if (art != null) {

                Bitmap songImage = BitmapFactory
                        .decodeByteArray(art, 0, art.length);
                bitmapArray.add(songImage);
            } else {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inScaled = false;
                Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher, options);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                        R.drawable.ic_launcher);
                bitmapArray.add(bm);
            }

            songsInfoDto.setSongImageBitmapArray(bitmapArray);

        } catch (Exception e) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher, options);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.ic_launcher);
            bitmapArray.add(bm);
            songsInfoDto.setSongImageBitmapArray(bitmapArray);
            e.printStackTrace();

        }

    }

    public void getAllSongs() {

        // Get the column index of the Thumbnails Image ID
        Uri allsongsuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        cursor = managedQuery(allsongsuri, STAR, selection, null, null);

        try {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        String songName = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));

                        songNameList.add(songName);

                        songsInfoDto.setSongNameArray(songNameList);

                        String fullpath = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Audio.Media.DATA));

                        fullsongpath.add(fullpath);

                        songsInfoDto.setSongPathArray(fullsongpath);

                        String albumName = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Audio.Media.ALBUM));

                        albumNameArrayList.add(albumName);

                        songsInfoDto.setAlbumNameArray(albumNameArrayList);

                        String artistName = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Audio.Media.ARTIST));

                        artistNameArrayList.add(artistName);

                        songsInfoDto.setArtistNameArray(artistNameArrayList);

                        String year = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Audio.Media.YEAR));

                        yearArrayList.add(year);

                        songsInfoDto.setYearArray(yearArrayList);

                        getImageFromDevice(songNameList.size() - 1);
                    } while (cursor.moveToNext());

                }

            }
        } catch (Exception e) {

            e.printStackTrace();

        }

    }
}
