package com.example.hi_breed.shop;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
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
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.BaseActivity;
import com.example.hi_breed.classesFile.ShopClass;
import com.example.hi_breed.phoneAccess.phone_UncropperActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class shop_edit_profile extends BaseActivity {

    Uri imgsUriProf,imgUriCover;
    ActivityResultLauncher<String> cropImage,cropImageCover;
    FirebaseUser firebaseUser;

    ImageView imageShopProfile,imageShopBackground;
    CardView imageEdit;
    LinearLayout backLayout;
    RelativeLayout shopNameLayout,bioLayout,genderLayout,birthdayLayout;
    TextView sName, bio, genders, birthday;
    // for date
    DatePickerDialog datePickerDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.owner_shop_edit_profile);
        //imageView
        imageShopBackground = findViewById(R.id.view_shop_coverImageID);
        imageShopProfile = findViewById(R.id.view_shop_Profile);


        //CardView
        imageEdit = findViewById(R.id.cardView_shop_Profile);


        //TextView
        sName = findViewById(R.id.shop_edit_shopName);
        bio = findViewById(R.id.shop_edit_Bio);
        genders = findViewById(R.id.shop_edit_Gender);
        birthday = findViewById(R.id.shop_edit_birthday);

        //Layout
        //Relative Layout
        shopNameLayout = findViewById(R.id.shopnameLayout);
        bioLayout = findViewById(R.id.bioLayout);
        birthdayLayout = findViewById(R.id.birthdayLayout);
        genderLayout = findViewById(R.id.genderLayout);


       //Directing to specific Path in Firebase

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser!=null)
            getUser();

        cropImage = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            Intent intent = new Intent(this.getApplicationContext(), phone_UncropperActivity.class);

            if(result!=null){
                intent.putExtra("SendingData", result.toString());
                startActivityForResult(intent,99);

            }
            else{

                Toast.makeText(this.getApplicationContext(),"No changes",Toast.LENGTH_SHORT).show();
            }
        });

        cropImageCover = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            Intent intent = new Intent(this.getApplicationContext(), phone_UncropperActivity.class);

            if(result!=null){
                intent.putExtra("SendingData", result.toString());
                startActivityForResult(intent,100);

            }
            else{

                Toast.makeText(this.getApplicationContext(),"No changes",Toast.LENGTH_SHORT).show();
            }
        });

        //OnClickListener
        //Linear Layout
        backLayout = findViewById(R.id.backLayoutShopEdit);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   shop_edit_profile.this.onBackPressed();
            }
        });
        shopNameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShopName();
            }
        });
        bioLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBioDialog();
            }
        });

        birthdayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              dateDialog();
            }
        });
        genderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGenderDialog();
            }
        });
        imageShopBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePermissionBackground();
            }
        });
        imageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePermissionProfile();
            }
        });


    }

    private void getUser() {
        FirebaseFirestore.getInstance().collection("Shop")
                .document(firebaseUser.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot s = task.getResult();
                                ShopClass shop = s.toObject(ShopClass.class);
                                if(shop!=null){

                                    //If the image is not yet set for profile
                                    Uri uri = Uri.parse("android.resource://"+ getPackageName()+"/"+R.drawable.noimage);
                                    //background
                                    if(shop.getBackgroundImage().equals("")){
                                        Glide.with( shop_edit_profile.this)
                                                .load(R.drawable.nobackground)
                                                .fitCenter()
                                                .into(imageShopProfile);;
                                    }
                                    else{
                                        Glide.with( shop_edit_profile.this)
                                                .load(shop.getBackgroundImage())
                                                .into(imageShopBackground);
                                    }
                                    //profile
                                    if(shop.getProfImage().equals("")){
                                        Glide.with( shop_edit_profile.this)
                                                .load(uri)
                                                .into(imageShopProfile);
                                    }else{
                                        Glide.with( shop_edit_profile.this)
                                                .load(shop.getProfImage())
                                                .into(imageShopProfile);
                                    }
                                    //bio
                                    if(shop.getBio().equals(""))
                                    {
                                        bio.setText("Set bio");
                                    }
                                    else{
                                        bio.setText(shop.getBio());
                                    }
                                    genders.setText(shop.getGender());
                                    birthday.setText(shop.getBirthday());
                                    sName.setText(shop.getShopName());
                                }
                            }
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void dateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this,R.layout.pet_add_birthday_dialog,null);
        DatePicker datePicker = view.findViewById(R.id.datePicker);
        datePicker.setMaxDate(System.currentTimeMillis());
        builder.setView(view);
        MaterialButton cancel,save;
        TextView pet_add_birthday_set_title = view.findViewById(R.id.pet_add_birthday_set_title);
        TextView pet_add_birthday_set_message = view.findViewById(R.id.pet_add_birthday_set_message);
        pet_add_birthday_set_title.setText("Set birthday");
        pet_add_birthday_set_message.setText("Note: Please input your birthday");
        cancel = view.findViewById(R.id.birthday_dialog_btn_cancel);
        save = view.findViewById(R.id.birthday_dialog_btn_done);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                int   day  = datePicker.getDayOfMonth();
                int   month= datePicker.getMonth();
                int   year = datePicker.getYear();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);

                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                String formatedDate = sdf.format(calendar.getTime());
                birthday.setText(formatedDate);
                update("birthday",formatedDate);
                Toast.makeText(shop_edit_profile.this, formatedDate, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void imagePermissionBackground() {
        Dexter.withContext(shop_edit_profile.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        cropImageCover.launch("image/*");
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(shop_edit_profile.this, "Permission Denied",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private void imagePermissionProfile() {
        Dexter.withContext(shop_edit_profile.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        cropImage.launch("image/*");
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(shop_edit_profile.this, "Permission Denied",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }


    String resultsForProfile="";
    String resultsForCover="";
    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 99 && resultCode == 102) {
            resultsForProfile = data.getStringExtra("CROPS");
            imgsUriProf = data.getData();
            if (resultsForProfile != null) {
                imgsUriProf = Uri.parse(resultsForProfile);
            }
            imageShopProfile.setImageURI(imgsUriProf);
            if(imgsUriProf != null){
                AlertDialog.Builder builder2 = new AlertDialog.Builder(shop_edit_profile.this);
                builder2.setCancelable(false);
                View view = View.inflate(shop_edit_profile.this,R.layout.screen_custom_alert,null);
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
                imageViewCompat.setImageDrawable(getDrawable(R.drawable.dialog_pet_borders));
                //message
                TextView message = view.findViewById(R.id.screen_custom_alert_message);
                title.setText("Updating your profile picture");
                message.setVisibility(View.VISIBLE);
                message.setText("Please wait a bit. We are updating your profile picture");
                builder2.setView(view);
                AlertDialog alert2 = builder2.create();
                alert2.show();
                alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                uploadToFirebaseStorage(imgsUriProf,alert2);

            }
            else{
                Toast.makeText(this,"Please upload one at a time.",Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 100 && resultCode == 102){
            resultsForCover = data.getStringExtra("CROPS");
            imgUriCover = data.getData();
            if (resultsForCover != null) {
                imgUriCover = Uri.parse(resultsForCover);
            }
            imageShopBackground.setImageURI(imgUriCover);
            if(imgUriCover != null) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(shop_edit_profile.this);
                builder2.setCancelable(false);
                View view = View.inflate(shop_edit_profile.this, R.layout.screen_custom_alert, null);
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
                imageViewCompat.setImageDrawable(getDrawable(R.drawable.dialog_pet_borders));
                //message
                TextView message = view.findViewById(R.id.screen_custom_alert_message);
                title.setText("Updating your cover picture");
                message.setVisibility(View.VISIBLE);
                message.setText("Please wait a bit. We are updating your cover picture");
                builder2.setView(view);
                AlertDialog alert2 = builder2.create();
                alert2.show();
                alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                uploadToFirebaseStorageBackGround(imgUriCover, alert2);
            }
        }

    }

    private void uploadToFirebaseStorageBackGround(Uri imgUriCover, AlertDialog alert2) {
        String userID="";
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            userID = firebaseUser.getUid();
        }

        StorageReference ImageFolder = FirebaseStorage.getInstance().getReference("User/"+userID+"/"+"Profile Background/");


        StorageReference imageName = ImageFolder.child("background");
        imageName.putFile(imgUriCover).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uris) {
                        String url = String.valueOf(uris);
                        updateImages("backgroundImage",url,alert2);
                    }
                });
            }
        });



    }

    private void uploadToFirebaseStorage(Uri imgsUriProf,AlertDialog alert2) {
        String userID="";
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            userID = firebaseUser.getUid();
        }

        StorageReference ImageFolder = FirebaseStorage.getInstance().getReference("User/"+userID+"/"+"Profile Picture/");


            StorageReference imageName = ImageFolder.child("profile");
            imageName.putFile(imgsUriProf).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uris) {
                            String url = String.valueOf(uris);
                            updateImages("profImage",url,alert2);
                        }
                    });
                }
            });


    }


    int count = 0;
    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    private void showShopName() {

        AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
        View view2 = View.inflate(this,R.layout.screen_custom_alert,null);
        builder3.setCancelable(false);
        //title
        TextView title2 = view2.findViewById(R.id.screen_custom_alert_title);
        title2.setText("Set Shop name");
        //message
        TextView message2 = view2.findViewById(R.id.screen_custom_alert_message);
        message2.setText("Please input your wanted name");
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
        MaterialButton cancel,okay;
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
        editText.setHint("Shop name . .");

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
                if(s.length()!=0){
                    if(s.length() >= 2){
                        valid.setVisibility(View.VISIBLE);
                    }
                    else {
                        valid.setVisibility(View.GONE);
                    }
                    clear.setVisibility(View.VISIBLE);
                    clear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editText.getText().clear();
                        }
                    });
                }
                else{

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
                if(editText.getText().toString().equals("") || editText.getText().toString().equals(0) || editText.getText().toString() == null){
                    count = 0;
                    Toast.makeText(shop_edit_profile.this, "Oops. You made no entry! a name was expected", Toast.LENGTH_SHORT).show();
                }
                else{
                    sName.setText(editText.getText().toString());
                    update("shopName",editText.getText().toString());
                    Toast.makeText(shop_edit_profile.this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                    count = 1;
                }
                if(count != 0){
                    alert3.dismiss();
                }
                else{

                }
            }
        });

    }

    @SuppressLint({"ResourceAsColor", "ResourceType", "SetTextI18n", "UseCompatLoadingForDrawables"})
    private void showBioDialog() {

        AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
        View view2 = View.inflate(this,R.layout.screen_custom_alert,null);
        builder3.setCancelable(false);
        //title
        TextView title2 = view2.findViewById(R.id.screen_custom_alert_title);
        title2.setText("Set a bio");
        //message
        TextView message2 = view2.findViewById(R.id.screen_custom_alert_message);
        message2.setText("Please a bio to express who you are.");
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
        MaterialButton cancel,okay;
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
        editText.setHint("Shop name . .");

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
                if(s.length()!=0){
                    if(s.length() >= 2){
                        valid.setVisibility(View.VISIBLE);
                    }
                    else {
                        valid.setVisibility(View.GONE);
                    }
                    clear.setVisibility(View.VISIBLE);
                    clear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editText.getText().clear();
                        }
                    });
                }
                else{

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
                if(editText.getText().toString().equals("") || editText.getText().toString().equals(0) || editText.getText().toString() == null){
                    count = 0;
                    Toast.makeText(shop_edit_profile.this, "Oops. You made no entry! a name was expected", Toast.LENGTH_SHORT).show();
                }
                else{
                    bio.setText(editText.getText().toString());
                    update("bio",editText.getText().toString());
                    Toast.makeText(shop_edit_profile.this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                    count = 1;
                }
                if(count != 0){
                    alert3.dismiss();
                }
                else{

                }
            }
        });

    }
    String genderHolder;
    @SuppressLint("SetTextI18n")
    private void showGenderDialog() {
        String[] gender = {"Male","Female","Other"};
        AlertDialog.Builder builder =new AlertDialog.Builder(shop_edit_profile.this);
        View view = View.inflate(shop_edit_profile.this,R.layout.pet_add_gender_dialog,null);
        MaterialButton done,cancel;
        done = view.findViewById(R.id.gender_dialog_btn_done);
        cancel = view.findViewById(R.id.gender_dialog_btn_cancel);
        TextView pet_gender_dialog_title = view.findViewById(R.id.pet_gender_dialog_title);
        TextView pet_gender_dialog_message = view.findViewById(R.id.pet_gender_dialog_message);
        pet_gender_dialog_title.setText("Set gender");
        pet_gender_dialog_message.setText("Select your gender");
        NumberPicker picker = view.findViewById(R.id.gender_numberPicker);
        picker.setMinValue(0);
        picker.setMaxValue(gender.length -1 );
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
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(genderHolder == ""|| genderHolder == null){
                    genderHolder = "0";
                }
                genders.setText(gender[Integer.parseInt(genderHolder)]);
                update("gender",gender[Integer.parseInt(genderHolder)]);
                Toast.makeText(shop_edit_profile.this, gender[Integer.parseInt(genderHolder)], Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        genderHolder="";
    }


    private void update(String field,String value)
    {
        Map<String,Object> map = new HashMap<>();
        map.put(field,value);
       FirebaseFirestore.getInstance().collection("Shop")
               .document(firebaseUser.getUid()).set(map, SetOptions.merge())
               .addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if(task.isSuccessful()){

                            }
                   }
               });
       if(field.equals("gender")){
           FirebaseFirestore.getInstance().collection("User")
                   .document(firebaseUser.getUid()).set(map, SetOptions.merge())
                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful()){

                           }
                       }
                   });
       }
       if(field.equals("birthday")){
           Map<String,Object> mapBirth = new HashMap<>();
           mapBirth.put("birth",value);
           FirebaseFirestore.getInstance().collection("User")
                   .document(firebaseUser.getUid()).set(mapBirth, SetOptions.merge())
                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful()){

                          }
                       }
                   });
       }
        Toast.makeText(this, "Successfully Updated", Toast.LENGTH_SHORT).show();
    }
    private void updateImages(String field,String value,AlertDialog alertDialog){


        Map<String,Object> map = new HashMap<>();
        map.put(field,value);
        FirebaseFirestore.getInstance().collection("Shop").document(firebaseUser.getUid()).set(map,SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                        }
                    }
                });
        if(field.equals("backgroundImage")){
            FirebaseFirestore.getInstance().collection("User")
                    .document(firebaseUser.getUid()).set(map, SetOptions.merge())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                            }
                        }
                    });
        }

        if(field.equals("profImage")){
            Map<String,Object> mapImage = new HashMap<>();
            mapImage.put("image",value);
            FirebaseFirestore.getInstance().collection("User")
                    .document(firebaseUser.getUid()).set(mapImage, SetOptions.merge())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                            }
                        }
                    });
        }

        alertDialog.dismiss();
        Toast.makeText(this, "Successfully Updated", Toast.LENGTH_SHORT).show();
    }
    private void backButtonDialog() {


    }

}