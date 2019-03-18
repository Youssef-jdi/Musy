package com.youssef.yasmine.musy.Activity;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.youssef.yasmine.musy.Adapter.AlbumAdapter;
import com.youssef.yasmine.musy.Adapter.ArtistAdapter;
import com.youssef.yasmine.musy.Adapter.PlaylistAdapter;
import com.youssef.yasmine.musy.Adapter.TrackAdapter;
import com.youssef.yasmine.musy.Adapter.TrackArtistAdapter;
import com.youssef.yasmine.musy.Model.Album;
import com.youssef.yasmine.musy.Model.Artist;
import com.youssef.yasmine.musy.Model.Playlist;
import com.youssef.yasmine.musy.Model.Track;
import com.youssef.yasmine.musy.R;
import com.youssef.yasmine.musy.Util.ConnectedUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArtistActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSimilar;
    private List<Artist> listSimilar;
    private RecyclerView.Adapter adapterSimilar;

    private RecyclerView recyclerViewAlbum;
    private List<Album> listAlbum;
    private RecyclerView.Adapter adapterAlbum;

    private RecyclerView recyclerViewPlaylist;
    private  RecyclerView.Adapter adapterPlaylist;
    private List<Playlist> listPlaylist;

    private RecyclerView recyclerViewTracks;
    private  RecyclerView.Adapter adapterTracks;
    private List<Track> listTracks;

    private TextView btnViewAll;
    private String name;
    private String connected_user;



    private String urlSimilar;
    private String urlAlbum;
    private String urlPlaylist;
    private String urlTopTracks;
    private String urlArtist;

    private String idArtist;

    private TextView artistName;
    private TextView artistFans;
    private CircleImageView artistePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        ConnectedUser connectedUser = ConnectedUser.getInstance();
        connected_user = connectedUser.getConnected_user();
        Log.d("connected_user_id", connected_user);
        Log.d("hellloooo", "helllo");
        idArtist = getIntent().getStringExtra("artist");
        Log.d("iiiiiiid", idArtist);
        urlSimilar = "https://api.deezer.com/artist/"+idArtist+"/related";
        urlAlbum = "https://api.deezer.com/artist/"+idArtist+"/albums";
        urlPlaylist = "https://api.deezer.com/artist/"+idArtist+"/playlists";
        urlTopTracks = "https://api.deezer.com/artist/"+idArtist+"/top";
        urlArtist = "https://api.deezer.com/artist/"+idArtist;

        artistName = (TextView)findViewById(R.id.ProfileArtistName);
        artistFans = (TextView)findViewById(R.id.ProfileArtistNbFan);
        artistePicture = (CircleImageView) findViewById(R.id.ProfileArtistImg);
        getArtist();

        btnViewAll = findViewById(R.id.viewAllAlbum);
        btnViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ArtistActivity.this, AllAlbumsActivity.class);
                intent.putExtra("idartist",idArtist);
                startActivity(intent);
            }
        });

        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(name);
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });


        recyclerViewSimilar = findViewById(R.id.ProfileArtistRecycler3);
        recyclerViewSimilar.setLayoutManager(new LinearLayoutManager(ArtistActivity.this, LinearLayoutManager.HORIZONTAL, false));
        listSimilar = new ArrayList<>();
        adapterSimilar = new ArtistAdapter(listSimilar, this);
        recyclerViewSimilar.setAdapter(adapterSimilar);
        getSimilar();

        recyclerViewAlbum = findViewById(R.id.ProfileArtistRecycler2);
        recyclerViewAlbum.setLayoutManager(new GridLayoutManager(ArtistActivity.this, 2));
        listAlbum = new ArrayList<>();
        adapterAlbum = new AlbumAdapter(listAlbum, this);
        recyclerViewAlbum.setAdapter(adapterAlbum);
        getAlbums();

        recyclerViewPlaylist = findViewById(R.id.ProfileArtistRecyclerView4);
        recyclerViewPlaylist.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        listPlaylist = new ArrayList<>();
        adapterPlaylist = new PlaylistAdapter(listPlaylist, getApplicationContext());
        recyclerViewPlaylist.setAdapter(adapterPlaylist);
        getRelatedPlaylists();

        recyclerViewTracks = findViewById(R.id.ProfileArtistRecycler1);
        recyclerViewTracks.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        listTracks = new ArrayList<>();
        adapterTracks = new TrackArtistAdapter(listTracks, getApplicationContext());
        recyclerViewTracks.setAdapter(adapterTracks);
        getTopTracks();

    }

    private void getTopTracks() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlTopTracks, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject j = jsonArray.getJSONObject(i);
                        String id = j.getString("id");
                        String title = j.getString("title_short");
                        String preview = j.getString("preview");
                        JSONObject JArtist = j.getJSONObject("artist");
                        String artisteName = JArtist.getString("name");
                        JSONObject Jalbum = j.getJSONObject("album");
                        String albumName = Jalbum.getString("title");
                        String cover = Jalbum.getString("cover_medium");
                        Track track = new Track();
                        Artist artist = new Artist();
                        artist.setName(artisteName);
                        Album album = new Album();
                        album.setTitle(albumName);
                        album.setCover(cover);
                        track.setAlbum(album);
                        track.setId(id);
                        track.setTitle(title);
                        track.setPreview(preview);
                        track.setArtist(artist);
                        listTracks.add(track);


                    }
                    Log.d("profil artiste track", listTracks.toString());
                    adapterTracks.notifyDataSetChanged();

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
        RequestQueue requestQueue = Volley.newRequestQueue(ArtistActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getSimilar() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlSimilar, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < 10; i++) {
                        JSONObject j = jsonArray.getJSONObject(i);
                        int id = j.getInt("id");
                        String name = j.getString("name");
                        String picture = j.getString("picture_medium");
                        int nb_fan = j.getInt("nb_fan");
                        Artist artist = new Artist();
                        artist.setId(String.valueOf(id));
                        artist.setName(name);
                        artist.setPicture(picture);
                        artist.setNb_fan(nb_fan);
                        listSimilar.add(artist);
                    }

                    adapterSimilar.notifyDataSetChanged();

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
        RequestQueue requestQueue = Volley.newRequestQueue(ArtistActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getAlbums() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlAlbum, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < 4; i++) {
                        JSONObject j = jsonArray.getJSONObject(i);
                        int id = j.getInt("id");
                        String title = j.getString("title");
                        String cover = j.getString("cover_medium");
                        String tracklist = j.getString("tracklist");
                       int nb_tracks = j.getInt("nb_tracks");
                        int nb_fan = j.getInt("fans");
                        Album album = new Album();
                        album.setId(String.valueOf(id));
                        album.setNb_tracks(nb_tracks);
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
        RequestQueue requestQueue = Volley.newRequestQueue(ArtistActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getRelatedPlaylists() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlPlaylist, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for(int i = 1 ; i<jsonArray.length(); i++){
                        JSONObject j = jsonArray.getJSONObject(i);
                        String title = j.getString("title");
                        String imgPlaylist = j.getString("picture_medium");
                        String tracklist = j.getString("tracklist");
                        Playlist playlist  = new Playlist();
                        playlist.setTitle(title);
                        playlist.setPicture(imgPlaylist);
                        playlist.setTrackList(tracklist);
                        listPlaylist.add(playlist);
                        Log.d("TRACKS!", tracklist);

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

    private void getArtist() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlArtist, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject j = new JSONObject(response);
                    // int id = j.getInt("id");
                     name = j.getString("name");
                    String picture = j.getString("picture_medium");
                    int nb_album = j.getInt("nb_album");
                    int nb_fan = j.getInt("nb_fan");
                    artistName.setText(name);
                    artistFans.setText(String.valueOf(nb_fan));
                    Picasso.with(ArtistActivity.this).load(picture).into(artistePicture);


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
        RequestQueue requestQueue = Volley.newRequestQueue(ArtistActivity.this);
        requestQueue.add(stringRequest);
    }

}
