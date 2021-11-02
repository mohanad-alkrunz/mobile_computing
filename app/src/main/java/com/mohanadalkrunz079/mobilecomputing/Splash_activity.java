package com.mohanadalkrunz079.mobilecomputing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.mohanadalkrunz079.mobilecomputing.databinding.ActivitySplashBinding;

public class Splash_activity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 2000;


    ActivitySplashBinding binding;
    //#Created by mohanad alkrunz 26-Oct-2021 computing lab
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.goNextPage.setOnClickListener(v->{
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    super.run();
                    sleep(SPLASH_TIME_OUT);
                } catch (InterruptedException e) {
                    System.out.println(e.toString());
                } finally {
                        startActivity(new Intent(Splash_activity.this,LoginActivity.class));
                }
            }
        };
        thread.start();
    }
}