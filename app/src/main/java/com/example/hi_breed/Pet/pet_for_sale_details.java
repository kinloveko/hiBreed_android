package com.example.hi_breed.Pet;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.hi_breed.R;
import com.example.hi_breed.checkout.checkout_activity;
import com.example.hi_breed.classesFile.BaseActivity;
import com.example.hi_breed.classesFile.PetSaleClass;
import com.example.hi_breed.classesFile.add_to_cart_class;
import com.example.hi_breed.classesFile.likes_class;
import com.example.hi_breed.classesFile.priceFormat;
import com.example.hi_breed.shop.view_breeder_shop;
import com.example.hi_breed.userFile.cart.add_to_cart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;

public class pet_for_sale_details extends BaseActivity {

    ImageView heart_like;
    LinearLayout backLayout,cartLayout;
    ImageSlider imageSliderDetails;
    TextView details_pet_name
            ,details_pet_breed
            ,details_pet_price,
            shop_name_TxtView_details
                    ,kennel_details
            ,details_pet_description
            ,details_pet_gender
            ,details_pet_size_kilo
            ,seeMore
            ,details_pet_color
            ,details_pet_vaccines
            ,details_pet_papers
            ,address_details;
    ConstraintLayout details_shop_profile_Layout;
    CircleImageView details_shopProfile_details;

    Button details_button_addToCard
            ,details_button_buyNow;
    RelativeLayout details_moredetails_relative;

    String id,breeder;
    String vac="";

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pet_for_sale_details);

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
        cartLayout = findViewById(R.id.cartLayout);
        cartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(pet_for_sale_details.this, add_to_cart.class));
            }
        });

        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pet_for_sale_details.this.onBackPressed();
                finish();
            }
        });


        heart_like = findViewById(R.id.heart_like);
        seeMore = findViewById(R.id.seeMore);

        seeMore.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(seeMore.getText().toString().equals("See more . . .")) {
                    details_pet_description.setMaxLines(Integer.MAX_VALUE);
                    details_pet_description.setEllipsize(null);
                    seeMore.setText("Show less . . .");
                }
                else{
                    details_pet_description.setMaxLines(3);
                    details_pet_description.setEllipsize(TextUtils.TruncateAt.END);
                    seeMore.setText("See more . . .");
                }
            }
        });
        details_moredetails_relative = findViewById(R.id.details_moredetails_relative);
        //TextView

        //TextView
        details_pet_name =findViewById(R.id.details_pet_name);
        details_pet_breed = findViewById(R.id.details_pet_breed);
        details_pet_price = findViewById(R.id.details_pet_price);
        shop_name_TxtView_details = findViewById(R.id. shop_name_TxtView_details);
        kennel_details = findViewById(R.id.kennel_details);
        details_pet_description = findViewById(R.id.details_pet_description);
        details_pet_gender = findViewById(R.id.details_pet_gender);
        details_pet_size_kilo = findViewById(R.id.details_pet_size_kilo);
        details_pet_color = findViewById(R.id.details_pet_color);
        details_pet_vaccines = findViewById(R.id.details_pet_vaccines);
        details_pet_papers = findViewById(R.id.details_pet_papers);
        address_details = findViewById(R.id.address_details);
        //Constraint Layout
        details_shop_profile_Layout = findViewById(R.id.viewShopLayout);

        //Button
        details_button_addToCard = findViewById(R.id.details_button_addToCard);
        details_button_buyNow = findViewById(R.id.details_button_buyNow);

        //CircleView
        details_shopProfile_details = findViewById(R.id.details_shopProfile_details);

        //imageSliderDetails
        imageSliderDetails = findViewById(R.id.imageSliderDetails);

        //Getting the passed value to the data adapter when the user click an item to recycler view
        Intent intent = getIntent();
        PetSaleClass pet = (PetSaleClass) intent.getSerializableExtra("mode");
        if(pet!=null){

            id = pet.getId();
            breeder = pet.getPet_breeder();


       
            
            details_button_buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    FirebaseFirestore.getInstance().collection("User")
                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .collection("security")
                            .document("security_doc")
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if(documentSnapshot.getString("contactNumber") != null &&
                                            !documentSnapshot.getString("contactNumber").equals("")){
                                        add_to_cart_class buy = new add_to_cart_class("1",pet.getId(),pet.getDisplayFor(),pet.getPet_price(),FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                                pet.getPet_breeder(),"pet",Timestamp.now());
                                        Intent i = new Intent(pet_for_sale_details.this, checkout_activity.class);
                                        List<add_to_cart_class> add = new ArrayList<>();
                                        add.add(buy);
                                        i.putExtra("mode",(Serializable) add);
                                        startActivity(i);
                                    }
                                    else{
                                        Toast.makeText(pet_for_sale_details.this, "Setup your phone number first", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });

            FirebaseFirestore.getInstance().collection("Likes")
                    .whereEqualTo("likedBy",FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .whereEqualTo("product_id",id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                if (task.getResult().size() > 0) {
                                    // The current user has already liked the product
                                    heart_like.setImageResource(R.drawable.icon_clicked_like);
                                    Log.d("SUCCESS", "User has already liked this product");
                                } else {
                                    // The current user has not yet liked the product
                                    heart_like.setImageResource(R.drawable.icon_heart_likes);
                                    Log.d("ERROR", "User has not yet liked this product");
                                }
                            } else {
                                heart_like.setImageResource(R.drawable.icon_heart_likes);
                                Log.e("ERROR", "Error getting likes", task.getException());
                            }
                          }
                    });

            if(pet.getPapers()!=null){
                details_pet_papers.setText("Available");
            }

            if(pet.getPhotos()!=null) {
                ArrayList<SlideModel> slideModels = new ArrayList<>();
                for (int i = 0; i < pet.getPhotos().size(); i++) {
                    slideModels.add(new SlideModel(pet.getPhotos().get(i), ScaleTypes.CENTER_INSIDE));
                }
                imageSliderDetails.setImageList(slideModels, ScaleTypes.CENTER_INSIDE);
            }

            details_pet_name.setText(pet.getPet_name());
            details_pet_breed.setText(pet.getPet_breed());
            priceFormat format = new priceFormat();

            details_pet_price.setText(format.priceFormatString(pet.getPet_price()));
            details_pet_description.setText(pet.getPet_description());

            //address
            FirebaseFirestore.getInstance().collection("User").document(pet.getPet_breeder())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                String address = task.getResult().getString("address");
                                address_details.setText(address);
                            }
                        }
                    });


            //for profile
            FirebaseFirestore.getInstance().collection("Shop").document(pet.getPet_breeder()).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        DocumentSnapshot snapshot = task.getResult();
                                        if(snapshot!=null){

                                            //shopName
                                            shop_name_TxtView_details.setText(snapshot.getString("shopName"));
                                            //for profile
                                            if(snapshot.getString("profImage") == ""){
                                                details_shopProfile_details.setImageResource(R.drawable.noimage);
                                            }
                                            else
                                            Picasso.get().load(snapshot.getString("profImage")).into(details_shopProfile_details);
                                        }
                                    }
                                }
                            });

            kennel_details.setText(pet.getKennel());
            details_pet_color.setText(pet.getPet_colorMarkings());
            details_pet_gender.setText(pet.getPet_gender());
            for(String str:pet.getPet_vaccine()){
                vac += str +"\n";
            }
            details_pet_vaccines.setText(vac);
            details_pet_size_kilo.setText(pet.getPetSize()+" "+pet.getPetKilo()+"kg");

        }

        details_button_addToCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart(pet);
            }
        });

        details_shop_profile_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewShop();
            }
        });


        heart_like.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                Object tag = heart_like.getTag();
                if (tag == null || !((Boolean) tag)) {
                    heart_like.setImageResource(R.drawable.icon_clicked_like);
                    heart_like.setTag(true);
                    saveLike();
                } else {
                    removeLike();
                    heart_like.setImageResource(R.drawable.icon_heart_likes);
                    heart_like.setTag(false);
                }
            }
        });
    }

    private void removeLike() {

        FirebaseFirestore.getInstance().collection("Likes").whereEqualTo("likedBy",FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .whereEqualTo("product_id",id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                    FirebaseFirestore.getInstance().collection("Likes")
                                            .document(document.getId())
                                            .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        FirebaseFirestore.getInstance().collection("User")
                                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                .collection("Likes")
                                                                .document(document.getId())
                                                                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if(task.isSuccessful()){
                                                                            Toast.makeText(pet_for_sale_details.this, "Successfully removed from your liked pets", Toast.LENGTH_SHORT).show();

                                                                            heart_like.setEnabled(true);
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                }
                                            });
                            }
                        }
                        else{
                            Log.e("ERROR", "Error getting documents", task.getException());
                        }
                    }
                });

    }

    private void saveLike() {
        likes_class like = new likes_class("",FirebaseAuth.getInstance().getCurrentUser().getUid(),id,breeder,"forSale", Timestamp.now());

        FirebaseFirestore.getInstance().collection("User")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("Likes")
                .add(like)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        heart_like.setEnabled(false);

                        FirebaseFirestore.getInstance().collection("User")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .collection("Likes").document(documentReference.getId())
                                .update("id",documentReference.getId(),"timestamp", FieldValue.serverTimestamp())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                          FirebaseFirestore.getInstance().collection("Likes")
                                                  .document(documentReference.getId())
                                                  .set(like).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                      @Override
                                                      public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                FirebaseFirestore.getInstance().collection("Likes")
                                                                        .document(documentReference.getId())
                                                                        .update("id",documentReference.getId(),"timestamp",FieldValue.serverTimestamp())
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if(task.isSuccessful()){
                                                                                    Toast.makeText(pet_for_sale_details.this, "Successfully added to your likes", Toast.LENGTH_SHORT).show();
                                                                                    heart_like.setEnabled(true);
                                                                                }
                                                                            }
                                                                        });
                                                            }
                                                      }
                                                  });
                                    }
                                });
                    }
                });
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    private void addToCart(PetSaleClass pet) {
        Map<String, Object> objectMap = new HashMap<>();

        objectMap.put("prod_id",pet.getId());
        objectMap.put("prod_seller",pet.getPet_breeder());
        objectMap.put("prod_category","forSale");
        objectMap.put("prod_price",pet.getPet_price());
        objectMap.put("id","");
        objectMap.put("type","pet");
        objectMap.put("addBy",FirebaseAuth.getInstance().getCurrentUser().getUid());
        objectMap.put("timestamp",Timestamp.now());

        AlertDialog.Builder builder2 = new AlertDialog.Builder(pet_for_sale_details.this);
        builder2.setCancelable(false);
        View view = View.inflate(pet_for_sale_details.this,R.layout.screen_custom_alert,null);
        //title
        TextView title = view.findViewById(R.id.screen_custom_alert_title);
        //loading text
        TextView loadingText = view.findViewById(R.id.screen_custom_alert_loadingText);
        loadingText.setVisibility(View.GONE);
        //gif
        GifImageView gif = view.findViewById(R.id.screen_custom_alert_gif);
        gif.setVisibility(View.GONE);
        //header image
        AppCompatImageView imageViewCompat = view.findViewById(R.id.appCompatImageView);
        imageViewCompat.setVisibility(View.VISIBLE);
        imageViewCompat.setImageDrawable(getDrawable(R.drawable.screen_alert_image_error_border));
        //message
        TextView message = view.findViewById(R.id.screen_custom_alert_message);
        title.setText("Are you sure?");
        message.setVisibility(View.VISIBLE);
        message.setText("Click okay if you want to add to cart this pet");
        LinearLayout buttonLayout = view.findViewById(R.id.screen_custom_alert_buttonLayout);
        buttonLayout.setVisibility(View.VISIBLE);
        MaterialButton cancel,okay;
        cancel = view.findViewById(R.id.screen_custom_dialog_btn_cancel);
        cancel.setVisibility(View.VISIBLE);

        okay = view.findViewById(R.id.screen_custom_alert_dialog_btn_done);
        okay.setText("Okay");
        builder2.setView(view);
        AlertDialog alert2 = builder2.create();
        alert2.show();
        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert2.dismiss();
            }
        });
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert2.dismiss();
                FirebaseFirestore.getInstance().collection("Cart")
                        .add(objectMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                                  FirebaseFirestore.getInstance().collection("Cart")
                                          .document(documentReference.getId())
                                          .update("id",documentReference.getId(),"timestamp",FieldValue.serverTimestamp())
                                          .addOnSuccessListener(new OnSuccessListener<Void>() {
                                              @Override
                                              public void onSuccess(Void unused) {
                                                  AlertDialog.Builder builder2 = new AlertDialog.Builder(pet_for_sale_details.this);
                                                  builder2.setCancelable(false);
                                                  View view = View.inflate(pet_for_sale_details.this,R.layout.screen_custom_alert,null);
                                                  //title
                                                  TextView title = view.findViewById(R.id.screen_custom_alert_title);
                                                  //loading text
                                                  TextView loadingText = view.findViewById(R.id.screen_custom_alert_loadingText);
                                                  loadingText.setVisibility(View.GONE);
                                                  //gif
                                                  GifImageView gif = view.findViewById(R.id.screen_custom_alert_gif);
                                                  gif.setVisibility(View.GONE);
                                                  //header image
                                                  AppCompatImageView imageViewCompat = view.findViewById(R.id.appCompatImageView);
                                                  imageViewCompat.setVisibility(View.VISIBLE);
                                                  imageViewCompat.setImageDrawable(getDrawable(R.drawable.dialog_cart_borders));
                                                  //message
                                                  TextView message = view.findViewById(R.id.screen_custom_alert_message);
                                                  title.setText("Add to cart");
                                                  message.setVisibility(View.VISIBLE);
                                                  message.setText("Successfully Added");
                                                  LinearLayout buttonLayout = view.findViewById(R.id.screen_custom_alert_buttonLayout);
                                                  buttonLayout.setVisibility(View.VISIBLE);
                                                  MaterialButton cancel,okay;
                                                  cancel = view.findViewById(R.id.screen_custom_dialog_btn_cancel);
                                                  cancel.setVisibility(View.GONE);
                                                  okay = view.findViewById(R.id.screen_custom_alert_dialog_btn_done);
                                                  okay.setText("Okay");
                                                  builder2.setView(view);
                                                  AlertDialog alert2 = builder2.create();
                                                  alert2.show();
                                                  alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                  okay.setOnClickListener(new View.OnClickListener() {
                                                      @Override
                                                      public void onClick(View v) {
                                                          alert2.dismiss();

                                                      }
                                                  });
                                              }
                                          });

                            }
                        });
            }
        });



    }

    private void viewShop() {
        Intent intent = new Intent(this, view_breeder_shop.class);
        intent.putExtra("breeder",breeder);
        startActivity(intent);
    }
}