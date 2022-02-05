package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Recommend extends AppCompatActivity {
    List<FirebaseData> recommendDataList;
    DatabaseReference root_reference;
    List<String> listUrl;
    ListView myListView;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    String userId;
    TextView emptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        userId = firebaseUser.getUid();
        root_reference = FirebaseDatabase.getInstance().getReference(userId);
        recommendDataList = new ArrayList<>();
        listUrl = new ArrayList<>();
        myListView = findViewById(R.id.recommend_list);
        emptyTextView = findViewById(R.id.emptyTextView);
        new maxUrlAsync().execute();
    }


    class maxUrlAsync extends AsyncTask<DatabaseReference, Void, DatabaseReference> {
        @Override
        protected DatabaseReference doInBackground(DatabaseReference... databaseReferences) {
            root_reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    listUrl.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        FirebaseData data = dataSnapshot.getValue(FirebaseData.class);
                        String urls = data.getUrl();
                        listUrl.add(urls);
                    }
                    String maxUrl = mostFrequent(listUrl);
                    if (maxUrl != null) {
                        new runRecommendTask().execute(maxUrl);

                    } else {
                        emptyTextView.setText("Please like the movies to see the recommended");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(DatabaseReference databaseReference) {
            super.onPostExecute(databaseReference);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    static String mostFrequent(List<String> list) {
        int max = 0;
        int curr = 0;
        String currKey = null;
        Set<String> unique = new HashSet<String>(list);

        for (String key : unique) {
            curr = Collections.frequency(list, key);

            if (max < curr) {
                max = curr;
                currKey = key;
            }
        }
        return currKey;

    }

    public class runRecommendTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {
            String response = HttpRequest.excuteGet(urls[0]);
            return response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String data) {

            try {
                JSONObject jsonObj = new JSONObject(data);
                JSONArray results = jsonObj.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject result = results.getJSONObject(i);

                    String title = result.getString("title");
                    FirebaseData dataObject = new FirebaseData(null, title, null);
                    recommendDataList.add(dataObject);

                }
                MovieTitleList dataListAdapter = new MovieTitleList(Recommend.this, recommendDataList);
                myListView.setAdapter(dataListAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}