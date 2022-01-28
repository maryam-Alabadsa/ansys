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
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapter.CompletedBooksِAdapter;
import com.example.myapplication.adapter.UnfinishedBooksAdapter;
import com.example.myapplication.constants.Constants;
import com.example.myapplication.databinding.FragmentCompletedBooksBinding;
import com.example.myapplication.databinding.FragmentUnfinishedBooksBinding;
import com.example.myapplication.listener.BookDesInterface;
import com.example.myapplication.models.Library;
import com.example.myapplication.models.MyEventBus;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UnfinishedBooksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnfinishedBooksFragment extends BaseFragment {
    FragmentUnfinishedBooksBinding binding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ProgressDialog progressDialog;

    public UnfinishedBooksFragment() {
        // Required empty public constructor
    }

    public static UnfinishedBooksFragment newInstance(String param1, String param2) {
        UnfinishedBooksFragment fragment = new UnfinishedBooksFragment();
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
        binding = FragmentUnfinishedBooksBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        setUpRecyclerView();
        return view;
    }

    UnfinishedBooksAdapter adapter;

    private void setUpRecyclerView() {

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        binding.rv.setLayoutManager(layoutManager);
        adapter = new UnfinishedBooksAdapter(getActivity(), new BookDesInterface() {
            @Override
            public void layout(String id, Boolean Cat) {
                EventBus.getDefault().post(new MyEventBus(Constants.FROM_ADAPTER_TO_BOOK_DES, id, Cat));
            }
        });

        binding.rv.setAdapter(adapter);
        ArrayList<Library> list=new ArrayList<>();
        list.add(new Library("AAh5xXMMzMwHOCmwpe1f","",false,710086));
        adapter.setList(list);
        firebaseFirestore.collection("library").document(currentUser.getUid()).collection("myObj")
                .whereEqualTo("finished",false).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.e("addOnSuccessListener", queryDocumentSnapshots.getDocuments().toString());
                        ArrayList<Library> list = (ArrayList<Library>) queryDocumentSnapshots.toObjects(Library.class);
                        progressDialog.dismiss();
                        adapter.setList(list);

                    }
                });

    }
}