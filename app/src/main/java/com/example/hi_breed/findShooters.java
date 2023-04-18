package com.example.hi_breed;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hi_breed.adapter.shooterAdapter.recommend_serviceAdapter;
import com.example.hi_breed.classesFile.ServiceDisplay;
import com.example.hi_breed.classesFile.rating_class;
import com.example.hi_breed.classesFile.service_class;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class findShooters extends AppCompatActivity {
     RelativeLayout toolbarID;
    RecyclerView recommended_shooter;
    recommend_serviceAdapter adapter;
    String matchID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_shooter);
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
        toolbarID = findViewById(R.id.toolbarID);
        toolbarID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findShooters.this.onBackPressed();
                finish();
            }
        });
        Intent intent = getIntent();
        matchID = intent.getStringExtra("matchID");
        recommended_shooter = findViewById(R.id.recommended_shooter);
        recommended_shooter.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
        adapter = new recommend_serviceAdapter(this,matchID);
        FirebaseFirestore.getInstance().collection("User")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            String address = documentSnapshot.getString("address");
                            getReviews(address);
                        }
                    }
                });

    }

    private void getReviews(String address) {

        FirebaseFirestore.getInstance().collection("Services")
                .whereEqualTo("displayFor","Service")
                .whereEqualTo("serviceType","Shooter Service")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<ServiceDisplay> serviceDisplays = new ArrayList<>();
                        List<DocumentSnapshot> dlist = queryDocumentSnapshots.getDocuments();
                        List<ServiceDisplay> reviewedServices = new ArrayList<>();
                        List<ServiceDisplay> unreviewedServices = new ArrayList<>();

                        for(DocumentSnapshot ds : dlist) {
                            service_class service = ds.toObject(service_class.class);
                            FirebaseFirestore.getInstance().collection("Reviews")
                                    .whereEqualTo("seller_id",service.getShooter_id())
                                    .whereEqualTo("rateFor","Service")
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            if (queryDocumentSnapshots.isEmpty()) {
                                                if(service.getAddress().toLowerCase().contains(address.toLowerCase())) {
                                                    unreviewedServices.add(new ServiceDisplay(service, 0.0f, 0));
                                                }
                                                else{
                                                    unreviewedServices.add(new ServiceDisplay(service, 0.0f, 0));
                                                }

                                            } else {
                                                float totalRating = 0;
                                                int numRatings = 0;
                                                String type = "";
                                                for (DocumentSnapshot s : queryDocumentSnapshots) {
                                                    if (s.getDouble("rating") != null && !Double.isNaN(s.getDouble("rating"))) {
                                                        totalRating += s.getDouble("rating").floatValue();
                                                        numRatings++;

                                                        rating_class rate = s.toObject(rating_class.class);
                                                        type = rate.getType();
                                                    }
                                                }
                                                if (numRatings > 0 && type.equals("Shooter Service")) {
                                                    int totalReviews = queryDocumentSnapshots.size();
                                                    float averageRating = totalRating / numRatings;
                                                    float percent = Math.min(averageRating / 5 * 100, 100);
                                                    if(service.getAddress().toLowerCase().contains(address.toLowerCase())){
                                                        reviewedServices.add(new ServiceDisplay(service, percent, totalReviews));
                                                    }
                                                    else{
                                                        reviewedServices.add(new ServiceDisplay(service, percent, totalReviews));
                                                    }
                                                }
                                            }
                                            // Check if we've processed all services
                                            if (reviewedServices.size() + unreviewedServices.size() == dlist.size()) {
                                                // Sort reviewed services by percentage in descending order
                                                Collections.sort(reviewedServices, new Comparator<ServiceDisplay>() {
                                                    @Override
                                                    public int compare(ServiceDisplay sd1, ServiceDisplay sd2) {
                                                        return Float.compare(sd2.getPercent(), sd1.getPercent());
                                                    }
                                                });
                                                // Combine reviewed and unreviewed services
                                                serviceDisplays.addAll(reviewedServices);
                                                serviceDisplays.addAll(unreviewedServices);
                                                adapter.clearList();
                                                for (ServiceDisplay sd : serviceDisplays) {
                                                    adapter.addServiceDisplay(sd);
                                                    Log.d("reviewsF",sd.getService().getAddress()+":"+sd.getPercent());
                                                }
                                                recommended_shooter.setAdapter(adapter);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}