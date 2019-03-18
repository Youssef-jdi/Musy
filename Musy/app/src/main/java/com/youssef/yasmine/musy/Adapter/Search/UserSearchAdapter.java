package com.youssef.yasmine.musy.Adapter.Search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.youssef.yasmine.musy.Activity.UsersActivity;
import com.youssef.yasmine.musy.Adapter.UserAdapter;
import com.youssef.yasmine.musy.Model.User;
import com.youssef.yasmine.musy.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserSearchAdapter extends RecyclerView.Adapter<UserSearchAdapter.ViewHolder> implements Filterable{

    private Context context;
    private List<User> list;
    private List<User> filtredlist;
    private UserAdapterListener listener;
   // private  String connected_user;

    public  UserSearchAdapter(List<User> list, Context context,UserAdapterListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_search_profilet, parent, false);
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
                intent.putExtra("idUser", user.getId());
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
                    List<User> newlist = new ArrayList<>();
                    for(User user:list){
                        if(user.getNickname().toLowerCase().contains(string.toLowerCase())){
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
                filtredlist = (List<User>) results.values;
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
        public CircleImageView imgFollowers;
        public Button btnFollow;
        public LinearLayout parentLayout;



        public ViewHolder(View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.textSeachNameProfile);
         imgFollowers = itemView.findViewById(R.id.imageSearchProfile);
//            btnFollow= itemView.findViewById(R.id.imageSearchProfile);
            parentLayout = itemView.findViewById(R.id.layoutSearchProfile);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onUserSelected(filtredlist.get(getAdapterPosition()));
                }
            });


        }

    }

    public interface UserAdapterListener {
        void onUserSelected(User contact);
    }

}
