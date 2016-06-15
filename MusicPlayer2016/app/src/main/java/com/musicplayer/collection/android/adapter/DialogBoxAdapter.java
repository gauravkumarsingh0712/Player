package com.musicplayer.collection.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.musicplayer.collection.android.R;
import com.musicplayer.collection.android.model.SongsInfoDto;

import java.util.ArrayList;

/**
 * Created by gauravkumar.singh on 5/4/2016.
 */
public class DialogBoxAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    public ViewHolder viewHolder;
    private Context context;
    private SongsInfoDto songsInfoDto;
    private ArrayList<String> listNameArrayList = new ArrayList<>();

    public DialogBoxAdapter(Context context,ArrayList<String> arrayList) {

        this.context = context;
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.listNameArrayList = arrayList;

        songsInfoDto = SongsInfoDto.getInstance();
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listNameArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.dialog_box_layout,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView
                    .findViewById(R.id.text_view);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        try {
            viewHolder.textView.setText(listNameArrayList.get(position));

            notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // TODO Auto-generated method stub
        return convertView;
    }

    public static class ViewHolder {
        TextView textView;

    }

}
