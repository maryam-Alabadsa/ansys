package com.example.myapplication.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.myapplication.R;
import com.example.myapplication.constants.Constants;
import com.example.myapplication.fav.Favorite;
import com.example.myapplication.fav.FavoriteArray;
import com.example.myapplication.models.Books;
import com.example.myapplication.models.ConstantsList;
import com.example.myapplication.models.Library;
import com.example.myapplication.on.bording.IntroActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SplashActivity extends BaseActivity {



    private void goToLogin() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.e("onCreate splash ", "onCreate");

        if (currentUser != null) {
            getAll();
        } else {
            Intent intent = new Intent(this, IntroActivity.class);
            intent.putExtra(Constants.INTENT_KEY, Constants.FROM_FRAGMENT_DES_TO_INTRO);
            startActivity(intent);
        }
    }


    public void getLibrary() {
        ArrayList<Library> list = new ArrayList<>();

        CollectionReference questionRef = firebaseFirestore.collection("Library");
        questionRef
                .whereEqualTo("uId", currentUser.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        Constants.LIST_LIBRARY = snapshot.toObject(Library.class);
//                        Constants.LIST_LIBRARY.add(new Library(snapshot.getReference().getId(),snapshot.toObject(Library.class)));
                    }
                }
            }
        });
    }

    Favorite favorites;
    ArrayList<FavoriteArray> favoriteArrays;


}