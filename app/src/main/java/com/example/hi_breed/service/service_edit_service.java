package com.example.hi_breed.service;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
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
import android.os.Environment;
import android.text.Editable;
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
import com.dpro.widgets.OnWeekdaysChangeListener;
import com.dpro.widgets.WeekdaysPicker;
import com.example.hi_breed.R;
import com.example.hi_breed.adapter.shooterAdapter.service_edit_adapter;
import com.example.hi_breed.classesFile.BaseActivity;
import com.example.hi_breed.classesFile.service_class;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class service_edit_service extends BaseActivity implements service_edit_adapter.CountOfImagesCurrent,service_edit_adapter.itemCurrentClickListenerPet
               {

    RecyclerView service_images_view;
    WeekdaysPicker widget;
    LinearLayout backLayoutService;
    CardView servicePhotoCardView;
    TextView availabilityTextView;
    TextView servicePriceTextView;
    TextView serviceDescCountID;
    TextView cancelChanges;
    TextView saveChanges;
    EditText serviceDescEdit;
    ImageView serviceDescClearButton,saveDescButton;
    RelativeLayout availabilityLayout;
    RelativeLayout priceLayout;
    service_edit_adapter adapter;
    ArrayList<Uri> uri = new ArrayList<>();
    Uri imageUri;
    String price;
    String id;
    String shooterID;
    String description_before,name_before;
    List<String> roles = new ArrayList<>();
    RelativeLayout serviceNameLayout;
     private TextInputEditText petNameEdit;
     TextView petNamecountID;

      private ImageView petNameclearButton,saveNameButton;

                   @SuppressLint("SetTextI18n")
                   @Override
                   public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shooter_edit_service);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));

                       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                           this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
                       }
                       else{
                           window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                           window.setStatusBarColor(Color.parseColor("#e28743"));
                       }
        //Relative
       serviceNameLayout = findViewById(R.id.serviceNameLayout);
                       petNameEdit = findViewById(R.id. petNameEdit);
                       petNameclearButton = findViewById(R.id.petNameclearButton);
                       petNamecountID = findViewById(R.id.petNamecountID);
                       //button
        Button delete = findViewById(R.id.Service_add_create_Button);
        delete.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder builder2 = new androidx.appcompat.app.AlertDialog.Builder(service_edit_service.this);
                builder2.setCancelable(false);
                View view = View.inflate(service_edit_service.this, R.layout.screen_custom_alert, null);
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
                message.setVisibility(View.GONE);
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
                androidx.appcompat.app.AlertDialog alert2 = builder2.create();
                alert2.show();
                alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                okay.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
                    @Override
                    public void onClick(View v) {
                        alert2.dismiss();
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(service_edit_service.this);
                        builder2.setCancelable(false);
                        View view = View.inflate(service_edit_service.this, R.layout.screen_custom_alert, null);
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
                        title.setText("Deleting your service");
                        message.setVisibility(View.VISIBLE);
                        message.setText("Please wait a bit. We are deleting your service info");
                        builder2.setView(view);
                        AlertDialog alert2 = builder2.create();
                        alert2.show();
                        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                        FirebaseFirestore.getInstance().collection("Services")
                                .document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            FirebaseFirestore.getInstance().collection("User")
                                                    .document(shooterID)
                                                    .collection("Service")
                                                    .document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                alert2.dismiss();
                                                              FirebaseFirestore.getInstance().collection("Search")
                                                                      .document(id)
                                                                      .delete()
                                                                      .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                          @Override
                                                                          public void onComplete(@NonNull Task<Void> task) {
                                                                                if(task.isSuccessful()){

                                                                                    StorageReference   storageReference= FirebaseStorage.getInstance().getReference("User/"+id+"/"+"Service/"+"Service Photos/");
                                                                                    storageReference.listAll()
                                                                                            .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                                                                                                @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
                                                                                                @Override
                                                                                                public void onSuccess(ListResult listResult) {
                                                                                                    for (StorageReference item : listResult.getItems()) {
                                                                                                        item.delete();
                                                                                                    }
                                                                                                    storageReference.delete();
                                                                                                    alert2.show();
                                                                                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(service_edit_service.this);
                                                                                                    builder2.setCancelable(false);
                                                                                                    View view = View.inflate(service_edit_service.this, R.layout.screen_custom_alert, null);
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
                                                                                                    title.setText("Successfully Deleted");
                                                                                                    message.setVisibility(View.VISIBLE);
                                                                                                    message.setText("Click okay to continue..");
                                                                                                    LinearLayout buttonLayout = view.findViewById(R.id.screen_custom_alert_buttonLayout);
                                                                                                    buttonLayout.setVisibility(View.VISIBLE);
                                                                                                    MaterialButton cancel, okay;
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
                                                                                                            startActivity(new Intent(service_edit_service.this, service_panel.class));
                                                                                                            finish();
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
        });
        //relative layout
        priceLayout = findViewById(R.id.priceLayout);
        availabilityLayout = findViewById(R.id.availabilityLayout);
        //editText
        serviceDescEdit = findViewById(R.id.serviceDescEdit);
        //recycler
        service_images_view =findViewById(R.id. service_images_view);
        //imageview for clearing the text in description
                       saveNameButton = findViewById(R.id.saveNameButton);
        serviceDescClearButton = findViewById(R.id.serviceDescClearButton);
        serviceDescCountID = findViewById(R.id.serviceDescCountID);
        saveDescButton = findViewById(R.id.saveDescButton);
        //TextView for output
        availabilityTextView = findViewById(R.id.availabilityTextView);
        servicePriceTextView = findViewById(R.id.servicePriceTextView);
        saveChanges = findViewById(R.id.saveChanges);
        cancelChanges = findViewById(R.id.cancelChanges);
        //CardView
        servicePhotoCardView = findViewById(R.id.servicePhotoCardView);

        //Linear Layout
        backLayoutService = findViewById(R.id.backLayoutService);


        backLayoutService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service_edit_service.this.onBackPressed();
                finish();
            }
        });
        //widget
        widget  = (WeekdaysPicker) findViewById(R.id.weekdays);


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
        if(petNameEdit.getText() !=null ||petNameEdit.getText().equals("")){
                           petNameEdit.addTextChangedListener(petNameTextEditorWatcher);
         }

        servicePhotoCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( checkPermission()){
                    imagePermission();
                }
                else{
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Permission is not granted, request it
                        ActivityCompat.requestPermissions(service_edit_service.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                30);
                    }
                }
            }
        });


        //INTENT
        Intent intent = getIntent();
        service_class service= (service_class) intent.getSerializableExtra("mode");
        if(service!=null){
            description_before = service.getService_description();
            FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot s = task.getResult();
                                roles.addAll((List<String>)s.get("role"));
                                if(roles.contains("Veterinarian")){
                                    serviceNameLayout.setVisibility(View.VISIBLE);
                                    name_before = service.getServiceType();
                                    petNameEdit.setText(service.getServiceType());
                                }
                                else{
                                    serviceNameLayout.setVisibility(View.GONE);
                                }
                            }
                        }
                    });


            id = service.getId();
            shooterID = service.getShooter_id();
            serviceDescEdit.setText(service.getService_description());
            if(service.getAvailability() == null)
            {

            }
            else
            availabilityTextView.setText("From"+service.getAvailability().get(0)+"-"+service.getAvailability().get(0));

            servicePriceTextView.setText(service.getService_fee());
            if(service.getPhotos()!=null){
                for (int i = 0; i<service.getPhotos().size();i++){
                    Uri imageUri = Uri.parse(service.getPhotos().get(i));
                    service_images_view.setVisibility(View.VISIBLE);
                    uri.add(imageUri);
                    if (uri.size() == 0) {
                        service_images_view.setVisibility(View.GONE);
                    }
                    adapter = new service_edit_adapter(uri, getApplicationContext(), this, this,id,shooterID);
                    adapter.notifyDataSetChanged();
                }
            }

            adapter = new service_edit_adapter(uri,getApplicationContext(),this, this,id,shooterID);
            service_images_view.setLayoutManager(new GridLayoutManager(this,4));
            service_images_view.setAdapter(adapter);

            LinkedHashMap<Integer, Boolean> mp = new LinkedHashMap<>();
            if(service.getSchedule()!=null){
                List<String> days = new ArrayList<>();
                for(String i : service.getSchedule()){
                    days.add(i);
                }
                if(days.contains("Monday")){
                    mp.put(Calendar.MONDAY, true);
                }else{
                    mp.put(Calendar.MONDAY, false);
                }

                if(days.contains("Tuesday"))
                {
                    mp.put(Calendar.TUESDAY, true);
                }
                else{
                    mp.put(Calendar.TUESDAY, false);
                }
                if(days.contains("Wednesday")){
                    mp.put(Calendar.WEDNESDAY, true);
                }else{
                    mp.put(Calendar.WEDNESDAY, false);
                }
                if(days.contains("Thursday")){
                    mp.put(Calendar.THURSDAY, true);
                }
                else{
                    mp.put(Calendar.THURSDAY, false);
                }
                if(days.contains("Friday")){
                    mp.put(Calendar.FRIDAY, true);
                }
                else{
                    mp.put(Calendar.FRIDAY, false);
                }
                if(days.contains("Saturday")){
                    mp.put(Calendar.SATURDAY, true);
                }
                else{
                    mp.put(Calendar.SATURDAY, false);
                }
                if(days.contains("Sunday")){
                    mp.put(Calendar.SUNDAY, true);
                }
                else{
                    mp.put(Calendar.SUNDAY, false);
                }
                widget.setCustomDays(mp);

                widget.setOnWeekdaysChangeListener(new OnWeekdaysChangeListener() {
                    @Override
                    public void onChange(View view, int clickedDayOfWeek, List<Integer> selectedDays) {
                        if (selectedDays.containsAll(mp.keySet())){
                            saveChanges.setVisibility(View.GONE);
                            cancelChanges.setVisibility(View.GONE);
                            return;
                        }
                        else{

                            saveChanges.setVisibility(View.VISIBLE);
                            cancelChanges.setVisibility(View.VISIBLE);
                            cancelChanges.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    widget.setCustomDays(mp);
                                    saveChanges.setVisibility(View.GONE);
                                    cancelChanges.setVisibility(View.GONE);
                                }
                            });
                            saveChanges.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Map<String,Object> data = new HashMap<>();
                                    data.put("schedule",widget.getSelectedDaysText());
                                    if(widget.noDaySelected()){
                                        Toast.makeText(service_edit_service.this, "Schedule must not be empty.", Toast.LENGTH_SHORT).show();
                                    }else{
                                        FirebaseFirestore.getInstance().collection("Services")
                                                .document(id)
                                                .set(data,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            FirebaseFirestore.getInstance().collection("User")
                                                                    .document(shooterID)
                                                                    .collection("Services")
                                                                    .document(id)
                                                                    .set(data,SetOptions.merge())
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if(task.isSuccessful()){
                                                                                Toast.makeText(service_edit_service.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                                                                                saveChanges.setVisibility(View.GONE);
                                                                                cancelChanges.setVisibility(View.GONE);

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
                    }
                });
            }

        }


    }

                   private boolean checkPermission() {
                       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                           return Environment.isExternalStorageManager();
                       } else {
                           int readCheck = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
                           int writeCheck = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
                           return readCheck == PackageManager.PERMISSION_GRANTED && writeCheck == PackageManager.PERMISSION_GRANTED;
                       }
                   }

                   @SuppressLint("ObsoleteSdkInt")
                   private void imagePermission() {
                       Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                       intent.setType("image/*");
                       if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
                           intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                       }

                       startActivityForResult(Intent.createChooser(intent,"Select Picture"),1);
                   }


    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && null != data){

            imageUri = data.getData();
            if(data.getClipData()!=null){
                Toast.makeText(this, "Please upload one at a time", Toast.LENGTH_SHORT).show();
            }
            else{
                AlertDialog.Builder builder2 = new AlertDialog.Builder(service_edit_service.this);
                builder2.setCancelable(false);
                View view = View.inflate(service_edit_service.this,R.layout.screen_custom_alert,null);
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
                title.setText("Updating your pictures");
                message.setVisibility(View.VISIBLE);
                message.setText("Please wait a bit. We are updating your photos");
                builder2.setView(view);
                AlertDialog alert2 = builder2.create();
                alert2.show();
                alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                uploadToFirebaseStorage(id,alert2,imageUri);
            }
        }
        else{
            //this code is for if user not picked any image
            Toast.makeText(this,"You Haven't Pick any image",Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadToFirebaseStorage(String id, AlertDialog alert2,Uri imageUri) {

        String imageName = "Question_Photo_" + id + "_" + System.currentTimeMillis();
        StorageReference ImageFolder = FirebaseStorage.getInstance().getReference("User/"+shooterID+"/"+"Service/"+"Service Photos/"+id+"/"+imageName);
        final UploadTask uploadTask = ImageFolder.putFile(imageUri);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        ImageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url = uri.toString();
                                StringLinkPaper(id,alert2,url);
                            }
                        });
                    }
            }
        });

    }
    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    private void StringLinkPaper(String id, AlertDialog alertDialog,String url) {
        FirebaseFirestore.getInstance()
                .collection("Services")
                .document(id)
                .update("photos", FieldValue.arrayUnion(url)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            FirebaseFirestore.getInstance().collection("User")
                                    .document(shooterID).collection("Services").document(id)
                                    .update("photos",FieldValue.arrayUnion(url))
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables", "NotifyDataSetChanged"})
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                uri.add(Uri.parse(url));
                                                adapter.notifyDataSetChanged();
                                                alertDialog.dismiss();
                                            }
                                        }
                                    });
                        }
                    }
                });

        alertDialog.dismiss();
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
        imageViewCompat.setImageDrawable(getDrawable(R.drawable.screen_alert_image_valid_borders));
        //message
        TextView message = view.findViewById(R.id.screen_custom_alert_message);
        title.setText("Successfully Updated");
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
                alert2.dismiss();
            }
        });


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
                    UpdateInfo("service_fee",price);
                    Toast.makeText(service_edit_service.this, "₱ "+text.getText().toString()+".0", Toast.LENGTH_SHORT).show();
                    counter = 1;
                }
                if(counter !=0){
                    alert.dismiss();
                }
                else{
                    Toast.makeText(service_edit_service.this, "Cannot make any changes", Toast.LENGTH_SHORT).show();
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

    String froms;
    String tos;
    String toHoursHolder = "";
    String toPmAMHolder = "";
    String toMinutesHolder = "";
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

                availabilityTextView.setText("From "+ convertDate(Integer.parseInt(finalHoursHolder)) +" : "+ convertDate(Integer.parseInt(finalMinutesHolder)) +" "+ finalPmAMHolder +"-"
                        +convertDate(Integer.parseInt(toHoursHolder))+" : "+convertDate(Integer.parseInt(toMinutesHolder))+" "+toPmAMHolder);
                List<String> avail = new ArrayList<>();
                avail = Arrays.asList(froms,tos);
                FirebaseFirestore.getInstance().collection("Services")
                                .document(id)
                                        .update("availability",avail)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(service_edit_service.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                                                            dialog.dismiss();
                                                        }
                                                    }
                                                });

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



    private void UpdateInfo(String field, String value) {
        Map<String,Object> data = new HashMap<>();
        data.put(field,value);
        //Services
        FirebaseFirestore.getInstance().collection("Services").document(id)
                .set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                //User
                                FirebaseFirestore.getInstance().collection("User").document(shooterID)
                                        .collection("Services").document(id).set(data,SetOptions.merge())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    FirebaseFirestore.getInstance().collection("Shop")
                                                            .document(shooterID).collection("Services")
                                                            .document(id)
                                                            .set(data,SetOptions.merge())
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(field.equals("serviceType")) {
                                                                        FirebaseFirestore.getInstance().collection("Search")
                                                                                .document(id)
                                                                                .set(data,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                            if(task.isSuccessful()){
                                                                                                Toast.makeText(service_edit_service.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                    }
                                                                                });
                                                                    }
                                                                    else{
                                                                        Toast.makeText(service_edit_service.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
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


    @Override
    public void clickCurrent(int size) {
        Toast.makeText(this,uri.size()+": selected",Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void itemCurrentClick(int position) {
        Dialog dialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        dialog.setContentView(R.layout.custom_dialog_zoom);

        TextView textView = dialog.findViewById(R.id.text_dialog);
        ImageView imageView = dialog.findViewById(R.id.image_view_dialog);
        Button buttonClose = dialog.findViewById(R.id.button_close_dialog);


        textView.setText("Image"+position);



        Glide.with(  this)
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
                               if(s.toString().equals(name_before)){
                                   saveNameButton.setVisibility(View.GONE);
                               }else{
                                   saveNameButton.setVisibility(View.VISIBLE);
                                   saveNameButton.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           UpdateInfo("serviceType",s.toString());
                                           name_before = s.toString();
                                           saveNameButton.setVisibility(View.GONE);
                                       }
                                   });
                               }
                           }
                           else{
                               saveNameButton.setVisibility(View.GONE);
                               petNameclearButton.setVisibility(View.GONE);
                           }
                       }

                       public void afterTextChanged(Editable s) {
                       }
                   };
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

                if(s.length() > 19){
                    if(s.toString().equals(description_before)){
                        saveDescButton.setVisibility(View.GONE);
                    }
                    else{
                        saveDescButton.setVisibility(View.VISIBLE);
                        saveDescButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                UpdateInfo("service_description",s.toString());
                                description_before = s.toString();
                                saveDescButton.setVisibility(View.GONE);
                            }
                        });
                    }

                }
                else{
                    saveDescButton.setVisibility(View.GONE);
                }
            }
            else{
                saveDescButton.setVisibility(View.GONE);
                serviceDescClearButton.setVisibility(View.GONE);
            }

        }

        public void afterTextChanged(Editable s) {
        }
    };


}