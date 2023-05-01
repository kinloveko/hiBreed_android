package com.example.hi_breed.service;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.hi_breed.R;
import com.example.hi_breed.checkout.checkout_thankyou;
import com.example.hi_breed.classesFile.ApiUtilities;
import com.example.hi_breed.classesFile.BaseActivity;
import com.example.hi_breed.classesFile.matches_class;
import com.example.hi_breed.classesFile.notificationData;
import com.example.hi_breed.classesFile.pushNotification;
import com.example.hi_breed.classesFile.service_class;
import com.example.hi_breed.message.message_conversation_activity;
import com.example.hi_breed.userFile.profile.current_address;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class service_set_appointment extends BaseActivity {


    TextView first_user,
            first_user_checkout_number,
            first_address,
            first_checkout_zip;

    TextView second_user,
            second_user_checkout_number,
            second_address,
            second_checkout_zip;

    TextView check_out_name,
            checkout_number,
            checkout_address,
            checkout_zip;

    TextView dateTextView, pet_text,
            itemSlot;
    Button saveButton;
    String userToken;
    RelativeLayout toolbarID, currentAddress, timeLayout, dateLayout, petDatingInfo;
    String matchID;
    List<String> participants = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_appointment);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.setStatusBarColor(Color.parseColor("#e28743"));
        }
        // find the picker
        petDatingInfo = findViewById(R.id.petDatingInfo);
        first_user = findViewById(R.id.first_user);
        first_user_checkout_number = findViewById(R.id.first_user_checkout_number);
        first_address = findViewById(R.id.first_address);
        first_checkout_zip = findViewById(R.id.first_checkout_zip);

        second_user = findViewById(R.id.second_user);
        second_user_checkout_number = findViewById(R.id.second_user_checkout_number);
        second_address = findViewById(R.id.second_address);
        second_checkout_zip = findViewById(R.id.second_checkout_zip);

        pet_text = findViewById(R.id.pet_text);
        currentAddress = findViewById(R.id.currentAddress);
        saveButton = findViewById(R.id.saveButton);
        timeLayout = findViewById(R.id.timeLayout);
        dateLayout = findViewById(R.id.dateLayout);
        dateTextView = findViewById(R.id.dateTextView);
        itemSlot = findViewById(R.id.itemSlot);
        check_out_name = findViewById(R.id.check_out_name);
        checkout_number = findViewById(R.id.checkout_number);
        checkout_address = findViewById(R.id.checkout_address);
        checkout_zip = findViewById(R.id.checkout_zip);

        toolbarID = findViewById(R.id.toolbarID);
        toolbarID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service_set_appointment.this.onBackPressed();
                finish();
            }
        });

        Intent intent = getIntent();
        service_class service = (service_class) intent.getSerializableExtra("model");

        if (intent.getStringExtra("matchID") != null)
            matchID = intent.getStringExtra("matchID");
        if (matchID != null) {
            //clients info for pet dating
            currentAddress.setVisibility(View.GONE);
            petDatingInfo.setVisibility(View.VISIBLE);

            FirebaseFirestore.getInstance().collection("Matches")
                    .document(matchID)
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {

                                participants = (List<String>) documentSnapshot.get("participants");
                                //client personal info
                                if (participants != null) {
                                    for (int i = 0; i < participants.size(); i++) {
                                        int finalI = i;
                                        //for info
                                        FirebaseFirestore.getInstance().collection("User")
                                                .document(participants.get(i)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot s) {
                                                        if (s.exists()) {
                                                            if (finalI == 0) {

                                                                first_user.setText(s.getString("firstName") + " " + s.getString("middleName") + " " + s.getString("lastName"));
                                                                first_address.setText(s.getString("address"));
                                                                first_checkout_zip.setText(s.getString("zipCode"));
                                                            } else {

                                                                second_user.setText(s.getString("firstName") + " " + s.getString("middleName") + " " + s.getString("lastName"));
                                                                second_address.setText(s.getString("address"));
                                                                second_checkout_zip.setText(s.getString("zipCode"));
                                                            }
                                                        }
                                                    }
                                                });
                                        //for contact number
                                        FirebaseFirestore.getInstance().collection("User")
                                                .document(participants.get(i)).collection("security")
                                                .document("security_doc").get()
                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot s) {
                                                        if (s.exists()) {
                                                            if (finalI == 0) {
                                                                first_user_checkout_number.setText(s.getString("contactNumber"));
                                                            } else {
                                                                second_user_checkout_number.setText(s.getString("contactNumber"));
                                                            }
                                                        }
                                                    }
                                                });
                                    }
                                }

                            }
                        }
                    });
            //shooter fcm
            FirebaseFirestore.getInstance().collection("User")
                    .document(service.getShooter_id()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            userToken = documentSnapshot.getString("fcmToken");
                        }
                    });


            // Step 1: Retrieve the value of the availability field from Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("Services").document(service.getId());
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {

                        List<String> availability = (List<String>) documentSnapshot.get("availability");
                        // Step 1: Get the available days from Firestore
                        List<String> availableDays = (List<String>) documentSnapshot.get("schedule");

                        // Get the selected date and time from the numberPickers
                        Calendar selectedDateTime = Calendar.getInstance();

                        // check if the time service is not available
                        Calendar currentDateTime = Calendar.getInstance();
                        String startTimeString = availability.get(0);
                        String endTimeString = availability.get(1);
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("h:mm a");

                        // Use "h:m a" pattern instead for minutes less than 10
                        if (startTimeString.contains(":0")) {
                            format = new SimpleDateFormat("h:m a");
                        }

                        Date endDate = null;
                        try {
                            endDate = format.parse(endTimeString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Calendar endCalendar = Calendar.getInstance();
                        endCalendar.setTime(endDate);
                        int endHour = endCalendar.get(Calendar.HOUR_OF_DAY);

                        selectedDateTime.set(Calendar.HOUR_OF_DAY, endHour); // replace with the hour selected by the user

                        dateLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                getDate(availableDays);
                            }
                        });
                        timeLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (dateTextView.getText().toString().equals("Date")) {
                                    Toast.makeText(service_set_appointment.this, "Select a date first", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                selectedDateTime.set(Calendar.YEAR, datePicker.getYear()); // replace with the year selected by the user
                                selectedDateTime.set(Calendar.MONTH, datePicker.getMonth()); // replace with the month selected by the user
                                selectedDateTime.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth()); // replace with the day selected by the user

                                if (selectedDateTime.getTime().before(currentDateTime.getTime())) {
                                    Toast.makeText(service_set_appointment.this, "This service is close at this time", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                getTime(availability);
                            }
                        });
                    }
                }
            });
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkPetDateInput(service, participants,matchID);
                }
            });
        } else {
            //one client
            petDatingInfo.setVisibility(View.GONE);
            currentAddress.setVisibility(View.VISIBLE);
            currentAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(service_set_appointment.this, current_address.class);
                    i.putExtra("address", checkout_address.getText().toString());
                    i.putExtra("zip", checkout_zip.getText().toString());
                    i.putExtra("phone", checkout_number.getText().toString());
                    startActivity(i);

                }
            });

            FirebaseFirestore.getInstance().collection("User")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(DocumentSnapshot s) {

                            check_out_name.setText(s.getString("firstName") + " " + s.getString("middleName") + " " + s.getString("lastName"));
                            checkout_address.setText(s.getString("address"));
                            checkout_zip.setText(s.getString("zipCode"));

                        }
                    });

            FirebaseFirestore.getInstance().collection("User")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("security")
                    .document("security_doc")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot s = task.getResult();
                                checkout_number.setText(s.getString("contactNumber"));
                            }
                        }
                    });

            FirebaseFirestore.getInstance().collection("User")
                    .document(service.getShooter_id()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            userToken = documentSnapshot.getString("fcmToken");
                        }
                    });

            // Step 1: Retrieve the value of the availability field from Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("Services").document(service.getId());
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {

                        List<String> availability = (List<String>) documentSnapshot.get("availability");
                        // Step 1: Get the available days from Firestore
                        List<String> availableDays = (List<String>) documentSnapshot.get("schedule");

                        // Get the selected date and time from the numberPickers
                        Calendar selectedDateTime = Calendar.getInstance();

                        // check if the time service is not available
                        Calendar currentDateTime = Calendar.getInstance();
                        String startTimeString = availability.get(0);
                        String endTimeString = availability.get(1);
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("h:mm a");

                        // Use "h:m a" pattern instead for minutes less than 10
                        if (startTimeString.contains(":0")) {
                            format = new SimpleDateFormat("h:m a");
                        }

                        Date endDate = null;
                        try {
                            endDate = format.parse(endTimeString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Calendar endCalendar = Calendar.getInstance();
                        endCalendar.setTime(endDate);
                        int endHour = endCalendar.get(Calendar.HOUR_OF_DAY);

                        selectedDateTime.set(Calendar.HOUR_OF_DAY, endHour); // replace with the hour selected by the user

                        dateLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                getDate(availableDays);
                            }
                        });
                        timeLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (dateTextView.getText().toString().equals("Date")) {
                                    Toast.makeText(service_set_appointment.this, "Select a date first", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                selectedDateTime.set(Calendar.YEAR, datePicker.getYear()); // replace with the year selected by the user
                                selectedDateTime.set(Calendar.MONTH, datePicker.getMonth()); // replace with the month selected by the user
                                selectedDateTime.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth()); // replace with the day selected by the user

                                if (selectedDateTime.getTime().before(currentDateTime.getTime())) {
                                    Toast.makeText(service_set_appointment.this, "This service is close at this time", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                getTime(availability);
                            }
                        });
                    }
                }
            });
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    checkInput(service);

                }
            });

        }
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    private void checkInput(service_class service) {

        String date = dateTextView.getText().toString();
        String time = itemSlot.getText().toString();

        if (date.equals("Date")) {
            Toast.makeText(this, "Please set a date", Toast.LENGTH_SHORT).show();
            return;
        }
        if (time.equals("Time")) {
            Toast.makeText(this, "Please set a time", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setCancelable(true);
        View view = View.inflate(this, R.layout.screen_custom_alert, null);
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
        title.setText("Sending your request");
        message.setVisibility(View.VISIBLE);
        message.setText("Please wait a bit. We are checking your appointments info");
        builder2.setView(view);
        AlertDialog alert2 = builder2.create();
        alert2.show();
        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Map<String, Object> trans = new HashMap<>();

        trans.put("id", "");
        trans.put("trans_date_created", Timestamp.now());
        trans.put("trans_payment", "on-site");
        trans.put("total_amount", service.getService_fee());
        trans.put("trans_type", "Service");
        trans.put("status", "pending");
        trans.put("customer_id", FirebaseAuth.getInstance().getCurrentUser().getUid());


        FirebaseFirestore.getInstance().collection("Transaction")
                .add(trans)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        FirebaseFirestore.getInstance().collection("Transaction")
                                .document(documentReference.getId())
                                .update("id", documentReference.getId(),
                                        "trans_date_created", FieldValue.serverTimestamp())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Map<String, Object> map = new HashMap<>();
                                        map.put("id", "");
                                        map.put("customer_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        map.put("seller_id", service.getShooter_id()); // id of the one who offers the service
                                        map.put("transaction_id", documentReference.getId()); //transaction id FK
                                        map.put("appointment_date", date); // date selected
                                        map.put("timestamp", Timestamp.now()); //date created
                                        map.put("appointment_time", time); // time selected
                                        map.put("type", service.getServiceType());
                                        map.put("service_price", service.getService_fee()); // Service fee
                                        map.put("service_id", service.getId()); //Service ID FK
                                        map.put("appointment_status", "pending");

                                        FirebaseFirestore.getInstance().collection("Appointments")
                                                .add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference s) {

                                                        FirebaseFirestore.getInstance().collection("Appointments")
                                                                .document(s.getId())
                                                                .update("id", s.getId(), "timestamp", FieldValue.serverTimestamp()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        FirebaseFirestore.getInstance().collection("Transaction")
                                                                                .document(documentReference.getId())
                                                                                .update("timestamp", FieldValue.serverTimestamp())
                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void unused) {

                                                                                        Map<String, Object> ids = new HashMap<>();
                                                                                        ids.put("service_id", service.getId());
                                                                                        ids.put("appointment_id", s.getId());

                                                                                        FirebaseFirestore.getInstance().collection("Transaction")
                                                                                                .document(documentReference.getId())
                                                                                                .set(ids, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onSuccess(Void unused) {

                                                                                                        Map<String, Object> map = new HashMap<>();

                                                                                                        map.put("id", s.getId());
                                                                                                        map.put("send_to_id", service.getShooter_id());
                                                                                                        map.put("sender", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                                                                        map.put("title", "Request Appointment");
                                                                                                        map.put("message", "Appointment request from:" + check_out_name.getText().toString());
                                                                                                        map.put("timestamp", Timestamp.now());
                                                                                                        map.put("type", "appointment");


                                                                                                        Map<String, Object> maps = new HashMap<>();

                                                                                                        maps.put("latestNotification", map);
                                                                                                        maps.put("notification", Arrays.asList(map));


                                                                                                        FirebaseFirestore.getInstance().collection("Notifications").document(service.getShooter_id())
                                                                                                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                    @Override
                                                                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                                        if (task.isSuccessful()) {
                                                                                                                            DocumentSnapshot a = task.getResult();
                                                                                                                            if (a.exists()) {

                                                                                                                                FirebaseFirestore.getInstance().collection("Notifications")
                                                                                                                                        .document(service.getShooter_id())
                                                                                                                                        .update("latestNotification", map, "notification", FieldValue.arrayUnion(map)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                            @Override
                                                                                                                                            public void onSuccess(Void unused) {
                                                                                                                                                alert2.dismiss();

                                                                                                                                                pushNotification notification = new pushNotification(new notificationData("Appointment request from:" + check_out_name.getText().toString(), check_out_name.getText().toString()
                                                                                                                                                        , s.getId(), service.getShooter_id(), "appointment", "seller", "pending"), userToken);
                                                                                                                                                sendNotif(notification);

                                                                                                                                            }
                                                                                                                                        });
                                                                                                                            } else {
                                                                                                                                FirebaseFirestore.getInstance().collection("Notifications")
                                                                                                                                        .document(service.getShooter_id())
                                                                                                                                        .set(maps).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                            @Override
                                                                                                                                            public void onSuccess(Void unused) {
                                                                                                                                                alert2.dismiss();

                                                                                                                                                pushNotification notification = new pushNotification(new notificationData("Appointment request from:" + check_out_name.getText().toString(), check_out_name.getText().toString()
                                                                                                                                                        , s.getId(), service.getShooter_id(), "appointment", "seller", "pending"), userToken);
                                                                                                                                                sendNotif(notification);
                                                                                                                                            }
                                                                                                                                        });
                                                                                                                            }
                                                                                                                        }
                                                                                                                    }
                                                                                                                });


                                                                                                    }
                                                                                                });


                                                                                    }
                                                                                });
                                                                    }
                                                                });
                                                    }
                                                });

                                    }
                                });

                    }
                });

    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    private void checkPetDateInput(service_class service, List<String> participants,String matchID) {
        String date = dateTextView.getText().toString();
        String time = itemSlot.getText().toString();

        if (date.equals("Date")) {
            Toast.makeText(this, "Please set a date", Toast.LENGTH_SHORT).show();
            return;
        }
        if (time.equals("Time")) {
            Toast.makeText(this, "Please set a time", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setCancelable(true);
        View view = View.inflate(this, R.layout.screen_custom_alert, null);
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
        title.setText("Sending your request");
        message.setVisibility(View.VISIBLE);
        message.setText("Please wait a bit. We are checking your appointments info");
        builder2.setView(view);
        AlertDialog alert2 = builder2.create();
        alert2.show();
        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //create a new fields for transaction
        Map<String, Object> trans = new HashMap<>();

        trans.put("id", "");
        trans.put("trans_date_created", Timestamp.now());
        trans.put("trans_payment", "on-site");
        trans.put("total_amount", service.getService_fee());
        trans.put("trans_type", "Service");
        trans.put("trans_for", "PetDating");
        trans.put("status", "pending");
        trans.put("customer_id", participants);

        FirebaseFirestore.getInstance().collection("Transaction")
                .add(trans)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        //update transaction set the auto-generated ID to id field and update the trans_date to serverTimestamp
                        FirebaseFirestore.getInstance().collection("Transaction")
                                .document(documentReference.getId())
                                .update("id", documentReference.getId(),
                                        "trans_date_created", FieldValue.serverTimestamp())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Log.d("MatchID","Updated TransactionID");
                                        //create a new fields for Appointments
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("id", "");
                                        map.put("customer_id", participants);
                                        map.put("seller_id", service.getShooter_id()); // id of the one who offers the service
                                        map.put("transaction_id", documentReference.getId()); //transaction id FK
                                        map.put("appointment_date", date); // date selected
                                        map.put("timestamp", Timestamp.now()); //date created
                                        map.put("appointment_time", time); // time selected
                                        map.put("type", service.getServiceType());
                                        map.put("pet_dating_match_id",matchID);
                                        map.put("appointment_for", "PetDating");
                                        map.put("service_price", service.getService_fee()); // Service fee
                                        map.put("service_id", service.getId());
                                        map.put("appointment_status", "pending");

                                        FirebaseFirestore.getInstance().collection("Appointments")
                                                .add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference s) {
                                                        //update Appointments set the auto-generated ID to id field and update the timestamp field to serverTimestamp

                                                        FirebaseFirestore.getInstance().collection("Appointments")
                                                                .document(s.getId())
                                                                .update("id", s.getId(), "timestamp", FieldValue.serverTimestamp()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        Log.d("MatchID","Updated appointment id:"+matchID);
                                                                        FirebaseFirestore.getInstance().collection("Transaction")
                                                                                .document(documentReference.getId())
                                                                                .update("timestamp", FieldValue.serverTimestamp())
                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void unused) {
                                                                                        Log.d("MatchID","Updated transaction timestamp id:"+matchID);
                                                                                        Map<String, Object> ids = new HashMap<>();
                                                                                        ids.put("service_id", service.getId());
                                                                                        ids.put("appointment_id", s.getId());

                                                                                        FirebaseFirestore.getInstance().collection("Transaction")
                                                                                                .document(documentReference.getId())
                                                                                                .set(ids, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onSuccess(Void unused) {
                                                                                                        Log.d("MatchID","Updated TransactionID");
                                                                                                        Map<String, Object> map = new HashMap<>();

                                                                                                        map.put("id", s.getId());
                                                                                                        map.put("send_to_id", service.getShooter_id());
                                                                                                        map.put("sender", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                                                                        map.put("title", "Pet Dating Appointment Request");
                                                                                                        map.put("message", "Appointment request from:" + first_user.getText().toString() + " & " + second_user.getText().toString());
                                                                                                        map.put("timestamp", Timestamp.now());
                                                                                                        map.put("type", "appointment");


                                                                                                        Map<String, Object> maps = new HashMap<>();

                                                                                                        maps.put("latestNotification", map);
                                                                                                        maps.put("notification", Arrays.asList(map));

                                                                                                        FirebaseFirestore.getInstance().collection("Matches")
                                                                                                                .document(matchID).update("appointment_id", s.getId(), "status", "pending")
                                                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                    @Override
                                                                                                                    public void onSuccess(Void unused) {
                                                                                                                        Log.d("MatchID","added new field in this specific matchID:"+matchID);
                                                                                                                        FirebaseFirestore.getInstance().collection("Notifications").document(service.getShooter_id())
                                                                                                                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                                    @Override
                                                                                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                                                        if (task.isSuccessful()) {
                                                                                                                                            DocumentSnapshot a = task.getResult();
                                                                                                                                            if (a.exists()) {

                                                                                                                                                FirebaseFirestore.getInstance().collection("Notifications")
                                                                                                                                                        .document(service.getShooter_id())
                                                                                                                                                        .update("latestNotification", map, "notification", FieldValue.arrayUnion(map)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                                            @Override
                                                                                                                                                            public void onSuccess(Void unused) {
                                                                                                                                                                alert2.dismiss();

                                                                                                                                                                pushNotification notification = new pushNotification(new notificationData("Appointment request from:" + first_user.getText().toString() + " & " + second_user.getText().toString(), "Pet Dating"
                                                                                                                                                                        , s.getId(), service.getShooter_id(), "appointment", "seller", "pending"), userToken);
                                                                                                                                                                sendNotif(notification);

                                                                                                                                                            }
                                                                                                                                                        });
                                                                                                                                            } else {
                                                                                                                                                FirebaseFirestore.getInstance().collection("Notifications")
                                                                                                                                                        .document(service.getShooter_id())
                                                                                                                                                        .set(maps).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                                            @Override
                                                                                                                                                            public void onSuccess(Void unused) {
                                                                                                                                                                alert2.dismiss();

                                                                                                                                                                pushNotification notification = new pushNotification(new notificationData("Appointment request from:" + first_user.getText().toString() + " & " + second_user.getText().toString(), "Pet Dating"
                                                                                                                                                                        , s.getId(), service.getShooter_id(), "appointment", "seller", "pending"), userToken);
                                                                                                                                                                sendNotif(notification);
                                                                                                                                                            }
                                                                                                                                                        });
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                });
                                                                                                                    }
                                                                                                                });
                                                                                                    }
                                                                                                });
                                                                                    }
                                                                                });
                                                                    }
                                                                });
                                                    }
                                                });

                                    }
                                });

                    }
                });

    }

    private void sendNotif(pushNotification notification) {
        ApiUtilities.getClient().sendNotification(notification).enqueue(new Callback<pushNotification>() {
            @Override
            public void onResponse(Call<pushNotification> call, Response<pushNotification> response) {
                if (response.isSuccessful()) {
                    if (matchID != null) {
                        FirebaseFirestore.getInstance().collection("Matches")
                                .document(matchID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {
                                            matches_class matches_class = documentSnapshot.toObject(matches_class.class);
                                            Intent i = new Intent(service_set_appointment.this, message_conversation_activity.class);
                                            i.putExtra("model", (Serializable) matches_class);
                                            startActivity(i);
                                            finish();
                                        }
                                    }
                                });
                    } else {
                        Intent i = new Intent(service_set_appointment.this, checkout_thankyou.class);
                        i.putExtra("from", "Service");
                        startActivity(i);
                        finish();
                    }
                } else {
                    if (matchID != null) {
                        FirebaseFirestore.getInstance().collection("Matches")
                                .document(matchID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {
                                            matches_class matches_class = documentSnapshot.toObject(matches_class.class);
                                            Intent i = new Intent(service_set_appointment.this, message_conversation_activity.class);
                                            i.putExtra("model", (Serializable) matches_class);
                                            startActivity(i);
                                            finish();
                                        }
                                    }
                                });
                    } else {
                        Intent i = new Intent(service_set_appointment.this, checkout_thankyou.class);
                        i.putExtra("from", "Service");
                        startActivity(i);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<pushNotification> call, Throwable t) {
                Toast.makeText(service_set_appointment.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    NumberPicker picker, pm_am_numberPicker, minutes_numberPicker;
    String pmAMHolder, minutesHolder, hoursHolder;

    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    private void getTime(List<String> availability) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.m_service_time_dialog, null);
        MaterialButton done, cancel;
        TextView service_title_dialog_title, service_message, from;

        from = view.findViewById(R.id.from);
        from.setVisibility(View.GONE);
        service_title_dialog_title = view.findViewById(R.id.service_title_dialog_title);
        service_title_dialog_title.setText("Set time appointment");
        service_title_dialog_title.setTextSize(15);
        service_message = view.findViewById(R.id.service_message);
        service_message.setText("Please provide your available time.");

        done = view.findViewById(R.id.gender_dialog_btn_done);
        done.setText("Proceed");
        cancel = view.findViewById(R.id.gender_dialog_btn_cancel);
        //pm am
        pm_am_numberPicker = view.findViewById(R.id.pm_am_numberPicker);
        String[] pmAM = {"AM", "PM"};
        pm_am_numberPicker.setMinValue(0);
        pm_am_numberPicker.setMaxValue(pmAM.length - 1);
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
        minutes_numberPicker = view.findViewById(R.id.minutes_numberPicker);
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
        String startTimeString = availability.get(0);
        String endTimeString = availability.get(1);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("h:mm a");

        // Use "h:m a" pattern instead for minutes less than 10
        if (startTimeString.contains(":0")) {
            format = new SimpleDateFormat("h:m a");
        }

        Date startDate = null;
        Date endDate = null;
        try {
            startDate = format.parse(startTimeString);
            endDate = format.parse(endTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        int startHour = startCalendar.get(Calendar.HOUR_OF_DAY);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        int endHour = endCalendar.get(Calendar.HOUR_OF_DAY);

        // Step 3: Set up the NumberPicker for hours
        //hours
        picker = view.findViewById(R.id.hours_numberPicker);
        picker.setMinValue(startHour);
        picker.setMaxValue(endHour);
        picker.setValue(startHour);
        picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        picker.setWrapSelectorWheel(false);
        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                boolean isAfternoon = newVal >= 12;
                picker.setDisplayedValues(null); // clear the previous values
                // Update the AM/PM marker
                if (isAfternoon) {
                    pm_am_numberPicker.setValue(1); // 1 = PM
                    pmAMHolder = "PM";
                } else {
                    pm_am_numberPicker.setValue(0); // 0 = AM
                    pmAMHolder = "AM";
                }
                hoursHolder = String.valueOf(newVal);
            }
        });

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        done.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (hoursHolder == "" || hoursHolder == null) {
                    hoursHolder = String.valueOf(startHour);
                }
                if (pmAMHolder == "" || pmAMHolder == null) {
                    pmAMHolder = "AM";
                } else {
                    pmAMHolder = "PM";
                }
                if (minutesHolder == "" || minutesHolder == null) {
                    minutesHolder = "0";
                }
                int hour = Integer.parseInt(hoursHolder);
                int minute = Integer.parseInt(minutesHolder);


                // Convert 24-hour format to 12-hour format

                String pmAMHolders = "AM";
                if (hour >= 12) {
                    pmAMHolders = "PM";
                    hour %= 12;
                }
                if (hour == 0) {
                    hour = 12;
                }
                LocalTime currentTime = LocalTime.now();
                LocalTime selectedTime = LocalTime.of(picker.getValue(), minutes_numberPicker.getValue());

                Calendar currentCalendar = Calendar.getInstance();
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.setTimeInMillis(datePicker.getCalendarView().getDate());

                if (selectedCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                        selectedCalendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH) &&
                        selectedCalendar.get(Calendar.DAY_OF_MONTH) == currentCalendar.get(Calendar.DAY_OF_MONTH)) {

                    // Selected date is the current date
                    if (selectedTime.isBefore(currentTime)) {
                        Toast.makeText(service_set_appointment.this, "Selected time is already passed", Toast.LENGTH_SHORT).show();
                    } else if (selectedTime.isAfter(currentTime.plusHours(2))) {
                        // Selected time is at least 2 hours ahead of current time
                        itemSlot.setText(convertDate(hour) + ":" + convertDate(minute) + " " + pmAMHolders);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(service_set_appointment.this, "Appointment must be at least 2 hours ahead", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Selected date is not the current date
                    itemSlot.setText(convertDate(hour) + ":" + convertDate(minute) + " " + pmAMHolders);
                    dialog.dismiss();
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

    public String convertDate(int input) {
        if (input >= 10) {
            return String.valueOf(input);
        } else {
            return "0" + String.valueOf(input);
        }
    }

    DatePicker datePicker;

    @SuppressLint({"DefaultLocale", "UseCompatLoadingForDrawables", "SetTextI18n"})
    private void getDate(List<String> schedule) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.product_add_expiration_dialog, null);
        datePicker = view.findViewById(R.id.datePickers);
        AppCompatImageView appCompatImageView = view.findViewById(R.id.appCompatImageView);
        appCompatImageView.setImageDrawable(getDrawable(R.drawable.dialog_calendar_borders));
        TextView from = view.findViewById(R.id.from);
        from.setVisibility(View.GONE);
        TextView service_title_dialog_title = view.findViewById(R.id.service_title_dialog_title);
        service_title_dialog_title.setText("Set date");
        TextView service_message = view.findViewById(R.id.service_message);
        service_message.setText("Note: Please select the date you want to set an appointment");
        Calendar calendars = Calendar.getInstance();
        long minDate = calendars.getTimeInMillis();
        calendars.set(9999, 11, 31);
        long maxDate = calendars.getTimeInMillis();

        datePicker.setMinDate(minDate);
        datePicker.setMaxDate(maxDate);

        builder.setView(view);
        MaterialButton cancel, save;

        cancel = view.findViewById(R.id.product_dialog_btn_cancel);
        save = view.findViewById(R.id.product_dialog_btn_done);

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);


                // Check if the day of the week is in the schedule array
                if (schedule.contains(getDayOfWeek(dayOfWeek))) {
                    // Display the selected date in the text view
                    SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
                    String formattedDate = sdf.format(calendar.getTime());
                    dateTextView.setText(formattedDate);
                    Toast.makeText(service_set_appointment.this, formattedDate, Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                } else {
                    // Display an error message
                    Toast.makeText(service_set_appointment.this, "Selected date is not available in the schedule", Toast.LENGTH_SHORT).show();
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

    private String getDayOfWeek(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return "Sunday";
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
            default:
                return "";
        }
    }

}