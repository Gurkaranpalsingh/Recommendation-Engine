package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MovieActivity extends AppCompatActivity {
     String API = "8d2ab327503dc0371d61d0c5db2bbc76";
     String BaseAPI ="https://api.themoviedb.org/3/";
     UrlCategory urlCategory;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;
    Button btnLogout;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        urlCategory = new UrlCategory();

        btnLogout = findViewById(R.id.btnlogout);

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        // Initialize firebase user

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        googleSignInClient = GoogleSignIn.getClient(MovieActivity.this,
                GoogleSignInOptions.DEFAULT_SIGN_IN);
        btnLogout.setOnClickListener(logout);
    }

    View.OnClickListener logout = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // Implementing signout functionality
            googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    // Check condition
                    if(task.isSuccessful()){
                        // When task is successful
                        firebaseAuth.signOut();
                        Toast.makeText(MovieActivity.this,"Logout Successful",Toast.LENGTH_SHORT).show();
                        // Redirect to Sign In Activity
                        startActivity(new Intent(MovieActivity.this,
                                MainActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                }
            });
        }
    };

        public void movieCategoryClick(View view) {
            String url = null;
            Intent intent = new Intent(MovieActivity.this, MovieCategories.class);
            if ((view.getId() == R.id.top_rated)) {
                url = BaseAPI + "movie/top_rated?api_key=" + API + "&language=en-US";
                urlCategory.setUrlCategory(url);
            }
            if ((view.getId() == R.id.popular)) {
               url = BaseAPI + "discover/movie?api_key=" + API +  "&with_genres=35" ;
                urlCategory.setUrlCategory(url);
            }
            if ((view.getId() == R.id.action)) {
                url =  BaseAPI +  "discover/movie?api_key=" + API +  "&with_genres=28" ;
                urlCategory.setUrlCategory(url);
            }
            intent.putExtra("URL", url);
            startActivity(intent);
        }
    }
