package com.mohanadalkrunz079.mobilecomputing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Toast;

import com.mohanadalkrunz079.mobilecomputing.databinding.ActivitySignUpBinding;

public class Sign_upActivity extends AppCompatActivity {


    ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        binding.login.setOnClickListener(v->{
            startActivity(new Intent(Sign_upActivity.this,LoginActivity.class));
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


        binding.create.setOnClickListener(v->{

            validateData();
        });

    }

    private void validateData() {


        if(binding.username.getText().toString().isEmpty()){
            binding.username.setError(getResources().getString(R.string.required));
            return ;
        }

        if(binding.email.getText().toString().isEmpty()){
            binding.email.setError(getResources().getString(R.string.required));
            return ;
        }

        if(binding.password.getText().toString().isEmpty()){
            binding.password.setError(getResources().getString(R.string.required));
            return ;
        }

        if(binding.rePassword.getText().toString().isEmpty()){
            binding.rePassword.setError(getResources().getString(R.string.required));
            return ;
        }

        if(! binding.password.getText().toString().equals(binding.rePassword.getText().toString())){
            Toast.makeText(Sign_upActivity.this, "Password are not similar", Toast.LENGTH_SHORT).show();
            binding.rePassword.setError(getResources().getString(R.string.required));
            return;
        }


        Toast.makeText(Sign_upActivity.this, "Success Signup", Toast.LENGTH_SHORT).show();
    }
}