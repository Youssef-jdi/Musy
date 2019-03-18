package com.youssef.yasmine.musy.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.youssef.yasmine.musy.Model.Track;
import com.youssef.yasmine.musy.R;

import java.util.ArrayList;
import java.util.List;

public class RecycleAdapter extends ArrayAdapter<Track> {
    private Context context;
    private ArrayList<Track> list;

    static class ViewHolder {
        TextView ArtistName;
        TextView SongName;

    }



    public RecycleAdapter(Context context, List<Track> objects) {
        super(context, R.layout.playlist_item_recycle,objects);
    }


    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        Track track = getItem(position);
        ViewHolder viewHolder;
        final View result;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.playlist_item_recycle,parent,false);
            viewHolder.ArtistName =  convertView.findViewById(R.id.song_artist);
            viewHolder.SongName = convertView.findViewById(R.id.song_title);
            result = convertView;
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        viewHolder.SongName.setText(track.getTitle());
        viewHolder.ArtistName.setText(track.getArtiste().getName());

        return convertView;
    }
}


