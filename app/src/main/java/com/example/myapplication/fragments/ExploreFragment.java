package com.example.myapplication.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activites.MainActivity;
import com.example.myapplication.adapter.Adapter_Rv_Books;
import com.example.myapplication.constants.Constants;
import com.example.myapplication.databinding.FragmentExploreBinding;
import com.example.myapplication.listener.BookDesInterface;
import com.example.myapplication.models.Books;
import com.example.myapplication.models.ConstantsList;
import com.example.myapplication.models.MyEventBus;
import com.example.myapplication.models.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class ExploreFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    FragmentExploreBinding binding;
    private static final String TAG = FragmentExploreBinding.class.getSimpleName();
    private Adapter_Rv_Books adapter_rv1;
    private Adapter_Rv_Books adapter_rv2;

    public ExploreFragment() {
        // Required empty public constructor
    }

    public static ExploreFragment newInstance(String param1, String param2) {
        ExploreFragment fragment = new ExploreFragment();
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
//        EventBus.getDefault().register(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentExploreBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
//         saveList();
//-------------------------------data base-------------------------------------

        binding.nested.setVisibility(View.GONE);
        setUpRecyclerView1();
        setUpRecyclerView2();
//-----------------------Even BUS FROM_EXPLORE_TO_LATEST_PUBLISHED-------------------------
        binding.tvShowAllNewBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new MyEventBus(Constants.FROM_EXPLORE_TO_LATEST_PUBLISHED, "FROM_EXPLORE_TO_LATEST_PUBLISHED"));
            }
        });
//-----------------------Even BUS MOST_LISTENED-------------------------
        binding.tvShowAllMostListened.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new MyEventBus(Constants.FROM_EXPLORE_TO_MOST_LISTENED, "FROM_EXPLORE_TO_MOST_LISTENED"));
            }
        });
        setData();
//---------------------------------------------------------------------------
        return view;
    }

    private void saveList() {
        for (Books user : Utils.getBooks(getActivity())) {
            firebaseFirestore.collection("Books")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentReference  " + documentReference.getId());

                        }
                    });
        }

    }

    private void setUpRecyclerView1() {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                getActivity(), RecyclerView.HORIZONTAL, false);
        binding.rvOneNew.setLayoutManager(layoutManager);
        binding.nested.setVisibility(View.VISIBLE);
        adapter_rv1 = new Adapter_Rv_Books(getActivity(), new BookDesInterface() {
            @Override
            public void layout(String id, Boolean Cat) {
                EventBus.getDefault().post(new MyEventBus(Constants.FROM_ADAPTER_TO_BOOK_DES, id, Cat));
            }
        });
//        adapter_rv1=new Adapter_Rv_Books(getActivity());

        binding.rvOneNew.setAdapter(adapter_rv1);
    }

    private void setUpRecyclerView2() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                getActivity(), RecyclerView.HORIZONTAL, false);
        binding.rvTwoNew.setLayoutManager(layoutManager);

        adapter_rv2 = new Adapter_Rv_Books(getActivity(), new BookDesInterface() {
            @Override
            public void layout(String id, Boolean Cat) {
                EventBus.getDefault().post(new MyEventBus(Constants.FROM_ADAPTER_TO_BOOK_DES, id, Cat));

            }

        });
//        adapter_rv2=new Adapter_Rv_Books(getActivity());


        binding.rvTwoNew.setAdapter(adapter_rv2);
    }

    private void setData() {
        ArrayList<ConstantsList> books1 = new ArrayList<>();
        ArrayList<ConstantsList> books2 = new ArrayList<>();
        for (int i = 0; i < Constants.LIST.size(); i++) {
            if (Constants.LIST.get(i).getBooks().getMost_listened()) {
                books2.add(Constants.LIST.get(i));
            } else books1.add(Constants.LIST.get(i));
        }
        adapter_rv2.setList(books2);
        adapter_rv1.setList(books1);
    }
}