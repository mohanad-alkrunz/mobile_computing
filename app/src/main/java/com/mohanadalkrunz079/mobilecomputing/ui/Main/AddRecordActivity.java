package com.mohanadalkrunz079.mobilecomputing.ui.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.mohanadalkrunz079.mobilecomputing.R;
import com.mohanadalkrunz079.mobilecomputing.database_helper.BMIDBHelper;
import com.mohanadalkrunz079.mobilecomputing.database_helper.UserDBHelper;
import com.mohanadalkrunz079.mobilecomputing.databinding.ActivityAddRecordBinding;
import com.mohanadalkrunz079.mobilecomputing.databinding.ActivityUserInformationBinding;

import java.util.Calendar;
import java.util.Date;

public class AddRecordActivity extends AppCompatActivity {


    ActivityAddRecordBinding binding;
    String uid,dob,gender;


    private BMIDBHelper bmidbHelper;

    private Calendar calendar;
    private int year, month, day;
    double last_record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddRecordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        dob = intent.getStringExtra("dob");
        gender = intent.getStringExtra("gender");
        last_record = intent.getDoubleExtra("last_record",0);


        initUI();


    }

    private void initUI() {
        bmidbHelper = new BMIDBHelper(this);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);


        binding.date.setOnClickListener(v->{
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





        binding.saveRecord.setOnClickListener(v->{
                double bmi = 0 ;
                double weight = Double.valueOf(binding.weight.getText().toString());
                double length = Double.valueOf(binding.length.getText().toString());

                double age = getAge(dob);
                double age_percent = 0.7;

                if ( age >= 2 && age <= 10){
                    age_percent = 0.7;
                }else if( (age > 10 && age <= 20) && gender.equals("Male")){
                    age_percent = 0.9;
                }else if( (age > 10 && age <= 20) && gender.equals("Female")){
                    age_percent = 0.9;
                }else if (age > 20 ){
                    age_percent = 1;
                }else{
                    Toast.makeText(AddRecordActivity.this, "UnSupported Age", Toast.LENGTH_SHORT).show();
                    return;
                }

                bmi = (weight / (length * length)) * age_percent;

                String status = "";

                if(bmi < 18.5){
                    status = "Underweight";
                    status =  checkStatus(status,bmi);
                }else if( bmi >= 18.5 && bmi < 25){
                    status = "Healthy Weight";
                    status =  checkStatus(status,bmi);
                }else if( bmi >= 25 && bmi < 30){
                    status = "Overweight";
                    status =  checkStatus(status,bmi);
                }else if (bmi >= 30){
                    status = "Obesity";
                    status =  checkStatus(status,bmi);
                }

            bmidbHelper.addRecord(
                    binding.weight.getText().toString(),
                    binding.length.getText().toString(),
                    binding.date.getText().toString(),
                    status,
                    uid,
                    bmi+""
            );
            Toast.makeText(AddRecordActivity.this, "Record Added Successful", Toast.LENGTH_SHORT).show();

        });
    }


    private String checkStatus(String status , double bmi){
         double delta = bmi - last_record;
         switch (status){
             case "Underweight":
                 if (delta < -1){
                     status += " (So Bad)";
                 }else if( delta >= -1 && delta < -0.6){
                     status += " (So Bad)";
                 }
                 else if( delta >= -0.6 && delta < -0.3){
                     status += " (So Bad)";
                 }else if( delta >= -0.3 && delta < 0){
                     status += " (Little Change)";
                 }else if( delta >= 0 && delta < 0.3){
                     status += " (Little Change)";
                 }else if( delta >= 0.3 && delta < 0.6){
                     status += " (Still Good)";
                 }else if( delta >= 0.6 && delta < 1){
                     status += " (Go Ahead)";
                 }else if( delta >= 1 ){
                     status += " (Go Ahead)";
                 }
                 break;
             case "Healthy Weight":
                 if (delta < -1){
                     status += " (So Bad)";
                 }else if( delta >= -1 && delta < -0.6){
                     status += " (Be Careful)";
                 }
                 else if( delta >= -0.6 && delta < -0.3){
                     status += " (Be Careful)";
                 }else if( delta >= -0.3 && delta < 0){
                     status += " (Little Change)";
                 }else if( delta >= 0 && delta < 0.3){
                     status += " (Little Change)";
                 }else if( delta >= 0.3 && delta < 0.6){
                     status += " (Be Careful)";
                 }else if( delta >= 0.6 && delta < 1){
                     status += " (Be Careful)";
                 }else if( delta >= 1 ){
                     status += " (Be Careful)";
                 }
                 break;
             case "Overweight":
                 if (delta < -1){
                     status += " (Be Careful)";
                 }else if( delta >= -1 && delta < -0.6){
                     status += " (Go Ahead)";
                 }
                 else if( delta >= -0.6 && delta < -0.3){
                     status += " (Still Good)";
                 }else if( delta >= -0.3 && delta < 0){
                     status += " (Little Change)";
                 }else if( delta >= 0 && delta < 0.3){
                     status += " (Little Change)";
                 }else if( delta >= 0.3 && delta < 0.6){
                     status += " (Be Careful)";
                 }else if( delta >= 0.6 && delta < 1){
                     status += " (So Bad)";
                 }else if( delta >= 1 ){
                     status += " (So Bad)";
                 }
                 break;
             case "Obesity":
                 if (delta < -1){
                     status += " (Go Ahead)";
                 }else if( delta >= -1 && delta < -0.6){
                     status += " (Go Ahead)";
                 }
                 else if( delta >= -0.6 && delta < -0.3){
                     status += " (Little Change)";
                 }else if( delta >= -0.3 && delta < 0){
                     status += " (Little Change)";
                 }else if( delta >= 0 && delta < 0.3){
                     status += " (Be Careful)";
                 }else if( delta >= 0.3 && delta < 0.6){
                     status += " (So Bad)";
                 }else if( delta >= 0.6 && delta < 1){
                     status += " (So Bad)";
                 }else if( delta >= 1 ){
                     status += " (So Bad)";
                 }
                 break;
         }


        return status;
    }

    private double getAge(String date){

        Date date1 = new Date(binding.date.getText().toString());

        Date date2 = new Date(date);

        long diff = date1.getTime() - date2.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        double age = days / 365.0 ;
        return age;
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
        binding.date.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }
}