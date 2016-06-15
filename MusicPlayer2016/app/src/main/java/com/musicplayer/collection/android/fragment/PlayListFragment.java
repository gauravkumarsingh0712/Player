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
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.musicplayer.collection.android.R;
import com.musicplayer.collection.android.adapter.AddToPlayListViewAdapter;
import com.musicplayer.collection.android.controller.AppController;
import com.musicplayer.collection.android.interfac.SongIndexInterface;
import com.musicplayer.collection.android.model.SongsInfoDto;

/**
 * Created by gauravkumar.singh on 5/4/2016.
 */
public class PlayListFragment extends Fragment {

    private SongsInfoDto mSongsInfoDto;
    private ListView listView;
    private AddToPlayListViewAdapter addToPlayListViewAdapter;
    private SongIndexInterface songIndexInterface;

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
        initListener();

        return view;
    }

    private void initView(View view) {
        listView = (ListView) view.findViewById(R.id.song_lait_list);
    }

    private void initData() {
        addToPlayListViewAdapter = new AddToPlayListViewAdapter(
                getActivity());
        listView.setAdapter(addToPlayListViewAdapter);
        listView.setFastScrollAlwaysVisible(true);
        addToPlayListViewAdapter.notifyDataSetChanged();
    }

    private void initListener() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mSongsInfoDto.setIsStartPlayList(true);
                songIndexInterface.setSongIndex(position);

                Tracker t = ((AppController) getActivity().getApplication()).getDefaultTracker();

                // Build and send an Event.
                t.send(new HitBuilders.EventBuilder()
                        .setCategory(getString(R.string.categoryId))
                        .setAction(getString(R.string.actionId))
                        .setLabel(getString(R.string.labelId))
                        .build());
                Toast.makeText(getActivity(), "Event is recorded. Check Google Analytics!", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        songIndexInterface = (SongIndexInterface)activity;

    }

}
