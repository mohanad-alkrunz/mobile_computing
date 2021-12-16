package com.mohanadalkrunz079.mobilecomputing.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mohanadalkrunz079.mobilecomputing.R;
import com.mohanadalkrunz079.mobilecomputing.database_helper.BMIDBHelper;
import com.mohanadalkrunz079.mobilecomputing.database_helper.UserDBHelper;
import com.mohanadalkrunz079.mobilecomputing.databinding.ActivityLoginBinding;
import com.mohanadalkrunz079.mobilecomputing.model.User;
import com.mohanadalkrunz079.mobilecomputing.ui.Main.AddFoodActivity;
import com.mohanadalkrunz079.mobilecomputing.ui.Main.MainActivity;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

//    UserDBHelper userDBHelper;
    private BMIDBHelper bmidbHelper;
    private SharedPreferences loginPreferences = null;
    private SharedPreferences.Editor loginPrefsEditor;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
//        userDBHelper = new UserDBHelper(this);
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

        mAuth.signInWithEmailAndPassword(binding.username.getText().toString(), binding.password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            System.out.println(user.getDisplayName());
                            loginPrefsEditor.putString("username", user.getDisplayName());
                            loginPrefsEditor.putString("id", user.getUid());
                            loginPrefsEditor.commit();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }
}