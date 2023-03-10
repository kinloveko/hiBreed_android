package com.example.hi_breed;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hi_breed.classesFile.appointment_class;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class acquired_service_details extends AppCompatActivity {

    TextView check_out_name,
            checkout_number,
            checkout_address,
            checkout_zip;
    TextView dateTextView,pet_text,check_out_service_acquired,
            itemSlot;
    Button cancelButton;
    RelativeLayout toolbarID,currentAddress,timeLayout,dateLayout;
    LinearLayout buttonLayout;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acquired_service_details);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.setStatusBarColor(Color.parseColor("#e28743"));
        }
        cancelButton=findViewById(R.id.cancelButton);
        toolbarID=findViewById(R.id.toolbarID);
        toolbarID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acquired_service_details.this.onBackPressed();
                finish();
            }
        });
        check_out_service_acquired = findViewById(R.id.check_out_service_acquired);
        pet_text = findViewById(R.id.pet_text);
        currentAddress =findViewById(R.id.currentAddress);
        timeLayout = findViewById(R.id.timeLayout);
        dateLayout = findViewById(R.id.dateLayout);
        dateTextView = findViewById(R.id.dateTextView);
        itemSlot = findViewById(R.id.itemSlot);
        check_out_name = findViewById(R.id.check_out_name);
        checkout_number = findViewById(R.id.checkout_number);
        checkout_address = findViewById(R.id.checkout_address);
        checkout_zip = findViewById(R.id.checkout_zip);
        buttonLayout = findViewById(R.id.buttonLayout);
        Intent intent = getIntent();

        appointment_class appointment = (appointment_class) intent.getSerializableExtra("mode");

        dateTextView.setText(appointment.getAppointment_date());
        itemSlot.setText(appointment.getAppointment_time());

        if(appointment.getCustomer_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            pet_text.setText("Service details");
            cancelButton.setVisibility(View.VISIBLE);
            FirebaseFirestore.getInstance().collection("Shop")
                    .document(appointment.getSeller_id())
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(DocumentSnapshot s) {
                            check_out_service_acquired.setVisibility(View.VISIBLE);
                            check_out_name.setText(s.getString("shopName"));

                          FirebaseFirestore.getInstance().collection("User")
                                  .document(s.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                      @Override
                                      public void onSuccess(DocumentSnapshot documentSnapshot) {

                                          checkout_address.setText(documentSnapshot.getString("address"));
                                          checkout_zip.setText(documentSnapshot.getString("zipCode"));

                                          FirebaseFirestore.getInstance().collection("User")
                                                  .document(s.getId()).collection("security")
                                                  .document("security_doc").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                      @Override
                                                      public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                checkout_number.setText(documentSnapshot.getString("contactNumber"));

                                                                FirebaseFirestore.getInstance().collection("Services").document(appointment.getService_id()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {


                                                                        check_out_service_acquired.setText(documentSnapshot.getString("name"));


                                                                    }
                                                                });
                                                      }
                                                  });
                                      }
                                  });

                        }
                    });

        }else
        if(appointment.getSeller_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            buttonLayout.setVisibility(View.VISIBLE);

        }
        else{
            cancelButton.setVisibility(View.GONE);
            buttonLayout.setVisibility(View.GONE);
        }

        FirebaseFirestore.getInstance().collection("User")
                .document(appointment.getCustomer_id())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(DocumentSnapshot s) {

                        check_out_name.setText(s.getString("firstName") + " " + s.getString("middleName") + " " + s.getString("lastName"));
                        checkout_address.setText(s.getString("address"));
                        checkout_zip.setText(s.getString("zipCode"));

                    }
                });


        FirebaseFirestore.getInstance().collection("User")
                .document(appointment.getCustomer_id())
                .collection("security")
                .document("security_doc")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot s = task.getResult();
                            checkout_number.setText(s.getString("contactNumber"));
                        }
                    }
                });



    }
}