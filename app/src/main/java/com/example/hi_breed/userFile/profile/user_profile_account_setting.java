package com.example.hi_breed.userFile.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.BaseActivity;


public class user_profile_account_setting extends BaseActivity {

    RelativeLayout accountInfo,contactInfo,security;
    LinearLayout backLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_account_setting);


        backLayout = findViewById(R.id.backLayout);
        accountInfo = findViewById(R.id.first_layout);
        contactInfo = findViewById(R.id.second_layout);
        security = findViewById(R.id.third_layout);

        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_profile_account_setting.this.onBackPressed();
                finish();
            }
        });
        accountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(user_profile_account_setting.this, user_profile_account_edit.class));
            }
        });
        contactInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(user_profile_account_setting.this, user_profile_account_contact.class));
            }
        });
        security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(user_profile_account_setting.this, user_profile_account_security_info.class));
            }
        });
    }
}
