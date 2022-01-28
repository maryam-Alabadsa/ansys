package com.example.myapplication.fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activites.LoginActivity;
import com.example.myapplication.constants.Constants;
import com.example.myapplication.databinding.ContentMainBinding;
import com.example.myapplication.databinding.FragmentExploreBinding;
import com.example.myapplication.databinding.FragmentLibraryBinding;
import com.example.myapplication.models.UnfinishedBooks;
import com.example.myapplication.on.bording.IntroActivity;


public class LibraryFragment extends BaseFragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FragmentLibraryBinding binding;
    ContentMainBinding contentMainBinding;
    ColorStateList def;

    private String mParam1;
    private String mParam2;

    public LibraryFragment() {
    }

    public static LibraryFragment newInstance(String param1, String param2) {
        LibraryFragment fragment = new LibraryFragment();
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
        binding = FragmentLibraryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        if (currentUser==null||(currentUser!=null&&currentUser.getEmail()==null)) {
                binding.imageView5.setVisibility(View.VISIBLE);
                binding.textView2.setVisibility(View.VISIBLE);
                binding.btnLogin.setVisibility(View.VISIBLE);
                binding.layout.setVisibility(View.INVISIBLE);
                binding.conteiner2.setVisibility(View.INVISIBLE);

        }

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), IntroActivity.class);
                intent.putExtra(Constants.INTENT_KEY, Constants.FROM_FRAGMENT_DES_TO_INTRO);
                startActivity(intent);
                mAuth.signOut();
            }
        });
        contentMainBinding = binding.include;
        contentMainBinding.item1.setOnClickListener(this);
        contentMainBinding.item2.setOnClickListener(this);
        contentMainBinding.item3.setOnClickListener(this);
        def = contentMainBinding.item2.getTextColors();
        openFragment(LoadedBooksFragment.newInstance("", ""));

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.item1) {
            contentMainBinding.select.animate().x(0).setDuration(100);
            contentMainBinding.item1.setTextColor(Color.WHITE);
            contentMainBinding.item2.setTextColor(def);
            contentMainBinding.item3.setTextColor(def);
            openFragment(LoadedBooksFragment.newInstance("", ""));
        } else if (view.getId() == R.id.item2) {
            contentMainBinding.item1.setTextColor(def);
            contentMainBinding.item2.setTextColor(Color.WHITE);
            contentMainBinding.item3.setTextColor(def);
            int size = contentMainBinding.item2.getWidth();
            contentMainBinding.select.animate().x(size).setDuration(100);
            openFragment(UnfinishedBooksFragment.newInstance("", ""));

        } else if (view.getId() == R.id.item3) {
            contentMainBinding.item1.setTextColor(def);
            contentMainBinding.item3.setTextColor(Color.WHITE);
            contentMainBinding.item2.setTextColor(def);
            int size = contentMainBinding.item2.getWidth() * 2;
            contentMainBinding.select.animate().x(size).setDuration(100);
            openFragment(CompletedBooksFragment.newInstance("", ""));
        }
    }


    void openFragment(Fragment fragemnt) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.replace(R.id.conteiner2, fragemnt);
        fragmentTransaction.commit();
    }

}