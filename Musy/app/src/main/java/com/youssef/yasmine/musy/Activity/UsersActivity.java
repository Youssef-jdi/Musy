package com.youssef.yasmine.musy.Activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.youssef.yasmine.musy.Adapter.AdapterMteei;
import com.youssef.yasmine.musy.Adapter.GenreAdapter;
import com.youssef.yasmine.musy.Adapter.UsersVideosAdapter;
import com.youssef.yasmine.musy.Model.Genre;
import com.youssef.yasmine.musy.Model.Video;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersActivity extends AppCompatActivity {

    private   String postUrl ;
    private   String postUrl2 ;
    private String check ;
    private Button btnfollow;
    private CircleImageView imageView;
    private TextView nb_Followers;
    private TextView nb_Following;
    private TextView name;
    private String usersId;
    private String connected_user;
    private RecyclerView recyclerView;
    private  RecyclerView.Adapter adapter;
    private List<Video> list;
   private String test;
    private String url ;
    private String urlNbFollowing ;
    private String urlNbFollowers ;
    private String urlvid ;
    private String server;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        ConnectedUser connectedUser = ConnectedUser.getInstance();
        connected_user = connectedUser.getConnected_user();
        ServerConnection serverConnection = ServerConnection.getInstance();
        server = serverConnection.getServer();
        postUrl = server+"/user/follow";
        postUrl2 = server+"/user/deletefollow";
        check = server+"/checkuserfollow";
        url = server+"/user/";
        urlNbFollowing = server+"/numberfollowing/";
        urlNbFollowers = server+"/numberfollowers/";
        urlvid = server+"/getvideoid/";
        Log.d("connected_user_id", connected_user);
        usersId = getIntent().getStringExtra("idUser");
       // connected_user = getIntent().getStringExtra("connected_user");


        recyclerView = findViewById(R.id.rvVideosUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        list = new ArrayList<>();
        fetchvideo();
        adapter= new UsersVideosAdapter(list, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.GONE);
        imageView = findViewById(R.id.imageUsers);
        name = findViewById(R.id.nameUsers);
        nb_Followers = findViewById(R.id.followersUsers);
        nb_Following = findViewById(R.id.followingUsers);
        btnfollow = findViewById(R.id.btnFollow);
        check(connected_user,usersId);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(test.equals("true")){
                    recyclerView.setVisibility(View.VISIBLE);
                    btnfollow.setText("Unfollow");
                }
                else {
                    recyclerView.setVisibility(View.GONE);
                    btnfollow.setText("Follow");
                }
                getData();
                getFollowersNumber();
                getFollowingNumber();
            }
        },1000);




        btnfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(test.equals("true")){
                    sendPost2(usersId, connected_user);
                    test = "false";
                    btnfollow.setText("Follow");


                }
                else {
                    sendPost(usersId,connected_user);
                    test = "true";
                    btnfollow.setText("Unfollow");

                }
            }
        });



    }










    private void fetchvideo(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlvid+usersId, new Response.Listener<JSONArray>() {
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






    private void check(String id_user,String id_following){
        StringRequest postRequest = new StringRequest(Request.Method.GET, check+"/"+id_user+"/"+id_following,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("test following", response);
                        test = response;
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(postRequest);
    }


    public void sendPost(final String id_user, final String id_follower){
        StringRequest postRequest = new StringRequest(Request.Method.POST, postUrl,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id_user", id_user);
                params.put("id_following", id_follower);
                // params.put("id", String.valueOf(account.getAccount().name));

                return  params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(postRequest);


    }

    public void sendPost2(final String id_user, final String id_following){
        StringRequest postRequest = new StringRequest(Request.Method.GET, postUrl2+"/"+id_user+"/"+id_following,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(postRequest);


    }

    private void getData() {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url+usersId, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Log.d("hello", String.valueOf(response));
                        JSONObject jsonObject = response.getJSONObject(i);
                        String nickname = jsonObject.getString("nickname");
                        String image = jsonObject.getString("picture");
                        Picasso.with(getApplicationContext()).load(image).into(imageView);
                        name.setText(nickname);

                        Log.d("teeeest", nickname);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArrayRequest);
    }

    private void getFollowingNumber() {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlNbFollowing+usersId, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Log.d("hello", String.valueOf(response));
                        JSONObject jsonObject = response.getJSONObject(i);
                        String following = jsonObject.getString("following");
                       nb_Following.setText(following);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArrayRequest);
    }

    private void getFollowersNumber() {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlNbFollowers+usersId, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Log.d("hello", String.valueOf(response));
                        JSONObject jsonObject = response.getJSONObject(i);
                        String followers= jsonObject.getString("followers");
                        nb_Followers.setText(followers);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArrayRequest);
    }

}

