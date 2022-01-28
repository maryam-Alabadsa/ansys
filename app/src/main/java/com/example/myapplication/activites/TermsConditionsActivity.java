package com.example.myapplication.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;

import com.example.myapplication.R;
import com.example.myapplication.constants.Constants;
import com.example.myapplication.databinding.ActivityLoginBinding;
import com.example.myapplication.databinding.ActivityTermsConditions2Binding;

public class TermsConditionsActivity extends AppCompatActivity {
    ActivityTermsConditions2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTermsConditions2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.textView3.setText(Html.fromHtml(Constants.HTML_TEXT, Html.FROM_HTML_MODE_COMPACT));
        } else {
            binding.textView3.setText(Html.fromHtml("<h2>Title</h2><br><p>Description here</p>"));
        }
    }
}