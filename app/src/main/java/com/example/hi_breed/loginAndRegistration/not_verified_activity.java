package com.example.hi_breed.loginAndRegistration;

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
<<<<<<< HEAD
import android.os.Handler;
=======
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
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
<<<<<<< HEAD
import androidx.appcompat.widget.AppCompatImageView;
=======
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.hi_breed.R;
import com.example.hi_breed.adapter.breeder_kennel_cert_RecyclerAdapter;
import com.example.hi_breed.adapter.shooterAdapter.shooter_proof_RecyclerAdapter;
<<<<<<< HEAD
import com.example.hi_breed.classesFile.BaseActivity;
import com.example.hi_breed.screenLoading.screen_splashScreen_MainActivity;
=======
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
import com.example.hi_breed.userFile.dashboard.user_dashboard;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
<<<<<<< HEAD
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
=======
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

<<<<<<< HEAD
import pl.droidsonroids.gif.GifImageView;

public class not_verified_activity extends BaseActivity implements breeder_kennel_cert_RecyclerAdapter.CountOfImagesWhenRemoved, breeder_kennel_cert_RecyclerAdapter.itemClickListener, shooter_proof_RecyclerAdapter.itemClickListeners, shooter_proof_RecyclerAdapter.CountOfImagesWhenRemoves {
=======
public class not_verified_activity extends AppCompatActivity implements breeder_kennel_cert_RecyclerAdapter.CountOfImagesWhenRemoved, breeder_kennel_cert_RecyclerAdapter.itemClickListener, shooter_proof_RecyclerAdapter.itemClickListeners,shooter_proof_RecyclerAdapter.CountOfImagesWhenRemoves{

>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02


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

<<<<<<< HEAD
    RecyclerView recyclerView, recyclerView_gallery_images_shooter;
=======
    RecyclerView recyclerView,recyclerView_gallery_images_shooter;
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
    breeder_kennel_cert_RecyclerAdapter adapter;
    shooter_proof_RecyclerAdapter shooter_adapter;

    ArrayList<Uri> uri = new ArrayList<>();
    Uri imageUri;
<<<<<<< HEAD
    ArrayList<Uri> uriShooter = new ArrayList<>();
=======
    ArrayList <Uri> uriShooter = new ArrayList<>();
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
    Uri imageUriShooter;
    Uri imgVet;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<Intent> activityResultLauncherShooter;

<<<<<<< HEAD
    CardView dropImageCardView, dropImageCardViewShooter, dropImageCardViewVet;
    ImageView dropImageView, dropImageViewVet, prc_image, imageview_vet_style;
    Button submit;
    RelativeLayout reg_transaction, reg_date_registered;
    TextInputLayout reg_memberID, reg_prc_id, reg_kennelName;
    TextInputEditText reg_memberID_edit, reg_prc_id_edit, reg_experienceEdit, reg_kennelNameEdit;
    TextView reg_date_registered_edit, prc_required_error, add_photo_vet, dropImageTextVIew, reg_transactionEdit, textViewExp, changeRole;
    String birth = "";
    LinearLayout memberLayouts, shooter_validation_linearLayout, breeder_validation_linearLayout, vet_validation_linear_layout;
=======
    CardView dropImageCardView,dropImageCardViewShooter,dropImageCardViewVet;
    ImageView dropImageView,dropImageViewVet,prc_image,imageview_vet_style;
    Button submit;
    RelativeLayout reg_transaction,reg_date_registered;
    TextInputLayout reg_memberID,reg_prc_id,reg_kennelName;
    TextInputEditText reg_memberID_edit,reg_prc_id_edit,reg_experienceEdit,reg_kennelNameEdit;
    TextView reg_date_registered_edit,prc_required_error,add_photo_vet,dropImageTextVIew,reg_transactionEdit,textViewExp,changeRole;
    String birth="";
    LinearLayout memberLayouts,shooter_validation_linearLayout,breeder_validation_linearLayout,vet_validation_linear_layout;
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
    TextInputLayout reg_experience;
    ArrayList<String> role;
    ArrayList<String> saveRole;
    RelativeLayout validationLayout;

<<<<<<< HEAD
=======

>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_verified);

        Window window = getWindow();
<<<<<<< HEAD
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.setStatusBarColor(Color.parseColor("#e28743"));
        }

        backLayoutService = findViewById(R.id.backLayoutService);
=======
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

>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
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
<<<<<<< HEAD
        shooter_adapter = new shooter_proof_RecyclerAdapter(uriShooter, this, this, this);
        adapter = new breeder_kennel_cert_RecyclerAdapter(uri, this, this, this);
        //RecyclerView
        recyclerView_gallery_images_shooter.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView_gallery_images_shooter.setAdapter(shooter_adapter);
        memberLayouts = findViewById(R.id.memberLayouts);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(adapter);


        validationLayout = findViewById(R.id.validationLayout);
=======
        shooter_adapter = new shooter_proof_RecyclerAdapter(uriShooter,this,this,this);
        adapter = new breeder_kennel_cert_RecyclerAdapter(uri,this,this, this);
        //RecyclerView
        recyclerView_gallery_images_shooter.setLayoutManager(new GridLayoutManager(this,4));
        recyclerView_gallery_images_shooter.setAdapter(shooter_adapter);
        memberLayouts = findViewById(R.id.memberLayouts);
        recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        recyclerView.setAdapter(adapter);


        validationLayout =findViewById(R.id.validationLayout);
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
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
<<<<<<< HEAD
        reg_memberID_edit = findViewById(R.id.reg_memberID_edit);
        reg_memberID = findViewById(R.id.reg_memberID);
        reg_prc_id_edit = findViewById(R.id.reg_prc_id_edit);
        reg_prc_id = findViewById(R.id.reg_prc_id);
        reg_date_registered = findViewById(R.id.reg_date_registered);
        reg_date_registered_edit = findViewById(R.id.reg_date_registered_edit);
=======
        reg_memberID_edit= findViewById(R.id.reg_memberID_edit);
        reg_memberID= findViewById(R.id.reg_memberID);
        reg_prc_id_edit= findViewById(R.id.reg_prc_id_edit);
        reg_prc_id= findViewById(R.id.reg_prc_id);
        reg_date_registered= findViewById(R.id.reg_date_registered);
        reg_date_registered_edit= findViewById(R.id.reg_date_registered_edit);
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02

// variable initialized
        //TextView

        prc_required_error = findViewById(R.id.prc_required_error);
        add_photo_vet = findViewById(R.id.add_photo_vet);
        textViewExp = findViewById(R.id.textViewExp);
        dropImageTextVIew = findViewById(R.id.dropImageTextView);

        reg_transactionEdit = findViewById(R.id.reg_transactionEdit);
        //cardView
        dropImageCardView = findViewById(R.id.dropImageCardView);
<<<<<<< HEAD
        dropImageCardViewShooter = findViewById(R.id.dropImageCardViewShooter);
        dropImageCardViewVet = findViewById(R.id.dropImageCardViewVet);

        //ImageView
        dropImageView = findViewById(R.id.dropImageView);
=======
        dropImageCardViewShooter =findViewById(R.id.dropImageCardViewShooter);
        dropImageCardViewVet = findViewById(R.id.dropImageCardViewVet);

        //ImageView
        dropImageView =findViewById(R.id.dropImageView);
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
        dropImageViewVet = findViewById(R.id.dropImageViewVet);
        prc_image = findViewById(R.id.prc_image);
        imageview_vet_style = findViewById(R.id.imageview_vet_style);
        //Relative Layout

        reg_transaction = findViewById(R.id.reg_transaction);

        //button
        submit = findViewById(R.id.submitButton);


<<<<<<< HEAD
        reg_experience = findViewById(R.id.reg_experience);
        reg_kennelName = findViewById(R.id.reg_kennelName);


=======

        reg_experience =findViewById(R.id.reg_experience);
        reg_kennelName = findViewById(R.id.reg_kennelName);



>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
        reg_experienceEdit = findViewById(R.id.reg_experienceEdit);

        reg_kennelNameEdit = findViewById(R.id.reg_kennelNameEdit);


<<<<<<< HEAD
=======

>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
        reg_experienceEdit.addTextChangedListener(new TextChange(reg_experienceEdit));

        reg_kennelNameEdit.addTextChangedListener(new TextChange(reg_kennelNameEdit));

        reg_prc_id_edit.addTextChangedListener(new TextChange(reg_prc_id_edit));

        reg_memberID_edit.addTextChangedListener(new TextChange(reg_memberID_edit));


        //Getting the input from 1st fragment Details
        role = new ArrayList<>();
        saveRole = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("User")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
<<<<<<< HEAD
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) return;
                        if (value.exists()) {
                            role = (ArrayList<String>) value.get("role");
                            birth = value.getString("birth");
                            changeRole.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(not_verified_activity.this, change_role.class));
                                    finish();
                                }
                            });

                            TextView dropImageTextView = findViewById(R.id.dropImageTextView);
                            TextView breederPCCI = findViewById(R.id.breederPCCI);
                            ImageView breederPCCIImage = findViewById(R.id.breederPCCIImage);
                            ImageView imagePCCI_CERT = findViewById(R.id.imagePCCI_CERT);
                            ImageView shooter_pcci_id = findViewById(R.id.shooter_pcci_id);
                            ImageView shooter_image_example = findViewById(R.id.shooter_image_example);
                            if (role.contains("Pet Shooter") && role.contains("Pet Breeder") && role.contains("Pet Owner") && role.contains("Veterinarian")) {
                                //hide pcci on breeder
                                breederPCCI.setVisibility(View.GONE);
                                breederPCCIImage.setVisibility(View.GONE);
                                imagePCCI_CERT.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        imageViewer(R.drawable.pcci_certificate);
                                    }
                                });
                                shooter_pcci_id.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        imageViewer(R.drawable.pcci_id);
                                    }
                                });
                                shooter_image_example.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        imageViewer(R.drawable.shooter_image);
                                    }
                                });
                            } else if (role.contains("Pet Shooter") && role.contains("Pet Breeder") && role.contains("Pet Owner")) {
                                //hide pcci on breeder
                                breederPCCI.setVisibility(View.GONE);
                                breederPCCIImage.setVisibility(View.GONE);
                                imagePCCI_CERT.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        imageViewer(R.drawable.pcci_certificate);
                                    }
                                });
                                shooter_pcci_id.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        imageViewer(R.drawable.pcci_id);
                                    }
                                });
                                shooter_image_example.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        imageViewer(R.drawable.shooter_image);
                                    }
                                });
                            } else if (role.contains("Pet Breeder")) {
                                //show pcci on breeder
                                imagePCCI_CERT.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        imageViewer(R.drawable.pcci_certificate);
                                    }
                                });
                                breederPCCIImage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        imageViewer(R.drawable.pcci_id);
                                    }
                                });
                                breederPCCI.setVisibility(View.VISIBLE);
                                breederPCCIImage.setVisibility(View.VISIBLE);
                                dropImageTextView.setText("Certificate for Kennel Name and ID (PCCI)");
                            }
                            else if(role.contains("Pet Shooter")){
                                //hide pcci on breeder
                                breederPCCI.setVisibility(View.GONE);
                                breederPCCIImage.setVisibility(View.GONE);
                                imagePCCI_CERT.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        imageViewer(R.drawable.pcci_certificate);
                                    }
                                });
                                shooter_pcci_id.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        imageViewer(R.drawable.pcci_id);
                                    }
                                });
                                shooter_image_example.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        imageViewer(R.drawable.shooter_image);
                                    }
                                });
                            }

                            if (role.contains("Pet Owner")) {
                                saveRole.add("Pet Owner");
                            }
                            if (role.contains("Pet Breeder") || role.contains("Pet Shooter") || role.contains("Veterinarian")) {
                                memberLayouts.setVisibility(View.VISIBLE);

                                if (role.contains("Pet Breeder")) {
                                    saveRole.add("Pet Breeder");
                                    breeder_validation_linearLayout.setVisibility(View.VISIBLE);
                                }
                                if (role.contains("Pet Shooter")) {
                                    saveRole.add("Pet Shooter");
                                    shooter_validation_linearLayout.setVisibility(View.VISIBLE);
                                }

                                if (role.contains("Veterinarian")) {
                                    saveRole.add("Veterinarian");
                                    vet_validation_linear_layout.setVisibility(View.VISIBLE);
                                }

                            } else {
                                startActivity(new Intent(not_verified_activity.this, user_dashboard.class));
                                finish();
                                submit.setVisibility(View.GONE);
                                validationLayout.setVisibility(View.GONE);
                                memberLayouts.setVisibility(View.GONE);
                            }

                        }
                    }
                });

        //LinearLayout
        shooter_validation_linearLayout = findViewById(R.id.shooter_validation_linearLayout);
        breeder_validation_linearLayout = findViewById(R.id.breeder_validation_linearLayout);
        vet_validation_linear_layout = findViewById(R.id.vet_validation_linear_layout);
=======
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        role = (ArrayList<String>) documentSnapshot.get("role");
                        birth = documentSnapshot.getString("birth");
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

>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02


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
<<<<<<< HEAD
                                for (int i = 0; i < countOfImages; i++) {
=======
                                for (int i = 0 ; i < countOfImages; i++) {
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
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

<<<<<<< HEAD
                            } else {
                                if (uri.size() < 5) {
=======
                            }else{
                                if(uri.size() <5){
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                                    //this is for to get the single images
                                    imageUri = result.getData().getData();
                                    //add the code into
                                    uri.add(imageUri);
<<<<<<< HEAD
                                } else {
=======
                                }else{
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                                    Toast.makeText(not_verified_activity.this, "Not Allowed to pick more than 5 images", Toast.LENGTH_SHORT).show();
                                }
                            }
                            adapter.notifyDataSetChanged();
<<<<<<< HEAD
                            Toast.makeText(not_verified_activity.this, uri.size() + ": selected", Toast.LENGTH_SHORT).show();
                        } else {
=======
                            Toast.makeText(not_verified_activity.this,uri.size()+": selected",Toast.LENGTH_SHORT).show();
                        }else{
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
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
<<<<<<< HEAD
                                for (int i = 0; i < getCountOfImagesShoot; i++) {
=======
                                for (int i = 0 ; i < getCountOfImagesShoot; i++) {
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
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

<<<<<<< HEAD
                            } else {
                                if (uriShooter.size() < 5) {
=======
                            }else{
                                if(uriShooter.size() <5){
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                                    //this is for to get the single images
                                    imageUriShooter = result.getData().getData();
                                    //add the code into
                                    uriShooter.add(imageUriShooter);
<<<<<<< HEAD
                                } else {
=======
                                }else{
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                                    Toast.makeText(not_verified_activity.this, "Not Allowed to pick more than 5 images", Toast.LENGTH_SHORT).show();
                                }
                            }
                            shooter_adapter.notifyDataSetChanged();
<<<<<<< HEAD
                            Toast.makeText(not_verified_activity.this, uriShooter.size() + ": selected", Toast.LENGTH_SHORT).show();
                        } else {
=======
                            Toast.makeText(not_verified_activity.this,uriShooter.size()+": selected",Toast.LENGTH_SHORT).show();
                        }else{
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
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
<<<<<<< HEAD
                if (checkPermission()) {
                    imagePermissionVet();
                } else {
=======
                if( checkPermission()){
                    imagePermissionVet();
                }
                else{
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
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
<<<<<<< HEAD
        submit = findViewById(R.id.submitButton);
=======
        submit =  findViewById(R.id.submitButton);
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInput();
            }
        });


    }

<<<<<<< HEAD
    private void imageViewer(int pcci_certificate) {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        dialog.setContentView(R.layout.custom_dialog_zoom);

        ImageView imageView = dialog.findViewById(R.id.image_view_dialog);
        Button buttonClose = dialog.findViewById(R.id.button_close_dialog);

        Picasso.get()
                .load(pcci_certificate)
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

    @SuppressLint("SetTextI18n")
    private void openDateRegistered() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.pet_add_birthday_dialog, null);
        DatePicker datePicker = view.findViewById(R.id.datePicker);
        datePicker.setMaxDate(System.currentTimeMillis());
        builder.setView(view);
        MaterialButton cancel, save;
        TextView message, title;
        title = view.findViewById(R.id.pet_add_birthday_set_title);
=======
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
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
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
<<<<<<< HEAD
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();
=======
                int   day  = datePicker.getDayOfMonth();
                int   month= datePicker.getMonth();
                int   year = datePicker.getYear();
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
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
<<<<<<< HEAD
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
=======
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        }

        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE);
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
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
<<<<<<< HEAD

=======
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            new AlertDialog.Builder(not_verified_activity.this)
                    .setTitle("Permission")
                    .setMessage("Please give the Storage permission")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
<<<<<<< HEAD
                        public void onClick(DialogInterface dialog, int which) {
=======
                        public void onClick( DialogInterface dialog, int which ) {
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
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
<<<<<<< HEAD
                        public void onClick(DialogInterface dialog, int which) {
=======
                        public void onClick( DialogInterface dialog, int which ) {
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
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

<<<<<<< HEAD
            ActivityCompat.requestPermissions(not_verified_activity.this, permissions, 10);
=======
            ActivityCompat.requestPermissions( not_verified_activity.this, permissions, 10);
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02

        }

    }


    @SuppressLint("ObsoleteSdkInt")
    private void imagePermissionShooter() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
<<<<<<< HEAD
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_Shooter);

    }

    String resultsForCover = "";

=======
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        }

        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_Shooter);

    }

    String resultsForCover="";
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

<<<<<<< HEAD
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {

            //this part is for to get multiple images
            if (data.getClipData() != null) {

                countOfImages = data.getClipData().getItemCount();
                for (int j = 0; j < countOfImages; j++) {

                    if (uri.size() < 5) {
                        imageUri = data.getClipData().getItemAt(j).getUri();
                        uri.add(imageUri);
                    } else {
                        Toast.makeText(this, "Not allowed to pick more than 5 images", Toast.LENGTH_SHORT).show();
=======
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
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                    }
                }
                //then notify the adapter
                adapter.notifyDataSetChanged();
<<<<<<< HEAD
                Toast.makeText(this, uri.size() + ": selected", Toast.LENGTH_SHORT).show();
            } else {
                //this is for to get the single images
                if (uri.size() < 5) {
                    imageUri = data.getData();
                    uri.add(imageUri);
                } else {
                    Toast.makeText(this, "Not allowed to pick more than 5 images", Toast.LENGTH_SHORT).show();
                }
            }
            adapter.notifyDataSetChanged();
            Toast.makeText(this, uri.size() + ": selected", Toast.LENGTH_SHORT).show();
        } else if (requestCode == PICK_IMAGE_Shooter && resultCode == RESULT_OK && null != data) {

            //this part is for to get multiple images
            if (data.getClipData() != null) {

                getCountOfImagesShoot = data.getClipData().getItemCount();
                for (int j = 0; j < getCountOfImagesShoot; j++) {

                    if (uri.size() < 5) {
                        imageUriShooter = data.getClipData().getItemAt(j).getUri();
                        uriShooter.add(imageUriShooter);
                    } else {
                        Toast.makeText(this, "Not allowed to pick more than 5 images", Toast.LENGTH_SHORT).show();
=======
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
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                    }
                }
                //then notify the adapter
                shooter_adapter.notifyDataSetChanged();
<<<<<<< HEAD
                Toast.makeText(this, uriShooter.size() + ": selected", Toast.LENGTH_SHORT).show();
            } else {
                //this is for to get the single images
                if (uriShooter.size() < 5) {
                    imageUriShooter = data.getData();
                    uriShooter.add(imageUriShooter);
                } else {
                    Toast.makeText(this, "Not allowed to pick more than 5 images", Toast.LENGTH_SHORT).show();
                }
            }
            shooter_adapter.notifyDataSetChanged();
            Toast.makeText(this, uriShooter.size() + ": selected", Toast.LENGTH_SHORT).show();
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_PICK_IMAGE) {
            Uri imageUri = data.getData();
            cropImage(imageUri);
        } else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            Uri croppedUri = UCrop.getOutput(data);
            // Use the cropped image URI as needed
            if (croppedUri != null) {
=======
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
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
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
<<<<<<< HEAD
        } else {
            //this code is for if user not picked any image
            Toast.makeText(this, "You Haven't Picked any image", Toast.LENGTH_SHORT).show();
=======
        }
        else {
            //this code is for if user not picked any image
            Toast.makeText(this,"You Haven't Picked any image",Toast.LENGTH_SHORT).show();
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
        }
    }

    private void cropImage(Uri sourceUri) {
        File cacheDir = not_verified_activity.this.getCacheDir();
        Uri destinationUri = Uri.fromFile(new File(cacheDir, "cropped"));
        UCrop uCrop = UCrop.of(sourceUri, destinationUri);
<<<<<<< HEAD
        uCrop.start(this);
=======
        uCrop.start( this);
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
    }

    private void checkInput() {


        String exp = reg_experienceEdit.getText().toString().trim();
        String kennel = reg_kennelNameEdit.getText().toString().trim();
        String memberID = reg_memberID_edit.getText().toString().trim();
<<<<<<< HEAD
        String prcID = reg_prc_id_edit.getText().toString().trim();
        String date = reg_date_registered_edit.getText().toString().trim();

        Bundle bundle = new Bundle();

        // to check if what is the role of the user
        if (role.contains("Pet Breeder") || role.contains("Pet Shooter") || role.contains("Veterinarian")) {

            if (role.contains("Pet Breeder")) {

                if (kennel.isEmpty()) {
=======
        String prcID =  reg_prc_id_edit.getText().toString().trim();
        String date = reg_date_registered_edit.getText().toString().trim();


        Bundle bundle = new Bundle();

        // to check if what is the role of the user
        if(role.contains("Pet Breeder") || role.contains("Pet Shooter")|| role.contains("Veterinarian")){

            if(role.contains("Pet Breeder")){

                if(kennel.isEmpty()){
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                    reg_kennelName.setHelperText("Please input your registered kennel name");
                    reg_kennelName.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    reg_kennelNameEdit.requestFocus();
                    return;
                }
<<<<<<< HEAD

                if (uri.size() == 0) {
=======
                if(uri.size()==0){
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                    Toast.makeText(this, "Certificate is needed", Toast.LENGTH_SHORT).show();

                    return;
                }
<<<<<<< HEAD

                if ((role.contains("Pet Breeder") && role.contains("Pet Owner")) && role.size() == 2) {
                    if (uri.size() < 2) {
                        Toast.makeText(this, "Kennel Certificate and PCCI (ID) are needed", Toast.LENGTH_SHORT).show();
                        dropImageTextVIew.setFocusableInTouchMode(true);
                        dropImageTextVIew.setFocusable(true);
                        return;
                    }
                }
                bundle.putString("kennel", kennel);
                bundle.putParcelableArrayList("uriBreeder", uri);
            }
            if (role.contains("Pet Shooter")) {

                if (reg_transactionEdit.getText().toString().equals("Date of last transaction . . .")) {
=======
                bundle.putString("kennel",kennel);
                bundle.putParcelableArrayList("uriBreeder",uri);
            }
            if(role.contains("Pet Shooter")){

                if(reg_transactionEdit.getText().toString().equals("Date of last transaction . . .")){
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                    reg_transactionEdit.setFocusable(true);
                    reg_transactionEdit.setFocusableInTouchMode(true);
                    Toast.makeText(this, "Date for your last transaction is empty", Toast.LENGTH_SHORT).show();
                    reg_transactionEdit.requestFocus();
                    return;
                }

<<<<<<< HEAD
                bundle.putString("lastTransaction", reg_transactionEdit.getText().toString());

                if (uriShooter.size() == 0) {
                    Toast.makeText(this, "Proof photos of successful stud/made are needed", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (uriShooter.size() == 1) {
                    Toast.makeText(this, "Proof photos of successful stud/made and PCCI ID are needed", Toast.LENGTH_SHORT).show();
                    return;
                }

                bundle.putParcelableArrayList("uriShooter", uriShooter);

            }
            if (role.contains("Veterinarian")) {
                if (imgVet == null) {
=======
                bundle.putString("lastTransaction",reg_transactionEdit.getText().toString());

                if(uriShooter.size()==0){
                    Toast.makeText(this, "Proof photos of successful stud/made are needed", Toast.LENGTH_SHORT).show();
                    return;
                }
                bundle.putParcelableArrayList("uriShooter",uriShooter);

            }
            if(role.contains("Veterinarian")){
                if(imgVet == null){
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                    prc_required_error.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "Please provide a picture of your License ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                prc_required_error.setVisibility(View.GONE);
<<<<<<< HEAD
                bundle.putString("vet_image", imgVet.toString());

                if (prcID.isEmpty()) {
=======
                bundle.putString("vet_image",imgVet.toString());

                if(prcID.isEmpty()){
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                    reg_prc_id_edit.setFocusable(true);
                    reg_prc_id_edit.setFocusableInTouchMode(true);
                    Toast.makeText(this, "PRC ID No. is empty it is required to fill in your ID No.", Toast.LENGTH_SHORT).show();
                    reg_prc_id_edit.requestFocus();
                    return;
<<<<<<< HEAD
                } else if (prcID.length() < 7) {
=======
                }
                else if(prcID.length() < 7){
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                    reg_prc_id.setHelperText("PRC ID No. must have 7 digits");
                    reg_prc_id.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    reg_prc_id_edit.requestFocus();
                    return;
                }
<<<<<<< HEAD
                bundle.putString("prcNo", prcID);
            }

            if (exp.isEmpty()) {
=======
                bundle.putString("prcNo",prcID);
            }

            if(exp.isEmpty()) {
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                reg_experience.setHelperText("Please input number of experience");
                reg_experience.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                reg_experienceEdit.requestFocus();
                return;
            }
<<<<<<< HEAD
            bundle.putString("exp", exp);
            if (memberID.isEmpty()) {
=======
            bundle.putString("exp",exp);
            if(memberID.isEmpty()){
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                reg_memberID.setHelperText("Please input member ID");
                reg_memberID.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                reg_memberID_edit.requestFocus();
                return;
<<<<<<< HEAD
            } else if (memberID.length() < 5) {
=======
            }
            else if(memberID.length() < 5){
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                reg_memberID.setHelperText("Member ID should have 5 digits");
                reg_memberID.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                reg_memberID_edit.requestFocus();
                return;
            }
<<<<<<< HEAD
            bundle.putString("memberID", memberID);
            if (date.isEmpty()) {
=======
            bundle.putString("memberID",memberID);
            if(date.isEmpty()){
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                reg_date_registered_edit.setFocusable(true);
                reg_date_registered_edit.setFocusableInTouchMode(true);
                reg_date_registered_edit.requestFocus();
                Toast.makeText(this, "Date of registration is empty", Toast.LENGTH_SHORT).show();
                return;
            }

<<<<<<< HEAD
            bundle.putString("dateOfRegistration", date);
        }

        //sending all the data to next fragment
        bundle.putStringArrayList("roles", saveRole);
        if (role.contains("Veterinarian") || role.contains("Pet Shooter") || role.contains("Pet Breeder")) {
=======
            bundle.putString("dateOfRegistration",date);
        }
        Toast.makeText(this, date+" "+birth + " "+kennel, Toast.LENGTH_SHORT).show();

        //sending all the data to next fragment
        bundle.putStringArrayList("roles",saveRole);
        if(role.contains("Veterinarian") || role.contains("Pet Shooter") || role.contains("Pet Breeder")) {
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
            if (role.contains("Veterinarian") && role.contains("Pet Breeder")) {
                FirebaseFirestore.getInstance().collection("Members")
                        .whereEqualTo("memberId", memberID)
                        .whereEqualTo("dateOfRegistration", date)
                        .whereEqualTo("prc_id_no", prcID)
                        .whereEqualTo("kennelName", kennel)
                        .whereEqualTo("birthday", birth)
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    FirebaseFirestore.getInstance().collection("User")
<<<<<<< HEAD
                                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .update("status", "verified")
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(not_verified_activity.this);
                                                    builder2.setCancelable(false);
                                                    View view = View.inflate(not_verified_activity.this, R.layout.screen_custom_alert, null);
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
                                                    title.setText("Your account is Verified");
                                                    message.setText("Enjoy using our application share it with your friends!");
                                                    //button
                                                    LinearLayout buttonLayout = view.findViewById(R.id.screen_custom_alert_buttonLayout);
                                                    buttonLayout.setVisibility(View.VISIBLE);
                                                    MaterialButton cancel, okay;
                                                    cancel = view.findViewById(R.id.screen_custom_dialog_btn_cancel);
                                                    cancel.setVisibility(View.GONE);
                                                    okay = view.findViewById(R.id.screen_custom_alert_dialog_btn_done);
                                                    okay.setText("OKAY");
                                                    okay.setBackgroundColor(Color.parseColor("#F6B75A"));
                                                    okay.setTextColor(Color.WHITE);
                                                    builder2.setView(view);
                                                    AlertDialog alert2 = builder2.create();
                                                    alert2.show();
                                                    alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                    okay.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            startActivity(new Intent(not_verified_activity.this, user_dashboard.class));
                                                            finish();
                                                        }
                                                    });
                                                }
                                            });

                                } else {
                                    Toast.makeText(getApplicationContext(), "Some fields are not matched in your inputted", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getApplicationContext(), "Account is still not verified", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else if (role.contains("Pet Breeder")) {
=======
                                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .update("status","verified");
                                    Toast.makeText(getApplicationContext(), "User status is verified", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(not_verified_activity.this,user_dashboard.class));
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Some fields are not matched in your inputted", Toast.LENGTH_SHORT).show();
                                }
                            }
                  });
            }
            else if (role.contains("Pet Breeder")) {
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                FirebaseFirestore.getInstance().collection("Members")
                        .whereEqualTo("memberId", memberID)
                        .whereEqualTo("dateOfRegistration", date)
                        .whereEqualTo("kennelName", kennel)
                        .whereEqualTo("birthday", birth)
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    FirebaseFirestore.getInstance().collection("User")
                                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
<<<<<<< HEAD
                                            .update("status", "verified").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(not_verified_activity.this);
                                                    builder2.setCancelable(false);
                                                    View view = View.inflate(not_verified_activity.this, R.layout.screen_custom_alert, null);
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
                                                    title.setText("Your account is Verified");
                                                    message.setText("Enjoy using our application share it with your friends!");
                                                    //button
                                                    LinearLayout buttonLayout = view.findViewById(R.id.screen_custom_alert_buttonLayout);
                                                    buttonLayout.setVisibility(View.VISIBLE);
                                                    MaterialButton cancel, okay;
                                                    cancel = view.findViewById(R.id.screen_custom_dialog_btn_cancel);
                                                    cancel.setVisibility(View.GONE);
                                                    okay = view.findViewById(R.id.screen_custom_alert_dialog_btn_done);
                                                    okay.setText("OKAY");
                                                    okay.setBackgroundColor(Color.parseColor("#F6B75A"));
                                                    okay.setTextColor(Color.WHITE);
                                                    builder2.setView(view);
                                                    AlertDialog alert2 = builder2.create();
                                                    alert2.show();
                                                    alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                    okay.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            startActivity(new Intent(not_verified_activity.this, user_dashboard.class));
                                                            finish();
                                                        }
                                                    });
                                                }
                                            });
                                } else {
                                    Toast.makeText(getApplicationContext(), "Some fields are not matched in your inputted", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getApplicationContext(), "Account is still not verified", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else if (role.contains("Veterinarian")) {
=======
                                            .update("status","verified");
                                    Toast.makeText(getApplicationContext(), "User status is verified", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(not_verified_activity.this,user_dashboard.class));
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Some fields are not matched in your inputted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            else  if (role.contains("Veterinarian")){
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                FirebaseFirestore.getInstance().collection("Members")
                        .whereEqualTo("memberId", memberID)
                        .whereEqualTo("dateOfRegistration", date)
                        .whereEqualTo("prc_id_no", prcID)
                        .whereEqualTo("birthday", birth)
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    FirebaseFirestore.getInstance().collection("User")
                                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
<<<<<<< HEAD
                                            .update("status", "verified").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(not_verified_activity.this);
                                                    builder2.setCancelable(false);
                                                    View view = View.inflate(not_verified_activity.this, R.layout.screen_custom_alert, null);
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
                                                    title.setText("Your account is Verified");
                                                    message.setText("Enjoy using our application share it with your friends!");
                                                    //button
                                                    LinearLayout buttonLayout = view.findViewById(R.id.screen_custom_alert_buttonLayout);
                                                    buttonLayout.setVisibility(View.VISIBLE);
                                                    MaterialButton cancel, okay;
                                                    cancel = view.findViewById(R.id.screen_custom_dialog_btn_cancel);
                                                    cancel.setVisibility(View.GONE);
                                                    okay = view.findViewById(R.id.screen_custom_alert_dialog_btn_done);
                                                    okay.setText("OKAY");
                                                    okay.setBackgroundColor(Color.parseColor("#F6B75A"));
                                                    okay.setTextColor(Color.WHITE);
                                                    builder2.setView(view);
                                                    AlertDialog alert2 = builder2.create();
                                                    alert2.show();
                                                    alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                    okay.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            startActivity(new Intent(not_verified_activity.this, user_dashboard.class));
                                                            finish();
                                                        }
                                                    });
                                                }
                                            });

                                } else {
                                    Toast.makeText(getApplicationContext(), "Some fields are not matched in your inputted", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getApplicationContext(), "Account is still not verified", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            } else if (role.contains("Pet Shooter")) {
=======
                                            .update("status","verified");
                                    Toast.makeText(getApplicationContext(), "User status is verified", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(not_verified_activity.this,user_dashboard.class));
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Some fields are not matched in your inputted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            else if (role.contains("Pet Shooter")){
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                FirebaseFirestore.getInstance().collection("Members")
                        .whereEqualTo("memberId", memberID)
                        .whereEqualTo("dateOfRegistration", date)
                        .whereEqualTo("birthday", birth)
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    FirebaseFirestore.getInstance().collection("User")
                                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
<<<<<<< HEAD
                                            .update("status", "verified").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(not_verified_activity.this);
                                                    builder2.setCancelable(false);
                                                    View view = View.inflate(not_verified_activity.this, R.layout.screen_custom_alert, null);
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
                                                    title.setText("Your account is Verified");
                                                    message.setText("Enjoy using our application share it with your friends!");
                                                    //button
                                                    LinearLayout buttonLayout = view.findViewById(R.id.screen_custom_alert_buttonLayout);
                                                    buttonLayout.setVisibility(View.VISIBLE);
                                                    MaterialButton cancel, okay;
                                                    cancel = view.findViewById(R.id.screen_custom_dialog_btn_cancel);
                                                    cancel.setVisibility(View.GONE);
                                                    okay = view.findViewById(R.id.screen_custom_alert_dialog_btn_done);
                                                    okay.setText("OKAY");
                                                    okay.setBackgroundColor(Color.parseColor("#F6B75A"));
                                                    okay.setTextColor(Color.WHITE);
                                                    builder2.setView(view);
                                                    AlertDialog alert2 = builder2.create();
                                                    alert2.show();
                                                    alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                    okay.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            startActivity(new Intent(not_verified_activity.this, user_dashboard.class));
                                                            finish();
                                                        }
                                                    });
                                                }
                                            });

                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Some fields are not matched in your inputted", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getApplicationContext(), "Your account is still not verified.", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            } else {
                Toast.makeText(this, "Verify your phone number go to account settings", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(not_verified_activity.this, user_dashboard.class));
                finish();
            }
        }
    }//end of check input method
=======
                                            .update("status","verified");
                                    Toast.makeText(getApplicationContext(), "User status is verified", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(not_verified_activity.this,user_dashboard.class));
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Some fields are not matched in your inputted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            else if(role.size() == 1 && role.contains("Pet Owner")){
                Toast.makeText(this, "Verify your phone number go to account settings", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(not_verified_activity.this,user_dashboard.class));
                finish();
            }
        }
   }//end of check input method
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02

    @SuppressLint("SetTextI18n")
    private void openTransactionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
<<<<<<< HEAD
        View view = View.inflate(this, R.layout.pet_add_birthday_dialog, null);
        DatePicker datePicker = view.findViewById(R.id.datePicker);
        datePicker.setMaxDate(System.currentTimeMillis());

        MaterialButton cancel, save;
        TextView message, title;
=======
        View view = View.inflate(this,R.layout.pet_add_birthday_dialog,null);
        DatePicker datePicker = view.findViewById(R.id.datePicker);
        datePicker.setMaxDate(System.currentTimeMillis());

        MaterialButton cancel,save;
        TextView message,title;
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
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
<<<<<<< HEAD
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();
=======
                int   day  = datePicker.getDayOfMonth();
                int   month= datePicker.getMonth();
                int   year = datePicker.getYear();
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
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
<<<<<<< HEAD
        Toast.makeText(this, uri.size() + ": selected", Toast.LENGTH_SHORT).show();
=======
        Toast.makeText(this,uri.size()+": selected",Toast.LENGTH_SHORT).show();
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void itemClick(int position) {
<<<<<<< HEAD
        Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
=======
        Dialog dialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02

        dialog.setContentView(R.layout.custom_dialog_zoom);

        TextView textView = dialog.findViewById(R.id.text_dialog);
        ImageView imageView = dialog.findViewById(R.id.image_view_dialog);
        Button buttonClose = dialog.findViewById(R.id.button_close_dialog);


<<<<<<< HEAD
        textView.setText("Image" + position);
        Glide.with(this)
=======
        textView.setText("Image"+position);
        Glide.with( this)
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
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
<<<<<<< HEAD
        Toast.makeText(this, uriShooter.size() + ": selected", Toast.LENGTH_SHORT).show();
=======
        Toast.makeText(this,uriShooter.size()+": selected",Toast.LENGTH_SHORT).show();
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void itemClicks(int position) {
<<<<<<< HEAD
        Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
=======
        Dialog dialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02

        dialog.setContentView(R.layout.custom_dialog_zoom);

        TextView textView = dialog.findViewById(R.id.text_dialog);
        ImageView imageView = dialog.findViewById(R.id.image_view_dialog);
        Button buttonClose = dialog.findViewById(R.id.button_close_dialog);


<<<<<<< HEAD
        textView.setText("Image" + position);
        Glide.with(this)
=======
        textView.setText("Image"+position);
        Glide.with( this)
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
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
<<<<<<< HEAD

        private TextChange(View v) {
=======
        private TextChange (View v) {
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
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
<<<<<<< HEAD
                    if (s.length() == 0) {
                        reg_experience.setHelperText("Please enter the number of years of experience");
                        reg_experience.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    } else {
=======
                    if(s.length() == 0){
                        reg_experience.setHelperText("Please enter the number of years of experience");
                        reg_experience.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    }
                    else{
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                        reg_experience.setHelperText("");
                    }
                    break;
                case R.id.reg_kennelNameEdit:
<<<<<<< HEAD
                    if (s.length() == 0) {
                        reg_kennelName.setHelperText("Please enter your registered kennel name");
                        reg_kennelName.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    } else {
=======
                    if(s.length() == 0){
                        reg_kennelName.setHelperText("Please enter your registered kennel name");
                        reg_kennelName.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    }
                    else{
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                        reg_kennelName.setHelperText("");
                    }
                    break;
                case R.id.reg_prc_id_edit:
<<<<<<< HEAD
                    if (s.length() == 0) {
                        reg_prc_id.setHelperText("Please enter your PRC id no.");
                        reg_prc_id.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    } else if (s.length() < 7) {
                        reg_prc_id.setHelperText("PRC id no. must have 7 digits");
                        reg_prc_id.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    } else {
=======
                    if(s.length() == 0){
                        reg_prc_id.setHelperText("Please enter your PRC id no.");
                        reg_prc_id.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    }
                    else if(s.length() < 7){
                        reg_prc_id.setHelperText("PRC id no. must have 7 digits");
                        reg_prc_id.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    }
                    else{
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                        reg_prc_id.setHelperText("");
                    }
                    break;
                case R.id.reg_memberID_edit:
<<<<<<< HEAD
                    if (s.length() == 0) {
                        reg_memberID.setHelperText("Please enter your Member id");
                        reg_memberID.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    } else {
=======
                    if(s.length() == 0){
                        reg_memberID.setHelperText("Please enter your Member id");
                        reg_memberID.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    }
                    else if(s.length() < 5){
                        reg_memberID.setHelperText("Member id must have 5 digits");
                        reg_memberID.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    }
                    else{
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02
                        reg_memberID.setHelperText("");
                    }
                    break;
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
<<<<<<< HEAD
    }

    ;
=======
    };
>>>>>>> 2ec62453e0f82df8f9e52e1f4bc29e4eae8d3c02

}
