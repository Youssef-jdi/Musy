package com.youssef.yasmine.musy.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.youssef.yasmine.musy.Adapter.ProfileAdapter;
import com.youssef.yasmine.musy.Model.Playlist;
import com.youssef.yasmine.musy.Model.User;
import com.youssef.yasmine.musy.R;
import com.youssef.yasmine.musy.Util.ConnectedUser;
import com.youssef.yasmine.musy.Util.ServerConnection;
import com.youssef.yasmine.musy.Util.VolleyMultipartRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    private String connected_user;
    private TextView nick;
    private TextView nb_following;
    private TextView nb_followers;
    private TextView lst_following;
    private TextView lst_followers;
    private CircleImageView imageView;
    private TextView changePicture;
    private static final int RESULT_LOAD_IMAGE = 1;
    private String upload_URL ;
    private RequestQueue rQueue;
    private ArrayList<HashMap<String, String>> arraylist;
    private String url ;
    private String urlNbFollowing ;
    private String urlNbFollowers ;
    String[] lstText = {"My Playlists", "MyVideos"};
    Integer[] lstIcon = {R.drawable.like, R.drawable.like, R.drawable.like, R.drawable.like, R.drawable.like, R.drawable.like};
    GoogleSignInClient mGoogleSignInClient;
    Button logout;
    private String server;



    public ProfileFragment() {
        // Required empty public constructor
    }

    public static  ProfileFragment newInstance() {
        return new  ProfileFragment();
    }


    ListView lst;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_profile, container, false);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1064544602796-escudjptefdl9ums0hjlv6l08mj0kh6q.apps.googleusercontent.com").requestServerAuthCode("1064544602796-escudjptefdl9ums0hjlv6l08mj0kh6q.apps.googleusercontent.com").requestProfile().
                        build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
       // connected_user = this.getArguments().getString("connected_user");
        ConnectedUser connectedUser = ConnectedUser.getInstance();
        connected_user = connectedUser.getConnected_user();
        ServerConnection serverConnection = ServerConnection.getInstance();
        server = serverConnection.getServer();
        upload_URL = server+"/upload/";
        url = server+"/user/";
        urlNbFollowing = server+"/numberfollowing/";
        urlNbFollowers = server+"/numberfollowers/";

        Log.d("connected_user_id", connected_user);
        logout = rootview.findViewById(R.id.idBtnLogOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(getActivity(),SignInActivity.class);
                        startActivity(intent);
                    }
                });

            }
        });
        nick = rootview.findViewById(R.id.textView3);
        changePicture = rootview.findViewById(R.id.changePicture);
        imageView = rootview.findViewById(R.id.imageView);
        getData();

        nb_following = rootview.findViewById(R.id.textView8);
        nb_followers = rootview.findViewById(R.id.textView9);
        getFollowersNumber();
        getFollowingNumber();

        lst_followers = rootview.findViewById(R.id.textView6);
        lst_following = rootview.findViewById(R.id.textView7);

        lst_followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), listFollowersActivity.class);
                intent.putExtra("connected_user", connected_user);
                startActivity(intent);


            }
        });

        lst_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ListFollowingActivity.class);
                intent.putExtra("connected_user", connected_user);
                startActivity(intent);

            }
        });


        lst = (ListView) rootview.findViewById(R.id.listViewProfile);
        ProfileAdapter profileAdapter = new ProfileAdapter(getContext(), lstText, lstIcon);
        lst.setAdapter(profileAdapter);
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(String text : lstText){
                    if(position == 0){
                        Intent intent = new Intent(getActivity(), PlaylistFavorisActivity.class);
                        startActivity(intent);
                    }else
                    if(position == 1){
                        Intent intent = new Intent(getActivity(), ProfileVideosActivity.class);
                       // intent.putExtra("connected_user", connected_user);
                        startActivity(intent);
                    }

                        else{

                        }

                }
            }
        });


        changePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });

        return rootview;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == RESULT_LOAD_IMAGE) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    imageView.setImageBitmap(bitmap);
                    uploadImage(bitmap,connected_user);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void uploadImage(final Bitmap bitmap, final String user_id){

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.d("Response", String.valueOf(response));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                 params.put("userId", user_id);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image", new DataPart(imagename + ".jpg", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };


        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(volleyMultipartRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void  requestMultiplePermissions(){
        Dexter.withActivity(getActivity())
                .withPermissions(

                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }



    private void getData() {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url+connected_user, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Log.d("hello", String.valueOf(response));
                        JSONObject jsonObject = response.getJSONObject(i);
                        String nickname = jsonObject.getString("nickname");
                        String image = jsonObject.getString("picture");
                        Picasso.with(getContext()).load(image).into(imageView);
                        nick.setText(nickname);


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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }

    private void getFollowingNumber() {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlNbFollowing+connected_user, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Log.d("hello", String.valueOf(response));
                        JSONObject jsonObject = response.getJSONObject(i);
                        String following = jsonObject.getString("following");
                        nb_following.setText(following);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }

    private void getFollowersNumber() {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlNbFollowers+connected_user, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Log.d("hello", String.valueOf(response));
                        JSONObject jsonObject = response.getJSONObject(i);
                        String followers= jsonObject.getString("followers");
                       nb_followers.setText(followers);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }







}
