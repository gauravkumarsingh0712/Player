package com.musicplayer.collection.android.controller;

import android.app.Application;

import com.crashlytics.android.answers.Answers;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.musicplayer.collection.android.R;

import io.fabric.sdk.android.Fabric;

/**
 * Created by gauravkumar.singh on 5/12/2016.
 */
public class AppController extends Application {

    private static AppController ourInstance = new AppController();

    public static synchronized  AppController getInstance() {
        return ourInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;
        Fabric.with(this, new Answers());
    }

    private Tracker mTracker;

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // Setting mTracker to Analytics Tracker declared in our xml Folder
            mTracker = analytics.newTracker(R.xml.analytics_tracker);
        }
        return mTracker;
    }

}
