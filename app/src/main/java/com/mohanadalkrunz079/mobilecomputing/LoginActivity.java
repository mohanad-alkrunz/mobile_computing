package com.mohanadalkrunz079.mobilecomputing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.mohanadalkrunz079.mobilecomputing.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.signup.setOnClickListener(v->{
            Toast.makeText(LoginActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
        });
    }
}