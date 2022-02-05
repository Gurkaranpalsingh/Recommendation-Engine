package com.example.movieapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MovieTitleList extends ArrayAdapter<FirebaseData> {
    private Activity context;
    private List<FirebaseData> movielist;

    public MovieTitleList(Activity context, List<FirebaseData> movielist){
        super(context, R.layout.liked_list_item,movielist);
        this.context = context;
        this.movielist = movielist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.liked_list_item,null,true);

        TextView textViewTitle = (TextView) listViewItem.findViewById(R.id.liked_label);
        FirebaseData firebaseData = movielist.get(position);
        textViewTitle.setText(firebaseData.getTitle());
        return listViewItem;
    }
}
