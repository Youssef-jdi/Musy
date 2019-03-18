package com.youssef.yasmine.musy.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.youssef.yasmine.musy.Adapter.PlaylistPlayerAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaylistActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Playlist> listPlaylist;

    private  RecyclerView.Adapter adapter;
   // private String id_user = "8";
    private String url ;
    private String addUrl;
    private Button add;
    private Track track;
    private String server;
    private String connected_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
       // connected_user = getIntent().getStringExtra("connected_user");
        ConnectedUser connectedUser = ConnectedUser.getInstance();
        connected_user = connectedUser.getConnected_user();
        ServerConnection serverConnection = ServerConnection.getInstance();
        server = serverConnection.getServer();
        url = server+"/getPlay/";
        addUrl = server+"/addplay";
        Log.d("connected_user_id", connected_user);
        getData();
        recyclerView = findViewById(R.id.rvplaylistplayer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        listPlaylist = new ArrayList<>();
        Track track = new Track();
        Artist artist = new Artist();
        artist.setName(getIntent().getStringExtra("artist"));
        Album album = new Album();
        album.setTitle(getIntent().getStringExtra("album"));
        track.setPreview(getIntent().getStringExtra("preview"));
        album.setCover(getIntent().getStringExtra("cover"));
        track.setTitle(getIntent().getStringExtra("title"));
        track.setId(getIntent().getStringExtra("trackid"));
        track.setAlbum(album);
        track.setArtist(artist);
        adapter = new PlaylistPlayerAdapter(listPlaylist, getApplicationContext(),track);
        recyclerView.setAdapter(adapter);

        add = findViewById(R.id.addplaylist);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlaylist();
                adapter.notifyDataSetChanged();
            }
        });



    }

    private void addPlaylist(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PlaylistActivity.this);
        alertDialog.setMessage("Entre name");
        final EditText editText = new EditText(PlaylistActivity.this);
        alertDialog.setView(editText);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               // add(editText.getText().toString());
                addPlay(editText.getText().toString());
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    public void addPlay(final String name){
        StringRequest postRequest = new StringRequest(Request.Method.POST, addUrl,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        listPlaylist.clear();
                        getData();
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("erouuuur", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("name", name);
                params.put("id_user",connected_user);
                return  params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(postRequest);
        adapter.notifyDataSetChanged();
    }

    private void getData() {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url+connected_user, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String nickname = jsonObject.getString("name_playlist");
                        String id = jsonObject.getString("id");
                        Playlist playlist = new Playlist();
                        playlist.setTitle(nickname);
                        playlist.setId(id);
                        Log.d("teeeest", nickname);

                        listPlaylist.add(playlist);

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
                adapter.notifyDataSetChanged();

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
