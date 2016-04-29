package com.musicplayer.collection.android.activity;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by gauravkumar.singh on 4/27/2016.
 */
public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    abstract public void initView();
    abstract public void initData();
    abstract public void initListener();
}
