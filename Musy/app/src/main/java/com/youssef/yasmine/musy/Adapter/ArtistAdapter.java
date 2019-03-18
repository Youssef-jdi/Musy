package com.youssef.yasmine.musy.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.youssef.yasmine.musy.Activity.ArtistActivity;
import com.youssef.yasmine.musy.Model.Artist;
import com.youssef.yasmine.musy.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {


    private Context context;
    private List<Artist> list;

    public ArtistAdapter(List<Artist> list, Context context) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(context).inflate(R.layout.item_artist, parent, false);
            return new ViewHolder(v);


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

            final Artist artist = list.get(position);
            holder.txtName.setText(artist.getName());
            Picasso.with(context).load(artist.getPicture()).into(holder.imgArtist);

            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ArtistActivity.class);
                    intent.putExtra("artist", artist.getId());
                    context.startActivity(intent);
                }
            });



    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName;
        public CircleImageView imgArtist;
        ConstraintLayout parentLayout;



        public ViewHolder(View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.idArtistName);
            imgArtist = itemView.findViewById(R.id.idImgArtist);
            parentLayout = itemView.findViewById(R.id.idLayoutArtist);


        }



    }

}
