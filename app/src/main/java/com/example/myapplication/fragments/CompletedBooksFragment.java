package com.example.myapplication.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapter.Adapter_Rv_Books;
import com.example.myapplication.adapter.CompletedBooksِAdapter;
import com.example.myapplication.constants.Constants;
import com.example.myapplication.databinding.FragmentCompletedBooksBinding;
import com.example.myapplication.databinding.FragmentFavBookBinding;
import com.example.myapplication.listener.BookDesInterface;
import com.example.myapplication.models.Library;
import com.example.myapplication.models.MyEventBus;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompletedBooksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompletedBooksFragment extends BaseFragment {
    FragmentCompletedBooksBinding binding;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ProgressDialog progressDialog;

    public CompletedBooksFragment() {
        // Required empty public constructor
    }

    public static CompletedBooksFragment newInstance(String param1, String param2) {
        CompletedBooksFragment fragment = new CompletedBooksFragment();
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

    CompletedBooksِAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCompletedBooksBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setUpRecyclerView();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        return view;
    }

    private void setUpRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        binding.rv.setLayoutManager(layoutManager);
        adapter = new CompletedBooksِAdapter(getActivity(), new BookDesInterface() {
            @Override
            public void layout(String id, Boolean Cat) {
//                EventBus.getDefault().post(new MyEventBus(Constants.FROM_BOOK_DES_TO_EXO_PLAYER, id,0));
            }
        });
        binding.rv.setAdapter(adapter);

        firebaseFirestore.collection("library").document(currentUser.getUid()).collection("myObj")
                .whereEqualTo("finished",true).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.e("addOnSuccessListener", queryDocumentSnapshots.getDocuments().toString());
                        ArrayList<Library> list = (ArrayList<Library>) queryDocumentSnapshots.toObjects(Library.class);
                        progressDialog.dismiss();
                        adapter.setList(list);

                    }
                });    }
}