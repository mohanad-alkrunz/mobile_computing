package com.mohanadalkrunz079.mobilecomputing.ui.auth;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mohanadalkrunz079.mobilecomputing.database_helper.UserDBHelper;
import com.mohanadalkrunz079.mobilecomputing.databinding.ActivityUserInformationBinding;
import com.mohanadalkrunz079.mobilecomputing.model.User;
import com.mohanadalkrunz079.mobilecomputing.ui.Main.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class UserInformationActivity extends AppCompatActivity {

    private SharedPreferences loginPreferences = null;
    UserDBHelper userDBHelper;
    ActivityUserInformationBinding binding;
    private Calendar calendar;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userDBHelper = new UserDBHelper(this);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);


        initUI();



        binding.saveData.setOnClickListener(v->{

            String gender = "";

            if(binding.female.isChecked()){
                gender = "Female";
            }else {
                gender = "Male";
            }
            ArrayList<User> users = userDBHelper.getUsers();
            String username = loginPreferences.getString("username","no user");
            for (int i = 0 ; i< users.size();i++){
                if(username.equals(users.get(i).getUsername())){
                    userDBHelper.updateUserInformation(
                            users.get(i).getID(),
                            gender,
                            binding.weight.getText().toString(),
                            binding.length.getText().toString(),
                            binding.dateOfBirth.getText().toString()

                    );
                    break;
                }
            }
            Intent intent = new Intent(UserInformationActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Toast.makeText(UserInformationActivity.this, "Information saved successful", Toast.LENGTH_SHORT).show();
        });


    }

    private void initUI() {

        binding.dateOfBirth.setOnClickListener(v->{
            setDate();
        });

        binding.addWeight.setOnClickListener(v->{
            if(!binding.weight.getText().toString().isEmpty()){
                double current_weight = Double.valueOf(binding.weight.getText().toString());
                binding.weight.setText((current_weight + 1) + "");
            }
        });

        binding.minusWeight.setOnClickListener(v->{
            if(!binding.weight.getText().toString().isEmpty()){
                double current_weight = Double.valueOf(binding.weight.getText().toString());
                if(current_weight != 0){
                    binding.weight.setText((current_weight - 1) + "");
                }
            }
        });

        binding.addLength.setOnClickListener(v->{
            if(!binding.length.getText().toString().isEmpty()){
                double current_length = Double.valueOf(binding.length.getText().toString());
                binding.length.setText((current_length + 1) + "");
            }
        });

        binding.minusLength.setOnClickListener(v->{
            if(!binding.length.getText().toString().isEmpty()){
                double current_length = Double.valueOf(binding.length.getText().toString());
                if(current_length != 0){
                    binding.length.setText((current_length - 1) + "");
                }
            }
        });


    }

    @SuppressWarnings("deprecation")
    public void setDate() {
        showDialog(999);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = (arg0, arg1, arg2, arg3) ->
            showDate(arg1, arg2+1, arg3);

    private void showDate(int year, int month, int day) {
        binding.dateOfBirth.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

}