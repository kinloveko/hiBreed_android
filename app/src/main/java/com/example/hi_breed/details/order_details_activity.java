package com.example.hi_breed.details;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.ApiUtilities;
import com.example.hi_breed.classesFile.TimestampConverter;
import com.example.hi_breed.classesFile.appointment_order_class;
import com.example.hi_breed.classesFile.matches_class;
import com.example.hi_breed.classesFile.notificationData;
import com.example.hi_breed.classesFile.priceFormat;
import com.example.hi_breed.classesFile.pushNotification;
import com.example.hi_breed.message.acquired_order_accepted_message;
import com.example.hi_breed.order_breeder_side.order_breeder_side;
import com.example.hi_breed.order_user_side.order_user_side;
import com.example.hi_breed.shop.rate_shop;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class order_details_activity extends AppCompatActivity {

    TextView check_out_name,text1,pet_text,
            checkout_number,transactionLabel,transactionMessage,
            checkout_address,
            checkout_zip;

    TextView statusLabel,status, transactionEndLabel ,transactionEnd ,transactionPaymentLabel,
            transactionPayment,subTotalLabel,subTotal,totalLabel,total,
            serviceFeeLabel,serviceFee;
    ImageView imageView;
    TextView product_name,breed,product_price,shop_name;

    Button cancelButton,messageButton,rateButton;
    RelativeLayout toolbarID,currentAddress,timeLayout,dateLayout,transactionLayout;
    LinearLayout buttonLayout,buttonContact,buttonDelete;
    String notCurrentUser;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        priceFormat price = new priceFormat();
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
        imageView = findViewById(R.id.imageView);
        messageButton = findViewById(R.id.messageButton);
        transactionLayout = findViewById(R.id.transactionLayout);
        transactionMessage = findViewById(R.id.transactionMessage);
        transactionLabel = findViewById(R.id.transactionLabel);
        currentAddress =findViewById(R.id.currentAddress);
        timeLayout = findViewById(R.id.timeLayout);
        dateLayout = findViewById(R.id.dateLayout);
        check_out_name = findViewById(R.id.check_out_name);
        checkout_number = findViewById(R.id.checkout_number);
        checkout_address = findViewById(R.id.checkout_address);
        checkout_zip = findViewById(R.id.checkout_zip);
        buttonLayout = findViewById(R.id.buttonLayout);
        text1 = findViewById(R.id.text1);
        pet_text= findViewById(R.id.pet_text);
        statusLabel = findViewById(R.id.statusLabel);
        status = findViewById(R.id.status);
        transactionEndLabel = findViewById(R.id.transactionEndLabel);
        transactionEnd = findViewById(R.id.transactionEnd);
        transactionPaymentLabel = findViewById(R.id.transactionPaymentLabel);
        transactionPayment = findViewById(R.id.transactionPayment);
        subTotalLabel = findViewById(R.id.subTotalLabel);
        subTotal = findViewById(R.id.subTotal);
        totalLabel = findViewById(R.id.totalLabel);
        total = findViewById(R.id.total);
        serviceFeeLabel = findViewById(R.id.serviceFeeLabel);
        serviceFee = findViewById(R.id.serviceFee);

        product_name = findViewById(R.id.product_name);
                breed = findViewById(R.id.breed);
        product_price = findViewById(R.id.product_price);

        rateButton = findViewById(R.id.rateButton);
        buttonContact = findViewById(R.id.buttonContact);
        buttonDelete = findViewById(R.id.buttonDelete);
        cancelButton= findViewById(R.id.cancelButton);
        toolbarID= findViewById(R.id.toolbarID);
        shop_name = findViewById(R.id.shop_name);

        toolbarID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_details_activity.this.onBackPressed();
                finish();
            }
        });

        Intent i = getIntent();
        appointment_order_class appointment = (appointment_order_class) i.getSerializableExtra("mode");

        if(appointment!=null) {

            if (!appointment.getCustomer_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                notCurrentUser = appointment.getCustomer_id();
            } else if (!appointment.getSeller_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                notCurrentUser = appointment.getSeller_id();
            }

            if(appointment.getOrder_status().equals("pending")){
                transactionLayout.setVisibility(View.VISIBLE);
                transactionLabel.setVisibility(View.GONE);
                transactionMessage.setVisibility(View.GONE);

                status.setText(appointment.getOrder_status());
                transactionPayment.setText("on-site");

                subTotal.setText("₱ "+price.priceFormatString(appointment.getItem_price()));
                total.setText("₱ "+price.priceFormatString(appointment.getItem_price())+".0");
                serviceFee.setText("₱ "+price.priceFormatString(appointment.getItem_price()));

                transactionEnd.setVisibility(View.GONE);
                transactionEndLabel.setVisibility(View.GONE);

                text1.setText("Order pending");
                if (appointment.getCustomer_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    cancelButton.setVisibility(View.VISIBLE);
                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            openDialog(appointment.getSeller_id(),appointment.getId(),"seller",appointment.getTransaction_id(),"buyer");

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
                                                map.put("sender",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                map.put("title","Order confirmed");
                                                map.put("message","Your order was confirmed by the seller");
                                                map.put("timestamp", Timestamp.now());
                                                map.put("type","order");
                                                map.put("id",appointment.getId());

                                                Map<String,Object> maps = new HashMap<>();

                                                maps.put("latestNotification",map);
                                                maps.put("notification", Arrays.asList(map));

                                                FirebaseFirestore.getInstance().collection("Appointments")
                                                        .document(appointment.getId())
                                                        .update("order_status","accepted")
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {

                                                                FirebaseFirestore.getInstance().collection("Transaction")
                                                                        .document(appointment.getTransaction_id())
                                                                        .update("status","ongoing").addOnSuccessListener(new OnSuccessListener<Void>() {
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
                                                                                                                    connection.put("matchFor","forOrders");
                                                                                                                    FirebaseFirestore.getInstance().collection("Matches").document(appointment.getId()).set(connection)
                                                                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                @Override
                                                                                                                                public void onSuccess(Void unused) {

                                                                                                                                    FirebaseFirestore.getInstance().collection("Matches")
                                                                                                                                            .document(appointment.getId())
                                                                                                                                            .update("time",FieldValue.serverTimestamp()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                                @Override
                                                                                                                                                public void onSuccess(Void unused) {
                                                                                                                                                    pushNotification notification = new pushNotification(new notificationData("Your order has been confirmed by the seller",
                                                                                                                                                            "Order confirmed",appointment.getId(),appointment.getCustomer_id(),"order","buyer","accepted"), token);
                                                                                                                                                    sendNotif(notification,"accepted","buyer");
                                                                                                                                                    }
                                                                                                                                            });
                                                                                                                                }
                                                                                                                            });
                                                                                                                }
                                                                                                            });
                                                                                                }
                                                                                                else
                                                                                                {
                                                                                                    FirebaseFirestore.getInstance().collection("Transaction")
                                                                                                            .document(appointment.getTransaction_id())
                                                                                                            .update("status","ongoing").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                @Override
                                                                                                                public void onSuccess(Void unused) {
                                                                                                                    FirebaseFirestore.getInstance().collection("Notifications")
                                                                                                                            .document(appointment.getCustomer_id())
                                                                                                                            .set(maps).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                @Override
                                                                                                                                public void onSuccess(Void unused) {

                                                                                                                                    Map<String, Object> connection = new HashMap<>();
                                                                                                                                    connection.put("matchID", appointment.getId());
                                                                                                                                    connection.put("matchFor","forOrder");
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
                                                                                                                                                                    pushNotification notification = new pushNotification(new notificationData("Your order has been confirmed by the seller",
                                                                                                                                                                            "Order confirmed",appointment.getId(),appointment.getCustomer_id(),"order","buyer","accepted"), token);
                                                                                                                                                                    sendNotif(notification,"accepted","buyer");
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

                    buttonDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openDialog(appointment.getCustomer_id(),appointment.getId(),"buyer",appointment.getTransaction_id(),"seller");
                        }
                    });

                }
            }
            else if(appointment.getOrder_status().equals("accepted")){
                text1.setText("Order ongoing");

                transactionLayout.setVisibility(View.VISIBLE);
                transactionLabel.setVisibility(View.GONE);
                transactionMessage.setVisibility(View.GONE);

                status.setText(appointment.getOrder_status());
                transactionPayment.setText("on-site");

                subTotal.setText("₱ "+price.priceFormatString(appointment.getItem_price()));
                total.setText("₱ "+price.priceFormatString(appointment.getItem_price())+".0");
                serviceFee.setText("₱ "+price.priceFormatString(appointment.getItem_price()));

                transactionEnd.setVisibility(View.GONE);
                transactionEndLabel.setVisibility(View.GONE);
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
                                        Intent intent = new Intent(order_details_activity.this, acquired_order_accepted_message.class);
                                        intent.putExtra("model",  (Serializable) match);
                                        startActivity(intent);
                                    }
                                });
                    }
                });
            }
            else if(appointment.getOrder_status().equals("completed")){
                text1.setText("Order completed");
                transactionLayout.setVisibility(View.VISIBLE);
                transactionLabel.setVisibility(View.GONE);
                transactionMessage.setVisibility(View.GONE);

                status.setText(appointment.getOrder_status());
                transactionPayment.setText("on-site");

                subTotal.setText("₱ "+price.priceFormatString(appointment.getItem_price()));
                total.setText("₱ "+price.priceFormatString(appointment.getItem_price())+".0");
                serviceFee.setText("₱ "+price.priceFormatString(appointment.getItem_price()));


                String formattedTime = TimestampConverter.exactDateTime(appointment.getOder_date_end());
                transactionEnd.setText(formattedTime);

                if(appointment.getCustomer_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){

                    FirebaseFirestore.getInstance().collection("Appointments")
                            .document(appointment.getId())
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Boolean isTrue = (Boolean) documentSnapshot.get("isRated");

                                    if(isTrue.equals(false)){
                                        rateButton.setVisibility(View.VISIBLE);
                                        rateButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent i = new Intent(order_details_activity.this, rate_shop.class);
                                                i.putExtra("model", (Serializable) appointment);
                                                startActivity(i);
                                            }
                                        });
                                    }
                                }
                            });

                }

            }
            else if(appointment.getOrder_status().equals("cancelled")){
                text1.setText("Order cancelled");
                transactionLabel.setText("Cancellation reason :");
                transactionMessage.setText(appointment.getOrder_cancelled_message());
                messageButton.setVisibility(View.GONE);
                buttonLayout.setVisibility(View.GONE);
                cancelButton.setVisibility(View.GONE);
                transactionLayout.setVisibility(View.VISIBLE);

                status.setText(appointment.getOrder_status());
                transactionPayment.setText("on-site");
                subTotal.setText("₱ "+appointment.getItem_price());
                total.setText("₱ "+appointment.getItem_price());
                serviceFee.setText("₱ "+appointment.getItem_price());

                String formattedTime = TimestampConverter.exactDateTime(appointment.getOder_date_end());
                transactionEnd.setText(formattedTime);

            }

            /**
              For visibility of buttons
             **/
            FirebaseFirestore.getInstance().collection("Appointments")
                    .document(appointment.getId())
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                return;
                            }
                            if (documentSnapshot.exists()) {

                                if (documentSnapshot.getString("order_status").equals("cancelled")) {

                                    if (documentSnapshot.getString("customer_id").equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                        cancelButton.setVisibility(View.GONE);
                                    }
                                    if (documentSnapshot.getString("seller_id").equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                        buttonLayout.setVisibility(View.GONE);

                                    }

                                } else if (documentSnapshot.getString("order_status").equals("accepted")) {
                                    messageButton.setVisibility(View.VISIBLE);
                                    buttonLayout.setVisibility(View.GONE);
                                    cancelButton.setVisibility(View.GONE);
                                } else if (documentSnapshot.getString("order_status").equals("completed")) {
                                    messageButton.setVisibility(View.GONE);
                                    buttonLayout.setVisibility(View.GONE);
                                    cancelButton.setVisibility(View.GONE);

                                }
                            }

                        }
                    });

            /**
             To check if the current user is the buyer
             **/
            if (appointment.getCustomer_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                pet_text.setText("Shop details");

                FirebaseFirestore.getInstance().collection("Shop")
                        .document(appointment.getSeller_id())
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onSuccess(DocumentSnapshot s) {
                                shop_name.setText(s.getString("shopName"));
                                check_out_name.setText(s.getString("shopName"));

                            }
                        });
                FirebaseFirestore.getInstance().collection("User")
                        .document(appointment.getSeller_id()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                checkout_address.setText(documentSnapshot.getString("address"));
                                checkout_zip.setText(documentSnapshot.getString("zipCode"));

                                FirebaseFirestore.getInstance().collection("User")
                                        .document(appointment.getSeller_id()).collection("security")
                                        .document("security_doc").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                checkout_number.setText(documentSnapshot.getString("contactNumber"));
                                            }
                                        });
                            }
                        });

            }
            else {
                /**
                 Getting the users name and phone number
                 **/
                FirebaseFirestore.getInstance().collection("Shop")
                        .document(appointment.getSeller_id())
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onSuccess(DocumentSnapshot s) {
                                shop_name.setText(s.getString("shopName"));
                            }
                        });
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
            }

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

            /**
                Check if what type of product is this to get the data
             **/
            if(appointment.getType().equals("pet")){
                FirebaseFirestore.getInstance().collection("Pet")
                        .document(appointment.getItem_id())
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                               if(documentSnapshot.exists()){
                                   List<String> list = (List<String>) documentSnapshot.get("photos");
                                   if (list != null) {
                                       Picasso.get().load(list.get(0)).placeholder(R.drawable.noimage)
                                               .into(imageView);
                                   }
                                   product_name.setText(documentSnapshot.getString("pet_breed"));
                                   product_price.setText(price.priceFormatString(documentSnapshot.getString("pet_price")));
                                   breed.setText(documentSnapshot.getString("pet_gender"));
                               }
                            }
                     });
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private void openDialog(String sendTo, String appointmentId, String role,String transactionID,String from) {

        Map<String,Object> map = new HashMap<>();
        map.put("dateCompleted",Timestamp.now());

        AlertDialog.Builder builder2 = new  AlertDialog.Builder(order_details_activity.this);
        View view = View.inflate(order_details_activity.this,R.layout.appointment_settings_cancell_options,null);
        TextView oneText = view.findViewById(R.id.oneButtonText);
        TextView twoText = view.findViewById(R.id.twoButtonText);

        if(role.equals("buyer")){
            oneText.setText("Time conflict with other appointments");
            twoText.setText("Client didn't respond on my chats");
        }

        RelativeLayout oneButton = view.findViewById(R.id.oneButton);
        RelativeLayout twoButton = view.findViewById(R.id.twoButton);
        RelativeLayout otherReasonButton = view.findViewById(R.id.otherReasonButton);

        builder2.setView(view);
        AlertDialog alert2 = builder2.create();
        alert2.show();
        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        oneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cancelDialog(sendTo,appointmentId,role,oneText.getText().toString(),transactionID,from,alert2);

            }
        });

        twoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cancelDialog(sendTo,appointmentId,role,twoText.getText().toString(),transactionID,from,alert2);

            }
        });

        otherReasonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageView imageBack = view.findViewById(R.id.imageBack);
                RelativeLayout reasonsLayout = view.findViewById(R.id.reasonsLayout);
                AppCompatImageView appCompatImageView = view.findViewById(R.id.appCompatImageView);
                LinearLayout otherReasonLayout = view.findViewById(R.id.otherReasonLayout);
                appCompatImageView.setVisibility(View.GONE);
                otherReasonLayout.setVisibility(View.VISIBLE);
                reasonsLayout.setVisibility(View.GONE);
                imageBack.setVisibility(View.VISIBLE);
                Button okayButton = view.findViewById(R.id.okayButton);
                EditText customReasonEdit = view.findViewById(R.id.customReasonEdit);

                imageBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        appCompatImageView.setVisibility(View.VISIBLE);
                        otherReasonLayout.setVisibility(View.GONE);
                        reasonsLayout.setVisibility(View.VISIBLE);
                        imageBack.setVisibility(View.GONE);
                        customReasonEdit.getText().clear();

                    }
                });
                okayButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(customReasonEdit.getText().toString().isEmpty()){
                            Toast.makeText(order_details_activity.this, "Please write your reason in the provided text box", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        cancelDialog(sendTo,appointmentId,role,customReasonEdit.getText().toString(),transactionID,from,alert2);
                    }
                });

            }
        });
    }

    private void cancelDialog(String sendTo, String appointmentId, String role,String text,String transactionID,String from,AlertDialog alertDialog) {

        FirebaseFirestore.getInstance().collection("User")
                .document(sendTo).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot value) {
                        if(value.exists()){
                            String token= value.getString("fcmToken");
                            Map<String,Object> map = new HashMap<>();
                            map.put("send_to_id", sendTo);
                            map.put("sender",FirebaseAuth.getInstance().getCurrentUser().getUid());
                            map.put("title","Cancelled order");
                            if(from.equals("seller"))
                            map.put("message","Your order has been cancelled : "+from);
                            if(from.equals("buyer"))
                                map.put("message","The order has been cancelled by the : "+from);

                            map.put("timestamp", Timestamp.now());
                            map.put("type","order");
                            map.put("id",appointmentId);

                            Map<String,Object> maps = new HashMap<>();

                            maps.put("latestNotification",map);
                            maps.put("notification", Arrays.asList(map));

                            FirebaseFirestore.getInstance().collection("Transaction")
                                    .document(transactionID)
                                    .update("status","cancelled").addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            FirebaseFirestore.getInstance().collection("Appointments")
                                                    .document(appointmentId)
                                                    .update("order_status","cancelled")
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {

                                                            Map<String,Object> map1 = new HashMap<>();
                                                            map1.put("oder_date_end",Timestamp.now());
                                                            map1.put("order_cancelled_message",text);
                                                            map1.put("cancelled_by",from);
                                                            FirebaseFirestore.getInstance().collection("Appointments")
                                                                    .document(appointmentId).set(map1, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            FirebaseFirestore.getInstance().collection("Notifications").document(sendTo)
                                                                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                        @Override
                                                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                                                            if(documentSnapshot.exists()){
                                                                                                FirebaseFirestore.getInstance().collection("Notifications")
                                                                                                        .document(sendTo)
                                                                                                        .update("latestNotification",map,"notification", FieldValue.arrayUnion(map)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onSuccess(Void unused) {

                                                                                                                pushNotification notification = new pushNotification(new notificationData("Your order has been cancelled",
                                                                                                                        "Cancelled order",appointmentId,sendTo,"order",role,"cancelled"), token);
                                                                                                                sendNotif(notification,"cancelled",role);
                                                                                                                Log.d("selectedTAB", notification.getData().getSELECTED_TAB());
                                                                                                                alertDialog.dismiss();
                                                                                                            }
                                                                                                        });
                                                                                            }
                                                                                            else{
                                                                                                FirebaseFirestore.getInstance().collection("Notifications")
                                                                                                        .document(sendTo).set(maps).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onSuccess(Void unused) {
                                                                                                                pushNotification notification = new pushNotification(new notificationData("Your order has been cancelled",
                                                                                                                        "Cancelled order",appointmentId,sendTo,"order",role,"cancelled"), token);
                                                                                                                sendNotif(notification,"cancelled",role);
                                                                                                                Log.d("selectedTAB", notification.getData().getSELECTED_TAB());
                                                                                                                alertDialog.dismiss();
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
                                    });

                        }
                    }
                });
    }


    private void sendNotif(pushNotification notification,String from,String notificationFor) {

        ApiUtilities.getClient().sendNotification(notification).enqueue(new Callback<pushNotification>() {
            @Override
            public void onResponse(Call<pushNotification> call, Response<pushNotification> response) {
                if(response.isSuccessful()){

                    if(from.equals("accepted")){

                        if(notificationFor.equals("buyer")){

                            Intent i = new Intent(order_details_activity.this, order_breeder_side.class);
                            i.putExtra("SELECTED_TAB",from);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            Toast.makeText(order_details_activity.this, "Your order successfully moved to accepted tab", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                        else{

                            Intent i = new Intent(order_details_activity.this, order_user_side.class);
                            i.putExtra("SELECTED_TAB",from);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            Toast.makeText(order_details_activity.this, "Your order successfully moved to accepted tab", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    }
                    else  if(from.equals("cancelled")){

                        if(notificationFor.equals("buyer")){
                            Intent i = new Intent(order_details_activity.this, order_breeder_side.class);
                            i.putExtra("SELECTED_TAB",from);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            Toast.makeText(order_details_activity.this, "Your order successfully moved to cancelled tab", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                        else{
                            Intent i = new Intent(order_details_activity.this, order_user_side.class);
                            i.putExtra("SELECTED_TAB",from);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            Toast.makeText(order_details_activity.this, "Your order successfully moved to cancelled tab", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    else{

                    }
                }else{

                }
            }

            @Override
            public void onFailure(Call<pushNotification> call, Throwable t) {
                Toast.makeText(order_details_activity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

}