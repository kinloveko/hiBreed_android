package com.example.hi_breed;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class filter_search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_search);

        Intent intent = getIntent();
        String category = intent.getStringExtra("category");
        String color = intent.getStringExtra("color");
        String breed = intent.getStringExtra("breed");
        String size = intent.getStringExtra("size");
        String gender = intent.getStringExtra("gender");
        String shop = intent.getStringExtra("shop");
        String location = intent.getStringExtra("location");
        String serviceShooter = intent.getStringExtra("serviceShooter");
        String serviceVet = intent.getStringExtra("serviceVet");
        String productAccessories = intent.getStringExtra("productAccessories");
        String productMedicine = intent.getStringExtra("productMedicine");
    }
}