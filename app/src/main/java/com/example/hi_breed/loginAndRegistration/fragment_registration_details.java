package com.example.hi_breed.loginAndRegistration;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    Chip owner,breeder,shooter, vet;
    Button nextButton;
    int count = 0;



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
                    Toast.makeText(fragment_registration_details.this.getContext(), "Select one or more to continue . .", Toast.LENGTH_SHORT).show();
                }
                else {
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("key",roleHolder);

                    fragment_registration_requirements requirements = new fragment_registration_requirements();
                    requirements.setArguments(bundle);

                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                    fr.replace(R.id.fragmentContainerView2,requirements);
                    fr.addToBackStack("name");
                    fr.commit();
                }
            }
        });

    }
}
