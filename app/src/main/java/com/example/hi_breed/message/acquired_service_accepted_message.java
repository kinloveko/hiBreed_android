package com.example.hi_breed.message;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hi_breed.R;
import com.example.hi_breed.adapter.message_adapter.message_conversation_reply_adapter;
import com.example.hi_breed.classesFile.ApiUtilities;
import com.example.hi_breed.classesFile.IDTokens;
import com.example.hi_breed.classesFile.TimestampConverter;
import com.example.hi_breed.classesFile.appointment_class;
import com.example.hi_breed.classesFile.appointment_dating_class;
import com.example.hi_breed.classesFile.chat_conversation_class;
import com.example.hi_breed.classesFile.matches_class;
import com.example.hi_breed.classesFile.notificationData;
import com.example.hi_breed.classesFile.pushNotification;
import com.example.hi_breed.service.rate_service;
import com.example.hi_breed.service_status_for_buyer.appointment_user_side;
import com.example.hi_breed.service_status_for_seller.service_status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class acquired_service_accepted_message extends AppCompatActivity {

    LinearLayout backLayoutService;
    private TextView headerName;
    private RelativeLayout toolbarID, transactionLayout;
    private ImageView image;
    private RecyclerView chat_recyclerview;
    private ImageView sendReplyImageView;
    private TextView userStatus, transactionLabel, transactionMessage;
    private TextInputEditText replyEdit;
    ImageView complete;
    message_conversation_reply_adapter adapter;
    ScrollView scrollView;
    FirebaseUser user;
    Intent intent;
    CircleImageView avail;
    Boolean available = true;
    String match;
    String name;
    String images;
    String transactionID;
    ListenerRegistration listenerRegistration;
    private List<String> notCurrentUserList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acquired_service_accepted_message);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.setStatusBarColor(Color.parseColor("#e28743"));
        }

        complete = findViewById(R.id.complete);
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        userStatus = findViewById(R.id.userStatus);
        avail = findViewById(R.id.avail);
        scrollView = findViewById(R.id.scrollView);
        backLayoutService = findViewById(R.id.backLayoutService);
        backLayoutService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acquired_service_accepted_message.this.onBackPressed();
                finish();
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        chat_recyclerview = findViewById(R.id.chat_recyclerview);

        adapter = new message_conversation_reply_adapter(this, user.getUid());
        chat_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        replyEdit = findViewById(R.id.replyEdit);
        sendReplyImageView = findViewById(R.id.sendReplyImageView);
        headerName = findViewById(R.id.headerName);
        toolbarID = findViewById(R.id.toolbarID);
        image = findViewById(R.id.image);

        intent = getIntent();

        matches_class m = (matches_class) intent.getSerializableExtra("model");


        if (m.getMatchID() != null)
            Log.d("myMessaging", m.getMatchID());

        //getting the participants id first condition if the list size is not 0
        if (m.getParticipants().size() != 0) {
            //if 3 it means that the this message involves 2 clients and one shooter or a vet
            if (m.getParticipants().size() == 3) {
                for (String participant : m.getParticipants()) {
                    if (!participant.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        notCurrentUserList.add(participant);
                    }
                }
                getReceiverInfo(notCurrentUserList);
            } else if (m.getParticipants().size() == 2) {
                for (String participant : m.getParticipants()) {
                    if (!participant.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        notCurrentUserList.add(participant);
                    }
                }
                getReceiverInfo(notCurrentUserList);
            }
        } else {
            FirebaseFirestore.getInstance().collection("Matches").document(m.getMatchID())
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            List<String> list = (List<String>) documentSnapshot.get("participants");
                            List<String> notCurrentUserList = new ArrayList<>();
                            if (list != null) for (String participant : list) {
                                if (!participant.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    notCurrentUserList.add(participant);
                                }
                            }
                            getReceiverInfo(notCurrentUserList);
                        }
                    });
        }

        listenerRegistration = FirebaseFirestore.getInstance()
                .collection("Appointments")
                .document(m.getMatchID())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }
                        if (documentSnapshot != null) {


                            transactionID = documentSnapshot.getString("transaction_id");
                            if (documentSnapshot.exists() && documentSnapshot.getString("appointment_status").equals("completed")) {
                                if (documentSnapshot.getString("appointment_for") != null) {
                                    List<String> customerId = (List<String>) documentSnapshot.get("customer_id");

                                    if (customerId.contains(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                        appointment_dating_class appoint = documentSnapshot.toObject(appointment_dating_class.class);
                                        Intent i = new Intent(acquired_service_accepted_message.this, rate_service.class);
                                        i.putExtra("model", (Serializable) appoint);
                                        i.putExtra("from", "dating");
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                        Toast.makeText(acquired_service_accepted_message.this, "Appointment successfully moved to completed tab", Toast.LENGTH_SHORT).show();
                                        finish();
                                        // Remove the listener after starting the new activity
                                        listenerRegistration.remove();
                                    }
                                } else {
                                    String customerId = documentSnapshot.getString("customer_id");
                                    if (customerId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                        appointment_class appoint = documentSnapshot.toObject(appointment_class.class);
                                        Intent i = new Intent(acquired_service_accepted_message.this, rate_service.class);
                                        i.putExtra("model", (Serializable) appoint);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                        Toast.makeText(acquired_service_accepted_message.this, "Appointment successfully moved to completed tab", Toast.LENGTH_SHORT).show();
                                        finish();
                                        // Remove the listener after starting the new activity
                                        listenerRegistration.remove();
                                    }
                                }

                            } else if (documentSnapshot.getString("appointment_status").equals("cancelled")) {

                                if (documentSnapshot.getString("appointment_for") != null) {
                                    List<String> ids = (List<String>) documentSnapshot.get("customer_id");
                                    if (ids.contains(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                        Intent i = new Intent(acquired_service_accepted_message.this, appointment_user_side.class);
                                        i.putExtra("SELECTED_TAB", "cancelled");
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                        Toast.makeText(acquired_service_accepted_message.this, "Appointment successfully moved to cancelled tab", Toast.LENGTH_SHORT).show();
                                        finish();
                                        listenerRegistration.remove();
                                    }
                                } else {
                                    if (documentSnapshot.get("customer_id").equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                        Intent i = new Intent(acquired_service_accepted_message.this, appointment_user_side.class);
                                        i.putExtra("SELECTED_TAB", "cancelled");
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                        Toast.makeText(acquired_service_accepted_message.this, "Appointment successfully moved to cancelled tab", Toast.LENGTH_SHORT).show();
                                        finish();
                                        listenerRegistration.remove();
                                    }
                                }

                            }
                        }
                    }
                });

        getConversation(m.getMatchID());
        match = m.getMatchID();

        FirebaseFirestore.getInstance().collection("Appointments")
                .document(m.getMatchID())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            if (documentSnapshot.getString("seller_id").equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                                complete.setVisibility(View.VISIBLE);

                            } else {
                                complete.setVisibility(View.GONE);
                            }
                        }
                    }
                });
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog(notCurrentUserList);
            }
        });

        getCurrent();
        replyEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    sendReplyImageView.setEnabled(false);
                } else {
                    sendReplyImageView.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
        sendReplyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(replyEdit.getText().toString(), m.getMatchID(), notCurrentUserList);

            }
        });
    }




    @SuppressLint("SetTextI18n")
    private void openDialog(List<String> notCurrentUserList) {

        Map<String, Object> map = new HashMap<>();
        map.put("dateCompleted", Timestamp.now());

        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.appointment_settings_layout, null);

        RelativeLayout completedButton, cancelButton;
        completedButton = view.findViewById(R.id.completedButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        builder2.setView(view);
        AlertDialog alert2 = builder2.create();
        alert2.show();
        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        completedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveCompleted(notCurrentUserList);

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert2.dismiss();
                AlertDialog.Builder builder2 = new AlertDialog.Builder(acquired_service_accepted_message.this);
                View view = View.inflate(acquired_service_accepted_message.this, R.layout.appointment_settings_cancell_options, null);

                RelativeLayout oneButton = view.findViewById(R.id.oneButton);
                RelativeLayout twoButton = view.findViewById(R.id.twoButton);
                RelativeLayout otherReasonButton = view.findViewById(R.id.otherReasonButton);
                TextView oneText = view.findViewById(R.id.oneButtonText);
                TextView twoText = view.findViewById(R.id.twoButtonText);
                oneText.setText("Time conflict with other appointments");
                twoText.setText("Client didn't respond on my chats");

                builder2.setView(view);
                AlertDialog alert2 = builder2.create();
                alert2.show();
                alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                oneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        saveChanges(oneText.getText().toString(), alert2, notCurrentUserList);
                    }
                });

                twoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        saveChanges(twoText.getText().toString(), alert2, notCurrentUserList);
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
                                if (customReasonEdit.getText().toString().isEmpty()) {
                                    Toast.makeText(acquired_service_accepted_message.this, "Please write your reason in the provided text box", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                saveChanges(customReasonEdit.getText().toString(), alert2, notCurrentUserList);
                            }
                        });

                    }
                });
            }
        });

    }

    private void saveCompleted(List<String> notCurrentUserList) {
        FirebaseFirestore.getInstance().collection("Appointments")
                .document(match).update("appointment_status", "completed")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Map<String, Object> map = new HashMap<>();
                        map.put("appointment_end_date", Timestamp.now());
                        if (notCurrentUserList.size() == 1) {
                            map.put("isRated", false);
                        } else if (notCurrentUserList.size() == 2) {
                            map.put("isRatedBy", new ArrayList<String>());
                            map.put("rated_id", new ArrayList<String>());

                            FirebaseFirestore.getInstance().collection("Appointments")
                                    .document(match).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            String pet_dating_match_id = documentSnapshot.getString("pet_dating_match_id");
                                            //here i will erase the appointment_id field and status
                                            if(pet_dating_match_id !=null)
                                            FirebaseFirestore.getInstance().collection("Matches")
                                                    .document(pet_dating_match_id).update("appointment_id", FieldValue.delete(),
                                                            "status", FieldValue.delete());
                                        }
                                    });
                        }

                        FirebaseFirestore.getInstance().collection("Appointments")
                                .document(match).set(map, SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        for (IDTokens i : IdAndToken) {
                                            Map<String, Object> map = new HashMap<>();
                                            map.put("send_to_id", i.getId());
                                            map.put("sender", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                            map.put("title", "Completed appointment");
                                            map.put("message", "Appointment has been completed");
                                            map.put("timestamp", Timestamp.now());
                                            map.put("type", "appointment");
                                            map.put("id", match);

                                            Map<String, Object> maps = new HashMap<>();

                                            maps.put("latestNotification", map);
                                            maps.put("notification", Arrays.asList(map));

                                            FirebaseFirestore.getInstance().collection("Transaction")
                                                    .document(transactionID)
                                                    .update("status", "completed", "transaction_end", FieldValue.serverTimestamp())
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {


                                                            FirebaseFirestore.getInstance().collection("Notifications")
                                                                    .document(i.getId())
                                                                    .get()
                                                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                                            if (documentSnapshot.exists()) {
                                                                                FirebaseFirestore.getInstance().collection("Notifications")
                                                                                        .document(i.getId())
                                                                                        .update("latestNotification", map, "notification", FieldValue.arrayUnion(map)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void unused) {

                                                                                                pushNotification notification = new pushNotification(new notificationData("Appointment has been completed",
                                                                                                        "Completed appointment", match, i.getId(), "appointment", "buyer", "completed"), i.getToken());
                                                                                                sendNotif(notification, "completed", "buyer");

                                                                                            }
                                                                                        });
                                                                            } else {

                                                                                FirebaseFirestore.getInstance().collection("Notifications")
                                                                                        .document(i.getId()).set(maps).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void unused) {

                                                                                                pushNotification notification = new pushNotification(new notificationData("Appointment has been completed",
                                                                                                        "Completed appointment", match, i.getId(), "appointment", "buyer", "completed"), i.getToken());
                                                                                                sendNotif(notification, "completed", "buyer");
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

    private void saveChanges(String textEdit, AlertDialog alertDialog, List<String> notCurrentUserList) {

        FirebaseFirestore.getInstance().collection("Appointments")
                .document(match)
                .update("appointment_status", "cancelled").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Map<String, Object> map = new HashMap<>();
                        map.put("appointment_end_date", Timestamp.now());
                        map.put("appointment_end_message", textEdit);
                        map.put("cancelled_by", "seller");

                        FirebaseFirestore.getInstance().collection("Appointments")
                                .document(match).set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        for (IDTokens i : IdAndToken) {
                                            Map<String, Object> map = new HashMap<>();
                                            map.put("id", match);
                                            map.put("send_to_id", i.getId());
                                            map.put("sender", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                            map.put("title", "Cancelled appointment");
                                            map.put("message", "Requested appointment has been cancelled");
                                            map.put("timestamp", Timestamp.now());
                                            map.put("type", "appointment");

                                            Map<String, Object> maps = new HashMap<>();

                                            maps.put("latestNotification", map);
                                            maps.put("notification", Arrays.asList(map));


                                            FirebaseFirestore.getInstance().collection("Transaction")
                                                    .document(transactionID)
                                                    .update("status", "cancelled", "transaction_end", FieldValue.serverTimestamp())
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {

                                                            FirebaseFirestore.getInstance().collection("Notifications").document(i.getId())
                                                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                                            if (documentSnapshot.exists()) {

                                                                                FirebaseFirestore.getInstance().collection("Notifications")
                                                                                        .document(i.getId())
                                                                                        .update("latestNotification", map, "notification", FieldValue.arrayUnion(map)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void unused) {

                                                                                                pushNotification notification = new pushNotification(new notificationData("Requested appointment has been cancelled",
                                                                                                        "Cancelled appointment", match, i.getId(), "appointment", "buyer", "cancelled"), i.getToken());
                                                                                                sendNotif(notification, "cancelled", "buyer");
                                                                                            }
                                                                                        });
                                                                            } else {
                                                                                FirebaseFirestore.getInstance().collection("Notifications")
                                                                                        .document(i.getId()).set(maps).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void unused) {

                                                                                                pushNotification notification = new pushNotification(new notificationData("Requested appointment has been cancelled",
                                                                                                        "Cancelled appointment", match, i.getId(), "appointment", "buyer", "cancelled"), i.getToken());
                                                                                                sendNotif(notification, "cancelled", "buyer");

                                                                                            }
                                                                                        });
                                                                            }

                                                                        }
                                                                    });
                                                        }
                                                    });

                                        }

                                        Toast.makeText(acquired_service_accepted_message.this, "Successfully Cancelled", Toast.LENGTH_SHORT).show();


                                    }
                                });
                    }
                });

    }

    private void getCurrent() {

        FirebaseFirestore.getInstance().collection("User")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot s = task.getResult();
                            if (s.exists()) {

                                String holder = s.get("firstName").toString() + " " + s.getString("middleName") + " " + s.getString("lastName");
                                name = holder;
                                images = s.getString("image");

                            }
                        }
                    }
                });
    }

    private void sendMessage(String text, String matchID, List<String> notCurrentUser) {
        List<String> addAllParticipants = new ArrayList<>();
        addAllParticipants.addAll(notCurrentUser);
        addAllParticipants.add(user.getUid());


        FieldValue serverTimestamp = FieldValue.serverTimestamp();
        Map<String, Object> latestMessage = new HashMap<>();
        latestMessage.put("sender", user.getUid());
        latestMessage.put("text", text);
        latestMessage.put("timestamp", Timestamp.now());

        Map<String, Object> conversation = new HashMap<>();
        conversation.put("participants", addAllParticipants);
        conversation.put("latestMessage", latestMessage);
        conversation.put("messages", Arrays.asList(latestMessage));
        conversation.put("matchID", matchID);
        conversation.put("chatFor", "forAppointments");

        for (IDTokens i : IdAndToken) {

            FirebaseFirestore.getInstance().collection("Chat").document(matchID)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot s = task.getResult();
                                if (s.exists()) {

                                    FirebaseFirestore.getInstance()
                                            .collection("Chat")
                                            .document(matchID)
                                            .update("latestMessage", latestMessage, "messages", FieldValue.arrayUnion(latestMessage))
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        pushNotification notification = new pushNotification(new notificationData(text, name, matchID, i.getId(), "messageAppointment", "", ""), i.getToken());
                                                        sendNotif(notification, "", "");
                                                        scrollView.post(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                                                            }
                                                        });

                                                    }
                                                }
                                            });
                                } else {
                                    FirebaseFirestore.getInstance()
                                            .collection("Chat")
                                            .document(matchID)
                                            .set(conversation, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        FirebaseFirestore.getInstance().collection("Chat")
                                                                .document(matchID)
                                                                .update("latestMessage.timestamp", serverTimestamp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {

                                                                            pushNotification notification = new pushNotification(new notificationData(text, name, matchID, i.getId(), "messageAppointment", "", ""), i.getToken());
                                                                            sendNotif(notification, "", "");
                                                                            scrollView.post(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
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
                        }
                    });
            replyEdit.getText().clear();
        }
    }

    private void sendNotif(pushNotification notification, String from, String notificationFor) {

        ApiUtilities.getClient().sendNotification(notification).enqueue(new Callback<pushNotification>() {
            @Override
            public void onResponse(Call<pushNotification> call, Response<pushNotification> response) {
                if (response.isSuccessful()) {
                    if (from.equals("completed")) {
                        if (notificationFor.equals("buyer")) {
                            Intent i = new Intent(acquired_service_accepted_message.this, service_status.class);
                            i.putExtra("SELECTED_TAB", from);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                        }
                    }
                    else if (from.equals("cancelled")) {
                        if (notificationFor.equals("buyer")) {
                            Intent i = new Intent(acquired_service_accepted_message.this, service_status.class);
                            i.putExtra("SELECTED_TAB", from);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                        }
                    } else {
                        //message only
                    }
                } else {
                    if (from.equals("completed")) {
                        if (notificationFor.equals("buyer")) {
                            Intent i = new Intent(acquired_service_accepted_message.this, service_status.class);
                            i.putExtra("SELECTED_TAB", from);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            Toast.makeText(acquired_service_accepted_message.this, "Appointment successfully moved to completed tab", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else if (from.equals("cancelled")) {
                        if (notificationFor.equals("buyer")) {
                            Intent i = new Intent(acquired_service_accepted_message.this, service_status.class);
                            i.putExtra("SELECTED_TAB", from);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            Toast.makeText(acquired_service_accepted_message.this, "Appointment successfully moved to cancelled tab", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<pushNotification> call, Throwable t) {
                Toast.makeText(acquired_service_accepted_message.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getConversation(String matchID) {

        FirebaseFirestore.getInstance().collection("Chat").whereEqualTo("matchID", matchID)
                .whereEqualTo("chatFor", "forAppointments")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }
                        if (value != null) {
                            for (DocumentSnapshot s : value) {
                                chat_conversation_class c = s.toObject(chat_conversation_class.class);
                                adapter.addMatchDisplay(c.getMessages());
                                chat_recyclerview.setAdapter(adapter);
                                chat_recyclerview.smoothScrollToPosition(chat_recyclerview.getAdapter().getItemCount());
                                scrollView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                                    }
                                });
                            }
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    String names = "";
    private List<IDTokens> IdAndToken = new ArrayList<>();

    private void getReceiverInfo(List<String> id) {
        List<Bitmap> bitmaps = new ArrayList<>();
        String separator = " & ";
        for (int i = 0; i < id.size(); i++) {
            if (id.size() == 1) {
                int finalI = i;
                FirebaseFirestore.getInstance().collection("User")
                        .document(id.get(i))
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot s = task.getResult();
                                    if (s.getString("fcmToken") != null) {
                                        String userToken = s.getString("fcmToken");
                                        IdAndToken.add(new IDTokens(id.get(finalI), userToken));
                                    }
                                    Picasso.get().load(s.getString("image")).placeholder(R.drawable.noimage)
                                            .into(image);

                                    headerName.setText(s.getString("firstName"));

                                    if (s.get("online") != null) {

                                        if (s.get("online").equals("true")) {
                                            avail.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#5CCD08")));
                                            userStatus.setText("Active now");
                                            available = true;
                                        } else {
                                            String formattedTime = TimestampConverter.timestamp((Timestamp) s.get("online"));
                                            avail.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFADB6C4")));
                                            userStatus.setText("Active " + formattedTime);
                                            available = false;
                                        }

                                    } else {

                                    }
                                }
                            }
                        });
            } else if (id.size() == 2) {
                int finalI = i;

                FirebaseFirestore.getInstance().collection("User")
                        .document(id.get(i))
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot s = task.getResult();
                                    if (s.getString("fcmToken") != null) {
                                        String userToken = s.getString("fcmToken");
                                        IdAndToken.add(new IDTokens(id.get(finalI), userToken));
                                    } else {
                                        IdAndToken.add(new IDTokens(id.get(finalI), ""));
                                    }

                                    String imageUrl = s.getString("image");
                                    if (imageUrl != null) {
                                        Picasso.get().load(imageUrl).into(new Target() {
                                            @Override
                                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                bitmaps.add(bitmap);
                                                if (bitmaps.size() == 2) {
                                                    // Merge the two bitmaps and set the merged bitmap to your ImageView
                                                    Bitmap mergedBitmap = mergeBitmaps(bitmaps.get(0), bitmaps.get(1));
                                                    image.setImageBitmap(mergedBitmap);
                                                }
                                            }

                                            @Override
                                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                                e.printStackTrace();
                                            }

                                            @Override
                                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                                            }
                                        });
                                    }
                                    String name = s.getString("firstName");
                                    if (names.isEmpty()) {
                                        names += name;
                                    } else {
                                        names += separator + name;
                                    }
                                    headerName.setText(names);
                                    if (s.get("online") != null) {

                                        if (s.get("online").equals("true")) {
                                            avail.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#5CCD08")));
                                            userStatus.setText("Active now");
                                            available = true;
                                        } else {
                                            String formattedTime = TimestampConverter.timestamp((Timestamp) s.get("online"));
                                            avail.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFADB6C4")));
                                            userStatus.setText("Active " + formattedTime);
                                            available = false;
                                        }

                                    } else {

                                    }
                                }
                            }
                        });
            }
        }
    }

    private Bitmap mergeBitmaps(Bitmap firstBitmap, Bitmap secondBitmap) {
        // Resize smaller bitmap to match dimensions of larger bitmap
        if (firstBitmap.getWidth() < secondBitmap.getWidth() || firstBitmap.getHeight() < secondBitmap.getHeight()) {
            firstBitmap = Bitmap.createScaledBitmap(firstBitmap, secondBitmap.getWidth(), secondBitmap.getHeight(), false);
        } else if (secondBitmap.getWidth() < firstBitmap.getWidth() || secondBitmap.getHeight() < firstBitmap.getHeight()) {
            secondBitmap = Bitmap.createScaledBitmap(secondBitmap, firstBitmap.getWidth(), firstBitmap.getHeight(), false);
        }
        // Create merged bitmap
        Bitmap mergedBitmap = Bitmap.createBitmap(firstBitmap.getWidth() + secondBitmap.getWidth(), firstBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mergedBitmap);
        canvas.drawBitmap(firstBitmap, 0f, 0f, null);
        canvas.drawBitmap(secondBitmap, firstBitmap.getWidth(), 0f, null);
        return mergedBitmap;
    }

}
