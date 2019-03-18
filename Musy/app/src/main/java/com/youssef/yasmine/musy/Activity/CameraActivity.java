package com.youssef.yasmine.musy.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.youssef.yasmine.musy.R;

public class CameraActivity extends AppCompatActivity {

    String preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        preview = getIntent().getStringExtra("track");
        Log.d("previeeeew", preview);
        Bundle bundle = new Bundle();
        bundle.putString("previewtrack", preview );
        Camera2VideoFragment fragInfo = new Camera2VideoFragment();
        fragInfo.setArguments(bundle);
        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragInfo)
                    .commit();
        }
    }

    public String getMyData() {
        return preview;
    }
}
