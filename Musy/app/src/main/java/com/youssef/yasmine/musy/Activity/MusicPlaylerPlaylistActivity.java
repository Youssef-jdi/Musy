package com.youssef.yasmine.musy.Activity;

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
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.tankery.lib.circularseekbar.CircularSeekBar;

public class MusicPlaylerPlaylistActivity extends AppCompatActivity implements MediaPlayer.OnBufferingUpdateListener,MediaPlayer.OnCompletionListener{
    List<Track> list;
    String trackurl;
    String imageAlbum;
    View mContainerView;
    CircleImageView img;
    ImageButton previous, play, next;
    MediaPlayer mp;
    CircularSeekBar seekBarProgress;
    ListView recyclerView;
    private String connected_user;
  //  public static ArrayList<Track> youssef;
    RecycleAdapter recycleAdapter;
    private int i = 0;
    private final Handler handler = new Handler();
    private int mediaFileLengthInMilliseconds;
    ImageButton video, playlist;
    Chronometer chronometer;
    private long pauseOffset;
    private TextView song, artiste;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        ConnectedUser connectedUser = ConnectedUser.getInstance();
        connected_user = connectedUser.getConnected_user();
        Log.d("connected_user_id", connected_user);
        setContentView(R.layout.activity_musicplayer);
        list = new ArrayList<>();
        list = (List<Track>) getIntent().getSerializableExtra("list");
        trackurl = getIntent().getStringExtra("tracklist");
        song = (TextView) findViewById(R.id.ghneya);
        artiste = (TextView) findViewById(R.id.mogheniAlbum);


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
                Intent intent = new Intent(MusicPlaylerPlaylistActivity.this,PlaylistActivity.class);
                intent.putExtra("trackid",list.get(i).getId());
                intent.putExtra("album",list.get(i).getAlbum().getTitle());
                Log.d("albumname", list.get(i).getAlbum().getTitle());
                intent.putExtra("artist",list.get(i).getArtiste().getName());
                intent.putExtra("title",list.get(i).getTitle());
                intent.putExtra("cover",list.get(i).getAlbum().getCover());
                intent.putExtra("preview",list.get(i).getPreview());
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
                loadplayer(list.get(i));
            }
        });


        loadplayer(list.get(i));


        recycleAdapter = new RecycleAdapter(getApplicationContext(),list);
        recyclerView.setAdapter(recycleAdapter);
        recycleAdapter.notifyDataSetChanged();
        setVideoClick();




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
        Intent intent = new Intent(MusicPlaylerPlaylistActivity.this,CameraActivity.class);
        Log.d("preivew", list.get(i).getPreview());
        intent.putExtra("track",list.get(i).getPreview());
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
            Picasso.with(getApplicationContext()).load(list.get(i).getAlbum().getCover()).into(img);
            mp.setDataSource(list.get(i).getPreview());
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

        song.setText(list.get(i).getTitle());
        artiste.setText(list.get(i).getArtiste().getName());
    }

    private void playprevious(){
        if(i>0){
            i--;
            mp.reset();
            loadplayer(list.get(i));
            chronometer.setBase(SystemClock.elapsedRealtime());
            pauseOffset = 0;
        }
        else {
            i = list.size()-1;
            mp.reset();
            loadplayer(list.get(i));
            chronometer.setBase(SystemClock.elapsedRealtime());
            pauseOffset = 0;
        }

    }


    private void playnext(){
        if(i<list.size()-1){
            i++;
            mp.reset();
            loadplayer(list.get(i));
            chronometer.setBase(SystemClock.elapsedRealtime());
            pauseOffset = 0;
        }
        else {
            i = 0;
            mp.reset();
            loadplayer(list.get(i));
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


}
