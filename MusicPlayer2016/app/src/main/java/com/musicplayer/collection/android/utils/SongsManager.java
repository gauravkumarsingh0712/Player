package com.musicplayer.collection.android.utils;

import android.content.Context;
import android.os.Environment;

import com.musicplayer.collection.android.activity.MusicPlayerActivity;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

public class SongsManager {

	private ArrayList<HashMap<String,String>> songsList = new ArrayList<>();

	public SongsManager(){

	}
	
	/**
	 * Function to read all mp3 files from sdcard
	 * and store the details in ArrayList
	 * */
	public ArrayList<HashMap<String, String>> getPlayList1(){
		File home = new File(MusicPlayerActivity.MEDIA_PATH);

		if (home.listFiles(new FileExtensionFilter()).length > 0) {
			for (File file : home.listFiles(new FileExtensionFilter())) {
				HashMap<String, String> song = new HashMap<String, String>();
				song.put("songTitle", file.getName().substring(0, (file.getName().length() - 4)));
				song.put("songPath", file.getPath());

				// Adding each song to SongList
				songsList.add(song);
			}
		}
		// return songs list array
		return songsList;
	}

	public ArrayList<String> getPlayList() {

		ArrayList<String> songListFiles = new ArrayList<String>();
		File file = new File(MusicPlayerActivity.MEDIA_PATH);

		File[] listFile = file.listFiles(new FileExtensionFilter());
		if (listFile != null) {
			for (File makeFoldre : listFile) {

				songListFiles.add(makeFoldre.getAbsolutePath());
			}

		}

		return songListFiles;
	}
	
	/**
	 * Class to filter files which are having .mp3 extension
	 * */
	class FileExtensionFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			return (name.endsWith(".mp3") || name.endsWith(".MP3"));
		}
	}

	public File getCacheFolder(Context context) {
		File cacheDir = null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			cacheDir = new File(Environment.getExternalStorageDirectory(), "songs");
			if(!cacheDir.isDirectory()) {
				cacheDir.mkdirs();
			}
		}

		if(!cacheDir.isDirectory()) {
			cacheDir = context.getCacheDir(); //get system cache folder
		}

		return cacheDir;
	}

}
