package com.example.hi_breed.product;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hi_breed.R;
import com.example.hi_breed.adapter.product_adapter.product_edit_adapter;
import com.example.hi_breed.classesFile.product_class;
import com.example.hi_breed.shop.user_breeder_shop_panel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
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
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class product_my_product_edit extends AppCompatActivity implements product_edit_adapter.CountOfImagesCurrent,product_edit_adapter.itemCurrentClickListenerPet{

    ListView listView;
    EditText editText;
    RelativeLayout addCustom;
    ArrayAdapter<String> arrayAdapter;
    FirebaseUser firebaseUser;
    FirebaseFirestore fireStore;
    DocumentReference documentReference;
    FirebaseStorage storage;
    //ImageView
    ImageView petColorSave, petDescSave, petNameSave;
    ImageView petNameClear, petDescClear, petColorClear;
    RecyclerView pet_images_view;
    Button delete;
    product_edit_adapter adapter;
    ArrayList<Uri> uri = new ArrayList<>();
    Uri imageUri;
    int countOfImages;
    String oldName,oldDesc,oldColor,productCategory;
    String id,vetID;
    TextView petKiloTextView, petSizeTextView, petBreedTextView, petBirthdayTextView, petPriceTextView, petNameCount, petDescCount, petColorCount;
    String kennel;
    //Relative Layout
    RelativeLayout sizeLayout, kiloLayout, categoryLayout, birthdayLayout, priceLayout,
            photoLayout,petLayout,descLayout,colorLayout;
    //Linear Layout
    LinearLayout backLayoutPet;
    CardView petPhotoCardView;
    TextInputEditText petNameEdit, petDescEdit, petColorEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_my_product_edit);


        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        }

        //User
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //Firebase
        fireStore = FirebaseFirestore.getInstance();
        documentReference = fireStore.collection("User").document(firebaseUser.getUid());


        storage = FirebaseStorage.getInstance();
        delete = findViewById(R.id.delete);
        //ImageView
        petDescClear = findViewById(R.id.petDescClearButton);
        petNameClear = findViewById(R.id.petNameclearButton);
        petColorClear = findViewById(R.id.petColorClearButton);
        //EditText
        petNameEdit = findViewById(R.id.petNameEdit);
        petNameCount = findViewById(R.id.petNamecountID);
        petDescEdit = findViewById(R.id.petsDescEdit);
        petDescCount = findViewById(R.id.petDescCountID);
        petColorEdit = findViewById(R.id.petColorEdit);
        petColorCount = findViewById(R.id.petColorCount);
        petDescSave = findViewById(R.id.petDescSave);
        petNameSave = findViewById(R.id.petNameSave);
        petColorSave = findViewById(R.id.petColorSave);
        //RecyclerView

        pet_images_view = findViewById(R.id.product_images_views);

        //for image
        adapter = new product_edit_adapter(uri, getApplicationContext(), this, this,id,vetID);
        pet_images_view.setLayoutManager(new GridLayoutManager(this, 4));

        //LinearLayout
        backLayoutPet = findViewById(R.id.backLayoutPet);
        //TextView
        petPriceTextView = findViewById(R.id.productPrice);
        petBirthdayTextView = findViewById(R.id.productExpiration);
        petBreedTextView = findViewById(R.id.productCategory);
        petKiloTextView = findViewById(R.id.productStocks);
        petSizeTextView = findViewById(R.id.productBrand);

        //CardView
        petPhotoCardView = findViewById(R.id.petPhotoCardView);
        //RelativeLayout
        colorLayout = findViewById(R.id.colorLayout);
        descLayout = findViewById(R.id.descLayout);
        colorLayout = findViewById(R.id.colorLayout);
        petLayout = findViewById(R.id.petLayout);
        photoLayout = findViewById(R.id.photoLayout);
        priceLayout = findViewById(R.id.priceLayout);
        birthdayLayout = findViewById(R.id.birthdayLayout);
        categoryLayout = findViewById(R.id.categoryLayout);
        kiloLayout = findViewById(R.id.kiloLayout);
        sizeLayout = findViewById(R.id.sizeLayout);


        Intent intent  = getIntent();
        product_class p = (product_class) intent.getSerializableExtra("mode");

        if(p!=null){
            id = p.getId();
            vetID = p.getVet_id();

            oldColor = p.getProd_treatment();
            oldDesc = p.getProd_description();
            oldName = p.getProd_name();
            productCategory = p.getProd_category();

            if(p.getProd_category().equals("Medicine")){
                colorLayout.setVisibility(View.VISIBLE);
                birthdayLayout.setVisibility(View.VISIBLE);
                petColorEdit.setText(p.getProd_treatment());
                petBirthdayTextView.setText(p.getProd_expiration());
            }
            else{
                colorLayout.setVisibility(View.GONE);
                birthdayLayout.setVisibility(View.GONE);
            }
            petBreedTextView.setText(p.getProd_category());
            petNameEdit.setText(p.getProd_name());
            petDescEdit.setText(p.getProd_description());
            petPriceTextView.setText(p.getProd_price());
            petKiloTextView.setText(p.getProd_stocks());
            petSizeTextView.setText(p.getProd_brand());

            if(p.getPhotos()!=null){
                for (int i = 0; i<p.getPhotos().size();i++){
                    Uri imageUri = Uri.parse(p.getPhotos().get(i));
                    pet_images_view.setVisibility(View.VISIBLE);
                    uri.add(imageUri);
                    if (uri.size() == 0) {
                        pet_images_view.setVisibility(View.GONE);
                    }
                    adapter = new product_edit_adapter(uri, getApplicationContext(),this,this,id,vetID);
                    adapter.notifyDataSetChanged();
                    pet_images_view.setAdapter(adapter);
                }
            }


        }


        backLayoutPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product_my_product_edit.this.onBackPressed();
                finish();
            }
        });
        categoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryDialog(p.getId(),p.getVet_id());
            }
        });
        kiloLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStocksDialog(p.getId(),p.getVet_id());
            }
        });
        sizeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBrandDialog(p.getId(),p.getVet_id());
            }
        });


        birthdayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExpirationDialog(p.getId(),p.getVet_id());
            }
        });

        priceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPrice(p.getId(),p.getVet_id());
            }
        });
        petPhotoCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( checkPermission()){
                    imagePermission();
                }
                else{
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Permission is not granted, request it
                        ActivityCompat.requestPermissions(product_my_product_edit.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                30);
                    }
                }
            }
        });


        if (petNameEdit.getText() != null || petNameEdit.getText().equals("")) {
            petNameEdit.addTextChangedListener(petNameTextEditorWatcher);
        }
        if (petDescEdit.getText() != null || petDescEdit.getText().equals("")) {
            petDescEdit.addTextChangedListener(petDescTextEditorWatcher);
        }
        if (petColorEdit.getText() != null || petColorEdit.getText().equals("")) {
            petColorEdit.addTextChangedListener(petColorTextEditorWatcher);
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder builder2 = new androidx.appcompat.app.AlertDialog.Builder(product_my_product_edit.this);
                builder2.setCancelable(false);
                View view = View.inflate(product_my_product_edit.this, R.layout.screen_custom_alert, null);
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
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(product_my_product_edit.this);
                        builder2.setCancelable(false);
                        View view = View.inflate(product_my_product_edit.this, R.layout.screen_custom_alert, null);
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


                        FirebaseFirestore.getInstance().collection("Pet")
                                .document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            FirebaseFirestore.getInstance().collection("User")
                                                    .document(vetID)
                                                    .collection("Product")
                                                    .document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                alert2.dismiss();
                                                               FirebaseFirestore.getInstance().collection("Shop")
                                                                       .document(vetID)
                                                                       .collection("Product")
                                                                       .document(id)
                                                                       .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                           @Override
                                                                           public void onComplete(@NonNull Task<Void> task) {
                                                                                if(task.isSuccessful()){
                                                                                    FirebaseFirestore.getInstance().collection("Search")
                                                                                            .document(id)
                                                                                            .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                    StorageReference   storageReference= FirebaseStorage.getInstance().getReference("User/"+id+"/"+"Product/"+productCategory+"_"+id);
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
                                                                                                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(product_my_product_edit.this);
                                                                                                                    builder2.setCancelable(false);
                                                                                                                    View view = View.inflate(product_my_product_edit.this, R.layout.screen_custom_alert, null);
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
                                                                                                                            startActivity(new Intent(product_my_product_edit.this, user_breeder_shop_panel.class));
                                                                                                                            finish();
                                                                                                                        }
                                                                                                                    });

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

    @SuppressLint("SetTextI18n")
    private void showStocksDialog(String id,String vetID) {

        AlertDialog.Builder builder = new AlertDialog.Builder(product_my_product_edit.this);
        View view = View.inflate(product_my_product_edit.this,R.layout.pet_add_vaccine_price_dialog,null);
        MaterialButton done, cancel;
        TextView message,title;
        message = view.findViewById(R.id.price_message);
        title = view.findViewById(R.id.price_title);

        EditText text;

        AppCompatImageView appCompatImageView = view.findViewById(R.id.appCompatImageView);
        appCompatImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.dialog_stocks_borders));

        message.setText("Tip: Set a dog friendly price to our lovely owners.");
        title.setText("Set available stocks");
        text = view.findViewById(R.id.price_dialog_textin);
        done = view.findViewById(R.id.price_dialog_btn_done);
        cancel = view.findViewById(R.id.price_dialog_btn_cancel);

        text.setHint("Enter available stocks . . .");
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
                    petKiloTextView.setText(text.getText().toString());
                    updateProduct(id,"prod_stocks",text.getText().toString(),vetID);
                    Toast.makeText(product_my_product_edit.this, text.getText().toString(), Toast.LENGTH_SHORT).show();
                    counter = 1;
                }
                if(counter !=0){
                    alert.dismiss();
                }
                else{
                    Toast.makeText(product_my_product_edit.this, "Cannot save an empty text field", Toast.LENGTH_SHORT).show();
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


    int    count = 0;
    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    private void showBrandDialog(String id,String vetID) {

        AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
        View view2 = View.inflate(this,R.layout.screen_custom_alert,null);
        builder3.setCancelable(false);
        //title
        TextView title2 = view2.findViewById(R.id.screen_custom_alert_title);
        title2.setText("Set product brand");
        //message
        TextView message2 = view2.findViewById(R.id.screen_custom_alert_message);
        message2.setText("Tip: Please input your product brand");
        //loading
        TextView loadingText = view2.findViewById(R.id.screen_custom_alert_loadingText);
        loadingText.setVisibility(View.GONE);
        //top image
        AppCompatImageView imageViewCompat2 = view2.findViewById(R.id.appCompatImageView);
        imageViewCompat2.setVisibility(View.VISIBLE);
        imageViewCompat2.setImageDrawable(getDrawable(R.drawable.dialog_brand_borders));
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
        editText.setHint("Set brand . . .");

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

                    clear.setVisibility(View.VISIBLE);
                    clear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editText.getText().clear();
                        }
                    });
                }
                else{
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
                if(editText.getText().toString().equals("")  || editText.getText().toString() == null){
                    count = 0;
                    Toast.makeText(product_my_product_edit.this, "Oops. You made no entry! a Product brand was expected", Toast.LENGTH_SHORT).show();
                }
                else{
                    petSizeTextView.setText(editText.getText().toString());
                    updateProduct(id,"prod_brand",editText.getText().toString(),vetID);
                    Toast.makeText(product_my_product_edit.this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
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

    private void openExpirationDialog(String id,String vetID) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this,R.layout.product_add_expiration_dialog,null);
        DatePicker datePicker = view.findViewById(R.id.datePickers);
        Calendar calendars = Calendar.getInstance();
        long minDate = calendars.getTimeInMillis();
        calendars.set(9999, 11, 31);
        long maxDate = calendars.getTimeInMillis();

        datePicker.setMinDate(minDate);
        datePicker.setMaxDate(maxDate);
        builder.setView(view);
        MaterialButton cancel,save;

        cancel = view.findViewById(R.id.product_dialog_btn_cancel);
        save = view.findViewById(R.id.product_dialog_btn_done);
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
                Calendar calendars = Calendar.getInstance();
                calendars.set(year, month, day);

                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                String formattedDate = sdf.format(calendars.getTime());
                petBirthdayTextView.setText(formattedDate);
                updateProduct(id,"prod_expiration",formattedDate,vetID);

                Toast.makeText(product_my_product_edit.this,formattedDate, Toast.LENGTH_SHORT).show();
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

    @SuppressLint("UseCompatLoadingForDrawables")
    private void showCategoryDialog(String id,String vetID) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View pop = View.inflate(product_my_product_edit.this,R.layout.pet_add_search_dialog,null);
        AppCompatImageView appCompatImageView = pop.findViewById(R.id.appCompatImageView);
        appCompatImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.dialog_product_borders));
        TextView textView = pop.findViewById(R.id.textView);
        textView.setVisibility(View.VISIBLE);
        addCustom = pop.findViewById(R.id.search_customBreedID);
        addCustom.setVisibility(View.GONE);
        editText = pop.findViewById(R.id.search_searchEditID);
        editText.setVisibility(View.GONE);
        TextInputLayout input = pop.findViewById(R.id.input);
        input.setVisibility(View.GONE);
        listView = pop.findViewById(R.id.search_breedListView);
        MaterialButton cancel;
        cancel = pop.findViewById(R.id.search_dialog_btn_cancel);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Arrays.asList("Medicine","Dog Accessories"));
        listView.setAdapter(arrayAdapter);
        listView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        builder.setView(pop);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if(editText.getText()!=null || editText.getText().equals("")) {
            editText.addTextChangedListener(filterTextWatcher);
        }

        if(listView.getCount() == 0 || listView == null){
            listView.setVisibility(View.GONE);
            listView.setCacheColorHint(Color.TRANSPARENT);
            listView.setBackground(new ColorDrawable(Color.TRANSPARENT));
            listView.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        }
        else{
            listView.setBackground(getDrawable(R.drawable.shape));
            listView.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            listView.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = 400;
            listView.setLayoutParams(params);
            listView.requestLayout();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long ids) {
                String holder = listView.getItemAtPosition(position).toString();
                if(holder.equals("Dog Accessories")){
                    photoLayout.setVisibility(View.VISIBLE);
                    descLayout.setVisibility(View.VISIBLE);
                    colorLayout.setVisibility(View.GONE);
                    petLayout.setVisibility(View.VISIBLE);
                    sizeLayout.setVisibility(View.VISIBLE);
                    kiloLayout.setVisibility(View.VISIBLE);
                    priceLayout.setVisibility(View.VISIBLE);
                    birthdayLayout.setVisibility(View.GONE);

                }else{
                    photoLayout.setVisibility(View.VISIBLE);
                    descLayout.setVisibility(View.VISIBLE);
                    petLayout.setVisibility(View.VISIBLE);
                    sizeLayout.setVisibility(View.VISIBLE);
                    kiloLayout.setVisibility(View.VISIBLE);
                    priceLayout.setVisibility(View.VISIBLE);
                    colorLayout.setVisibility(View.VISIBLE);
                    birthdayLayout.setVisibility(View.VISIBLE);
                }

                petBreedTextView.setText(holder);
                updateProduct(id,"prod_category",holder,vetID);
                Toast.makeText(product_my_product_edit.this, holder, Toast.LENGTH_SHORT).show();
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
    int counter = 0;
    private void showDialogPrice(String id,String vetID) {

        AlertDialog.Builder builder = new AlertDialog.Builder(product_my_product_edit.this);
        View view = View.inflate(product_my_product_edit.this,R.layout.pet_add_vaccine_price_dialog,null);
        MaterialButton done, cancel;
        EditText text;
        text = view.findViewById(R.id.price_dialog_textin);
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
                    petPriceTextView.setText(text.getText().toString());
                    updateProduct(id,"prod_price",text.getText().toString(),vetID);
                    Toast.makeText(product_my_product_edit.this, text.getText().toString(), Toast.LENGTH_SHORT).show();
                    counter = 1;
                }
                if(counter !=0){
                    alert.dismiss();
                }
                else{
                    Toast.makeText(product_my_product_edit.this, "Cannot save an empty text field", Toast.LENGTH_SHORT).show();
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


    private void updateProduct(String id, String field, String text,String vetID) {
        Map<String,Object> map = new HashMap<>();
        map.put(field,text);

        //Pet Products
        FirebaseFirestore.getInstance().collection("Pet")
                .document(id).set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        FirebaseFirestore.getInstance().collection("Shop")
                                .document(vetID).collection("Product")
                                .document(id)
                                .set(map,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                            FirebaseFirestore.getInstance().collection("User")
                                                    .document(vetID)
                                                    .collection("Product")
                                                    .document(id)
                                                    .set(map,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                Toast.makeText(product_my_product_edit.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                    }
                                });
                    }
                });


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
                AlertDialog.Builder builder2 = new AlertDialog.Builder(product_my_product_edit.this);
                builder2.setCancelable(false);
                View view = View.inflate(product_my_product_edit.this,R.layout.screen_custom_alert,null);
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
                title.setText("Updating your product pictures");
                message.setVisibility(View.VISIBLE);
                message.setText("Please wait a bit. We are updating your product picture");
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

    private void uploadToFirebaseStorage(String id, AlertDialog alert2, Uri imageUri) {

        String imageName = "product_" + id + "_" + System.currentTimeMillis();
        StorageReference ImageFolder = FirebaseStorage.getInstance().getReference("User/"+id+"/"+"Product"+"/"+productCategory+"_"+id+"/"+imageName);
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

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    private void StringLinkPaper(String id, AlertDialog alertDialog, String url) {
        FirebaseFirestore.getInstance()
                .collection("Pet")
                .document(id)
                .update("photos", FieldValue.arrayUnion(url)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            FirebaseFirestore.getInstance().collection("Shop")
                                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .collection("Product")
                                                            .document(id)
                                                                    .update("photos",FieldValue.arrayUnion(url)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                FirebaseFirestore.getInstance().collection("User")
                                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Product").document(id)
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


    private final TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            arrayAdapter.getFilter().filter(s);
        }
    };
    private final TextWatcher petNameTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @SuppressLint("SetTextI18n")
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            petNameCount.setText(s.length() +"/100");
            if(s.length()!=0){
                petNameClear.setVisibility(View.VISIBLE);
                petNameClear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        petNameEdit.getText().clear();
                    }
                });
                if(s.toString().equals(oldName)){
                    petNameSave.setVisibility(View.GONE);
                }
                else{
                    petNameSave.setVisibility(View.VISIBLE);
                    petNameSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateProduct(id,"prod_name",s.toString(),vetID);
                            oldName = s.toString();


                            petNameSave.setVisibility(View.GONE);
                        }
                    });
                }
            }
            else{
                petNameSave.setVisibility(View.GONE);
                petNameClear.setVisibility(View.GONE);
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
            petDescCount.setText(s.length() +"/2000");
            if(s.length()!=0){
                petDescClear.setVisibility(View.VISIBLE);
                petDescClear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        petDescEdit.getText().clear();
                    }
                });
                if(s.toString().equals(oldDesc)){
                    petDescSave.setVisibility(View.GONE);
                }
                else{
                    petDescSave.setVisibility(View.VISIBLE);
                    petDescSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateProduct(id,"prod_description",s.toString(),vetID);
                            oldDesc = s.toString();
                            petDescSave.setVisibility(View.GONE);
                        }
                    });
                }
            }
            else{
                petDescSave.setVisibility(View.GONE);
                petDescClear.setVisibility(View.GONE);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };
    private final TextWatcher petColorTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @SuppressLint("SetTextI18n")
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            petColorCount.setText(s.length() +"/200");
            if(s.length()!=0){
                petColorClear.setVisibility(View.VISIBLE);
                petColorClear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        petColorEdit.getText().clear();
                    }
                });
                if(s.toString().equals(oldColor)){
                    petColorSave.setVisibility(View.GONE);
                }
                else{
                    petColorSave.setVisibility(View.VISIBLE);
                    petColorSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateProduct(id,"prod_description",s.toString(),vetID);
                            oldColor = s.toString();
                            petColorSave.setVisibility(View.GONE);
                        }
                    });
                }
            }
            else{
                petColorSave.setVisibility(View.GONE);
                petColorClear.setVisibility(View.GONE);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };



    @Override
    public void clickCurrent(int size) {
        Toast.makeText(this,uri.size()+": selected",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void itemCurrentClick(int position) {
        Dialog dialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        dialog.setContentView(R.layout.custom_dialog_zoom);

        TextView textView = dialog.findViewById(R.id.text_dialog);
        ImageView imageView = dialog.findViewById(R.id.image_view_dialog);
        Button buttonClose = dialog.findViewById(R.id.button_close_dialog);


        textView.setText("Image"+position);
        Glide.with(this).load(uri.get(position)).into(imageView);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}