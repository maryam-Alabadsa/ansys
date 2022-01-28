package com.example.myapplication.fragments;

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
import com.example.myapplication.adapter.Adapter_Rv_Books;
import com.example.myapplication.constants.Constants;
import com.example.myapplication.databinding.FragmentFavBookBinding;
import com.example.myapplication.databinding.FragmentShowBooksBinding;
import com.example.myapplication.fav.Favorite;
import com.example.myapplication.fav.FavoriteArray;
import com.example.myapplication.listener.BookDesInterface;
import com.example.myapplication.models.Books;
import com.example.myapplication.models.ConstantsList;
import com.example.myapplication.models.MyEventBus;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavBookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavBookFragment extends BaseFragment {
    FragmentFavBookBinding binding;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FavBookFragment() {
        // Required empty public constructor
    }

    public static FavBookFragment newInstance(String param1, String param2) {
        FavBookFragment fragment = new FavBookFragment();
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
        binding = FragmentFavBookBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        getListFav();
        setUpRecyclerView();
        return view;
    }

    Adapter_Rv_Books adapter;

    private void setUpRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        binding.rv.setLayoutManager(layoutManager);
        adapter = new Adapter_Rv_Books(getActivity(), new BookDesInterface() {
            @Override
            public void layout(String id, Boolean Cat) {
                EventBus.getDefault().post(new MyEventBus(Constants.FROM_ADAPTER_TO_BOOK_DES, id,Cat));
            }
        });
        binding.rv.setAdapter(adapter);
    }

    Favorite favorites;
    ArrayList<FavoriteArray> favoriteArrays;

    public void getListFav() {

        ArrayList<ConstantsList> list = new ArrayList<>();
        favoriteArrays = new ArrayList<>();

        CollectionReference questionRef = firebaseFirestore.collection("fav");
        questionRef
                .whereEqualTo("userId", currentUser.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                binding.progressBar2.setVisibility(View.GONE);
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        favorites = snapshot.toObject(Favorite.class);
                    }
                    favoriteArrays=favorites.getList();
                    for (int i = 0; i < Constants.LIST.size(); i++) {
                        for (int j = 0; j <favoriteArrays.size() ; j++) {
                            if (Constants.LIST.get(i).getId().equals(favoriteArrays.get(j).getBookId())) {
                                list.add(Constants.LIST.get(i));
                            }
                        }
                    }
                    adapter.setList(list);
                }
            }
        });
    }


}