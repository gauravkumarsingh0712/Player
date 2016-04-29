package com.musicplayer.collection.android.activity;

import android.content.res.Configuration;
import android.graphics.Point;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;

import com.musicplayer.collection.android.R;
import com.musicplayer.collection.android.custom.CustomVideoView;

/**
 * Created by gauravkumar.singh on 4/27/2016.
 */
public class VideoPlayerActivity extends BaseActivity {

    private CustomVideoView mVideoView;
    private MediaMetadataRetriever mMetadataRetriever;
    private MediaController mController;
    private String videoFileName;
    private int displayHeight = 0, displayWidth = 0;
    private LinearLayout videoViewSongInfoLayout;
    private FrameLayout videoViewLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player_activity);

        initView();
        setDimensions();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            videoFileName = bundle.getString("videofilename");
        }

        mMetadataRetriever = new MediaMetadataRetriever();

        startPlayback(videoFileName);

    }


    @Override
    public void initView() {
        mVideoView = (CustomVideoView) findViewById(R.id.myplaysurface);
        videoViewLayout = (FrameLayout) findViewById(R.id.video_view_layout);
        videoViewSongInfoLayout = (LinearLayout) findViewById(R.id.video_view_song_info_layout);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getScreenDimensions();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            videoViewSongInfoLayout.setVisibility(View.GONE);

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(displayWidth, displayHeight);
            videoViewLayout.setLayoutParams(lp);
            mVideoView.setDimensions(displayWidth, displayHeight);
            mVideoView.getHolder().setFixedSize(displayWidth, displayHeight);

        } else {
            videoViewSongInfoLayout.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(displayWidth, (int) (displayHeight * 0.35));
            videoViewLayout.setLayoutParams(lp);
            mVideoView.setDimensions(displayWidth, (int) (displayHeight * 0.35));
            mVideoView.getHolder().setFixedSize(displayWidth, (int) (displayHeight * 0.35));
        }

    }

    public void setDimensions() {
        Point size = new Point();
        WindowManager w = getWindowManager();
        int measuredHeight = 0;
        int measuredWidth = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            w.getDefaultDisplay().getSize(size);
            measuredHeight = size.y;
            measuredWidth = size.x;
        } else {
            Display d = w.getDefaultDisplay();
            measuredHeight = d.getHeight();
            measuredWidth = d.getWidth();
        }

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) (measuredHeight * 0.35));
        videoViewLayout.setLayoutParams(lp);

        mVideoView.setDimensions(measuredWidth, (int) (measuredHeight * 0.35));
    }

    public void getScreenDimensions() {

        Point size = new Point();
        WindowManager w = getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            w.getDefaultDisplay().getSize(size);
            displayHeight = size.y;
            displayWidth = size.x;
        } else {
            Display d = w.getDefaultDisplay();
            displayHeight = d.getHeight();
            displayWidth = d.getWidth();
        }
    }


    public void startPlayback(String videoPath) {

        mMetadataRetriever.setDataSource(videoPath);
        Uri uri = Uri.parse(videoPath);
        mVideoView.setVideoURI(uri);

        mController = new MediaController(this, false);
        mVideoView.setMediaController(mController);
        mVideoView.requestFocus();
        mVideoView.start();
    }

}
