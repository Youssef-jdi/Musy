package com.youssef.yasmine.musy.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.youssef.yasmine.musy.Adapter.UsersVideosAdapter;
import com.youssef.yasmine.musy.Model.Video;
import com.youssef.yasmine.musy.R;
import com.youssef.yasmine.musy.Util.ConnectedUser;
import com.youssef.yasmine.musy.Util.ServerConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProfileVideosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private  RecyclerView.Adapter adapter;
    private List<Video> list;
    private String connected_user;
    private String url;
    private String server;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_videos);
        ConnectedUser cu = ConnectedUser.getInstance();
        connected_user = cu.getConnected_user();
        ServerConnection serverConnection = ServerConnection.getInstance();
        server = serverConnection.getServer();
        url = server+"/getvideoid/";
        recyclerView = findViewById(R.id.ekherrecycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        fetchvideo();
        list = new ArrayList<>();
        adapter= new UsersVideosAdapter(list, this);
        recyclerView.setAdapter(adapter);
    }

    private void fetchvideo(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url+connected_user, new Response.Listener<JSONArray>() {
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
                        adapter.notifyDataSetChanged();
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}
