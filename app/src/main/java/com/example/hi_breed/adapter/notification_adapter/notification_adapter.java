package com.example.hi_breed.adapter.notification_adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.TimeStampClass;
import com.example.hi_breed.classesFile.add_to_cart_class;
import com.example.hi_breed.classesFile.appointment_class;
import com.example.hi_breed.classesFile.appointment_order_class;
import com.example.hi_breed.classesFile.notification_data_class;
import com.example.hi_breed.details.acquired_service_details;
import com.example.hi_breed.details.order_details_activity;
import com.example.hi_breed.message.message_activity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class notification_adapter extends RecyclerView.Adapter<notification_adapter.ViewHolder> {
    private String id;
    private Context context;
    private List<notification_data_class> notification;
    private List<String> matchID;
    private String user;
    private ItemCheckedChangeListener listener;

    public notification_adapter(Context context, ItemCheckedChangeListener listener,String user){
        this.context = context;
        this.notification = new ArrayList<>();
        this.matchID = new ArrayList<>();
        this.user = user;
        this.listener = listener;
    }

    public void clearList(){
        this.notification.clear();
        this.matchID.clear();
        notifyDataSetChanged();
    }
    public void add(List<notification_data_class> cart,String id){
        this.notification = cart;
        this.id = id;
        this.matchID.add(id);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_layout,parent,false);

        return new ViewHolder(view,listener);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder v, int position) {
        notification_data_class add = notification.get(position);

        if(add!=null){
            v.notificationTitle.setText(add.getTitle());
            v.notificationMessage.setText(add.getMessage());
            Instant instant = Instant.ofEpochMilli(add.getTimestamp().toDate().getTime());
            TimeStampClass timeStamp = new TimeStampClass();
            v.time.setText(timeStamp.getRelativeTime(instant));

            if(add.getType().equals("appointment")){


                FirebaseFirestore.getInstance().collection("Appointments").document(add.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        FirebaseFirestore.getInstance().collection("Services").document(documentSnapshot.getString("service_id"))
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        List<String> list = (List<String>) documentSnapshot.get("photos");
                                        if(list.size()!=0){
                                            Picasso.get().load(list.get(0)).placeholder(R.drawable.noimage)
                                                    .into(v.imageView);
                                        }
                                    }
                                });
                    }
                });
            }
            else if(add.getType().equals("matchDating")){

                FirebaseFirestore.getInstance().collection("Matches")
                        . document(add.getId())
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                if(documentSnapshot.exists()){
                                    List<String> list = (List<String>) documentSnapshot.get("participants");

                                    if(list!=null) {
                                        if (!list.get(0).equals(user)) {
                                            FirebaseFirestore.getInstance().collection("User")
                                                    .document(list.get(0))
                                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                            if (documentSnapshot.exists()) {
                                                                Picasso.get().load(documentSnapshot.getString("image"))
                                                                        .placeholder(R.drawable.noimage)
                                                                        .into(v.imageView);
                                                            }
                                                        }
                                                    });
                                        }
                                        else {
                                            FirebaseFirestore.getInstance().collection("User")
                                                    .document(list.get(1))
                                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                            if (documentSnapshot.exists()) {
                                                                Picasso.get().load(documentSnapshot.getString("image"))
                                                                        .placeholder(R.drawable.noimage)
                                                                        .into(v.imageView);
                                                            }
                                                        }
                                                    });

                                        }
                                    }
                                }

                            }
                        });

            }
            else if(add.getType().equals("order")){
                FirebaseFirestore.getInstance().collection("Appointments")
                        .document(add.getId())
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists()){
                                    if(user.equals(documentSnapshot.getString("seller_id"))){
                                            FirebaseFirestore.getInstance().collection("User")
                                                    .document(documentSnapshot.getString("customer_id"))
                                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                            if(documentSnapshot.exists()){
                                                                Picasso.get().load(documentSnapshot.getString("image"))
                                                                        .placeholder(R.drawable.noimage)
                                                                        .into(v.imageView);
                                                            }
                                                        }
                                                    });
                                    }
                                    else if(user.equals(documentSnapshot.getString("customer_id"))){

                                            FirebaseFirestore.getInstance().collection("Appointments")
                                                    .document(add.getId())
                                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                            if(documentSnapshot.exists()){
                                                                FirebaseFirestore.getInstance().collection("Pet")
                                                                        .document(documentSnapshot.getString("item_id"))
                                                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                     if(documentSnapshot.exists()){
                                                                                         List<String> list = (List<String>) documentSnapshot.get("photos");
                                                                                         if(list!=null)
                                                                                             Picasso.get().load(list.get(0))
                                                                                                     .placeholder(R.drawable.noimage)
                                                                                                     .into(v.imageView);
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


            }
        }

        v.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(add.getType().equals("appointment")){
                    FirebaseFirestore.getInstance().collection("Appointments")
                            .document(add.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                   appointment_class appointment = documentSnapshot.toObject(appointment_class.class);
                                    Intent i = new Intent(context.getApplicationContext(), acquired_service_details.class);
                                    i.putExtra("mode",(Serializable) appointment);
                                    context.startActivity(i);
                                }
                            });
                }
                else if(add.getType().equals("matchDating")){
                    Intent i = new Intent(context.getApplicationContext(), message_activity.class);
                    context.startActivity(i);
                }
                else if(add.getType().equals("order")){
                    FirebaseFirestore.getInstance().collection("Appointments")
                            .document(add.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    appointment_order_class appointment = documentSnapshot.toObject(appointment_order_class.class);
                                    Intent i = new Intent(context.getApplicationContext(), order_details_activity.class);
                                    i.putExtra("mode",(Serializable) appointment);
                                    context.startActivity(i);
                                }
                            });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notification.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ItemCheckedChangeListener onItemCheckedChanged;
        ImageView imageView
                ,menu;
        TextView notificationTitle;
        TextView notificationMessage;
        TextView time;
        public ViewHolder(@NonNull View v,  ItemCheckedChangeListener onItemCheckedChanged) {
            super(v);
            imageView = v.findViewById(R.id.imageView);
            menu = v.findViewById(R.id.menu);
            notificationTitle = v.findViewById(R.id.notificationTitle);
            notificationMessage = v.findViewById(R.id.notificationMessage);
            time = v.findViewById(R.id.time);

           this.onItemCheckedChanged = onItemCheckedChanged;

        }
    }

    public interface ItemCheckedChangeListener {
        void onItemCheckedChanged(add_to_cart_class item, boolean isChecked);
    }
}
