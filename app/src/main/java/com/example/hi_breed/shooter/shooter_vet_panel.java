package com.example.hi_breed.shooter;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class shooter_vet_panel extends BaseActivity {

    RelativeLayout backLayoutPet;
    Button shooter_verifyButton;
    RelativeLayout serviceLayout,show_serviceLayout;
    RecyclerView show_service_recycler;
    s_p_serviceAdapter adapter;
    TextView acquiredNumber;
    CardView acquiredCardView;
    String number;

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

        acquiredCardView = findViewById(R.id.acquiredCardView);
        backLayoutPet = findViewById(R.id.toolbarID);
        shooter_verifyButton = findViewById(R.id.shooter_verifyButton);
        serviceLayout = findViewById(R.id.serviceLayout);

        backLayoutPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shooter_vet_panel.this.onBackPressed();
                finish();
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
                                    Toast.makeText(shooter_vet_panel.this, "Please setup your phone number first", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                else{
                                    startActivity(new Intent(shooter_vet_panel.this, shooter_vet_add_service.class));
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
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot s = task.getResult();
                            List<DocumentSnapshot> list = (List<DocumentSnapshot>) s.get("role");
                            if(list.contains("Veterinarian")){
                                getVeterinarianService();
                            }
                            else{
                                getService();
                            }
                        }
                    }
                });
        FirebaseFirestore.getInstance().collection("Appointments")
                .whereEqualTo("seller_id", FirebaseAuth.getInstance().getCurrentUser().getUid())
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
                    startActivity(new Intent(shooter_vet_panel.this, service_status.class));
            }
        });

    }
    private void getVeterinarianService(){
        FirebaseFirestore.getInstance()
                .collection("Services")
                .whereEqualTo("shooter_id", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        if(list.size() !=0){
                            serviceLayout.setVisibility(View.GONE);
                            for (DocumentSnapshot s:list){
                                service_class service_class = s.toObject(service_class.class);
                                adapter.addServiceDisplay(service_class);
                            }
                        }else
                        {
                            serviceLayout.setVisibility(View.VISIBLE);
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
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        if(list.size() !=0){
                            serviceLayout.setVisibility(View.GONE);
                            for (DocumentSnapshot s:list){
                                service_class service_class = s.toObject(service_class.class);
                                adapter.addServiceDisplay(service_class);
                            }
                        }else
                        {
                            serviceLayout.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}