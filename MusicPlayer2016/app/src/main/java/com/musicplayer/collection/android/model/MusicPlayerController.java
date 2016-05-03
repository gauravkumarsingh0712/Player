package com.musicplayer.collection.android.model;

import com.musicplayer.collection.android.activity.MainActivity;

/**
 * Created by gauravkumar.singh on 4/29/2016.
 */
public class MusicPlayerController {

    public static MusicPlayerController mInstance;

    public static MusicPlayerController getInstance() {
        if (mInstance == null) {
            mInstance = new MusicPlayerController();
        }

        return mInstance;
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    private MainActivity mainActivity;


}
