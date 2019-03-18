package com.youssef.yasmine.musy.Activity;

import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.youssef.yasmine.musy.Adapter.PlaylistTracksAdapter;
import com.youssef.yasmine.musy.Model.Album;
import com.youssef.yasmine.musy.Model.Artist;
import com.youssef.yasmine.musy.Model.Playlist;
import com.youssef.yasmine.musy.Model.Track;
import com.youssef.yasmine.musy.R;
import com.youssef.yasmine.musy.Util.ConnectedUser;
import com.youssef.yasmine.musy.Util.ServerConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlaylistTracksActivity extends AppCompatActivity  {

    private RecyclerView recyclerViewTrack;
    private List<Track> listTrack;
    private  RecyclerView.Adapter adapterTrack;
    private  String  connected_user;
    private String tracksUrl;
    private Button BtnPlay;
    private String playlist_id;
    private String server;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_tracks);
        ConnectedUser connectedUser = ConnectedUser.getInstance();
        connected_user = connectedUser.getConnected_user();
        ServerConnection serverConnection = ServerConnection.getInstance();
        server = serverConnection.getServer();
        tracksUrl = server+"/getTracks/";
        Log.d("connected_user_id", connected_user);
       playlist_id = getIntent().getStringExtra("playlist_id");
        Log.d("palylistid", playlist_id);
        BtnPlay = findViewById(R.id.playplaylists);

        recyclerViewTrack = findViewById(R.id.rvPlaylistTracks);
        recyclerViewTrack.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        listTrack = new ArrayList<>();
        getData();
        adapterTrack = new PlaylistTracksAdapter(listTrack, getApplicationContext());
        recyclerViewTrack.setAdapter(adapterTrack);


    }

    private void setbtn(){
        if(listTrack.size()!=0){
            BtnPlay.setEnabled(true);
            BtnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PlaylistTracksActivity.this, MusicPlaylerPlaylistActivity.class);
                    intent.putExtra("list", (Serializable) listTrack);
                    startActivity(intent);
                }
            });
        }else{
            BtnPlay.setEnabled(false);

        }

    }


    private void getData() {


         JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(tracksUrl+playlist_id, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String idTrack = jsonObject.getString("id");
                        String nameTrack = jsonObject.getString("name");
                        String album = jsonObject.getString("album");
                        String artist = jsonObject.getString("artist");
                        String preview = jsonObject.getString("url");
                        String cover = jsonObject.getString("cover");
                       Track track = new Track();
                       track.setId(idTrack);
                       track.setTitle(nameTrack);
                       track.setPreview(preview);
                        Album album1 = new Album();
                        album1.setTitle(album);
                        album1.setCover(cover);
                        track.setAlbum(album1);
                        Artist artist1 = new Artist();
                        artist1.setName(artist);
                        track.setArtist(artist1);

                        listTrack.add(track);

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
                Log.d("nbre of tradks", String.valueOf(listTrack.size()));
                for (Track track:
                     listTrack) {
                    Log.d("tradsk title", track.getTitle());
                }
                setbtn();
                adapterTrack.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}
