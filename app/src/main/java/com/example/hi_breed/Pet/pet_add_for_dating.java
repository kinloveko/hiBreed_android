package com.example.hi_breed.Pet;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.LayoutInflater;
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
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hi_breed.R;
import com.example.hi_breed.adapter.petImagesRecyclerAdapter;
import com.example.hi_breed.classesFile.BaseActivity;
import com.example.hi_breed.classesFile.BreedClass;
import com.example.hi_breed.classesFile.PetDateClass;
import com.example.hi_breed.classesFile.PhotoClass;
import com.example.hi_breed.classesFile.UserClass;
import com.example.hi_breed.userFile.dashboard.user_dashboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
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
import java.util.Objects;

import pl.droidsonroids.gif.GifImageView;

public class pet_add_for_dating extends BaseActivity implements petImagesRecyclerAdapter.CountOfImagesWhenRemovedPet,
        petImagesRecyclerAdapter.itemClickListenerPet{


    ListView listView;
    EditText editText;
    RelativeLayout addCustom;
    ArrayAdapter<String> arrayAdapter;
    RelativeLayout vaccine_container;
    LinearLayout added_vaccine;
    FirebaseUser firebaseUser;
    FirebaseFirestore fireStore;
    FirebaseStorage storage1;
    //ImageView
    ImageView petNameClear,petDescClear,petColorClear;
    RecyclerView pet_images_view;
    petImagesRecyclerAdapter pet_adapter;
    ArrayList<String> saveVaccine = new ArrayList<>();
    ArrayList<Uri> uri = new ArrayList<>();
    Uri imageUri;
    private int countOfImages;
    String numberHolder;
    TextView petSizeTextView,petKiloTextView,genderTextView,petBreedTextView,petBirthdayTextView,petPriceTextView,petNameCount,petDescCount,petColorCount,dewormingTextView,dewormCountOfTimes,text3,text4;

    //Relative Layout
    RelativeLayout petSizeLayout,petKiloLayout,genderLayout,categoryLayout,birthdayLayout,vaccineLayout;
    //Linear Layout
    LinearLayout backLayoutPet;
    CardView petPhotoCardView;
    TextInputEditText petNameEdit,petDescEdit,petColorEdit;
    EditText textIn;
    TextView imageClear;
    //Button
    Button buttonAdd,pet_add_create_Button;

    ProgressDialog progressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pet_add_for_dating);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        }

        progressDialog = new ProgressDialog(this);

        //User
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //Firebase
        fireStore = FirebaseFirestore.getInstance();

        storage1 = FirebaseStorage.getInstance();

        //ImageView
        petDescClear =findViewById(R.id.petDescClearButton);
        petNameClear = findViewById(R.id.petNameclearButton);
        petColorClear = findViewById(R.id.petColorClearButton);
        //EditText
        petNameEdit = findViewById(R.id.petNameEdit);
        petNameCount = findViewById(R.id.petNamecountID);
        petDescEdit = findViewById(R.id.petDescEdit);
        petDescCount = findViewById(R.id.petDescCountID);
        petColorEdit = findViewById(R.id.petColorEdit);
        petColorCount = findViewById(R.id.petColorCount);
        //Button
        pet_add_create_Button = findViewById(R.id.pet_add_create_Button);
        //RecyclerView
        pet_images_view = findViewById(R.id.pet_images_view);
        //adapter
        pet_adapter = new petImagesRecyclerAdapter(uri,getApplicationContext(),this, this);
        pet_images_view.setLayoutManager(new GridLayoutManager(this,4));
        pet_images_view.setAdapter(pet_adapter);
        //LinearLayout
        backLayoutPet = findViewById(R.id.backLayoutPet);
        //TextView
        dewormingTextView = findViewById(R.id.dewormingTextView);
        dewormCountOfTimes = findViewById(R.id.dewormCountOfTimes);
        petPriceTextView = findViewById(R.id.petPriceTextView);
        petBirthdayTextView = findViewById(R.id.petBirthdayTextView);
        genderTextView = findViewById(R.id.petGender);
        petBreedTextView = findViewById(R.id.petBreed);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);
        petSizeTextView = findViewById(R.id.petSize);
        petKiloTextView = findViewById(R.id.petKilo);

        //CardView
        petPhotoCardView = findViewById(R.id.petPhotoCardView);

        textIn =  findViewById(R.id.textin);
        imageClear = findViewById(R.id.clearIDText);
        textIn.addTextChangedListener(addVaccineTextWatcher);
        buttonAdd = findViewById(R.id.add);

        //RelativeLayout
        vaccineLayout = findViewById(R.id.vaccineLayout);
        birthdayLayout = findViewById(R.id.birthdayLayout);
        categoryLayout = findViewById(R.id.categoryLayout);
        vaccine_container = findViewById(R.id.save_container);
        added_vaccine = findViewById(R.id.added_vaccine);
        petKiloLayout = findViewById(R.id.kiloLayout);
        petSizeLayout = findViewById(R.id.sizeLayout);


        backLayoutPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pet_add_for_dating.this.onBackPressed();
                finish();
            }
        });
        categoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryDialog();
            }
        });
        petKiloLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKiloDialog();
            }
        });
        petSizeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSizeDialog();
            }
        });
        vaccineLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vaccine_container.isShown()){
                    vaccine_container.setVisibility(View.GONE);
                }
                else{
                    openVaccineDropdown();
                    vaccine_container.setVisibility(View.VISIBLE);
                }
            }
        });
        genderLayout = findViewById(R.id.genderLayout);
        genderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGenderDialog();
            }
        });

        birthdayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
  /*              datePickerClass pickerClass = new datePickerClass(datePickerDialog,pet_add.this,R.id.petBirthdayTextView);
               if(pickerClass!=null) {
                   pickerClass.initDatePicker();
               }
            */   openBirthdayDialog();

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

        if(petNameEdit.getText() !=null || petNameEdit.getText().equals("")) {
            petNameEdit.addTextChangedListener(petNameTextEditorWatcher);
        }
        if(petDescEdit.getText() !=null || petDescEdit.getText().equals("")) {
            petDescEdit.addTextChangedListener(petDescTextEditorWatcher);
        }
        if( petColorEdit.getText() !=null ||  petColorEdit.getText().equals("")) {
            petColorEdit.addTextChangedListener(petColorTextEditorWatcher);
        }
    }



    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    private void showSizeDialog() {

        AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
        View view2 = View.inflate(this,R.layout.screen_custom_alert,null);
        builder3.setCancelable(false);
        //title
        TextView title2 = view2.findViewById(R.id.screen_custom_alert_title);
        title2.setText("Pet Size");
        //message
        TextView message2 = view2.findViewById(R.id.screen_custom_alert_message);
        message2.setText("Please input your pets size");
        //loading
        TextView loadingText = view2.findViewById(R.id.screen_custom_alert_loadingText);
        loadingText.setVisibility(View.GONE);
        //top image
        AppCompatImageView imageViewCompat2 = view2.findViewById(R.id.appCompatImageView);
        imageViewCompat2.setVisibility(View.VISIBLE);
        imageViewCompat2.setImageDrawable(getDrawable(R.drawable.dialog_size_borders));
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
        editText.setHint("Ex. small . .");

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
                if(editText.getText().toString().equals("")  || editText.getText().toString() == null){
                    count = 0;
                    Toast.makeText(pet_add_for_dating.this, "Oops. You made no entry! a Pet size was expected", Toast.LENGTH_SHORT).show();
                }
                else{
                    petSizeTextView.setText(editText.getText().toString());
                    Toast.makeText(pet_add_for_dating.this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
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

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    private void showKiloDialog() {

        AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
        View view2 = View.inflate(this,R.layout.screen_custom_alert,null);
        builder3.setCancelable(false);
        //title
        TextView title2 = view2.findViewById(R.id.screen_custom_alert_title);
        title2.setText("Pet Weight");
        //message
        TextView message2 = view2.findViewById(R.id.screen_custom_alert_message);
        message2.setText("Please input your pets weight");
        //loading
        TextView loadingText = view2.findViewById(R.id.screen_custom_alert_loadingText);
        loadingText.setVisibility(View.GONE);
        //top image
        AppCompatImageView imageViewCompat2 = view2.findViewById(R.id.appCompatImageView);
        imageViewCompat2.setVisibility(View.VISIBLE);
        imageViewCompat2.setImageDrawable(getDrawable(R.drawable.dialog_kilo_borders));
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
        editText.setHint("Kilogram . .");

        editText.setInputType(InputType.TYPE_CLASS_PHONE);
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
                    Toast.makeText(pet_add_for_dating.this, "Oops. You made no entry! a Pet weight was expected", Toast.LENGTH_SHORT).show();
                }
                else{
                    petKiloTextView.setText(editText.getText().toString());
                    Toast.makeText(pet_add_for_dating.this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
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

    private void openBirthdayDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(pet_add_for_dating.this);
        View view = View.inflate(pet_add_for_dating.this,R.layout.pet_add_birthday_dialog,null);
        DatePicker datePicker = view.findViewById(R.id.datePicker);
        datePicker.setMaxDate(System.currentTimeMillis());
        builder.setView(view);
        MaterialButton cancel,save;

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
                petBirthdayTextView.setText(formatedDate);
                Toast.makeText(pet_add_for_dating.this, formatedDate, Toast.LENGTH_SHORT).show();
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

    private int count = 0;    String numberHolders;
    private static int counter = 0;
    View saveView;
    private void openVaccineDropdown() {

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View arg0) {
                if (textIn.getText().toString().equals("") || textIn.getText().toString() == null) {
                    Toast.makeText(pet_add_for_dating.this, "Input is empty", Toast.LENGTH_SHORT).show();
                } else {

                    AlertDialog.Builder numberBuilder = new AlertDialog.Builder(pet_add_for_dating.this);
                    View view = View.inflate(pet_add_for_dating.this, R.layout.pet_add_vaccine_taken_dialog, null);
                    numberBuilder.setCancelable(false);
                    MaterialButton done,cancel;
                    done = view.findViewById(R.id.btn_done);
                    cancel = view.findViewById(R.id.btn_cancel);
                    NumberPicker numberPicker = view.findViewById(R.id.numberPicker);
                    numberPicker.setMinValue(1);
                    numberPicker.setMaxValue(5);
                    numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                            numberHolder = String.valueOf(newVal);

                        }
                    });

                    numberBuilder.setView(view);
                    AlertDialog finalAlert = numberBuilder.create();
                    finalAlert.show();
                    finalAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            count +=1;
                            LayoutInflater layoutInflater =
                                    (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            saveView = layoutInflater.inflate(R.layout.pet_add_vaccine_save_row, null);
                            TextView save_textOut = saveView.findViewById(R.id.vaccine_save_textout);
                            TextView save_count = saveView.findViewById(R.id.vaccine_save_count);

                            if(numberHolder == ""|| numberHolder == null || numberHolder == "0"){
                                numberHolder = "1";
                            }

                            save_textOut.setText(textIn.getText().toString());
                            save_count.setText(numberHolder);


                            save_textOut.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(pet_add_for_dating.this);
                                    View views = View.inflate(pet_add_for_dating.this, R.layout.pet_add_edit_vaccine_dialog, null);

                                    EditText edit =  views.findViewById(R.id.edit_vaccine_dialog_textin);
                                    MaterialButton done,cancel;
                                    done = views.findViewById(R.id.edit_vaccine_dialog_btn_done);
                                    cancel = views.findViewById(R.id.edit_vaccine_dialog_btn_cancel);

                                    builder.setView(views);
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                    alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                                    done.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if(edit.getText().toString() == null || edit.getText().toString().equals("") ){
                                                counter = 0;

                                            }else {
                                                counter = 1;
                                                AlertDialog.Builder numberBuilder = new AlertDialog.Builder(pet_add_for_dating.this);
                                                View view = View.inflate(pet_add_for_dating.this, R.layout.pet_add_vaccine_taken_dialog, null);
                                                MaterialButton done_copy,cancel_copy;
                                                done_copy = view.findViewById(R.id.btn_done);
                                                cancel_copy = view.findViewById(R.id.btn_cancel);
                                                NumberPicker numberPicker = view.findViewById(R.id.numberPicker);
                                                numberPicker.setMinValue(1);
                                                numberPicker.setMaxValue(5);
                                                numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                                    @Override
                                                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                                        numberHolders = String.valueOf(newVal);
                                                    }
                                                });
                                                numberBuilder.setView(view);
                                                AlertDialog finalAlert = numberBuilder.create();
                                                finalAlert.show();
                                                finalAlert.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dropshadow));
                                                done_copy.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        ((LinearLayout) saveView.getParent()).removeView(saveView);
                                                        saveVaccine.remove(saveView);
                                                        save_textOut.setText(edit.getText().toString());
                                                        if(numberHolders == ""|| numberHolders == null || numberHolders == "0") {
                                                            numberHolders = "1";
                                                        }
                                                        save_count.setText(numberHolders);
                                                        added_vaccine.addView(saveView);
                                                        counter = 1;
                                                        finalAlert.dismiss();
                                                    }
                                                });
                                                cancel_copy.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        finalAlert.dismiss();
                                                    }
                                                });
                                                numberHolders="";
                                            }
                                            if(counter==0){
                                                Toast.makeText(pet_add_for_dating.this, "Cannot make any changes", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                alert.dismiss();
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
                            });
                            Button removeButton = saveView.findViewById(R.id.vaccine_save_remove);
                            removeButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ((LinearLayout) saveView.getParent()).removeView(saveView);
                                    count-=1;
                                    if(count==0){
                                        text3.setVisibility(View.GONE);
                                        text4.setVisibility(View.GONE);
                                    }
                                    else{
                                        Toast.makeText(pet_add_for_dating.this, "Cannot make any changes", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            if(count!=0){
                                text3.setVisibility(View.VISIBLE);
                                text4.setVisibility(View.VISIBLE);
                            }
                            textIn.getText().clear();

                            added_vaccine.addView(saveView);



                            finalAlert.dismiss();
                            numberHolder="";

                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finalAlert.dismiss();
                        }
                    });
                }
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void showCategoryDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //ListView
        BreedClass breed = new BreedClass();

        View pop = View.inflate(pet_add_for_dating.this,R.layout.pet_add_search_dialog,null);
        addCustom = pop.findViewById(R.id.search_customBreedID);
        editText = pop.findViewById(R.id.search_searchEditID);
        listView = pop.findViewById(R.id.search_breedListView);
        MaterialButton cancel;
        cancel = pop.findViewById(R.id.search_dialog_btn_cancel);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,breed.breed);
        listView.setAdapter(arrayAdapter);
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
                petBreedTextView.setText(holder);
                Toast.makeText(pet_add_for_dating.this, holder, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        addCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(pet_add_for_dating.this);
                View custom = View.inflate(pet_add_for_dating.this,R.layout.pet_add_custom_breed_dialog,null);
                EditText customEdit;
                MaterialButton cancel,done;
                customEdit = custom.findViewById(R.id.custom_breed_dialog_textin);
                cancel = custom.findViewById(R.id.custom_breed_dialog_btn_cancel);
                done = custom.findViewById(R.id.custom_breed_dialog_btn_done);

                builder.setView(custom);
                AlertDialog dialogAddCustom = builder.create();
                dialogAddCustom.show();
                dialogAddCustom.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(customEdit.getText().toString() == null || customEdit.getText().toString().equals("")){
                            counter = 0;
                        }
                        else{
                            petBreedTextView.setText(customEdit.getText().toString());
                            counter=1;
                        }
                        if (counter!=0){
                            Toast.makeText(pet_add_for_dating.this, customEdit.getText().toString(), Toast.LENGTH_SHORT).show();
                            dialogAddCustom.dismiss();
                        }
                        else{
                            Toast.makeText(pet_add_for_dating.this, "Cannot make any changes", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogAddCustom.dismiss();
                    }
                });

            }
        });
    }
    @SuppressLint({"UseCompatLoadingForDrawables", "ResourceType"})
    String genderHolder="";
    private void showGenderDialog() {
        String[] gender = {"Male","Female"};
        AlertDialog.Builder builder =new AlertDialog.Builder(pet_add_for_dating.this);
        View view = View.inflate(pet_add_for_dating.this,R.layout.pet_add_gender_dialog,null);
        MaterialButton done,cancel;
        done = view.findViewById(R.id.gender_dialog_btn_done);
        cancel = view.findViewById(R.id.gender_dialog_btn_cancel);

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
                genderTextView.setText(gender[Integer.parseInt(genderHolder)]);
                Toast.makeText(pet_add_for_dating.this, gender[Integer.parseInt(genderHolder)], Toast.LENGTH_SHORT).show();
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
                        pet_images_view.setVisibility(View.VISIBLE);
                        uri.add(imageUri);
                    }else{
                        Toast.makeText(this,"Not allowed to pick more than 5 images",Toast.LENGTH_SHORT).show();
                    }
                }
                //then notify the adapter
                pet_adapter.notifyDataSetChanged();
                Toast.makeText(this,uri.size()+": selected",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this,uri.size()+": selected",Toast.LENGTH_SHORT).show();
        }
        else{
            //this code is for if user not picked any image

            Toast.makeText(this,"You Haven't Pick any image",Toast.LENGTH_SHORT).show();
        }
    }



    private int upload = 0;
    int counterPhotos=0;

    private void uploadToFirebaseStorage(String countIDD,AlertDialog dialog) {
        String userID="";
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            userID = firebaseUser.getUid();
        }
        ArrayList<String> petPhotos = new ArrayList<>();
        StorageReference ImageFolder = FirebaseStorage.getInstance().getReference("User/"+userID+"/"+"Pet Images/"+"Pet"+countIDD);
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
    private static int load = 0;
    private void StringLink(String id, AlertDialog alertDialog,ArrayList<String>petPhotos) {
        ArrayList<String> hashMap = new ArrayList<String>();
        hashMap.addAll(petPhotos);

        PhotoClass photos = new PhotoClass(hashMap);
        FirebaseFirestore.getInstance()
                .collection("Pet")
                .document(id)
                .set(photos, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            FirebaseFirestore.getInstance()
                                    .collection("Shop")
                                    .document(firebaseUser.getUid())
                                    .collection("Pet")
                                    .document(id)
                                    .set(photos,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                FirebaseFirestore.getInstance()
                                                        .collection("User")
                                                        .document(firebaseUser.getUid())
                                                        .collection("Pet")
                                                        .document(id)
                                                        .set(photos,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    alertDialog.dismiss();

                                                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(pet_add_for_dating.this);
                                                                    builder2.setCancelable(false);
                                                                    View view = View.inflate(pet_add_for_dating.this,R.layout.screen_custom_alert,null);
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



    private void transistion(){
        Thread mSplashThread = new Thread() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(pet_add_for_dating.this, user_dashboard.class);
                pet_add_for_dating.this.overridePendingTransition(R.drawable.fade_in, R.drawable.fade_out);
                startActivity(intent);
                finish();
            }
        };
        mSplashThread.start();
    }


    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    private void uploadToFirebaseDatabase() {
        String petName = Objects.requireNonNull(petNameEdit.getText()).toString();
        String description = Objects.requireNonNull(petDescEdit.getText()).toString();
        String colorMarkings = Objects.requireNonNull(petColorEdit.getText()).toString();
        String breedCategory = petBreedTextView.getText().toString();
        String genderPet = genderTextView.getText().toString();
        String birthdayPet = petBirthdayTextView.getText().toString();
        String petSize = petSizeTextView.getText().toString();
        String petKilo = petKiloTextView.getText().toString();

        if(petName.isEmpty()){
            Toast.makeText(this, "Pet name should not be empty", Toast.LENGTH_SHORT).show();
            return;
        }else if (petName.length() < 2){
            petNameEdit.requestFocus();
            Toast.makeText(this, "Pet should have name at least 2 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if(description.isEmpty()){
            Toast.makeText(this, "Description should not be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (description.length() < 20){
            petNameEdit.requestFocus();
            Toast.makeText(this, "Description must have 20 characters and above", Toast.LENGTH_SHORT).show();
            return;
        }

        if(colorMarkings.isEmpty()){
            Toast.makeText(this, "Color/Markings should not be empty", Toast.LENGTH_SHORT).show();
            return;
        }else if(colorMarkings.length() < 3){
            Toast.makeText(this, "Color/Markings must have 3 characters and above", Toast.LENGTH_SHORT).show();
            return;
        }

        if(breedCategory.equals("Breed")){
            Toast.makeText(this, "Please choose the specific breed of your pet", Toast.LENGTH_SHORT).show();
            return;
        }


        if(added_vaccine ==null || added_vaccine.getChildCount() ==0){
            Toast.makeText(this, "Add vaccine or medicine at least 1", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            View row;
            ArrayList<String> list = new ArrayList<>();
            int countView = added_vaccine.getChildCount();
            for(int i = 0; i< countView; i++){
                row =added_vaccine.getChildAt(i);
                TextView edit = row.findViewById(R.id.vaccine_save_textout);
                TextView count = row.findViewById(R.id.vaccine_save_count);
                String text = edit.getText().toString();
                String countNo = count.getText().toString();
                saveVaccine.addAll(Arrays.asList(text+":"+countNo));
            }

/*            //for printing

                for(int i = 0; i<saveVaccine.size();i++) {
                    Toast.makeText(this,saveVaccine.get(0), Toast.LENGTH_SHORT).show();

        }*/
        }
        if(genderPet.equals("Gender")){
            Toast.makeText(this, "Please choose the specific gender of your pet", Toast.LENGTH_SHORT).show();
            return;
        }

        if(birthdayPet.equals("Birthday")){
            Toast.makeText(this, "Please set the birthday of your pet", Toast.LENGTH_SHORT).show();
            return;
        }

        if(uri.size() == 0){
            Toast.makeText(this, "Please provide a picture of your pet", Toast.LENGTH_SHORT).show();
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
        imageViewCompat.setImageDrawable(getDrawable(R.drawable.dialog_pet_borders));
        //message
        TextView message = view.findViewById(R.id.screen_custom_alert_message);
        title.setText("Creating your pets profile");
        message.setVisibility(View.VISIBLE);
        message.setText("Please wait a bit. We are creating your pets info");
        builder2.setView(view);
        AlertDialog alert2 = builder2.create();
        alert2.show();
        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        FirebaseFirestore.getInstance().collection("User").document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                         if(task.isSuccessful()){
                       DocumentSnapshot snapshot = task.getResult();
                             UserClass user = snapshot.toObject(UserClass.class);
                            if(user!=null){

                               PetDateClass petClass = new PetDateClass("",petName,description,colorMarkings,breedCategory,petSize,petKilo,null ,saveVaccine,genderPet,birthdayPet,user.getAddress(),firebaseUser.getUid(),"forDating", Timestamp.now(),true);

                                       FirebaseFirestore.getInstance().collection("Pet").add(petClass)
                                               .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                   @Override
                                                   public void onSuccess(DocumentReference documentReference) {
                                                       PetDateClass petClass = new PetDateClass(documentReference.getId(),petName,description,colorMarkings,breedCategory,petSize,petKilo,null ,saveVaccine,genderPet,birthdayPet,user.getAddress(),firebaseUser.getUid(),"forDating", Timestamp.now(),true);
                                                       FirebaseFirestore.getInstance().collection("User")
                                                               .document(firebaseUser.getUid())
                                                               .collection("Pet")
                                                               .document(documentReference.getId())
                                                               .set(petClass)
                                                               .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                   @Override
                                                                   public void onComplete(@NonNull Task<Void> task) {
                                                                        FirebaseFirestore.getInstance().collection("Pet")
                                                                                .document(documentReference.getId())
                                                                                .update("timestamp", FieldValue.serverTimestamp(),"id",documentReference.getId())
                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void unused) {
                                                                                        FirebaseFirestore.getInstance().collection("User")
                                                                                                .document(firebaseUser.getUid())
                                                                                                .collection("Pet")
                                                                                                .document(documentReference.getId()).update("timestamp",FieldValue.serverTimestamp()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onSuccess(Void unused) {
                                                                                                        uploadToFirebaseStorage(documentReference.getId(),alert2);
                                                                                                    }
                                                                                                });
                                                                                    }
                                                                                });
                                                                   }
                                                               });

                                                   }
                                               });
                            }
                         }
            }
        });

    }

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
            petColorCount.setText(s.length() +"/200");
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
    private final TextWatcher addVaccineTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length()!=0){
                imageClear.setVisibility(View.VISIBLE);
                imageClear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textIn.getText().clear();
                    }
                });
            }
            else{
                imageClear.setVisibility(View.GONE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


}