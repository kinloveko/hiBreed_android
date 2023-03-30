package com.example.hi_breed;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class change_role extends AppCompatActivity {



    ArrayList<String> roleHolder;
    Chip owner,breeder,shooter, vet;
    Button nextButton;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_role);

        //List
        roleHolder = new ArrayList<>();
        //button
        nextButton =findViewById(R.id.nextButton);
        //chip
        owner = findViewById(R.id.ownerID_checkBox);
        breeder = findViewById(R.id.breederID_checkBox);
        shooter = findViewById(R.id.shooterID_checkBox);
        vet = findViewById(R.id.vetID_checkBox);
        count = 0;
        roleHolder.clear();
        owner.setChecked(false);
        breeder.setChecked(false);
        shooter.setChecked(false);
        vet.setChecked(false);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.poppinsmedium);

        vet.setTypeface(typeface);
        owner.setTypeface(typeface);
        breeder.setTypeface(typeface);
        shooter.setTypeface(typeface);




        FirebaseFirestore.getInstance().collection("User")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        List<String> list = (List<String>) documentSnapshot.get("role");
                        if(list != null){
                            if(list.contains("Pet Owner")){
                                count++;
                                roleHolder.add(owner.getText().toString());
                                owner.setChipStartPadding(20);
                                owner.setChecked(true);
                            }
                            if(list.contains("Pet Breeder")){
                                count++;
                                roleHolder.add(breeder.getText().toString());
                                breeder.setChipStartPadding(20);
                                breeder.setChecked(true);
                            }
                            if(list.contains("Pet Shooter")){
                                count++;
                                shooter.setChecked(true);
                                roleHolder.add(shooter.getText().toString());
                                shooter.setChipStartPadding(20);
                            }
                            if(list.contains("Veterinarian")){
                                count ++;
                                vet.setChecked(true);
                                roleHolder.add(vet.getText().toString());
                                vet.setChipStartPadding(15);
                            }

                        }
                    }
                });

        owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(owner.isChecked()){
                    count++;

                    roleHolder.add(owner.getText().toString());
                    owner.setChipStartPadding(20);
                }
                else{

                    if(breeder.isChecked()){
                        count--;
                        breeder.setChecked(false);
                        roleHolder.remove(breeder.getText().toString());

                    }

                    count--;
                    roleHolder.remove(owner.getText().toString());
                    owner.setChipStartPadding(0);

                }
            }
        });

        breeder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(breeder.isChecked()){

                    // checked if the owner already checked
                    if(owner.isChecked()){
                        count++;
                        roleHolder.add(breeder.getText().toString());
                        breeder.setChipStartPadding(20);
                    }
                    else{
                        count+=2;
                        owner.setChecked(true);
                        owner.setChipStartPadding(20);
                        roleHolder.add(owner.getText().toString());

                        roleHolder.add(breeder.getText().toString());
                        breeder.setChipStartPadding(20);
                    }


                }
                else{
                    count--;
                    roleHolder.remove(breeder.getText().toString());
                    breeder.setChipStartPadding(0);
                }
            }
        });

        shooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shooter.isChecked()){
                    // checked if the owner already checked

                    count++;
                    roleHolder.add(shooter.getText().toString());
                    shooter.setChipStartPadding(20);

                }
                else{
                    count--;
                    roleHolder.remove(shooter.getText().toString());
                    shooter.setChipStartPadding(0);
                }
            }
        });

        vet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vet.isChecked()){
                    count +=2;
                    roleHolder.add(vet.getText().toString());
                    vet.setChipStartPadding(15);
                }
                else{
                    count--;
                    roleHolder.remove(vet.getText().toString());
                    vet.setChipStartPadding(0);
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count==0){
                    Toast.makeText(change_role.this, "Select one or more to continue . .", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(roleHolder.size() == 1){
                        if(roleHolder.contains("Pet Owner")){
                            FirebaseFirestore.getInstance().collection("User")
                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .update("role", roleHolder).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                            FirebaseFirestore.getInstance().
                                                    collection("User")
                                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .update("status","verified").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(change_role.this, "1", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(change_role.this,not_verified_activity.class));
                                                            finish();
                                                        }
                                                    });
                                        }
                                    });
                        }
                    }
                    else{
                        FirebaseFirestore.getInstance().collection("User")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .update("role", roleHolder).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(change_role.this, "2", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(change_role.this,not_verified_activity.class));
                                        finish();

                                    }
                                });

                    }

                }
            }
        });
    }
}