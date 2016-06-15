package com.musicplayer.collection.android.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.musicplayer.collection.android.R;
import com.musicplayer.collection.android.adapter.VideoPlayerListViewAdapter;

import java.util.ArrayList;

/**
 * Created by gauravkumar.singh on 4/27/2016.
 */
public class VideoViewActivity extends BaseActivity {
    private ListView videolist;
    private Cursor videocursor;
    private int video_column_index;
    private int video_column_index_data;
    private String filename;
    private VideoPlayerListViewAdapter videoPlayerListViewAdapter;
    private ArrayList<String> videoSongNameList = new ArrayList<>();
    private ArrayList<String> filenameList = new ArrayList<>();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_view_activity);

        init_phone_video();

        initView();
        initListener();
        videoPlayerListViewAdapter = new VideoPlayerListViewAdapter(this,videoSongNameList);
        videolist.setAdapter(videoPlayerListViewAdapter);
        videoPlayerListViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void initView() {
        videolist = (ListView) findViewById(R.id.listview_video);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        videolist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.gc();
                filename = filenameList.get(position);
                Intent intent = new Intent(VideoViewActivity.this, VideoPlayerActivity.class);
                intent.putExtra("videofilename", filename);
                intent.putExtra("index",position);
                intent.putStringArrayListExtra("songname", videoSongNameList);
                startActivity(intent);
            }
        });
    }

    private void init_phone_video() {
        System.gc();
        String[] proj = {MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE};
        videocursor = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                proj, null, null, null);
        try {
            if (videocursor != null) {
                if (videocursor.moveToFirst()) {
                    do {
                        video_column_index = videocursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
                        String videoSongName = videocursor.getString(video_column_index);
                        videoSongNameList.add(videoSongName);
                        video_column_index_data = videocursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                        String filename = videocursor.getString(video_column_index_data);
                        filenameList.add(filename);
                    } while (videocursor.moveToNext());

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}