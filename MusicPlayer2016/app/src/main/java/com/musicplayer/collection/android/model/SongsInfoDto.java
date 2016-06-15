package com.musicplayer.collection.android.model;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by gauravkumar.singh on 4/27/2016.
 */
public class SongsInfoDto {

    private ArrayList<String> albumNameArray = new ArrayList<>();
    private ArrayList<String> artistNameArray = new ArrayList<>();
    private ArrayList<String> yearArray = new ArrayList<>();
    public ArrayList<String> songPathArray = new ArrayList<>();
    private ArrayList<String> songNameArray = new ArrayList<>();
    private ArrayList<Bitmap> songImageBitmapArray = new ArrayList<>();
    private ArrayList<String> addToPlayListArray = new ArrayList<>();
    private ArrayList<Integer> indexPlayListArray = new ArrayList<>();
    public static SongsInfoDto songsInfoDto;
    private boolean isStartPlayList = false;

    public boolean isStartPlayList() {
        return isStartPlayList;
    }

    public void setIsStartPlayList(boolean isStartPlayList) {
        this.isStartPlayList = isStartPlayList;
    }

    public ArrayList<Integer> getIndexPlayListArray() {
        return indexPlayListArray;
    }

    public void setIndexPlayListArray(ArrayList<Integer> indexPlayListArray) {
        this.indexPlayListArray = indexPlayListArray;
    }

    public ArrayList<String> getYearArray() {
        return yearArray;
    }

    public void setYearArray(ArrayList<String> yearArray) {
        this.yearArray = yearArray;
    }

    public ArrayList<String> getArtistNameArray() {
        return artistNameArray;
    }

    public void setArtistNameArray(ArrayList<String> artistNameArray) {
        this.artistNameArray = artistNameArray;
    }

    public ArrayList<String> getAlbumNameArray() {
        return albumNameArray;
    }

    public void setAlbumNameArray(ArrayList<String> albumNameArray) {
        this.albumNameArray = albumNameArray;
    }


    public static SongsInfoDto getInstance() {
        if (songsInfoDto == null)
        {
            songsInfoDto = new SongsInfoDto();
        }

        return songsInfoDto;
    }


    public ArrayList<String> getSongNameArray() {
        return songNameArray;
    }

    public void setSongNameArray(ArrayList<String> songNameArray) {
        this.songNameArray = songNameArray;
    }

    public ArrayList<String> getSongPathArray() {
        return songPathArray;
    }

    public void setSongPathArray(ArrayList<String> songPathArray) {
        this.songPathArray = songPathArray;
    }

    public ArrayList<Bitmap> getSongImageBitmapArray() {
        return songImageBitmapArray;
    }

    public void setSongImageBitmapArray(ArrayList<Bitmap> songImageBitmapArray) {
        this.songImageBitmapArray = songImageBitmapArray;
    }

    public ArrayList<String> getAddToPlayListArray() {
        return addToPlayListArray;
    }

    public void setAddToPlayListArray(ArrayList<String> addToPlayListArray) {
        this.addToPlayListArray = addToPlayListArray;
    }

}
