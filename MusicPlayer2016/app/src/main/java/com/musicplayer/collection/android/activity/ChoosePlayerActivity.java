package com.musicplayer.collection.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.musicplayer.collection.android.R;

/**
 * Created by gauravkumar.singh on 4/27/2016.
 */
public class ChoosePlayerActivity extends BaseActivity implements View.OnClickListener {

    private Button audioPlayerButton;
    private Button videoPlayerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_player_activity);

        initView();
        initListener();

    }

    @Override
    public void initView() {

        audioPlayerButton = (Button) findViewById(R.id.audio_player_btn);
        videoPlayerButton = (Button) findViewById(R.id.video_player_btn);

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        audioPlayerButton.setOnClickListener(this);
        videoPlayerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.audio_player_btn: {

                Intent in = new Intent(ChoosePlayerActivity.this,
                        MusicPlayerActivity.class);
                startActivity(in);

            }
            break;
            case R.id.video_player_btn: {
                Intent in = new Intent(ChoosePlayerActivity.this,
                        VideoViewActivity.class);
                startActivity(in);
            }
            break;
        }

    }
}
