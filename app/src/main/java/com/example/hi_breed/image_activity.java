/*
package com.example.hi_breed;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.yalantis.ucrop.UCrop;

import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;


public class image_activity extends AppCompatActivity {



    Uri imgVet,uri;
    CardView  dropImageCardViewVet,dropImageCardViewShooter;
    ImageView  shooter_image,dropImageViewVet,prc_image ;
    Button submit;
    RelativeLayout reg_birth ;
    TextInputLayout  reg_prc_id,reg_first,reg_last,reg_middle,reg_address,reg_zip ;
    TextInputEditText reg_prc_id_edit,reg_firstEdit,reg_lastEdit,reg_middleEdit,reg_addressEdit,reg_zipEdit ;
    TextView add_photo_vet, reg_birthEdit ;

    // Declare class variable for the face detector
    private CascadeClassifier faceDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);



        dropImageCardViewShooter =findViewById(R.id.dropImageCardViewShooter);
        shooter_image=findViewById(R.id.shooter_image);
        //InputText
        reg_first = findViewById(R.id.firstName_Input);
        reg_middle = findViewById(R.id.reg_middleName);
        reg_last =findViewById(R.id.reg_lastName);
        reg_birth = findViewById(R.id.reg_birth);
        reg_address = findViewById(R.id.reg_address);
        reg_zip = findViewById(R.id.reg_zip);
        add_photo_vet = findViewById(R.id.add_photo_vet);
                //EditText
        //TextInput
        reg_firstEdit = findViewById(R.id.firstName_Edit);
        reg_middleEdit = findViewById(R.id.reg_middleNameEdit);
        reg_lastEdit = findViewById(R.id.reg_lastNameEdit);
        prc_image = findViewById(R.id.prc_image);
        reg_addressEdit = findViewById(R.id.reg_addressEdit);
        reg_zipEdit = findViewById(R.id.reg_zipEdit);
        dropImageCardViewVet = findViewById(R.id.dropImageCardViewVet);
        reg_prc_id= findViewById(R.id.reg_prc_id);
                reg_prc_id_edit= findViewById(R.id.reg_prc_id_edit);
        dropImageViewVet= findViewById(R.id.dropImageViewVet);
        reg_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBirthdayDialog();
            }
        });

        dropImageCardViewShooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( checkPermission()){
                    imagePermissionShooter();
                }
                else{
                    if (ContextCompat.checkSelfPermission(image_activity.this, READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Permission is not granted, request it
                        ActivityCompat.requestPermissions(image_activity.this,
                                new String[]{READ_EXTERNAL_STORAGE},
                                20);
                    }
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
                    if (ContextCompat.checkSelfPermission(image_activity.this, READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Permission is not granted, request it
                        ActivityCompat.requestPermissions(image_activity.this,
                                new String[]{READ_EXTERNAL_STORAGE},
                                30);
                    }
                }
            }
        });

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInput();
            }
        });
        // Initialize the face detector using the XML file


    }



    final int REQUEST_PICK_IMAGE = 1;
    final int REQUEST_PICK_IMAGE_SHOOTER = 2;
    private void imagePermissionVet() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }
    private void imagePermissionShooter() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGE_SHOOTER);
    }
    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int readCheck = ContextCompat.checkSelfPermission(image_activity.this, READ_EXTERNAL_STORAGE);
            int writeCheck = ContextCompat.checkSelfPermission(image_activity.this, WRITE_EXTERNAL_STORAGE);
            return readCheck == PackageManager.PERMISSION_GRANTED && writeCheck == PackageManager.PERMISSION_GRANTED;
        }
    }
    @SuppressLint("SetTextI18n")
    private void openBirthdayDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(image_activity.this);
        View view = View.inflate(image_activity.this,R.layout.pet_add_birthday_dialog,null);
        DatePicker datePicker = view.findViewById(R.id.datePicker);
        datePicker.setMaxDate(System.currentTimeMillis());
        builder.setView(view);
        MaterialButton cancel,save;
        TextView message,title;
        title =view.findViewById(R.id.pet_add_birthday_set_title);
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
                int   day  = datePicker.getDayOfMonth();
                int   month= datePicker.getMonth();
                int   year = datePicker.getYear();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);

                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                String formatedDate = sdf.format(calendar.getTime());
                reg_birthEdit.setText(formatedDate);
                reg_birthEdit.setTextColor(ColorStateList.valueOf(Color.BLACK));
                Toast.makeText(image_activity.this, formatedDate, Toast.LENGTH_SHORT).show();
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

    private void checkInput() {

    }



    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_PICK_IMAGE) {
            Uri imageUri = data.getData();
            cropImage(imageUri,prc_image,"vet");
        }
        else  if (resultCode == RESULT_OK && requestCode == REQUEST_PICK_IMAGE_SHOOTER) {
            Uri imageUri = data.getData();
            cropImage(imageUri,shooter_image,"shooter");
        }
        else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            Uri croppedUri = UCrop.getOutput(data);
            // Use the cropped image URI as needed
            if (croppedUri != null) {
                if (currentImageView != null && types != null) {
                    if (types.equals("vet")) {
                        prc_image.setVisibility(View.VISIBLE);
                        imgVet = croppedUri;
                        Bitmap bitmap = loadFromUri(croppedUri);

                        Glide.with(this)
                                .load(croppedUri)
                                .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
                                .placeholder(R.drawable.noimage)
                                .into(prc_image);
                        add_photo_vet.setVisibility(View.GONE);
                        dropImageViewVet.setVisibility(View.GONE);
                    } else if (types.equals("shooter")) {
                        uri = croppedUri;
                        Glide.with(this)
                                .load(croppedUri)
                                .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
                                .placeholder(R.drawable.noimage)
                                .into(shooter_image);



                    }else{
// Invalid input photos
                             reg_zipEdit.setText("Invalid input photos");
                            }
                        }
                    }

            } else if (resultCode == UCrop.RESULT_ERROR) {
                Throwable error = UCrop.getError(data);

                // Handle UCrop error as needed
            } else {
                //this code is for if user not picked any image
                Toast.makeText(this, "You Haven't Picked any image", Toast.LENGTH_SHORT).show();
            }
        }

    private Bitmap loadFromUri(Uri croppedUri) {
        Bitmap bitmap = null;
            try {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(),croppedUri);
                        bitmap = ImageDecoder.decodeBitmap(source);
                }
                else{
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),croppedUri);

                }
            }catch(IOException e){
                e.printStackTrace();
            }
        return bitmap;
    }

    private ImageView currentImageView;
    private String types;
    private void cropImage(Uri sourceUri, ImageView imageView,String type) {
        currentImageView = imageView;
        types =type;
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "cropped"));
        UCrop uCrop = UCrop.of(sourceUri, destinationUri);
        uCrop.start(this);
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
                Toast.makeText(image_activity.this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == 20){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, open the gallery
                imagePermissionShooter();
            } else {
                // Permission is not granted, show an error message
                Toast.makeText(image_activity.this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}*/
