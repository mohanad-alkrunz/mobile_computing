package com.mohanadalkrunz079.mobilecomputing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
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
            startActivity(new Intent(LoginActivity.this,Sign_upActivity.class));
        });

        binding.showPassword.setOnClickListener(v->{

            binding.password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            binding.hidePassword.setVisibility(View.VISIBLE);
            binding.showPassword.setVisibility(View.GONE);
        });

        binding.hidePassword.setOnClickListener(v->{

            binding.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            binding.showPassword.setVisibility(View.VISIBLE);
            binding.hidePassword.setVisibility(View.GONE);
        });



        binding.login.setOnClickListener(v->{

            validateInputs();



        });
    }

    private void validateInputs() {

        if(binding.username.getText().toString().isEmpty()){
            binding.username.setError(getResources().getString(R.string.required));
            return ;
        }

        if(binding.password.getText().toString().isEmpty()){
            binding.password.setError(getResources().getString(R.string.required));
            return ;
        }

        Toast.makeText(LoginActivity.this, "Success Login", Toast.LENGTH_SHORT).show();
    }
}