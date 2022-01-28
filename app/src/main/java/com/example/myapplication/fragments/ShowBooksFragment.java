package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapter.Adapter_Rv_Books;
import com.example.myapplication.constants.Constants;
import com.example.myapplication.databinding.FragmentExploreBinding;
import com.example.myapplication.databinding.FragmentShowBooksBinding;
import com.example.myapplication.listener.BookDesInterface;
import com.example.myapplication.models.Books;
import com.example.myapplication.models.ConstantsList;
import com.example.myapplication.models.MyEventBus;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class ShowBooksFragment extends BaseFragment {
    FragmentShowBooksBinding binding;
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    public ShowBooksFragment() {
        // Required empty public constructor
    }

    public static ShowBooksFragment newInstance(String param1) {
        ShowBooksFragment fragment = new ShowBooksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentShowBooksBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        if (mParam1 == "FROM_EXPLORE_TO_LATEST_PUBLISHED") {
            binding.viewToolbar2.setTitle("الاحدث نشرا ");
            setUpRecyclerView();
           adapter.setList(setData(true));
        } else {
            binding.viewToolbar2.setTitle("الاكثر استماعا ");
            setUpRecyclerView();
            adapter.setList(setData(false));
        }
        return view;
    }

    Adapter_Rv_Books adapter;

    private void setUpRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        binding.rv.setLayoutManager(layoutManager);
        adapter = new Adapter_Rv_Books(getActivity(), new BookDesInterface() {
            @Override
            public void layout(String id, Boolean Cat) {
                EventBus.getDefault().post(new MyEventBus(Constants.FROM_ADAPTER_TO_BOOK_DES, id, Cat));
            }
        });

        binding.rv.setAdapter(adapter);
    }

    private ArrayList<ConstantsList> setData(Boolean b) {
        ArrayList<ConstantsList> books = new ArrayList<>();
        for (int i = 0; i < Constants.LIST.size(); i++) {
            if (Constants.LIST.get(i).getBooks().getMost_listened()==b) {
                books.add(Constants.LIST.get(i));
            }
        }
   return books;
    }

}