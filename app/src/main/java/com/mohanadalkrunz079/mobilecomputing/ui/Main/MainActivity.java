package com.mohanadalkrunz079.mobilecomputing.ui.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.mohanadalkrunz079.mobilecomputing.adapters.BMIRecordsAdapter;
import com.mohanadalkrunz079.mobilecomputing.database_helper.BMIDBHelper;
import com.mohanadalkrunz079.mobilecomputing.database_helper.UserDBHelper;
import com.mohanadalkrunz079.mobilecomputing.databinding.ActivityMainBinding;
import com.mohanadalkrunz079.mobilecomputing.model.BMIRecord;
import com.mohanadalkrunz079.mobilecomputing.model.User;
import com.mohanadalkrunz079.mobilecomputing.ui.auth.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding ;

    private UserDBHelper userDBHelper;
    private User user;

    private BMIDBHelper bmidbHelper;

    boolean isLogin = false;

    private SharedPreferences loginPreferences = null;
    private SharedPreferences.Editor loginPrefsEditor;

    private List<BMIRecord> recordList;
    private BMIRecordsAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        initUI();
        checkUser();
        displayUserRecords();


    }

    private void displayUserRecords() {

        List<BMIRecord> tempList = new ArrayList<>();
        recordList.clear();
        tempList = bmidbHelper.getRecords();
        for(int i=0 ; i<tempList.size();i++){
            if(tempList.get(i).getUID().equals(user.getID())){
                recordList.add(tempList.get(i));
            }
        }
        if (recordList.size() == 0){
            binding.recordsRv.setVisibility(View.GONE);
            binding.noRecords.setVisibility(View.VISIBLE);
            binding.currentStatus.setText("Unknown");
        }else{
            binding.recordsRv.setVisibility(View.VISIBLE);
            binding.noRecords.setVisibility(View.GONE);
            binding.currentStatus.setText(recordList.get(recordList.size()-1).getStatus());
        }

    }

    private void initUI(){
        userDBHelper = new UserDBHelper(this);
        user = new User();

        bmidbHelper = new BMIDBHelper(this);

        recordList = new ArrayList<>();
        adapter = new BMIRecordsAdapter(recordList);
        binding.recordsRv.setAdapter(adapter);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();





        binding.logout.setOnClickListener(v->{
            loginPrefsEditor.putString("username", "no user");
            loginPrefsEditor.commit();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

    }

    private void checkUser() {
        ArrayList<User> users_list = userDBHelper.getUsers();
        for (int i = 0; i < users_list.size(); i++) {
            if (loginPreferences.getString("username","no user").equals(users_list.get(i).getUsername())) {
                user.setUsername(users_list.get(i).getUsername());
                user.setEmail(users_list.get(i).getEmail());
                user.setID(users_list.get(i).getID());
                isLogin = true;
                break;
            } else {
                isLogin = false;
            }

        }
        binding.welcome.setText("Hi , "+user.getUsername());
        if(!isLogin){
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }


        binding.addRecord.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this,AddRecordActivity.class);
            intent.putExtra("uid",user.getID());
            intent.putExtra("dob",user.getDob());
            startActivity(intent);
        });
    }


}


