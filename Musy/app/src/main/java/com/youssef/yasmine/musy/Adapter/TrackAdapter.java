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
import com.youssef.yasmine.musy.Activity.MusicPlaylerPlaylistActivity;
import com.youssef.yasmine.musy.Model.Track;
import com.youssef.yasmine.musy.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {

    private Context context;
    private List<Track> list;

    public TrackAdapter(List<Track> list, Context context) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_track, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Track track = list.get(position);

        holder.txtTitle.setText(track.getTitle());
        holder.txtArtist.setText(track.getArtiste().getName());
        Picasso.with(context).load(track.getAlbum().getCover()).into(holder.imgPopular);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MusicPlaylerPlaylistActivity.class);
                List<Track> newlist = new ArrayList<>();
                newlist.add(track);
                intent.putExtra("list", (Serializable) newlist);


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
        public TextView txtArtist;
        public ImageView imgPopular;
        ConstraintLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.idtitlePopular);
            txtArtist = itemView.findViewById(R.id.idArtistPopular);
            imgPopular = itemView.findViewById(R.id.idImgPopular);
            parentLayout = itemView.findViewById(R.id.idLayoutPopular);
        }

    }

}
