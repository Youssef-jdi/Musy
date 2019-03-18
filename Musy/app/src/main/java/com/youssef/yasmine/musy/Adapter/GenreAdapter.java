package com.youssef.yasmine.musy.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.youssef.yasmine.musy.Activity.MusicByGenreActivity;
import com.youssef.yasmine.musy.Model.Genre;
import com.youssef.yasmine.musy.R;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {

    private Context context;
    private List<Genre> list;

    public GenreAdapter(List<Genre> list, Context context) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_genre, parent, false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Genre genre = list.get(position);
        holder.txtGenre.setText(genre.getName());
        Picasso.with(context).load(genre.getPicture()).into(holder.imgGenre);
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MusicByGenreActivity.class);
                intent.putExtra("genreid", genre.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtGenre;
        public ImageView imgGenre;
        ConstraintLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            txtGenre = itemView.findViewById(R.id.idTextGenre);
            imgGenre = itemView.findViewById(R.id.idImgGenre);
          parentLayout = itemView.findViewById(R.id.idlayoutGenre);
        }
    }


}
