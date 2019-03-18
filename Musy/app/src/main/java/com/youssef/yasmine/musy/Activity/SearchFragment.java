package com.youssef.yasmine.musy.Activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.youssef.yasmine.musy.Adapter.AlbumAdapter;
import com.youssef.yasmine.musy.Adapter.ArtistAdapter;
import com.youssef.yasmine.musy.Adapter.Search.AlbumSearchAdapter;
import com.youssef.yasmine.musy.Adapter.Search.ArtistSeachAdapter;
import com.youssef.yasmine.musy.Adapter.Search.UserSearchAdapter;
import com.youssef.yasmine.musy.Adapter.UserAdapter;
import com.youssef.yasmine.musy.Model.Album;
import com.youssef.yasmine.musy.Model.Artist;
import com.youssef.yasmine.musy.Model.User;
import com.youssef.yasmine.musy.R;
import com.youssef.yasmine.musy.Util.ConnectedUser;
import com.youssef.yasmine.musy.Util.ServerConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements UserSearchAdapter.UserAdapterListener, ArtistSeachAdapter.ArtistSearchAdapterListener,
AlbumSearchAdapter.AlbumAdapterListener{


    private SearchView searchView;
    private static final String urlAlbum = "https://api.deezer.com/search/album?q=";
    private static final String urlArtist = "https://api.deezer.com/search/artist?q=";
    private static  String urlProfile ;

    RequestQueue requestQueue1;
    private String server;
    private RecyclerView recyclerViewProfile;
    private  UserSearchAdapter adapterProfile;
    private List<User> listProfile;

    private RecyclerView recyclerViewAlbum;
    private List<Album> listAlbum;
    private AlbumSearchAdapter adapterAlbum;

    private RecyclerView recyclerViewArtist;
    private  ArtistSeachAdapter adapterArtist;
    private List<Artist> listArtist;
    private String connected_user;



    private OnFragmentInteractionListener mListener;

    public SearchFragment() {

    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        ConnectedUser connectedUser = ConnectedUser.getInstance();
        connected_user = connectedUser.getConnected_user();
        ServerConnection serverConnection = ServerConnection.getInstance();
        server = serverConnection.getServer();
        urlProfile = server+"/search/";
        Log.d("connected_user_id", connected_user);

       // connected_user = this.getArguments().getString("connected_user");
      //  Log.d("connected_user", connected_user);

        searchView = rootView.findViewById(R.id.searchView);
        recyclerViewAlbum = rootView.findViewById(R.id.rvSearchAlbum);
        recyclerViewAlbum.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        listAlbum = new ArrayList<>();
        adapterAlbum = new AlbumSearchAdapter(listAlbum, getContext(), this);
        recyclerViewAlbum.setAdapter(adapterAlbum);

        recyclerViewArtist = rootView.findViewById(R.id.rvSearchArtist);
        recyclerViewArtist.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        listArtist = new ArrayList<>();
        adapterArtist = new ArtistSeachAdapter(listArtist, getContext() , this);
        recyclerViewArtist.setAdapter(adapterArtist);

        recyclerViewProfile = rootView.findViewById(R.id.rvSearchProfile);
        recyclerViewProfile.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        listProfile = new ArrayList<>();
         adapterProfile = new UserSearchAdapter(listProfile,getContext(),this);
        recyclerViewProfile.setAdapter(adapterProfile);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                listProfile.clear();
                searchByUser(query);
                adapterProfile.getFilter().filter(query);
                adapterProfile.notifyDataSetChanged();

                listArtist.clear();
                searchByArtist(query);
                adapterArtist.getFilter().filter(query);
                adapterArtist.notifyDataSetChanged();

                listAlbum.clear();
                searchByAlbum(query);
                adapterAlbum.getFilter().filter(query);
                adapterAlbum.notifyDataSetChanged();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                Log.d("shooooow", newText);
                listProfile.clear();
                adapterProfile.getFilter().filter(newText);
                searchByUser(newText);
                adapterProfile.notifyDataSetChanged();

                listArtist.clear();
                searchByArtist(newText);
                adapterArtist.getFilter().filter(newText);
                adapterArtist.notifyDataSetChanged();

                listAlbum.clear();
                searchByAlbum(newText);
                adapterAlbum.getFilter().filter(newText);
                adapterAlbum.notifyDataSetChanged();

                return true;
            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onUserSelected(User contact) {
        Log.d("naarach chnaamel beha ", contact.getNickname());
    }

    @Override
    public void onArtistSelected(Artist contact) {

    }

    @Override
    public void onAlbumSelected(Album contact) {

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void searchByAlbum(String searchText) {
        listAlbum.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlAlbum + searchText, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Albuuum", response);

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < 5; i++) {
                        JSONObject j = jsonArray.getJSONObject(i);
                        String id = j.getString("id");
                        String title = j.getString("title");
                        String cover = j.getString("cover");
                        Album album = new Album();
                        album.setTitle(title);
                        album.setId(id);
                        album.setCover(cover);
                        listAlbum.add(album);
                        Log.d("seaaarcg", "seaaaarch");
                    }

                    adapterAlbum.notifyDataSetChanged();

                    requestQueue1.getCache().clear();

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
       requestQueue1 = Volley.newRequestQueue(getContext());
        requestQueue1.add(stringRequest);
    }

    private void searchByUser(String searchText) {

        listProfile.clear();
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlProfile+searchText, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Log.d("hello", String.valueOf(response));
                        JSONObject jsonObject = response.getJSONObject(i);
                        String nickname = jsonObject.getString("nickname");
                        String picture = jsonObject.getString("picture");
                        String id = jsonObject.getString("id");

                        User user = new User();
                        user.setNickname(nickname);
                        user.setId(id);
                        user.setPicture(picture);
                        listProfile.add(user);
                        Log.d("seaaarcg", "seaaaarch");
                        adapterProfile.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }

    private void searchByArtist(String searchText) {
        listArtist.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlArtist + searchText, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    Log.d("ARTIIIIST", response);

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < 5; i++) {
                        JSONObject j = jsonArray.getJSONObject(i);
                        String id = j.getString("id");
                        String title = j.getString("name");
                        int nb_fan = j.getInt("nb_fan");
                        String cover = j.getString("picture");
                        Artist artist = new Artist();
                        artist.setName(title);
                        artist.setNb_fan(nb_fan);
                        artist.setPicture(cover);
                        artist.setId(id);
                        listArtist.add(artist);
                        Log.d("seaaarcg", "seaaaarch");
                    }

                    adapterArtist.notifyDataSetChanged();

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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
