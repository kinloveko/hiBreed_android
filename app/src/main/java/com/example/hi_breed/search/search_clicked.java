package com.example.hi_breed.search;

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

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hi_breed.R;
import com.example.hi_breed.adapter.search_bar_adapter.searchClickedAdapter;
import com.example.hi_breed.classesFile.BaseActivity;
import com.example.hi_breed.classesFile.item;

import java.util.ArrayList;
import java.util.List;

public class search_clicked extends BaseActivity {

    private TextView headerName;
    private LinearLayout searchBack;
    private RecyclerView search_result;
    private searchClickedAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_clicked);
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
                search_clicked.this.onBackPressed();
                finish();
            }
        });

        headerName = findViewById(R.id.headerName);

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        adapter.clearList();
        Intent intent = getIntent();
        String i = intent.getStringExtra("item");
        headerName.setText("Search results:"+i);
        List<item> hold = (List<item>) getIntent().getSerializableExtra("parcel");
        List<item> newSearch = new ArrayList<>();
        for(item item:hold){
            if(item.getCategory().toLowerCase().contains(i.toLowerCase())){
                newSearch.add(item);
                adapter.addSearch(newSearch);
            }
        }
        search_result.setAdapter(adapter);
    }
}