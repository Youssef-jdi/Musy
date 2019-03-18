package com.youssef.yasmine.musy.Activity;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.youssef.yasmine.musy.R;
import com.youssef.yasmine.musy.Util.ConnectedUser;

import java.util.ArrayList;
import java.util.List;

import me.riddhimanadib.library.BottomBarHolderActivity;
import me.riddhimanadib.library.NavigationPage;

public class NavigationActivity extends BottomBarHolderActivity {

    String id_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_Navigation);
       // id_user = getIntent().getStringExtra("id_user");



        NavigationPage page1 = new NavigationPage("Home", ContextCompat.getDrawable(this, R.drawable.homess), HomeFragment.newInstance());
        NavigationPage page2 = new NavigationPage("Profile", ContextCompat.getDrawable(this, R.drawable.pross), ProfileFragment.newInstance());
        NavigationPage page3 = new NavigationPage("Feed", ContextCompat.getDrawable(this, R.drawable.vidss), FeedFragment.newInstance());
        NavigationPage page4 = new NavigationPage("Search", ContextCompat.getDrawable(this, R.drawable.seass), SearchFragment.newInstance());

        List<NavigationPage> navigationPages = new ArrayList<>();
        navigationPages.add(page1);
        navigationPages.add(page2);
        navigationPages.add(page3);
        navigationPages.add(page4);


        // pass them to super method
        super.setupBottomBarHolderActivity(navigationPages);
    }
}
