package com.example.myapplication.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activites.LoginActivity;
import com.example.myapplication.activites.TermsConditionsActivity;
import com.example.myapplication.constants.Constants;
import com.example.myapplication.databinding.FragmentExploreBinding;
import com.example.myapplication.databinding.FragmentSettingsBinding;
import com.example.myapplication.models.MyEventBus;
import com.example.myapplication.on.bording.IntroActivity;

import org.greenrobot.eventbus.EventBus;

public class SettingsFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    FragmentSettingsBinding binding;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.tvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int img_num = sp.getInt(Constants.IMG_KEY, 0);
        if (currentUser==null||(currentUser!=null&&currentUser.getEmail()==null)) {
            Toast.makeText(getActivity(), "login", Toast.LENGTH_SHORT).show();
            binding.userEmail.setText("");
            binding.userName.setText("انت مسجل كزائر");
            binding.tvSignIn.setVisibility(View.VISIBLE);
            binding.tvLogOut.setVisibility(View.INVISIBLE);
            binding.view.setVisibility(View.VISIBLE);
        }else {
            binding.tvSignIn.setVisibility(View.INVISIBLE);

            binding.userEmail.setText(currentUser.getEmail() + "");
        binding.userName.setText(currentUser.getDisplayName() + "" + "");
        getUserImg(img_num, binding.userImg);}
        binding.tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        binding.tvFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new MyEventBus(Constants.FROM_SETTINGS_TO_FAV, "FROM_SETTINGS_TO_FAV"));
            }
        });
        binding.tvTermsConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TermsConditionsActivity.class));
            }
        });
        binding.tvAbutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new MyEventBus(Constants.FROM_SETTINGS_TO_ABOUT_US, "FROM_SETTINGS_TO_ABOUT_US"));
            }
        });
        return view;
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Title");
        builder.setMessage("هل تريد تسجيل الخروج من التطبيق؟");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(getActivity(), IntroActivity.class);
                intent.putExtra(Constants.INTENT_KEY, Constants.FROM_FRAGMENT_DES_TO_INTRO);
                startActivity(intent);
                mAuth.signOut();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

}