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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hi_breed.R;
import com.example.hi_breed.adapter.message_adapter.message_conversation_reply_adapter;
import com.example.hi_breed.classesFile.ApiUtilities;
import com.example.hi_breed.classesFile.BaseActivity;
import com.example.hi_breed.classesFile.TimestampConverter;
import com.example.hi_breed.classesFile.appointment_dating_class;
import com.example.hi_breed.classesFile.chat_conversation_class;
import com.example.hi_breed.classesFile.matches_class;
import com.example.hi_breed.classesFile.notificationData;
import com.example.hi_breed.classesFile.pushNotification;
import com.example.hi_breed.details.acquired_service_details;
import com.example.hi_breed.service.findShooters;
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

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class message_conversation_activity extends BaseActivity {

    LinearLayout backLayoutService;
    private TextView headerName;
    private RelativeLayout toolbarID;
    private CircleImageView image;
    private RecyclerView chat_recyclerview;
    private ImageView sendReplyImageView;
    private TextView userStatus;
    private TextInputEditText replyEdit;
    ImageView findShooter, dots_menu;
    String notCurrentUser;
    RelativeLayout reply_layout;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_conversation);


        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.setStatusBarColor(Color.parseColor("#e28743"));
        }
        reply_layout = findViewById(R.id.reply_layout);
        findShooter = findViewById(R.id.findShooter);
        dots_menu = findViewById(R.id.dots_menu);

        userStatus = findViewById(R.id.userStatus);
        avail = findViewById(R.id.avail);
        scrollView = findViewById(R.id.scrollView);
        backLayoutService = findViewById(R.id.backLayoutService);
        backLayoutService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message_conversation_activity.this.onBackPressed();
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

        if (m.getParticipants().size() != 0) {
            notCurrentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
            if (!notCurrentUser.equals(m.getParticipants().get(0))) {
                notCurrentUser = m.getParticipants().get(0);
                getReceiverInfo(notCurrentUser);
            } else if (!notCurrentUser.equals(m.getParticipants().get(1))) {
                notCurrentUser = m.getParticipants().get(1);
                getReceiverInfo(notCurrentUser);
            }
        } else {
            FirebaseFirestore.getInstance().collection("Matches").document(m.getMatchID())
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            List<String> list = (List<String>) documentSnapshot.get("participants");
                            if (list != null) {
                                if (!list.get(0).equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                                    notCurrentUser = list.get(0);
                                    getReceiverInfo(notCurrentUser);
                                } else {
                                    notCurrentUser = list.get(1);
                                    getReceiverInfo(notCurrentUser);
                                }
                            }
                        }
                    });
        }


        getConversation(m.getMatchID());
        match = m.getMatchID();

        getCurrent();

        //check if the user is in a transaction if not they can leave the conversation
        FirebaseFirestore.getInstance().collection("Matches")
                .document(match).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) return;
                        if (value.exists()) {
                            // this statement is to check if this conversation has a transaction in a shooter
                            //if not they can leave the conversation
                            if (value.getString("status") != null) {
                                dots_menu.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(message_conversation_activity.this, "You cannot leave unless the transaction is completed", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } else {
                                dots_menu.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        openDialog(match);
                                    }
                                });

                            }
                        }
                    }
                });

        FirebaseFirestore.getInstance().collection("Matches")
                .document(match)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) return;

                        if (value.exists()) {
                            if (value.getString("status") != null) {
                                findShooter.setImageResource(R.drawable.bag);
                                findShooter.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        FirebaseFirestore.getInstance().collection("Appointments")
                                                .document(m.getAppointment_id()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        if (documentSnapshot.exists()) {
                                                            appointment_dating_class appoint = documentSnapshot.toObject(appointment_dating_class.class);
                                                            Intent i = new Intent(message_conversation_activity.this, acquired_service_details.class);
                                                            i.putExtra("mode", (Serializable) appoint);
                                                            i.putExtra("from", "petDating");
                                                            startActivity(i);
                                                        }
                                                    }
                                                });

                                    }
                                });
                            } else {
                                findShooter.setImageResource(R.drawable.find_shooter);
                                findShooter.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent i = new Intent(message_conversation_activity.this, findShooters.class);
                                        i.putExtra("matchID", match);
                                        startActivity(i);
                                    }
                                });
                            }
                        }
                    }
                });


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

                sendMessage(replyEdit.getText().toString(), m.getMatchID(), notCurrentUser);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void openDialog(String matchID) {


        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.messaging_leave_layout, null);

        RelativeLayout completedButton, cancelButton;
        completedButton = view.findViewById(R.id.oneButton);
        cancelButton = view.findViewById(R.id.twoButton);

        builder2.setView(view);
        AlertDialog alert2 = builder2.create();
        alert2.show();
        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        completedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // in this query i add a field in my Matches -> specificMatchID (matchID) -> leave_participants
                FirebaseFirestore.getInstance().collection("Matches")
                        .document(matchID).update("leave_participants",
                                FieldValue.arrayUnion(FirebaseAuth.getInstance().getCurrentUser().getUid())).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                // in this query i add a field in my Chat -> specificMatchID (matchID) -> leave_participants
                                FirebaseFirestore.getInstance().collection("Chat")
                                        .document(matchID).update("leave_participants",
                                                FieldValue.arrayUnion(FirebaseAuth.getInstance().getCurrentUser().getUid())).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                alert2.dismiss();
                                                AlertDialog.Builder builder2 = new AlertDialog.Builder(message_conversation_activity.this);
                                                View view = View.inflate(message_conversation_activity.this, R.layout.messaging_leave_sucess_dialog, null);

                                                RelativeLayout oneButton;
                                                oneButton = view.findViewById(R.id.oneButton);

                                                builder2.setView(view);
                                                AlertDialog alert2 = builder2.create();
                                                alert2.show();
                                                alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                                oneButton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        startActivity(new Intent(message_conversation_activity.this, message_activity.class));
                                                        finish();
                                                        alert2.dismiss();

                                                    }
                                                });
                                            }
                                        });
                            }
                        });
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert2.dismiss();
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

    private void sendMessage(String text, String matchID, String notCurrentUser) {

        FieldValue serverTimestamp = FieldValue.serverTimestamp();
        Map<String, Object> latestMessage = new HashMap<>();
        latestMessage.put("sender", user.getUid());
        latestMessage.put("text", text);
        latestMessage.put("timestamp", Timestamp.now());

        Map<String, Object> conversation = new HashMap<>();
        conversation.put("participants", Arrays.asList(user.getUid(), notCurrentUser));
        conversation.put("latestMessage", latestMessage);
        conversation.put("messages", Arrays.asList(latestMessage));
        conversation.put("matchID", matchID);
        conversation.put("chatFor", "forDating");

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
                                                    if (!available) {
                                                        pushNotification notification = new pushNotification(new notificationData(text, name, matchID, notCurrentUser, "message", null, "pending"), userToken);
                                                        sendNotif(notification);
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
                                                                        if (!available) {
                                                                            pushNotification notification = new pushNotification(new notificationData(text, name, matchID, notCurrentUser, "message", null, "pending"), userToken);
                                                                            sendNotif(notification);
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
                                            }
                                        });
                            }
                        }
                    }
                });
        replyEdit.getText().clear();
    }


    private void sendNotif(pushNotification notification) {
        ApiUtilities.getClient().sendNotification(notification).enqueue(new Callback<pushNotification>() {
            @Override
            public void onResponse(Call<pushNotification> call, Response<pushNotification> response) {
                if (response.isSuccessful()) {

                } else {

                }
            }

            @Override
            public void onFailure(Call<pushNotification> call, Throwable t) {
                Toast.makeText(message_conversation_activity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getConversation(String matchID) {

        FirebaseFirestore.getInstance().collection("Chat").whereEqualTo("matchID", matchID)
                .whereEqualTo("chatFor", "forDating")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }
                        if (value != null) {
                            for (DocumentSnapshot s : value) {
                                if (s.get("leave_conversation") != null) {
                                    reply_layout.setVisibility(View.GONE);
                                }
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

    private void getReceiverInfo(String id) {

        FirebaseFirestore.getInstance().collection("User")
                .document(id)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            DocumentSnapshot s = task.getResult();
                            if (s.getString("fcmToken") != null) {
                                userToken = s.getString("fcmToken");
                            }

                            Glide.with(message_conversation_activity.this)
                                    .load(s.getString("image"))
                                    .placeholder(R.drawable.noimage)
                                    .error(R.drawable.screen_alert_image_error_border)
                                    .into(image);
                            headerName.setText(s.getString("firstName"));

                            FirebaseFirestore.getInstance().collection("Matches")
                                    .document(match)
                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                List<String> list = (List<String>) documentSnapshot.get("leave_participants");
                                                if (list != null) {
                                                    if (list.contains(id)) {
                                                        findShooter.setVisibility(View.GONE);
                                                        reply_layout.setVisibility(View.GONE);
                                                        userStatus.setText("Leave the conversation");
                                                        RelativeLayout leave_conversation = findViewById(R.id.leave_conversation);
                                                        ScrollView scrollView = findViewById(R.id.scrollView);
                                                        RelativeLayout.LayoutParams scrollLayoutParams = new RelativeLayout.LayoutParams(
                                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                                ViewGroup.LayoutParams.WRAP_CONTENT
                                                        );

                                                        scrollLayoutParams.addRule(RelativeLayout.ABOVE, R.id.leave_conversation);
                                                        scrollView.setLayoutParams(scrollLayoutParams);
                                                        TextView leaveText = findViewById(R.id.leaveText);
                                                        leave_conversation.setVisibility(View.VISIBLE);
                                                        leaveText.setText("You can't reply to this conversation.\n" +
                                                                s.getString("firstName") + " Leave the conversation.");
                                                    }
                                                } else {
                                                    reply_layout.setVisibility(View.VISIBLE);
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
                                                    //end of if-else for online
                                                }


                                            }
                                        }
                                    });
                        }
                    }
                });

    }
}