package com.youssef.yasmine.musy.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youssef.yasmine.musy.Model.Track;
import com.youssef.yasmine.musy.R;

import java.util.List;

public class PlaylistTracksAdapter extends RecyclerView.Adapter<PlaylistTracksAdapter.ViewHolder>  {

    private Context context;
    private List<Track> list;


    public PlaylistTracksAdapter(List<Track> list, Context context) {
        this.context = context;
        this.list = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_playlist_tracks, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Track track = list.get(position);

        holder.txtTitle.setText(track.getTitle());
        holder.txtArtist.setText(track.getArtiste().getName());





    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle;
        public TextView txtArtist;
        LinearLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.trackName);
            txtArtist = itemView.findViewById(R.id.trackArtist);
          //  parentLayout = itemView.findViewById(R.id.idLayoutPlaylist);
        }
    }

}
