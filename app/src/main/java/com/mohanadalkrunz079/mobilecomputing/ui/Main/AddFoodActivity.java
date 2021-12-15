package com.mohanadalkrunz079.mobilecomputing.ui.Main;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.mohanadalkrunz079.mobilecomputing.R;
import com.mohanadalkrunz079.mobilecomputing.database_helper.FoodDBHelper;
import com.mohanadalkrunz079.mobilecomputing.databinding.ActivityAddFoodBinding;
import com.mohanadalkrunz079.mobilecomputing.model.Food;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class AddFoodActivity extends AppCompatActivity {

    ActivityAddFoodBinding binding;
    String[] food_categories = { "Fish", "Vegetables",
            "Juice", "Fruits",
            "Fries", "Frozen" };

    String uid = "";
    String food_id="";
    private String Document_img1="";
    private static final int REQUEST_GALLERY_CODE_PRIMARY = 201;
    private File file;
    String cat;
    private FoodDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        food_id = intent.getStringExtra("food_id");
        if(food_id == null){
            food_id ="";
        }



        dbHelper = new FoodDBHelper(this);
        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                food_categories);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.category.setAdapter(ad);

        binding.category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(AddFoodActivity.this, food_categories[position] + uid, Toast.LENGTH_SHORT).show();
                cat = food_categories[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if (!food_id.isEmpty()){
            binding.title.setText("Edit Food");
            List<Food> food_list = dbHelper.getRecords();
            for (int i=0 ; i<food_list.size();i++){
                if(food_list.get(i).getId().equals(food_id)){
                    binding.calory.setText(food_list.get(i).getCalory());
                    for(int j=0;j<food_categories.length;j++){
                        if (food_list.get(i).getCategory().equals(food_categories[j])){
                            binding.category.setSelection(j);
                        }
                    }
                    binding.foodName.setText(food_list.get(i).getName());
                    byte[] imgByte = food_list.get(i).getImage();
                    binding.attachImage.setImageBitmap(BitmapFactory.decodeByteArray(imgByte,0,imgByte.length));
                }
            }
        }

        binding.uploadImage.setOnClickListener(v->{
            selectImagePrimary();
        });

        binding.saveRecord.setOnClickListener(v->{

            if(validate_fields()){

                String name = binding.foodName.getText().toString();
                String category =  cat;
                String calory = binding.calory.getText().toString();
                String userid = uid;
                BitmapDrawable bitmapDrawable = ((BitmapDrawable) binding.attachImage.getDrawable());
                Bitmap bitmap = bitmapDrawable .getBitmap();
                byte[] image = getBitmapAsByteArray(bitmap);
                String key_name = new Date().getTime() + "";
                if(food_id.isEmpty()){
                    dbHelper.addRecord(name,category,image,calory,userid,key_name);
                    Toast.makeText(AddFoodActivity.this, "Food Added Successful", Toast.LENGTH_SHORT).show();
                }else{
                    dbHelper.updateFoodInformation(food_id,name,cat,calory,image);
                }



            }
        });
    }

    private boolean validate_fields(){
        if (binding.foodName.getText().toString().isEmpty()){
            binding.foodName.setError(getResources().getString(R.string.required));
            return false;
        }
        if (binding.calory.getText().toString().isEmpty()){
            binding.calory.setError(getResources().getString(R.string.required));
            return false;
        }
        return true;
    }

    private void selectImagePrimary() {
        String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, galleryPermissions)) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GALLERY_CODE_PRIMARY);
        } else {
            EasyPermissions.requestPermissions(this, "Access for storage",
                    101, galleryPermissions);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY_CODE_PRIMARY) {
            if (resultCode == RESULT_OK) {
                String path = getRealPathFromURI(this, data.getData());
                Log.d("path", path);
                file = (getFile(path));
                binding.attachImage.setImageURI(data.getData());
            }
        }
    }
    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }


    public static String getRealPathFromURI(Context context , Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    public   static File getFile(String path) {
        return new File(path);
    }

}