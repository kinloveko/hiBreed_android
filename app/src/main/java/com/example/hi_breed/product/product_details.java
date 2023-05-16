package com.example.hi_breed.product;

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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.hi_breed.R;
import com.example.hi_breed.adapter.product_adapter.productForSaleAdapter;
import com.example.hi_breed.adapter.review_order_adapter;
import com.example.hi_breed.checkout.checkout_activity;
import com.example.hi_breed.classesFile.add_to_cart_class;
import com.example.hi_breed.classesFile.likes_class;
import com.example.hi_breed.classesFile.product_class;
import com.example.hi_breed.classesFile.rating_class;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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

public class product_details extends AppCompatActivity {


    RatingBar ratingBar;
    review_order_adapter adapter;
    RecyclerView reviewsShop;

   ImageView heart_like;
   Button details_button_addToCard,details_button_buyNow;
   LinearLayout backLayout,recommended_layout,
            cartLayout;
   ImageSlider imageSliderDetails;
    TextView details_product_name,expLabel,treatLabel,viewShopID,seeMore,expand,
    details_product_category,
            details_product_price,
    details_product_description,
            details_brand,
    details_stocks,
            details_treatment,
    details_expiration,
            seller_name,
    address_details;
     CircleImageView seller_profile;
    String id,vet_id;
    private productForSaleAdapter adapterMedicine;
    private RecyclerView recommended_recycler;
    private productForSaleAdapter adapterProduct;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);
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
        recommended_layout  =  findViewById(R.id.recommended_layout);
        recommended_recycler =  findViewById(R.id.recommended_recycler);
        adapterProduct = new productForSaleAdapter(this);
        adapterMedicine = new productForSaleAdapter(this);
        recommended_recycler.setLayoutManager(new GridLayoutManager(this,2));



        details_button_buyNow = findViewById(R.id.details_button_buyNow);
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                product_details.this.onBackPressed();

            }
        });
        ratingBar = findViewById(R.id.ratingBar);
        reviewsShop = findViewById(R.id.reviewsShop);
        reviewsShop.setLayoutManager(new GridLayoutManager(this,1));
        adapter = new review_order_adapter(this);


        heart_like = findViewById(R.id.heart_like);
        cartLayout = findViewById(R.id.cartLayout);
        cartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(product_details.this, add_to_cart.class));

            }
        });

        details_button_addToCard = findViewById(R.id.details_button_addToCard);
        imageSliderDetails = findViewById(R.id.imageSliderDetails);
        details_product_name = findViewById(R.id.details_product_name);
        details_product_category = findViewById(R.id.details_product_category);
        details_product_price = findViewById(R.id.details_product_price);
        details_product_description = findViewById(R.id.details_product_description);
        details_brand = findViewById(R.id.details_brand);
        details_stocks = findViewById(R.id.details_stocks);
        details_treatment = findViewById(R.id.details_treatment);
        details_expiration = findViewById(R.id.details_expiration);
        seller_name = findViewById(R.id.seller_name);
        address_details = findViewById(R.id.address_details);
        seller_profile = findViewById(R.id.seller_profile);
        treatLabel = findViewById(R.id.treatLabel);
        expLabel = findViewById(R.id.expLabel);
        viewShopID = findViewById(R.id.viewShopID);
        expand = findViewById(R.id.expand);
        expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expand.getText().toString().equals("View all reviews . . .")){
                    expand.setText("Hide reviews . . .");
                    adapter.setExpanded(true);
                }
                else{
                    expand.setText("View all reviews . . .");
                    adapter.setExpanded(false);
                }
            }
        });
        seeMore = findViewById(R.id.seeMore);

        seeMore.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(seeMore.getText().toString().equals("See more . . .")) {
                        details_product_description.setMaxLines(Integer.MAX_VALUE);
                        details_product_description.setEllipsize(null);
                      seeMore.setText("Show less . . .");
                }
                else{
                    details_product_description.setMaxLines(3);
                    details_product_description.setEllipsize(TextUtils.TruncateAt.END);
                    seeMore.setText("See more . . .");
                }
            }
        });

        Intent intent = getIntent();
        product_class p = (product_class) intent.getSerializableExtra("mode");

        //recommendation
        if(p.getProd_category().equals("Medicine")){
            getMedicine(p.getId());
        }
        else {
            getProducts( p.getId());
        }

        id = p.getId();
        vet_id = p.getVet_id();
        String prod_category= p.getProd_category();

        FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot value = task.getResult();
                            if(value.exists()){
                                if(value.getString("status").equals("pending")){
                                    details_button_buyNow.setEnabled(false);
                                    details_button_addToCard.setEnabled(false);
                                    heart_like.setEnabled(false);
                                }
                                else if(value.getString("status").equals("verified")){
                                    details_button_addToCard.setEnabled(true);
                                    details_button_buyNow.setEnabled(true);
                                    heart_like.setEnabled(true);
                                }
                            }

                        }
                    }
                });


        if(p.getVet_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            details_button_addToCard.setVisibility(View.GONE);
            details_button_buyNow.setText("Edit your product");
            details_button_buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(product_details.this, product_my_product_edit.class);
                    intent.putExtra("mode", (Serializable) p);
                    startActivity(intent);
                }
            });
        }
        else{
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

                                        add_to_cart_class buy = new add_to_cart_class("1",p.getId(),p
                                                .getDisplayFor(),p.getProd_price(),FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                                p.getVet_id(),prod_category,Timestamp.now());
                                        Intent i = new Intent(product_details.this, checkout_activity.class);
                                        List<add_to_cart_class> add = new ArrayList<>();
                                        add.add(buy);
                                        i.putExtra("mode",(Serializable) add);
                                        startActivity(i);
                                    }
                                    else{
                                        Toast.makeText(product_details.this, "Setup your phone number first", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });

            details_button_addToCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addToCart(p);
                }
            });
        }
        getReviews(p.getVet_id());

        if(p.getProd_category().equals("Medicine")){
            treatLabel.setVisibility(View.VISIBLE);
            expLabel.setVisibility(View.VISIBLE);
            details_expiration.setVisibility(View.VISIBLE);
            details_treatment.setVisibility(View.VISIBLE);
            details_treatment.setText(p.getProd_treatment());
            details_expiration.setText(p.getProd_expiration());
        }
        details_brand.setText(p.getProd_brand());
        details_stocks.setText(p.getProd_stocks());
        details_product_price.setText(p.getProd_price());
        details_product_name.setText(p.getProd_name());
        details_product_description.setText(p.getProd_description());
        details_product_category.setText(p.getProd_category());

        FirebaseFirestore.getInstance().collection("Likes")
                .whereEqualTo("likedBy", FirebaseAuth.getInstance().getCurrentUser().getUid())
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
        FirebaseFirestore.getInstance().collection("Shop")
                .document(p.getVet_id())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot s = task.getResult();
                                Picasso.get().load(s.getString("profImage")).into(seller_profile);
                                  seller_name.setText(s.getString("shopName"));
                            }
                    }
                });
        FirebaseFirestore.getInstance().collection("User")
                .document(p.getVet_id())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        address_details.setText(documentSnapshot.getString("address"));
                    }
                });
        if(p.getPhotos()!=null) {
            ArrayList<SlideModel> slideModels = new ArrayList<>();
            for (int i = 0; i < p.getPhotos().size(); i++) {
                slideModels.add(new SlideModel(p.getPhotos().get(i), ScaleTypes.CENTER_INSIDE));
            }
            imageSliderDetails.setImageList(slideModels,  ScaleTypes.CENTER_INSIDE);
        }

        viewShopID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(product_details.this, view_breeder_shop.class);
                i.putExtra("breeder",p.getVet_id());
                startActivity(i);
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
                    saveLike(p);
                } else {
                    removeLike(p);
                    heart_like.setImageResource(R.drawable.icon_heart_likes);
                    heart_like.setTag(false);
                }
            }
        });

    }

    private void getProducts(String id) {

        FirebaseFirestore.getInstance().collection("Pet")
                .whereEqualTo("displayFor","forProducts")
                .whereEqualTo("prod_category","Dog Accessories")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null)
                        {
                            return;
                        }
                        if(value!=null){
                            adapterProduct.clearList();
                            List<DocumentSnapshot> list = value.getDocuments();
                            if(list.size()!=0) {
                                for (DocumentSnapshot s : list) {
                                     if(!s.getString("id").equals(id)) {
                                         product_class p = s.toObject(product_class.class);
                                         adapterProduct.addPetDisplay(p);
                                         recommended_layout.setVisibility(View.VISIBLE);
                                         recommended_recycler.setVisibility(View.VISIBLE);
                                     }
                                }
                                recommended_recycler.setAdapter(adapterProduct);
                            }
                        }
                        else{
                            recommended_layout.setVisibility(View.GONE);
                            recommended_recycler.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void getMedicine(String id) {
        FirebaseFirestore.getInstance().collection("Pet")
                .whereEqualTo("displayFor","forProducts")
                .whereEqualTo("prod_category","Medicine")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null)
                        {
                            return;
                        }
                        if(value!=null){
                            adapterMedicine.clearList();
                            List<DocumentSnapshot> list = value.getDocuments();
                            if(list.size()!=0) {
                                for (DocumentSnapshot s : list) {
                                    if (!s.getString("id").equals(id)) {
                                        product_class p = s.toObject(product_class.class);
                                        adapterMedicine.addPetDisplay(p);
                                        recommended_layout.setVisibility(View.VISIBLE);
                                        recommended_recycler.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                            recommended_recycler.setAdapter(adapterMedicine);
                        }
                        else{
                            recommended_layout.setVisibility(View.GONE);
                            recommended_recycler.setVisibility(View.GONE);
                        }
                    }
                });

    }


    private void getReviews(String pet_breeder) {
        FirebaseFirestore.getInstance().collection("Reviews")
                .whereEqualTo("seller_id",pet_breeder)
                .whereEqualTo("rateFor","Shop")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint({"DefaultLocale", "SetTextI18n"})
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            return;
                        }
                        if (value != null && !value.isEmpty()) {
                            expand.setVisibility(View.VISIBLE);
                            float totalRating = 0;
                            int numRatings = 0;
                            adapter.clearList();
                            for (DocumentSnapshot s : value) {
                                if (s.getDouble("rating") != null && !Double.isNaN(s.getDouble("rating"))) {
                                    totalRating += s.getDouble("rating").floatValue();
                                    numRatings++;
                                    rating_class rate = s.toObject(rating_class.class);
                                    adapter.addServiceDisplay(rate);
                                }
                            }
                            if (numRatings > 0) {
                                int totalReviews = value.size();
                                float averageRating = totalRating / numRatings;
                                TextView ratingTextView = findViewById(R.id.ratingTextView);
                                TextView numberOfReviewsTextView = findViewById(R.id.numberOfReviewsTextView);
                                ratingTextView.setText(String.format("%.1f /5 ", averageRating));
                                numberOfReviewsTextView.setText("(" + totalReviews + " Review" + ")");
                                ratingBar.setRating(averageRating);
                                reviewsShop.setAdapter(adapter);
                            } else {
                                TextView ratingTextView = findViewById(R.id.ratingTextView);
                                TextView numberOfReviewsTextView = findViewById(R.id.numberOfReviewsTextView);
                                ratingTextView.setText("No ratings yet");
                                numberOfReviewsTextView.setText("");
                                ratingBar.setRating(0);
                                reviewsShop.setAdapter(adapter);
                            }
                        } else {
                            TextView ratingTextView = findViewById(R.id.ratingTextView);
                            TextView numberOfReviewsTextView = findViewById(R.id.numberOfReviewsTextView);
                            ratingTextView.setText("No ratings yet");
                            numberOfReviewsTextView.setText("");
                            ratingBar.setRating(0);
                            reviewsShop.setAdapter(adapter);
                        }
                    }
                });
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    private void addToCart(product_class p) {
        Map<String, Object> objectMap = new HashMap<>();

        objectMap.put("prod_id",p.getId());
        objectMap.put("prod_seller",p.getVet_id());
        objectMap.put("prod_category",details_product_category.getText().toString());
        objectMap.put("prod_price",p.getProd_price());
        objectMap.put("id","");
        objectMap.put("type",p.getProd_category());
        objectMap.put("addBy",FirebaseAuth.getInstance().getCurrentUser().getUid());
        objectMap.put("timestamp",Timestamp.now());

        AlertDialog.Builder builder2 = new AlertDialog.Builder(product_details.this);
        builder2.setCancelable(false);
        View view = View.inflate(product_details.this,R.layout.screen_custom_alert,null);
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
                                                AlertDialog.Builder builder2 = new AlertDialog.Builder(product_details.this);
                                                builder2.setCancelable(false);
                                                View view = View.inflate(product_details.this,R.layout.screen_custom_alert,null);
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

    private void removeLike(product_class p) {

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
                                                                        Toast.makeText(product_details.this, "Successfully removed from your liked pets", Toast.LENGTH_SHORT).show();

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

    private void saveLike(product_class p) {
        likes_class like = new likes_class("",FirebaseAuth.getInstance().getCurrentUser().getUid(),id,vet_id,p.getProd_category(), Timestamp.now());

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
                                                                                Toast.makeText(product_details.this, "Successfully added to your likes", Toast.LENGTH_SHORT).show();
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

}