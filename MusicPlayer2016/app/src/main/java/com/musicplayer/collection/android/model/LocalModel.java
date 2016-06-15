package com.musicplayer.collection.android.model;

import com.musicplayer.collection.android.interfac.UpdateUIAdapterInterface;

/**
 * Created by gauravkumar.singh on 5/6/2016.
 */
public class LocalModel {

    public static LocalModel mInstance;
    private UpdateUIAdapterInterface updateUIAdapterInterface;

    public static LocalModel getInstance() {
        if (mInstance == null) {
            mInstance = new LocalModel();
        }
        return mInstance;
    }


    public UpdateUIAdapterInterface getUpdateUIAdapterInterface() {
        return updateUIAdapterInterface;
    }

    public void setUpdateUIAdapterInterface(UpdateUIAdapterInterface updateUIAdapterInterface) {
        this.updateUIAdapterInterface = updateUIAdapterInterface;
    }


}
