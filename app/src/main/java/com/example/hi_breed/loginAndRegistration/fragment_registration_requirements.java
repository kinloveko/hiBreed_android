package com.example.hi_breed.loginAndRegistration;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
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
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.hi_breed.R;
import com.example.hi_breed.adapter.breeder_kennel_cert_RecyclerAdapter;
import com.example.hi_breed.adapter.shooterAdapter.shooter_proof_RecyclerAdapter;
import com.example.hi_breed.classesFile.ImageClassifierHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.mlkit.common.model.LocalModel;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yalantis.ucrop.UCrop;


import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.task.vision.classifier.Classifications;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import pl.droidsonroids.gif.GifImageView;


public class fragment_registration_requirements extends Fragment implements ImageClassifierHelper.ClassifierListener, breeder_kennel_cert_RecyclerAdapter.CountOfImagesWhenRemoved, breeder_kennel_cert_RecyclerAdapter.itemClickListener, shooter_proof_RecyclerAdapter.itemClickListeners, shooter_proof_RecyclerAdapter.CountOfImagesWhenRemoves {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.registration__requirements, container, false);
    }

    private static final int REQUEST_PICK_IMAGE = 102;
    private static final int REQUEST_PICK_USER_VALID_ID = 101;
    private static final int REQUEST_PICK_PARENT_VALID_ID = 99;
    private static final int REQUEST_PICK_CONSENT = 98;
    private static final int PICK_IMAGE = 1;
    private static final int PICK_IMAGE_Shooter = 2;
    private int i = -1;
    private int countOfImages;
    private int getCountOfImagesShoot;
    Button clickHere;
    private FirebaseAuth mAuth;
    FirebaseFirestore firestore;

    RecyclerView recyclerView, recyclerView_gallery_images_shooter;
    breeder_kennel_cert_RecyclerAdapter adapter;
    shooter_proof_RecyclerAdapter shooter_adapter;

    ArrayList<Uri> uri = new ArrayList<>();
    Uri imageUri;
    ArrayList<Uri> uriShooter = new ArrayList<>();
    Uri imageUriShooter;
    Uri imgVet;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<Intent> activityResultLauncherShooter;

    CardView dropImageCardView, dropImageCardViewShooter, dropImageCardViewVet;
    ImageView dropImageView, dropImageViewVet, prc_image, imageview_vet_style;
    Button submit;
    RelativeLayout reg_birth, reg_gender, reg_transaction, reg_date_registered;
    TextInputLayout reg_memberID, reg_prc_id, reg_first, reg_last, reg_middle, reg_address, reg_zip, reg_kennelName;
    TextInputEditText reg_memberID_edit, reg_prc_id_edit, reg_firstEdit, reg_lastEdit, reg_middleEdit, reg_addressEdit, reg_zipEdit, reg_experienceEdit, reg_kennelNameEdit;
    TextView reg_date_registered_edit, prc_required_error, add_photo_vet, dropImageTextVIew, reg_birthEdit, reg_genderEdit, reg_transactionEdit, textViewExp;
    LinearLayout backLayoutService;
    LinearLayout memberLayouts, shooter_validation_linearLayout, breeder_validation_linearLayout, vet_validation_linear_layout;
    TextInputLayout reg_experience;
    ArrayList<String> role;
    ArrayList<String> saveRole;
    RelativeLayout validationLayout;
    LinearLayout userValidation;
    CardView userDropImage;
    ImageView imageview_user_style,
            user_dropImageViewVet,
            user_image_upload;
    TextView add_photo_user;
    LinearLayout parent_consent_layout;
    CardView guardian;
    ImageView guardian_img,
            guardian_image_view,
            guardian_valid_id;
    TextView guardian_add_photo;
    CardView guardian_consent;
    ImageView guardian_img_consent,
            guardian_image_view_consent,
            guardian_consent_submitted;
    TextView guardian_add_photo_consent;
    Button downloadButton;
    Uri user_valid_URI, parent_valid_URI, consent_URI;


    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        //for recycler view
        recyclerView_gallery_images_shooter = view.findViewById(R.id.recyclerView_gallery_images_shooter);
        recyclerView = view.findViewById(R.id.recyclerView_gallery_images);
        //for adapter
        shooter_adapter = new shooter_proof_RecyclerAdapter(uriShooter, getContext(), this, this);
        adapter = new breeder_kennel_cert_RecyclerAdapter(uri, getContext(), this, this);
        //RecyclerView
        recyclerView_gallery_images_shooter.setLayoutManager(new GridLayoutManager(getContext(), 4));
        recyclerView_gallery_images_shooter.setAdapter(shooter_adapter);
        memberLayouts = view.findViewById(R.id.memberLayouts);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        recyclerView.setAdapter(adapter);

        backLayoutService = view.findViewById(R.id.backLayoutService);
        backLayoutService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Simulate back button press
                getActivity().onBackPressed();

            }
        });
        validationLayout = view.findViewById(R.id.validationLayout);
        clickHere = view.findViewById(R.id.clickHere);
        clickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.pcci.org.ph/how-tos/apply-for-kennel-name/";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        reg_memberID_edit = view.findViewById(R.id.reg_memberID_edit);
        reg_memberID = view.findViewById(R.id.reg_memberID);
        reg_prc_id_edit = view.findViewById(R.id.reg_prc_id_edit);
        reg_prc_id = view.findViewById(R.id.reg_prc_id);
        reg_date_registered = view.findViewById(R.id.reg_date_registered);
        reg_date_registered_edit = view.findViewById(R.id.reg_date_registered_edit);

        // variable initialized

        //Parent Consent
        userValidation = view.findViewById(R.id.userValidation);
        parent_consent_layout = view.findViewById(R.id.parent_consent_layout);

        //cardView onclick
        userDropImage = view.findViewById(R.id.userDropImage);
        guardian = view.findViewById(R.id.guardian);
        guardian_consent = view.findViewById(R.id.guardian_consent);


        //ImageView upload image that needs to be visible when upload something
        user_image_upload = view.findViewById(R.id.user_image_upload);
        guardian_valid_id = view.findViewById(R.id.guardian_valid_id);
        guardian_consent_submitted = view.findViewById(R.id.guardian_consent_submitted);

        //Image and textView the needs to set the visibility to GONE
        //UserValidation
        imageview_user_style = view.findViewById(R.id.imageview_user_style);
        user_dropImageViewVet = view.findViewById(R.id.user_dropImageViewVet);
        add_photo_user = view.findViewById(R.id.add_photo_user);
        //Parent Validation
        guardian_img = view.findViewById(R.id.guardian_img);
        guardian_image_view = view.findViewById(R.id.guardian_image_view);
        guardian_add_photo = view.findViewById(R.id.guardian_add_photo);
        //Consent Validation
        guardian_img_consent = view.findViewById(R.id.guardian_img_consent);
        guardian_image_view_consent = view.findViewById(R.id.guardian_image_view_consent);
        guardian_add_photo_consent = view.findViewById(R.id.guardian_add_photo_consent);

        prc_required_error = view.findViewById(R.id.prc_required_error);
        add_photo_vet = view.findViewById(R.id.add_photo_vet);
        textViewExp = view.findViewById(R.id.textViewExp);
        dropImageTextVIew = view.findViewById(R.id.dropImageTextView);
        reg_birthEdit = view.findViewById(R.id.reg_birthEdit);
        reg_genderEdit = view.findViewById(R.id.reg_genderEdit);
        reg_transactionEdit = view.findViewById(R.id.reg_transactionEdit);
        //cardView
        dropImageCardView = view.findViewById(R.id.dropImageCardView);
        dropImageCardViewShooter = view.findViewById(R.id.dropImageCardViewShooter);
        dropImageCardViewVet = view.findViewById(R.id.dropImageCardViewVet);

        //ImageView
        dropImageView = view.findViewById(R.id.dropImageView);
        dropImageViewVet = view.findViewById(R.id.dropImageViewVet);
        prc_image = view.findViewById(R.id.prc_image);
        imageview_vet_style = view.findViewById(R.id.imageview_vet_style);
        //Relative Layout
        reg_birth = view.findViewById(R.id.reg_birth);
        reg_gender = view.findViewById(R.id.reg_gender);
        reg_transaction = view.findViewById(R.id.reg_transaction);
        downloadButton = view.findViewById(R.id.download_button);


        //PDF download
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse("https://drive.google.com/file/d/16WoD9WZHtGLW1dbQVj25_G9Lp4MeEpbL/view?fbclid=IwAR3qmKUFV4YHRpjd19e61EfFxa4l9YiMFRegYi3qoZi1_AxkXl6nLnTLgPY"));
                request.setTitle("Parent Consent");
                request.setDescription("Downloading parent consent form");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "parent_consent.pdf");
                Toast.makeText(getContext(), "Downloading parent consent form . . .", Toast.LENGTH_SHORT).show();
                DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                downloadManager.enqueue(request);
            }
        });

        ImageView valid_id_example_consent = view.findViewById(R.id.valid_id_example_consent);
        valid_id_example_consent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);

                dialog.setContentView(R.layout.custom_dialog_zoom);

                ImageView imageView = dialog.findViewById(R.id.image_view_dialog);
                Button buttonClose = dialog.findViewById(R.id.button_close_dialog);

                Picasso.get()
                        .load(R.drawable.example_consent)
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

        //button
        submit = view.findViewById(R.id.submitButton);

        //InputText
        reg_first = view.findViewById(R.id.firstName_Input);
        reg_middle = view.findViewById(R.id.reg_middleName);
        reg_last = view.findViewById(R.id.reg_lastName);

        reg_address = view.findViewById(R.id.reg_address);
        reg_zip = view.findViewById(R.id.reg_zip);
        reg_experience = view.findViewById(R.id.reg_experience);
        reg_kennelName = view.findViewById(R.id.reg_kennelName);
        //EditText

        //TextInput
        reg_firstEdit = view.findViewById(R.id.firstName_Edit);
        reg_middleEdit = view.findViewById(R.id.reg_middleNameEdit);
        reg_lastEdit = view.findViewById(R.id.reg_lastNameEdit);

        reg_addressEdit = view.findViewById(R.id.reg_addressEdit);
        reg_experienceEdit = view.findViewById(R.id.reg_experienceEdit);
        reg_zipEdit = view.findViewById(R.id.reg_zipEdit);
        reg_kennelNameEdit = view.findViewById(R.id.reg_kennelNameEdit);

        //watcher
        reg_firstEdit.addTextChangedListener(new TextChange(reg_firstEdit));

        reg_lastEdit.addTextChangedListener(new TextChange(reg_lastEdit));

        reg_addressEdit.addTextChangedListener(new TextChange(reg_addressEdit));

        reg_zipEdit.addTextChangedListener(new TextChange(reg_zipEdit));

        reg_experienceEdit.addTextChangedListener(new TextChange(reg_experienceEdit));

        reg_kennelNameEdit.addTextChangedListener(new TextChange(reg_kennelNameEdit));

        reg_prc_id_edit.addTextChangedListener(new TextChange(reg_prc_id_edit));

        reg_memberID_edit.addTextChangedListener(new TextChange(reg_memberID_edit));

        //Getting the input from 1st fragment Details
        role = new ArrayList<>();
        saveRole = new ArrayList<>();

        Bundle bundle = this.getArguments();
        role = Objects.requireNonNull(bundle).getStringArrayList("key");

        //LinearLayout
        shooter_validation_linearLayout = view.findViewById(R.id.shooter_validation_linearLayout);
        breeder_validation_linearLayout = view.findViewById(R.id.breeder_validation_linearLayout);
        vet_validation_linear_layout = view.findViewById(R.id.vet_validation_linear_layout);

        if (role.size() == 1 && role.contains("Pet Owner")) {
            // Only show this
            userValidation.setVisibility(View.VISIBLE);
            validationLayout.setVisibility(View.GONE);
            saveRole.add("Pet Owner");
        } else {
            userValidation.setVisibility(View.GONE);

            if (role.contains("Pet Owner")) {
                saveRole.add("Pet Owner");
            }

            if (role.contains("Pet Breeder") || role.contains("Pet Shooter") || role.contains("Veterinarian")) {
                memberLayouts.setVisibility(View.VISIBLE);

                TextView dropImageTextView = view.findViewById(R.id.dropImageTextView);
                TextView breederPCCI = view.findViewById(R.id.breederPCCI);
                ImageView breederPCCIImage = view.findViewById(R.id.breederPCCIImage);
                ImageView imagePCCI_CERT = view.findViewById(R.id.imagePCCI_CERT);
                ImageView shooter_pcci_id = view.findViewById(R.id.shooter_pcci_id);
                ImageView shooter_image_example = view.findViewById(R.id.shooter_image_example);


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
                } else if (role.contains("Pet Shooter")) {
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
                validationLayout.setVisibility(View.GONE);
                memberLayouts.setVisibility(View.GONE);
            }
        }

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
                                for (int i = 0; i < countOfImages; i++) {
                                    //limiting the no of images picked up from gallery
                                    if (uri.size() < 5) {
                                        imageUri = result.getData().getClipData().getItemAt(i).getUri();
                                        uri.add(imageUri);
                                    } else {
                                        Toast.makeText(getContext(), "Not Allowed to pick more than 5 images", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                //then notify the adapter
                                adapter.notifyDataSetChanged();
                                Toast.makeText(getContext(), uri.size() + ":selected", Toast.LENGTH_SHORT).show();

                            } else {
                                if (uri.size() < 5) {
                                    //this is for to get the single images
                                    imageUri = result.getData().getData();
                                    //add the code into
                                    uri.add(imageUri);
                                } else {
                                    Toast.makeText(getContext(), "Not Allowed to pick more than 5 images", Toast.LENGTH_SHORT).show();
                                }
                            }
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getContext(), uri.size() + ": selected", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "You Haven't Pick any Image", Toast.LENGTH_SHORT).show();
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
                                for (int i = 0; i < getCountOfImagesShoot; i++) {
                                    //limiting the no of images picked up from gallery
                                    if (uriShooter.size() < 5) {
                                        imageUriShooter = result.getData().getClipData().getItemAt(i).getUri();
                                        uriShooter.add(imageUriShooter);
                                    } else {
                                        Toast.makeText(getContext(), "Not Allowed to pick more than 5 images", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                //then notify the adapter
                                shooter_adapter.notifyDataSetChanged();
                                Toast.makeText(getContext(), uriShooter.size() + ":selected", Toast.LENGTH_SHORT).show();

                            } else {
                                if (uriShooter.size() < 5) {
                                    //this is for to get the single images
                                    imageUriShooter = result.getData().getData();
                                    //add the code into
                                    uriShooter.add(imageUriShooter);
                                } else {
                                    Toast.makeText(getContext(), "Not Allowed to pick more than 5 images", Toast.LENGTH_SHORT).show();
                                }
                            }
                            shooter_adapter.notifyDataSetChanged();
                            Toast.makeText(getContext(), uriShooter.size() + ": selected", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "You Haven't Pick any Image", Toast.LENGTH_SHORT).show();
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
                if (checkPermission()) {
                    imagePermissionVet();
                } else {
                    requestPermission();
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Permission is not granted, request it
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                30);
                    }
                }
            }
        });

        reg_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGenderDialog();
            }
        });
        reg_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBirthdayDialog();
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

        userDropImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    imagePermissionUserValid();
                } else {
                    requestPermission();
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Permission is not granted, request it
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                30);
                    }
                }
            }
        });
        guardian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    imagePermissionParentValid();
                } else {
                    requestPermission();
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Permission is not granted, request it
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                30);
                    }
                }
            }
        });
        guardian_consent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    imagePermissionConsent();
                } else {
                    requestPermission();
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Permission is not granted, request it
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                30);
                    }
                }
            }
        });

        //button
        submit = view.findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInput();
            }
        });
    }

    private void imageViewer(int pcci_certificate) {
        Dialog dialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);

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

    int userAge = 0;

    @SuppressLint("SetTextI18n")
    private void openBirthdayDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = View.inflate(getContext(), R.layout.pet_add_birthday_dialog, null);
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
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();
                Calendar birthdate = Calendar.getInstance();
                birthdate.set(year, month, day);

                Calendar today = Calendar.getInstance();

                int age = today.get(Calendar.YEAR) - birthdate.get(Calendar.YEAR);
                if (today.get(Calendar.MONTH) < birthdate.get(Calendar.MONTH)) {
                    age--;
                } else if (today.get(Calendar.MONTH) == birthdate.get(Calendar.MONTH)
                        && today.get(Calendar.DAY_OF_MONTH) < birthdate.get(Calendar.DAY_OF_MONTH)) {
                    age--;
                }
                //Only owner will be check if the user is minor or not
                if (role.size() == 1 && role.contains("Pet Owner")) {
                    if (age < 18 && age >= 12) {
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                        String formatedDate = sdf.format(birthdate.getTime());
                        reg_birthEdit.setText(formatedDate);
                        reg_birthEdit.setTextColor(ColorStateList.valueOf(Color.BLACK));
                        Toast.makeText(getContext(), "Detected as minor", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(), "Age:" + String.valueOf(age), Toast.LENGTH_SHORT).show();
                        parent_consent_layout.setVisibility(View.VISIBLE);
                        userAge = age;


                        dialog.dismiss();
                    } else if (age < 12) {

                        Toast.makeText(getContext(), "Age restriction: 12 and above only", Toast.LENGTH_SHORT).show();

                    } else {
                        parent_consent_layout.setVisibility(View.GONE);
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                        String formatedDate = sdf.format(birthdate.getTime());
                        reg_birthEdit.setText(formatedDate);
                        reg_birthEdit.setTextColor(ColorStateList.valueOf(Color.BLACK));
                        Toast.makeText(getContext(), formatedDate, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(), "Age:" + String.valueOf(age), Toast.LENGTH_SHORT).show();

                        userAge = age;
                        dialog.dismiss();
                    }
                }
                //If vet/shooter/breeder it must be 18 above
                else {
                    if (age < 18) {
                        Toast.makeText(getContext(), "Age restriction: 18 and above only", Toast.LENGTH_SHORT).show();
                    } else {
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                        String formatedDate = sdf.format(birthdate.getTime());
                        reg_birthEdit.setText(formatedDate);
                        reg_birthEdit.setTextColor(ColorStateList.valueOf(Color.BLACK));
                        Toast.makeText(getContext(), formatedDate, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(), "Age:" + String.valueOf(age), Toast.LENGTH_SHORT).show();

                        userAge = age;
                        dialog.dismiss();
                    }
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void openDateRegistered() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = View.inflate(getContext(), R.layout.pet_add_birthday_dialog, null);
        DatePicker datePicker = view.findViewById(R.id.datePicker);
        datePicker.setMaxDate(System.currentTimeMillis());
        builder.setView(view);
        MaterialButton cancel, save;
        TextView message, title;
        title = view.findViewById(R.id.pet_add_birthday_set_title);
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
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);

                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                String formatedDate = sdf.format(calendar.getTime());
                reg_date_registered_edit.setText(formatedDate);
                reg_date_registered_edit.setTextColor(ColorStateList.valueOf(Color.BLACK));
                Toast.makeText(getContext(), formatedDate, Toast.LENGTH_SHORT).show();
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
        if (requestCode == 30) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, open the gallery
                imagePermissionVet();
            } else {
                // Permission is not granted, show an error message
                Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private void imagePermission() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int readCheck = ContextCompat.checkSelfPermission(getActivity(), READ_EXTERNAL_STORAGE);
            int writeCheck = ContextCompat.checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE);
            return readCheck == PackageManager.PERMISSION_GRANTED && writeCheck == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void imagePermissionVet() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    private void imagePermissionUserValid() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_USER_VALID_ID);
    }

    private void imagePermissionParentValid() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_PARENT_VALID_ID);
    }

    private void imagePermissionConsent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_CONSENT);
    }


    private final String[] permissions = {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Permission")
                    .setMessage("Please give the Storage permission")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                intent.addCategory("android.intent.category.DEFAULT");
                                intent.setData(Uri.parse(String.format("package:%s", new Object[]{getActivity().getPackageName()})));
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

            ActivityCompat.requestPermissions(fragment_registration_requirements.this.getActivity(), permissions, 30);

        }
    }

    private void requestPermissionShot() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Permission")
                    .setMessage("Please give the Storage permission")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                intent.addCategory("android.intent.category.DEFAULT");
                                intent.setData(Uri.parse(String.format("package:%s", new Object[]{getActivity().getPackageName()})));
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

            ActivityCompat.requestPermissions(getActivity(), permissions, 10);

        }

    }

    @SuppressLint("ObsoleteSdkInt")
    private void imagePermissionShooter() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_Shooter);

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {

            //this part is for to get multiple images
            if (data.getClipData() != null) {

                countOfImages = data.getClipData().getItemCount();
                for (int j = 0; j < countOfImages; j++) {

                    if (uri.size() < 5) {
                        imageUri = data.getClipData().getItemAt(j).getUri();
                        uri.add(imageUri);
                    } else {
                        Toast.makeText(getContext(), "Not allowed to pick more than 5 images", Toast.LENGTH_SHORT).show();
                    }

                }
                //then notify the adapter
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(), uri.size() + ": selected", Toast.LENGTH_SHORT).show();
            } else {
                //this is for to get the single images
                if (uri.size() < 5) {
                    imageUri = data.getData();
                    uri.add(imageUri);
                } else {
                    Toast.makeText(getContext(), "Not allowed to pick more than 5 images", Toast.LENGTH_SHORT).show();
                }
            }
            adapter.notifyDataSetChanged();
            Toast.makeText(getContext(), uri.size() + ": selected", Toast.LENGTH_SHORT).show();
        } else if (requestCode == PICK_IMAGE_Shooter && resultCode == RESULT_OK && null != data) {
            //this part is for to get multiple images
            if (data.getClipData() != null) {
                getCountOfImagesShoot = data.getClipData().getItemCount();
                for (int j = 0; j < getCountOfImagesShoot; j++) {
                    if (uri.size() < 5) {
                        imageUriShooter = data.getClipData().getItemAt(j).getUri();
                        uriShooter.add(imageUriShooter);
                    } else {
                        Toast.makeText(getContext(), "Not allowed to pick more than 5 images", Toast.LENGTH_SHORT).show();
                    }
                }
                //then notify the adapter
                shooter_adapter.notifyDataSetChanged();
                Toast.makeText(getContext(), uriShooter.size() + ": selected", Toast.LENGTH_SHORT).show();
            } else {
                //this is for to get the single images
                if (uriShooter.size() < 5) {
                    imageUriShooter = data.getData();
                    uriShooter.add(imageUriShooter);
                } else {
                    Toast.makeText(getContext(), "Not allowed to pick more than 5 images", Toast.LENGTH_SHORT).show();
                }
            }
            shooter_adapter.notifyDataSetChanged();
            Toast.makeText(getContext(), uriShooter.size() + ": selected", Toast.LENGTH_SHORT).show();
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_PICK_IMAGE) {
            Uri imageUri = data.getData();
            cropImage(imageUri, prc_image, "vet");
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_PICK_USER_VALID_ID) {
            Uri imageUri = data.getData();
            cropImage(imageUri, user_image_upload, "user");

        } else if (resultCode == RESULT_OK && requestCode == REQUEST_PICK_PARENT_VALID_ID) {
            Uri imageUri = data.getData();
            cropImage(imageUri, guardian_valid_id, "parent");
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_PICK_CONSENT) {
            Uri imageUri = data.getData();
            cropImage(imageUri, guardian_consent_submitted, "consent");
        } else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP && types.equals("vet")) {
            Uri croppedUri = UCrop.getOutput(data);
            // Use the cropped image URI as needed
            if (croppedUri != null) {
                if (currentImageView != null && types != null) {
                    prc_image.setVisibility(View.VISIBLE);
                    imgVet = croppedUri;
                    Glide.with(getActivity())
                            .load(croppedUri)
                            .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
                            .placeholder(R.drawable.noimage)
                            .into(prc_image);
                    add_photo_vet.setVisibility(View.GONE);
                    dropImageViewVet.setVisibility(View.GONE);
                }
            }
        } else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP && types.equals("user")) {
            Uri croppedUri = UCrop.getOutput(data);
            // Use the cropped image URI as needed
            if (croppedUri != null) {
                if (currentImageView != null && types != null) {

                    user_valid_URI = croppedUri;

                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), croppedUri);
                        classification(bitmap, types);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return;
                }
            }
        } else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP && types.equals("parent")) {
            Uri croppedUri = UCrop.getOutput(data);
            // Use the cropped image URI as needed
            if (croppedUri != null) {
                if (currentImageView != null && types != null) {
                    guardian_valid_id.setVisibility(View.VISIBLE);
                    parent_valid_URI = croppedUri;
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), croppedUri);
                        classification(bitmap, types);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return;
                }
            }
        } else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP && types.equals("consent")) {
            Uri croppedUri = UCrop.getOutput(data);
            // Use the cropped image URI as needed
            if (croppedUri != null) {
                if (currentImageView != null && types != null) {
                    guardian_consent_submitted.setVisibility(View.VISIBLE);
                    consent_URI = croppedUri;
                    Glide.with(getActivity())
                            .load(croppedUri)
                            .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
                            .placeholder(R.drawable.noimage)
                            .into(guardian_consent_submitted);
                    guardian_img_consent.setVisibility(View.GONE);
                    guardian_image_view_consent.setVisibility(View.GONE);
                    guardian_add_photo_consent.setVisibility(View.GONE);
                }
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            Throwable error = UCrop.getError(data);
            // Handle UCrop error as needed
        } else {
            //this code is for if user not picked any image
            Toast.makeText(getContext(), "You Haven't Picked any image", Toast.LENGTH_SHORT).show();
        }
    }


    private ImageClassifierHelper imageClassifierHelper;

    private void classification(Bitmap image, String types) {

        Log.d("Classify", "send:" + image.toString());
        if (types.equals("user")) {
            user_image_upload.setImageBitmap(image);
        } else if (types.equals("parent")) {
            guardian_valid_id.setImageBitmap(image);
        }

        imageClassifierHelper = ImageClassifierHelper.create(requireContext(), this);

        synchronized (this) {
            imageClassifierHelper.classify(image, 0);
        }
    }

    @Override
    public void onError(String error) {
        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    public void onResults(List<Classifications> results, long inferenceTime) {
        List<Category> categories = results.get(0).getCategories();
        Log.d("Classify", "result1");
        if (!categories.isEmpty()) {
            Log.d("Classify", "result2");
            Category category = categories.get(0);
            float score = category.getScore();
            if (category.getLabel().equals("not_valid")) {
                if(types.equals("user")){
                    user_dropImageViewVet.setVisibility(View.VISIBLE);
                    add_photo_user.setVisibility(View.VISIBLE);
                    user_image_upload.setVisibility(View.GONE);
                    user_valid_URI = null;
                }
                else{
                    guardian_valid_id.setVisibility(View.GONE);
                     parent_valid_URI = null;
                    guardian_img.setVisibility(View.VISIBLE);
                    guardian_add_photo.setVisibility(View.VISIBLE);
                }


                if (isAdded() && getContext() != null) {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(requireContext());
                    builder2.setCancelable(false);
                    View view = View.inflate(getContext(), R.layout.screen_custom_alert, null);
                    //title
                    TextView title = view.findViewById(R.id.screen_custom_alert_title);
                    //loading text
                    TextView loadingText = view.findViewById(R.id.screen_custom_alert_loadingText);
                    loadingText.setVisibility(View.GONE);
                    //gif
                    GifImageView gif = view.findViewById(R.id.screen_custom_alert_gif);
                    gif.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.scan));

                    //header image
                    AppCompatImageView imageViewCompat = view.findViewById(R.id.appCompatImageView);
                    imageViewCompat.setVisibility(View.VISIBLE);
                    imageViewCompat.setImageDrawable(getContext().getDrawable(R.drawable.screen_alert_image_error_border));
                    //message
                    TextView message = view.findViewById(R.id.screen_custom_alert_message);
                    title.setText("Invalid ID");
                    message.setText("The submitted photo is not clear or does not appear to be a valid ID. Please try again with a clear and valid ID.");
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
                            alert2.dismiss();

                        }
                    });
                }
            } else {
                if(types.equals("user")){
                    user_dropImageViewVet.setVisibility(View.GONE);
                    add_photo_user.setVisibility(View.GONE);
                    user_image_upload.setVisibility(View.VISIBLE);
                }
                else{
                    guardian_valid_id.setVisibility(View.VISIBLE);
                    guardian_img.setVisibility(View.GONE);
                    guardian_add_photo.setVisibility(View.GONE);
                }

                Toast.makeText(getContext(), "Submitted ID passed ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        synchronized (this) {
            if (imageClassifierHelper != null)
                imageClassifierHelper.clearImageClassifier();
        }
    }

    private ImageView currentImageView;
    private String types;

    private void cropImage(Uri sourceUri, ImageView imageView, String type) {
        currentImageView = imageView;
        types = type;

        Uri destinationUri = Uri.fromFile(new File(getContext().getCacheDir(), "cropped"));
        UCrop uCrop = UCrop.of(sourceUri, destinationUri);
        uCrop.start(getContext(), this);

    }

    private void checkInput() {

        String first = Objects.requireNonNull(reg_firstEdit.getText()).toString().trim();
        String middle = Objects.requireNonNull(reg_middleEdit.getText()).toString().trim();
        String last = Objects.requireNonNull(reg_lastEdit.getText()).toString().trim();
        String address = Objects.requireNonNull(reg_addressEdit.getText()).toString().trim();
        String zip = Objects.requireNonNull(reg_zipEdit.getText()).toString().trim();
        String birth = reg_birthEdit.getText().toString().trim();
        String gender = reg_genderEdit.getText().toString().trim();
        String exp = reg_experienceEdit.getText().toString().trim();
        String kennel = reg_kennelNameEdit.getText().toString().trim();
        String memberID = reg_memberID_edit.getText().toString().trim();
        String prcID = reg_prc_id_edit.getText().toString().trim();
        String date = reg_date_registered_edit.getText().toString().trim();


        //first condition
        if (first.isEmpty()) {
            reg_first.setHelperText("*Please enter your first name");
            reg_first.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
            reg_firstEdit.requestFocus();
            return;
        } else if (first.length() < 2) {
            reg_first.setHelperText("Name must have 2 characters");
            reg_first.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
            reg_firstEdit.requestFocus();
            return;
        } //end of firstname
        //last condition
        if (last.isEmpty()) {
            reg_last.setHelperText("Please enter your last name");
            reg_last.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
            reg_lastEdit.requestFocus();
            return;
        } else if (last.length() < 2) {
            reg_last.setHelperText("Last name must have 2 letters");
            reg_last.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
            reg_lastEdit.requestFocus();
            return;
        } //end of lastname


        if (gender.equals("Gender . . .")) {
            reg_genderEdit.setFocusable(true);
            reg_genderEdit.setFocusableInTouchMode(true);
            reg_genderEdit.requestFocus();
            Toast.makeText(getContext(), "Gender is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (birth.equals("Birthday . . .")) {
            reg_birthEdit.setFocusable(true);
            reg_birthEdit.setFocusableInTouchMode(true);
            reg_birthEdit.requestFocus();
            Toast.makeText(getContext(), "Birthday is empty", Toast.LENGTH_SHORT).show();
            return;
        }

//address
        if (address.isEmpty()) {
            reg_address.setHelperText("Please input your correct address");
            reg_address.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
            reg_addressEdit.requestFocus();

            return;
        }

        if (zip.isEmpty()) {
            reg_zip.setHelperText("Please input your zip code");
            reg_zip.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
            reg_zipEdit.requestFocus();
            return;
        } else if (zip.length() != 4) {
            reg_zip.setHelperText("Zipcode must 4 numbers");
            reg_zip.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
            reg_zipEdit.requestFocus();

            return;
        } else {
            reg_zip.setError("");

        }//end of zip

        Bundle bundle = new Bundle();

        // to check if what is the role of the user

        if (role.size() == 1 && role.contains("Pet Owner")) {
            if (user_valid_URI == null) {
                Toast.makeText(this.getContext(), "Please Upload your valid id", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Bitmap bitmapUser = ((BitmapDrawable) user_image_upload.getDrawable()).getBitmap();
                ByteArrayOutputStream bytesUser = new ByteArrayOutputStream();
                bitmapUser.compress(Bitmap.CompressFormat.JPEG, 100, bytesUser);
                File fileUser = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "user.jpg");
                try (FileOutputStream fos = new FileOutputStream(fileUser)) {
                    fos.write(bytesUser.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Uri imageUriUSer = Uri.fromFile(fileUser);
                bundle.putString("user_valid_id", imageUriUSer.toString());
            }


            if (userAge < 18 && userAge >= 12) {
                if (parent_valid_URI == null) {
                    Toast.makeText(this.getContext(), "Parent or Guardian valid id is needed", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Bitmap bitmapParent = ((BitmapDrawable) guardian_valid_id.getDrawable()).getBitmap();
                    ByteArrayOutputStream bytesParent = new ByteArrayOutputStream();
                    bitmapParent.compress(Bitmap.CompressFormat.JPEG, 100, bytesParent);
                    File fileParent = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "parent.jpg");
                    try (FileOutputStream fos = new FileOutputStream(fileParent)) {
                        fos.write(bytesParent.toByteArray());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Uri imageUriParent = Uri.fromFile(fileParent);
                    bundle.putString("parent_valid_id", imageUriParent.toString());
                }

                if (consent_URI == null) {
                    Toast.makeText(this.getContext(), "Please upload your parent consent form as an image", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Bitmap bitmapConsent = ((BitmapDrawable) guardian_consent_submitted.getDrawable()).getBitmap();
                    ByteArrayOutputStream bytesParentConsent = new ByteArrayOutputStream();
                    bitmapConsent.compress(Bitmap.CompressFormat.JPEG, 100, bytesParentConsent);
                    File fileConsent = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "consent.jpg");
                    try (FileOutputStream fos = new FileOutputStream(fileConsent)) {
                        fos.write(bytesParentConsent.toByteArray());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Uri imageUriConsent = Uri.fromFile(fileConsent);
                    bundle.putString("consent_image", imageUriConsent.toString());
                }

            }
        } else if (role.contains("Pet Breeder") || role.contains("Pet Shooter") || role.contains("Veterinarian")) {

            if (role.contains("Pet Breeder")) {

                if (kennel.isEmpty()) {
                    reg_kennelName.setHelperText("Please input your registered kennel name");
                    reg_kennelName.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    reg_kennelNameEdit.requestFocus();
                    return;
                }
                if (uri.size() == 0) {
                    Toast.makeText(getContext(), "You must upload certificate for your kennel name", Toast.LENGTH_SHORT).show();
                    dropImageTextVIew.setFocusableInTouchMode(true);
                    dropImageTextVIew.setFocusable(true);
                    return;
                }
                if ((role.contains("Pet Breeder") && role.contains("Pet Owner")) && role.size() == 2) {
                    if (uri.size() < 2) {
                        Toast.makeText(getContext(), "You must upload certificate for your kennel name and PCCI (ID)", Toast.LENGTH_SHORT).show();
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
                    reg_transactionEdit.setFocusable(true);
                    reg_transactionEdit.setFocusableInTouchMode(true);
                    Toast.makeText(getContext(), "Date for your last transaction is empty", Toast.LENGTH_SHORT).show();
                    reg_transactionEdit.requestFocus();
                    return;
                }

                bundle.putString("lastTransaction", reg_transactionEdit.getText().toString());

                if (uriShooter.size() == 0) {
                    Toast.makeText(getContext(), "Proof photos of successful stud/made are needed", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (uriShooter.size() == 1) {
                    Toast.makeText(getContext(), "Proof photos of successful stud/made and PCCI ID are needed", Toast.LENGTH_SHORT).show();
                    return;
                }
                bundle.putParcelableArrayList("uriShooter", uriShooter);

            }
            if (role.contains("Veterinarian")) {
                if (imgVet == null) {
                    prc_required_error.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Please provide a picture of your License ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                prc_required_error.setVisibility(View.GONE);
                bundle.putString("vet_image", imgVet.toString());

                if (prcID.isEmpty()) {
                    reg_prc_id_edit.setFocusable(true);
                    reg_prc_id_edit.setFocusableInTouchMode(true);
                    reg_prc_id.setHelperText("PRC ID No. is required");
                    reg_prc_id_edit.requestFocus();
                    return;
                } else if (prcID.length() < 7) {
                    reg_prc_id.setHelperText("PRC ID No. must have 7 digits");
                    reg_prc_id.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    reg_prc_id_edit.requestFocus();
                    return;
                }
                bundle.putString("prcNo", prcID);
            }

            if (exp.isEmpty()) {
                reg_experience.setHelperText("Please input number of experience");
                reg_experience.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                reg_experienceEdit.requestFocus();
                return;
            }
            bundle.putString("exp", exp);
            if (memberID.isEmpty()) {
                reg_memberID.setHelperText("Please input member ID");
                reg_memberID.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                reg_memberID_edit.requestFocus();
                return;
            } else if (memberID.length() < 5) {
                reg_memberID.setHelperText("Member ID should have 5 digits");
                reg_memberID.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                reg_memberID_edit.requestFocus();
                return;
            }
            bundle.putString("memberID", memberID);
            if (date.isEmpty()) {
                reg_date_registered_edit.setFocusable(true);
                reg_date_registered_edit.setFocusableInTouchMode(true);
                reg_date_registered_edit.requestFocus();
                Toast.makeText(getContext(), "Date of registration is empty", Toast.LENGTH_SHORT).show();
                return;
            }

            bundle.putString("dateOfRegistration", date);
        }

        //sending all the data to next fragment
        bundle.putStringArrayList("roles", saveRole);
        bundle.putString("age", String.valueOf(userAge));
        bundle.putString("first", first);
        bundle.putString("middle", middle);
        bundle.putString("last", last);
        bundle.putString("birth", birth);
        bundle.putString("gender", gender);
        bundle.putString("address", address);
        bundle.putString("zip", zip);

        fragment_registration_email_password requirements = new fragment_registration_email_password();
        requirements.setArguments(bundle);

        FragmentTransaction fr = getFragmentManager().beginTransaction();
        fr.replace(R.id.fragmentContainerView2, requirements);
        fr.addToBackStack("name");
        fr.commit();

    }//end of check input method


    @SuppressLint("SetTextI18n")
    private void openTransactionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = View.inflate(getContext(), R.layout.pet_add_birthday_dialog, null);
        DatePicker datePicker = view.findViewById(R.id.datePicker);
        datePicker.setMaxDate(System.currentTimeMillis());

        MaterialButton cancel, save;
        TextView message, title;
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
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);

                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                String formatedDate = sdf.format(calendar.getTime());
                reg_transactionEdit
                        .setText(formatedDate);
                reg_transactionEdit.setTextColor(ColorStateList.valueOf(Color.BLACK));
                Toast.makeText(getContext(), formatedDate, Toast.LENGTH_SHORT).show();
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

    String genderHolder = "";

    @SuppressLint("SetTextI18n")
    private void showGenderDialog() {
        String[] gender = {"Male", "Female", "Other"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = View.inflate(getContext(), R.layout.pet_add_gender_dialog, null);
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
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (genderHolder == "" || genderHolder == null) {
                    genderHolder = "0";
                }
                reg_genderEdit.setText(gender[Integer.parseInt(genderHolder)]);
                reg_genderEdit.setTextColor(ColorStateList.valueOf(Color.BLACK));
                Toast.makeText(getContext(), gender[Integer.parseInt(genderHolder)], Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public void clicked(int size) {
        Toast.makeText(getContext(), uri.size() + ": selected", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void itemClick(int position) {
        Dialog dialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        dialog.setContentView(R.layout.custom_dialog_zoom);

        TextView textView = dialog.findViewById(R.id.text_dialog);
        ImageView imageView = dialog.findViewById(R.id.image_view_dialog);
        Button buttonClose = dialog.findViewById(R.id.button_close_dialog);


        textView.setText("Image" + position);
        Glide.with(this)
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
        Toast.makeText(getContext(), uriShooter.size() + ": selected", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void itemClicks(int position) {
        Dialog dialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        dialog.setContentView(R.layout.custom_dialog_zoom);

        TextView textView = dialog.findViewById(R.id.text_dialog);
        ImageView imageView = dialog.findViewById(R.id.image_view_dialog);
        Button buttonClose = dialog.findViewById(R.id.button_close_dialog);


        textView.setText("Image" + position);
        Glide.with(this)
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

        private TextChange(View v) {
            view = v;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onTextChanged(CharSequence s, int i, int i1, int i2) {

            switch (view.getId()) {

                case R.id.firstName_Edit:
                    if (s != null && s.length() >= 2) {
                        reg_first.setHelperText("First name is valid");
                        reg_first.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#5CCD08")));
                    } else {
                        reg_first.setHelperText("Name must have 2 characters");
                        reg_first.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    }
                    break;

                case R.id.reg_lastNameEdit:
                    if (s != null && s.length() >= 2) {

                        reg_last.setHelperText("Last name is valid");
                        reg_last.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#5CCD08")));
                    } else {
                        reg_last.setHelperText("Last name must have 2 letters");
                        reg_last.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));

                    }
                    break;

                case R.id.reg_addressEdit:

                    if (s != null) {
                        if (s.length() > 5) {
                            reg_address.setHelperText("Address is valid");
                            reg_address.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#5CCD08")));
                        } else {

                            reg_address.setHelperText("Please input your correct address");
                            reg_address.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                        }
                    } else {
                        reg_address.setHelperText("Please input your address");
                        reg_address.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    }
                    break;

                case R.id.reg_zipEdit:
                    if (s != null && s.length() == 4) {
                        reg_zip.setHelperText("Zipcode is valid");
                        reg_zip.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#5CCD08")));
                    } else {
                        reg_zip.setHelperText("Zipcode must 4 numbers");
                        reg_zip.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    }
                    break;
                case R.id.reg_experienceEdit:
                    if (s.length() == 0) {
                        reg_experience.setHelperText("Please enter the number of years of experience");
                        reg_experience.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    } else {
                        reg_experience.setHelperText("");
                    }
                    break;
                case R.id.reg_kennelNameEdit:
                    if (s.length() == 0) {
                        reg_kennelName.setHelperText("Please enter your registered kennel name");
                        reg_kennelName.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    } else {
                        reg_kennelName.setHelperText("");
                    }
                    break;
                case R.id.reg_prc_id_edit:
                    if (s.length() == 0) {
                        reg_prc_id.setHelperText("Please enter your PRC id no.");
                        reg_prc_id.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    } else if (s.length() < 7) {
                        reg_prc_id.setHelperText("PRC id no. must have 7 digits");
                        reg_prc_id.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    } else {
                        reg_prc_id.setHelperText("");
                    }
                    break;
                case R.id.reg_memberID_edit:
                    if (s.length() == 0) {
                        reg_memberID.setHelperText("Please enter your Member id");
                        reg_memberID.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    } else if (s.length() < 5) {
                        reg_memberID.setHelperText("Member id must have 5 digits");
                        reg_memberID.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    } else {
                        reg_memberID.setHelperText("");
                    }
                    break;
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    ;
}