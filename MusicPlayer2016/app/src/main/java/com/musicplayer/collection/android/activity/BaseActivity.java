package com.musicplayer.collection.android.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by gauravkumar.singh on 4/27/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    abstract public void initView();
    abstract public void initData();
    abstract public void initListener();
}
