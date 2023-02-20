package com.example.hi_breed.shop;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.hi_breed.Pet.pet_add_for_dating;
import com.example.hi_breed.Pet.pet_add_for_selling;
import com.example.hi_breed.Pet.pet_my_pets_panel;
import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.BaseActivity;
import com.example.hi_breed.product.product_add_activity;
import com.example.hi_breed.product.product_my_product;
import com.example.hi_breed.shooter.shooter_panel;
import com.example.hi_breed.userFile.dashboard.user_dashboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class user_breeder_shop_panel extends BaseActivity {
    FirebaseFirestore fireStore;
    DocumentReference databaseReference;
    FirebaseUser firebaseUser;
    TextView viewShop;
    ArrayList<String> role = new ArrayList<>();
    TextView breederName, breederLabel;
    CircleImageView imageView;
    CardView createPetProfile,myPets,sellPetCardView8,serviceCardView8,myProducts;
    CircleImageView cardView2;
    TextView textView15,textViewSellPetName,textViewLabel;

    LinearLayout owner,activityStatus;
    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_breeder_shop_panel);

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
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        fireStore = FirebaseFirestore.getInstance();
        databaseReference = fireStore.collection("User").document(firebaseUser.getUid());

        textViewSellPetName = findViewById(R.id.textView22);
        textViewLabel = findViewById(R.id.textView19);

        myProducts = findViewById(R.id.myProducts);
        activityStatus = findViewById(R.id.activityStatus);
        owner = findViewById(R.id.owner);
        //Create Pet Profile
        createPetProfile = findViewById(R.id.myPetsCardView8);
        //Show all pet
        myPets = findViewById(R.id.myPetsCardView9);
        //Sell pet
        sellPetCardView8 = findViewById(R.id.sellPetCardView8);
        //Service
        //Cardview
        cardView2 = findViewById(R.id.cardView2);
        serviceCardView8 = findViewById(R.id.serviceCardView8);
        viewShop = findViewById(R.id.viewShopID);
        breederName =findViewById(R.id.shop_userNameTxtView);
        breederLabel =findViewById(R.id.shop_userLabelTextView);
        imageView = findViewById(R.id.shop_profileImage);
        textView15 = findViewById(R.id.ey);

        databaseReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){

                        String ownerName =documentSnapshot.getString("firstName")+" "+documentSnapshot.getString("middleName")+" "+ documentSnapshot.getString("lastName");
                        breederName.setText(ownerName);
                        breederLabel.setText("hiBreed.ph/" + documentSnapshot.getString("lastName"));
                        if (documentSnapshot.getString("image") == "") {
                            Uri image = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.noimage);
                            Glide.with( user_breeder_shop_panel.this)
                                    .load(R.drawable.noimage)
                                    .into(imageView);
                        }
                        else {
                            Glide.with( user_breeder_shop_panel.this)
                                    .load(documentSnapshot.getString("image"))
                                    .into(imageView);
                        }

                }
            }
        });
            databaseReference = fireStore.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            databaseReference
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                if (task.isSuccessful()) {
                                    List<String> arrayList = (List<String>) task.getResult().get("role");
                                    if (arrayList != null) {
                                        role.addAll(arrayList);

                                        if(role.contains("Pet Owner") && role.contains("Pet Breeder") && role.contains("Pet Shooter")){
                                            viewShop.setVisibility(View.VISIBLE);
                                            textView15.setText("MY SHOP");
                                            owner.setVisibility(View.VISIBLE);
                                            activityStatus.setVisibility(View.VISIBLE);
                                        }else
                                        if(role.contains("Pet Owner") && role.contains("Pet Breeder")){
                                            viewShop.setVisibility(View.VISIBLE);
                                            textView15.setText("MY SHOP");
                                            owner.setVisibility(View.VISIBLE);
                                            activityStatus.setVisibility(View.VISIBLE);
                                        }else
                                        if(role.contains("Pet Shooter") && role.contains("Pet Owner")){
                                            viewShop.setVisibility(View.GONE);
                                            textView15.setText("MY SHOP");
                                            owner.setVisibility(View.VISIBLE);
                                            activityStatus.setVisibility(View.GONE);
                                        }else
                                        if(role.contains("Pet Shooter")){
                                            viewShop.setVisibility(View.VISIBLE);
                                            textView15.setText("MY SHOP");
                                            owner.setVisibility(View.VISIBLE);
                                            activityStatus.setVisibility(View.GONE);
                                        }else
                                        if(role.contains("Pet Owner")){
                                            viewShop.setVisibility(View.GONE);
                                            textView15.setText("MY PET");
                                            owner.setVisibility(View.VISIBLE);
                                            activityStatus.setVisibility(View.GONE);
                                        }else
                                        if(role.contains("Veterinarian")){
                                            viewShop.setVisibility(View.VISIBLE);
                                            textView15.setText("MY CLINIC");
                                            owner.setVisibility(View.VISIBLE);
                                            activityStatus.setVisibility(View.VISIBLE);
                                            myProducts.setVisibility(View.VISIBLE);
                                            textViewLabel.setText("Products and Services");
                                            textViewSellPetName.setText("Sell products");
                                        }

                                    }
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(user_breeder_shop_panel.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

        myPets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(role.contains("Pet Owner") && role.contains("Pet Breeder") && role.contains("Pet Shooter")){
                    startActivity(new Intent(user_breeder_shop_panel.this, pet_my_pets_panel.class));
                    return;

                }
                if(role.contains("Pet Owner") && role.contains("Pet Breeder")){
                    startActivity(new Intent(user_breeder_shop_panel.this, pet_my_pets_panel.class));
                    return;
                }

                if(role.contains("Pet Shooter") && role.contains("Pet Owner")){
                    startActivity(new Intent(user_breeder_shop_panel.this, pet_my_pets_panel.class));
                    return;
                }

                if(role.contains("Pet Shooter")){
                    Toast.makeText(user_breeder_shop_panel.this, "You must be an Owner or Breeder or both", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(role.contains("Pet Owner")){
                    startActivity(new Intent(user_breeder_shop_panel.this, pet_my_pets_panel.class));
                    return;
                }
                if(role.contains("Veterinarian")){
                    startActivity(new Intent(user_breeder_shop_panel.this, pet_my_pets_panel.class));
                    return;
                }
            }
        });

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(user_breeder_shop_panel.this, user_dashboard.class));
                finish();
            }
        });
        sellPetCardView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(role.contains("Pet Owner") && role.contains("Pet Breeder") && role.contains("Pet Shooter")){
                    startActivity(new Intent(user_breeder_shop_panel.this, pet_add_for_selling.class));
                    return;

                }
                if(role.contains("Pet Owner") && role.contains("Pet Breeder")){
                    startActivity(new Intent(user_breeder_shop_panel.this, pet_add_for_selling.class));
                    return;
                }

                if(role.contains("Pet Shooter") && role.contains("Pet Owner")){
                    Toast.makeText(user_breeder_shop_panel.this, "Only a breeder can sell a pet", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(role.contains("Pet Shooter")){
                    Toast.makeText(user_breeder_shop_panel.this, "Only a breeder can sell a pet", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(role.contains("Pet Owner")){
                    Toast.makeText(user_breeder_shop_panel.this, "Only a breeder can sell a pet", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(role.contains("Veterinarian")){
                    startActivity(new Intent(user_breeder_shop_panel.this, product_add_activity.class));
                    return;
                }
            }
        });
        serviceCardView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(role.contains("Pet Owner") && role.contains("Pet Breeder") && role.contains("Pet Shooter")){
                    startActivity(new Intent(user_breeder_shop_panel.this, shooter_panel.class));
                    return;

                }
                if(role.contains("Pet Owner") && role.contains("Pet Breeder")){
                    Toast.makeText(user_breeder_shop_panel.this, "Only a dog shooter can access", Toast.LENGTH_SHORT).show();

                    return;
                }

                if(role.contains("Pet Shooter") && role.contains("Pet Owner")){
                    startActivity(new Intent(user_breeder_shop_panel.this, shooter_panel.class));
                    return;
                }

                if(role.contains("Pet Shooter")){
                    startActivity(new Intent(user_breeder_shop_panel.this, shooter_panel.class));
                    return;
                }
                if(role.contains("Veterinarian")){
                    startActivity(new Intent(user_breeder_shop_panel.this, shooter_panel.class));
                    return;
                }
                if(role.contains("Pet Owner")){
                    Toast.makeText(user_breeder_shop_panel.this, "Only a dog shooter can access", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        viewShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(user_breeder_shop_panel.this,shop_view_profile.class));
            }
        });
        createPetProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(role.contains("Pet Owner") && role.contains("Pet Breeder") && role.contains("Pet Shooter")){
                    startActivity(new Intent(user_breeder_shop_panel.this, pet_add_for_dating.class));
                    return;
                }
                if(role.contains("Pet Owner") && role.contains("Pet Breeder")){
                    startActivity(new Intent(user_breeder_shop_panel.this, pet_add_for_dating.class));
                    return;
                }

                if(role.contains("Pet Shooter") && role.contains("Pet Owner")){
                    startActivity(new Intent(user_breeder_shop_panel.this, pet_add_for_dating.class));
                    return;
                }
                if(role.contains("Pet Shooter")){
                    Toast.makeText(user_breeder_shop_panel.this, "You must be an Owner to add a pet", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(role.contains("Pet Owner")){
                    startActivity(new Intent(user_breeder_shop_panel.this, pet_add_for_dating.class));
                    return;
                }
                if(role.contains("Veterinarian")){
                    startActivity(new Intent(user_breeder_shop_panel.this, pet_add_for_dating.class));
                    return;
                }

            }
        });
        myProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(user_breeder_shop_panel.this, product_my_product.class));
            }
        });

    }
}