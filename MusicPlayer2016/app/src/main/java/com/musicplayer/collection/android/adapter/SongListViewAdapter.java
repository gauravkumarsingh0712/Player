package com.musicplayer.collection.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.musicplayer.collection.android.R;
import com.musicplayer.collection.android.activity.PlayListActivity;
import com.musicplayer.collection.android.fragment.SongsListFragment;
import com.musicplayer.collection.android.interfac.UpdateUIAdapterInterface;
import com.musicplayer.collection.android.model.LocalModel;
import com.musicplayer.collection.android.model.SongsInfoDto;

import es.claucookie.miniequalizerlibrary.EqualizerView;

public class SongListViewAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    public ViewHolder viewHolder;
    private Context context;
    private SongsInfoDto songsInfoDto;
    private int index;
    public SongListViewAdapter(Context context,UpdateUIAdapterInterface updateUIAdapterInterface) {

        this.context = context;
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LocalModel.getInstance().setUpdateUIAdapterInterface(updateUIAdapterInterface);

        songsInfoDto = SongsInfoDto.getInstance();

    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return songsInfoDto.getSongPathArray().size();
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
    public View getView(int position, View convertView, ViewGroup parent)
    {

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.song_list_adapter,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView
                    .findViewById(R.id.song_name_list);
            viewHolder.imageView = (ImageView) convertView
                    .findViewById(R.id.image_of_songs);
            viewHolder.equalizer = (EqualizerView) convertView.findViewById(R.id.equalizer_view);
            viewHolder.spinnerImageView = (ImageView) convertView.findViewById(R.id.Spinner_image_view);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        try {
            viewHolder.spinnerImageView.setTag(position);
            viewHolder.textView.setText(songsInfoDto.getSongNameArray().get(position));
            viewHolder.imageView.setImageBitmap(songsInfoDto.getSongImageBitmapArray().get(position));

            if (position == SongsListFragment.pos) {
                viewHolder.equalizer.setVisibility(View.VISIBLE);
                viewHolder.equalizer.animateBars();
                viewHolder.textView.setTextColor(context.getResources().getColor(R.color.Red));
                viewHolder.textView.setSelected(true);
            } else {
                viewHolder.equalizer.setVisibility(View.GONE);
                viewHolder.equalizer.stopBars();
                viewHolder.textView.setTextColor(context.getResources().getColor(R.color.black));
                viewHolder.textView.setSelected(false);
            }

            System.out.println("position is : "+position);

            initListener();

            notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // TODO Auto-generated method stub
        return convertView;
    }

    public static class ViewHolder {
        TextView textView;
        ImageView imageView;
        EqualizerView equalizer;
        ImageView spinnerImageView;

    }

    private void initListener() {

        viewHolder.spinnerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = (Integer) v.getTag();

                Intent in = new Intent(context,
                        PlayListActivity.class);
                in.putExtra("position", index);
                in.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                context.startActivity(in);
            }
        });
    }

}
