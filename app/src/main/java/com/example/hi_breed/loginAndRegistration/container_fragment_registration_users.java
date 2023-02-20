package com.example.hi_breed.loginAndRegistration;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import com.example.hi_breed.R;

public class container_fragment_registration_users extends AppCompatActivity {

    FragmentContainerView fragmentContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_for_owner);

        fragmentContainerView = findViewById(R.id.fragmentContainerView2);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainerView2,new fragment_registration_details());
        fragmentTransaction.commit();

    }//end of onCreate()

    public void gotoLogin(View view) {
        startActivity(new Intent(this,Login.class));
        finish();
    }
    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();

        }

    }

}
