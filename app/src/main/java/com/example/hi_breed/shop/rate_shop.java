package com.example.hi_breed.shop;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.appointment_order_class;
import com.example.hi_breed.order_user_side.order_user_side;
import com.example.hi_breed.service_status_for_buyer.appointment_user_side;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class rate_shop extends AppCompatActivity {

    private float userRate = 0;
    private EditText editText;
    private TextView title, message;
    private RatingBar ratingBar;
    private Button rateLater,rateNow;
    private ImageView image,imageService;
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_shop);

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

        title = findViewById(R.id.title);
        message = findViewById(R.id.message);


        editText = findViewById(R.id.editText);
        ratingBar = findViewById(R.id.ratingBar);

        rateLater = findViewById(R.id.rateLater);
        rateNow = findViewById(R.id.rateNow);

        image  = findViewById(R.id.image);
        imageService  = findViewById(R.id.imageService);

        Intent intent = getIntent();
        appointment_order_class appoint = (appointment_order_class) intent.getSerializableExtra("model");

        if(appoint!=null){

            FirebaseFirestore.getInstance().collection("Pet").document(appoint.getItem_id()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        List<String> list = (List<String>) documentSnapshot.get("photos");
                        if(list!=null)
                            Picasso.get().load(list.get(0)).placeholder(R.drawable.noimage).into(imageService);
                    }
                }
            });

            FirebaseFirestore.getInstance().collection("Shop").document(appoint.getSeller_id()).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            message.setText("How was the "+appoint.getType()+" from "+documentSnapshot.getString("shopName"));
                        }
                    });
        }



        rateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userRate != 0){
                    Map<String,Object> map = new HashMap<>();
                    map.put("id","");
                    map.put("rateShop",appoint.getSeller_id());
                    map.put("customer_id",appoint.getCustomer_id());
                    map.put("type",appoint.getType());
                    map.put("seller_id",appoint.getSeller_id());
                    map.put("rateFor","Shop");
                    map.put("rating",userRate);
                    map.put("comment",editText.getText().toString());
                    map.put("timestamp", Timestamp.now());
                    map.put("isRated",true);

                    FirebaseFirestore.getInstance().collection("Reviews")
                            .add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Map<String,Object> a = new HashMap<>();
                                    a.put("isRated",true);
                                    a.put("rated_id",documentReference.getId());
                                    FirebaseFirestore.getInstance().collection("Appointments")
                                            .document(appoint.getId())
                                            .set(a, SetOptions.merge())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    FirebaseFirestore.getInstance().collection("Reviews")
                                                            .document(documentReference.getId())
                                                            .update("id",documentReference.getId(),"timestamp", FieldValue.serverTimestamp())
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
                                                                @Override
                                                                public void onSuccess(Void unused) {

                                                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(rate_shop.this);
                                                                    builder2.setCancelable(false);
                                                                    View view = View.inflate(rate_shop.this,R.layout.screen_custom_alert,null);
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
                                                                    imageViewCompat.setImageDrawable(getDrawable(R.drawable.screen_alert_image_valid_borders));
                                                                    //message
                                                                    TextView message = view.findViewById(R.id.screen_custom_alert_message);
                                                                    title.setText("We appreciate your review and value your opinion.");
                                                                    title.setTextSize(14);
                                                                    message.setVisibility(View.GONE);
                                                                    //button

                                                                    builder2.setView(view);
                                                                    AlertDialog alert2 = builder2.create();
                                                                    alert2.show();
                                                                    alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                                                    new Handler().postDelayed(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            alert2.dismiss();
                                                                            Intent i = new Intent(rate_shop.this, order_user_side.class);
                                                                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                            startActivity(i);
                                                                            finish();

                                                                        }
                                                                    },2000);
                                                                }
                                                            });
                                                }
                                            });
                                }
                            });
                }
                else{
                    Toast.makeText(rate_shop.this, "Please rate before submitting", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rateLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(rate_shop.this, appointment_user_side.class));
                finish();

            }
        });
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                if(rating <= 1){
                    image.setImageResource(R.drawable.one_star);
                }
                else if (rating <= 2){
                    image.setImageResource(R.drawable.two_star);
                }
                else if (rating <= 3){
                    image.setImageResource(R.drawable.three_star);
                }
                else if (rating <= 4){
                    image.setImageResource(R.drawable.four_star);
                }
                else if (rating <= 5){
                    image.setImageResource(R.drawable.five_star);
                }
                animateImage(image);

                userRate = rating;
            }
        });
    }

    private void animateImage(ImageView ratingImage){

        ScaleAnimation scaleAnimation = new ScaleAnimation(0,1f,0,1f,
                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);

        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(200);
        ratingImage.startAnimation(scaleAnimation);

    }
}