package com.mohanadalkrunz079.mobilecomputing.ui.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mohanadalkrunz079.mobilecomputing.adapters.FoodAdapter;
import com.mohanadalkrunz079.mobilecomputing.database_helper.FoodDBHelper;
import com.mohanadalkrunz079.mobilecomputing.databinding.ActivityAddFoodBinding;
import com.mohanadalkrunz079.mobilecomputing.databinding.ActivityFoodListBinding;
import com.mohanadalkrunz079.mobilecomputing.model.Food;

import java.util.ArrayList;
import java.util.List;

public class FoodListActivity extends AppCompatActivity {

    ActivityFoodListBinding binding;
    private FoodDBHelper foodDBHelper;
    private FoodAdapter adapter;

    private List<Food> foodList = new ArrayList<>();
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFoodListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");

        foodDBHelper = new FoodDBHelper(this);



        adapter = new FoodAdapter(foodList, new FoodAdapter.FoodListener() {
            @Override
            public void onEditClickListener(Context context, String food_id) {
                Intent intent = new Intent(context,AddFoodActivity.class);
                intent.putExtra("food_id",food_id);
                intent.putExtra("uid",uid);
                startActivity(intent);
            }

            @Override
            public void onDeleteClickListener(String food_id) {
                foodDBHelper.delete(food_id);
                getData();
            }
        });
        binding.foodRv.setAdapter(adapter);
        getData();
    }


    private void getData(){

        foodList.clear();

        List<Food> tempList = foodDBHelper.getRecords();

        for (int i = 0 ; i< tempList.size();i++){
            if(tempList.get(i).getUid().equals(uid)){
                foodList.add(tempList.get(i));
            }
        }
        adapter.notifyDataSetChanged();

    }
}