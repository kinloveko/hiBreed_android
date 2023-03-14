package com.example.hi_breed.message;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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
import com.example.hi_breed.classesFile.chat_conversation_class;
import com.example.hi_breed.classesFile.matches_class;
import com.example.hi_breed.classesFile.notificationData;
import com.example.hi_breed.classesFile.pushNotification;
import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class message_conversation_activity extends BaseActivity {
    LinearLayout backLayoutService;
    private   TextView headerName;
    private   RelativeLayout toolbarID;
    private   CircleImageView image;
    private RecyclerView chat_recyclerview;
    private ImageView sendReplyImageView;
    private TextView userStatus;
    private TextInputEditText replyEdit;
    ImageView findShooter;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_conversation);


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

        findShooter = findViewById(R.id.findShooter);
        findShooter.setOnClickListener(new View.OnClickListener() {
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
                message_conversation_activity.this.onBackPressed();
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
        notCurrentUser = intent.getStringExtra("notCurrentUser");
        getConversation(m.getMatchID());
        match = m.getMatchID();
        getCurrent();
        getReceiverInfo(notCurrentUser);
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
                                                    if(!available){
                                                       pushNotification notification = new pushNotification(new notificationData(text,name,matchID,notCurrentUser,"message",null),userToken);
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
                                                                        if(!available){
                                                                            pushNotification notification = new pushNotification(new notificationData(text,name,matchID,notCurrentUser,"message",null), userToken);
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
                if(response.isSuccessful()){

                }
                else{

                 }
            }

            @Override
            public void onFailure(Call<pushNotification> call, Throwable t) {
                Toast.makeText(message_conversation_activity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getConversation(String matchID) {

        FirebaseFirestore.getInstance().collection("Chat").whereEqualTo("matchID",matchID)
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

                            Glide.with( message_conversation_activity.this)
                                    .load(s.getString("image"))
                                    .placeholder(R.drawable.noimage)
                                    .error(R.drawable.screen_alert_image_error_border)
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