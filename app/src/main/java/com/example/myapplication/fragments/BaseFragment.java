package com.example.myapplication.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.activites.LoginActivity;
import com.example.myapplication.activites.MainActivity;
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

import static android.content.Context.MODE_PRIVATE;

public class BaseFragment extends Fragment {
    public FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    public FirebaseFirestore firebaseFirestore;
    public FirebaseStorage firebaseStorage;
    public MainActivity activity;

    @Override
    public void onAttach(Context context) {
//        Toast.makeText(context, "mnmnmnmmnmn", Toast.LENGTH_SHORT).show();
        super.onAttach(context);
        if (activity == null && context instanceof MainActivity) {
            activity = (MainActivity) context;
        }
    }

    @Override
    public void onDetach() {
        this.activity = null;
        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        firebaseFirestore.setFirestoreSettings(settings);

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
                        Constants.LIST.add(new ConstantsList(snapshot.getReference().getId(),snapshot.toObject(Books.class)));

                    }
                }
            }
        });
    }

//    public void getListNew(Adapter_Rv_Books adapter_rv_books) {
//
//        ArrayList<Books> list = new ArrayList<>();
//        CollectionReference questionRef = firebaseFirestore.collection("Books");
//        questionRef
//                .whereEqualTo("most_listened", false)
////                .orderBy("name_book", Query.Direction.ASCENDING)
//                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                if (!queryDocumentSnapshots.isEmpty()) {
//                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
//                        list.add(snapshot.toObject(Books.class));
//                    }
//                    adapter_rv_books.setList(list);
//
//                }
//            }
//        });
//    }

//    public void getListMostListened(Adapter_Rv_Books adapter_rv_books) {
//        ArrayList<Books> list = new ArrayList<>();
//        CollectionReference questionRef = firebaseFirestore.collection("Books");
//        questionRef
//                .whereEqualTo("most_listened", true)
//                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                if (!queryDocumentSnapshots.isEmpty()) {
//                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
//                        list.add(snapshot.toObject(Books.class));
//                        Constants.LIST_MOST_LISTENED.add(snapshot.toObject(Books.class));
//                    }
//                    adapter_rv_books.setList(list);
//                }
//            }
//        });
//    }

    public void goToMain() {
        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    public void goToLogin() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    public void getUserImg(int p, ImageView imageView) {
        switch (p) {
            case Constants.IMG_MAN1:
                imageView.setImageResource(R.drawable.man1);
                break; // optional
            case Constants.IMG_MAN2:
                imageView.setImageResource(R.drawable.man2);
                break; // optional
            case Constants.IMG_MAN3:
                imageView.setImageResource(R.drawable.man3);
                break; // optional
            case Constants.IMG_WOMAN1:
                imageView.setImageResource(R.drawable.woman1);
                break; // optional
            case Constants.IMG_WOMAN2:
                imageView.setImageResource(R.drawable.woman2);
                break; // optional
            case Constants.IMG_WOMAN3:
                imageView.setImageResource(R.drawable.woman3);
                break; // optional

            default: // Optional
                imageView.setImageResource(R.drawable.user_img);
        }
    }

    public boolean restorePreData() {
        SharedPreferences preferences = getActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);
        Boolean isIntroActivityOpenedBefore = preferences.getBoolean("isIntroOpened", false);
        return isIntroActivityOpenedBefore;
    }

    public void savePrefsData() {
        SharedPreferences preferences = getActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isIntroOpened", true);
        editor.apply();
    }
}
