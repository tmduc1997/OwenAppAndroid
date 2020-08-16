package com.example.owen2.app.Activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.owen2.R;

public class fragment_Pre_Login extends Fragment {
    ImageView img_back;
    Button btn_login,btn_signup;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_pre_login,container,false);
        img_back =view.findViewById(R.id.close_prelogin);
        btn_login=view.findViewById(R.id.pre_btn_login);
        btn_signup=view.findViewById(R.id.pre_btn_signup);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               fragmentManager = ((FragmentActivity)getContext()).getSupportFragmentManager();
               fragmentTransaction=fragmentManager.beginTransaction();
               fragmentTransaction.remove(fragment_Pre_Login.this);
               fragmentTransaction.commit();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = ((FragmentActivity)getContext()).getSupportFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.full_screen,new fragment_login());
                fragmentTransaction.commit();
            }
        });
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = ((FragmentActivity)getContext()).getSupportFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.full_screen,new fragmentSignUP());
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


}
