package com.example.hi_breed.classesFile;

import android.annotation.SuppressLint;
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
import com.example.hi_breed.message.acquired_service_accepted_message;
import com.example.hi_breed.message.message_activity;
import com.example.hi_breed.message.message_conversation_activity;
import com.example.hi_breed.order_breeder_side.order_breeder_side;
import com.example.hi_breed.order_user_side.order_user_side;
import com.example.hi_breed.service_status_for_buyer.appointment_user_side;
import com.example.hi_breed.service_status_for_seller.service_status;
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
import java.util.Random;

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
        String notificationFor = remoteMessage.getData().get("notificationFor");
        String SELECTED_TAB = remoteMessage.getData().get("SELECTED_TAB");

        // it's either appointment,transaction,message
        String type = remoteMessage.getData().get("type");

        Log.d("MyApp", "SELECTED_TAB FROM FIREBASE MESSAGING SERVICE: " + SELECTED_TAB);
        Log.d("MyApp", notCurrentUser);
        Log.d("MyApp", match);
        if(type.equals("appointment")){
            if(notificationFor.equals("seller")){
                int notificationId = (int) (new Random().nextInt() + System.currentTimeMillis());
                Intent intent = new Intent(this, service_status.class);
                intent.putExtra("SELECTED_TAB",(Serializable) SELECTED_TAB);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Log.d("MyApp", "SELECTED_TAB FROM FIREBASE MESSAGING SERVICE Seller: " + SELECTED_TAB);

                PendingIntent pendingIntent;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    pendingIntent = PendingIntent.getActivities(getApplicationContext(), 0, new Intent[]{intent}, PendingIntent.FLAG_MUTABLE);
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    createNotificationManagerAppointments(manager);

                    Notification.Builder builder = new Notification.Builder(MessagingService.this, "Appointment")
                            .setSmallIcon(R.drawable.hibreedlogo)
                            .setContentTitle(sender)
                            .setContentText(text)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .setGroup(match);

                    Notification notification = builder.build();
                    manager.notify(notificationId, notification);
                }
                else{
                    pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    createNotificationManagerAppointments(manager);

                    Notification.Builder builder = new Notification.Builder(MessagingService.this, "Appointment")
                            .setSmallIcon(R.drawable.hibreedlogo)
                            .setContentTitle(sender)
                            .setContentText(text)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .setGroup(match);

                    Notification notification = builder.build();
                    manager.notify(notificationId, notification);
                }

            }
            else{

                int notificationId = (int) (new Random().nextInt() + System.currentTimeMillis());
                Intent intent = new Intent(this, appointment_user_side.class);
                intent.putExtra("SELECTED_TAB",SELECTED_TAB);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Log.d("MyApp", "SELECTED_TAB FROM FIREBASE MESSAGING SERVICE buyer: " + SELECTED_TAB);

                PendingIntent  pendingIntent;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    pendingIntent = PendingIntent.getActivities(getApplicationContext(), 0, new Intent[]{intent}, PendingIntent.FLAG_MUTABLE);
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    createNotificationManagerAppointments(manager);

                    Notification.Builder builder = new Notification.Builder(MessagingService.this, "Appointment")
                            .setSmallIcon(R.drawable.hibreedlogo)
                            .setContentTitle(sender)
                            .setContentText(text)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setAutoCancel(true)
                            .setColor(Color.WHITE)
                            .setContentIntent(pendingIntent)
                            .setGroup(match);

                    Notification notification = builder.build();
                    manager.notify(notificationId, notification);
                }
                else{
                    pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    createNotificationManagerAppointments(manager);

                    Notification.Builder builder = new Notification.Builder(MessagingService.this, "Appointment")
                            .setSmallIcon(R.drawable.hibreedlogo)
                            .setContentTitle(sender)
                            .setContentText(text)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setAutoCancel(true)
                            .setColor(Color.WHITE)
                            .setContentIntent(pendingIntent)
                            .setGroup(match);

                    Notification notification = builder.build();
                    manager.notify(notificationId, notification);
                }
            }

        }
        else if(type.equals("order")){
            if(notificationFor.equals("seller")){
                int notificationId = (int) (new Random().nextInt() + System.currentTimeMillis());
                Intent intent = new Intent(this, order_breeder_side.class);
                intent.putExtra("SELECTED_TAB",(Serializable) SELECTED_TAB);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Log.d("MyApp", "SELECTED_TAB FROM FIREBASE MESSAGING ORDER Seller: " + SELECTED_TAB);

                PendingIntent pendingIntent;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    pendingIntent = PendingIntent.getActivities(getApplicationContext(), 0, new Intent[]{intent}, PendingIntent.FLAG_MUTABLE);
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    createNotificationManagerOrder(manager);

                    Notification.Builder builder = new Notification.Builder(MessagingService.this, "Order")
                            .setSmallIcon(R.drawable.hibreedlogo)
                            .setContentTitle("Order Notification!")
                            .setContentText(text)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .setColor(Color.WHITE)
                            .setGroup(match);

                    Notification notification = builder.build();
                    manager.notify(notificationId, notification);
                }
                else {
                    pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    createNotificationManagerOrder(manager);

                    Notification.Builder builder = new Notification.Builder(MessagingService.this, "Order")
                            .setSmallIcon(R.drawable.hibreedlogo)
                            .setContentTitle("Order Notification!")
                            .setContentText(text)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .setColor(Color.WHITE)
                            .setGroup(match);

                    Notification notification = builder.build();
                    manager.notify(notificationId, notification);
                }

            }
            else{

                int notificationId = (int) (new Random().nextInt() + System.currentTimeMillis());
                Intent intent = new Intent(this, order_user_side.class);
                intent.putExtra("SELECTED_TAB",SELECTED_TAB);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Log.d("MyApp", "SELECTED_TAB FROM FIREBASE MESSAGING SERVICE buyer: " + SELECTED_TAB);
                PendingIntent pendingIntent;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    pendingIntent = PendingIntent.getActivities(getApplicationContext(), 0, new Intent[]{intent}, PendingIntent.FLAG_MUTABLE);
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    createNotificationManagerOrder(manager);

                    Notification.Builder builder = new Notification.Builder(MessagingService.this, "Order")
                            .setSmallIcon(R.drawable.hibreedlogo)
                            .setContentTitle("Order Notification!")
                            .setContentText(text)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .setColor(Color.WHITE)
                            .setGroup(match);

                    Notification notification = builder.build();
                    manager.notify(notificationId, notification);
                }
                else {
                    pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    createNotificationManagerOrder(manager);

                    Notification.Builder builder = new Notification.Builder(MessagingService.this, "Order")
                            .setSmallIcon(R.drawable.hibreedlogo)
                            .setContentTitle("Order Notification!")
                            .setContentText(text)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .setColor(Color.WHITE)
                            .setGroup(match);

                    Notification notification = builder.build();
                    manager.notify(notificationId, notification);
                }


            }

        }
        else if(type.equals("messageAppointment")){

            FirebaseFirestore.getInstance().collection("Matches")
                    .document(match).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot s = task.getResult();
                                if(s.exists()){
                                    List<String> user = (List<String>) s.get("participants");
                                    Timestamp time = (Timestamp) s.get("time");
                                    timestamp = time;
                                    int notificationId = (int) System.currentTimeMillis();
                                    matches_class m = new matches_class(user,match,true,timestamp);
                                    Intent intent = new Intent(getApplicationContext(), acquired_service_accepted_message.class);
                                    intent.putExtra("model",(Serializable) m);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                    PendingIntent pendingIntent;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        pendingIntent = PendingIntent.getActivities(getApplicationContext(), 0, new Intent[]{intent}, PendingIntent.FLAG_MUTABLE);

                                        } else {
                                        pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                     }

                                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    createNotificationManager(manager);

                                    Notification.Builder builder = new Notification.Builder(MessagingService.this, CHANNEL_ID)
                                            .setSmallIcon(R.drawable.hibreedlogo)
                                            .setContentTitle("New message from: "+sender)
                                            .setContentText(text)
                                            .setPriority(Notification.PRIORITY_HIGH)
                                            .setAutoCancel(true)
                                            .setContentIntent(pendingIntent)
                                            .setColor(Color.WHITE)
                                            .setGroup(match);

                                    Notification notification = builder.build();
                                    manager.notify(notificationId, notification);
                                }
                            }
                        }
                    });

        }
        else if(type.equals("message")){

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

            @SuppressLint("InlinedApi") PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,new Intent[]{intent}, PendingIntent.FLAG_MUTABLE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            createNotificationManager(manager);

            Notification.Builder builder = new Notification.Builder(MessagingService.this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.hibreedlogo)
                    .setContentTitle("New message from: "+sender)
                    .setContentText(text)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setColor(Color.WHITE)
                    .setGroup(match);

            Notification notification = builder.build();
            manager.notify(notificationId, notification);

        }
        else if(type.equals("matchNotification")){
            Log.d("MyApp", match);
            int notificationId = (int) System.currentTimeMillis();

            Intent intent = new Intent(this, message_activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                pendingIntent = PendingIntent.getActivities(getApplicationContext(), 0, new Intent[]{intent}, PendingIntent.FLAG_MUTABLE);
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                createNotificationManagerMatches(manager);

                Notification.Builder builder = new Notification.Builder(MessagingService.this, "Matches")
                        .setSmallIcon(R.drawable.hibreedlogo)
                        .setContentTitle("You have a matched!")
                        .setContentText(text)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setColor(Color.WHITE)
                        .setGroup(match);

                Notification notification = builder.build();
                manager.notify(notificationId, notification);
            } else {
                pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                createNotificationManagerMatches(manager);

                Notification.Builder builder = new Notification.Builder(MessagingService.this, "Matches")
                        .setSmallIcon(R.drawable.hibreedlogo)
                        .setContentTitle("You have a matched!")
                        .setContentText(text)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setColor(Color.WHITE)
                        .setGroup(match);

                Notification notification = builder.build();
                manager.notify(notificationId, notification);
            }


        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationManagerOrder(NotificationManager manager) {
        NotificationChannel channel;
        channel = new NotificationChannel("Order","Order", NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("You have a matched!");
        channel.enableLights(true);
        channel.setLightColor(Color.WHITE);
        manager.createNotificationChannel(channel);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationManagerMatches(NotificationManager manager) {
        NotificationChannel channel;
        channel = new NotificationChannel("Matches","Matches", NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("You have a matched!");
        channel.enableLights(true);
        channel.setLightColor(Color.WHITE);
        manager.createNotificationChannel(channel);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationManagerAppointments(NotificationManager manager) {
        NotificationChannel channel;
        channel = new NotificationChannel("Appointment","Appointment", NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("You have notification!");
        channel.enableLights(true);
        channel.setLightColor(Color.WHITE);
        manager.createNotificationChannel(channel);
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


