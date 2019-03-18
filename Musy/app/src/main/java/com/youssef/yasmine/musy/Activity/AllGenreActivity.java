package com.youssef.yasmine.musy.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.youssef.yasmine.musy.Adapter.GenreAdapter;
import com.youssef.yasmine.musy.Model.Genre;
import com.youssef.yasmine.musy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllGenreActivity extends AppCompatActivity {

    private RecyclerView recyclerViewGenre;
    private  RecyclerView.Adapter adapterGenre;
    private List<Genre> listGenre;
    private  static final String urlGenre = "https://api.deezer.com/genre/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_genre);
        recyclerViewGenre = findViewById(R.id.recyclerViewAllGenre);
        recyclerViewGenre.setLayoutManager(new GridLayoutManager(AllGenreActivity.this, 2));
        listGenre = new ArrayList<>();
        adapterGenre = new GenreAdapter(listGenre, getApplicationContext());
        recyclerViewGenre.setAdapter(adapterGenre);
        getData();
    }

    private void getData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlGenre, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for(int i = 1 ; i<jsonArray.length(); i++){
                        JSONObject j = jsonArray.getJSONObject(i);
                        int id = j.getInt("id");
                        String name = j.getString("name");
                        String picture = j.getString("picture_big");
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
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
