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
import com.mohanadalkrunz079.mobilecomputing.database_helper.BMIDBHelper;
import com.mohanadalkrunz079.mobilecomputing.database_helper.UserDBHelper;
import com.mohanadalkrunz079.mobilecomputing.databinding.ActivityLoginBinding;
import com.mohanadalkrunz079.mobilecomputing.model.User;
import com.mohanadalkrunz079.mobilecomputing.ui.Main.MainActivity;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    UserDBHelper userDBHelper;
    private BMIDBHelper bmidbHelper;
    private SharedPreferences loginPreferences = null;
    private SharedPreferences.Editor loginPrefsEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userDBHelper = new UserDBHelper(this);
        bmidbHelper = new BMIDBHelper(this);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        if (! loginPreferences.getString("username","no user").equals("no user")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        binding.signup.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, Sign_upActivity.class));
        });
        binding.showPassword.setOnClickListener(v -> {
            binding.password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            binding.hidePassword.setVisibility(View.VISIBLE);
            binding.showPassword.setVisibility(View.GONE);
        });
        binding.hidePassword.setOnClickListener(v -> {
            binding.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            binding.showPassword.setVisibility(View.VISIBLE);
            binding.hidePassword.setVisibility(View.GONE);
        });

        binding.login.setOnClickListener(v -> {
            validateInputs();
        });
    }

    private void validateInputs() {

        if (binding.username.getText().toString().isEmpty()) {
            binding.username.setError(getResources().getString(R.string.required));
            return;
        }

        if (binding.password.getText().toString().isEmpty()) {
            binding.password.setError(getResources().getString(R.string.required));
            return;
        }


        ArrayList<User> users_list = userDBHelper.getUsers();

        String username = binding.username.getText().toString();
        String password = binding.password.getText().toString();
        boolean isLogin = false;
        if (users_list != null || users_list.size() != 0) {
            for (int i = 0; i < users_list.size(); i++) {
                if (username.equals(users_list.get(i).getUsername()) && password.equals(users_list.get(i).getPassword())) {
                    loginPrefsEditor.putString("id", users_list.get(i).getID());
                    loginPrefsEditor.commit();
                    isLogin = true;
                    break;
                } else {
                    isLogin = false;
                }

            }
        }

        if (isLogin) {
            Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
            loginPrefsEditor.putString("username", binding.username.getText().toString());
            loginPrefsEditor.commit();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else{
            Toast.makeText(LoginActivity.this, "Invalid Login", Toast.LENGTH_SHORT).show();
        }

    }
}