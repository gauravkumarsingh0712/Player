package com.musicplayer.collection.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.musicplayer.collection.android.R;
import com.musicplayer.collection.android.adapter.ArtistListViewAdapter;
import com.musicplayer.collection.android.model.SongsInfoDto;

/**
 * Created by Ratan on 7/29/2015.
 */
public class ArtistListFragment extends Fragment {

    private SongsInfoDto mSongsInfoDto;
    private ListView listView;
    private ArtistListViewAdapter artistListViewAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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

        return view;
    }

    private void initView(View view) {
        listView = (ListView) view.findViewById(R.id.song_lait_list);
    }

    private void initData() {
        artistListViewAdapter = new ArtistListViewAdapter(
                getActivity());
        listView.setAdapter(artistListViewAdapter);
        listView.setFastScrollAlwaysVisible(true);
        artistListViewAdapter.notifyDataSetChanged();
    }

    private void initListener() {

    }
}
