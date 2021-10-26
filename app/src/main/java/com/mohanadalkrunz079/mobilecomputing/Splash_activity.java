package com.mohanadalkrunz079.mobilecomputing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.mohanadalkrunz079.mobilecomputing.databinding.ActivitySplashBinding;

public class Splash_activity extends AppCompatActivity {

    ActivitySplashBinding binding;
    //#Created by mohanad alkrunz 26-Oct-2021 computing lab
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.goNextPage.setOnClickListener(v->{
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        });

    }
}