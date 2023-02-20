package com.example.hi_breed.classesFile;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class BaseActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    public String userId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication app = (MyApplication) getApplication();
        firestore = app.firestore;
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

        firestore.collection("User").document(userId)
                .update("online", "true");
    }
    @Override
    protected void onStart() {
        super.onStart();
        firestore.collection("User").document(userId)
                .update("online", "true");
    }
    @Override
    protected void onPause() {
        super.onPause();
        firestore.collection("User").document(userId)
                .update("online", FieldValue.serverTimestamp());
    }
}
