package com.example.hi_breed.userFile.profile;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.BaseActivity;
import com.example.hi_breed.classesFile.EmailPassClass;
import com.example.hi_breed.loginAndRegistration.Login;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.auth.api.phone.SmsRetrieverStatusCodes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.droidsonroids.gif.GifImageView;

public class
user_profile_account_contact extends BaseActivity {

    FirebaseAuth mAuth;
    String verificationID;
    PhoneAuthProvider.ForceResendingToken tokens;
    String clickName;
    String before_email;
    String before_contact;
    String get_password;
    RelativeLayout email, contact;
    TextView emailInput, contactInput;
    FirebaseFirestore fireStore;
    DocumentReference documentReference;
    TextView editButton, undoEditButton;
    FirebaseUser user;
    LinearLayout backLayout;
    CountDownTimer cTimer = null;
    String phoneNumber;

    final Pattern phonePattern = Pattern.compile("^(09)\\d{9}");
    final Pattern p = Pattern.compile("^" +
            "(?=.*[@#$%^&!+=])" +     // at least 1 special character
            "(?=\\S+$)" +            // no white spaces
            ".{5,}" +                // at least 5 characters
            "$");

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_account_contact_info);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.setStatusBarColor(Color.parseColor("#e28743"));
        }
        mAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();

        if (user != null) {

            documentReference = fireStore.collection("User").document(user.getUid())
                    .collection("security").document("security_doc");
            getUser(documentReference);
        }

        //textview
        editButton = findViewById(R.id.editButton_contact);
        undoEditButton = findViewById(R.id.undoEditButton_contact);

        email = findViewById(R.id.contact_email_layout);
        contact = findViewById(R.id.contact_mobile_layout);

        emailInput = findViewById(R.id.email_input);
        contactInput = findViewById(R.id.contact_input);

        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_profile_account_contact.this.onBackPressed();
                finish();
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
            @Override
            public void onClick(View v) {
                email.setOnClickListener(new ClickClass());
                contact.setOnClickListener(new ClickClass());
                editButton.setVisibility(View.GONE);
                undoEditButton.setVisibility(View.VISIBLE);
            }
        });

        undoEditButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
            @Override
            public void onClick(View v) {
                email.setClickable(false);
                contact.setClickable(false);
                editButton.setVisibility(View.VISIBLE);
                undoEditButton.setVisibility(View.GONE);
                AlertDialog.Builder builder2 = new AlertDialog.Builder(user_profile_account_contact.this);
                builder2.setCancelable(false);
                View view = View.inflate(user_profile_account_contact.this, R.layout.screen_custom_alert, null);
                //title
                TextView title = view.findViewById(R.id.screen_custom_alert_title);
                //loading text
                TextView loadingText = view.findViewById(R.id.screen_custom_alert_loadingText);
                loadingText.setVisibility(View.GONE);
                //gif
                GifImageView gif = view.findViewById(R.id.screen_custom_alert_gif);
                Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.kawaii_access);
                gif.setImageURI(uri);
                //header image
                AppCompatImageView imageViewCompat = view.findViewById(R.id.appCompatImageView);
                imageViewCompat.setVisibility(View.VISIBLE);
                imageViewCompat.setImageDrawable(getDrawable(R.drawable.screen_alert_image_valid_borders));
                //message
                TextView message = view.findViewById(R.id.screen_custom_alert_message);
                title.setText("Access closed");
                message.setVisibility(View.GONE);
                //button
                builder2.setView(view);
                AlertDialog alert2 = builder2.create();
                alert2.show();
                alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alert2.dismiss();
                    }
                }, 2000);
            }
        });

    }

    int count = 0;

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    private void allAlertDialog(String clickName) {
        if (clickName.equals("email")) {
            AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
            View view2 = View.inflate(this, R.layout.screen_custom_alert, null);
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
            MaterialButton cancel, okay;
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
                    if (s.length() != 0) {

                        clear.setVisibility(View.VISIBLE);
                        clear.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                editText.getText().clear();
                            }
                        });
                    } else {
                        clear.setVisibility(View.GONE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            builder3.setView(view2);
            AlertDialog alert3 = builder3.create();
            alert3.show();
            alert3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert3.dismiss();
                }
            });

            okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (editText.getText().toString().equals("") || editText.getText().toString() == null) {
                        count = 0;
                        Toast.makeText(user_profile_account_contact.this, "Oops. You made no entry! your password was expected", Toast.LENGTH_SHORT).show();
                    } else if (!(editText.getText().toString().equals(get_password))) {
                        count = 1;
                        Toast.makeText(user_profile_account_contact.this, "Oops. You entered password doesn't matched to this user", Toast.LENGTH_SHORT).show();
                    } else {
                        alert3.dismiss();

                        AlertDialog.Builder builder3 = new AlertDialog.Builder(user_profile_account_contact.this);
                        View view2 = View.inflate(user_profile_account_contact.this, R.layout.screen_custom_alert, null);
                        builder3.setCancelable(false);
                        //title
                        TextView title2 = view2.findViewById(R.id.screen_custom_alert_title);
                        title2.setText("Email");
                        //message
                        TextView message2 = view2.findViewById(R.id.screen_custom_alert_message);
                        message2.setText("Please input your email");
                        //loading
                        TextView loadingText = view2.findViewById(R.id.screen_custom_alert_loadingText);
                        loadingText.setVisibility(View.GONE);
                        //top image
                        AppCompatImageView imageViewCompat2 = view2.findViewById(R.id.appCompatImageView);
                        imageViewCompat2.setVisibility(View.VISIBLE);
                        imageViewCompat2.setImageDrawable(getDrawable(R.drawable.dialog_email_borders));
                        //gif
                        GifImageView gif = view2.findViewById(R.id.screen_custom_alert_gif);
                        gif.setVisibility(View.GONE);
                        //button layout
                        LinearLayout buttonLayout = view2.findViewById(R.id.screen_custom_alert_buttonLayout);
                        buttonLayout.setVisibility(View.VISIBLE);
                        //button
                        MaterialButton cancel, okay;
                        //cancel button
                        cancel = view2.findViewById(R.id.screen_custom_dialog_btn_cancel);
                        cancel.setText("Cancel");
                        //Okay button
                        okay = view2.findViewById(R.id.screen_custom_alert_dialog_btn_done);
                        okay.setText("Save");
                        okay.setBackgroundColor(Color.parseColor("#F6B75A"));
                        okay.setTextColor(Color.WHITE);
                        //EditText Layout
                        RelativeLayout editLayout = view2.findViewById(R.id.screen_custom_editText_layout);
                        editLayout.setVisibility(View.VISIBLE);
                        //EditText
                        EditText editText = view2.findViewById(R.id.screen_custom_editText);
                        editText.setHint("Email address..");
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
                                if (s.length() != 0) {
                                    Matcher m = p.matcher(s);
                                    if (m.matches()) {
                                        valid.setVisibility(View.VISIBLE);
                                    } else {
                                        valid.setVisibility(View.GONE);
                                    }
                                    clear.setVisibility(View.VISIBLE);
                                    clear.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            editText.getText().clear();
                                        }
                                    });
                                } else {
                                    valid.setVisibility(View.GONE);
                                    clear.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                        builder3.setView(view2);
                        AlertDialog alert4 = builder3.create();
                        alert4.show();
                        alert4.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alert4.dismiss();
                            }
                        });

                        okay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Matcher m = p.matcher(editText.getText().toString());
                                if (editText.getText().toString().equals("") || editText.getText().toString() == null) {
                                    count = 0;
                                    Toast.makeText(user_profile_account_contact.this, "Oops. You made no entry! your email was expected", Toast.LENGTH_SHORT).show();
                                } else if (editText.getText().toString().equals(before_email)) {
                                    count = 1;
                                    Toast.makeText(user_profile_account_contact.this, "You entered same value to your existing email", Toast.LENGTH_SHORT).show();
                                } else if (!(m.matches())) {
                                    Toast.makeText(user_profile_account_contact.this, "Oops. Enter a valid email ex. example@gmail.com", Toast.LENGTH_SHORT).show();
                                    count = 0;
                                } else {
                                    FirebaseAuth.getInstance().fetchSignInMethodsForEmail(editText.getText().toString())
                                            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                                                    boolean isNewUser = Objects.requireNonNull(task.getResult().getSignInMethods()).isEmpty();
                                                    if (isNewUser) {
                                                        user.updateEmail(editText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    count = 1;
                                                                    emailInput.setText(editText.getText().toString());

                                                                    //save old and new email in fire store @security --> security_doc
                                                                    Map<String, Object> data = new HashMap<>();
                                                                    data.put("old_email", before_email);
                                                                    data.put("email", editText.getText().toString());
                                                                    FirebaseFirestore.getInstance().collection("User")
                                                                            .document(user.getUid())
                                                                            .collection("security")
                                                                            .document("security_doc")
                                                                            .set(data, SetOptions.merge());
                                                                    // check if the user is verified

                                                                    alert4.dismiss();
                                                                    AlertDialog.Builder builder3 = new AlertDialog.Builder(user_profile_account_contact.this);
                                                                    View view2 = View.inflate(user_profile_account_contact.this, R.layout.screen_custom_alert, null);
                                                                    builder3.setCancelable(false);
                                                                    //title
                                                                    TextView title2 = view2.findViewById(R.id.screen_custom_alert_title);
                                                                    title2.setText("Verify your new email");
                                                                    //message
                                                                    TextView message2 = view2.findViewById(R.id.screen_custom_alert_message);
                                                                    message2.setText("Click VERIFY to send a verification link to your email that you've provided.");
                                                                    //loading
                                                                    TextView loadingText = view2.findViewById(R.id.screen_custom_alert_loadingText);
                                                                    loadingText.setVisibility(View.GONE);
                                                                    //top image
                                                                    AppCompatImageView imageViewCompat2 = view2.findViewById(R.id.appCompatImageView);
                                                                    imageViewCompat2.setVisibility(View.VISIBLE);
                                                                    imageViewCompat2.setImageDrawable(getDrawable(R.drawable.dialog_email_borders));
                                                                    //gif
                                                                    GifImageView gif = view2.findViewById(R.id.screen_custom_alert_gif);
                                                                    gif.setVisibility(View.GONE);
                                                                    //button layout
                                                                    LinearLayout buttonLayout = view2.findViewById(R.id.screen_custom_alert_buttonLayout);
                                                                    buttonLayout.setVisibility(View.VISIBLE);
                                                                    //button
                                                                    MaterialButton cancel, okay;
                                                                    //cancel button
                                                                    cancel = view2.findViewById(R.id.screen_custom_dialog_btn_cancel);
                                                                    cancel.setText("Cancel");
                                                                    //Okay button
                                                                    okay = view2.findViewById(R.id.screen_custom_alert_dialog_btn_done);
                                                                    okay.setText("Verify");
                                                                    okay.setBackgroundColor(Color.parseColor("#F6B75A"));
                                                                    okay.setTextColor(Color.WHITE);

                                                                    //EditText Layout
                                                                    RelativeLayout editLayout = view2.findViewById(R.id.screen_custom_editText_layout);
                                                                    editLayout.setVisibility(View.GONE);
                                                                    //EditText

                                                                    //valid
                                                                    ImageView valid = view2.findViewById(R.id.screen_custom_valid_icon);
                                                                    //clear text
                                                                    TextView clear = view2.findViewById(R.id.screen_custom_clearText);
                                                                    clear.setVisibility(View.GONE);

                                                                    builder3.setView(view2);
                                                                    AlertDialog alert3 = builder3.create();
                                                                    alert3.show();
                                                                    alert3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                                                    cancel.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {
                                                                            alert4.dismiss();
                                                                            alert3.dismiss();

                                                                            //getting the old email
                                                                            FirebaseFirestore.getInstance()
                                                                                    .collection("User")
                                                                                    .document(user.getUid())
                                                                                    .collection("security")
                                                                                    .document("security_doc")
                                                                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                            if (task.isSuccessful()) {

                                                                                                DocumentSnapshot value = task.getResult();
                                                                                                String oldEmail = value.getString("old_email");
                                                                                                Map<String, Object> data = new HashMap<>();
                                                                                                data.put("email", oldEmail);
                                                                                                data.put("old_email", "");

                                                                                                FirebaseAuth.getInstance().getCurrentUser()
                                                                                                        .updateEmail(oldEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                                //storing in fireStore
                                                                                                                FirebaseAuth.getInstance().getCurrentUser();
                                                                                                                FirebaseFirestore
                                                                                                                        .getInstance()
                                                                                                                        .collection("User")
                                                                                                                        .document(user.getUid())
                                                                                                                        .collection("security")
                                                                                                                        .document("security_doc")
                                                                                                                        .set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                            @Override
                                                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                if (task.isSuccessful()) {

                                                                                                                                    getUser(documentReference);

                                                                                                                                }
                                                                                                                            }
                                                                                                                        });
                                                                                                                Toast.makeText(user_profile_account_contact.this, "No changes, cancel button clicked.", Toast.LENGTH_LONG).show();
                                                                                                            }
                                                                                                        });
                                                                                            }


                                                                                        }
                                                                                    });
                                                                        }
                                                                    });

                                                                    okay.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {
                                                                            loadingText.setVisibility(View.VISIBLE);
                                                                            user.sendEmailVerification();
                                                                            okay.setVisibility(View.GONE);
                                                                            cancel.setVisibility(View.GONE);


                                                                            cTimer = new CountDownTimer(15000, 1000) {
                                                                                public void onTick(long millisUntilFinished) {
                                                                                    loadingText.setText("seconds remaining: " + String.valueOf(millisUntilFinished / 1000));

                                                                                }

                                                                                public void onFinish() {
                                                                                    if (user.isEmailVerified()) {
                                                                                        count = 1;
                                                                                        alert4.dismiss();
                                                                                        FirebaseAuth.getInstance().getCurrentUser().updateEmail(editText.getText().toString());
                                                                                        Toast.makeText(user_profile_account_contact.this, "Updated Successfully", Toast.LENGTH_SHORT).show();

                                                                                    } else {
                                                                                        loadingText.setVisibility(View.GONE);
                                                                                        message2.setText("If you wish to verify again click verify button and cancel if you wish to cancel the changing of your email");
                                                                                        title2.setText("Re send verification?");
                                                                                        okay.setVisibility(View.VISIBLE);
                                                                                        cancel.setVisibility(View.VISIBLE);
                                                                                    }
                                                                                }
                                                                            };
                                                                            cTimer.start();
                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {

                                                                alert4.dismiss();
                                                                Toast.makeText(user_profile_account_contact.this, "Please try again, or go Sign out and Sign in again. Then you can continue changing your email", Toast.LENGTH_LONG).show();
                                                            }
                                                        });

                                                    } else {
                                                        alert4.dismiss();
                                                        AlertDialog.Builder builder3 = new AlertDialog.Builder(user_profile_account_contact.this);
                                                        View view2 = View.inflate(user_profile_account_contact.this, R.layout.screen_custom_alert, null);
                                                        builder3.setCancelable(false);
                                                        TextView title2 = view2.findViewById(R.id.screen_custom_alert_title);
                                                        TextView loadingText = view2.findViewById(R.id.screen_custom_alert_loadingText);
                                                        loadingText.setVisibility(View.GONE);
                                                        AppCompatImageView imageViewCompat2 = view2.findViewById(R.id.appCompatImageView);
                                                        imageViewCompat2.setVisibility(View.VISIBLE);
                                                        imageViewCompat2.setImageDrawable(getResources().getDrawable(R.drawable.screen_alert_image_error_border));
                                                        GifImageView gif = view2.findViewById(R.id.screen_custom_alert_gif);
                                                        gif.setVisibility(View.GONE);
                                                        TextView message2 = view2.findViewById(R.id.screen_custom_alert_message);
                                                        title2.setText("Email address already exist!");
                                                        message2.setText("Please use another email address.");
                                                        LinearLayout buttonLayout = view2.findViewById(R.id.screen_custom_alert_buttonLayout);
                                                        buttonLayout.setVisibility(View.VISIBLE);
                                                        MaterialButton cancel, okay;
                                                        cancel = view2.findViewById(R.id.screen_custom_dialog_btn_cancel);
                                                        cancel.setVisibility(View.GONE);
                                                        okay = view2.findViewById(R.id.screen_custom_alert_dialog_btn_done);
                                                        okay.setText("Okay");
                                                        okay.setBackgroundColor(Color.parseColor("#999999"));
                                                        okay.setTextColor(Color.WHITE);
                                                        builder3.setView(view2);
                                                        AlertDialog alert3 = builder3.create();
                                                        alert3.show();
                                                        alert3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                        okay.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                alert3.dismiss();
                                                                alert4.show();
                                                            }
                                                        });

                                                    }


                                                }
                                            });

                                }
                                if (count != 0) {
                                    alert4.dismiss();
                                }
                            }

                        });
                    }

                    if (count != 0) {
                        alert3.dismiss();
                    }
                }
            });

        } else if (clickName.equals("contact")) {

            AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
            View view2 = View.inflate(this, R.layout.screen_custom_alert, null);
            builder3.setCancelable(false);
            //title
            TextView title2 = view2.findViewById(R.id.screen_custom_alert_title);
            title2.setText("Contact number");
            //message
            TextView message2 = view2.findViewById(R.id.screen_custom_alert_message);
            message2.setText("Please input your contact number");
            //loading
            TextView loadingText = view2.findViewById(R.id.screen_custom_alert_loadingText);
            loadingText.setVisibility(View.GONE);
            //top image
            AppCompatImageView imageViewCompat2 = view2.findViewById(R.id.appCompatImageView);
            imageViewCompat2.setVisibility(View.VISIBLE);
            imageViewCompat2.setImageDrawable(getDrawable(R.drawable.dialog_phone_borders));
            //gif
            GifImageView gif = view2.findViewById(R.id.screen_custom_alert_gif);
            gif.setVisibility(View.GONE);
            //button layout
            LinearLayout buttonLayout = view2.findViewById(R.id.screen_custom_alert_buttonLayout);
            buttonLayout.setVisibility(View.VISIBLE);
            //button
            MaterialButton cancel, okay;
            //cancel button
            cancel = view2.findViewById(R.id.screen_custom_dialog_btn_cancel);
            cancel.setText("Cancel");
            //Okay button
            okay = view2.findViewById(R.id.screen_custom_alert_dialog_btn_done);
            okay.setText("Save");
            okay.setBackgroundColor(Color.parseColor("#F6B75A"));
            okay.setTextColor(Color.WHITE);
            //EditText
            EditText editText = view2.findViewById(R.id.screen_custom_editText);
            RelativeLayout editLayout = view2.findViewById(R.id.screen_custom_editText_layout);
            editLayout.setVisibility(View.VISIBLE);
            editText.setHint("Phone ..");
            editText.setError(null);
            int maxLength = 11;
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
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
                    if (s.length() != 0) {
                        Matcher m = phonePattern.matcher(s);
                        if (m.matches()) {
                            valid.setVisibility(View.VISIBLE);
                        } else {
                            valid.setVisibility(View.GONE);
                        }
                        clear.setVisibility(View.VISIBLE);
                        clear.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                editText.getText().clear();
                            }
                        });
                    } else {
                        valid.setVisibility(View.GONE);
                        clear.setVisibility(View.GONE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            builder3.setView(view2);
            AlertDialog alert3 = builder3.create();
            alert3.show();
            alert3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert3.dismiss();

                }
            });
            okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Matcher m = phonePattern.matcher(editText.getText().toString());
                    if (editText.getText().toString().equals("") || editText.getText().toString() == null) {
                        count = 0;
                        Toast.makeText(user_profile_account_contact.this, "Oops. You made no entry! your phone number was expected", Toast.LENGTH_SHORT).show();
                    } else if (!(m.matches())) {
                        Toast.makeText(user_profile_account_contact.this, "Oops. Enter a valid phone number ex. 09106851425 ", Toast.LENGTH_SHORT).show();
                        count = 0;
                    } else if (editText.getText().toString().equals(before_contact)) {
                        count = 1;
                        contactInput.setText(editText.getText().toString());
                        Toast.makeText(user_profile_account_contact.this, "Oops. We expecting a different name, but you entered the same value.", Toast.LENGTH_SHORT).show();
                    } else {


                        okay.setEnabled(false);

// This is your send verification code method
// Remove leading "0" and replace with "+63"
                        phoneNumber = editText.getText().toString().trim();
                        if (phoneNumber.startsWith("0") && phoneNumber.length() == 11) {
                            phoneNumber = "+63" + phoneNumber.substring(1);
                            FirebaseFirestore.getInstance().collection("User")
                                    .document(user.getUid())
                                    .collection("security")
                                    .document("security_doc")
                                    .update("contactNumber", phoneNumber).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            alert3.dismiss();
                                            Toast.makeText(user_profile_account_contact.this, "Successfully Set", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        Toast.makeText(user_profile_account_contact.this, phoneNumber, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void getUser(DocumentReference documentReference) {
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {
                        EmailPassClass securityClass = snapshot.toObject(EmailPassClass.class);
                        if (securityClass != null) {
                            before_contact = securityClass.getContactNumber();
                            before_email = securityClass.getEmail();

                            get_password = securityClass.getPass();

                            if (securityClass.getEmail().equals("")) {
                                emailInput.setText("Set email address");
                            } else {
                                emailInput.setText(securityClass.getEmail());
                            }
                            if (securityClass.getContactNumber() == null || securityClass.getContactNumber().equals("")) {
                                contactInput.setText("Set phone number");
                            } else {
                                contactInput.setText(securityClass.getContactNumber());
                            }
                        }
                    } else {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(user_profile_account_contact.this, Login.class));
                        finish();
                    }
                }
            }
        });
    }

    private class ClickClass implements View.OnClickListener {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.contact_email_layout:
                    clickName = "email";
                    allAlertDialog(clickName);
                    break;
                case R.id.contact_mobile_layout:
                    clickName = "contact";
                    allAlertDialog(clickName);
                    break;
            }
        }
    }
}