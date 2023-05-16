package com.example.hi_breed.search;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hi_breed.R;
import com.example.hi_breed.adapter.search_bar_adapter.searchAdapter;
import com.example.hi_breed.classesFile.BaseActivity;
import com.example.hi_breed.classesFile.item;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class search_dashboard_home extends BaseActivity {


    private CardView card;
    private FragmentContainerView fragment;
    private ImageView backArrowImage;
    private com.example.hi_breed.adapter.search_bar_adapter.searchAdapter searchAdapter;
    private RecyclerView recyclerView;
    private List<item> list = new ArrayList<>();
    List<item> hold = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_dashboard_home);

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


        card = findViewById(R.id.card);

        recyclerView = findViewById(R.id.search_result);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        searchAdapter =new searchAdapter(this);
        List<String> category = new ArrayList<>();

        Query query = FirebaseFirestore.getInstance().collection("Search").whereEqualTo("show",true);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    return;
                }
                if (value != null) {
                    searchAdapter.clearList();
                    searchAdapter.holdClear();
                    for (DocumentSnapshot s : value) {
                        item i = s.toObject(item.class);

                            hold.add(i);
                            searchAdapter.hold(hold);

                        if (category.contains(s.getString("category"))) {
                            continue;
                        } else {

                            category.add(s.getString("category"));
                            item item = s.toObject(item.class);
                            list.add(item);
                        }
                    }
                    recyclerView.setAdapter(searchAdapter);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

        EditText searchBar = findViewById(R.id.search_bar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //this List purpose to check the id if its already in the view
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!s.toString().isEmpty()) {
                            card.setEnabled(true);
                            searchItems(s.toString());
                        }else {
                            searchAdapter.clearList();
                            card.setEnabled(false);
                        }
                    }

                },500);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!searchBar.getText().toString().isEmpty()) {
                    clickItems(searchBar.getText().toString());
                    card.setEnabled(true);
                }
                else
                    card.setEnabled(false);
            }
        });

        backArrowImage = findViewById(R.id.backArrowImage);
        backArrowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_dashboard_home.this.onBackPressed();
                finish();
            }
        });

    }

    private void clickItems(String text){
        List<item> newSearch = new ArrayList<>();
        for(item i:list){
            if(i.getCategory().toLowerCase().contains(text.toLowerCase())||i.getType().toLowerCase().contains(text.toLowerCase())){
               if(!newSearch.contains(i.getId())){
                   newSearch.add(i);
                   searchAdapter.addSearch(newSearch);
               }
                 recyclerView.setVisibility(View.VISIBLE);
            }
        }
        Intent intent = new Intent(search_dashboard_home.this, search_clicked.class);
        intent.putExtra("item",text);
        intent.putExtra("parcel", (Serializable) newSearch);
        startActivity(intent);
    }

    private void searchItems(String newText) {
        List<item> newSearch = new ArrayList<>();
        for(item i:list){
            if(i.getCategory().toLowerCase().contains(newText.toLowerCase()) || i.getType().toLowerCase().contains(newText.toLowerCase())){
                if(!newSearch.contains(i.getId())){
                    newSearch.add(i);
                    searchAdapter.addSearch(newSearch);

                }
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

}