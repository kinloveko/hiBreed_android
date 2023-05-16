package com.example.hi_breed.userFile.profile;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.BaseActivity;
import com.example.hi_breed.loginAndRegistration.Login;
<<<<<<< HEAD
import com.example.hi_breed.screenLoading.screen_splashScreen_MainActivity;
import com.example.hi_breed.userFile.dashboard.user_dashboard;
=======
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;


public class user_profile_account_security_info extends BaseActivity {
    LinearLayout backLayout;
<<<<<<< HEAD
    RelativeLayout second_layout, first_layout;
=======
    RelativeLayout second_layout,first_layout;
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_account_security_info);
        Window window = getWindow();
<<<<<<< HEAD
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
=======
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        }
        else{
            window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
            window.setStatusBarColor(Color.parseColor("#e28743"));
        }
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_profile_account_security_info.this.onBackPressed();
                finish();
            }
        });
        second_layout = findViewById(R.id.second_layout);
        first_layout = findViewById(R.id.first_layout);

        first_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection("User")
<<<<<<< HEAD
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .collection("security").document("security_doc")
                        .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                                if (error != null) return;
                                if (documentSnapshot.exists()) {
                                    if (documentSnapshot.exists()) {
                                        String email = documentSnapshot.getString("email");
                                        String password = documentSnapshot.getString("pass");
                                        changePassword(email, password);
=======
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .collection("security").document("security_doc")
                        .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                                if(error!=null) return;
                                if(documentSnapshot.exists()){
                                    if(documentSnapshot.exists()){
                                        String email = documentSnapshot.getString("email");
                                        String password = documentSnapshot.getString("pass");
                                        changePassword(email,password);
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                                    }
                                }
                            }
                        });

            }
        });

        second_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUserAccount();
            }
        });


    }
<<<<<<< HEAD

    int count = 0;

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    private void changePassword(String email, String password) {
        AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
        View view2 = View.inflate(this, R.layout.screen_custom_alert, null);
=======
    int count = 0;
    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    private void changePassword(String email,String password) {
        AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
        View view2 = View.inflate(this,R.layout.screen_custom_alert,null);
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
        builder3.setCancelable(false);
        //title
        TextView title2 = view2.findViewById(R.id.screen_custom_alert_title);
        title2.setText("Enter password");
        //message
        TextView message2 = view2.findViewById(R.id.screen_custom_alert_message);
        message2.setText("Enter your password to continue.");
        //loading
        TextView loadingText = view2.findViewById(R.id.screen_custom_alert_loadingText);
        loadingText.setVisibility(View.GONE);
        //top image
        AppCompatImageView imageViewCompat2 = view2.findViewById(R.id.appCompatImageView);
        imageViewCompat2.setVisibility(View.VISIBLE);
        imageViewCompat2.setImageDrawable(getDrawable(R.drawable.dialog_pass_borders));
        //gif
        GifImageView gif = view2.findViewById(R.id.screen_custom_alert_gif);
        gif.setVisibility(View.GONE);
        //button layout
        LinearLayout buttonLayout = view2.findViewById(R.id.screen_custom_alert_buttonLayout);
        buttonLayout.setVisibility(View.VISIBLE);
        //button
<<<<<<< HEAD
        MaterialButton cancel, okay;
=======
        MaterialButton cancel,okay;
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
        //cancel button
        cancel = view2.findViewById(R.id.screen_custom_dialog_btn_cancel);
        cancel.setText("Cancel");
        //Okay button
        okay = view2.findViewById(R.id.screen_custom_alert_dialog_btn_done);
        okay.setText("Enter");
        okay.setBackgroundColor(Color.parseColor("#F6B75A"));
        okay.setTextColor(Color.WHITE);
        //EditText
        EditText editText = view2.findViewById(R.id.screen_custom_editText);
        RelativeLayout editLayout = view2.findViewById(R.id.screen_custom_editText_layout);
        editLayout.setVisibility(View.VISIBLE);
        editText.setHint("Password..");
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        //valid
        ImageView valid = view2.findViewById(R.id.screen_custom_valid_icon);
        //clear text
        TextView clear = view2.findViewById(R.id.screen_custom_clearText);
        clear.setVisibility(View.GONE);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
<<<<<<< HEAD
                if (s.length() != 0) {
=======
                if(s.length()!=0){
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02

                    clear.setVisibility(View.VISIBLE);
                    clear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editText.getText().clear();
                        }
                    });
<<<<<<< HEAD
                } else {
=======
                }
                else{
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                    clear.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
<<<<<<< HEAD
        AlertDialog alert3 = null;
        if (!isFinishing()) {
            builder3.setView(view2);
            alert3 = builder3.create();
=======
        AlertDialog alert3 =null;
        if (!isFinishing()) {
            builder3.setView(view2);
            alert3  = builder3.create();
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
            alert3.show();
            alert3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        AlertDialog finalAlert = alert3;
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalAlert.dismiss();
            }
        });

        AlertDialog finalAlert1 = alert3;
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

<<<<<<< HEAD
                if (editText.getText().toString().equals("") || editText.getText().toString() == null) {
                    finalAlert1.dismiss();
                    Toast.makeText(user_profile_account_security_info.this, "Oops. You made no entry! your password was expected", Toast.LENGTH_SHORT).show();
                } else if (!(editText.getText().toString().equals(password))) {
                    finalAlert1.dismiss();
                    Toast.makeText(user_profile_account_security_info.this, "Oops. You entered password doesn't matched to this user", Toast.LENGTH_SHORT).show();
                } else {
                    finalAlert1.dismiss();
                    AlertDialog.Builder builder3 = new AlertDialog.Builder(user_profile_account_security_info.this);
                    View view2 = View.inflate(user_profile_account_security_info.this, R.layout.screen_custom_alert, null);
=======
                if(editText.getText().toString().equals("") || editText.getText().toString() == null){
                    finalAlert1.dismiss();
                    Toast.makeText(user_profile_account_security_info.this, "Oops. You made no entry! your password was expected", Toast.LENGTH_SHORT).show();
                }
                else if(!(editText.getText().toString().equals(password))){
                    finalAlert1.dismiss();
                    Toast.makeText(user_profile_account_security_info.this, "Oops. You entered password doesn't matched to this user", Toast.LENGTH_SHORT).show();
                }
                else{
                    finalAlert1.dismiss();
                    AlertDialog.Builder builder3 = new AlertDialog.Builder(user_profile_account_security_info.this);
                    View view2 = View.inflate(user_profile_account_security_info.this,R.layout.screen_custom_alert,null);
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                    builder3.setCancelable(false);
                    //title
                    TextView title2 = view2.findViewById(R.id.screen_custom_alert_title);
                    title2.setText("Enter new password");
                    //message
                    TextView message2 = view2.findViewById(R.id.screen_custom_alert_message);
                    message2.setText("Enter your new password");
                    //loading
                    TextView loadingText = view2.findViewById(R.id.screen_custom_alert_loadingText);
                    loadingText.setVisibility(View.GONE);
                    //top image
                    AppCompatImageView imageViewCompat2 = view2.findViewById(R.id.appCompatImageView);
                    imageViewCompat2.setVisibility(View.VISIBLE);
                    imageViewCompat2.setImageDrawable(getDrawable(R.drawable.dialog_pass_borders));
                    //gif
                    GifImageView gif = view2.findViewById(R.id.screen_custom_alert_gif);
                    gif.setVisibility(View.GONE);
                    //button layout
                    LinearLayout buttonLayout = view2.findViewById(R.id.screen_custom_alert_buttonLayout);
                    buttonLayout.setVisibility(View.VISIBLE);
                    //button
<<<<<<< HEAD
                    MaterialButton cancel, okay;
=======
                    MaterialButton cancel,okay;
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                    //cancel button
                    cancel = view2.findViewById(R.id.screen_custom_dialog_btn_cancel);
                    cancel.setText("Cancel");
                    //Okay button
                    okay = view2.findViewById(R.id.screen_custom_alert_dialog_btn_done);
                    okay.setText("Enter");
                    okay.setBackgroundColor(Color.parseColor("#F6B75A"));
                    okay.setTextColor(Color.WHITE);
                    //EditText
                    EditText editText = view2.findViewById(R.id.screen_custom_editText);
                    RelativeLayout editLayout = view2.findViewById(R.id.screen_custom_editText_layout);
                    editLayout.setVisibility(View.VISIBLE);
                    editText.setHint("Password..");
                    editText.setInputType(InputType.TYPE_CLASS_TEXT);
                    //valid
                    ImageView valid = view2.findViewById(R.id.screen_custom_valid_icon);
                    //clear text
                    TextView clear = view2.findViewById(R.id.screen_custom_clearText);
                    clear.setVisibility(View.GONE);
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
<<<<<<< HEAD
                            if (s.length() != 0) {
=======
                            if(s.length()!=0){
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02

                                clear.setVisibility(View.VISIBLE);
                                clear.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        editText.getText().clear();
                                    }
                                });
<<<<<<< HEAD
                            } else {
=======
                            }
                            else{
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                                clear.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    builder3.setView(view2);
                    AlertDialog alert = builder3.create();
                    alert.show();
                    alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finalAlert1.dismiss();
                            alert.dismiss();
                        }
                    });

                    okay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
<<<<<<< HEAD
                            if (editText.getText().toString().equals("") || editText.getText().toString() == null) {
                                count = 0;
                                Toast.makeText(user_profile_account_security_info.this, "Oops. You made no entry! your password was expected", Toast.LENGTH_SHORT).show();
                            } else if (editText.getText().toString().equals(password)) {

                                Toast.makeText(user_profile_account_security_info.this, "Oops. You entered an old password", Toast.LENGTH_SHORT).show();
                            } else {
=======
                            if(editText.getText().toString().equals("") || editText.getText().toString() == null){
                                count = 0;
                                Toast.makeText(user_profile_account_security_info.this, "Oops. You made no entry! your password was expected", Toast.LENGTH_SHORT).show();
                            }
                            else if(editText.getText().toString().equals(password)){

                                Toast.makeText(user_profile_account_security_info.this, "Oops. You entered an old password", Toast.LENGTH_SHORT).show();
                            }
                            else{
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                AuthCredential credential = EmailAuthProvider.getCredential(email, password);

                                assert user != null;
                                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            user.updatePassword(editText.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
<<<<<<< HEAD
                                                                FirebaseFirestore.getInstance().collection("User")
                                                                        .document(user.getUid())
                                                                        .collection("security")
                                                                        .document("security_doc")
                                                                        .update("pass", editText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    finalAlert1.dismiss();
                                                                                    alert.dismiss();
                                                                                    Toast.makeText(user_profile_account_security_info.this, "Password Successfully Changed", Toast.LENGTH_SHORT).show();
                                                                                    startActivity(new Intent(user_profile_account_security_info.this, user_profile_account_security_info.class));
                                                                                    finish();
                                                                                }
                                                                            }
                                                                        });
=======
                                                              FirebaseFirestore.getInstance().collection("User")
                                                                      .document(user.getUid())
                                                                      .collection("security")
                                                                      .document("security_doc")
                                                                      .update("pass",editText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                          @Override
                                                                          public void onComplete(@NonNull Task<Void> task) {
                                                                                    if(task.isSuccessful()){
                                                                                        finalAlert1.dismiss();
                                                                                        alert.dismiss();
                                                                                        Toast.makeText(user_profile_account_security_info.this, "Password Successfully Changed", Toast.LENGTH_SHORT).show();
                                                                                      startActivity(new Intent(user_profile_account_security_info.this,user_profile_account_security_info.class));
                                                                                      finish();
                                                                                    }
                                                                          }
                                                                      });
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                                                            }
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.d("myApp", e.getMessage());
                                                        }
                                                    });
                                        } else {
                                            alert.dismiss();
                                        }
                                    }
                                });

                            }
                        }
                    });

                }
            }
        });

    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    private void deleteUserAccount() {
<<<<<<< HEAD
        AlertDialog.Builder builder2 = new AlertDialog.Builder(user_profile_account_security_info.this);
        builder2.setCancelable(false);
        View view = View.inflate(user_profile_account_security_info.this, R.layout.screen_custom_alert, null);
        //title
        TextView title = view.findViewById(R.id.screen_custom_alert_title);
        //loading text
        TextView loadingText = view.findViewById(R.id.screen_custom_alert_loadingText);
        loadingText.setVisibility(View.GONE);
        //gif
        GifImageView gif = view.findViewById(R.id.screen_custom_alert_gif);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.kawaii_cry);
        gif.setImageURI(uri);
        //header image
        AppCompatImageView imageViewCompat = view.findViewById(R.id.appCompatImageView);
        imageViewCompat.setVisibility(View.VISIBLE);
        imageViewCompat.setImageDrawable(getDrawable(R.drawable.screen_alert_image_error_border));
        //message
        TextView message = view.findViewById(R.id.screen_custom_alert_message);
        title.setText("Are you sure?");
        message.setVisibility(View.VISIBLE);
        message.setText("If you want to delete your account click delete button");
        //button
        LinearLayout buttonLayout = view.findViewById(R.id.screen_custom_alert_buttonLayout);
        buttonLayout.setVisibility(View.VISIBLE);
        MaterialButton cancel, okay;
        cancel = view.findViewById(R.id.screen_custom_dialog_btn_cancel);
        okay = view.findViewById(R.id.screen_custom_alert_dialog_btn_done);
        okay.setText("Delete");
        okay.setBackgroundColor(Color.parseColor("#F6B75A"));
        okay.setTextColor(Color.WHITE);

        builder2.setView(view);
        AlertDialog alert2 = builder2.create();
        alert2.show();
        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        okay.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
            @Override
            public void onClick(View v) {
                alert2.dismiss();
=======


>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                AlertDialog.Builder builder2 = new AlertDialog.Builder(user_profile_account_security_info.this);
                builder2.setCancelable(false);
                View view = View.inflate(user_profile_account_security_info.this, R.layout.screen_custom_alert, null);
                //title
                TextView title = view.findViewById(R.id.screen_custom_alert_title);
                //loading text
                TextView loadingText = view.findViewById(R.id.screen_custom_alert_loadingText);
                loadingText.setVisibility(View.GONE);
                //gif
                GifImageView gif = view.findViewById(R.id.screen_custom_alert_gif);
<<<<<<< HEAD
                gif.setVisibility(View.GONE);
                //header image
                AppCompatImageView imageViewCompat = view.findViewById(R.id.appCompatImageView);
                imageViewCompat.setVisibility(View.VISIBLE);
                imageViewCompat.setImageDrawable(getDrawable(R.drawable.screen_alert_image_valid_borders));
                //message
                TextView message = view.findViewById(R.id.screen_custom_alert_message);
                title.setText("Deleting your account");
                message.setVisibility(View.VISIBLE);
                message.setText("Please wait a bit. We are deleting your account info");
=======
                Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.kawaii_cry);
                gif.setImageURI(uri);
                //header image
                AppCompatImageView imageViewCompat = view.findViewById(R.id.appCompatImageView);
                imageViewCompat.setVisibility(View.VISIBLE);
                imageViewCompat.setImageDrawable(getDrawable(R.drawable.screen_alert_image_error_border));
                //message
                TextView message = view.findViewById(R.id.screen_custom_alert_message);
                title.setText("Are you sure?");
                message.setVisibility(View.VISIBLE);
                message.setText("If you want to delete your account click delete button");
                //button
                LinearLayout buttonLayout = view.findViewById(R.id.screen_custom_alert_buttonLayout);
                buttonLayout.setVisibility(View.VISIBLE);
                MaterialButton cancel, okay;
                cancel = view.findViewById(R.id.screen_custom_dialog_btn_cancel);
                okay = view.findViewById(R.id.screen_custom_alert_dialog_btn_done);
                okay.setText("Delete");
                okay.setBackgroundColor(Color.parseColor("#F6B75A"));
                okay.setTextColor(Color.WHITE);

>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                builder2.setView(view);
                AlertDialog alert2 = builder2.create();
                alert2.show();
                alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

<<<<<<< HEAD
                FirebaseFirestore.getInstance().collection("User")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    List<String> role = (List<String>) documentSnapshot.get("role");
                                    if (role != null) {
                                        FirebaseFirestore.getInstance().collection("Notifications")
                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                    }
                                                });
                                        deletePetDating();
                                        if (role.contains("Veterinarian")) {
                                            deleteServices();
                                            deleteProducts();
                                        }
                                        if (role.contains("Pet Shooter")) {
                                            deleteServices();
                                        }
                                        if (role.contains("Pet Breeder")) {
                                            deletePetForSale();
                                        }
                                        if (role.contains("Pet Breeder") || role.contains("Pet Shooter") || role.contains("Veterinarian")) {
                                            shopDelete();

                                            FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .collection("certificate").document("certificate_doc").delete();

                                            FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .collection("proof_photo").document("proof_doc").delete();

                                            FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .collection("validation").document("validation_doc").delete();
                                        }

                                        FirebaseFirestore.getInstance().collection("User")
                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .update("status","deleted").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        alert2.dismiss();

                                                        startActivity(new Intent(user_profile_account_security_info.this, user_dashboard.class));
                                                        finish();
                                                    }
                                                });
                                    }
                                }
                            }
                        });
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert2.dismiss();

            }
        });

    }



    private void shopDelete() {
        //shop
=======
                okay.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
                    @Override
                    public void onClick(View v) {
                        alert2.dismiss();
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(user_profile_account_security_info.this);
                        builder2.setCancelable(false);
                        View view = View.inflate(user_profile_account_security_info.this, R.layout.screen_custom_alert, null);
                        //title
                        TextView title = view.findViewById(R.id.screen_custom_alert_title);
                        //loading text
                        TextView loadingText = view.findViewById(R.id.screen_custom_alert_loadingText);
                        loadingText.setVisibility(View.GONE);
                        //gif
                        GifImageView gif = view.findViewById(R.id.screen_custom_alert_gif);
                        gif.setVisibility(View.GONE);
                        //header image
                        AppCompatImageView imageViewCompat = view.findViewById(R.id.appCompatImageView);
                        imageViewCompat.setVisibility(View.VISIBLE);
                        imageViewCompat.setImageDrawable(getDrawable(R.drawable.screen_alert_image_valid_borders));
                        //message
                        TextView message = view.findViewById(R.id.screen_custom_alert_message);
                        title.setText("Deleting your product");
                        message.setVisibility(View.VISIBLE);
                        message.setText("Please wait a bit. We are deleting your product info");
                        builder2.setView(view);
                        AlertDialog alert2 = builder2.create();
                        alert2.show();
                        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        FirebaseFirestore.getInstance().collection("User")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if(documentSnapshot.exists()){
                                            List<String> role = (List<String>) documentSnapshot.get("role");
                                            if(role!=null){
                                                deletePetDating();
                                                if(role.contains("Veterinarian")){
                                                    deleteServices();
                                                    deleteProducts();
                                                }
                                                if(role.contains("Pet Shooter")){
                                                    deleteServices();
                                                }
                                                if(role.contains("Pet Breeder")){
                                                    deletePetForSale();
                                                }
                                                if(role.contains("Pet Breeder") || role.contains("Pet Shooter") || role.contains("Veterinarian")){
                                                    shopDelete();

                                                    FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .collection("certificate").document("certificate_doc").delete();

                                                    FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .collection("proof_photo").document("proof_doc").delete();

                                                    FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .collection("validation").document("validation_doc").delete();

                                                }
                                                deleteOwnRecord(alert2);
                                            }

                                        }
                                    }
                                });


                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert2.dismiss();

                    }
                });

            }

    private void deleteOwnRecord(AlertDialog alertDialog) {
        FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .collection("security").document("security_doc").delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                        }
                    }
                });
        FirebaseFirestore.getInstance().collection("User")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        FirebaseAuth.getInstance().getCurrentUser().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                    alertDialog.dismiss();
                                    FirebaseAuth.getInstance().signOut();
                                    startActivity(new Intent(user_profile_account_security_info.this, Login.class));
                                    finish();
                            }
                        });
                    }
                });
    }

    private void shopDelete() {
        //shop

>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
        FirebaseFirestore.getInstance().collection("Shop")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
    }

    private void deletePetForSale() {
        FirebaseFirestore.getInstance()
                .collection("Pet")
<<<<<<< HEAD
                .whereEqualTo("pet_breeder", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("displayFor", "forSale")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.size() != 0) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot s : list) {
=======
                .whereEqualTo("pet_breeder",FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("displayFor","forSale")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.size()!=0){
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for(DocumentSnapshot s : list){
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                                String id = s.getString("id");
                                //product or medicine
                                FirebaseFirestore.getInstance().collection("Pet")
                                        .document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                //search
                                                FirebaseFirestore.getInstance().collection("Search")
                                                        .document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                FirebaseFirestore.getInstance().collection("User")
                                                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                        .collection("Pet")
                                                                        .document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                FirebaseFirestore.getInstance().collection("Shop")
                                                                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                                        .collection("Pet").document(id).delete();
                                                                            }
                                                                        });

                                                            }
                                                        });
                                            }
                                        });

                            }
                        }
                    }
                });
    }

    private void deletePetDating() {
        FirebaseFirestore.getInstance().collection("Pet")
<<<<<<< HEAD
                .whereEqualTo("pet_breeder", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("displayFor", "forDating")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.size() != 0) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot s : list) {
                                String pet_id = s.getString("id");
                                FirebaseFirestore.getInstance().collection("Pet")
                                        .document(pet_id)
                                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                FirebaseFirestore.getInstance().collection("User")
                                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .collection("Pet")
                                                        .document(pet_id).delete();
                                            }
                                        });
=======
                .whereEqualTo("pet_breeder",FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("displayFor","forDating")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.size()!=0) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot s : list) {
                                    String pet_id = s.getString("id");
                                    FirebaseFirestore.getInstance().collection("Pet")
                                            .document(pet_id)
                                            .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    FirebaseFirestore.getInstance().collection("User")
                                                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .collection("Pet")
                                                            .document(pet_id).delete();
                                                }
                                            });
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                            }
                        }
                    }
                });
    }

    private void deleteProducts() {
        FirebaseFirestore.getInstance()
                .collection("Pet")
<<<<<<< HEAD
                .whereEqualTo("vet_id", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.size() != 0) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot s : list) {
=======
                .whereEqualTo("vet_id",FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.size()!=0){
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for(DocumentSnapshot s : list){
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                                String id = s.getString("id");
                                //product or medicine
                                FirebaseFirestore.getInstance().collection("Pet")
                                        .document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                //search
                                                FirebaseFirestore.getInstance().collection("Search")
                                                        .document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                //shop
                                                                FirebaseFirestore.getInstance().collection("Shop")
                                                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {
                                                                                FirebaseFirestore.getInstance().collection("User")
                                                                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                                        .collection("Products").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                FirebaseFirestore.getInstance().collection("Shop")
                                                                                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                                                        .collection("Products").document(id).delete();
                                                                                            }
                                                                                        });

                                                                            }
                                                                        });
                                                            }
                                                        });
                                            }
                                        });
                            }
                        }
                    }
                });
    }

    private void deleteServices() {
        FirebaseFirestore.getInstance().collection("Services")
<<<<<<< HEAD
                .whereEqualTo("shooter_id", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.size() != 0) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot s : list) {
=======
                .whereEqualTo("shooter_id",FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.size()!=0){
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for(DocumentSnapshot s : list) {
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                                String service_id = s.getString("id");
                                FirebaseFirestore.getInstance().collection("Services")
                                        .document(service_id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                FirebaseFirestore.getInstance().collection("Search").document(service_id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                .collection("Services").document(service_id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        FirebaseFirestore.getInstance().collection("Shop")
                                                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                                .collection("Services").document(service_id).delete();
                                                                    }
                                                                });

                                                    }
                                                });
                                            }
                                        });
                            }
                        }
                    }
                });
    }

}
