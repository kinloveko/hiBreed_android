package com.example.hi_breed;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class Report_activity extends AppCompatActivity {

    ImageView photo_report;
    Button reportUser;
    LinearLayout backLayoutService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.setStatusBarColor(Color.parseColor("#e28743"));
        }
        backLayoutService = findViewById(R.id.backLayoutService);
        backLayoutService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        reportUser = findViewById(R.id.reportUser);
        photo_report = findViewById(R.id.photo_report);
        reportUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recipient = "info.hibreed@gmail.com";
                String subject = "Reporting a user";

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Gmail app is not installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        photo_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(Report_activity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

                dialog.setContentView(R.layout.custom_dialog_zoom);

                ImageView imageView = dialog.findViewById(R.id.image_view_dialog);
                Button buttonClose = dialog.findViewById(R.id.button_close_dialog);


                Glide.with(getApplicationContext())
                        .load(R.drawable.report_user)
                        .placeholder(R.drawable.noimage)
                        .error(R.drawable.screen_alert_image_error_border)
                        .into(imageView);
                buttonClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });


    }
}