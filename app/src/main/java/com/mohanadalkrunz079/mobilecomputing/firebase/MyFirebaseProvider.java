package com.mohanadalkrunz079.mobilecomputing.firebase;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mohanadalkrunz079.mobilecomputing.model.BMIRecord;

import java.util.Date;




/**
 * Created by eyada on 23/11/2017.
 */

public class MyFirebaseProvider {
    private static DatabaseReference databaseReference;
    private static String currentUserUID;


    public static DatabaseReference getDatabaseReference() {
        if (databaseReference == null)
            databaseReference = FirebaseDatabase.getInstance().getReference();
        return databaseReference;
    }

    public static DatabaseReference getChildDatabaseReference(String childName) {
        return getDatabaseReference().child(childName);
    }

    public static void DoAddRecord(BMIRecord record, String user_id, final MessageListener messageListener) {

        DatabaseReference messageNode = getChildDatabaseReference("BMI_Rec").child(user_id).child(record.getID());
        messageNode.setValue(record).addOnSuccessListener(aVoid -> messageListener.onResult(true, record.getID())).addOnFailureListener(e -> messageListener.onResult(false, ""));
    }






}