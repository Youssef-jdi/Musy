package com.youssef.yasmine.musy.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youssef.yasmine.musy.Activity.PlaylistTracksActivity;
import com.youssef.yasmine.musy.Model.Playlist;
import com.youssef.yasmine.musy.R;

import java.util.List;

public class PlaylistFavorisAdapter extends RecyclerView.Adapter<PlaylistFavorisAdapter.ViewHolder> {

    private Context context;
    private List<Playlist> list;


    public PlaylistFavorisAdapter(List<Playlist> list, Context context) {
        this.context = context;
        this.list = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_playlist_player, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Playlist playlist = list.get(position);

        holder.playlist.setText(playlist.getTitle());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlaylistTracksActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("playlist_id", playlist.getId());
                context.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView playlist;

        LinearLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            playlist= itemView.findViewById(R.id.playlistplayer);
            parentLayout = itemView.findViewById(R.id.layoutplaylistplayer);
        }
    }
}
