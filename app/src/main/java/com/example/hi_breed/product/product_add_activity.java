package com.example.hi_breed.product;

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
import com.example.hi_breed.adapter.petImagesRecyclerAdapter;
import com.example.hi_breed.classesFile.item;
import com.example.hi_breed.classesFile.product_class;
import com.example.hi_breed.shop.user_breeder_shop_panel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;

import pl.droidsonroids.gif.GifImageView;

public class product_add_activity extends AppCompatActivity implements petImagesRecyclerAdapter.CountOfImagesWhenRemovedPet,  petImagesRecyclerAdapter.itemClickListenerPet {
    private  product_class prod;
    ListView listView;
    EditText editText;
    RelativeLayout addCustom;
    ArrayAdapter<String> arrayAdapter;
    FirebaseUser firebaseUser;
    FirebaseFirestore fireStore;
    DocumentReference documentReference;
    FirebaseStorage storage;
    //ImageView
    ImageView petNameClear, petDescClear, petColorClear;
    RecyclerView pet_images_view;
    petImagesRecyclerAdapter pet_adapter;
    ArrayList<Uri> uri = new ArrayList<>();
    Uri imageUri;
    int countOfImages;
    String address = "";
    TextView petKiloTextView, petSizeTextView, petBreedTextView, petBirthdayTextView, petPriceTextView, petNameCount, petDescCount, petColorCount;
    String kennel;
    //Relative Layout
    RelativeLayout sizeLayout, kiloLayout, categoryLayout, birthdayLayout, priceLayout,
    photoLayout,petLayout,descLayout,colorLayout;
    //Linear Layout
    LinearLayout backLayoutPet;
    CardView petPhotoCardView;
    TextInputEditText petNameEdit, petDescEdit, petColorEdit;
    //Button
    Button pet_add_create_Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_add_activity);

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

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
               address = documentSnapshot.getString("address");
            }
        });

        storage = FirebaseStorage.getInstance();

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
        //Button
        pet_add_create_Button = findViewById(R.id.pet_add_create_Button);
        //RecyclerView

        pet_images_view = findViewById(R.id.product_images_views);

        //for image
        pet_adapter = new petImagesRecyclerAdapter(uri, getApplicationContext(), this, this);
        pet_images_view.setLayoutManager(new GridLayoutManager(this, 4));
        pet_images_view.setAdapter(pet_adapter);


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


        backLayoutPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product_add_activity.this.onBackPressed();
                finish();
            }
        });
        categoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryDialog();
            }
        });
        kiloLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStocksDialog();
            }
        });
        sizeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBrandDialog();
            }
        });


        birthdayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExpirationDialog();
            }
        });

        priceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPrice();
            }
        });
        petPhotoCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePermission();
            }
        });

        pet_add_create_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadToFirebaseDatabase();
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

    }

    @SuppressLint("SetTextI18n")
    private void showStocksDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(product_add_activity.this);
        View view = View.inflate(product_add_activity.this,R.layout.pet_add_vaccine_price_dialog,null);
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
                    Toast.makeText(product_add_activity.this, text.getText().toString(), Toast.LENGTH_SHORT).show();
                    counter = 1;
                }
                if(counter !=0){
                    alert.dismiss();
                }
                else{
                    Toast.makeText(product_add_activity.this, "Cannot save an empty text field", Toast.LENGTH_SHORT).show();
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
    private void showBrandDialog() {

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
                    Toast.makeText(product_add_activity.this, "Oops. You made no entry! a Product brand was expected", Toast.LENGTH_SHORT).show();
                }
                else{
                    petSizeTextView.setText(editText.getText().toString());
                    Toast.makeText(product_add_activity.this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
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

    private void openExpirationDialog() {

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


                Toast.makeText(product_add_activity.this,formattedDate, Toast.LENGTH_SHORT).show();
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
    private void showCategoryDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View pop = View.inflate(product_add_activity.this,R.layout.pet_add_search_dialog,null);
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
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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

                Toast.makeText(product_add_activity.this, holder, Toast.LENGTH_SHORT).show();
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
    private void showDialogPrice() {

        AlertDialog.Builder builder = new AlertDialog.Builder(product_add_activity.this);
        View view = View.inflate(product_add_activity.this,R.layout.pet_add_vaccine_price_dialog,null);
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
                    Toast.makeText(product_add_activity.this, text.getText().toString(), Toast.LENGTH_SHORT).show();
                    counter = 1;
                }
                if(counter !=0){
                    alert.dismiss();
                }
                else{
                    Toast.makeText(product_add_activity.this, "Cannot save an empty text field", Toast.LENGTH_SHORT).show();
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

                        uri.add(imageUri);
                        pet_images_view.setVisibility(View.VISIBLE);

                    }else{
                        Toast.makeText(this,"Not allowed to pick more than 5 images",Toast.LENGTH_SHORT).show();
                    }
                }
                //then notify the adapter
                pet_adapter.notifyDataSetChanged();
            }else{
                //this is for single images
                if(uri.size() <5){
                    imageUri =data.getData();
                    uri.add(imageUri);
                    pet_images_view.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(this,"Not allowed to pick more than 5 images",Toast.LENGTH_SHORT).show();
                }
            }
            pet_adapter.notifyDataSetChanged();
        }
        else{
            //this code is for if user not picked any image

            Toast.makeText(this,"You Haven't Pick any image",Toast.LENGTH_SHORT).show();
        }
    }


    private void transistion(){
        Thread mSplashThread = new Thread() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(product_add_activity.this, user_breeder_shop_panel.class);
                product_add_activity.this.overridePendingTransition(R.drawable.fade_in, R.drawable.fade_out);
                startActivity(intent);
                finish();
            }
        };
        mSplashThread.start();
    }
    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    private void uploadToFirebaseDatabase() {
        String product_name = Objects.requireNonNull(petNameEdit.getText()).toString();
        String product_description = Objects.requireNonNull(petDescEdit.getText()).toString();
        String product_treatment = Objects.requireNonNull(petColorEdit.getText()).toString();
        String product_category = petBreedTextView.getText().toString();
        String product_expiration = petBirthdayTextView.getText().toString();
        String product_price = petPriceTextView.getText().toString();
        String brand = petSizeTextView.getText().toString();
        String stock = petKiloTextView.getText().toString();


        if(product_category.equals("Category")){
            Toast.makeText(this, "Please choose the category for this product", Toast.LENGTH_SHORT).show();
            return;
        }
        if(product_name.isEmpty()){
            Toast.makeText(this, "Product name should not be empty", Toast.LENGTH_SHORT).show();
            return;
        }else if (product_name.length() < 2){
            petNameEdit.requestFocus();
            Toast.makeText(this, "Product name should have name at least 2 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        if(product_description.isEmpty()){
            Toast.makeText(this, "Description should not be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (product_description.length() < 20){
            petNameEdit.requestFocus();
            Toast.makeText(this, "Product Description must have 20 characters and above", Toast.LENGTH_SHORT).show();
            return;
        }
        if(product_category.equals("Medicine")){
            if(product_treatment.isEmpty()){
                Toast.makeText(this, "Product treatment should not be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if(product_expiration.equals("Expiration")){
                Toast.makeText(this, "Please set the birthday of your pet", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(brand.equals("Brand")){
            Toast.makeText(this, "Please enter the brand for this product", Toast.LENGTH_SHORT).show();
            return;
        }
        if(stock.equals("Stocks")){
            Toast.makeText(this, "Please enter the Stocks for this product", Toast.LENGTH_SHORT).show();
            return;
        }
        if(product_price.equals("Price")){
            Toast.makeText(this, "Please set the price for your pet", Toast.LENGTH_SHORT).show();
            return;
        }
        if(uri.size() == 0){
            Toast.makeText(this, "Please provide a picture for your product", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setCancelable(true);
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
        imageViewCompat.setImageDrawable(getDrawable(R.drawable.dialog_pet_borders));
        //message
        TextView message = view.findViewById(R.id.screen_custom_alert_message);
        title.setText("Creating your product");
        message.setVisibility(View.VISIBLE);
        message.setText("Please wait a bit. We are creating your product info");
        builder2.setView(view);
        AlertDialog alert2 = builder2.create();
        alert2.show();
        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if(product_category.equals("Medicine")){
         prod = new product_class("",product_name,product_description,product_treatment,product_category,brand,product_price,product_expiration,stock,null,FirebaseAuth.getInstance().getCurrentUser().getUid(),"forProducts",address,Timestamp.now(),true);
         FirebaseFirestore.getInstance().collection("Pet")
                 .add(prod).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                     @Override
                     public void onSuccess(DocumentReference documentReference) {

                         FirebaseFirestore.getInstance().collection("Pet")
                                 .document(documentReference.getId())
                                 .update("id",documentReference.getId())
                                 .addOnSuccessListener(new OnSuccessListener<Void>() {
                                     @Override
                                     public void onSuccess(Void unused) {
                                         product_class  prod = new product_class(documentReference.getId(),product_name,product_description,"",product_category,brand,product_price,"",stock,null,FirebaseAuth.getInstance().getCurrentUser().getUid(),"forProducts",address, Timestamp.now(),true);
                                         //Shop Collection
                                         FirebaseFirestore.getInstance().collection("Shop")
                                                 .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                 .collection("Product")
                                                 .document(documentReference.getId())
                                                 .set(prod)
                                                 .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                     @Override
                                                     public void onComplete(@NonNull Task<Void> task) {
                                                         if(task.isSuccessful()){
                                                             //USER collection
                                                             FirebaseFirestore.getInstance().collection("User")
                                                                     .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                     .collection("Product")
                                                                     .document(documentReference.getId())
                                                                     .set(prod).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                         @Override
                                                                         public void onComplete(@NonNull Task<Void> task) {
                                                                             if(task.isSuccessful()){
                                                                                 UploadToStorage(documentReference.getId(),product_category,alert2);
                                                                             }
                                                                         }
                                                                     });
                                                         }
                                                     }
                                                 });

                                     }
                                 });


                     }
                 });

       }
        else{
         prod = new product_class("",product_name,product_description,"",product_category,brand,product_price,"",stock,null,FirebaseAuth.getInstance().getCurrentUser().getUid(),"forProducts",address,Timestamp.now(),true);
            FirebaseFirestore.getInstance().collection("Pet")
                    .add(prod).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            FirebaseFirestore.getInstance().collection("Pet")
                                    .document(documentReference.getId())
                                    .update("id",documentReference.getId())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                          product_class  prod = new product_class(documentReference.getId(),product_name,product_description,"",product_category,brand,product_price,"",stock,null,FirebaseAuth.getInstance().getCurrentUser().getUid(),"forProducts",address,null,true);

                                          //Shop Collection
                                            FirebaseFirestore.getInstance().collection("Shop")
                                                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                    .collection("Product")
                                                                            .document(documentReference.getId())
                                                                                    .set(prod)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){

                                                                //USER collection
                                                                FirebaseFirestore.getInstance().collection("User")
                                                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                        .collection("Product")
                                                                        .document(documentReference.getId())
                                                                        .set(prod).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                           if(task.isSuccessful()){
                                                                               UploadToStorage(documentReference.getId(),product_category,alert2);
                                                                           }
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    });
                                        }
                                    });
                        }
                    });
       }
    }
    int upload=0, counterPhotos=0;

    private void UploadToStorage(String id,String category ,AlertDialog alert2) {
        String userID="";
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            userID = firebaseUser.getUid();
        }
            ArrayList<String> petPhotos = new ArrayList<>();
            String name = "product_" + id + "_" + System.currentTimeMillis();
            StorageReference ImageFolder = FirebaseStorage.getInstance().getReference("User/" + userID + "/" + "Product/" + petBreedTextView.getText().toString() + "/" + name);
            for (upload = 0; upload < uri.size(); upload++) {
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
                                if (counterPhotos == uri.size()) {
                                    StringLink(id, alert2, petPhotos);
                                }

                            }
                        });
                    }
                });
        }


    }

    private void StringLink(String id, AlertDialog alert2, ArrayList<String> petPhotos) {
        ArrayList<String> hashMap = new ArrayList<String>();
        hashMap.addAll(petPhotos);


        FirebaseFirestore.getInstance()
                .collection("Pet")
                .document(id)
                .update("photos",hashMap,"timestamp", FieldValue.serverTimestamp()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            FirebaseFirestore.getInstance()
                                    .collection("Shop")
                                    .document(firebaseUser.getUid())
                                    .collection("Product")
                                    .document(id)
                                    .update("photos",hashMap,"timestamp", FieldValue.serverTimestamp()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                FirebaseFirestore.getInstance()
                                                        .collection("User")
                                                        .document(firebaseUser.getUid())
                                                        .collection("Product")
                                                        .document(id)
                                                        .update("photos",hashMap,"timestamp", FieldValue.serverTimestamp()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    alert2.dismiss();

                                                                   item i = new item(id,firebaseUser.getUid(),petPriceTextView.getText().toString(),petBreedTextView.getText().toString(),petNameEdit.getText().toString(),address,true);

                                                                   FirebaseFirestore.getInstance().collection("Search")
                                                                           .document(id)
                                                                           .set(i).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                               @Override
                                                                               public void onComplete(@NonNull Task<Void> task) {
                                                                                    if(task.isSuccessful()){
                                                                                        AlertDialog.Builder builder2 = new AlertDialog.Builder(product_add_activity.this);
                                                                                        builder2.setCancelable(false);
                                                                                        View view = View.inflate(product_add_activity.this,R.layout.screen_custom_alert,null);
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
            }
            else{
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
            }
            else{
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
            petColorCount.setText(s.length() +"/2000");
            if(s.length()!=0){
                petColorClear.setVisibility(View.VISIBLE);
                petColorClear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        petColorEdit.getText().clear();
                    }
                });
            }
            else{
                petColorClear.setVisibility(View.GONE);
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