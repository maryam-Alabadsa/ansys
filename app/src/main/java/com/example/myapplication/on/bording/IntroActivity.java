package com.example.myapplication.on.bording;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import com.example.myapplication.R;
import com.example.myapplication.activites.BaseActivity;
import com.example.myapplication.activites.LoginActivity;
import com.example.myapplication.activites.MainActivity;
import com.example.myapplication.activites.RegisterActivity;
import com.example.myapplication.activites.SplashActivity;
import com.example.myapplication.constants.Constants;
import com.example.myapplication.databinding.ActivityIntroBinding;
import com.example.myapplication.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends BaseActivity {
    //    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    ActivityIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //Data
        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("مكتبة ضخمة و متجددة باستمرار ", R.drawable.book_stack1));
        mList.add(new ScreenItem("الاستماع للكتب بأفضل جودة ", R.drawable.audio_book));
        mList.add(new ScreenItem("امكانية تنزيل الكتب ع جهازك بدن نترنت ", R.drawable.download_file));

        if (restorePreData()) {
            if ( getIntent().getIntExtra(Constants.INTENT_KEY,0)== Constants.FROM_FRAGMENT_DES_TO_INTRO) {
                binding.screenViewpager.setCurrentItem( mList.size() - 1, true);

            }else{
                Intent mainActivity = new Intent(getApplicationContext(), SplashActivity.class);
                startActivity(mainActivity);
                finish();
            }

        }


        //Setup viewPager
        introViewPagerAdapter = new IntroViewPagerAdapter(this, mList);
        binding.screenViewpager.setAdapter(introViewPagerAdapter);

        //Setup tab indicator
        binding.tabIndicator.setupWithViewPager(binding.screenViewpager);

        //Button Next
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.screenViewpager.setCurrentItem(binding.screenViewpager.getCurrentItem() + 1, true);
            }
        });

        binding.tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == mList.size() - 1) {
                    loadLastScreen();
                } else {
                    loadScreens();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //Button Login
        binding.btnGetLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(mainActivity);
                finish();
            }
        });
        //Button Register
        binding.btnGetRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainActivity = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(mainActivity);
                finish();
            }
        });
        binding.tvRegisterVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.progressBar.setVisibility(View.VISIBLE);
                mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            binding.progressBar.setVisibility(View.GONE);
                            Log.d("signIn as a visitor", "signIn as a visitor:success");
                            Intent intent = new Intent(IntroActivity.this, SplashActivity.class);
                            startActivity(intent);
                            savePrefsData();
                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                            Log.d("signIn as a visitor", "signIn as a visitor:failure", task.getException());
                        }

                    }

                });

            }
        });
    }

    private void loadLastScreen() {
        binding.linearLayoutNext.setVisibility(View.INVISIBLE);
        binding.linearLayoutGetStarted.setVisibility(View.VISIBLE);
        binding.tvRegisterVisitor.setVisibility(View.VISIBLE);
    }

    private void loadScreens() {
        binding.linearLayoutNext.setVisibility(View.VISIBLE);
        binding.linearLayoutGetStarted.setVisibility(View.INVISIBLE);
        binding.tvRegisterVisitor.setVisibility(View.INVISIBLE);
    }
}
