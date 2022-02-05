package com.example.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SortingAdapter extends ArrayAdapter<SortingData> {
    public SortingAdapter(Context context, ArrayList<SortingData> countryList){
        super(context,0,countryList);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return intView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return intView(position,convertView,parent);
    }


    private View intView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if(convertView ==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sort_spinner_row,parent,false);
        }
        // Storing the variable
        TextView textViewName = convertView.findViewById(R.id.text_view_name);
        // getting the each item
        SortingData currentItem = getItem(position);
        // setting the image and text
        if(currentItem != null) {
            textViewName.setText(currentItem.getName());
        }
        return convertView;
    }
}
