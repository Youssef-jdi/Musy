package com.youssef.yasmine.musy.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.youssef.yasmine.musy.Model.Playlist;
import com.youssef.yasmine.musy.Model.Track;
import com.youssef.yasmine.musy.R;
import com.youssef.yasmine.musy.Util.ServerConnection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaylistPlayerAdapter extends RecyclerView.Adapter<PlaylistPlayerAdapter.ViewHolder> {

    private Context context;
    private List<Playlist> list;
    private String addUrl ;
    private Track track;
    private String server;
    public PlaylistPlayerAdapter(List<Playlist> list, Context context, Track track) {
        this.context = context;
        this.list = list;
        this.track = track;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_playlist_player, viewGroup, false);
        ServerConnection serverConnection = ServerConnection.getInstance();
        server = serverConnection.getServer();
        addUrl = server+"/addtrackplaylist";
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Playlist playlist = list.get(i);

        viewHolder.txtTitle.setText(playlist.getTitle());



        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlay(track.getTitle(),String.valueOf(playlist.getId()),track.getId(),track.getArtiste().getName(),track.getAlbum().getTitle(),track.getPreview(),track.getAlbum().getCover());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle;

        LinearLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.playlistplayer);

            parentLayout = itemView.findViewById(R.id.layoutplaylistplayer);
        }
    }

    public void addPlay(final String name, final String idplaylist, final String idTrack, final String artist , final String album,final String preview,final String cover){
        StringRequest postRequest = new StringRequest(Request.Method.POST, addUrl,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context,"successful",Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                Log.d("track name", name);
                Log.d("idtrack", idTrack);
                Log.d("artist", artist);
                Log.d("idplaylist", idplaylist);
                Log.d("album", album);

                params.put("name", name);
                params.put("id",idTrack);
                params.put("artist",artist);
                params.put("album",album);
                params.put("playlistid",idplaylist);
                params.put("url",preview);
                params.put("cover",cover);
                return  params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(postRequest);

    }
}
