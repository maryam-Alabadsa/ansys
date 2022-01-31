package com.example.myapplication.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.adapter.Adapter_Rv_Books;
import com.example.myapplication.constants.Constants;
import com.example.myapplication.models.Books;
import com.example.myapplication.models.ConstantsList;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {
    public FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    public FirebaseStorage firebaseStorage;
   public FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();

        firebaseStorage=FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
//        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
//                .setPersistenceEnabled(true)
//                .build();
//        firebaseFirestore.setFirestoreSettings(settings);
    }
    public Integer getListNew() {
        ArrayList<Books> list = new ArrayList<>();
        CollectionReference questionRef = firebaseFirestore.collection("Books");
        questionRef
                .whereEqualTo("most_listened", false)

                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        list.add(snapshot.toObject(Books.class));
                    }
                }
            }
        });

        return list.size();
    }
    public boolean restorePreData() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        Boolean isIntroActivityOpenedBefore = preferences.getBoolean("isIntroOpened", false);
        return isIntroActivityOpenedBefore;
    }

    public void savePrefsData() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isIntroOpened", true);
        editor.apply();
    }
    public void getAll() {
        ArrayList<Books> list = new ArrayList<>();

        CollectionReference questionRef = firebaseFirestore.collection("Books");
        questionRef

                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        list.add(snapshot.toObject(Books.class));
                        Constants.LIST.add(new ConstantsList(snapshot.getReference().getId(), snapshot.toObject(Books.class)));
                    }
                }
                goToNext();
            }
        });
    }
    private void goToNext() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
