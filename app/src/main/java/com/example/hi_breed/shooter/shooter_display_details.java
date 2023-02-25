package com.example.hi_breed.shooter;

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

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.BaseActivity;
import com.example.hi_breed.classesFile.likes_class;
import com.example.hi_breed.classesFile.service_class;
import com.example.hi_breed.set_appointment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;

public class shooter_display_details extends BaseActivity {
    ImageView heart_like,share_to_messenger;
   LinearLayout backLayout;
   ImageSlider imageSliderDetails;
   TextView details_shooter_name;
    TextView         details_service_type;
    TextView details_shooter_price;
    TextView details_pet_description;
    TextView details_service_address;
    TextView        details_service_availability;
    TextView details_service_schedule;
    TextView seeMore;
     Button details_button_hireNow;
    String id,shooter_id;
    String sched="";


    @SuppressLint({"ObsoleteSdkInt", "SetTextI18n"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shooter_display_details);

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
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shooter_display_details.this.onBackPressed();
                finish();
            }
        });

        Intent intent = getIntent();
        service_class service = (service_class) intent.getSerializableExtra("mode");
        if(service!=null){
        //shooter image
            id = service.getId();
            shooter_id = service.getShooter_id();
            FirebaseFirestore.getInstance().collection("Likes")
                    .whereEqualTo("likedBy", FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .whereEqualTo("product_id",service.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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



            //slide
            if(service.getPhotos() !=null) {
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
                            if(task.isSuccessful()){
                                DocumentSnapshot snapshot = task.getResult();

                                details_service_type.setText(snapshot.getString("shopName"));
                            }
                        }
                    });

            details_shooter_name.setText(service.getServiceType());
            details_shooter_price.setText(service.getService_fee());
            details_service_availability.setText("From "+service.getAvailability().get(0)+"-"+service.getAvailability().get(1));
            details_pet_description.setText(service.getService_description());
            if(service.getSchedule().size() !=0){

                for(int i = 0; i < service.getSchedule().size();i++){

                    sched += service.getSchedule().get(i)+" ";
                }
                details_service_schedule.setText(sched);
            }

        }
        else{
            Toast.makeText(this, "no data has been fetch", Toast.LENGTH_SHORT).show();
            this.finish();
        }
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
                Intent intent = new Intent(shooter_display_details.this, set_appointment.class);
                intent.putExtra("model", (Serializable) service);
                   startActivity(intent);
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
                                                                        Toast.makeText(shooter_display_details.this, "Successfully removed from your liked pets", Toast.LENGTH_SHORT).show();

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
        likes_class like = new likes_class("",FirebaseAuth.getInstance().getCurrentUser().getUid(),id,shooter_id,"Service", Timestamp.now());

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
                                                                                Toast.makeText(shooter_display_details.this, "Successfully added to your likes", Toast.LENGTH_SHORT).show();
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