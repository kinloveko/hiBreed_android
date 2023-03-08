package com.example.hi_breed;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hi_breed.adapter.search_bar_adapter.searchClickedAdapter;
import com.example.hi_breed.classesFile.item;
import com.example.hi_breed.marketplace.m_market_place_container;

import java.util.ArrayList;
import java.util.List;

public class filter_search extends AppCompatActivity {

    private TextView noMatches;
    private LinearLayout searchBack;
    private RecyclerView search_result;
    private searchClickedAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_search);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        }
        else{
            window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.setStatusBarColor(Color.parseColor("#e28743"));
        }
        search_result = findViewById(R.id.search_result);
        search_result.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new searchClickedAdapter(this);
        searchBack = findViewById(R.id.searchBack);

        searchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(filter_search.this, m_market_place_container.class);
                // set the new task and clear flags
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();

            }
        });

        noMatches = findViewById(R.id.noMatches);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(filter_search.this, m_market_place_container.class);
        // set the new task and clear flags
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        List<item> newSearch = new ArrayList<>();
        adapter.clearList();
        Intent intent = getIntent();
        if(intent.getSerializableExtra("mode") !=null){

            List<item> hold = (List<item>) intent.getSerializableExtra("mode");

            for(item item:hold){
                newSearch.add(item);
                adapter.addSearch(newSearch);
            }
            search_result.setAdapter(adapter);
        }


        if (newSearch.size() != 0){
            noMatches.setVisibility(View.GONE);
            search_result.setVisibility(View.VISIBLE);
        }
        else{
            noMatches.setVisibility(View.VISIBLE);
            search_result.setVisibility(View.GONE);
        }


    }
}