package com.mohanadalkrunz079.mobilecomputing.ui.Main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mohanadalkrunz079.mobilecomputing.adapters.BMIRecordsAdapter;
import com.mohanadalkrunz079.mobilecomputing.database_helper.BMIDBHelper;
import com.mohanadalkrunz079.mobilecomputing.database_helper.UserDBHelper;
import com.mohanadalkrunz079.mobilecomputing.databinding.ActivityMainBinding;
import com.mohanadalkrunz079.mobilecomputing.firebase.MyFirebaseProvider;
import com.mohanadalkrunz079.mobilecomputing.model.BMIRecord;
import com.mohanadalkrunz079.mobilecomputing.model.User;
import com.mohanadalkrunz079.mobilecomputing.ui.auth.LoginActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding ;

    private BMIRecordsAdapter adapter;

    final List<BMIRecord> my_records = new ArrayList<>();

    FirebaseAuth auth;
    FirebaseUser user;

    DatabaseReference recordsDBRef, userDBref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        user= auth.getCurrentUser();
        if (auth.getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        initUI();
        initDBRef();
        setData();

    }

    private void initDBRef() {
        recordsDBRef = MyFirebaseProvider.getChildDatabaseReference("BMI_Rec");
        userDBref = MyFirebaseProvider.getChildDatabaseReference("Users");
        recordsDBRef.keepSynced(true);
        userDBref.keepSynced(true);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user == null){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        binding.welcome.setText("Hi , "+user.getDisplayName());


        binding.addFood.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, AddFoodActivity.class);
            startActivity(intent);
        });

        binding.viewFood.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, FoodListActivity.class);
            startActivity(intent);
        });

        binding.addRecord.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this,AddRecordActivity.class);
            startActivity(intent);
        });

    }

    private void initUI(){

        adapter = new BMIRecordsAdapter(my_records);
        binding.recordsRv.setAdapter(adapter);

        binding.logout.setOnClickListener(v->{

            auth.signOut();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

    }

    public void setData() {
        my_records.clear();
        MyFirebaseProvider.getChildDatabaseReference("BMI_Rec").child(user.getUid()).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        my_records.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            BMIRecord c = snapshot1.getValue(BMIRecord.class);
                            my_records.add(c);
                        }

                        adapter.notifyDataSetChanged();
                        if(my_records.size()==0){
                            binding.noRecords.setVisibility(View.VISIBLE);
                            binding.recordsRv.setVisibility(View.GONE);

                        }else{
                            Collections.reverse(my_records);
                            binding.noRecords.setVisibility(View.GONE);
                            binding.recordsRv.setVisibility(View.VISIBLE);
                            binding.currentStatus.setText(my_records.get(0).getStatus());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}


