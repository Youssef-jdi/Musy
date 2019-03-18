package com.youssef.yasmine.musy.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.youssef.yasmine.musy.Adapter.PlaylistFavorisAdapter;
import com.youssef.yasmine.musy.Adapter.PlaylistTracksAdapter;
import com.youssef.yasmine.musy.Model.Playlist;
import com.youssef.yasmine.musy.Model.Track;
import com.youssef.yasmine.musy.R;
import com.youssef.yasmine.musy.Util.ConnectedUser;
import com.youssef.yasmine.musy.Util.ServerConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlaylistFavorisActivity extends AppCompatActivity {

    private String connected_user;
    private RecyclerView recyclerViewPlaylist;
    private List<Playlist> listPlaylist;
    private  RecyclerView.Adapter adapterPlaylist;
    private String url ;
    private String server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_favoris);

        ConnectedUser connectedUser = ConnectedUser.getInstance();
        connected_user = connectedUser.getConnected_user();
        ServerConnection serverConnection = ServerConnection.getInstance();
        server = serverConnection.getServer();
        url = server+"/getPlay/";
        Log.d("connected_user_id", connected_user);

      //  connected_user = getIntent().getStringExtra("connected_user");
       // Log.d("connected_user", connected_user);

        recyclerViewPlaylist = findViewById(R.id.rvplaylistfavoris);
        recyclerViewPlaylist.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        listPlaylist = new ArrayList<>();
        adapterPlaylist = new PlaylistFavorisAdapter(listPlaylist, getApplicationContext());
        recyclerViewPlaylist.setAdapter(adapterPlaylist);
        getData();
    }

    private void getData() {


        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url+connected_user, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String nickname = jsonObject.getString("name_playlist");
                        String id = jsonObject.getString("id");
                        String id_user = jsonObject.getString("id_user");
                        Playlist playlist = new Playlist();
                        playlist.setTitle(nickname);
                        playlist.setId(id);
                        playlist.setUser_id(id_user);
                        Log.d("teeeest", nickname);

                        listPlaylist.add(playlist);

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
                adapterPlaylist.notifyDataSetChanged();

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
