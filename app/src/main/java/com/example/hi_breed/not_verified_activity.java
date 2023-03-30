package com.example.hi_breed;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.hi_breed.adapter.breeder_kennel_cert_RecyclerAdapter;
import com.example.hi_breed.adapter.shooterAdapter.shooter_proof_RecyclerAdapter;
import com.example.hi_breed.userFile.dashboard.user_dashboard;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class not_verified_activity extends AppCompatActivity implements breeder_kennel_cert_RecyclerAdapter.CountOfImagesWhenRemoved, breeder_kennel_cert_RecyclerAdapter.itemClickListener, shooter_proof_RecyclerAdapter.itemClickListeners,shooter_proof_RecyclerAdapter.CountOfImagesWhenRemoves{



    LinearLayout backLayoutService;
    private static final int Read_Permission = 101;
    private static final int PICK_IMAGE = 1;
    private static final int Read_PermissionShooter = 102;
    private static final int PICK_IMAGE_Shooter = 2;
    private int i = -1;
    private int countOfImages;
    private int getCountOfImagesShoot;
    Button clickHere;
    private FirebaseAuth mAuth;
    FirebaseFirestore firestore;

    RecyclerView recyclerView,recyclerView_gallery_images_shooter;
    breeder_kennel_cert_RecyclerAdapter adapter;
    shooter_proof_RecyclerAdapter shooter_adapter;

    ArrayList<Uri> uri = new ArrayList<>();
    Uri imageUri;
    ArrayList <Uri> uriShooter = new ArrayList<>();
    Uri imageUriShooter;
    Uri imgVet;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<Intent> activityResultLauncherShooter;

    CardView dropImageCardView,dropImageCardViewShooter,dropImageCardViewVet;
    ImageView dropImageView,dropImageViewVet,prc_image,imageview_vet_style;
    Button submit;
    RelativeLayout reg_transaction,reg_date_registered;
    TextInputLayout reg_memberID,reg_prc_id,reg_kennelName;
    TextInputEditText reg_memberID_edit,reg_prc_id_edit,reg_experienceEdit,reg_kennelNameEdit;
    TextView reg_date_registered_edit,prc_required_error,add_photo_vet,dropImageTextVIew,reg_transactionEdit,textViewExp,changeRole;

    LinearLayout memberLayouts,shooter_validation_linearLayout,breeder_validation_linearLayout,vet_validation_linear_layout;
    TextInputLayout reg_experience;
    ArrayList<String> role;
    ArrayList<String> saveRole;
    RelativeLayout validationLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_verified);

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
        backLayoutService = findViewById(R.id.backLayoutService);

        backLayoutService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                not_verified_activity.this.onBackPressed();
                finish();
            }
        });
        changeRole = findViewById(R.id.changeRole);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        //for recycler view
        recyclerView_gallery_images_shooter = findViewById(R.id.recyclerView_gallery_images_shooter);
        recyclerView = findViewById(R.id.recyclerView_gallery_images);
        //for adapter
        shooter_adapter = new shooter_proof_RecyclerAdapter(uriShooter,this,this,this);
        adapter = new breeder_kennel_cert_RecyclerAdapter(uri,this,this, this);
        //RecyclerView
        recyclerView_gallery_images_shooter.setLayoutManager(new GridLayoutManager(this,4));
        recyclerView_gallery_images_shooter.setAdapter(shooter_adapter);
        memberLayouts = findViewById(R.id.memberLayouts);
        recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        recyclerView.setAdapter(adapter);


        validationLayout =findViewById(R.id.validationLayout);
        clickHere = findViewById(R.id.clickHere);
        clickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.pcci.org.ph/how-tos/apply-for-kennel-name/";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        reg_memberID_edit= findViewById(R.id.reg_memberID_edit);
        reg_memberID= findViewById(R.id.reg_memberID);
        reg_prc_id_edit= findViewById(R.id.reg_prc_id_edit);
        reg_prc_id= findViewById(R.id.reg_prc_id);
        reg_date_registered= findViewById(R.id.reg_date_registered);
        reg_date_registered_edit= findViewById(R.id.reg_date_registered_edit);

// variable initialized
        //TextView

        prc_required_error = findViewById(R.id.prc_required_error);
        add_photo_vet = findViewById(R.id.add_photo_vet);
        textViewExp = findViewById(R.id.textViewExp);
        dropImageTextVIew = findViewById(R.id.dropImageTextView);

        reg_transactionEdit = findViewById(R.id.reg_transactionEdit);
        //cardView
        dropImageCardView = findViewById(R.id.dropImageCardView);
        dropImageCardViewShooter =findViewById(R.id.dropImageCardViewShooter);
        dropImageCardViewVet = findViewById(R.id.dropImageCardViewVet);

        //ImageView
        dropImageView =findViewById(R.id.dropImageView);
        dropImageViewVet = findViewById(R.id.dropImageViewVet);
        prc_image = findViewById(R.id.prc_image);
        imageview_vet_style = findViewById(R.id.imageview_vet_style);
        //Relative Layout

        reg_transaction = findViewById(R.id.reg_transaction);

        //button
        submit = findViewById(R.id.submitButton);



        reg_experience =findViewById(R.id.reg_experience);
        reg_kennelName = findViewById(R.id.reg_kennelName);



        reg_experienceEdit = findViewById(R.id.reg_experienceEdit);

        reg_kennelNameEdit = findViewById(R.id.reg_kennelNameEdit);



        reg_experienceEdit.addTextChangedListener(new TextChange(reg_experienceEdit));

        reg_kennelNameEdit.addTextChangedListener(new TextChange(reg_kennelNameEdit));

        reg_prc_id_edit.addTextChangedListener(new TextChange(reg_prc_id_edit));

        reg_memberID_edit.addTextChangedListener(new TextChange(reg_memberID_edit));


        //Getting the input from 1st fragment Details
        role = new ArrayList<>();
        saveRole = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("User")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        role = (ArrayList<String>) documentSnapshot.get("role");

                        changeRole.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                    startActivity(new Intent(not_verified_activity.this,change_role.class));
                                    finish();
                            }
                        });



                        if(role.contains("Pet Owner")){
                            saveRole.add("Pet Owner");
                        }
                        if(role.contains("Pet Breeder") || role.contains("Pet Shooter")|| role.contains("Veterinarian")){
                            memberLayouts.setVisibility(View.VISIBLE);

                            if(role.contains("Pet Breeder")){
                                saveRole.add("Pet Breeder");
                                breeder_validation_linearLayout.setVisibility(View.VISIBLE);
                            }
                            if(role.contains("Pet Shooter")){
                                saveRole.add("Pet Shooter");
                                shooter_validation_linearLayout.setVisibility(View.VISIBLE);
                            }

                            if(role.contains("Veterinarian")){
                                saveRole.add("Veterinarian");
                                vet_validation_linear_layout.setVisibility(View.VISIBLE);
                            }

                        }
                        else{
                            startActivity(new Intent(not_verified_activity.this, user_dashboard.class));
                            finish();
                            submit.setVisibility(View.GONE);
                            validationLayout.setVisibility(View.GONE);
                            memberLayouts.setVisibility(View.GONE);
                        }

                    }
                });


        //LinearLayout
        shooter_validation_linearLayout =  findViewById(R.id.shooter_validation_linearLayout);
        breeder_validation_linearLayout = findViewById(R.id.breeder_validation_linearLayout);
        vet_validation_linear_layout =  findViewById(R.id.vet_validation_linear_layout);



        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        //Refresh this code
                        if (result.getResultCode() == RESULT_OK && null != result.getData()) {
                            if (result.getData().getClipData() != null) {

                                //this part is for getting the multiple images
                                countOfImages = result.getData().getClipData().getItemCount();
                                for (int i = 0 ; i < countOfImages; i++) {
                                    //limiting the no of images picked up from gallery
                                    if (uri.size() < 5) {
                                        imageUri = result.getData().getClipData().getItemAt(i).getUri();
                                        uri.add(imageUri);
                                    } else {
                                        Toast.makeText(not_verified_activity.this, "Not Allowed to pick more than 5 images", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                //then notify the adapter
                                adapter.notifyDataSetChanged();
                                Toast.makeText(not_verified_activity.this, uri.size() + ":selected", Toast.LENGTH_SHORT).show();

                            }else{
                                if(uri.size() <5){
                                    //this is for to get the single images
                                    imageUri = result.getData().getData();
                                    //add the code into
                                    uri.add(imageUri);
                                }else{
                                    Toast.makeText(not_verified_activity.this, "Not Allowed to pick more than 5 images", Toast.LENGTH_SHORT).show();
                                }
                            }
                            adapter.notifyDataSetChanged();
                            Toast.makeText(not_verified_activity.this,uri.size()+": selected",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(not_verified_activity.this, "You Haven't Pick any Image", Toast.LENGTH_SHORT).show();
                        }
                    }

                });

        activityResultLauncherShooter = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        //Refresh this code
                        if (result.getResultCode() == RESULT_OK && null != result.getData()) {
                            if (result.getData().getClipData() != null) {

                                //this part is for getting the multiple images
                                getCountOfImagesShoot = result.getData().getClipData().getItemCount();
                                for (int i = 0 ; i < getCountOfImagesShoot; i++) {
                                    //limiting the no of images picked up from gallery
                                    if (uriShooter.size() < 5) {
                                        imageUriShooter = result.getData().getClipData().getItemAt(i).getUri();
                                        uriShooter.add(imageUriShooter);
                                    } else {
                                        Toast.makeText(not_verified_activity.this, "Not Allowed to pick more than 5 images", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                //then notify the adapter
                                shooter_adapter.notifyDataSetChanged();
                                Toast.makeText(not_verified_activity.this, uriShooter.size() + ":selected", Toast.LENGTH_SHORT).show();

                            }else{
                                if(uriShooter.size() <5){
                                    //this is for to get the single images
                                    imageUriShooter = result.getData().getData();
                                    //add the code into
                                    uriShooter.add(imageUriShooter);
                                }else{
                                    Toast.makeText(not_verified_activity.this, "Not Allowed to pick more than 5 images", Toast.LENGTH_SHORT).show();
                                }
                            }
                            shooter_adapter.notifyDataSetChanged();
                            Toast.makeText(not_verified_activity.this,uriShooter.size()+": selected",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(not_verified_activity.this, "You Haven't Pick any Image", Toast.LENGTH_SHORT).show();
                        }
                    }

                });

        dropImageCardViewShooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    imagePermissionShooter();
                } else {
                    requestPermissionShot(); // Request Permission
                }
            }
        });

        dropImageCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    imagePermission();
                } else {
                    requestPermission(); // Request Permission
                }
            }
        });

        dropImageCardViewVet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( checkPermission()){
                    imagePermissionVet();
                }
                else{
                    if (ContextCompat.checkSelfPermission(not_verified_activity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Permission is not granted, request it
                        ActivityCompat.requestPermissions(not_verified_activity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                30);
                    }
                }
            }
        });


        reg_date_registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDateRegistered();
            }
        });

        reg_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTransactionDialog();
            }
        });

        //button
        submit =  findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInput();
            }
        });


    }

    @SuppressLint("SetTextI18n")
    private void openDateRegistered() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this,R.layout.pet_add_birthday_dialog,null);
        DatePicker datePicker = view.findViewById(R.id.datePicker);
        datePicker.setMaxDate(System.currentTimeMillis());
        builder.setView(view);
        MaterialButton cancel,save;
        TextView message,title;
        title =view.findViewById(R.id.pet_add_birthday_set_title);
        title.setText("Select Date");
        message = view.findViewById(R.id.pet_add_birthday_set_message);
        message.setText("Please select the day that you are registered in the club");
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
                reg_date_registered_edit.setText(formatedDate);
                reg_date_registered_edit.setTextColor(ColorStateList.valueOf(Color.BLACK));
                Toast.makeText(not_verified_activity.this, formatedDate, Toast.LENGTH_SHORT).show();
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

    // Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 30) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, open the gallery
                imagePermissionVet();
            } else {
                // Permission is not granted, show an error message
                Toast.makeText(not_verified_activity.this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private void imagePermission() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        }

        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE);
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int readCheck = ContextCompat.checkSelfPermission(not_verified_activity.this, READ_EXTERNAL_STORAGE);
            int writeCheck = ContextCompat.checkSelfPermission(not_verified_activity.this, WRITE_EXTERNAL_STORAGE);
            return readCheck == PackageManager.PERMISSION_GRANTED && writeCheck == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void imagePermissionVet() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    private static final int REQUEST_PICK_IMAGE = 102;

    private final String[] permissions = {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            new AlertDialog.Builder(not_verified_activity.this)
                    .setTitle("Permission")
                    .setMessage("Please give the Storage permission")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick( DialogInterface dialog, int which ) {
                            try {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                intent.addCategory("android.intent.category.DEFAULT");
                                intent.setData(Uri.parse(String.format("package:%s", not_verified_activity.this.getPackageName())));
                                activityResultLauncher.launch(intent);
                            } catch (Exception e) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                activityResultLauncher.launch(intent);
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setCancelable(false)
                    .show();
        } else {

            ActivityCompat.requestPermissions(not_verified_activity.this, permissions, 30);

        }
    }

    private void requestPermissionShot() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            new AlertDialog.Builder(not_verified_activity.this)
                    .setTitle("Permission")
                    .setMessage("Please give the Storage permission")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick( DialogInterface dialog, int which ) {
                            try {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                intent.addCategory("android.intent.category.DEFAULT");
                                intent.setData(Uri.parse(String.format("package:%s", not_verified_activity.this.getPackageName())));
                                activityResultLauncherShooter.launch(intent);
                            } catch (Exception e) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                activityResultLauncherShooter.launch(intent);
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setCancelable(false)
                    .show();
        } else {

            ActivityCompat.requestPermissions( not_verified_activity.this, permissions, 10);

        }

    }


    @SuppressLint("ObsoleteSdkInt")
    private void imagePermissionShooter() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        }

        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_Shooter);

    }

    String resultsForCover="";
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data){

            //this part is for to get multiple images
            if(data.getClipData() != null){

                countOfImages = data.getClipData().getItemCount();
                for(int j = 0; j<countOfImages; j++){

                    if(uri.size() < 5){
                        imageUri = data.getClipData().getItemAt(j).getUri();
                        uri.add(imageUri);
                    }else{
                        Toast.makeText(this,"Not allowed to pick more than 5 images",Toast.LENGTH_SHORT).show();
                    }
                }
                //then notify the adapter
                adapter.notifyDataSetChanged();
                Toast.makeText(this,uri.size()+": selected",Toast.LENGTH_SHORT).show();
            }else{
                //this is for to get the single images
                if(uri.size() <5){
                    imageUri =data.getData();
                    uri.add(imageUri);
                }else{
                    Toast.makeText(this,"Not allowed to pick more than 5 images",Toast.LENGTH_SHORT).show();
                }
            }
            adapter.notifyDataSetChanged();
            Toast.makeText(this,uri.size()+": selected",Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == PICK_IMAGE_Shooter && resultCode == RESULT_OK && null != data){

            //this part is for to get multiple images
            if(data.getClipData() != null){

                getCountOfImagesShoot = data.getClipData().getItemCount();
                for(int j = 0; j<getCountOfImagesShoot; j++){

                    if(uri.size() < 5){
                        imageUriShooter = data.getClipData().getItemAt(j).getUri();
                        uriShooter.add(imageUriShooter);
                    }else{
                        Toast.makeText(this,"Not allowed to pick more than 5 images",Toast.LENGTH_SHORT).show();
                    }
                }
                //then notify the adapter
                shooter_adapter.notifyDataSetChanged();
                Toast.makeText(this,uriShooter.size()+": selected",Toast.LENGTH_SHORT).show();
            }else{
                //this is for to get the single images
                if(uriShooter.size() <5){
                    imageUriShooter =data.getData();
                    uriShooter.add(imageUriShooter);
                }else{
                    Toast.makeText(this,"Not allowed to pick more than 5 images",Toast.LENGTH_SHORT).show();
                }
            }
            shooter_adapter.notifyDataSetChanged();
            Toast.makeText(this,uriShooter.size()+": selected",Toast.LENGTH_SHORT).show();
        }
        else     if (resultCode == RESULT_OK && requestCode == REQUEST_PICK_IMAGE) {
            Uri imageUri = data.getData();
            cropImage(imageUri);
        }
        else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            Uri croppedUri = UCrop.getOutput(data);
            // Use the cropped image URI as needed
            if(croppedUri!=null){
                prc_image.setVisibility(View.VISIBLE);
                imgVet = croppedUri;
                Glide.with(this)
                        .load(croppedUri)
                        .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
                        .placeholder(R.drawable.noimage)
                        .into(prc_image);
                add_photo_vet.setVisibility(View.GONE);
                dropImageViewVet.setVisibility(View.GONE);
            }

        } else if (resultCode == UCrop.RESULT_ERROR) {
            Throwable error = UCrop.getError(data);
            // Handle UCrop error as needed
        }
        else {
            //this code is for if user not picked any image
            Toast.makeText(this,"You Haven't Picked any image",Toast.LENGTH_SHORT).show();
        }
    }

    private void cropImage(Uri sourceUri) {
        File cacheDir = not_verified_activity.this.getCacheDir();
        Uri destinationUri = Uri.fromFile(new File(cacheDir, "cropped"));
        UCrop uCrop = UCrop.of(sourceUri, destinationUri);
        uCrop.start( this);
    }

    private void checkInput() {


        String exp = reg_experienceEdit.getText().toString().trim();
        String kennel = reg_kennelNameEdit.getText().toString().trim();
        String memberID = reg_memberID_edit.getText().toString().trim();
        String prcID =  reg_prc_id_edit.getText().toString().trim();
        String date = reg_date_registered_edit.getText().toString().trim();


        Bundle bundle = new Bundle();

        // to check if what is the role of the user
        if(role.contains("Pet Breeder") || role.contains("Pet Shooter")|| role.contains("Veterinarian")){

            if(role.contains("Pet Breeder")){

                if(kennel.isEmpty()){
                    reg_kennelName.setHelperText("Please input your registered kennel name");
                    reg_kennelName.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    reg_kennelNameEdit.requestFocus();
                    return;
                }
                if(uri.size()==0){
                    Toast.makeText(this, "Certificate is needed", Toast.LENGTH_SHORT).show();

                    return;
                }
                bundle.putString("kennel",kennel);
                bundle.putParcelableArrayList("uriBreeder",uri);
            }
            if(role.contains("Pet Shooter")){

                if(reg_transactionEdit.getText().toString().equals("Date of last transaction . . .")){
                    reg_transactionEdit.setFocusable(true);
                    reg_transactionEdit.setFocusableInTouchMode(true);
                    Toast.makeText(this, "Date for your last transaction is empty", Toast.LENGTH_SHORT).show();
                    reg_transactionEdit.requestFocus();
                    return;
                }

                bundle.putString("lastTransaction",reg_transactionEdit.getText().toString());

                if(uriShooter.size()==0){
                    Toast.makeText(this, "Proof photos of successful stud/made are needed", Toast.LENGTH_SHORT).show();
                    return;
                }
                bundle.putParcelableArrayList("uriShooter",uriShooter);

            }
            if(role.contains("Veterinarian")){
                if(imgVet == null){
                    prc_required_error.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "Please provide a picture of your License ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                prc_required_error.setVisibility(View.GONE);
                bundle.putString("vet_image",imgVet.toString());

                if(prcID.isEmpty()){
                    reg_prc_id_edit.setFocusable(true);
                    reg_prc_id_edit.setFocusableInTouchMode(true);
                    Toast.makeText(this, "PRC ID No. is empty it is required to fill in your ID No.", Toast.LENGTH_SHORT).show();
                    reg_prc_id_edit.requestFocus();
                    return;
                }
                else if(prcID.length() < 7){
                    reg_prc_id.setHelperText("PRC ID No. must have 7 digits");
                    reg_prc_id.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    reg_prc_id_edit.requestFocus();
                    return;
                }
                bundle.putString("prcNo",prcID);
            }

            if(exp.isEmpty()) {
                reg_experience.setHelperText("Please input number of experience");
                reg_experience.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                reg_experienceEdit.requestFocus();
                return;
            }
            bundle.putString("exp",exp);
            if(memberID.isEmpty()){
                reg_memberID.setHelperText("Please input member ID");
                reg_memberID.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                reg_memberID_edit.requestFocus();
                return;
            }
            else if(memberID.length() < 5){
                reg_memberID.setHelperText("Member ID should have 5 digits");
                reg_memberID.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                reg_memberID_edit.requestFocus();
                return;
            }
            bundle.putString("memberID",memberID);
            if(date.isEmpty()){
                reg_date_registered_edit.setFocusable(true);
                reg_date_registered_edit.setFocusableInTouchMode(true);
                reg_date_registered_edit.requestFocus();
                Toast.makeText(this, "Date of registration is empty", Toast.LENGTH_SHORT).show();
                return;
            }

            bundle.putString("dateOfRegistration",date);
        }

        //sending all the data to next fragment
        bundle.putStringArrayList("roles",saveRole);




    }//end of check input method


    @SuppressLint("SetTextI18n")
    private void openTransactionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this,R.layout.pet_add_birthday_dialog,null);
        DatePicker datePicker = view.findViewById(R.id.datePicker);
        datePicker.setMaxDate(System.currentTimeMillis());

        MaterialButton cancel,save;
        TextView message,title;
        title = view.findViewById(R.id.pet_add_birthday_set_title);
        title.setText("Date of last transaction");
        message = view.findViewById(R.id.pet_add_birthday_set_message);
        message.setText("Please indicate the date of your last transaction you perform dog shooting.");
        cancel = view.findViewById(R.id.birthday_dialog_btn_cancel);
        save = view.findViewById(R.id.birthday_dialog_btn_done);

        builder.setView(view);
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
                reg_transactionEdit
                        .setText(formatedDate);
                reg_transactionEdit.setTextColor(ColorStateList.valueOf(Color.BLACK));
                Toast.makeText(not_verified_activity.this, formatedDate, Toast.LENGTH_SHORT).show();
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


    @Override
    public void clicked(int size) {
        Toast.makeText(this,uri.size()+": selected",Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
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

    @Override
    public void clicks(int size) {
        Toast.makeText(this,uriShooter.size()+": selected",Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void itemClicks(int position) {
        Dialog dialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        dialog.setContentView(R.layout.custom_dialog_zoom);

        TextView textView = dialog.findViewById(R.id.text_dialog);
        ImageView imageView = dialog.findViewById(R.id.image_view_dialog);
        Button buttonClose = dialog.findViewById(R.id.button_close_dialog);


        textView.setText("Image"+position);
        Glide.with( this)
                .load(uriShooter.get(position))
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


    private class TextChange implements TextWatcher {


        View view;
        private TextChange (View v) {
            view = v;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onTextChanged(CharSequence s, int i, int i1, int i2) {

            switch (view.getId()) {

                case R.id.reg_experienceEdit:
                    if(s.length() == 0){
                        reg_experience.setHelperText("Please enter the number of years of experience");
                        reg_experience.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    }
                    else{
                        reg_experience.setHelperText("");
                    }
                    break;
                case R.id.reg_kennelNameEdit:
                    if(s.length() == 0){
                        reg_kennelName.setHelperText("Please enter your registered kennel name");
                        reg_kennelName.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    }
                    else{
                        reg_kennelName.setHelperText("");
                    }
                    break;
                case R.id.reg_prc_id_edit:
                    if(s.length() == 0){
                        reg_prc_id.setHelperText("Please enter your PRC id no.");
                        reg_prc_id.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    }
                    else if(s.length() < 7){
                        reg_prc_id.setHelperText("PRC id no. must have 7 digits");
                        reg_prc_id.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    }
                    else{
                        reg_prc_id.setHelperText("");
                    }
                    break;
                case R.id.reg_memberID_edit:
                    if(s.length() == 0){
                        reg_memberID.setHelperText("Please enter your Member id");
                        reg_memberID.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    }
                    else if(s.length() < 5){
                        reg_memberID.setHelperText("Member id must have 5 digits");
                        reg_memberID.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    }
                    else{
                        reg_memberID.setHelperText("");
                    }
                    break;
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

}
