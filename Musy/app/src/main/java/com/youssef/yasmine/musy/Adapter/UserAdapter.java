package com.youssef.yasmine.musy.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.youssef.yasmine.musy.Activity.UsersActivity;
import com.youssef.yasmine.musy.Model.Track;
import com.youssef.yasmine.musy.Model.User;
import com.youssef.yasmine.musy.R;

import java.util.List;

public class UserAdapter   extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<User> list;

    public UserAdapter(List<User> list, Context context) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final User user = list.get(position);

        holder.txtTitle.setText(user.getNickname());
        Picasso.with(context).load(user.getPicture()).into(holder.imgFollowers);



        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UsersActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("idUser", user.getId());
              //  intent.putExtra("connected_user",connected_user);
                context.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle;
        public ImageView imgFollowers;
        public Button btnFollow;
        public LinearLayout parentLayout;



        public ViewHolder(View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.nicknameUserFollow);
            imgFollowers = itemView.findViewById(R.id.imageViewUserFollow);
           // btnFollow= itemView.findViewById(R.id.btnUserFollow);
            parentLayout = itemView.findViewById(R.id.idlayoutUser);


        }

    }

}
