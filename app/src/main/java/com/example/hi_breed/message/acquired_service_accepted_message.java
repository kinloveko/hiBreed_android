package com.example.hi_breed.message;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.example.hi_breed.classesFile.TimestampConverter;
import com.example.hi_breed.classesFile.appointment_class;
import com.example.hi_breed.classesFile.chat_conversation_class;
import com.example.hi_breed.classesFile.matches_class;
import com.example.hi_breed.classesFile.notificationData;
import com.example.hi_breed.classesFile.pushNotification;
import com.example.hi_breed.rate_service;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
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
    private RelativeLayout toolbarID,transactionLayout;
    private CircleImageView image;
    private RecyclerView chat_recyclerview;
    private ImageView sendReplyImageView;
    private TextView userStatus,transactionLabel,transactionMessage;
    private TextInputEditText replyEdit;
    ImageView complete;
    String notCurrentUser;
    message_conversation_reply_adapter adapter;
    ScrollView scrollView;
    FirebaseUser user;
    Intent intent;
    CircleImageView avail;
    Boolean available = true;
    String userToken;
    String match;
    String name;
    String images;
    String transactionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.acquired_service_accepted_message);

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

            adapter = new message_conversation_reply_adapter(this,user.getUid());
            chat_recyclerview.setLayoutManager(new LinearLayoutManager(this));
            replyEdit = findViewById(R.id.replyEdit);
            sendReplyImageView = findViewById(R.id.sendReplyImageView);
            headerName = findViewById(R.id.headerName);
            toolbarID = findViewById(R.id.toolbarID);
            image = findViewById(R.id.image);

            intent = getIntent();


                matches_class m = (matches_class) intent.getSerializableExtra("model");


                if( m.getMatchID()!=null)
                    Log.d("myMessaging",m.getMatchID());

            if(m.getParticipants().size()!=0) {
                notCurrentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                if (!notCurrentUser.equals(m.getParticipants().get(0))) {
                    notCurrentUser = m.getParticipants().get(0);
                    getReceiverInfo(notCurrentUser);
                } else if (!notCurrentUser.equals(m.getParticipants().get(1))) {
                    notCurrentUser = m.getParticipants().get(1);
                    getReceiverInfo(notCurrentUser);
                }
            }
            else{
                FirebaseFirestore.getInstance().collection("Matches").document(m.getMatchID())
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                List<String> list = (List<String>)  documentSnapshot.get("participants");
                                if (list != null) {
                                    if(!list.get(0).equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){

                                        notCurrentUser = list.get(0);
                                        getReceiverInfo(notCurrentUser);
                                    }
                                    else{
                                        notCurrentUser = list.get(1);
                                        getReceiverInfo(notCurrentUser);
                                    }
                                }
                            }
                        });
            }


        FirebaseFirestore.getInstance().collection("Appointments").document(m.getMatchID())
                        .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                                if(error!=null){
                                    return;
                                }
                                if(documentSnapshot.exists()){
                                    transactionID = documentSnapshot.getString("transaction_id");
                                    if(documentSnapshot.getString("appointment_status").equals("completed")){

                                        if(documentSnapshot.getString("customer_id").equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                            appointment_class appoint = documentSnapshot.toObject(appointment_class.class);
                                            Intent i = new Intent(acquired_service_accepted_message.this, rate_service.class);
                                            i.putExtra("model", (Serializable) appoint);
                                            startActivity(i);
                                            Toast.makeText(acquired_service_accepted_message.this, "Appointment successfully moved to completed tab", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }
                                    else
                                    if(documentSnapshot.getString("appointment_status").equals("cancelled")){

                                        if(documentSnapshot.getString("customer_id") .equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                            Intent i = new Intent(acquired_service_accepted_message.this, appointment_user_side.class);
                                            i.putExtra("SELECTED_TAB", "cancelled");
                                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(i);
                                            Toast.makeText(acquired_service_accepted_message.this, "Appointment successfully moved to cancelled tab", Toast.LENGTH_SHORT).show();
                                            finish();
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
                            if(documentSnapshot.exists()){
                                if(documentSnapshot.getString("seller_id").equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){

                                    complete.setVisibility(View.VISIBLE);

                                }
                                else{
                                    complete.setVisibility(View.GONE);
                                }
                            }
                        }
                    });

            complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialog();
                }
            });

            getCurrent();

            replyEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.toString().isEmpty()){
                        sendReplyImageView.setEnabled(false);
                    }
                    else{
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

                    sendMessage(replyEdit.getText().toString(),m.getMatchID(),notCurrentUser);
                }
            });

    }

    @SuppressLint("SetTextI18n")
    private void openDialog() {

        Map<String,Object> map = new HashMap<>();
        map.put("dateCompleted",Timestamp.now());

        AlertDialog.Builder builder2 = new  AlertDialog.Builder(this);
        View view = View.inflate(this,R.layout.appointment_settings_layout,null);

        RelativeLayout completedButton,cancelButton;
        completedButton = view.findViewById(R.id.completedButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        builder2.setView(view);
        AlertDialog alert2 = builder2.create();
        alert2.show();
        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        completedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveCompleted();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert2.dismiss();
                AlertDialog.Builder builder2 = new  AlertDialog.Builder(acquired_service_accepted_message.this);
                View view = View.inflate(acquired_service_accepted_message.this,R.layout.appointment_settings_cancell_options,null);

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

                        saveChanges(oneText.getText().toString(),alert2);
                    }
                });

                twoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        saveChanges(twoText.getText().toString(),alert2);
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
                                    Toast.makeText(acquired_service_accepted_message.this, "Please write your reason in the provided text box", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                saveChanges(customReasonEdit.getText().toString(),alert2);
                            }
                        });

                    }
                });
            }
        });

    }

    private void saveCompleted(){

        FirebaseFirestore.getInstance().collection("Appointments")
                .document(match).update("appointment_status","completed")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Map<String,Object> map = new HashMap<>();
                        map.put("appointment_end_date",Timestamp.now());
                        map.put("isRated",false);
                        FirebaseFirestore.getInstance().collection("Appointments")
                                .document(match).set(map,SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Map<String,Object> map = new HashMap<>();
                                        map.put("send_to_id", notCurrentUser);
                                        map.put("message","Appointment has been completed");
                                        map.put("timestamp", Timestamp.now());
                                        map.put("type","appointment");
                                        map.put("id",match);

                                        Map<String,Object> maps = new HashMap<>();

                                        maps.put("latestNotification",map);
                                        maps.put("notification", Arrays.asList(map));

                                        FirebaseFirestore.getInstance().collection("Transaction")
                                                .document(transactionID)
                                                .update("status","completed","transaction_end",FieldValue.serverTimestamp())
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                        FirebaseFirestore.getInstance().collection("Notifications")
                                                                .document(notCurrentUser)
                                                                .get()
                                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                                        if(documentSnapshot.exists()){
                                                                            FirebaseFirestore.getInstance().collection("Notifications")
                                                                                    .document(notCurrentUser)
                                                                                    .update("latestNotification",map,"notification", FieldValue.arrayUnion(map)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void unused) {

                                                                                            pushNotification notification = new pushNotification(new notificationData("Appointment has been completed",
                                                                                                    "Completed appointment",match,notCurrentUser,"appointment","buyer","completed"), userToken);
                                                                                            sendNotif(notification,"completed","buyer");

                                                                                        }
                                                                                    });
                                                                        }
                                                                        else{

                                                                            FirebaseFirestore.getInstance().collection("Notifications")
                                                                                    .document(notCurrentUser).set(maps).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void unused) {

                                                                                            pushNotification notification = new pushNotification(new notificationData("Appointment has been completed",
                                                                                                    "Completed appointment",match,notCurrentUser,"appointment","buyer","completed"), userToken);
                                                                                            sendNotif(notification,"completed","buyer");

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

    private void saveChanges(String textEdit,AlertDialog alertDialog) {

        FirebaseFirestore.getInstance().collection("Appointments")
                .document(match)
                .update("appointment_status","cancelled").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Map<String,Object> map = new HashMap<>();
                        map.put("appointment_end_date",Timestamp.now());
                        map.put("appointment_end_message",textEdit);
                        map.put("cancelled_by","seller");

                        FirebaseFirestore.getInstance().collection("Appointments")
                                .document(match).set(map,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {


                                        Map<String,Object> map = new HashMap<>();
                                        map.put("id",match);
                                        map.put("send_to_id", notCurrentUser);
                                        map.put("message","Requested appointment has been cancelled");
                                        map.put("timestamp", Timestamp.now());
                                        map.put("type","appointment");


                                        Map<String,Object> maps = new HashMap<>();

                                        maps.put("latestNotification",map);
                                        maps.put("notification", Arrays.asList(map));


                                        FirebaseFirestore.getInstance().collection("Transaction")
                                                .document(transactionID)
                                                .update("status","cancelled","transaction_end",FieldValue.serverTimestamp())
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                        FirebaseFirestore.getInstance().collection("Notifications").document(notCurrentUser)
                                                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                                        if(documentSnapshot.exists()){

                                                                            FirebaseFirestore.getInstance().collection("Notifications")
                                                                                    .document(notCurrentUser)
                                                                                    .update("latestNotification",map,"notification", FieldValue.arrayUnion(map)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void unused) {

                                                                                            pushNotification notification = new pushNotification(new notificationData("Requested appointment has been cancelled",
                                                                                                    "Cancelled appointment",match,notCurrentUser,"appointment","buyer","cancelled"), userToken);
                                                                                            sendNotif(notification,"cancelled","buyer");
                                                                                        }
                                                                                    });
                                                                        }
                                                                        else{
                                                                            FirebaseFirestore.getInstance().collection("Notifications")
                                                                                    .document(notCurrentUser).set(maps).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void unused) {

                                                                                            pushNotification notification = new pushNotification(new notificationData("Requested appointment has been cancelled",
                                                                                                    "Cancelled appointment",match,notCurrentUser,"appointment","buyer","cancelled"), userToken);
                                                                                            sendNotif(notification,"cancelled","buyer");

                                                                                        }
                                                                                    });
                                                                        }

                                                                    }
                                                                });
                                                    }
                                                });

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
                        if(task.isSuccessful()){
                            DocumentSnapshot s = task.getResult();
                            if(s.exists()){

                                String holder = s.get("firstName").toString() + " "+s.getString("middleName")+" " + s.getString("lastName");
                                name = holder;
                                images = s.getString("image");

                            }
                        }
                    }
                });
    }

    private void sendMessage(String text,String matchID,String notCurrentUser) {

        FieldValue serverTimestamp = FieldValue.serverTimestamp();
        Map<String, Object> latestMessage = new HashMap<>();
        latestMessage.put("sender",user.getUid());
        latestMessage.put("text", text);
        latestMessage.put("timestamp", Timestamp.now());

        Map<String, Object> conversation = new HashMap<>();
        conversation.put("participants", Arrays.asList(user.getUid(),notCurrentUser));
        conversation.put("latestMessage", latestMessage);
        conversation.put("messages", Arrays.asList(latestMessage));
        conversation.put("matchID",matchID);
        conversation.put("chatFor","forAppointments");

        FirebaseFirestore.getInstance().collection("Chat").document(matchID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot s = task.getResult();
                            if(s.exists()){

                                FirebaseFirestore.getInstance()
                                        .collection("Chat")
                                        .document(matchID)
                                        .update("latestMessage", latestMessage, "messages", FieldValue.arrayUnion(latestMessage))
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){

                                                        pushNotification notification = new pushNotification(new notificationData(text,name,matchID,notCurrentUser,"messageAppointment","",""),userToken);
                                                        sendNotif(notification,"","");
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
                            else{
                                FirebaseFirestore.getInstance()
                                        .collection("Chat")
                                        .document(matchID)
                                        .set(conversation, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    FirebaseFirestore.getInstance().collection("Chat")
                                                            .document(matchID)
                                                            .update("latestMessage.timestamp", serverTimestamp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){

                                                                            pushNotification notification = new pushNotification(new notificationData(text,name,matchID,notCurrentUser,"messageAppointment","","" ), userToken);
                                                                            sendNotif(notification,"","");
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

    private void sendNotif(pushNotification notification,String from,String notificationFor) {

        ApiUtilities.getClient().sendNotification(notification).enqueue(new Callback<pushNotification>() {
            @Override
            public void onResponse(Call<pushNotification> call, Response<pushNotification> response) {
                if(response.isSuccessful()){
                        if(from.equals("completed")){
                            if(notificationFor.equals("buyer")){
                                Intent i = new Intent(acquired_service_accepted_message.this, service_status.class);
                                i.putExtra("SELECTED_TAB",from);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                Toast.makeText(acquired_service_accepted_message.this, "Appointment successfully moved to completed tab", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                        else if (from.equals("cancelled")){
                            if(notificationFor.equals("buyer")) {
                                Intent i = new Intent(acquired_service_accepted_message.this, service_status.class);
                                i.putExtra("SELECTED_TAB", from);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                Toast.makeText(acquired_service_accepted_message.this, "Appointment successfully moved to cancelled tab", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                        else{
                            //message only
                        }
                }
                else{

                }
            }
            @Override
            public void onFailure(Call<pushNotification> call, Throwable t) {
                Toast.makeText(acquired_service_accepted_message.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getConversation(String matchID) {

        FirebaseFirestore.getInstance().collection("Chat").whereEqualTo("matchID",matchID)
                .whereEqualTo("chatFor","forAppointments")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            return;
                        }
                        if (value!=null) {
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

    private void getReceiverInfo(String id){

        FirebaseFirestore.getInstance().collection("User")
                .document(id)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){

                            DocumentSnapshot s = task.getResult();
                            if(s.getString("fcmToken") !=null){
                                userToken = s.getString("fcmToken");
                            }

                            Picasso.get().load(s.getString("image")).placeholder(R.drawable.noimage)
                                            .into(image);

                            headerName.setText(s.getString("firstName"));

                            if(s.get("online")!=null){

                                if(s.get("online").equals("true")) {
                                    avail.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#5CCD08")));
                                    userStatus.setText("Active now");
                                    available = true;
                                }
                                else {
                                    String formattedTime = TimestampConverter.timestamp((Timestamp) s.get("online"));
                                    avail.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFADB6C4")));
                                    userStatus.setText("Active "+formattedTime);
                                    available = false;
                                }

                            }else{

                            }
                        }
                    }
                });
    }

}
