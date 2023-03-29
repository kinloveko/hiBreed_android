package com.example.hi_breed.userFile.profile;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hi_breed.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class current_address extends AppCompatActivity {

    String addressBefore,zipBefore;
    TextInputEditText phoneEdit,addressEdit,zipEdit;
    RelativeLayout phoneLayout,toolbarID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_address);

        phoneEdit = findViewById(R.id.phoneEdit);
        addressEdit = findViewById(R.id.addressEdit);
        zipEdit = findViewById(R.id.zipEdit);
        toolbarID = findViewById(R.id.toolbarID);
        toolbarID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_address.this.onBackPressed();
                finish();
            }
        });

        Intent i = getIntent();
        String address = i.getStringExtra("address");
        String zip = i.getStringExtra("zip");
        String phone = i.getStringExtra("phone");

        addressBefore = i.getStringExtra("address");
        zipBefore = i.getStringExtra("zip");
        phoneEdit.setText(phone);
        addressEdit.setText(address);
        zipEdit.setText(zip);
        phoneLayout = findViewById(R.id.phoneLayout);
        phoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(current_address.this, user_profile_account_contact.class));
            }
        });
        addressNameclearButton = findViewById(R.id.addressNameclearButton);
        addressSaveButton = findViewById(R.id.addressSaveButton);
        zipclearButton = findViewById(R.id.zipclearButton);
        zipSaveButton = findViewById(R.id.zipSaveButton);


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

        if(addressEdit.getText() !=null || addressEdit.getText().equals("")) {
            addressEdit.addTextChangedListener(addressWatcher);
        }
        if(zipEdit.getText() !=null || zipEdit.getText().equals("")) {
            zipEdit.addTextChangedListener(zipWatcher);
        }
    }

    ImageView addressNameclearButton,zipSaveButton,addressSaveButton,zipclearButton;


    private final TextWatcher addressWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @SuppressLint("SetTextI18n")
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length

            if(s!=null){
                addressNameclearButton.setVisibility(View.VISIBLE);
                addressNameclearButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addressEdit.getText().clear();
                    }
                });

                if(s.length() > 19){
                    if(s.toString().equals(addressBefore)){
                        addressSaveButton.setVisibility(View.GONE);
                    }
                    else{
                        addressSaveButton.setVisibility(View.VISIBLE);
                        addressSaveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                UpdateInfo("address",s.toString());
                                addressBefore = s.toString();
                                addressSaveButton.setVisibility(View.GONE);
                            }
                        });
                    }

                }
                else{
                    addressSaveButton.setVisibility(View.GONE);
                }
            }
            else{
                addressSaveButton.setVisibility(View.GONE);
                addressNameclearButton.setVisibility(View.GONE);
            }

        }

        public void afterTextChanged(Editable s) {
        }
    };

    private final TextWatcher zipWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @SuppressLint("SetTextI18n")
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length

            if(s!=null){
                zipclearButton.setVisibility(View.VISIBLE);
                zipclearButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        zipEdit.getText().clear();
                    }
                });

                if(s.length() == 4){
                    if(s.toString().equals(zipBefore)){
                        zipSaveButton.setVisibility(View.GONE);
                    }
                    else{
                        zipSaveButton.setVisibility(View.VISIBLE);
                        zipSaveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                UpdateInfo("zipCode",s.toString());
                                zipBefore = s.toString();
                                zipSaveButton.setVisibility(View.GONE);
                            }
                        });
                    }

                }
                else{
                    zipSaveButton.setVisibility(View.GONE);
                }
            }
            else{
                zipSaveButton.setVisibility(View.GONE);
                zipclearButton.setVisibility(View.GONE);
            }

        }

        public void afterTextChanged(Editable s) {
        }
    };


    private void UpdateInfo(String field, String value) {

        FirebaseFirestore.getInstance().collection("User")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .update(field,value).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(current_address.this, "Successfully change", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}