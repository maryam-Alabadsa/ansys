package com.example.myapplication.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.constants.Constants;
import com.example.myapplication.databinding.FragmentAboutUsBinding;
import com.example.myapplication.databinding.FragmentLibraryBinding;

public class AboutUsFragment extends Fragment {

    FragmentAboutUsBinding binding;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AboutUsFragment() {
        // Required empty public constructor
    }

    public static AboutUsFragment newInstance(String param1, String param2) {
        AboutUsFragment fragment = new AboutUsFragment();
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
        binding = FragmentAboutUsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tvText.setText(Html.fromHtml(Constants.HTML_TEXT_ABOUT_US, Html.FROM_HTML_MODE_COMPACT));
        } else {
            binding.tvText.setText(Html.fromHtml("<h2>Title</h2><br><p>Description here</p>"));
        }
        return view;
    }
}