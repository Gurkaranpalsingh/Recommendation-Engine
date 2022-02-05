package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class MainActivity extends AppCompatActivity {

    private SharedPreferences.Editor editor;
    private boolean isDarkModeOn;
    SignInButton btnSignIn;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        btnSignIn = findViewById(R.id.btnLogin);

        //Initialize sign in option
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken("79101734382-3tprmna5hracii3an37pntbqgma72cp4.apps.googleusercontent.com")
                .requestEmail()
                .build();

        // Initiazlise sign in client
        googleSignInClient = GoogleSignIn.getClient(MainActivity.this, googleSignInOptions);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize sign in Intent
                Intent intent = googleSignInClient.getSignInIntent();

                // start activity for result
                startActivityForResult(intent, 100);
            }
        });
        // Initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        // Initialize firebase user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        // Check condition
        if (firebaseUser != null) {


            startActivity(new Intent(MainActivity.this, MovieActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }


        // Saving state of our app
        // using SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);
        // When user reopens the app
        // after applying dark/light mode
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }


//        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
//        boolean firstStart = prefs.getBoolean("firstStart", true);
//        if(firstStart) {
//            showstartDailog();
//        }
    }

//    private void showstartDailog() {
//        new AlertDialog.Builder(this).setTitle("Welcome First time user")
//                .setMessage("Based on your likes the app will recommend the movies").setPositiveButton("ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        }).create().show();
//
//        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putBoolean("firstStart",false);
//        editor.apply();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_sample_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.dark_mode) {

            if (isDarkModeOn) {
                // if dark mode is on it
                // will turn it off
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                // it will set isDarkModeOn
                // boolean to false
                editor.putBoolean("isDarkModeOn", false);
                editor.apply();
                // change text of Button

            } else {
                // if dark mode is off
                // it will turn it on
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                // it will set isDarkModeOn
                // boolean to true
                editor.putBoolean("isDarkModeOn", true);
                editor.apply();

                // change text of Button

            }
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    // On Activity Result

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn
                    .getSignedInAccountFromIntent(data);

            if (signInAccountTask.isSuccessful()) {

                String s = " Google Sign in is successful";
                displayToast(s);

                try {
                    GoogleSignInAccount googleSignInAccount = signInAccountTask
                            .getResult(ApiException.class);

                    // Check condition
                    if (googleSignInAccount != null) {
                        // Initialize Auth Credentials
                        AuthCredential authCredential = GoogleAuthProvider
                                .getCredential(googleSignInAccount.getIdToken()
                                        , null);

                        // Check Credentials
                        firebaseAuth.signInWithCredential(authCredential)
                                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        // Check Condition
                                        if (task.isSuccessful()) {
                                            // When task is successful
                                            FirebaseUser user = firebaseAuth.getCurrentUser();
                                            Intent intent = new Intent(MainActivity.this, MovieActivity.class);
                                            startActivity(intent);
                                        }
                                    }
                                });

                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void displayToast(String s) {
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
    }

}