package com.youssef.yasmine.musy.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.youssef.yasmine.musy.Activity.MusicPlayerAbumActivity;
import com.youssef.yasmine.musy.Activity.MusicPlayerActivity;
import com.youssef.yasmine.musy.Model.Album;
import com.youssef.yasmine.musy.R;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private Context context;
    private List<Album> list;


    public AlbumAdapter(List<Album> list, Context context) {
        this.context = context;
        this.list = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_album, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Album album = list.get(position);

        holder.txtTitle.setText(album.getTitle());
        holder.txtnbtracks.setText(String.valueOf(album.getNb_tracks()));
        Picasso.with(context).load(album.getCover()).into(holder.imgAlbum);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MusicPlayerAbumActivity.class);
                intent.putExtra("imagealbum",album.getCover());
                intent.putExtra("albumname",album.getTitle());
                intent.putExtra("tracklist", album.getTrackList());
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
        public TextView txtnbtracks;
        public ImageView imgAlbum;
        ConstraintLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.idNomAlbum);
            txtnbtracks = itemView.findViewById(R.id.idNbAlbumTracks);
            imgAlbum = itemView.findViewById(R.id.idImgAlbum);
            parentLayout = itemView.findViewById(R.id.idlayoutAlbum);
        }
    }





}
