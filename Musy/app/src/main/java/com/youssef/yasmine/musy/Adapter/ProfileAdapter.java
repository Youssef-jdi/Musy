package com.youssef.yasmine.musy.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.youssef.yasmine.musy.R;

public class ProfileAdapter extends ArrayAdapter<String> {


    private  String [] lstText ;
    private  Integer [] lstIcon ;
    Context mContext;

    public ProfileAdapter( Context context,String [] lstText,  Integer [] lstIcon ) {
        super(context, R.layout.item_profile_user, lstText);
        this.mContext=context;
        this.lstText = lstText;
        this.lstIcon = lstIcon;

    }

    static class ViewHolder {
        TextView txt;
        ImageView icon;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        final View result;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_profile_user, parent, false);
            viewHolder.txt = (TextView) convertView.findViewById(R.id.idtxtProfile);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.idIcon);
            result=convertView;
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }
        viewHolder.txt.setText(lstText[position]);
        viewHolder.icon.setImageResource(lstIcon[position]);
        return convertView;
    }
}
