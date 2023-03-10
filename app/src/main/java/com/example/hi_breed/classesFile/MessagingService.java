package com.example.hi_breed.classesFile;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.hi_breed.R;
import com.example.hi_breed.message.message_conversation_activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    String CHANNEL_ID = "Chat";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);


    }
    List<String> participants = new ArrayList<>();
    Timestamp timestamp;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        String sender = remoteMessage.getData().get("title");
        String text =  remoteMessage.getData().get("message");
        String match = remoteMessage.getData().get("matchID");
        String notCurrentUser = remoteMessage.getData().get("notCurrentUser");

        FirebaseFirestore.getInstance().collection("Matches")
                .document(match).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot s = task.getResult();
                                if(s.exists()){
                                    List<String> user = (List<String>) s.get("participants");
                                    participants.addAll(user);
                                    Timestamp time = (Timestamp) s.get("time");
                                    timestamp = time;
                                }
                            }
                    }
                });

        int notificationId = (int) System.currentTimeMillis();
        matches_class m = new matches_class(participants,match,true,timestamp);
        Intent intent = new Intent(this, message_conversation_activity.class);
        intent.putExtra("model",(Serializable) m);
        intent.putExtra("notCurrentUser",notCurrentUser);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,new Intent[]{intent}, PendingIntent.FLAG_MUTABLE);


        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            createNotificationManager(manager);

        Notification.Builder builder = new Notification.Builder(MessagingService.this, CHANNEL_ID)
                .setSmallIcon(R.drawable.hibreedlogo)
                .setContentTitle("New message from: "+sender)
                .setContentText(text)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setGroup(match);

        Notification notification = builder.build();
        manager.notify(notificationId, notification);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationManager(NotificationManager manager) {
        NotificationChannel channel;
            channel = new NotificationChannel(CHANNEL_ID,"Chat", NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("You have notification!");
            channel.enableLights(true);
            channel.setLightColor(Color.WHITE);
        manager.createNotificationChannel(channel);

    }
}


