package com.example.myapplication.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication.constants.Constants;
import com.example.myapplication.databinding.DialogLayoutBinding;
import com.example.myapplication.listener.MyDialogInterface;

public class MyDialog extends Dialog {
    DialogLayoutBinding binding;
    Context context;
    MyDialogInterface dialoginterface;

    private static final String img_key = "img_key  ";

    public MyDialog(@NonNull Context context, MyDialogInterface dialoginterface) {
        super(context);
        this.context = context;
        this.dialoginterface = dialoginterface;
    }

    SharedPreferences sp;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sp = PreferenceManager.getDefaultSharedPreferences(context);
        edit = sp.edit();
        Window window = this.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        binding.man1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialoginterface.setImg(1);
                edit.putInt(Constants.IMG_KEY, Constants.IMG_MAN1);
                edit.commit();
                dismiss();
            }
        });
        binding.man2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialoginterface.setImg(2);
                edit.putInt(Constants.IMG_KEY, Constants.IMG_MAN2);
                edit.commit();
                dismiss();
            }
        });
        binding.man3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialoginterface.setImg(3);
                edit.putInt(Constants.IMG_KEY, Constants.IMG_MAN3);
                edit.commit();
                dismiss();
            }
        });
        binding.woman1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialoginterface.setImg(4);
                edit.putInt(Constants.IMG_KEY, Constants.IMG_WOMAN1);
                edit.commit();
                dismiss();
            }
        });
        binding.woman2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialoginterface.setImg(5);
                edit.putInt(Constants.IMG_KEY, Constants.IMG_WOMAN2);
                edit.commit();
                dismiss();
            }
        });
        binding.woman3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialoginterface.setImg(6);
                edit.putInt(Constants.IMG_KEY, Constants.IMG_WOMAN3);
                edit.commit();
                dismiss();
            }
        });

    }
}
