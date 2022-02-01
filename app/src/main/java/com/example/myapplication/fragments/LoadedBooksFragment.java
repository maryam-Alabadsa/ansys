package com.example.myapplication.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapter.LoadedBooksAdapter;
import com.example.myapplication.adapter.UnfinishedBooksAdapter;
import com.example.myapplication.constants.Constants;
import com.example.myapplication.databinding.FragmentLoadedBooksBinding;
import com.example.myapplication.databinding.FragmentUnfinishedBooksBinding;
import com.example.myapplication.listener.BookDesInterface;
import com.example.myapplication.listener.BookLoadedInterface;
import com.example.myapplication.models.Library;
import com.example.myapplication.models.LoadedBooks;
import com.example.myapplication.models.MyEventBus;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoadedBooksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoadedBooksFragment extends BaseFragment {
    FragmentLoadedBooksBinding binding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ProgressDialog progressDialog;

    public LoadedBooksFragment() {
        // Required empty public constructor
    }


    public static LoadedBooksFragment newInstance(String param1, String param2) {
        LoadedBooksFragment fragment = new LoadedBooksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoadedBooksBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setUpRecyclerView();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        return view;
    }

    LoadedBooksAdapter adapter;

    private void setUpRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        binding.rv.setLayoutManager(layoutManager);
        adapter = new LoadedBooksAdapter(getActivity(), new BookLoadedInterface() {
            @Override
            public void layout(String bookName) {
                EventBus.getDefault().post(new MyEventBus(Constants.FROM_BOOK_DES_TO_EXO_PLAYER, bookName, 0));

//                String path= Environment.getExternalStorageDirectory().getPath()+"/ansys/" + bookName;
//                File file=new File(path);
//                MediaPlayer mediaPlayer=MediaPlayer.create(getActivity(), Uri.parse(path));
//                mediaPlayer.start();
            }
        });
        binding.rv.setAdapter(adapter);
        firebaseFirestore.collection("library").document(currentUser.getUid())
                .collection("myloadedBooks").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.e("addOnSuccessListener", queryDocumentSnapshots.getDocuments().toString());
                        ArrayList<LoadedBooks> list = (ArrayList<LoadedBooks>) queryDocumentSnapshots.toObjects(LoadedBooks.class);
                        progressDialog.dismiss();
                        adapter.setList(list);
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (permitted(permissions)) {
//                    load();
                } else {
                    Toast.makeText(getActivity(),"not_permitted", Toast.LENGTH_SHORT).show();
                }
        }
    }
    public static final String[] PERMISSIONS = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    boolean permitted(String[] ss) {
        for (String s : ss) {
            if (ContextCompat.checkSelfPermission(getActivity(), s) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    boolean permitted() {
        for (String s : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(getActivity(), s) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, 1);
                return false;
            }
        }
        return true;
    }
}