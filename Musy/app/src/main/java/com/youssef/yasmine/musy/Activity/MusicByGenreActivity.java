package com.youssef.yasmine.musy.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.youssef.yasmine.musy.Adapter.ArtistAdapter;
import com.youssef.yasmine.musy.Adapter.PlaylistAdapter;
import com.youssef.yasmine.musy.Adapter.TrackAdapter;
import com.youssef.yasmine.musy.Model.Album;
import com.youssef.yasmine.musy.Model.Artist;
import com.youssef.yasmine.musy.Model.Playlist;
import com.youssef.yasmine.musy.Model.Track;
import com.youssef.yasmine.musy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MusicByGenreActivity extends AppCompatActivity {

    String urlArtist;
    String urlPlaylist;
    String urltracks;
    String idgenre;

    private   String urlGenre;

    private RecyclerView recyclerViewPlaylist;
    private RecyclerView.Adapter adapterPlaylist;
    private List<Playlist> listPlaylist;

    private RecyclerView recyclerViewTrack;
    private RecyclerView.Adapter adapterTrack;
    private List<Track> listTrack;

    private RecyclerView recyclerViewArtist;
    private RecyclerView.Adapter adapterArtist;
    private List<Artist> listArtist;

    private TextView nomGenre;
    private ImageView imageGenre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_by_genre);
        Log.d("hellloooo", "helllo");
        idgenre = getIntent().getStringExtra("genreid");
        Log.d("iiiiiiid", idgenre);
        nomGenre = findViewById(R.id.nomGenre);
        imageGenre  = findViewById(R.id.backdrop);
        urlGenre = "https://api.deezer.com/genre/"+idgenre;
        urlArtist = "https://api.deezer.com/chart/" + idgenre + "/artists";
        urlPlaylist = "https://api.deezer.com/chart/" + idgenre + "/playlists";
        urltracks = "https://api.deezer.com/chart/" + idgenre + "/tracks";

        getGenre();
        recyclerViewPlaylist = findViewById(R.id.idRecyclerPlaylist);
        recyclerViewPlaylist.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        listPlaylist = new ArrayList<>();
        adapterPlaylist = new PlaylistAdapter(listPlaylist, getApplicationContext());
        recyclerViewPlaylist.setAdapter(adapterPlaylist);
        getPlaylistPicks();

        recyclerViewTrack = findViewById(R.id.idRecyclerPopular);
        recyclerViewTrack.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        listTrack = new ArrayList<>();
        adapterTrack = new TrackAdapter(listTrack, getApplicationContext());
        recyclerViewTrack.setAdapter(adapterTrack);
        getTracks();

        recyclerViewArtist = findViewById(R.id.idRecyclerArtist);
        recyclerViewArtist.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        listArtist = new ArrayList<>();
        adapterArtist = new ArtistAdapter(listArtist, getApplicationContext());
        recyclerViewArtist.setAdapter(adapterArtist);
        getTopArtists();

    }

    private void getPlaylistPicks() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlPlaylist, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for(int i = 1 ; i<jsonArray.length(); i++){
                        JSONObject j = jsonArray.getJSONObject(i);
                        String title = j.getString("title");
                        int nb_tracks = j.getInt("nb_tracks");
                        String imgPlaylist = j.getString("picture_big");
                        String tracklist = j.getString("tracklist");
                        Playlist playlist  = new Playlist();
                        playlist.setTitle(title);
                        playlist.setNb_tracks(nb_tracks);
                        playlist.setPicture(imgPlaylist);
                        playlist.setTrackList(tracklist);
                        listPlaylist.add(playlist);
                        Log.d("PLAYLISTS!", tracklist);

                    }
                    adapterPlaylist.notifyDataSetChanged();

                }catch (JSONException e){
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void getTracks() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urltracks, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray =jsonObject.getJSONArray("data");
                    for(int i = 1 ; i<jsonArray.length(); i++){
                        JSONObject j = jsonArray.getJSONObject(i);
                        String title = j.getString("title");
                        String preview = j.getString("preview");
                        JSONObject JArtist = j.getJSONObject("artist");
                        String artiste = JArtist.getString("name");
                        JSONObject JAlbum = j.getJSONObject("album");
                        String imageAlbum = JAlbum.getString("cover_big");
                        Track track = new Track();
                        Artist artist = new Artist();
                        Album album = new Album();
                        artist.setName(artiste);
                        album.setCover(imageAlbum);
                        track.setAlbum(album);
                        track.setArtist(artist);
                        track.setTitle(title);
                        listTrack.add(track);


                    }
                    adapterTrack.notifyDataSetChanged();

                }catch (JSONException e){
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void getTopArtists() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlArtist, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for(int i = 1 ; i<jsonArray.length(); i++){
                        JSONObject j = jsonArray.getJSONObject(i);
                        int id = j.getInt("id");
                        String name = j.getString("name");
                        String imgArtist = j.getString("picture_big");
                        String topTrackArtist = j.getString("tracklist");
                        Artist artist = new Artist();
                        artist.setId(String.valueOf(id));
                        artist.setPicture(imgArtist);
                        artist.setName(name);
                        listArtist.add(artist);

                        Log.d("ARTIST!", topTrackArtist);

                    }
                    adapterArtist.notifyDataSetChanged();

                }catch (JSONException e){
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void getGenre() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlGenre, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);

                        String title = jsonObject.getString("name");
                        String image = jsonObject.getString("picture_big");

                        nomGenre.setText(title);
                    Picasso.with(getApplicationContext()).load(image).into(imageGenre);

                }catch (JSONException e){
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

}
