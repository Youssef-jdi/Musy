package com.youssef.yasmine.musy.Adapter.Search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.youssef.yasmine.musy.Activity.MusicPlayerAbumActivity;
import com.youssef.yasmine.musy.Activity.MusicPlayerActivity;
import com.youssef.yasmine.musy.Model.Album;
import com.youssef.yasmine.musy.R;

import java.util.ArrayList;
import java.util.List;

public class AlbumSearchAdapter  extends RecyclerView.Adapter<AlbumSearchAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<Album> list;
    private List<Album> filtredlist;
    private AlbumAdapterListener listener;
  //  private String connected_user;


    public AlbumSearchAdapter(List<Album> list, Context context, AlbumAdapterListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_search_album, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Album album = list.get(position);

        holder.txtTitle.setText(album.getTitle());
        holder.txtnbtracks.setText(String.valueOf(album.getNb_fans()));

            Picasso.with(context).load(album.getCover()).into(holder.imgAlbum);



        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MusicPlayerAbumActivity.class);
                intent.putExtra("tracklist", album.getTrackList());
              //  intent.putExtra("connected_user",connected_user);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String string = constraint.toString();
                if(string.isEmpty()){
                    filtredlist = list;
                }
                else {
                    List<Album> newlist = new ArrayList<>();
                    for(Album user:list){
                        if(user.getTitle().toLowerCase().contains(string.toLowerCase())){
                            newlist.add(user);
                        }
                    }
                    filtredlist = newlist;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filtredlist;
                return  filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filtredlist = (List<Album>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle;
        public TextView txtnbtracks;
        public ImageView imgAlbum;
        LinearLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.textSeachNameAlbum);
            txtnbtracks = itemView.findViewById(R.id.textSearchArtistAlbum);
            imgAlbum = itemView.findViewById(R.id.imageSearchAlbum);
            parentLayout = itemView.findViewById(R.id.layoutSearchAlbum);
        }
    }

    public interface AlbumAdapterListener {
        void onAlbumSelected(Album contact);
    }

}
