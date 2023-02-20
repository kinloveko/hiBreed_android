package com.example.hi_breed.ask_a_professional;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hi_breed.R;
import com.example.hi_breed.adapter.ask_professional.askProf_adapter;
import com.example.hi_breed.classesFile.BaseActivity;
import com.example.hi_breed.classesFile.POST;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ask_a_professional extends BaseActivity {

    EditText search_searchEditID;
    LinearLayout add_question;
    LinearLayout backLayoutPet;
    RecyclerView ask_question_recycler;
    askProf_adapter adapter;
    List<POST> postList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_aprofessional);

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

        search_searchEditID = findViewById(R.id.search_searchEditID);
        add_question = findViewById(R.id.add_question);
        backLayoutPet = findViewById(R.id.backLayoutPet);
        backLayoutPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ask_a_professional.this.onBackPressed();
                finish();
            }
        });
        ask_question_recycler = findViewById(R.id.ask_question_recycler);
        adapter =new askProf_adapter(this, FirebaseAuth.getInstance().getCurrentUser().getUid());
        ask_question_recycler.setLayoutManager(new GridLayoutManager(this,1));
        ask_question_recycler.setAdapter(adapter);


        FirebaseFirestore.getInstance().collection("Post")
                .whereEqualTo("displayFor", "Post").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            return;
                        }
                        if (value != null) {
                            adapter.clearList();
                            List<DocumentSnapshot> list = value.getDocuments();
                            if (list != null) {
                                for (DocumentSnapshot s : list) {
                                    POST post = s.toObject(POST.class);
                                    postList.add(post);
                                    adapter.addPetDisplay(postList);
                                }
                            }
                        }
                    }
                });

        search_searchEditID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(!s.toString().isEmpty()){
                        searchText(s.toString());
                    }
                    else{
                        adapter.addPetDisplay(postList);
                    }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        add_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ask_a_professional.this,ask_question.class));
            }
        });

    }

    private void searchText(String search) {
        List<POST> newList = new ArrayList<>();

        for(POST i : postList){
            if(i.getTitle().toLowerCase().contains(search.toLowerCase())){
                newList.add(i);
                adapter.addPetDisplay(newList);
            }
        }


    }

}