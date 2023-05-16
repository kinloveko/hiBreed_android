package com.example.hi_breed.product;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hi_breed.R;
import com.example.hi_breed.adapter.product_adapter.productForDisplay;
import com.example.hi_breed.classesFile.product_class;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class product_my_product extends AppCompatActivity {

    RecyclerView my_products_view;
    productForDisplay adapter;
    RelativeLayout toolbarID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_my_product);


        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        }
        toolbarID = findViewById(R.id.toolbarID);
        toolbarID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product_my_product.this.onBackPressed();
                finish();
            }
        });
        my_products_view = findViewById(R.id.my_products_view);
        my_products_view.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new productForDisplay(getApplication());
        my_products_view.setAdapter(adapter);
        FirebaseFirestore.getInstance().collection("Pet")
                .whereEqualTo("vet_id", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("show",true)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            return;
                        }
                        if(value!=null){
                            adapter.clearList();
                            List<DocumentSnapshot> list = value.getDocuments();
                            for(DocumentSnapshot s: list){
                                product_class p = s.toObject(product_class.class);
                                adapter.addPetDisplay(p);
                            }
                        }

                    }
                });
    }
}