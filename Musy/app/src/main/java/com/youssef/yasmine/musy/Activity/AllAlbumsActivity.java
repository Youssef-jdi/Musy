package com.youssef.yasmine.musy.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.youssef.yasmine.musy.Adapter.AlbumAdapter;
import com.youssef.yasmine.musy.Model.Album;
import com.youssef.yasmine.musy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllAlbumsActivity extends AppCompatActivity {


    private RecyclerView recyclerViewAlbum;
    private List<Album> listAlbum;
    private RecyclerView.Adapter adapterAlbum;

    private String urlAlbum;
    private String idArtist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_albums);

        idArtist = getIntent().getStringExtra("idartist");
        urlAlbum = "https://api.deezer.com/artist/"+idArtist+"/albums";

        recyclerViewAlbum = findViewById(R.id.recyclerViewAllAlbum);
        recyclerViewAlbum.setLayoutManager(new GridLayoutManager(AllAlbumsActivity.this, 2));
        listAlbum = new ArrayList<>();
        adapterAlbum = new AlbumAdapter(listAlbum, this);
        recyclerViewAlbum.setAdapter(adapterAlbum);
        getAlbums();
    }

    private void getAlbums() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlAlbum, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject j = jsonArray.getJSONObject(i);
                        int id = j.getInt("id");
                        String title = j.getString("title");
                        String cover = j.getString("cover_medium");
                        String tracklist = j.getString("tracklist");
                        int nb_fan = j.getInt("fans");
                        Album album = new Album();
                        album.setId(String.valueOf(id));
                        album.setTitle(title);
                        album.setCover(cover);
                        album.setTrackList(tracklist);
                        album.setNb_fans(nb_fan);
                        listAlbum.add(album);

                    }
                    Log.d("profil artiste track", listAlbum.toString());
                    adapterAlbum.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(AllAlbumsActivity.this);
        requestQueue.add(stringRequest);
    }
}
