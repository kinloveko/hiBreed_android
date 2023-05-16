package com.example.hi_breed.shop;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.hi_breed.Pet.pet_add_for_dating;
import com.example.hi_breed.Pet.pet_add_for_selling;
import com.example.hi_breed.Pet.pet_my_pets_panel;
import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.BaseActivity;
import com.example.hi_breed.loginAndRegistration.not_verified_activity;
import com.example.hi_breed.message.message_activity;
import com.example.hi_breed.order_breeder_side.order_breeder_side;
import com.example.hi_breed.product.product_add_activity;
import com.example.hi_breed.product.product_my_product;
import com.example.hi_breed.service.service_panel;
import com.example.hi_breed.userFile.dashboard.user_dashboard;
import com.example.hi_breed.userFile.profile.user_profile_account_setting;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class user_breeder_shop_panel extends BaseActivity {
    FirebaseFirestore fireStore;
    DocumentReference databaseReference;
    FirebaseUser firebaseUser;
    TextView viewShop;
    ArrayList<String> role = new ArrayList<>();
    TextView breederName, breederLabel,IDReview;
    CircleImageView imageView;
    CardView createPetProfile,myPets,cardView4,cardView5,pending,ongoing,reviews,sellPetCardView8,serviceCardView8,myProducts,sellProductView8;
    CircleImageView cardView2;
    TextView textView15,IDNumberPending,textViewSellPetName,IDNumberOngoing,textViewLabel,notVerified;
            Boolean userNotVerified = false;
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
        cardView4= findViewById(R.id.cardView4);
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(user_breeder_shop_panel.this, user_profile_account_setting.class));
            }
        });

        cardView5= findViewById(R.id.cardView5);
        cardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(user_breeder_shop_panel.this, message_activity.class));
            }
        });
        notVerified = findViewById(R.id.notVerified);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        fireStore = FirebaseFirestore.getInstance();
        databaseReference = fireStore.collection("User").document(firebaseUser.getUid());
        IDNumberPending = findViewById(R.id.IDNumberPending);
        textViewSellPetName = findViewById(R.id.textView22);
        textViewLabel = findViewById(R.id.textView19);
        IDNumberOngoing = findViewById(R.id.IDNumberOngoing);
        IDReview = findViewById(R.id.IDReview);
        myProducts = findViewById(R.id.myProducts);
        activityStatus = findViewById(R.id.activityStatus);
        notVerified.setSelected(true);
        owner = findViewById(R.id.owner);
        //Create Pet Profile
        createPetProfile = findViewById(R.id.myPetsCardView8);
        //Show all pet
        myPets = findViewById(R.id.myPetsCardView9);
        //Sell pet
        sellPetCardView8 = findViewById(R.id.sellPetCardView8);
        //Service
        //Cardview
        pending = findViewById(R.id.pending);
        ongoing = findViewById(R.id.ongoing);
        reviews = findViewById(R.id.reviews);


        sellProductView8 = findViewById(R.id.sellProductView8);
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
                                        if(task.getResult().getString("status").equals("pending")){
<<<<<<< HEAD
                                            notVerified.setVisibility(View.VISIBLE);
                                            if(role.contains("Pet Owner") && role.size() == 1) {
=======

                                            notVerified.setVisibility(View.VISIBLE);
                                            if(!(role.contains("Pet Shooter") && role.contains("Pet Breeder") && role.contains("Veterinarian")  ) ){
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                                                notVerified.setText("We are still reviewing all of your submitted documentation; you have not yet been verified.");
                                                createPetProfile.setEnabled(false);
                                                sellPetCardView8.setEnabled(false);
                                                sellProductView8.setEnabled(false);
                                                myPets.setEnabled(false);
                                                myProducts.setEnabled(false);
                                                serviceCardView8.setEnabled(false);
                                            }
                                            else {
<<<<<<< HEAD
                                                createPetProfile.setEnabled(false);
                                                sellPetCardView8.setEnabled(false);
                                                sellProductView8.setEnabled(false);
                                                myPets.setEnabled(false);
                                                myProducts.setEnabled(false);
                                                serviceCardView8.setEnabled(false);
                                                notVerified.setText("Your account is not verified. If you want to revalidate your account, click here!");
=======

>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                                                userNotVerified = true;
                                                notVerified.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        startActivity(new Intent(user_breeder_shop_panel.this, not_verified_activity.class));
                                                    }
                                                });
                                            }

                                        }
                                        if(role.contains("Pet Breeder") || role.contains("Pet Shooter") || role.contains("Veterinarian")){
                                            viewShop.setVisibility(View.VISIBLE);
                                            textView15.setText("MY SHOP");
                                                owner.setVisibility(View.VISIBLE);

                                            if(role.contains("Pet Breeder") || role.contains("Veterinarian")){
<<<<<<< HEAD
=======

>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                                                activityStatus.setVisibility(View.VISIBLE);
                                                FirebaseFirestore.getInstance().collection("Appointments")
                                                        .whereEqualTo("seller_id",FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .whereEqualTo("order_status","pending")
                                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                                if(error!=null){
                                                                    return;
                                                                }
                                                                if(value.size()!=0){
                                                                    IDNumberPending.setText(String.valueOf(value.size()));
                                                                }
                                                            }
                                                        });
                                                pending.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        startActivity(new Intent(user_breeder_shop_panel.this, order_breeder_side.class));
                                                    }
                                                });

                                                FirebaseFirestore.getInstance().collection("Appointments")
                                                        .whereEqualTo("seller_id",FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .whereEqualTo("order_status","accepted")
                                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                                if(error!=null){
                                                                    return;
                                                                }
                                                                if(value.size()!=0){
                                                                    IDNumberOngoing.setText(String.valueOf(value.size()));
                                                                }
                                                            }
                                                        });

                                                ongoing.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent i = new Intent(user_breeder_shop_panel.this, order_breeder_side.class);
                                                        i.putExtra("SELECTED_TAB","accepted");
                                                        startActivity(i);
                                                    }
                                                });

                                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                CollectionReference reviewsRef = db.collection("Reviews");
                                                Query query = reviewsRef.whereEqualTo("seller_id", FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .whereEqualTo("rateFor","Shop");
                                                query.addSnapshotListener((value, error) -> {
                                                    if (error != null) {
                                                        Log.d("MyApp", "Error getting reviews: ", error);
                                                        return;
                                                    }
                                                    if(value!=null){
                                                        List<DocumentSnapshot> reviews = value.getDocuments();
                                                        if(reviews.size()!=0){
                                                            IDReview.setText(String.valueOf(reviews.size()));
                                                        }
                                                    }
                                                    });
                                                    reviews.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent i = new Intent(user_breeder_shop_panel.this, order_breeder_side.class);
                                                            i.putExtra("SELECTED_TAB","reviews");
                                                            startActivity(i);
                                                        }
                                                    });

                                            }
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

        sellProductView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userNotVerified){
                    Toast.makeText(user_breeder_shop_panel.this, "Verify your account first", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(role.contains("Veterinarian")){
                        FirebaseFirestore.getInstance().collection("User")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .collection("security")
                                .document("security_doc")
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if(documentSnapshot.exists()){
                                            if(documentSnapshot.getString("contactNumber").equals("")){
                                                Toast.makeText(user_breeder_shop_panel.this, "Setup your phone number first", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                startActivity(new Intent(user_breeder_shop_panel.this, product_add_activity.class));
                                            }
                                        }
                                    }
                                });

                    }
                    else{
                        Toast.makeText(user_breeder_shop_panel.this, "Only a veterinarian can sell product", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        myPets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userNotVerified){
                    Toast.makeText(user_breeder_shop_panel.this, "Verify your account first", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (role.contains("Pet Owner") || role.contains("Veterinarian")) {
                        FirebaseFirestore.getInstance().collection("User")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .collection("security")
                                .document("security_doc")
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {
                                            if (documentSnapshot.getString("contactNumber").equals("")) {
                                                Toast.makeText(user_breeder_shop_panel.this, "Setup your phone number first", Toast.LENGTH_SHORT).show();
                                            } else {
                                                startActivity(new Intent(user_breeder_shop_panel.this, pet_my_pets_panel.class));
                                            }
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(user_breeder_shop_panel.this, "You must be a Pet owner", Toast.LENGTH_SHORT).show();
                    }
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
                if(userNotVerified){
                    Toast.makeText(user_breeder_shop_panel.this, "Verify your account first", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (role.contains("Pet Breeder")) {
                        FirebaseFirestore.getInstance().collection("User")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .collection("security")
                                .document("security_doc")
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {
                                            if (documentSnapshot.getString("contactNumber").equals("")) {
                                                Toast.makeText(user_breeder_shop_panel.this, "Setup your phone number first", Toast.LENGTH_SHORT).show();
                                            } else {
                                                startActivity(new Intent(user_breeder_shop_panel.this, pet_add_for_selling.class));
                                            }
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(user_breeder_shop_panel.this, "Only a breeder can sell a pet", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        serviceCardView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userNotVerified){
                    Toast.makeText(user_breeder_shop_panel.this, "Verify your account first", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (role.contains("Veterinarian") || role.contains("Pet Shooter")) {
                        FirebaseFirestore.getInstance().collection("User")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .collection("security")
                                .document("security_doc")
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {
                                            if (documentSnapshot.getString("contactNumber").equals("")) {
                                                Toast.makeText(user_breeder_shop_panel.this, "Setup your phone number first", Toast.LENGTH_SHORT).show();
                                            } else {
                                                startActivity(new Intent(user_breeder_shop_panel.this, service_panel.class));
                                            }
                                        }
                                    }
                                });

                    } else {
                        Toast.makeText(user_breeder_shop_panel.this, "Only a dog shooter or veterinarian can access", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        viewShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userNotVerified){
                    Toast.makeText(user_breeder_shop_panel.this, "Verify your account first", Toast.LENGTH_SHORT).show();
                }
                else {
                    startActivity(new Intent(user_breeder_shop_panel.this, shop_view_profile.class));
                }
            }
        });
        createPetProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(role.contains("Pet Owner") || role.contains("Veterinarian")){
                        FirebaseFirestore.getInstance().collection("User")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .collection("security")
                                .document("security_doc")
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if(documentSnapshot.exists()){
                                            if(documentSnapshot.getString("contactNumber").equals("")){
                                                Toast.makeText(user_breeder_shop_panel.this, "Setup your phone number first", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                startActivity(new Intent(user_breeder_shop_panel.this, pet_add_for_dating.class));
                                            }
                                        }
                                    }
                                });
                    }
                    else{
                        Toast.makeText(user_breeder_shop_panel.this, "You must be a Pet owner", Toast.LENGTH_SHORT).show();
                    }
            }
        });
        myProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userNotVerified){
                    Toast.makeText(user_breeder_shop_panel.this, "Verify your account first", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (role.contains("Veterinarian")) {
                        startActivity(new Intent(user_breeder_shop_panel.this, product_my_product.class));
                    } else {
                        Toast.makeText(user_breeder_shop_panel.this, "Only a veterinarian can sell product", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }
}