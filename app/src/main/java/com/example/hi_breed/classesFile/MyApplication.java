package com.example.hi_breed.classesFile;

import android.app.Application;

import com.google.firebase.firestore.FirebaseFirestore;
public class MyApplication extends Application {

    public FirebaseFirestore firestore;


    @Override
    public void onCreate() {
        super.onCreate();
        firestore = FirebaseFirestore.getInstance();

    }

    public FirebaseFirestore getFirestore() {
        return firestore;
    }
}