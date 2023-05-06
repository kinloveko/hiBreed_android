package com.example.hi_breed.screenLoading;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hi_breed.R;
import com.example.hi_breed.loginAndRegistration.Login;
import com.example.hi_breed.loginAndRegistration.container_fragment_registration_users;

public class screen_WelcomeToHiBreed extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_welcome_to_hi_breed);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        }

    }


    public void SignIn_Onclick(View view) {
                Intent intent = new Intent(screen_WelcomeToHiBreed.this, Login.class);
                startActivity(intent);
    }


    public void createAccount_Onclick(View view) {
        {
        /*    startActivity(new Intent(this, image_activity.class));*/
        startActivity(new Intent(this, container_fragment_registration_users.class));
        }

    }

    }
