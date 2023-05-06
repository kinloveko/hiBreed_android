package com.example.hi_breed.service;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.hi_breed.R;
import com.example.hi_breed.adapter.review_order_adapter;
import com.example.hi_breed.adapter.shooterAdapter.m_serviceAdapter;
import com.example.hi_breed.classesFile.BaseActivity;
import com.example.hi_breed.classesFile.likes_class;
import com.example.hi_breed.classesFile.rating_class;
import com.example.hi_breed.classesFile.service_class;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.ramijemli.percentagechartview.PercentageChartView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class service_display_details extends BaseActivity {


    ImageView ratingBar;
    review_order_adapter adapter;
    RecyclerView reviewsService;
    ImageView heart_like, share_to_messenger;
    LinearLayout backLayout, recommended_layout;
    ImageSlider imageSliderDetails;
    TextView details_shooter_name;
    TextView details_service_type;
    TextView details_shooter_price;
    TextView details_pet_description;
    TextView expand;
    TextView details_service_address;
    TextView details_service_availability;
    TextView details_service_schedule;
    TextView seeMore;
    Button details_button_hireNow;
    String id, shooter_id;
    String sched = "";
    PercentageChartView view_id;
    String from;
    String matchID;
    RecyclerView recommended_recycler;
    private m_serviceAdapter adapterShoot;

    @SuppressLint({"ObsoleteSdkInt", "SetTextI18n"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_display_details);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.setStatusBarColor(Color.parseColor("#e28743"));
        }
        ratingBar = findViewById(R.id.ratingBar);
        reviewsService = findViewById(R.id.reviewsShop);
        reviewsService.setLayoutManager(new GridLayoutManager(this, 1));
        adapter = new review_order_adapter(this);
        view_id = findViewById(R.id.view_id);
        expand = findViewById(R.id.expand);
        heart_like = findViewById(R.id.heart_like);
        backLayout = findViewById(R.id.backLayout);
        imageSliderDetails = findViewById(R.id.imageSliderDetails);
        details_shooter_name = findViewById(R.id.details_shooter_name);
        details_service_type = findViewById(R.id.details_service_type);
        details_shooter_price = findViewById(R.id.details_shooter_price);
        details_pet_description = findViewById(R.id.details_pet_description);
        details_service_address = findViewById(R.id.details_service_address);
        details_service_availability = findViewById(R.id.details_service_availability);
        details_service_schedule = findViewById(R.id.details_service_schedule);
        details_button_hireNow = findViewById(R.id.details_button_hireNow);

        expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expand.getText().toString().equals("View all reviews . . .")) {
                    expand.setText("Hide reviews . . .");
                    adapter.setExpanded(true);
                } else {
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
                if (seeMore.getText().toString().equals("See more . . .")) {
                    details_pet_description.setMaxLines(Integer.MAX_VALUE);
                    details_pet_description.setEllipsize(null);
                    seeMore.setText("Show less . . .");
                } else {
                    details_pet_description.setMaxLines(3);
                    details_pet_description.setEllipsize(TextUtils.TruncateAt.END);
                    seeMore.setText("See more . . .");
                }
            }
        });
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service_display_details.this.onBackPressed();
                finish();
            }
        });


        recommended_layout = findViewById(R.id.recommended_layout);
        recommended_recycler = findViewById(R.id.recommended_recycler);
        recommended_recycler.setLayoutManager(new GridLayoutManager(this, 1));
        adapterShoot = new m_serviceAdapter(this);
        recommended_recycler.setAdapter(adapterShoot);


        Intent intent = getIntent();
        service_class service = (service_class) intent.getSerializableExtra("mode");
        from = intent.getStringExtra("from");
        matchID = intent.getStringExtra("matchID");
        if (service != null) {
            //shooter image
            id = service.getId();
            getShooterRecommendation(id,service.getServiceType());
            shooter_id = service.getShooter_id();
            if (!service.getServiceType().equals("Veterinarian Service")) {
                view_id.setVisibility(View.VISIBLE);
            }
            getReviews(service.getShooter_id());

            FirebaseFirestore.getInstance().collection("Likes")
                    .whereEqualTo("likedBy", FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .whereEqualTo("product_id", service.getId())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
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

            //slide
            if (service.getPhotos() != null) {
                ArrayList<SlideModel> slideModels = new ArrayList<>();
                for (int i = 0; i < service.getPhotos().size(); i++) {
                    slideModels.add(new SlideModel(service.getPhotos().get(i), ScaleTypes.CENTER_INSIDE));
                }
                imageSliderDetails.setImageList(slideModels, ScaleTypes.CENTER_INSIDE);
            }
            details_service_address.setText(service.getAddress());

            FirebaseFirestore.getInstance().collection("Shop").document(service.getShooter_id())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot snapshot = task.getResult();

                                details_service_type.setText(snapshot.getString("shopName"));
                            }
                        }
                    });

            details_shooter_name.setText(service.getServiceType());
            details_shooter_price.setText(service.getService_fee());

            details_service_availability.setText("From " + service.getAvailability().get(0) + "-" + service.getAvailability().get(1));

            details_pet_description.setText(service.getService_description());
            if (service.getSchedule().size() != 0) {

                for (int i = 0; i < service.getSchedule().size(); i++) {

                    sched += service.getSchedule().get(i) + " ";
                }
                details_service_schedule.setText(sched);
            }

        } else {
            Toast.makeText(this, "no data has been fetch", Toast.LENGTH_SHORT).show();
            this.finish();
        }

        //checking the status of the user if it is verified or not
        FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot value = task.getResult();
                            if (value.exists()) {
                                if (value.getString("status").equals("pending")) {
                                    details_button_hireNow.setEnabled(false);
                                    heart_like.setEnabled(false);
                                } else if (value.getString("status").equals("verified")) {
                                    details_button_hireNow.setEnabled(true);
                                    heart_like.setEnabled(true);
                                }
                            }

                        }
                    }
                });
        //adding heart like to the person
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
        details_button_hireNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection("User")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .collection("security")
                        .document("security_doc")
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot s = task.getResult();

                                    if (s.getString("contactNumber") == null || s.getString("contactNumber").equals("")) {
                                        Toast.makeText(service_display_details.this, "Please setup your phone number first", Toast.LENGTH_SHORT).show();

                                    } else {
                                        if (from != null) {
                                            Intent intent = new Intent(service_display_details.this, service_set_appointment.class);
                                            intent.putExtra("model", (Serializable) service);
                                            intent.putExtra("from", from);
                                            intent.putExtra("matchID", matchID);
                                            startActivity(intent);
                                        } else {
                                            Intent intent = new Intent(service_display_details.this, service_set_appointment.class);
                                            intent.putExtra("model", (Serializable) service);
                                            startActivity(intent);
                                        }

                                    }

                                }
                            }
                        });
            }
        });
    }

    private void getShooterRecommendation(String id,String category) {

        FirebaseFirestore.getInstance().collection("Services")
                .whereEqualTo("displayFor", "Service")
                .whereEqualTo("serviceType", category)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        adapterShoot.clearList();
                        List<DocumentSnapshot> dlist = value.getDocuments();
                        if(value!=null){
                            if(dlist.size()!=0){
                                for (DocumentSnapshot ds : dlist) {
                                    if (!ds.getString("id").equals(id)) {
                                        service_class pet = ds.toObject(service_class.class);
                                        adapterShoot.addServiceDisplay(pet);
                                        recommended_layout.setVisibility(View.VISIBLE);
                                        recommended_recycler.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        }
                        else{
                            recommended_layout.setVisibility(View.GONE);
                            recommended_recycler.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void getReviews(String shooter_id) {
        FirebaseFirestore.getInstance().collection("Reviews")
                .whereEqualTo("seller_id", shooter_id)
                .whereEqualTo("rateFor", "Service")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint({"DefaultLocale", "SetTextI18n"})
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }
                        if (value != null && !value.isEmpty()) {
                            expand.setVisibility(View.VISIBLE);
                            float totalRating = 0;
                            int numRatings = 0;
                            String type = "";
                            adapter.clearList();
                            for (DocumentSnapshot s : value) {
                                if (s.getDouble("rating") != null && !Double.isNaN(s.getDouble("rating"))) {
                                    totalRating += s.getDouble("rating").floatValue();
                                    numRatings++;

                                    rating_class rate = s.toObject(rating_class.class);
                                    type = rate.getType();
                                    adapter.addServiceDisplay(rate);
                                }
                            }
                            if (numRatings > 0) {

                                int totalReviews = value.size();
                                float averageRating = totalRating / numRatings;
                                TextView textsRate = findViewById(R.id.textsRate);
                                TextView numberOfReviewsTextView = findViewById(R.id.numberOfReviewsTextView);
                                TextView ratingValue = findViewById(R.id.ratingValue);
                                ratingValue.setText(String.format("%.1f /5 ", averageRating));
                                float percent = Math.min(averageRating / 5 * 100, 100);
                                if (type.equals("Veterinarian Service")) {
                                    numberOfReviewsTextView.setText("(" + totalReviews + " Review" + ")");
                                } else {
                                    textsRate.setVisibility(View.VISIBLE);
                                    view_id.setProgress(percent, true);
                                    numberOfReviewsTextView.setText("(" + totalReviews + " Review" + ")");
                                }
                                reviewsService.setAdapter(adapter);
                            } else {
                                TextView ratingTextView = findViewById(R.id.ratingTextView);
                                TextView numberOfReviewsTextView = findViewById(R.id.numberOfReviewsTextView);
                                ratingTextView.setText("No ratings yet");
                                numberOfReviewsTextView.setText("");
                                reviewsService.setAdapter(adapter);
                            }
                        } else {
                            TextView ratingTextView = findViewById(R.id.ratingTextView);
                            TextView numberOfReviewsTextView = findViewById(R.id.numberOfReviewsTextView);
                            ratingTextView.setText("No ratings yet");
                            numberOfReviewsTextView.setText("");
                            reviewsService.setAdapter(adapter);
                        }
                    }
                });
    }

    private void removeLike() {
        FirebaseFirestore.getInstance().collection("Likes").whereEqualTo("likedBy", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("product_id", id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                FirebaseFirestore.getInstance().collection("Likes")
                                        .document(document.getId())
                                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    FirebaseFirestore.getInstance().collection("User")
                                                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .collection("Likes")
                                                            .document(document.getId())
                                                            .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(service_display_details.this, "Successfully removed from your liked pets", Toast.LENGTH_SHORT).show();

                                                                        heart_like.setEnabled(true);
                                                                    }
                                                                }
                                                            });
                                                }
                                            }
                                        });
                            }
                        } else {
                            Log.e("ERROR", "Error getting documents", task.getException());
                        }
                    }
                });

    }

    private void saveLike() {
        likes_class like = new likes_class("", FirebaseAuth.getInstance().getCurrentUser().getUid(), id, shooter_id, "Service", Timestamp.now());

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
                                .update("id", documentReference.getId(), "timestamp", FieldValue.serverTimestamp())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        FirebaseFirestore.getInstance().collection("Likes")
                                                .document(documentReference.getId())
                                                .set(like).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            FirebaseFirestore.getInstance().collection("Likes")
                                                                    .document(documentReference.getId())
                                                                    .update("id", documentReference.getId(), "timestamp", FieldValue.serverTimestamp())
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                Toast.makeText(service_display_details.this, "Successfully added to your likes", Toast.LENGTH_SHORT).show();
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