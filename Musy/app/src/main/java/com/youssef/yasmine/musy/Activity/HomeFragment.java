package com.youssef.yasmine.musy.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.youssef.yasmine.musy.Adapter.AlbumAdapter;
import com.youssef.yasmine.musy.Adapter.ArtistAdapter;
import com.youssef.yasmine.musy.Adapter.GenreAdapter;
import com.youssef.yasmine.musy.Adapter.PlaylistAdapter;
import com.youssef.yasmine.musy.Adapter.TrackAdapter;
import com.youssef.yasmine.musy.Model.Album;
import com.youssef.yasmine.musy.Model.Artist;
import com.youssef.yasmine.musy.Model.Genre;
import com.youssef.yasmine.musy.Model.Playlist;
import com.youssef.yasmine.musy.Model.Track;
import com.youssef.yasmine.musy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {


    private OnFragmentInteractionListener mListener;


    private  static final String urlGenre = "https://api.deezer.com/genre/";
    private  static final String urlChart = "https://api.deezer.com/chart";

    private RecyclerView recyclerViewGenre;
    private  RecyclerView.Adapter adapterGenre;
    private List<Genre> listGenre;

    private RecyclerView recyclerViewPlaylist;
    private  RecyclerView.Adapter adapterPlaylist;
    private List<Playlist> listPlaylist;

    private RecyclerView recyclerViewTrack;
    private  RecyclerView.Adapter adapterTrack;
    private List<Track> listTrack;

    private RecyclerView recyclerViewArtist;
    private  RecyclerView.Adapter adapterArtist;
    private List<Artist> listArtist;



    private TextView textMore;



    public HomeFragment() {
    }

    public static  HomeFragment newInstance() {
        return new  HomeFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview =  inflater.inflate(R.layout.fragment_home, container, false);

        recyclerViewGenre = rootview.findViewById(R.id.idRecyclerGenre);
        recyclerViewGenre.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        listGenre = new ArrayList<>();
        adapterGenre = new GenreAdapter(listGenre, getContext());
        recyclerViewGenre.setAdapter(adapterGenre);
        getData();

        recyclerViewPlaylist = rootview.findViewById(R.id.idRecyclerPlaylist);
        recyclerViewPlaylist.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        listPlaylist = new ArrayList<>();
        adapterPlaylist = new PlaylistAdapter(listPlaylist, getContext());
        recyclerViewPlaylist.setAdapter(adapterPlaylist);
        getPlaylistPicks();

        recyclerViewTrack = rootview.findViewById(R.id.idRecyclerPopular);
        recyclerViewTrack.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        listTrack = new ArrayList<>();
        adapterTrack = new TrackAdapter(listTrack, getContext());
        recyclerViewTrack.setAdapter(adapterTrack);
        getTracks();

        recyclerViewArtist = rootview.findViewById(R.id.idRecyclerArtist);
        recyclerViewArtist.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        listArtist = new ArrayList<>();
        adapterArtist = new ArtistAdapter(listArtist, getContext());
        recyclerViewArtist.setAdapter(adapterArtist);
        getTopArtists();




        textMore = rootview.findViewById(R.id.moreGenre);
        textMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllGenreActivity.class);
                startActivity(intent);
            }
        });

        return rootview;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void getData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlGenre, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for(int i = 1 ; i<5; i++){
                        JSONObject j = jsonArray.getJSONObject(i);
                        int id = j.getInt("id");
                        String name = j.getString("name");
                        String picture = j.getString("picture_medium");
                        Genre genre = new Genre(String.valueOf(id),name, picture);
                        listGenre.add(genre);
                    }
                    adapterGenre.notifyDataSetChanged();

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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void getPlaylistPicks() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlChart, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonTracks = jsonObject.getJSONObject("playlists");
                    JSONArray jsonArray = jsonTracks.getJSONArray("data");
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void getTracks() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlChart, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonTracks = jsonObject.getJSONObject("tracks");
                    JSONArray jsonArray = jsonTracks.getJSONArray("data");
                    for(int i = 1 ; i<jsonArray.length(); i++){
                        JSONObject j = jsonArray.getJSONObject(i);
                        String title = j.getString("title");
                        String id = j.getString("id");
                        String preview = j.getString("preview");
                        JSONObject JArtist = j.getJSONObject("artist");
                        String artiste = JArtist.getString("name");
                        JSONObject JAlbum = j.getJSONObject("album");
                        String imageAlbum = JAlbum.getString("cover_big");
                        String titlealbum= JAlbum.getString("title");

                        Track track = new Track();
                        track.setId(id);
                        track.setPreview(preview);
                        Artist artist = new Artist();
                        Album album = new Album();
                        album.setTitle(titlealbum);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void getTopArtists() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlChart, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonTracks = jsonObject.getJSONObject("artists");
                    JSONArray jsonArray = jsonTracks.getJSONArray("data");
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

                        Log.d("TRACKS!", topTrackArtist);

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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


}
