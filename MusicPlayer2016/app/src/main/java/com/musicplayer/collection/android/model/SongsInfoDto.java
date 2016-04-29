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
    private ArrayList<String> songPathArray = new ArrayList<>();
    private ArrayList<String> songNameArray = new ArrayList<>();
    private ArrayList<Bitmap> songImageBitmapArray = new ArrayList<>();

    public static SongsInfoDto songsInfoDto;

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
        if (songsInfoDto == null) {
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

}
