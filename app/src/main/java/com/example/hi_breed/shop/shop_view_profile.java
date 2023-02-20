package com.example.hi_breed.shop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hi_breed.R;
import com.example.hi_breed.adapter.edit_pet_for_sale_adapter.petDisplayForSaleAdapter;
import com.example.hi_breed.classesFile.BaseActivity;
import com.example.hi_breed.classesFile.PetSaleClass;
import com.example.hi_breed.classesFile.ShopClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class shop_view_profile extends BaseActivity {


    FirebaseUser firebaseUser;
    RecyclerView petForSale_view;
    petDisplayForSaleAdapter adapter;
    TextView viewShopName,viewShopVerified,viewShopReviews,viewShopLink,editShop;
    ImageView imageShopProfile,imageShopBackground;
    LinearLayout backLayoutShop;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_shop_view_profile);



        Window w = getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        imageShopBackground = findViewById(R.id.view_shop_coverImageID);
        imageShopProfile = findViewById(R.id.view_shop_Profile);


        backLayoutShop = findViewById(R.id.backLayoutShop);

        viewShopLink = findViewById(R.id.view_shop_link);
        viewShopName = findViewById(R.id.view_shop_name);
        viewShopVerified = findViewById(R.id.view_shop_verified);
        viewShopReviews = findViewById(R.id.view_shop_reviews);
        editShop = findViewById(R.id.editShop);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseFirestore.getInstance().collection("Shop")
                .document(firebaseUser.getUid())
                        .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            DocumentSnapshot snapshot = task.getResult();
                                            ShopClass shopName = snapshot.toObject(ShopClass.class);
                                            viewShopName.setText(shopName.getShopName());
                                            viewShopLink.setText("hiBreed.ph/"+shopName.getShopName());
                                            viewShopLink.setTextSize(11);
                                            viewShopReviews.setTextSize(11);
                                            if(firebaseUser.isEmailVerified()){
                                                viewShopVerified.setText("Verified");
                                            }
                                            if(shopName.getProfImage().equals("") ||shopName.getProfImage() == null)
                                            Glide.with( shop_view_profile.this)
                                                    .load(R.drawable.noimage)
                                                    .into(imageShopProfile);
                                            else
                                                Glide.with( shop_view_profile.this)
                                                        .load(shopName.getProfImage())
                                                        .into(imageShopProfile);

                                            if(shopName.getBackgroundImage().equals("") || shopName.getBackgroundImage() == null) {
                                                Glide.with( shop_view_profile.this)
                                                        .load(R.drawable.nobackground)
                                                        .into(imageShopBackground);
                                            }
                                            else {
                                                Glide.with( shop_view_profile.this)
                                                        .load(shopName.getBackgroundImage())
                                                        .into(imageShopBackground);
                                            }
                                        }
                                    }
                                });


        editShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(shop_view_profile.this,shop_edit_profile.class));
            }
        });

    backLayoutShop.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            shop_view_profile.this.onBackPressed();
        }
    });


        //RecyclerView
        petForSale_view = findViewById(R.id.petForSale_view);
        adapter = new petDisplayForSaleAdapter(this);
        petForSale_view.setLayoutManager(new GridLayoutManager(this,2));
        petForSale_view.setAdapter(adapter);
        getPetForSale();




    }


    private void getPetForSale() {
        FirebaseFirestore.getInstance().collection("Pet")
                .whereEqualTo("pet_breeder",firebaseUser.getUid()).whereEqualTo("displayFor","forSale")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            if(list!=null){
                              for(DocumentSnapshot s: list){
                                  PetSaleClass pet = s.toObject(PetSaleClass.class);
                                  adapter.addPetDisplay(pet);
                              }
                            }
                        }
                    }
                });



    }

}