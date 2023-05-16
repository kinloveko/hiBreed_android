package com.example.hi_breed.userFile.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;

import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.BaseActivity;
import com.example.hi_breed.phoneAccess.phone_UncropperActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class user_profile_account_edit extends BaseActivity {

    /*
         updateButton.setText("Save");
                updateButton.setText("Edit");*/
    LinearLayout backLayout;

    private DocumentReference documentReference;
    private FirebaseFirestore firebaseFirestore;
    StorageReference fileRefBreeder;
    StorageReference fileRefBreederBackground;
    private FirebaseUser firebaseUser;
    Uri imgsUri, imgUriCover;
    ActivityResultLauncher<String> cropImage, cropImageCover;
    ImageView imageView, imageBackground;
    String userID;
    String alertCaller;
    RelativeLayout reg_firstLayout, reg_lastLayout, reg_middleLayout, reg_addressLayout, reg_zipLayout, reg_birthdayLayout, reg_genderLayout;
    TextView reg_first, reg_last, reg_middle, reg_gender, reg_birthday, reg_address, reg_zip;
    TextView updateButton, editButton, undoEditButton;
    String before_first, before_last, before_middle, before_address, before_zip, before_birthday, before_gender;


    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_account_info);
        Window w = getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            userID = firebaseUser.getUid();
            documentReference = firebaseFirestore.collection("User").document(userID);
            getUserInfo();
        }

        //background cover
        fileRefBreederBackground = FirebaseStorage.getInstance().getReference("User/" + firebaseUser.getUid() + "/" + "Profile Background/");
        //profile
        fileRefBreeder = FirebaseStorage.getInstance().getReference("User/" + userID + "/" + "Profile Picture/");


        //Relative layout
        reg_firstLayout = findViewById(R.id.first_layout);
        reg_middleLayout = findViewById(R.id.middle_layout);
        reg_lastLayout = findViewById(R.id.last_layout);
        reg_birthdayLayout = findViewById(R.id.birthday_layout);
        reg_genderLayout = findViewById(R.id.gender_layout);
        reg_addressLayout = findViewById(R.id.address_layout);
        reg_zipLayout = findViewById(R.id.zip_layout);

        //textview
        reg_first = findViewById(R.id.first_input);
        reg_middle = findViewById(R.id.middle_input);
        reg_last = findViewById(R.id.last_input);
        reg_birthday = findViewById(R.id.birthday_input);
        reg_gender = findViewById(R.id.gender_input);
        reg_address = findViewById(R.id.address_input);
        reg_zip = findViewById(R.id.zip_input);


        imageBackground = findViewById(R.id.imageBackground);
        imageView = findViewById(R.id.profileImage);
        //button textview
        updateButton = findViewById(R.id.updateButton);
        editButton = findViewById(R.id.editButton);
        undoEditButton = findViewById(R.id.undoEditButton);


        backLayout = findViewById(R.id.backLayout);
        CardView edit = findViewById(R.id.editIcon);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePermission();
            }
        });
        //set to read only

        // on click for edit text
        editButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                reg_firstLayout.setOnClickListener(new ClickClass());
                reg_middleLayout.setOnClickListener(new ClickClass());
                reg_lastLayout.setOnClickListener(new ClickClass());
                reg_birthdayLayout.setOnClickListener(new ClickClass());
                reg_genderLayout.setOnClickListener(new ClickClass());
                reg_addressLayout.setOnClickListener(new ClickClass());
                reg_zipLayout.setOnClickListener(new ClickClass());
                editButton.setVisibility(View.GONE);
                undoEditButton.setVisibility(View.VISIBLE);
            }

        });
        undoEditButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                reg_firstLayout.setClickable(false);
                reg_middleLayout.setClickable(false);
                reg_lastLayout.setClickable(false);
                reg_birthdayLayout.setClickable(false);
                reg_genderLayout.setClickable(false);
                reg_addressLayout.setClickable(false);
                reg_zipLayout.setClickable(false);
                editButton.setVisibility(View.VISIBLE);
                undoEditButton.setVisibility(View.GONE);
                AlertDialog.Builder builder2 = new AlertDialog.Builder(user_profile_account_edit.this);
                builder2.setCancelable(false);
                View view = View.inflate(user_profile_account_edit.this, R.layout.screen_custom_alert, null);
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
                title.setText("Edit closed");
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

        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_profile_account_edit.this.onBackPressed();
                finish();
            }
        });

        //background image request
        cropImageCover = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            Intent intent = new Intent(this.getApplicationContext(), phone_UncropperActivity.class);

            if (result != null) {
                intent.putExtra("SendingData", result.toString());
                startActivityForResult(intent, 100);
            } else {
                Toast.makeText(this.getApplicationContext(), "No changes", Toast.LENGTH_SHORT).show();
            }
        });
        //profile image request
        cropImage = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            Intent intent = new Intent(user_profile_account_edit.this.getApplicationContext(), phone_UncropperActivity.class);

            if (result != null) {
                intent.putExtra("SendingData", result.toString());
                startActivityForResult(intent, 99);

            } else {
                Toast.makeText(this.getApplicationContext(), "No changes", Toast.LENGTH_SHORT).show();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePermission();
            }
        });
        imageBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePermissionBackground();
            }
        });


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProfileImage();
            }
        });
    }

    @Override
    public void onBackPressed() {

        user_profile_account_edit.this.finish();
    }

    //to get the value of the imageView
    String imageProf = "";
    String imageCover = "";
    List<String> roles = new ArrayList<>();

    private void getUserInfo() {

        FirebaseFirestore.getInstance().collection("User").document(firebaseUser.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                if (documentSnapshot.exists()) {

                    roles = (List<String>) documentSnapshot.get("role");
                    before_first = documentSnapshot.getString("firstName");
                    before_middle = documentSnapshot.getString("middleName");
                    before_last = documentSnapshot.getString("lastName");
                    before_address = documentSnapshot.getString("address");
                    before_zip = documentSnapshot.getString("birth");
                    before_gender = documentSnapshot.getString("gender");
                    before_birthday = documentSnapshot.getString("zipCode");

                    reg_first.setText(documentSnapshot.getString("firstName"));
                    reg_middle.setText(documentSnapshot.getString("middleName"));
                    reg_last.setText(documentSnapshot.getString("lastName"));
                    reg_gender.setText(documentSnapshot.getString("gender"));
                    reg_birthday.setText(documentSnapshot.getString("birth"));
                    reg_address.setText(documentSnapshot.getString("address"));
                    reg_zip.setText(documentSnapshot.getString("zipCode"));
                    if (documentSnapshot.getString("image") == "" || documentSnapshot.getString("image") == null) {
                        imageView.setImageResource(R.drawable.noimage);
                        imageProf = "";
                    } else {
                        Picasso.get().load(documentSnapshot.getString("image")).into(imageView);
                        //setting the variable to what imageView picture has.
                        imageProf = documentSnapshot.getString("image");
                    }
                    if (documentSnapshot.getString("backgroundImage") == "" || documentSnapshot.getString("backgroundImage") == null) {
                        imageBackground.setImageResource(R.drawable.nobackground);
                        imageCover = "";
                    } else {
                        Picasso.get().load(documentSnapshot.getString("backgroundImage")).into(imageBackground);
                        //setting the variable to what imageView picture has.
                        imageCover = documentSnapshot.getString("backgroundImage");
                    }
                }
            }
        });
    }


    int count = 0;
    String genderHolder = "";

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    private void dialogForAll(String alertCaller) {


        if (alertCaller.equals("firstName")) {

            AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
            View view2 = View.inflate(this, R.layout.screen_custom_alert, null);
            builder3.setCancelable(false);
            //title
            TextView title2 = view2.findViewById(R.id.screen_custom_alert_title);
            title2.setText("First name");
            //message
            TextView message2 = view2.findViewById(R.id.screen_custom_alert_message);
            message2.setText("Please input your Firstname");
            //loading
            TextView loadingText = view2.findViewById(R.id.screen_custom_alert_loadingText);
            loadingText.setVisibility(View.GONE);
            //top image
            AppCompatImageView imageViewCompat2 = view2.findViewById(R.id.appCompatImageView);
            imageViewCompat2.setVisibility(View.VISIBLE);
            imageViewCompat2.setImageDrawable(getDrawable(R.drawable.dialog_details_borders));
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
            editText.setHint("First name..");

            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            editText.setVisibility(View.VISIBLE);
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
                        if (s.length() >= 2) {
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
                    if (editText.getText().toString().equals("") || editText.getText().toString() == null) {
                        count = 0;
                        Toast.makeText(user_profile_account_edit.this, "Oops. You made no entry! a First name was expected", Toast.LENGTH_SHORT).show();
                    } else if (editText.getText().toString().equals(before_first)) {
                        count = 1;
                        reg_first.setText(editText.getText().toString());

                        Toast.makeText(user_profile_account_edit.this, "Oops. We expecting a different name, but you entered the same value.", Toast.LENGTH_SHORT).show();
                    } else if (editText.length() < 2) {
                        count = 0;
                        Toast.makeText(user_profile_account_edit.this, "Oops. Input must have 2 characters", Toast.LENGTH_SHORT).show();
                    } else {
                        reg_first.setText(editText.getText().toString());
                        Toast.makeText(user_profile_account_edit.this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                        count = 1;
                        Map<String, Object> data = new HashMap<>();
                        data.put("firstName", editText.getText().toString());

                        firebaseFirestore.collection("User")
                                .document(firebaseUser.getUid())
                                .set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(user_profile_account_edit.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                    if (count != 0) {
                        alert3.dismiss();
                    } else {

                    }
                }
            });

        } else if (alertCaller.equals("middleName")) {
            AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
            View view2 = View.inflate(this, R.layout.screen_custom_alert, null);
            builder3.setCancelable(false);
            //title
            TextView title2 = view2.findViewById(R.id.screen_custom_alert_title);
            title2.setText("Middle name");
            //message
            TextView message2 = view2.findViewById(R.id.screen_custom_alert_message);
            message2.setText("Please input your middle name. If you don't have one leave it blank, and Click CANCEL.");
            //loading
            TextView loadingText = view2.findViewById(R.id.screen_custom_alert_loadingText);
            loadingText.setVisibility(View.GONE);
            //top image
            AppCompatImageView imageViewCompat2 = view2.findViewById(R.id.appCompatImageView);
            imageViewCompat2.setVisibility(View.VISIBLE);
            imageViewCompat2.setImageDrawable(getDrawable(R.drawable.dialog_details_borders));
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
            editText.setVisibility(View.VISIBLE);
            editText.setHint("Middle name..");
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
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
                        Toast.makeText(user_profile_account_edit.this, "Oops. You made no entry! your Middle name was expected", Toast.LENGTH_SHORT).show();
                    } else if (editText.getText().toString().equals(before_middle)) {
                        count = 1;
                        reg_middle.setText(editText.getText().toString());
                        Toast.makeText(user_profile_account_edit.this, "Oops. We expecting a different name, but you entered the same value.", Toast.LENGTH_SHORT).show();
                    } else {
                        reg_middle.setText(editText.getText().toString());
                        Toast.makeText(user_profile_account_edit.this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                        count = 1;
                        Map<String, Object> data = new HashMap<>();
                        data.put("middleName", editText.getText().toString());
                        firebaseFirestore.collection("User")
                                .document(firebaseUser.getUid())
                                .set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(user_profile_account_edit.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                    if (count != 0) {
                        alert3.dismiss();
                    } else {

                    }
                }
            });
        } else if (alertCaller.equals("lastName")) {
            AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
            View view2 = View.inflate(this, R.layout.screen_custom_alert, null);
            builder3.setCancelable(false);
            //title
            TextView title2 = view2.findViewById(R.id.screen_custom_alert_title);
            title2.setText("Last name");
            //message
            TextView message2 = view2.findViewById(R.id.screen_custom_alert_message);
            message2.setText("Please input your last name, and click SAVE if you're done. Click CANCEL if you don't want to edit something.");
            //loading
            TextView loadingText = view2.findViewById(R.id.screen_custom_alert_loadingText);
            loadingText.setVisibility(View.GONE);
            //top image
            AppCompatImageView imageViewCompat2 = view2.findViewById(R.id.appCompatImageView);
            imageViewCompat2.setVisibility(View.VISIBLE);
            imageViewCompat2.setImageDrawable(getDrawable(R.drawable.dialog_details_borders));
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
            editText.setVisibility(View.VISIBLE);
            editText.setHint("Last name..");
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
                        if (s.length() >= 2) {
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
                    if (editText.getText().toString().equals("") || editText.getText().toString() == null) {
                        count = 0;
                        Toast.makeText(user_profile_account_edit.this, "Oops. You made no entry! your Last name was expected", Toast.LENGTH_SHORT).show();
                    } else if (editText.getText().toString().equals(before_last)) {
                        count = 1;
                        reg_last.setText(editText.getText().toString());
                        Toast.makeText(user_profile_account_edit.this, "Oops. We expecting a different name, but you entered the same value.", Toast.LENGTH_SHORT).show();
                    } else if (editText.length() < 2) {
                        count = 0;
                        Toast.makeText(user_profile_account_edit.this, "Oops. Input must have 2 characters", Toast.LENGTH_SHORT).show();
                    } else {

                        reg_last.setText(editText.getText().toString());
                        Toast.makeText(user_profile_account_edit.this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                        count = 1;
                        Map<String, Object> data = new HashMap<>();
                        data.put("lastName", editText.getText().toString());
                        firebaseFirestore.collection("User")
                                .document(firebaseUser.getUid())
                                .set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(user_profile_account_edit.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                    if (count != 0) {
                        alert3.dismiss();
                    } else {

                    }
                }
            });
        } else if (alertCaller.equals("gender")) {

            String[] gender = {"Male", "Female", "Other"};
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            View view = View.inflate(this, R.layout.pet_add_gender_dialog, null);
            MaterialButton done, cancel;

            TextView message, title;
            message = view.findViewById(R.id.pet_gender_dialog_message);
            title = view.findViewById(R.id.pet_gender_dialog_title);

            message.setText("Please select your gender");
            title.setText("Set Gender");
            done = view.findViewById(R.id.gender_dialog_btn_done);
            cancel = view.findViewById(R.id.gender_dialog_btn_cancel);

            NumberPicker picker = view.findViewById(R.id.gender_numberPicker);
            picker.setMinValue(0);
            picker.setMaxValue(gender.length - 1);
            picker.setDisplayedValues(gender);
            picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            picker.setWrapSelectorWheel(false);
            picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                    genderHolder = String.valueOf(newVal);
                }
            });
            builder.setView(view);
            android.app.AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (genderHolder == "" || genderHolder == null) {
                        genderHolder = "0";
                    }
                    reg_gender.setText(gender[Integer.parseInt(genderHolder)]);
                    Map<String, Object> data = new HashMap<>();
                    data.put("gender", gender[Integer.parseInt(genderHolder)]);
                    firebaseFirestore.collection("User")
                            .document(firebaseUser.getUid())
                            .set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(user_profile_account_edit.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });

                    Toast.makeText(user_profile_account_edit.this, gender[Integer.parseInt(genderHolder)], Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            genderHolder = "";
        } else if (alertCaller.equals("birthday")) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            View view = View.inflate(this, R.layout.pet_add_birthday_dialog, null);
            DatePicker datePicker = view.findViewById(R.id.datePicker);
            datePicker.setMaxDate(System.currentTimeMillis());
            builder.setView(view);
            MaterialButton cancel, save;
            TextView message, title;
            title = view.findViewById(R.id.pet_add_birthday_set_title);
            title.setText("Set Birthday");
            message = view.findViewById(R.id.pet_add_birthday_set_message);
            message.setText("Please set your exact birthday");
            cancel = view.findViewById(R.id.birthday_dialog_btn_cancel);
            save = view.findViewById(R.id.birthday_dialog_btn_done);
            android.app.AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            save.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    int day = datePicker.getDayOfMonth();
                    int month = datePicker.getMonth();
                    int year = datePicker.getYear();
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);

                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    String formatedDate = sdf.format(calendar.getTime());
                    if (formatedDate.equals(before_birthday)) {
                        count = 1;
                        Toast.makeText(user_profile_account_edit.this, "Expected new birthday value", Toast.LENGTH_SHORT).show();
                    } else {
                        count = 1;
                        Map<String, Object> data = new HashMap<>();
                        data.put("birth", formatedDate);
                        firebaseFirestore.collection("User")
                                .document(firebaseUser.getUid())
                                .set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        reg_birthday.setText(formatedDate);
                                        reg_birthday.setTextColor(ColorStateList.valueOf(Color.parseColor("#8A8888")));
                                        Toast.makeText(user_profile_account_edit.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }


                    reg_birthday.setTextColor(ColorStateList.valueOf(Color.BLACK));
                    Toast.makeText(user_profile_account_edit.this, formatedDate, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } else if (alertCaller.equals("address")) {
            AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
            View view2 = View.inflate(this, R.layout.screen_custom_alert, null);
            builder3.setCancelable(false);
            //title
            TextView title2 = view2.findViewById(R.id.screen_custom_alert_title);
            title2.setText("User Address");
            //message
            TextView message2 = view2.findViewById(R.id.screen_custom_alert_message);
            message2.setText("Please input your Address, and click SAVE if you're done. Click CANCEL if you don't want to edit something.");
            //loading
            TextView loadingText = view2.findViewById(R.id.screen_custom_alert_loadingText);
            loadingText.setVisibility(View.GONE);
            //top image
            AppCompatImageView imageViewCompat2 = view2.findViewById(R.id.appCompatImageView);
            imageViewCompat2.setVisibility(View.VISIBLE);
            imageViewCompat2.setImageDrawable(getDrawable(R.drawable.dialog_address_borders));
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
            editText.setVisibility(View.VISIBLE);
            editText.setHint("Address..");
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
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
                        Toast.makeText(user_profile_account_edit.this, "Oops. You made no entry! your current Address was expected", Toast.LENGTH_SHORT).show();
                    } else if (editText.getText().toString().equals(before_address)) {
                        count = 1;
                        reg_address.setText(editText.getText().toString());
                        Toast.makeText(user_profile_account_edit.this, "Oops. We expecting a different info, but you entered the same value.", Toast.LENGTH_SHORT).show();
                    } else {
                        reg_address.setText(editText.getText().toString());
                        Toast.makeText(user_profile_account_edit.this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                        count = 1;
                        Map<String, Object> data = new HashMap<>();
                        data.put("address", editText.getText().toString());
                        firebaseFirestore.collection("User")
                                .document(firebaseUser.getUid())
                                .set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        if (roles.contains("Pet Shooter") || roles.contains("Veterinarian")) {
                                            FirebaseFirestore.getInstance().collection("Services")
                                                    .whereEqualTo("shooter_id", FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .get()
                                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                                                            if (list != null) {

                                                                for (DocumentSnapshot s : list) {
                                                                    FirebaseFirestore.getInstance().collection("Services")
                                                                            .document(s.getId())
                                                                            .update("address", editText.getText().toString())
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void unused) {

                                                                                    FirebaseFirestore.getInstance().collection("Search")
                                                                                            .document(s.getId()).update("address", editText.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void unused) {
                                                                                                    count++;
                                                                                                    if (count == list.size()) {
                                                                                                        if (roles.contains("Veterinarian")) {

                                                                                                            before_address = editText.getText().toString();
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            });
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        }
                                                    });
                                        }

                                        if (roles.contains("Pet Breeder") || roles.contains("Veterinarian")) {
                                            gotoAnotherUpdateForItems(editText.getText().toString());
                                            before_address = editText.getText().toString();
                                        }
                                    }
                                });
                    }
                    if (count != 0) {
                        alert3.dismiss();
                    } else {

                    }
                }
            });
        } else if (alertCaller.equals("zip")) {
            AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
            View view2 = View.inflate(this, R.layout.screen_custom_alert, null);
            builder3.setCancelable(false);
            //title
            TextView title2 = view2.findViewById(R.id.screen_custom_alert_title);
            title2.setText("Zip Code / Postal Code");
            //message
            TextView message2 = view2.findViewById(R.id.screen_custom_alert_message);
            message2.setText("Please input your Zip Code/ Postal Code, and click SAVE if you're done. Click CANCEL if you don't want to edit something.");
            //loading
            TextView loadingText = view2.findViewById(R.id.screen_custom_alert_loadingText);
            loadingText.setVisibility(View.GONE);
            //top image
            AppCompatImageView imageViewCompat2 = view2.findViewById(R.id.appCompatImageView);
            imageViewCompat2.setVisibility(View.VISIBLE);
            imageViewCompat2.setImageDrawable(getDrawable(R.drawable.dialog_zip_borders));
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
            editText.setVisibility(View.VISIBLE);
            editText.setHint("Zip code/Postal code..");
            int maxLength = 4;
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
                public void onTextChanged(CharSequence s, int start, int before, int c) {
                    if (s.length() != 0) {
                        if (s.length() != 4) {
                            count = 0;
                        } else {
                            count = 1;
                            valid.setVisibility(View.VISIBLE);
                        }
                        clear.setVisibility(View.VISIBLE);
                        clear.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                editText.getText().clear();
                            }
                        });
                    } else {
                        valid.setVisibility(View.VISIBLE);
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
                        Toast.makeText(user_profile_account_edit.this, "Oops. You made no entry! a Zip code/Postal Code was expected", Toast.LENGTH_SHORT).show();
                    } else if (editText.getText().toString().equals(before_zip)) {
                        count = 1;

                        reg_zip.setText(editText.getText().toString());
                        Toast.makeText(user_profile_account_edit.this, "Oops. We expecting a different info, but you entered the same value.", Toast.LENGTH_SHORT).show();
                    } else if (editText.length() != 4) {
                        count = 0;
                        Toast.makeText(user_profile_account_edit.this, "Oops. Input must have 4 numbers.", Toast.LENGTH_SHORT).show();
                    } else {
                        reg_zip.setText(editText.getText().toString());
                        Toast.makeText(user_profile_account_edit.this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                        count = 1;
                        Map<String, Object> data = new HashMap<>();
                        data.put("zipCode", editText.getText().toString());
                        firebaseFirestore.collection("User")
                                .document(firebaseUser.getUid())
                                .set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(user_profile_account_edit.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                    if (count != 0) {
                        alert3.dismiss();
                    } else {

                    }
                }
            });
        } else {
            Toast.makeText(this, "Cannot open a dialog", Toast.LENGTH_SHORT).show();
        }
    }

    private void gotoAnotherUpdateForItems(String toString) {
        if (roles.contains("Pet breeder")) {
            FirebaseFirestore.getInstance().collection("Pet")
                    .whereEqualTo("displayFor", "forSale")
                    .whereEqualTo("pet_breeder", FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            if (list != null) {
                                for (DocumentSnapshot s : list) {
                                    FirebaseFirestore.getInstance().collection("Pet")
                                            .document(s.getId()).update("address", toString).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    FirebaseFirestore.getInstance().collection("Search")
                                                            .document(s.getId()).update("address", toString).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    count++;
                                                                    if (count == list.size()) {
                                                                        Toast.makeText(user_profile_account_edit.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                }
                                            });

                                }
                            }

                        }
                    });
        } else if (roles.contains("Veterinarian")) {
            FirebaseFirestore.getInstance().collection("Pet")
                    .whereEqualTo("displayFor", "forProducts")
                    .whereEqualTo("vet_id", FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            if (list != null) {
                                for (DocumentSnapshot s : list) {
                                    FirebaseFirestore.getInstance().collection("Pet")
                                            .document(s.getId())
                                            .update("address", toString).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    FirebaseFirestore.getInstance().collection("Search")
                                                            .document(s.getId())
                                                            .update("address", toString).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    count++;
                                                                    if (count == list.size()) {
                                                                        Toast.makeText(user_profile_account_edit.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                                                                    }
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


    private static final int REQUEST_CODE_PROFILE_IMAGE = 99;
    private static final int REQUEST_CODE_BACKGROUND_IMAGE = 100;

    private void imagePermissionBackground() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_BACKGROUND_IMAGE);
    }


    private void imagePermission() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_PROFILE_IMAGE);
    }

    String resultsForCover = "";
    String results = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 99) {
            Uri imageUri = data.getData();
            cropImage(imageUri, imageView, "profile");
        } else if (resultCode == RESULT_OK && requestCode == 100) {
            Uri imageUri = data.getData();
            cropImage(imageUri, imageBackground, "background");
        } else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            Uri croppedUri = UCrop.getOutput(data);
            // Use the cropped image URI as needed
            if (croppedUri != null) {
                if (currentImageView != null && types != null) {
                    if (types.equals("profile")) {
                        results = croppedUri.toString();
                        uploadProfileImage();
                    } else {
                        resultsForCover = croppedUri.toString();
                        uploadBackground();
                    }

                }

            }
        }
    }


    private ImageView currentImageView;
    private String types;

    private void cropImage(Uri sourceUri, ImageView imageView, String type) {
        currentImageView = imageView;
        types = type;
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "cropped"));
        UCrop uCrop = UCrop.of(sourceUri, destinationUri);
        uCrop.start(this);
    }

    @SuppressLint("SetTextI18n")
    private void uploadProfileImage() {
        imgsUri = Uri.parse(results);
        imgUriCover = Uri.parse(resultsForCover);
        //for breeder

        AlertDialog.Builder builder2 = new AlertDialog.Builder(user_profile_account_edit.this);
        builder2.setCancelable(false);
        View view = View.inflate(user_profile_account_edit.this, R.layout.screen_custom_alert, null);
        //title
        TextView title = view.findViewById(R.id.screen_custom_alert_title);
        //loading text
        TextView loadingText = view.findViewById(R.id.screen_custom_alert_loadingText);
        loadingText.setVisibility(View.VISIBLE);
        //gif
        GifImageView gif = view.findViewById(R.id.screen_custom_alert_gif);
        gif.setVisibility(View.GONE);
        //header image
        AppCompatImageView imageViewCompat = view.findViewById(R.id.appCompatImageView);
        imageViewCompat.setVisibility(View.GONE);
        //message
        TextView message = view.findViewById(R.id.screen_custom_alert_message);

        title.setVisibility(View.GONE);
        message.setVisibility(View.GONE);
        //button

        builder2.setView(view);
        AlertDialog alert2 = builder2.create();
        alert2.show();
        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        StorageReference imageName = fileRefBreeder.child("profile");

        imageName.putFile(imgsUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                builder2.setView(view);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alert2.dismiss();
                        updateButton.setVisibility(View.GONE);
                    }
                }, 2000);
                imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageProf = uri.toString();
                        //for user profile
                        Map<String, Object> data = new HashMap<>();
                        data.put("image", uri.toString());
                        //for shop profile

                        documentReference.set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Map<String, Object> shopImage = new HashMap<>();
                                    shopImage.put("profImage", uri.toString());
                                    if (roles.contains("Pet Owner") && roles.size() == 1) {
                                        Toast.makeText(user_profile_account_edit.this, "Successfully Changed.", Toast.LENGTH_SHORT).show();

                                    } else {
                                        DocumentReference ref = FirebaseFirestore.getInstance()
                                                .collection("Shop")
                                                .document(firebaseUser.getUid());
                                        ref.set(shopImage, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(user_profile_account_edit.this, "Successfully Changed.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    Toast.makeText(user_profile_account_edit.this, "Cannot make any changes", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    public void uploadBackground() {

        AlertDialog.Builder builder2 = new AlertDialog.Builder(user_profile_account_edit.this);
        builder2.setCancelable(false);
        View view = View.inflate(user_profile_account_edit.this, R.layout.screen_custom_alert, null);
        //title
        TextView title = view.findViewById(R.id.screen_custom_alert_title);
        //loading text
        TextView loadingText = view.findViewById(R.id.screen_custom_alert_loadingText);
        loadingText.setVisibility(View.VISIBLE);
        //gif
        GifImageView gif = view.findViewById(R.id.screen_custom_alert_gif);
        gif.setVisibility(View.GONE);
        //header image
        AppCompatImageView imageViewCompat = view.findViewById(R.id.appCompatImageView);
        imageViewCompat.setVisibility(View.GONE);
        //message
        TextView message = view.findViewById(R.id.screen_custom_alert_message);

        title.setVisibility(View.GONE);
        message.setVisibility(View.GONE);
        //button

        builder2.setView(view);
        AlertDialog alert2 = builder2.create();
        alert2.show();
        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        imgUriCover = Uri.parse(resultsForCover);
        StorageReference imageCoverName = fileRefBreederBackground.child("background");
        imageCoverName.putFile(imgUriCover).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                builder2.setView(view);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateButton.setVisibility(View.GONE);
                        alert2.dismiss();
                    }
                }, 2000);

                imageCoverName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageCover = uri.toString();
                        Map<String, Object> data = new HashMap<>();
                        data.put("backgroundImage", uri.toString());
                        documentReference.set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    if (roles.contains("Pet Owner") && roles.size() == 1) {
                                        Toast.makeText(user_profile_account_edit.this, "Successfully Changed.", Toast.LENGTH_SHORT).show();

                                    } else {
                                        DocumentReference ref = FirebaseFirestore.getInstance()
                                                .collection("Shop")
                                                .document(firebaseUser.getUid());
                                        ref.set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(user_profile_account_edit.this, "Successfully Changed.", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }

                                }
                            }
                        });
                    }
                });
            }
        });

    }


    private class ClickClass implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.first_layout:
                    alertCaller = "firstName";
                    dialogForAll(alertCaller);
                    break;
                case R.id.middle_layout:
                    alertCaller = "middleName";
                    dialogForAll(alertCaller);
                    break;
                case R.id.last_layout:
                    alertCaller = "lastName";
                    dialogForAll(alertCaller);
                    break;
                case R.id.birthday_layout:
                    alertCaller = "birthday";
                    dialogForAll(alertCaller);
                    break;
                case R.id.gender_layout:
                    alertCaller = "gender";
                    dialogForAll(alertCaller);
                    break;
                case R.id.address_layout:
                    alertCaller = "address";
                    dialogForAll(alertCaller);
                    break;
                case R.id.zip_layout:
                    alertCaller = "zip";
                    dialogForAll(alertCaller);
                    break;

            }

        }
    }
}



