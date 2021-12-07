package com.mohanadalkrunz079.mobilecomputing.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Toast;

import com.mohanadalkrunz079.mobilecomputing.R;
import com.mohanadalkrunz079.mobilecomputing.database_helper.UserDBHelper;
import com.mohanadalkrunz079.mobilecomputing.databinding.ActivitySignUpBinding;
import com.mohanadalkrunz079.mobilecomputing.model.User;

import java.util.ArrayList;

public class Sign_upActivity extends AppCompatActivity {


    ActivitySignUpBinding binding;

    UserDBHelper userDBHelper;


    private SharedPreferences loginPreferences = null;
    private SharedPreferences.Editor loginPrefsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userDBHelper = new UserDBHelper(this);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        binding.login.setOnClickListener(v->{
            startActivity(new Intent(Sign_upActivity.this, LoginActivity.class));
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


        ArrayList<User> users_list = userDBHelper.getUsers() ;

        if(users_list != null || users_list.size() != 0){
            for(int i=0 ; i< users_list.size();i++){

                if(binding.username.getText().toString().equals(users_list.get(i).getUsername())){
                    Toast.makeText(Sign_upActivity.this, "This username is already exist", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(binding.email.getText().toString().equals(users_list.get(i).getEmail())){
                    Toast.makeText(Sign_upActivity.this, "This email is already exist", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }


        userDBHelper.addUser(
                binding.username.getText().toString(),
                binding.password.getText().toString(),
                binding.email.getText().toString()
                            );

        loginPrefsEditor.putString("username", binding.username.getText().toString());
        loginPrefsEditor.commit();

        Intent intent = new Intent(Sign_upActivity.this, UserInformationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        Toast.makeText(Sign_upActivity.this, "Success Signup", Toast.LENGTH_SHORT).show();
    }
}