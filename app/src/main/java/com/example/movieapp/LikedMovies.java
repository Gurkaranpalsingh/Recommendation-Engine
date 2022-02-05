
package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LikedMovies extends AppCompatActivity {
    private ListView liked_listView;
    List<FirebaseData> dataList;

    DatabaseReference root_reference;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    String userId;
    TextView liked_movies_heading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_movies);
        userId = firebaseUser.getUid();

        liked_listView = findViewById(R.id.liked_list);
        root_reference = FirebaseDatabase.getInstance().getReference(userId);
        dataList = new ArrayList<>();
        liked_movies_heading = findViewById(R.id.liked_Movies_heading);
        new LikedMoviesAsync().execute();
    }


    class LikedMoviesAsync extends AsyncTask<DatabaseReference,Void, DatabaseReference> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(DatabaseReference databaseReference) {
            super.onPostExecute(databaseReference);
        }

        @Override
        protected DatabaseReference doInBackground(DatabaseReference... databaseReferences) {

            root_reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dataList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        FirebaseData data = dataSnapshot.getValue(FirebaseData.class);
                        dataList.add(data);
                    }
                    if(dataList.isEmpty()){
                        liked_movies_heading.setText("You have not liked any movie");
                    }
                    MovieTitleList adapter = new MovieTitleList(LikedMovies.this, dataList);
                    liked_listView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            return null;
        }
    }

}