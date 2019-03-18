package com.youssef.yasmine.musy.Adapter.Search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.youssef.yasmine.musy.Activity.ArtistActivity;
import com.youssef.yasmine.musy.Adapter.ArtistAdapter;
import com.youssef.yasmine.musy.Model.Artist;
import com.youssef.yasmine.musy.Model.User;
import com.youssef.yasmine.musy.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArtistSeachAdapter extends RecyclerView.Adapter<ArtistSeachAdapter.ViewHolder> implements Filterable {


    private Context context;
    private List<Artist> list;
    private List<Artist> filtredlist;
    private ArtistSearchAdapterListener listener;

    public ArtistSeachAdapter(List<Artist> list, Context context, ArtistSearchAdapterListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_search_artist, parent, false);
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
                    List<Artist> newlist = new ArrayList<>();
                    for(Artist artist:list){
                        if(artist.getName().toLowerCase().contains(string.toLowerCase())){
                            newlist.add(artist);
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
                filtredlist = (List<Artist>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName;
        public TextView followers;
        public CircleImageView imgArtist;
        LinearLayout parentLayout;



        public ViewHolder(View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.textSeachNameArtist);
            followers = itemView.findViewById(R.id.textSearchFollowersArtist);
            imgArtist = itemView.findViewById(R.id.imageSearchArtist);
            parentLayout = itemView.findViewById(R.id.layoutSearchArtist);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onArtistSelected(filtredlist.get(getAdapterPosition()));
                }
            });
        }
    }
    public interface ArtistSearchAdapterListener {
        void onArtistSelected(Artist contact);
    }


}
