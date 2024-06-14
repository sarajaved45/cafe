package com.example.cafedesign;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CreationsAdapter extends BaseAdapter {

    private Context context;
    private List<Creation> creationsList;

    public CreationsAdapter(Context context, List<Creation> creationsList) {
        this.context = context;
        this.creationsList = creationsList;
    }

    @Override
    public int getCount() {
        return creationsList.size();
    }

    @Override
    public Object getItem(int position) {
        return creationsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }


}

