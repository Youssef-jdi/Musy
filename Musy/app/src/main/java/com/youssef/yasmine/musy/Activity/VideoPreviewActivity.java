package com.youssef.yasmine.musy.Activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.youssef.yasmine.musy.R;
import com.youssef.yasmine.musy.Util.ConnectedUser;
import com.youssef.yasmine.musy.Util.ServerConnection;
import com.youssef.yasmine.musy.Util.VolleyMultipartRequest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VideoPreviewActivity extends AppCompatActivity {
    private String connected_user;
    VideoView videoView;
    ImageButton save, upload;
    //String urlSong = "https://cdns-preview-d.dzcdn.net/stream/c-deda7fa9316d9e9e880d2c6207e92260-5.mp3";
    String urlSong;
    String videofile;
    final int request_code = 99;
    private RequestQueue rQueue;
    private ArrayList<HashMap<String, String>> arraylist;
    private String uploadUrl;
    int downloadedSize = 0;
    int totalSize = 0;
    MediaPlayer mediaPlayer;
    private String server;
   // private String id_user= "10";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_preview);
        ConnectedUser connectedUser = ConnectedUser.getInstance();
        connected_user = connectedUser.getConnected_user();
        ServerConnection serverConnection = ServerConnection.getInstance();
        server = serverConnection.getServer();
        uploadUrl = server+"/video/post";
        Log.d("connected_user_id", connected_user);
        videoView = findViewById(R.id.videoView);
        // video = getIntent().getStringExtra("video");
        mediaPlayer = new MediaPlayer();
       // loadplayer(urlSong);
        mediaPlayer.setVolume(20,20);
        videofile = getIntent().getStringExtra("path");
        urlSong = getIntent().getStringExtra("url");
        upload = findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
        videoView.setVideoURI(Uri.parse(videofile));
       videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
           @Override
           public void onCompletion(MediaPlayer mp) {
               mediaPlayer.pause();
           }
       });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVolume(0,0);

            }
        });


        videoView.requestFocus();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                videoView.start();
                loadplayer(urlSong);
            }
        });



        save = findViewById(R.id.button2);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(VideoPreviewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, request_code);

                } else {
                    exportMp4ToGallery(getApplicationContext(), videofile);
                    //  saveVideo(video);
                    // galleryAddPic(videofile);
                    Log.d("saving", "saving");
                }

            }
        });

    }

    public void loadplayer(String url){
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    public void exportMp4ToGallery(Context context, String filePath) {

        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            final ContentValues values = new ContentValues(2);
            values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
            values.put(MediaStore.Video.Media.DATA, filePath);
            context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    values);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.parse("Musy://" + filePath)));

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(filePath)));
        } else {
            Toast.makeText(getApplicationContext(), "not granted", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //  super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case request_code: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    exportMp4ToGallery(getApplicationContext(), videofile);
                    // saveVideo(video);
                    // galleryAddPic(videofile);
                    Log.d("saving", "saving");
                }
            }
        }
    }


    private void upload() {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, uploadUrl,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                       // Log.d("ressssssoo", new String(response.data));
                        rQueue.getCache().clear();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "erreur 1 " + error.getMessage(), Toast.LENGTH_SHORT).show();
                       // Log.d("erreur", error.getMessage());
                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_user",connected_user);
                params.put("url",urlSong);
                return params;
            }

            /*
             *pass files using below method
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                try {
                    File myfile = new File(videofile);

                    params.put("videoURL", new DataPart(myfile.getName(), fullyReadFileToBytes(new File(videofile))));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return params;
            }
        };


        rQueue = Volley.newRequestQueue(this);
        rQueue.add(volleyMultipartRequest);
    }


    private byte[] fullyReadFileToBytes(File f) throws IOException {
        int size = (int) f.length();
        byte bytes[] = new byte[size];
        byte tmpBuff[] = new byte[size];
        FileInputStream fis = new FileInputStream(f);
        ;
        try {

            int read = fis.read(bytes, 0, size);
            if (read < size) {
                int remain = size - read;
                while (remain > 0) {
                    read = fis.read(tmpBuff, 0, remain);
                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                    remain -= read;
                }
            }
        } catch (IOException e) {
            throw e;
        } finally {
            fis.close();
        }

        return bytes;
    }


    public void download() {
        int count;
        try {
            URL url = new URL(urlSong);
            URLConnection conexion = url.openConnection();
            conexion.connect();
            // this will be useful so that you can show a tipical 0-100% progress bar
            int lenghtOfFile = conexion.getContentLength();

            // downlod the file
            InputStream input = new BufferedInputStream(url.openStream());
            //File file = File.createTempFile(new Date().toString(),null,getCacheDir());
            File file = new File(getExternalCacheDir() + "/yasmine.aac");
            OutputStream output = new FileOutputStream(file);
            Log.d("path", file.getAbsolutePath());
            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
           // mux(file.getAbsolutePath());
           // muxing(file.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("erreur 1", e.getMessage());
        } catch (MalformedURLException e) {
            Log.d("erreur 2", e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("erreur 3", e.getMessage());
            e.printStackTrace();
        }

    }






}