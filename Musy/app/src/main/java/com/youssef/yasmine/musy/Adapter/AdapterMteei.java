package com.youssef.yasmine.musy.Adapter;

import android.content.Context;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.youssef.yasmine.musy.Model.User;
import com.youssef.yasmine.musy.Model.Video;
import com.youssef.yasmine.musy.R;
import com.youssef.yasmine.musy.Util.ServerConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterMteei extends RecyclerView.Adapter<AdapterMteei.ViewHolder> {
    private Context context;
    private List<Video> list;
   // private MediaPlayer mediaPlayer;
    private User user;
    private String urlUser ;
    private String server;
    public AdapterMteei(List<Video> list, Context context) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_feed, parent, false);
        ServerConnection serverConnection = ServerConnection.getInstance();
        server = serverConnection.getServer();
        urlUser = server+"/user/";
        return new ViewHolder(v);


    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Video video = list.get(position);
        fetchuser(video.getId_user());
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Picasso.with(context).load(user.getPicture()).into(holder.taswira);
                holder.name.setText(user.getNickname());
            }
        },1000);
        holder.progressBar.setVisibility(View.VISIBLE);
        final MediaController mediaController = new MediaController(context);
        mediaController.setAnchorView(holder.parentLayout);
        holder.video.setMediaController(mediaController);
        mediaController.setMediaPlayer(new MediaController.MediaPlayerControl() {
            @Override
            public void start() {

                holder.video.start();
            }

            @Override
            public void pause() {
                holder.video.stopPlayback();

            }

            @Override
            public int getDuration() {
                return 0;
            }

            @Override
            public int getCurrentPosition() {
                return 0;
            }

            @Override
            public void seekTo(int pos) {

            }

            @Override
            public boolean isPlaying() {
                return false;
            }

            @Override
            public int getBufferPercentage() {
                return 0;
            }

            @Override
            public boolean canPause() {
                return false;
            }

            @Override
            public boolean canSeekBackward() {
                return false;
            }

            @Override
            public boolean canSeekForward() {
                return false;
            }

            @Override
            public int getAudioSessionId() {
                return 0;
            }
        });
        holder.video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                holder.progressBar.setVisibility(View.GONE);
                mp.setVolume(0f,0f);
            }
        });
        holder.video.setVideoURI(Uri.parse(video.getVideo_path()));










    }



    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public VideoView video;
        public ProgressBar progressBar;
        public CircleImageView taswira;
        public TextView name;
        ConstraintLayout parentLayout;



        public ViewHolder(View itemView) {
            super(itemView);
            taswira = itemView.findViewById(R.id.taswirataataswira);
            name = itemView.findViewById(R.id.textView11);
            progressBar = itemView.findViewById(R.id.progress);
            video = itemView.findViewById(R.id.videoview);
            parentLayout = itemView.findViewById(R.id.item);


        }



    }

    private void fetchuser(String id){

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlUser+id, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("la reponse la", String.valueOf(response));
                for (int i = 0; i < response.length(); i++) {
                    try {

                        JSONObject jsonObject = response.getJSONObject(i);
                        String nickname = jsonObject.getString("nickname");
                        String picture = jsonObject.getString("picture");
                        String id = jsonObject.getString("id");

                        user = new User();
                        user.setNickname(nickname);
                        user.setId(id);
                        user.setPicture(picture);


                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley taa", error.toString());

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
    }



}
