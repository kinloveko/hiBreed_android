package com.example.hi_breed.loginAndRegistration;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hi_breed.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class forgot_activity extends AppCompatActivity {

    LinearLayout backLayoutService;
    TextInputEditText email_Edit;
    TextInputLayout email_layout;
    Button Button_LoginID, gotoLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        }


        backLayoutService = findViewById(R.id.backLayoutService);
        email_Edit = findViewById(R.id.email_Edit);
        email_layout = findViewById(R.id.email_layout);
        Button_LoginID = findViewById(R.id.Button_LoginID);
        gotoLogin = findViewById(R.id.gotoLogin);

        backLayoutService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgot_activity.this.onBackPressed();
                finish();
            }
        });
        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(forgot_activity.this, Login.class));
                finish();
            }
        });
        Button_LoginID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email_Edit.getText().toString().isEmpty()) {
                    Toast.makeText(forgot_activity.this, "Email text field is empty", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();

                    String email = email_Edit.getText().toString();

                    mAuth.fetchSignInMethodsForEmail(email)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    SignInMethodQueryResult result = task.getResult();
                                    if (result.getSignInMethods().isEmpty()) {

                                        Toast.makeText(forgot_activity.this, "Email isn't registered in our system", Toast.LENGTH_SHORT).show();


                                    } else {
                                        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()) {

                                                    Toast.makeText(forgot_activity.this, "Email sent! Please check your inbox/spam", Toast.LENGTH_SHORT).show();
                                                    email_Edit.getText().clear();
                                                    startActivity(new Intent(forgot_activity.this,Login.class));
                                                    finish();
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    // Error occurred
                                    Log.d("Error", task.getException().toString());
                                }
                            });
                }


            }
        });


    }

    @Override
    public void onBackPressed() {
        finish();
    }
}