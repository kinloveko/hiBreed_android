package com.example.hi_breed;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hi_breed.classesFile.ApiUtilities;
import com.example.hi_breed.classesFile.appointment_class;
import com.example.hi_breed.classesFile.matches_class;
import com.example.hi_breed.classesFile.notificationData;
import com.example.hi_breed.classesFile.pushNotification;
import com.example.hi_breed.service_status_for_buyer.appointment_user_side;
import com.example.hi_breed.service_status_for_seller.service_status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class acquired_service_details extends AppCompatActivity {

    TextView check_out_name,
            checkout_number,
            checkout_address,
            checkout_zip;
    TextView dateTextView,pet_text,check_out_service_acquired,
            itemSlot;
    Button cancelButton,messageButton;
    RelativeLayout toolbarID,currentAddress,timeLayout,dateLayout;
    LinearLayout buttonLayout,buttonContact,buttonDelete;
    String notCurrentUser;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acquired_service_details);

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

        buttonContact = findViewById(R.id.buttonContact);
        buttonDelete = findViewById(R.id.buttonDelete);
        cancelButton=findViewById(R.id.cancelButton);
        toolbarID=findViewById(R.id.toolbarID);

        toolbarID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acquired_service_details.this.onBackPressed();
                finish();
            }
        });
        messageButton = findViewById(R.id.messageButton);

        check_out_service_acquired = findViewById(R.id.check_out_service_acquired);
        pet_text = findViewById(R.id.pet_text);
        currentAddress =findViewById(R.id.currentAddress);
        timeLayout = findViewById(R.id.timeLayout);
        dateLayout = findViewById(R.id.dateLayout);
        dateTextView = findViewById(R.id.dateTextView);
        itemSlot = findViewById(R.id.itemSlot);
        check_out_name = findViewById(R.id.check_out_name);
        checkout_number = findViewById(R.id.checkout_number);
        checkout_address = findViewById(R.id.checkout_address);
        checkout_zip = findViewById(R.id.checkout_zip);
        buttonLayout = findViewById(R.id.buttonLayout);


        Intent intent = getIntent();

        appointment_class appointment = (appointment_class) intent.getSerializableExtra("mode");

        if(appointment!=null) {

            if(!appointment.getCustomer_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                notCurrentUser = appointment.getCustomer_id();
            }
            else if (!appointment.getSeller_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                notCurrentUser = appointment.getSeller_id();
            }

            dateTextView.setText(appointment.getAppointment_date());
            itemSlot.setText(appointment.getAppointment_time());


            if(appointment.getAppointment_status().equals("pending")){

                if (appointment.getCustomer_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    cancelButton.setVisibility(View.VISIBLE);
                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseFirestore.getInstance().collection("User")
                                    .document(appointment.getSeller_id()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot value) {
                                            if(value.exists()){
                                                String token= value.getString("fcmToken");
                                                Map<String,Object> map = new HashMap<>();
                                                map.put("send_to_id", appointment.getSeller_id());
                                                map.put("message","Requested appointment has been cancelled");
                                                map.put("timestamp", Timestamp.now());
                                                map.put("type","appointment");

                                                Map<String,Object> maps = new HashMap<>();
                                                maps.put("id",appointment.getId());
                                                maps.put("latestNotification",map);
                                                maps.put("notification", Arrays.asList(map));

                                                FirebaseFirestore.getInstance().collection("Appointments")
                                                        .document(appointment.getId())
                                                        .update("appointment_status","cancelled")
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {

                                                                FirebaseFirestore.getInstance().collection("Notifications").document(appointment.getCustomer_id())
                                                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                                                if(documentSnapshot.exists()){
                                                                                    FirebaseFirestore.getInstance().collection("Notifications")
                                                                                            .document(appointment.getSeller_id())
                                                                                            .update("latestNotification",map,"notification", FieldValue.arrayUnion(map)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void unused) {
                                                                                                    pushNotification notification = new pushNotification(new notificationData("Requested appointment has been cancelled",
                                                                                                            "Cancelled appointment",appointment.getId(),appointment.getSeller_id(),"appointment","seller","cancelled"), token);
                                                                                                    sendNotif(notification,"cancelled","seller");
                                                                                                }
                                                                                            });
                                                                                }
                                                                                else{
                                                                                    FirebaseFirestore.getInstance().collection("Notifications")
                                                                                            .document(appointment.getSeller_id()).set(maps).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void unused) {
                                                                                                    pushNotification notification = new pushNotification(new notificationData("Requested appointment has been cancelled",
                                                                                                            "Cancelled appointment",appointment.getId(),appointment.getSeller_id(),"appointment","seller","cancelled"), token);
                                                                                                    sendNotif(notification,"cancelled","seller");
                                                                                                }
                                                                                            });
                                                                                }
                                                                            }
                                                                        });
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    });
                }
                else
                if (appointment.getSeller_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    buttonLayout.setVisibility(View.VISIBLE);
                    buttonContact.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseFirestore.getInstance().collection("User")
                                    .document(appointment.getCustomer_id()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot value) {
                                            if(value.exists()){
                                                String token= value.getString("fcmToken");
                                                Map<String,Object> map = new HashMap<>();
                                                map.put("send_to_id", appointment.getCustomer_id());
                                                map.put("message","Your request appointment has been accepted");
                                                map.put("timestamp", Timestamp.now());
                                                map.put("type","appointment");


                                                Map<String,Object> maps = new HashMap<>();
                                                maps.put("id",appointment.getId());
                                                maps.put("latestNotification",map);
                                                maps.put("notification", Arrays.asList(map));

                                                FirebaseFirestore.getInstance().collection("Appointments")
                                                        .document(appointment.getId())
                                                        .update("appointment_status","accepted")
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {

                                                                FirebaseFirestore.getInstance().collection("Notifications").document(appointment.getCustomer_id())
                                                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                                                if(documentSnapshot.exists()){

                                                                                    FirebaseFirestore.getInstance().collection("Notifications")
                                                                                            .document(appointment.getCustomer_id())
                                                                                            .update("latestNotification",map,"notification", FieldValue.arrayUnion(map)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void unused) {


                                                                                                    Map<String, Object> connection = new HashMap<>();
                                                                                                    connection.put("matchID", appointment.getId());
                                                                                                    connection.put("participants", Arrays.asList(appointment.getCustomer_id(), appointment.getSeller_id()));
                                                                                                    connection.put("time", Timestamp.now());
                                                                                                    connection.put("show", true);

                                                                                                    FirebaseFirestore.getInstance().collection("Matches").document(appointment.getId()).set(connection)
                                                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                @Override
                                                                                                                public void onSuccess(Void unused) {
                                                                                                                    FirebaseFirestore.getInstance().collection("Matches")
                                                                                                                            .document(appointment.getId())
                                                                                                                            .update("time",FieldValue.serverTimestamp()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                @Override
                                                                                                                                public void onSuccess(Void unused) {
                                                                                                                                    pushNotification notification = new pushNotification(new notificationData("Your request appointment has been accepted",
                                                                                                                                            "Accepted appointment",appointment.getId(),appointment.getCustomer_id(),"appointment","buyer","accepted"), token);
                                                                                                                                    sendNotif(notification,"accepted","buyer");
                                                                                                                                    Toast.makeText(acquired_service_details.this, "Message ID created", Toast.LENGTH_SHORT).show();
                                                                                                                                }
                                                                                                                            });
                                                                                                                }
                                                                                                            });
                                                                                                }
                                                                                            });
                                                                                }
                                                                                else
                                                                                {
                                                                                    FirebaseFirestore.getInstance().collection("Notifications")
                                                                                            .document(appointment.getCustomer_id())
                                                                                            .set(maps).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void unused) {


                                                                                                    Map<String, Object> connection = new HashMap<>();
                                                                                                    connection.put("matchID", appointment.getId());
                                                                                                    connection.put("participants", Arrays.asList(appointment.getCustomer_id(), appointment.getSeller_id()));
                                                                                                    connection.put("time", Timestamp.now());
                                                                                                    connection.put("show", true);

                                                                                                    FirebaseFirestore.getInstance().collection("Matches").document(appointment.getId()).set(connection)
                                                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                @Override
                                                                                                                public void onSuccess(Void unused) {
                                                                                                                    FirebaseFirestore.getInstance().collection("Matches")
                                                                                                                            .document(appointment.getId())
                                                                                                                            .update("time",FieldValue.serverTimestamp()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                @Override
                                                                                                                                public void onSuccess(Void unused) {
                                                                                                                                    pushNotification notification = new pushNotification(new notificationData("Your request appointment has been accepted",
                                                                                                                                            "Accepted appointment",appointment.getId(),appointment.getCustomer_id(),"appointment","buyer","accepted"), token);
                                                                                                                                    sendNotif(notification,"accepted","buyer");
                                                                                                                                    Toast.makeText(acquired_service_details.this, "Message ID created", Toast.LENGTH_SHORT).show();
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
                                                        });
                                            }
                                        }
                                    });
                        }
                    });
                    buttonDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseFirestore.getInstance().collection("User")
                                    .document(appointment.getCustomer_id()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot value) {
                                            if(value.exists()){
                                                String token= value.getString("fcmToken");
                                                Map<String,Object> map = new HashMap<>();
                                                map.put("send_to_id", appointment.getCustomer_id());
                                                map.put("message","Your request appointment has been cancelled");
                                                map.put("timestamp", Timestamp.now());
                                                map.put("type","appointment");

                                                Map<String,Object> maps = new HashMap<>();
                                                maps.put("id",appointment.getId());
                                                maps.put("latestNotification",map);
                                                maps.put("notification", Arrays.asList(map));

                                                FirebaseFirestore.getInstance().collection("Appointments")
                                                        .document(appointment.getId())
                                                        .update("appointment_status","cancelled")
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {

                                                                FirebaseFirestore.getInstance().collection("Notifications").document(appointment.getCustomer_id())
                                                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                                                if(documentSnapshot.exists()){
                                                                                    FirebaseFirestore.getInstance().collection("Notifications")
                                                                                            .document(appointment.getCustomer_id())
                                                                                            .update("latestNotification",map,"notification", FieldValue.arrayUnion(map)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void unused) {
                                                                                                    pushNotification notification = new pushNotification(new notificationData("Your request appointment has been cancelled",
                                                                                                            "Cancelled appointment",appointment.getId(),appointment.getCustomer_id(),"appointment","buyer","cancelled"), token);
                                                                                                    sendNotif(notification,"cancelled","buyer");
                                                                                                }
                                                                                            });
                                                                                }
                                                                                else{
                                                                                    FirebaseFirestore.getInstance().collection("Notifications")
                                                                                            .document(appointment.getCustomer_id()).set(maps).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void unused) {
                                                                                                    pushNotification notification = new pushNotification(new notificationData("Your request appointment has been cancelled",
                                                                                                            "Cancelled appointment",appointment.getId(),appointment.getCustomer_id(),"appointment","buyer","cancelled"), token);
                                                                                                    sendNotif(notification,"cancelled","buyer");
                                                                                                }
                                                                                            });
                                                                                }
                                                                            }
                                                                        });
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    });
                }
            }
            else if(appointment.getAppointment_status().equals("accepted")){

                messageButton.setVisibility(View.VISIBLE);
                buttonLayout.setVisibility(View.GONE);
                cancelButton.setVisibility(View.GONE);

                messageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseFirestore.getInstance().collection("Matches")
                                .document(appointment.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        matches_class match = documentSnapshot.toObject(matches_class.class);
                                        Intent intent = new Intent(acquired_service_details.this, acquired_service_accepted_message.class);
                                        intent.putExtra("model",  (Serializable) match);
                                        startActivity(intent);
                                    }
                                });
                    }
                });
            }
            else if(appointment.getAppointment_status().equals("completed")){

            }
            else if(appointment.getAppointment_status().equals("cancelled")){

                messageButton.setVisibility(View.GONE);
                buttonLayout.setVisibility(View.GONE);
                cancelButton.setVisibility(View.GONE);

            }

            if (appointment.getCustomer_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                pet_text.setText("Service details");

                FirebaseFirestore.getInstance().collection("Shop")
                        .document(appointment.getSeller_id())
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onSuccess(DocumentSnapshot s) {
                                check_out_service_acquired.setVisibility(View.VISIBLE);
                                check_out_name.setText(s.getString("shopName"));

                                FirebaseFirestore.getInstance().collection("User")
                                        .document(s.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                checkout_address.setText(documentSnapshot.getString("address"));
                                                checkout_zip.setText(documentSnapshot.getString("zipCode"));

                                                FirebaseFirestore.getInstance().collection("User")
                                                        .document(s.getId()).collection("security")
                                                        .document("security_doc").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                checkout_number.setText(documentSnapshot.getString("contactNumber"));

                                                                FirebaseFirestore.getInstance().collection("Services").document(appointment.getService_id()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {


                                                                        check_out_service_acquired.setText(documentSnapshot.getString("name"));


                                                                    }
                                                                });
                                                            }
                                                        });
                                            }
                                        });

                            }
                        });

            }

            FirebaseFirestore.getInstance().collection("User")
                    .document(appointment.getCustomer_id())
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
                    .document(appointment.getCustomer_id())
                    .collection("security")
                    .document("security_doc")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot s = task.getResult();
                                checkout_number.setText(s.getString("contactNumber"));
                            }
                        }
                    });
        }
    }

    private void sendNotif(pushNotification notification,String from,String notificationFor) {

        ApiUtilities.getClient().sendNotification(notification).enqueue(new Callback<pushNotification>() {
            @Override
            public void onResponse(Call<pushNotification> call, Response<pushNotification> response) {
                if(response.isSuccessful()){
                    if(from.equals("accepted")){

                            if(notificationFor.equals("buyer")){
                                Intent i = new Intent(acquired_service_details.this, service_status.class);
                                i.putExtra("SELECTED_TAB",from);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                Toast.makeText(acquired_service_details.this, "Appointment successfully moved to accepted tab", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else{
                                Intent i = new Intent(acquired_service_details.this, appointment_user_side.class);
                                i.putExtra("SELECTED_TAB",from);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                Toast.makeText(acquired_service_details.this, "Appointment successfully moved to accepted tab", Toast.LENGTH_SHORT).show();
                                finish();
                            }


                         }
                        else{
                        if(notificationFor.equals("buyer")){
                            Intent i = new Intent(acquired_service_details.this, service_status.class);
                            i.putExtra("SELECTED_TAB",from);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            Toast.makeText(acquired_service_details.this, "Appointment successfully moved to cancelled tab", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else{
                            Intent i = new Intent(acquired_service_details.this, appointment_user_side.class);
                            i.putExtra("SELECTED_TAB",from);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            Toast.makeText(acquired_service_details.this, "Appointment successfully moved to cancelled tab", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        }
                }else{

                }
            }

            @Override
            public void onFailure(Call<pushNotification> call, Throwable t) {
                Toast.makeText(acquired_service_details.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

}