package com.youssef.yasmine.musy.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.youssef.yasmine.musy.Activity.MusicPlayerActivity;
import com.youssef.yasmine.musy.Model.Playlist;
import com.youssef.yasmine.musy.R;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    private Context context;
    private List<Playlist> list;


    public PlaylistAdapter(List<Playlist> list, Context context) {
        this.context = context;
        this.list = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_playlist, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Playlist playlist = list.get(position);

        holder.txtTitle.setText(playlist.getTitle());
        holder.txtntracks.setText(String.valueOf(playlist.getNb_tracks()));
        Picasso.with(context).load(playlist.getPicture()).into(holder.imgPlaylist);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MusicPlayerActivity.class);
                intent.putExtra("tracklist", playlist.getTrackList());
                context.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle;
        public TextView txtntracks;
        public ImageView imgPlaylist;
        ConstraintLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.idNomPlaylist);
            txtntracks = itemView.findViewById(R.id.idNbTracks);
            imgPlaylist = itemView.findViewById(R.id.idImgPlaylist);
            parentLayout = itemView.findViewById(R.id.idlayoutPlaylist);
        }
    }

}
