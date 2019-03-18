package com.youssef.yasmine.musy.Activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.youssef.yasmine.musy.Adapter.AdapterMteei;
import com.youssef.yasmine.musy.Model.Video;
import com.youssef.yasmine.musy.R;
import com.youssef.yasmine.musy.Util.ConnectedUser;
import com.youssef.yasmine.musy.Util.ServerConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FeedFragment extends Fragment {

    RecyclerView recyclerView;
    List<Video> list;
    AdapterMteei adapterMteei;
    VideoView videoView;
    private View currentFocusedLayout, oldFocusedLayout;
    private ProgressBar progressBar;
    private String connected_user;
    private String server;
    private OnFragmentInteractionListener mListener;

    public FeedFragment() {
        // Required empty public constructor
    }


    public static FeedFragment newInstance() {
        FeedFragment fragment = new FeedFragment();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_feed, container, false);


        ConnectedUser connectedUser = ConnectedUser.getInstance();
        connected_user = connectedUser.getConnected_user();
        ServerConnection serverConnection = ServerConnection.getInstance();
        server = serverConnection.getServer();


        fetchdata();


        recyclerView = rootview.findViewById(R.id.rv);
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapterMteei = new AdapterMteei(list,getContext());
        recyclerView.setAdapter(adapterMteei);
        recyclerView.setHasFixedSize(true);




        return  rootview;
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


    private void fetchdata(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(server+"/getvideos/"+connected_user, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {


                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Video video = new Video();
                        video.setVideo_path(jsonObject.getString("video_path"));
                        video.setTrack(jsonObject.getString("track"));
                        video.setId_user(jsonObject.getString("id_user"));
                        list.add(video);
                        adapterMteei.notifyDataSetChanged();
                        Log.d("video", video.getVideo_path());
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
}
