package com.youssef.yasmine.musy.Activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.youssef.yasmine.musy.R;
import com.youssef.yasmine.musy.Util.ConnectedUser;
import com.youssef.yasmine.musy.Util.ServerConnection;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import me.riddhimanadib.library.NavigationPage;

public class SignInActivity extends AppCompatActivity {

    SignInButton btn;
    GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 9002 ;
    String url;
    String postUrl;
    String server;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ServerConnection serverConnection = ServerConnection.getInstance();
        server = serverConnection.getServer();
        url = server+"/checkuser/";
        postUrl = server+"/user_create";
        btn = findViewById(R.id.sign_in_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1071167256369-un6tm446gmglnuuvh31g3d97c62fgk3u.apps.googleusercontent.com").requestServerAuthCode("1071167256369-un6tm446gmglnuuvh31g3d97c62fgk3u.apps.googleusercontent.com").requestProfile().
                        build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        btn.setSize(SignInButton.SIZE_STANDARD);
        btn.setColorScheme(SignInButton.COLOR_DARK);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }


    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            ConnectedUser connectedUser = ConnectedUser.getInstance();
            connectedUser.setConnected_user(account.getId());
            chechUser(account);

        } else {
            Log.d("deconnectééééééé", "updateUI: ");
        }
    }

    public void chechUser(final GoogleSignInAccount account){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url + account.getId(), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("userrrrrr", String.valueOf(response));
                if(response.length() == 0) {
                    addUser(account);
                }
                else {
                    Intent intent = new Intent(SignInActivity.this,NavigationActivity.class);
                    startActivity(intent);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errouruser", error.getMessage());

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    public void addUser(final GoogleSignInAccount account){
        StringRequest postRequest = new StringRequest(Request.Method.POST, postUrl,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        updateUI(account);
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
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("eMail", String.valueOf(account.getEmail()));
                params.put("nickName", String.valueOf(account.getDisplayName()));
                params.put("Picture", String.valueOf(account.getPhotoUrl()));
                params.put("id", String.valueOf(account.getId()));
                // params.put("id", String.valueOf(account.getAccount().name));

                return  params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(postRequest);
        Intent intent = new Intent(SignInActivity.this,NavigationActivity.class);
        startActivity(intent);

    }


    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      //  callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);



            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("heloo", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }


    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}
