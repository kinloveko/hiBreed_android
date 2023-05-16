package com.example.hi_breed.loginAndRegistration;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.hi_breed.R;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class fragment_registration_details extends Fragment {


<<<<<<< HEAD
=======

>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.registration__details, container, false);
    }

    ArrayList<String> roleHolder;
<<<<<<< HEAD
    Chip owner, breeder, shooter, vet;
    Button nextButton;
    int count = 0;

    TextView veterinarianDescription,
            shooterDescription,
            breederDescription,
            petOwnerDescription;
=======
    Chip owner,breeder,shooter, vet;
    Button nextButton;
    int count = 0;

    TextView  veterinarianDescription,
              shooterDescription,
              breederDescription,
              petOwnerDescription;
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
    LinearLayout backLayoutService;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //List
        roleHolder = new ArrayList<>();
        //button
        nextButton = view.findViewById(R.id.nextButton);
        //chip
        owner = view.findViewById(R.id.ownerID_checkBox);
        breeder = view.findViewById(R.id.breederID_checkBox);
        shooter = view.findViewById(R.id.shooterID_checkBox);
        vet = view.findViewById(R.id.vetID_checkBox);
<<<<<<< HEAD
        veterinarianDescription = view.findViewById(R.id.veterinarianDescription);
        shooterDescription = view.findViewById(R.id.shooterDescription);
        breederDescription = view.findViewById(R.id.breederDescription);
        petOwnerDescription = view.findViewById(R.id.petOwnerDescription);
=======
        veterinarianDescription= view.findViewById(R.id.veterinarianDescription);
        shooterDescription= view.findViewById(R.id.shooterDescription);
        breederDescription= view.findViewById(R.id.breederDescription);
        petOwnerDescription= view.findViewById(R.id.petOwnerDescription);
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
        backLayoutService = view.findViewById(R.id.backLayoutService);
        backLayoutService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Simulate back button press
                getActivity().onBackPressed();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        count = 0;
        roleHolder.clear();
        owner.setChecked(false);
        breeder.setChecked(false);
        shooter.setChecked(false);
        vet.setChecked(false);
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.poppinsmedium);

        vet.setTypeface(typeface);
        owner.setTypeface(typeface);
        breeder.setTypeface(typeface);
        shooter.setTypeface(typeface);

        owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< HEAD
                //if not checked
                if (owner.isChecked()) {
=======

                if(owner.isChecked()){
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                    count++;

                    roleHolder.add(owner.getText().toString());
                    owner.setChipStartPadding(20);
                    petOwnerDescription.setVisibility(View.VISIBLE);
<<<<<<< HEAD
                } else {
                    //is checked
                    if (breeder.isChecked()) {
=======
                }
                else{

                    if(breeder.isChecked()){
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                        count--;
                        breeder.setChecked(false);
                        roleHolder.remove(breeder.getText().toString());
                        breederDescription.setVisibility(View.GONE);
                    }
<<<<<<< HEAD
=======

>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                    count--;
                    roleHolder.remove(owner.getText().toString());
                    owner.setChipStartPadding(0);
                    petOwnerDescription.setVisibility(View.GONE);
                }
            }
        });

        breeder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< HEAD
                if (breeder.isChecked()) {

                    // checked if the owner already checked
                    if (owner.isChecked()) {
=======
                if(breeder.isChecked()){

                    // checked if the owner already checked
                    if(owner.isChecked()){
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                        count++;
                        roleHolder.add(breeder.getText().toString());
                        breeder.setChipStartPadding(20);
                        breederDescription.setVisibility(View.VISIBLE);
                        petOwnerDescription.setVisibility(View.VISIBLE);
<<<<<<< HEAD
                    } else {
                        count += 2;
=======
                    }
                    else{
                        count+=2;
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                        owner.setChecked(true);
                        owner.setChipStartPadding(20);
                        roleHolder.add(owner.getText().toString());

                        roleHolder.add(breeder.getText().toString());
                        breeder.setChipStartPadding(20);
                        petOwnerDescription.setVisibility(View.VISIBLE);
                        breederDescription.setVisibility(View.VISIBLE);
                    }
<<<<<<< HEAD
                } else {
=======


                }
                else{
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                    count--;
                    roleHolder.remove(breeder.getText().toString());
                    breeder.setChipStartPadding(0);
                    breederDescription.setVisibility(View.GONE);
                }
            }
        });

        shooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< HEAD
                if (shooter.isChecked()) {
                    // checked if the owner already checked

                    count++;
                    roleHolder.add(shooter.getText().toString());
                    shooter.setChipStartPadding(20);
                    shooterDescription.setVisibility(View.VISIBLE);
                } else {
=======
                if(shooter.isChecked()){
                    // checked if the owner already checked

                       count++;
                       roleHolder.add(shooter.getText().toString());
                       shooter.setChipStartPadding(20);
                       shooterDescription.setVisibility(View.VISIBLE);
                }
                else{
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                    count--;
                    roleHolder.remove(shooter.getText().toString());
                    shooter.setChipStartPadding(0);
                    shooterDescription.setVisibility(View.GONE);
                }
            }
        });

        vet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< HEAD
                if (vet.isChecked()) {
                    count++;
                    roleHolder.add(vet.getText().toString());
                    vet.setChipStartPadding(15);
                    veterinarianDescription.setVisibility(View.VISIBLE);
                } else {
=======
                if(vet.isChecked()){
                    count +=2;
                    roleHolder.add(vet.getText().toString());
                    vet.setChipStartPadding(15);
                    veterinarianDescription.setVisibility(View.VISIBLE);
                }
                else{
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                    count--;
                    roleHolder.remove(vet.getText().toString());
                    vet.setChipStartPadding(0);
                    veterinarianDescription.setVisibility(View.GONE);
                }
            }
        });

<<<<<<< HEAD

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 0) {
                    Toast.makeText(fragment_registration_details.this.getContext(), "Select one or more to continue . .", Toast.LENGTH_SHORT).show();
                } else {


                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("key", roleHolder);
=======
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count==0){
                    Toast.makeText(fragment_registration_details.this.getContext(), "Select one or more to continue . .", Toast.LENGTH_SHORT).show();
                }
                else {
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("key",roleHolder);
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02

                    fragment_registration_requirements requirements = new fragment_registration_requirements();
                    requirements.setArguments(bundle);

                    FragmentTransaction fr = getFragmentManager().beginTransaction();
<<<<<<< HEAD
                    fr.replace(R.id.fragmentContainerView2, requirements);
                    fr.addToBackStack("name");
                    fr.commit();


=======
                    fr.replace(R.id.fragmentContainerView2,requirements);
                    fr.addToBackStack("name");
                    fr.commit();
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                }
            }
        });

<<<<<<< HEAD

=======
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
    }
}
