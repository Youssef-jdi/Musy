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
import com.youssef.yasmine.musy.Adapter.UserAdapter;
import com.youssef.yasmine.musy.Model.User;
import com.youssef.yasmine.musy.R;
import com.youssef.yasmine.musy.Util.ConnectedUser;
import com.youssef.yasmine.musy.Util.ServerConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListFollowingActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFollowing;
    private  RecyclerView.Adapter adapterFollowing;
    private List<User> listFollowing;
    private String server;
    private String urlFollowing ;
   private String connected_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_following);

        //connected_user = getIntent().getStringExtra("connected_user");
        ConnectedUser connectedUser = ConnectedUser.getInstance();
        connected_user = connectedUser.getConnected_user();
        Log.d("connected_user_id", connected_user);
        ServerConnection serverConnection = ServerConnection.getInstance();
        server = serverConnection.getServer();
        urlFollowing = server+"/userfollowing/"+connected_user;
        recyclerViewFollowing = findViewById(R.id.recyclerViewFollowing);
        recyclerViewFollowing.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        listFollowing = new ArrayList<>();
        adapterFollowing = new UserAdapter(listFollowing, getApplicationContext());
        recyclerViewFollowing.setAdapter(adapterFollowing);
        getFollowing();
    }

    private void getFollowing() {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlFollowing, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Log.d("helloFollowing", String.valueOf(response));
                        JSONObject jsonObject = response.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String nickname = jsonObject.getString("nickname");
                        String image = jsonObject.getString("picture");
                        User user = new User();
                        user.setId(id);
                        user.setNickname(nickname);
                        user.setPicture(image);
                        listFollowing.add(user);


                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }
                adapterFollowing.notifyDataSetChanged();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArrayRequest);
    }
}
