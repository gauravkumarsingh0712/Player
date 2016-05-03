package com.musicplayer.collection.android.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.musicplayer.collection.android.R;
import com.musicplayer.collection.android.activity.MainActivity;
import com.musicplayer.collection.android.activity.MusicPlayerActivity;
import com.musicplayer.collection.android.adapter.SongListViewAdapter;
import com.musicplayer.collection.android.interfac.InformationInterface;
import com.musicplayer.collection.android.model.SongsInfoDto;
import com.musicplayer.collection.android.service.MusicPlayerService;

/**
 * Created by Ratan on 7/29/2015.
 */
public class SongsListFragment extends Fragment implements InformationInterface {

    private SongsInfoDto mSongsInfoDto;
    private ListView listView;
    private SongListViewAdapter songListViewAdapter;
    private int songIndex;
    public static int pos;
    SongIndexInterface songIndexInterface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSongsInfoDto = SongsInfoDto.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.songs_list_layout, container,
                false);
        initView(view);
        initData();
        initListener();

        return view;
    }

    private void initView(View view) {
        listView = (ListView) view.findViewById(R.id.song_lait_list);
    }

    private void initData() {
        songListViewAdapter = new SongListViewAdapter(
                MusicPlayerActivity.getInstanse());
        listView.setAdapter(songListViewAdapter);
        listView.setFastScrollAlwaysVisible(true);
        songListViewAdapter.notifyDataSetChanged();
    }

    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                System.out.println("list view click listener : ");
                // getting listitem index
                songIndex = position;
                pos = songIndex;
                songListViewAdapter.notifyDataSetChanged();
                MusicPlayerService.currentSongIndex = songIndex;
                songIndexInterface.setSongIndex(songIndex);
            }
        });

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        songIndexInterface = (SongIndexInterface)activity;

        ((MainActivity)getActivity()).informationInterface= this;
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("onReusme call in fragmnet");
        pos = MusicPlayerService.currentSongIndex;
        songListViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void getData(int index) {
        pos = index;
        songListViewAdapter.notifyDataSetChanged();
    }

    public interface SongIndexInterface {
        public void setSongIndex(int index);
    }

}
