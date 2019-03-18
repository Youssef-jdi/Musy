package com.youssef.yasmine.musy.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.youssef.yasmine.musy.Adapter.RecycleAdapter;
import com.youssef.yasmine.musy.Model.Album;
import com.youssef.yasmine.musy.Model.Artist;
import com.youssef.yasmine.musy.Model.Track;
import com.youssef.yasmine.musy.R;
import com.youssef.yasmine.musy.Util.BlurBuilder;
import com.youssef.yasmine.musy.Util.ConnectedUser;
import com.youssef.yasmine.musy.Util.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import me.tankery.lib.circularseekbar.CircularSeekBar;

public class MusicPlayerActivity  extends AppCompatActivity implements MediaPlayer.OnBufferingUpdateListener,MediaPlayer.OnCompletionListener {
    // String url = "https://cdns-preview-d.dzcdn.net/stream/c-deda7fa9316d9e9e880d2c6207e92260-5.mp3";
    String trackurl ;


    View mContainerView;
    CircleImageView img;
    ImageButton previous,play,next;
    MediaPlayer mp;
    CircularSeekBar seekBarProgress;
    ListView recyclerView;
    public static ArrayList<Track> youssef ;
    RecycleAdapter recycleAdapter;
    private int i=0;
    private final Handler handler = new Handler();
    private int mediaFileLengthInMilliseconds;
    ImageButton video,playlist ;
    Chronometer chronometer;
    private  long pauseOffset;
    private TextView song,artiste;
    public ArrayList<Track> favoris;
    DBHelper dbHelper;
    private String connected_user;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicplayer);
        //connected_user = getIntent().getStringExtra("connected_user");
        ConnectedUser connectedUser = ConnectedUser.getInstance();
        connected_user = connectedUser.getConnected_user();
        Log.d("connected_user_id", connected_user);
       //   favoris = new ArrayList<>();

        trackurl = getIntent().getStringExtra("tracklist");
        song = (TextView) findViewById(R.id.ghneya);
        artiste = (TextView) findViewById(R.id.mogheniAlbum);

       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               fetchData();
           }
       });

        youssef = new ArrayList<>();
        video = findViewById(R.id.video);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mContainerView = findViewById(R.id.mcontainerview);
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.taswirasplash);
        Bitmap blurredBitmap = BlurBuilder.blur( getApplicationContext(), originalBitmap );
        mContainerView.setBackground(new BitmapDrawable(getResources(), blurredBitmap));
        mp = new MediaPlayer();
        chronometer = findViewById(R.id.chronometer);
        playlist = findViewById(R.id.playlist);
        playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MusicPlayerActivity.this,PlaylistActivity.class);
                intent.putExtra("trackid",youssef.get(i).getId());
                intent.putExtra("album",youssef.get(i).getAlbum().getTitle());
                intent.putExtra("artist",youssef.get(i).getArtiste().getName());
                intent.putExtra("title",youssef.get(i).getTitle());
                intent.putExtra("preview",youssef.get(i).getPreview());
                intent.putExtra("cover",youssef.get(i).getAlbum().getCover());
                intent.putExtra("connected_user",connected_user);
                mp.pause();
                startActivity(intent);
            }
        });
        img = findViewById(R.id.img);
        play = findViewById(R.id.play);
        previous = findViewById(R.id.back);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playprevious();
            }
        });
        next = findViewById(R.id.next);
        seekBarProgress = findViewById(R.id.seekBar);
        seekBarProgress.setMax(99);
        seekBarProgress.setCircleColor(R.color.colorAccent);
        recyclerView = findViewById(R.id.recycleviewplaylist);

        mp.setOnBufferingUpdateListener(this);
        mp.setOnCompletionListener(this);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    playnext();


            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mp.isPlaying()) {
                    mp.start();
                    chronometer.setBase(SystemClock.elapsedRealtime()- pauseOffset);
                    chronometer.start();

                    play.setBackground(getDrawable(R.drawable.pause));
                } else {
                    mp.pause();
                    play.setBackground(getDrawable(R.drawable.play));
                    chronometer.stop();
                    pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();

                }
                primarySeekBarProgressUpdater();



            }
        });

        seekBarProgress.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {

            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

                CircularSeekBar sb = (CircularSeekBar) seekBar;
                int playPositionInMillisecconds = Math.round((mediaFileLengthInMilliseconds / 100) * sb.getProgress());
                mp.seekTo(playPositionInMillisecconds);


            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });

        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mp.reset();
                i = position;
                loadplayer(youssef.get(i));
            }
        });
        dbHelper = new DBHelper(getApplicationContext());
        favoris = dbHelper.getTracks();





    }

   /* public void setFavoris(int j){
        for(Track tr:favoris) {
            if(youssef.get(i).getId().equals(tr.getId())){
                like.setImageDrawable(getDrawable(R.drawable.like));
            }
            else {
                like.setImageDrawable(getDrawable(R.drawable.dislike));
            }

        }
    }*/



    public void updateUi(){
       Intent intent = new Intent(MusicPlayerActivity.this,CameraActivity.class);
        Log.d("preivew", youssef.get(i).getPreview());
        intent.putExtra("track",youssef.get(i).getPreview());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        mp.pause();
        super.onBackPressed();
    }

    public void loadplayer(Track track){
        //setFavoris(i);
        try {
            settext();
            chronometer.start();
            Picasso.with(getApplicationContext()).load(youssef.get(i).getAlbum().getCover()).into(img);
            mp.setDataSource(youssef.get(i).getPreview());
            mp.prepare();
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    mediaFileLengthInMilliseconds = mp.getDuration();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }






    }

    private void settext(){

        song.setText(youssef.get(i).getTitle());
        artiste.setText(youssef.get(i).getArtiste().getName());
    }

    private void playprevious(){
        if(i>0){
            i--;
            mp.reset();
            loadplayer(youssef.get(i));
            chronometer.setBase(SystemClock.elapsedRealtime());
            pauseOffset = 0;
        }
        else {
            i = youssef.size()-1;
            mp.reset();
            loadplayer(youssef.get(i));
            chronometer.setBase(SystemClock.elapsedRealtime());
            pauseOffset = 0;
        }

    }


    private void playnext(){
        if(i<24){
            i++;
            mp.reset();
            loadplayer(youssef.get(i));
            chronometer.setBase(SystemClock.elapsedRealtime());
            pauseOffset = 0;
        }
        else {
            i = 0;
            mp.reset();
            loadplayer(youssef.get(i));
            chronometer.setBase(SystemClock.elapsedRealtime());
            pauseOffset = 0;
        }

    }



    private void primarySeekBarProgressUpdater() {
        seekBarProgress.setProgress((int) (((float) mp.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100));
        if (mp.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    primarySeekBarProgressUpdater();
                }
            };
            handler.postDelayed(notification, 1000);
        }
    }


    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        //  seekBarProgress.setSecondaryProgress(percent);
    }



    @Override
    public void onCompletion(MediaPlayer mp) {
        //  play.setBackground(getDrawable(R.drawable.play));
        playnext();
    }

    private void setVideoClick(){
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.pause();
                updateUi();
            }
        });
    }

    private void fetchData(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,trackurl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("data");

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject objet = data.getJSONObject(i);
                       Track track = new Track();
                        track.setId(objet.getString("id"));
                        track.setTitle(objet.getString("title"));
                        track.setPreview(objet.getString("preview"));
                        Log.d("title", objet.getString("title"));
                        Artist artist = new Artist();
                        JSONObject artistObject = objet.getJSONObject("artist");
                        artist.setName(artistObject.getString("name"));
                        artist.setId(artistObject.getString("id"));
                        Album album = new Album();
                        JSONObject albumObject = objet.getJSONObject("album");
                        album.setTitle(albumObject.getString("title"));
                        album.setCover(albumObject.getString("cover_medium"));
                        track.setAlbum(album);
                        track.setArtist(artist);
                        youssef.add(track);

                        /*    Gson gson = new Gson();
                            Type listType = new TypeToken<ArrayList<Track>>() {}.getType();
                            youssef = gson.fromJson(data.toString(), listType);*/


                    }

                     loadplayer(youssef.get(i));


                    recycleAdapter = new RecycleAdapter(getApplicationContext(),youssef);
                    recyclerView.setAdapter(recycleAdapter);
                    recycleAdapter.notifyDataSetChanged();
                    setVideoClick();



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("erroor", error.getMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }







}
