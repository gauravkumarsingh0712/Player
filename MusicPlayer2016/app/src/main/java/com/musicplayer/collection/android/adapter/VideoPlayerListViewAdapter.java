package com.musicplayer.collection.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.musicplayer.collection.android.R;

import java.util.ArrayList;

/**
 * Created by gauravkumar.singh on 4/28/2016.
 */
public class VideoPlayerListViewAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    private Context context;
    private ViewHolder viewHolder;
    private ArrayList<String> videoSongNameList = new ArrayList<>();

    public VideoPlayerListViewAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.videoSongNameList = arrayList;
    }


    @Override
    public int getCount() {
        return videoSongNameList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.video_list_adapter,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView
                    .findViewById(R.id.song_name_list);
            viewHolder.imageView = (ImageView) convertView
                    .findViewById(R.id.image_of_songs);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        try {
            viewHolder.textView
                    .setText(videoSongNameList.get(position));

        } catch (Exception e) {
            e.printStackTrace();
        }

        // TODO Auto-generated method stub
        return convertView;
    }

    public static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}
