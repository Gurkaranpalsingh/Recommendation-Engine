package com.example.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ListAdapter extends ArrayAdapter<Data> {
    private Context context;
    private int adapter_view_layout;
    public ListAdapter(@NonNull Context context, int resource, @NonNull List<Data> objects) {
        super(context, resource, objects);
        this.context = context;
        this.adapter_view_layout = resource;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(adapter_view_layout, parent, false);
        }

        TextView title = convertView.findViewById(R.id.tv_title);
        TextView vote_average = convertView.findViewById(R.id.tv_voteAverage);
        TextView release_date = convertView.findViewById(R.id.tv_date);
        Data singleData = getItem(position);
        title.setText(singleData.getTitle());
        release_date.setText( "Release date - " + singleData.getRelease_date());
        vote_average.setText("Rating - "+ String.valueOf(singleData.getVote_average()));

        return convertView;
    }
}
