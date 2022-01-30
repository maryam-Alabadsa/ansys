package com.example.myapplication.activites;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.constants.Constants;
import com.example.myapplication.databinding.ActivityRegisterBinding;
import com.example.myapplication.on.bording.IntroActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends BaseActivity {
    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        //----------------------btn_Register-----------------------
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.progressBar.setVisibility(View.VISIBLE);
                String email = binding.etEmail.getText().toString();
                String password = binding.etPassword.getText().toString();
                createAccount(email, password);
//                savePrefsData();

            }
        });
        binding.tvTermsConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), TermsConditionsActivity.class));
            }
        });
        //-----------------------btn_back-----------------------
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
//

    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        if (!binding.etEmail.getText().toString().isEmpty() && !binding.etPassword.getText().toString().isEmpty()) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                binding.progressBar.setVisibility(View.GONE);
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("createAccount", "createUserWithEmail:success");
                                Toast.makeText(RegisterActivity.this, "createUserWithEmail:success", Toast.LENGTH_SHORT).show();
                                currentUser = mAuth.getCurrentUser();
                                Toast.makeText(RegisterActivity.this, currentUser.getEmail() + "", Toast.LENGTH_SHORT).show();
                                updateProfile();
                                getAll();
                            } else {
                                binding.progressBar.setVisibility(View.GONE);

                                // If sign in fails, display a message to the user.
                                Log.e("createAccount", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Authentication failed."+ task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } else {
            binding.edEmail.setError("الرحاء ادخال بياناتك ");
            binding.etPassword.setError("الرحاء ادخال بياناتك ");
        }
        // [END create_user_with_email]
    }

    private void updateProfile() {

        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(binding.fullNameEt.getText().toString())
                .build();
        mAuth.getCurrentUser().updateProfile(userProfileChangeRequest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            currentUser.reload();
                            Toast.makeText(RegisterActivity.this, "Profile Update for" + currentUser.getDisplayName(), Toast.LENGTH_SHORT).show();
                        } else {
                            task.getException().printStackTrace();
                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })

        ;

    }


}
