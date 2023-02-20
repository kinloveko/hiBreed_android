package com.example.hi_breed.Pet;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.fragment.app.FragmentTransaction;

import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.BaseActivity;

public class pet_my_pets_panel extends BaseActivity {

    LinearLayout sellPetCardView_my_pet,datePetCard;
    RelativeLayout backLayout;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_pets_container);


        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        }

        datePetCard = findViewById(R.id.datePetCard);
        sellPetCardView_my_pet = findViewById(R.id.sellPetCardView_my_pet);



                 getSupportFragmentManager().beginTransaction()
                         .replace(R.id.fragment_my_pet,new pet_my_pet_for_sale_fragment(),"fragment_for_sale")
                .commit();


        datePetCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragment = getSupportFragmentManager().beginTransaction();
                fragment.replace(R.id.fragment_my_pet,new pet_my_pet_for_date_fragment(),"fragment_for_sale")
                        .commit();
            }
        });

        sellPetCardView_my_pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragment = getSupportFragmentManager().beginTransaction();
                fragment.replace(R.id.fragment_my_pet,new pet_my_pet_for_sale_fragment(),"fragment_for_date")
                        .commit();
            }
        });

        backLayout = findViewById(R.id.toolbarID);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pet_my_pets_panel.this.onBackPressed();
                finish();
            }
        });

    }

}