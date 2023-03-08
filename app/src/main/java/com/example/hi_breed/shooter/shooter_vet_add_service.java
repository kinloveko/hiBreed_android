package com.example.hi_breed.shooter;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dpro.widgets.WeekdaysPicker;
import com.example.hi_breed.R;
import com.example.hi_breed.adapter.petImagesRecyclerAdapter;
import com.example.hi_breed.classesFile.BaseActivity;
import com.example.hi_breed.classesFile.PhotoClass;
import com.example.hi_breed.classesFile.item;
import com.example.hi_breed.classesFile.service_class;
import com.example.hi_breed.shop.user_breeder_shop_panel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import pl.droidsonroids.gif.GifImageView;

public class shooter_vet_add_service extends BaseActivity implements petImagesRecyclerAdapter.CountOfImagesWhenRemovedPet,
        petImagesRecyclerAdapter.itemClickListenerPet{

    private RecyclerView service_images_view;
    private WeekdaysPicker widget;
    private LinearLayout backLayoutService;
    private Button Service_add_create_Button;
    private CardView servicePhotoCardView;
    private TextView availabilityTextView;
    private TextView servicePriceTextView;
    private TextView serviceDescCountID;
    private EditText serviceDescEdit;
    private ImageView serviceDescClearButton;
    private RelativeLayout availabilityLayout;
    private RelativeLayout priceLayout;
    private petImagesRecyclerAdapter adapter;
    private ArrayList<Uri> uri = new ArrayList<>();
    private Uri imageUri;
    private  String address;
    private String price;
    private String name;
    private String picture;
    private TextView petNamecountID;
    private TextInputEditText petNameEdit;
    private RelativeLayout serviceNameLayout;
    private ImageView petNameclearButton;
    private List<String> roles = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shooter_add_service);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        }
        else
        {
            window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.setStatusBarColor(Color.parseColor("#e28743"));
        }

        //relative layout
        serviceNameLayout = findViewById(R.id.serviceNameLayout);
        priceLayout = findViewById(R.id.priceLayout);
        availabilityLayout = findViewById(R.id.availabilityLayout);
        //editText
        serviceDescEdit = findViewById(R.id.serviceDescEdit);
        petNameEdit = findViewById(R.id.petNameEdit);
        //imageview for clearing the text in description
        serviceDescClearButton = findViewById(R.id.serviceDescClearButton);
        serviceDescCountID = findViewById(R.id.serviceDescCountID);
        //TextView for output
        availabilityTextView = findViewById(R.id.availabilityTextView);
        servicePriceTextView = findViewById(R.id.servicePriceTextView);
        petNamecountID = findViewById(R.id.petNamecountID);

        //ImageView
        petNameclearButton = findViewById(R.id.petNameclearButton);

        //CardView
        servicePhotoCardView = findViewById(R.id.servicePhotoCardView);
        //Button
        Service_add_create_Button = findViewById(R.id.Service_add_create_Button);
        //Linear Layout
        backLayoutService = findViewById(R.id.backLayoutService);

        //Recycler view
        service_images_view = findViewById(R.id.service_images_view);
        adapter = new petImagesRecyclerAdapter(uri,getApplicationContext(),this, this);
        service_images_view.setLayoutManager(new GridLayoutManager(this,4));
        service_images_view.setAdapter(adapter);

        backLayoutService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shooter_vet_add_service.this.onBackPressed();
                finish();
            }
        });


        FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                             if(task.isSuccessful()){
                                 DocumentSnapshot s = task.getResult();
                                 address = s.getString("address");
                                 name = s.getString("firstName") +" " +s.getString("middleName")+" "+s.getString("lastName");
                                 picture = s.getString("image");
                                   roles.addAll((List<String>)s.get("role"));
                                 if(roles.contains("Veterinarian")){
                                     serviceNameLayout.setVisibility(View.VISIBLE);
                                 }
                                 else{
                                     serviceNameLayout.setVisibility(View.GONE);
                                 }
                             }
                    }
                });


        //widget
        widget  = (WeekdaysPicker) findViewById(R.id.weekdays);
        // Integer = CalendarDay, Boolean = selected
        LinkedHashMap<Integer, Boolean> mp = new LinkedHashMap<>();
        mp.put(Calendar.MONDAY, false);
        mp.put(Calendar.TUESDAY, false); //counting
        mp.put(Calendar.WEDNESDAY, false);
        mp.put(Calendar.THURSDAY, false);
        mp.put(Calendar.FRIDAY, false);
        mp.put(Calendar.SATURDAY, false);
        mp.put(Calendar.SUNDAY, false);//For duplicated values, the first one is counting, but the last one is updating the selected value
        //Add the custom map
        widget.setCustomDays(mp);


        priceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPrice();
            }
        });

        availabilityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimeFrom();
            }
        });

        if(serviceDescEdit.getText() !=null || serviceDescEdit.getText().equals("")) {
            serviceDescEdit.addTextChangedListener(petDescTextEditorWatcher);
        }
        if(petNameEdit.getText() != null || petNameEdit.getText().equals("")){
            petNameEdit.addTextChangedListener(petNameTextEditorWatcher);
        }

        servicePhotoCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePermission();
            }
        });
        Service_add_create_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInput();
            }
        });

    }

    @SuppressLint("ObsoleteSdkInt")
    private void imagePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        }
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),1);
    }
    private int countOfImages = 0;
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && null != data){

            //this part is for to get multiple images
            if(data.getClipData() != null){

                countOfImages = data.getClipData().getItemCount();
                for(int j = 0; j<countOfImages; j++){

                    if(uri.size() < 5){
                        imageUri = data.getClipData().getItemAt(j).getUri();
                        service_images_view.setVisibility(View.VISIBLE);
                        uri.add(imageUri);

                    }else{
                        Toast.makeText(this,"Not allowed to pick more than 5 images",Toast.LENGTH_SHORT).show();
                    }
                }
                //then notify the adapter
                adapter.notifyDataSetChanged();
            }else{
                //this is for single images
                if(uri.size() <5){
                    imageUri =data.getData();
                    uri.add(imageUri);
                    service_images_view.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(this,"Not allowed to pick more than 5 images",Toast.LENGTH_SHORT).show();
                }
            }
           adapter.notifyDataSetChanged();
        }
        else{
            //this code is for if user not picked any image
            Toast.makeText(this,"You Haven't Pick any image",Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    private void checkInput() {
        String description = serviceDescEdit.getText().toString();


        String avail = availabilityTextView.getText().toString();
        List<String> avails = Arrays.asList(froms,tos);
        String name = petNameEdit.getText().toString();


        if(uri.size() == 0){
            Toast.makeText(this, "Please provide a picture of your successful service", Toast.LENGTH_SHORT).show();
            return;
        }

        if(roles.contains("Veterinarian")){
            if(name.isEmpty()){
                Toast.makeText(this, "Please input a specific name of your service", Toast.LENGTH_SHORT).show();
                petNameEdit.requestFocus();
                return;
            }
        }

        if(description.isEmpty()){
            Toast.makeText(this, "Please input a description", Toast.LENGTH_SHORT).show();
            serviceDescEdit.requestFocus();
            return;
        }

        if(widget.noDaySelected()){
            Toast.makeText(this, "Please input your schedule", Toast.LENGTH_SHORT).show();
            widget.requestFocus();
            return;
        }

        if(avail.equals("Availability")){
            Toast.makeText(this, "Please input your time availability", Toast.LENGTH_SHORT).show();
            availabilityTextView.requestFocus();
            return;
        }

        if(servicePriceTextView.getText().toString().equals("Price")){
            Toast.makeText(this, "Please input your service fee", Toast.LENGTH_SHORT).show();
            servicePriceTextView.requestFocus();
            return;
        }

        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setCancelable(false);
        View view = View.inflate(this,R.layout.screen_custom_alert,null);
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
        imageViewCompat.setImageDrawable(getDrawable(R.drawable.dialog_details_borders));
        //message
        TextView message = view.findViewById(R.id.screen_custom_alert_message);
        title.setText("Creating your service info");
        message.setVisibility(View.VISIBLE);
        message.setText("Please wait a bit. We are saving your service info");
        builder2.setView(view);
        AlertDialog alert2 = builder2.create();
        alert2.show();
        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        List<String> schedule = widget.getSelectedDaysText();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = UUID.randomUUID().toString();

        if(roles.contains("Veterinarian")){
            service_class service = new service_class(id,name,description,schedule,avails,address,price,null,user.getUid(),"Veterinarian Service","Service", Timestamp.now(),true);
            FirebaseFirestore.getInstance().collection("User").document(user.getUid())
                    .collection("Services")
                    .document(id)
                    .set(service, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                FirebaseFirestore.getInstance().collection("Shop")
                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .collection("Services")
                                        .document(id)
                                        .set(service,SetOptions.merge())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    FirebaseFirestore.getInstance()
                                                            .collection("Services")
                                                            .document(id).set(service,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    uploadToService(id,user,alert2);
                                                                }
                                                            });
                                                }
                                            }
                                        });
                            }
                        }
                    });


        }
        else{
            service_class service = new service_class(id,"Dog Shooter",description,schedule,avails,address,price,null,user.getUid(),"Shooter Service","Service", Timestamp.now(),true);
            FirebaseFirestore.getInstance().collection("User").document(user.getUid())
                    .collection("Services")
                    .document(id)
                    .set(service, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                FirebaseFirestore.getInstance().collection("Shop")
                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .collection("Services")
                                        .document(id)
                                        .set(service,SetOptions.merge())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    FirebaseFirestore.getInstance()
                                                            .collection("Services")
                                                            .document(id).set(service,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    uploadToService(id,user,alert2);
                                                                }
                                                            });
                                                }
                                            }
                                        });
                            }
                        }
                    });
        }

    }

    private int upload = 0;
    int counterPhotos=0;

    private void uploadToService(String countIDD ,FirebaseUser firebaseUser,AlertDialog dialog) {

        String userID="";
        if (firebaseUser != null) {
            userID = firebaseUser.getUid();
        }
        ArrayList<String> petPhotos = new ArrayList<>();
        StorageReference ImageFolder = FirebaseStorage.getInstance().getReference("User/"+userID+"/"+"Service/"+"Service Photos/"+countIDD);
        for(upload = 0;upload<uri.size();upload++){
            Uri Image = uri.get(upload);
            StorageReference imageName = ImageFolder.child(Image.getLastPathSegment());
            imageName.putFile(Image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uris) {
                            counterPhotos++;
                            String url = String.valueOf(uris);
                            petPhotos.add(url);
                            if(counterPhotos == uri.size()){
                                StringLink(countIDD,dialog,petPhotos);
                            }

                        }
                    });
                }
            });
        }
    }

    private void StringLink(String id, AlertDialog alertDialog,ArrayList<String>petPhotos) {
        ArrayList<String> hashMap = new ArrayList<String>();
        hashMap.addAll(petPhotos);

        PhotoClass photos = new PhotoClass(hashMap);
        FirebaseFirestore.getInstance()
                .collection("Services")
                .document(id)
                .set(photos, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseFirestore.getInstance()
                                .collection("User")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .collection("Services")
                                .document(id)
                                .update("photos",photos,"timestamp", FieldValue.serverTimestamp()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){

                                            FirebaseFirestore.getInstance().collection("Shop")
                                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .collection("Services")
                                                    .document(id)
                                                    .update("photos",photos,"timestamp",FieldValue.serverTimestamp())
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {

                                                            if(roles.contains("Veterinarian")){
                                                                item item = new item(id,FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                                                        price,"Veterinarian Service",petNameEdit.getText().toString(),address,true);

                                                                FirebaseFirestore.getInstance().collection("Search").document(id)
                                                                        .set(item,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                alertDialog.dismiss();
                                                                                AlertDialog.Builder builder2 = new AlertDialog.Builder(shooter_vet_add_service.this);
                                                                                builder2.setCancelable(false);
                                                                                View view = View.inflate(shooter_vet_add_service.this,R.layout.screen_custom_alert,null);
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
                                                                                title.setText("Successfully Created");
                                                                                message.setVisibility(View.VISIBLE);
                                                                                message.setText("Click okay to continue..");
                                                                                LinearLayout buttonLayout = view.findViewById(R.id.screen_custom_alert_buttonLayout);
                                                                                buttonLayout.setVisibility(View.VISIBLE);
                                                                                MaterialButton cancel,okay;
                                                                                cancel = view.findViewById(R.id.screen_custom_dialog_btn_cancel);
                                                                                cancel.setVisibility(View.GONE);
                                                                                okay = view.findViewById(R.id.screen_custom_alert_dialog_btn_done);
                                                                                okay.setText("Okay");
                                                                                builder2.setView(view);
                                                                                AlertDialog alert2 = builder2.create();
                                                                                alert2.show();
                                                                                alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                                                okay.setOnClickListener(new View.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(View v) {
                                                                                        transistion();
                                                                                    }
                                                                                });
                                                                            }
                                                                        });
                                                            }
                                                            else{

                                                                item item = new item(id,FirebaseAuth.getInstance().getCurrentUser().getUid(),price,"Shooter Service","Shooter Service",address,true);

                                                                FirebaseFirestore.getInstance().collection("Search").document(id)
                                                                        .set(item,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                alertDialog.dismiss();
                                                                                AlertDialog.Builder builder2 = new AlertDialog.Builder(shooter_vet_add_service.this);
                                                                                builder2.setCancelable(false);
                                                                                View view = View.inflate(shooter_vet_add_service.this,R.layout.screen_custom_alert,null);
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
                                                                                title.setText("Successfully Created");
                                                                                message.setVisibility(View.VISIBLE);
                                                                                message.setText("Click okay to continue..");
                                                                                LinearLayout buttonLayout = view.findViewById(R.id.screen_custom_alert_buttonLayout);
                                                                                buttonLayout.setVisibility(View.VISIBLE);
                                                                                MaterialButton cancel,okay;
                                                                                cancel = view.findViewById(R.id.screen_custom_dialog_btn_cancel);
                                                                                cancel.setVisibility(View.GONE);
                                                                                okay = view.findViewById(R.id.screen_custom_alert_dialog_btn_done);
                                                                                okay.setText("Okay");
                                                                                builder2.setView(view);
                                                                                AlertDialog alert2 = builder2.create();
                                                                                alert2.show();
                                                                                alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                                                okay.setOnClickListener(new View.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(View v) {
                                                                                        transistion();
                                                                                    }
                                                                                });
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                    }
                });

    }

    private void transistion(){
        Thread mSplashThread = new Thread() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(getApplication(), user_breeder_shop_panel.class);
                shooter_vet_add_service.this.overridePendingTransition(R.drawable.fade_in, R.drawable.fade_out);
                startActivity(intent);
                finish();
            }
        };
        mSplashThread.start();
    }

    String hoursHolder = "";
    String pmAMHolder = "";
    String minutesHolder = "";
    @SuppressLint("SetTextI18n")
    private void setTimeFrom() {

        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        View view = View.inflate(this,R.layout.m_service_time_dialog,null);
        MaterialButton done,cancel;

        done = view.findViewById(R.id.gender_dialog_btn_done);
        done.setText("Proceed");
        cancel = view.findViewById(R.id.gender_dialog_btn_cancel);
        //pm am
        NumberPicker pm_am_numberPicker = view.findViewById(R.id.pm_am_numberPicker);
        String[] pmAM = {"AM","PM"};
        pm_am_numberPicker.setMinValue(0);
        pm_am_numberPicker.setMaxValue(pmAM.length-1);
        pm_am_numberPicker.setDisplayedValues(pmAM);
        pm_am_numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        pm_am_numberPicker.setWrapSelectorWheel(false);
        pm_am_numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                pmAMHolder = String.valueOf(newVal);
            }
        });
        //minutes
        NumberPicker minutes_numberPicker = view.findViewById(R.id.minutes_numberPicker);
        minutes_numberPicker.setMinValue(0);
        minutes_numberPicker.setMaxValue(59);
        minutes_numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        minutes_numberPicker.setWrapSelectorWheel(false);
        minutes_numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                minutesHolder = String.valueOf(newVal);
            }
        });
        //hours
        NumberPicker picker = view.findViewById(R.id.hours_numberPicker);
        picker.setMinValue(1);
        picker.setMaxValue(12);
        picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        picker.setWrapSelectorWheel(false);
        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                hoursHolder = String.valueOf(newVal);
            }
        });
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        done.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(hoursHolder == ""|| hoursHolder == null){
                    hoursHolder = "1";
                }
                if(pmAMHolder == ""|| pmAMHolder == null){
                    pmAMHolder = "AM";
                }
                else{
                    pmAMHolder = "PM";
                }
                if(minutesHolder == "" || minutesHolder == null){
                    minutesHolder ="0";
                }


                setTimeTo(dialog,hoursHolder,minutesHolder,pmAMHolder);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    String toHoursHolder = "";
    String toPmAMHolder = "";
    String toMinutesHolder = "";
    String froms;
    String tos;
    @SuppressLint("SetTextI18n")
    private void setTimeTo(AlertDialog alertDialog, String hoursHolder, String minutesHolder, String pmAMHolder) {


        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        View view = View.inflate(this,R.layout.m_service_time_dialog,null);
        MaterialButton done,cancel;
        TextView from = view.findViewById(R.id.from);
        from.setText("TO");
        done = view.findViewById(R.id.gender_dialog_btn_done);
        done.setText("Save");
        cancel = view.findViewById(R.id.gender_dialog_btn_cancel);
        //pm am
        NumberPicker pm_am_numberPicker = view.findViewById(R.id.pm_am_numberPicker);
        String[] pmAM = {"AM","PM"};
        pm_am_numberPicker.setMinValue(0);
        pm_am_numberPicker.setMaxValue(pmAM.length-1);
        pm_am_numberPicker.setDisplayedValues(pmAM);
        pm_am_numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        pm_am_numberPicker.setWrapSelectorWheel(false);
        pm_am_numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                toPmAMHolder = String.valueOf(newVal);
            }
        });
        //minutes
        NumberPicker minutes_numberPicker = view.findViewById(R.id.minutes_numberPicker);
        minutes_numberPicker.setMinValue(0);
        minutes_numberPicker.setMaxValue(59);
        minutes_numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        minutes_numberPicker.setWrapSelectorWheel(false);
        minutes_numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                toMinutesHolder = String.valueOf(newVal);
            }
        });
        //hours
        NumberPicker picker = view.findViewById(R.id.hours_numberPicker);
        picker.setMinValue(1);
        picker.setMaxValue(12);
        picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        picker.setWrapSelectorWheel(false);
        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                toHoursHolder = String.valueOf(newVal);
            }
        });
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        String finalHoursHolder = hoursHolder;
        String finalMinutesHolder = minutesHolder;
        String finalPmAMHolder = pmAMHolder;
        done.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(toHoursHolder == ""|| toHoursHolder == null){
                    toHoursHolder = "1";
                }
                if(toPmAMHolder == ""|| toPmAMHolder == null){
                    toPmAMHolder = "AM";
                }
                else{
                    toPmAMHolder = "PM";
                }
                if(toMinutesHolder == "" || toMinutesHolder == null){
                    toMinutesHolder ="0";
                }
                alertDialog.dismiss();
                availabilityTextView.setTextSize(10);
                froms = convertDate(Integer.parseInt(finalHoursHolder)) +":"+ convertDate(Integer.parseInt(finalMinutesHolder))+" "+finalPmAMHolder;
                tos =convertDate(Integer.parseInt(toHoursHolder))+":"+convertDate(Integer.parseInt(toMinutesHolder))+" "+toPmAMHolder;
                availabilityTextView.setText("From "+ convertDate(Integer.parseInt(finalHoursHolder)) +":"+ convertDate(Integer.parseInt(finalMinutesHolder)) +" "+ finalPmAMHolder +"-"
                +convertDate(Integer.parseInt(toHoursHolder))+" : "+convertDate(Integer.parseInt(toMinutesHolder))+" "+toPmAMHolder);
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        toHoursHolder="";
        toPmAMHolder ="";
        toMinutesHolder = "";
    }

    public String convertDate(int input) {
        if (input >= 10) {
            return String.valueOf(input);
        } else {
            return "0" + String.valueOf(input);
        }
    }
    int counter = 0;
    @SuppressLint("SetTextI18n")
    private void showDialogPrice() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this,R.layout.pet_add_vaccine_price_dialog,null);
        MaterialButton done, cancel;
        EditText text;
        TextView title = view.findViewById(R.id.price_title);
        title.setText("Set your service fee");

        text = view.findViewById(R.id.price_dialog_textin);
        text.setHint("Set service fee . . .");
        text.setInputType(InputType.TYPE_CLASS_PHONE);
        done = view.findViewById(R.id.price_dialog_btn_done);
        cancel = view.findViewById(R.id.price_dialog_btn_cancel);

        builder.setView(view);
        AlertDialog alert = builder.create();
        alert.show();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text.getText().toString() == null || text.getText().toString().equals("")) {
                    counter = 0;

                } else {
                    servicePriceTextView.setText("₱ "+text.getText().toString()+".0");
                    price =text.getText().toString();
                    Toast.makeText(shooter_vet_add_service.this, "₱ "+text.getText().toString()+".0", Toast.LENGTH_SHORT).show();
                    counter = 1;
                }
                if(counter !=0){
                    alert.dismiss();
                }
                else{
                    Toast.makeText(shooter_vet_add_service.this, "Cannot make any changes", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });


    }
    private final TextWatcher petDescTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @SuppressLint("SetTextI18n")
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            serviceDescCountID.setText(s.length() +"/2000");
            if(s.length()!=0){
                serviceDescClearButton.setVisibility(View.VISIBLE);
                serviceDescClearButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        serviceDescEdit.getText().clear();
                    }
                });
            }
            else{
                serviceDescClearButton.setVisibility(View.GONE);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };
    private final TextWatcher petNameTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @SuppressLint("SetTextI18n")
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            petNamecountID.setText(s.length() +"/100");

            if(s.length()!=0){
                petNameclearButton.setVisibility(View.VISIBLE);
                petNameclearButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        petNameEdit.getText().clear();
                    }
                });
            }
            else{
                petNameclearButton.setVisibility(View.GONE);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };
    @Override
    public void clicked(int size) {
        Toast.makeText(this,uri.size()+": selected",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void itemClick(int position) {
        Dialog dialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        dialog.setContentView(R.layout.custom_dialog_zoom);

        TextView textView = dialog.findViewById(R.id.text_dialog);
        ImageView imageView = dialog.findViewById(R.id.image_view_dialog);
        Button buttonClose = dialog.findViewById(R.id.button_close_dialog);


        textView.setText("Image"+position);
        Glide.with( this)
                .load(uri.get(position))
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
}