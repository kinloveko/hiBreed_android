package com.example.hi_breed.service;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hi_breed.R;
import com.example.hi_breed.adapter.shooterAdapter.s_p_serviceAdapter;
import com.example.hi_breed.classesFile.BaseActivity;
import com.example.hi_breed.classesFile.service_class;
import com.example.hi_breed.service_status_for_seller.service_status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class service_panel extends BaseActivity {

    RelativeLayout backLayoutPet;
    Button shooter_verifyButton;
    RelativeLayout serviceLayout,show_serviceLayout;
    RecyclerView show_service_recycler;
    s_p_serviceAdapter adapter;
    TextView acquiredNumber,text,onGoingTextView,IDReview;
    CardView acquiredCardView,onGoing,reviews;
    String number;

    ArrayList<String> roles = new ArrayList<>();
    int count = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shooter_pannel);
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

        IDReview = findViewById(R.id.IDReview);
        reviews = findViewById(R.id.reviews);
        text = findViewById(R.id.text);
        acquiredCardView = findViewById(R.id.acquiredCardView);
        backLayoutPet = findViewById(R.id.toolbarID);
        shooter_verifyButton = findViewById(R.id.shooter_verifyButton);
        serviceLayout = findViewById(R.id.serviceLayout);
        onGoing = findViewById(R.id.onGoing);
        onGoingTextView = findViewById(R.id.onGoingTextView);
        backLayoutPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service_panel.this.onBackPressed();
                finish();
            }
        });


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reviewsRef = db.collection("Reviews");
        Query query = reviewsRef.whereEqualTo("seller_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
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
                Intent i = new Intent(service_panel.this, service_status.class);
                i.putExtra("SELECTED_TAB","reviews");
                startActivity(i);
            }
        });


        shooter_verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection("User")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .collection("security")
                                                .document("security_doc")
                                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.getString("contactNumber")==null ||
                                         documentSnapshot.getString("contactNumber").equals("")){
                                    Toast.makeText(service_panel.this, "Please setup your phone number first", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                else{
                                    Intent i = new Intent(service_panel.this, service_add_service.class);
                                    i.putStringArrayListExtra("list", (ArrayList<String>) roles);
                                    startActivity(i);
                                   }
                            }
                        });
            }
        });

        acquiredNumber = findViewById(R.id.acquiredNumber);
        show_service_recycler = findViewById(R.id.show_service_recycler);
        adapter = new s_p_serviceAdapter(this);
        show_service_recycler.setLayoutManager(new GridLayoutManager(this,1));
        show_service_recycler.setAdapter(adapter);

        FirebaseFirestore.getInstance().collection("User")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot s = task.getResult();
                            List<DocumentSnapshot> list = (List<DocumentSnapshot>) s.get("role");
                            if (list != null) {
                                if (list.contains("Veterinarian") && list.contains("Pet Shooter")) {
                                    handleVeterinarianAndPetShooterServices();
                                    Toast.makeText(service_panel.this, "VET SHOOT", Toast.LENGTH_SHORT).show();
                                } else if (list.contains("Veterinarian")) {
                                    getVeterinarianService();
                                    Toast.makeText(service_panel.this, "VET", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(service_panel.this, " SHOOT", Toast.LENGTH_SHORT).show();

                                    getService();
                                }
                            }
                        }
                    }
                });
        FirebaseFirestore.getInstance().collection("Appointments")
                .whereEqualTo("seller_id", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("appointment_status","pending")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            return;
                        }
                        if(value!=null){
                            List<DocumentSnapshot> list = value.getDocuments();
                            if(list!=null) {
                                acquiredNumber.setText(Integer.toString(list.size())); // Convert int to string
                            }
                        }
                    }
                });
        acquiredCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(service_panel.this, service_status.class));
            }
        });
        FirebaseFirestore.getInstance().collection("Appointments")
                .whereEqualTo("seller_id", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("appointment_status","accepted")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            return;
                        }
                        if(value!=null){
                            List<DocumentSnapshot> list = value.getDocuments();
                            if(list!=null) {
                                onGoingTextView.setText(Integer.toString(list.size())); // Convert int to string
                            }
                        }
                    }
                });
        onGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(service_panel.this, service_status.class);
                i.putExtra("SELECTED_TAB","accepted");
                startActivity(i);
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void handleVeterinarianAndPetShooterServices() {
        FirebaseFirestore.getInstance()
                .collection("Services")
                .whereEqualTo("shooter_id", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("serviceType", "Veterinarian Service")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot s : list) {
                                roles.add("Veterinarian Service");
                                service_class service_class = s.toObject(service_class.class);
                                adapter.addServiceDisplay(service_class);

                                if(roles.size() == 2){
                                    serviceLayout.setVisibility(View.GONE);
                                }
                                else{
                                    if(!roles.contains("Veterinarian")){
                                        serviceLayout.setVisibility(View.VISIBLE);
                                    }
                                    else if(roles.contains("Shooter Service")){
                                        serviceLayout.setVisibility(View.VISIBLE);
                                    }
                                    else{
                                        serviceLayout.setVisibility(View.VISIBLE);
                                    }
                                }

                            }
                    }
                });

        FirebaseFirestore.getInstance()
                .collection("Services")
                .whereEqualTo("shooter_id", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("serviceType", "Shooter Service")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot s : list) {
                                roles.add("Shooter Service");
                                service_class service_class = s.toObject(service_class.class);
                                adapter.addServiceDisplay(service_class);

                                if(roles.size() == 2){
                                    serviceLayout.setVisibility(View.GONE);
                                }
                                else{
                                    if(!roles.contains("Veterinarian")){
                                        serviceLayout.setVisibility(View.VISIBLE);
                                    }
                                    else if(roles.contains("Shooter Service")){
                                        serviceLayout.setVisibility(View.VISIBLE);
                                    }
                                    else{
                                        serviceLayout.setVisibility(View.VISIBLE);
                                    }
                                }

                            }
                    }
                });
    }


    private void getVeterinarianService(){
        FirebaseFirestore.getInstance()
                .collection("Services")
                .whereEqualTo("shooter_id", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("serviceType","Veterinarian Service")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot s : list) {
                                service_class service_class = s.toObject(service_class.class);
                                adapter.addServiceDisplay(service_class);
                            }
                    }
                });
    }

    private void getService() {
        FirebaseFirestore.getInstance()
                .collection("Services")
                .whereEqualTo("serviceType","Shooter Service")
                .whereEqualTo("shooter_id", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot s : list) {
                                service_class service_class = s.toObject(service_class.class);
                                adapter.addServiceDisplay(service_class);
                                serviceLayout.setVisibility(View.GONE);
                        }

                    }
                });
    }
}