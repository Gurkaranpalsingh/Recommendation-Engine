package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.util.HashMap;

public class Detail extends AppCompatActivity {

    DatabaseReference databaseMovie;
    String title;
    String url;
    FirebaseData firebaseData;
    Button detail_button, remove_button;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    String uId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        uId = firebaseUser.getUid();
        TextView detail_title = findViewById(R.id.detail_title);
        TextView detail_overview = findViewById(R.id.detail_overview);
        TextView detail_date = findViewById(R.id.detail_date);
        detail_button = findViewById(R.id.detail_button);
        remove_button = findViewById(R.id.remove_button);
        url = UrlCategory.url;
        ImageView imageView = findViewById(R.id.detail_image);
        new DetailAsync().execute();
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        String overview = intent.getStringExtra("overview");
        String publishDate = intent.getStringExtra("publishDate");
        String[] dateArray = publishDate.split("\\W+");
        String pubDate = dateArray[2] + "-" + dateArray[1] + "-" + dateArray[0];
        detail_title.setText(title);
        detail_overview.setText(overview);
        detail_date.setText(pubDate);

        Glide.with(Detail.this)
                .load("https://source.unsplash.com/featured/?" + title)
                .into(imageView);
    }

    private void addMovies() {

        String id = databaseMovie.push().getKey();
        firebaseData = new FirebaseData(id, title, url);
        databaseMovie.child(title).setValue(firebaseData);
        Toast.makeText(this, "Movie is added to the firebase", Toast.LENGTH_SHORT).show();
    }

    private void deleteMovie(String movieId) {

        DatabaseReference databaseRef = databaseMovie.child(movieId);
        databaseRef.removeValue();
        Toast.makeText(this, "Movie is deleted from the firebase", Toast.LENGTH_SHORT).show();
    }


    class DetailAsync extends AsyncTask<DatabaseReference, Void, DatabaseReference> {
        protected DatabaseReference doInBackground(DatabaseReference... args) {
            databaseMovie = FirebaseDatabase.getInstance().getReference(uId);
            return databaseMovie;
        }

        @Override
        protected void onPostExecute(DatabaseReference databaseMovie) {
            detail_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addMovies();
                    remove_button.setEnabled(true);
                    detail_button.setEnabled(false);
                    detail_button.setText("Disabled");
                    remove_button.setText("Remove");
                }
            });

            remove_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteMovie(title);
                    remove_button.setEnabled(false);
                    detail_button.setEnabled(true);
                    detail_button.setText("Like");
                    remove_button.setText("Disabled");
                }
            });
        }
    }
}