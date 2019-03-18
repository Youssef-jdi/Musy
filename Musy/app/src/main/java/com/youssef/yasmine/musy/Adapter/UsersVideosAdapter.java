package com.youssef.yasmine.musy.Adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;
import com.youssef.yasmine.musy.Model.User;
import com.youssef.yasmine.musy.Model.Video;
import com.youssef.yasmine.musy.R;

import java.io.IOException;
import java.util.List;

public class UsersVideosAdapter extends RecyclerView.Adapter<UsersVideosAdapter.ViewHolder> {

    private Context context;
    private List<Video> list;


    public UsersVideosAdapter(List<Video> list, Context context) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_uers_videos, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Video video = list.get(position);
        final MediaController mediaController = new MediaController(context);
        mediaController.setAnchorView(holder.parentLayout);
        holder.videoView.setMediaController(mediaController);
        holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVolume(0f,0f);
                holder.videoView.start();
            }
        });
        holder.videoView.setVideoURI(Uri.parse(video.getVideo_path()));





    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public VideoView videoView;
        public LinearLayout parentLayout;



        public ViewHolder(View itemView) {
            super(itemView);

            videoView = itemView.findViewById(R.id.videoViewUsers);
            parentLayout = itemView.findViewById(R.id.idlayoutUser);


        }

    }

}


