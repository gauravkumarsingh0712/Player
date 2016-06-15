package com.musicplayer.collection.android.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.musicplayer.collection.android.R;
import com.musicplayer.collection.android.activity.MainActivity;
import com.musicplayer.collection.android.activity.MusicPlayerActivity;
import com.musicplayer.collection.android.adapter.SongListViewAdapter;
import com.musicplayer.collection.android.interfac.InformationInterface;
import com.musicplayer.collection.android.interfac.SongIndexInterface;
import com.musicplayer.collection.android.interfac.UpdateUIAdapterInterface;
import com.musicplayer.collection.android.model.SongsInfoDto;
import com.musicplayer.collection.android.service.MusicPlayerService;

/**
 * Created by Gaurav on 7/29/2015.
 */
public class SongsListFragment extends Fragment implements InformationInterface,UpdateUIAdapterInterface {

    private SongsInfoDto mSongsInfoDto;
    private ListView listView;
    private SongListViewAdapter songListViewAdapter;
    private int songIndex;
    public static int pos;
    SongIndexInterface songIndexInterface;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSongsInfoDto = SongsInfoDto.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

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
                MusicPlayerActivity.getInstanse(),this);
        listView.setAdapter(songListViewAdapter);
        listView.setFastScrollAlwaysVisible(true);
        songListViewAdapter.notifyDataSetChanged();
        System.out.println("initData SongsListFragment : ");
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

//                Tracker t = ((AppController) getActivity().getApplication()).getDefaultTracker();
//
//                // Build and send an Event.
//                t.send(new HitBuilders.EventBuilder()
//                        .setCategory(getString(R.string.categoryId))
//                        .setAction(getString(R.string.actionId))
//                        .setLabel(getString(R.string.labelId))
//                        .build());
//                Toast.makeText(getActivity(), "Event is recorded. Check Google Analytics!", Toast.LENGTH_LONG).show();

                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "123");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Blue Eyes");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            }
        });

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, MotionEvent event) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (listView != null) {
                            listView.setFastScrollAlwaysVisible(false);
                        }
                    }
                }, 2000);


                return false;
            }
        });

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        songIndexInterface = (SongIndexInterface) activity;
        ((MainActivity) getActivity()).informationInterface = this;

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

    @Override
    public void updateUi() {

        songListViewAdapter.notifyDataSetChanged();
    }
}
