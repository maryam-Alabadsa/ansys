package com.example.myapplication.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.activites.LoginActivity;
import com.example.myapplication.activites.MainActivity;
import com.example.myapplication.constants.Constants;
import com.example.myapplication.databinding.FragmentExploreBinding;
import com.example.myapplication.databinding.FragmentUserBinding;
import com.example.myapplication.dialog.MyDialog;
import com.example.myapplication.listener.MyDialogInterface;
import com.example.myapplication.models.MyEventBus;
import com.example.myapplication.on.bording.IntroActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.greenrobot.eventbus.EventBus;

import static android.app.Activity.RESULT_OK;


public class UserFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FragmentUserBinding binding;
    private String mParam1;
    private String mParam2;

    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
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
        binding = FragmentUserBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        if (currentUser==null||(currentUser!=null&&currentUser.getEmail()==null)) {
            Intent intent=new Intent(getActivity(), IntroActivity.class);
            mAuth.signOut();
            intent.putExtra(Constants.INTENT_KEY, Constants.FROM_FRAGMENT_DES_TO_INTRO);
            startActivity(intent);
        }

        fillItemFields();
        //---------------------change user info--------------
        binding.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableFields();
            }
        });
        //--------------------change password-------------------
//        binding.changePasswordBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                EventBus.getDefault().post(new MyEventBus(Constants.FROM_USER_TO_CHANGE_PASSWORD, "From EXPLORE To LATEST_PUBLISHED"));
//
//            }
//        });
        //--------------------change user img-------------------

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int img_num = sp.getInt(Constants.IMG_KEY, 0);
        binding.viewChangeUserImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDialog myDialog = new MyDialog(getActivity(), new MyDialogInterface() {
                    @Override
                    public void setImg(int p) {
                        getUserImg(p, binding.userImg);
                    }
                });
                myDialog.show();
            }
        });

        //------------------------------btn save changes-------------------------------------------------------
        binding.saveChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                afterChanged();
            }
        });
        getUserImg(img_num, binding.userImg);


        return view;
    }

    private void afterChanged() {
        updateProfile();
        binding.emailEd.setEnabled(false);
        binding.fullNameEd.setEnabled(false);
        binding.viewChangeUserImg.setVisibility(View.GONE);
        binding.saveChange.setVisibility(View.GONE);
        binding.fullNameEd.setText(currentUser.getDisplayName() + "");
    }

    private void enableFields() {
        binding.fullNameEd.setEnabled(true);
        binding.fullNameEd.setText("");
        binding.viewChangeUserImg.setVisibility(View.VISIBLE);
        binding.saveChange.setVisibility(View.VISIBLE);
    }

    private void fillItemFields() {
        binding.fullNameEd.setText(currentUser.getDisplayName() + "");
        binding.emailEd.setEnabled(false);
        binding.fullNameEd.setEnabled(false);
        binding.emailEd.setText(currentUser.getEmail() + "");
    }

    private void updateProfile() {
        String name;
        if (binding.fullNameEd.getText().toString().isEmpty()){
            name=currentUser.getDisplayName();
        }else {
            name=binding.fullNameEd.getText().toString();
        }

        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        currentUser.updateProfile(userProfileChangeRequest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete( Task<Void> task) {

                        if (task.isSuccessful()) {
                            currentUser.reload();
                            binding.fullNameEd.setText(currentUser.getDisplayName() + "");
                            Toast.makeText(getActivity(), "Profile Update for" + currentUser.getDisplayName(), Toast.LENGTH_SHORT).show();
                        } else {
                            task.getException().printStackTrace();
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })

        ;

    }

    public MainActivity activity;

}