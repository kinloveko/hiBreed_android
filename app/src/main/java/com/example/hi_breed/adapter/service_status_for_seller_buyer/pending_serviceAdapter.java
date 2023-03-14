package com.example.hi_breed.adapter.service_status_for_seller_buyer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hi_breed.R;
import com.example.hi_breed.acquired_service_details;
import com.example.hi_breed.classesFile.ApiUtilities;
import com.example.hi_breed.classesFile.appointment_class;
import com.example.hi_breed.classesFile.notificationData;
import com.example.hi_breed.classesFile.pushNotification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class pending_serviceAdapter extends RecyclerView.Adapter<pending_serviceAdapter.ViewHolder> {
        Context context;
    private List<appointment_class> list;
    private String userID;
    public pending_serviceAdapter(Context context,String userID){
            this.context = context;
            this.userID =userID;
            this.list = new ArrayList<>();
    }

    public void clearList(){
        this.list.clear();
        notifyDataSetChanged();
    }

    public void addServiceDisplay(appointment_class appointment_class){
        list.add(appointment_class);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_status_layout_pending,parent,false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        appointment_class productModel = list.get(position);

        if(productModel.getCustomer_id().equals(userID))
        {

            holder.buttonLayout.setVisibility(View.GONE);

            FirebaseFirestore.getInstance().collection("Services")
                            .document(productModel.getService_id())
                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if(documentSnapshot.get("photos")!=null){
                                List<String> list = (List<String>) documentSnapshot.get("photos");
                                Picasso.get().load(list.get(0)).placeholder(R.drawable.noimage).into(holder.imageRecycler);
                                holder.nameRecycler.setText(documentSnapshot.getString("name"));
                                FirebaseFirestore.getInstance().collection("User").document(productModel.getSeller_id()).collection("security")
                                        .document("security_doc")
                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                holder.numberRecycler.setText(documentSnapshot.getString("contactNumber"));
                                            }
                                        });
                            }
                        }
                    });
        }
        else if(productModel.getSeller_id().equals(userID)){

            holder.buttonLayout.setVisibility(View.VISIBLE);
            DocumentReference doc=
            FirebaseFirestore.getInstance().collection("User").document(productModel.getCustomer_id());

                    doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {

                                DocumentSnapshot snapshot = task.getResult();
                                Picasso.get().load(snapshot.getString("image")).placeholder(R.drawable.noimage).into(holder.imageRecycler);

                                holder.nameRecycler.setText(snapshot.getString("firstName") + " " + snapshot.getString("middleName")
                                        + " " + snapshot.getString("lastName"));
                                doc.collection("security")
                                        .document("security_doc")
                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                holder.numberRecycler.setText(documentSnapshot.getString("contactNumber"));
                                            }
                                        });
                            }
                        }
                    });
        }

        holder.descriptionRecycler.setText(productModel.getAppointment_date()+" @ "+productModel.getAppointment_time());

        holder.buttonContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection("User")
                                .document(productModel.getCustomer_id()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                if(error!=null){
                                    return;
                                }
                                if(value.exists()){
                                    String token= value.getString("fcmToken");
                                    FirebaseFirestore.getInstance().collection("Appointments")
                                            .document(productModel.getId())
                                            .update("appointment_status","accepted")
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    pushNotification notification = new pushNotification(new notificationData("Your request appointment has been accepted",
                                                            "Accepted appointment",productModel.getId(),productModel.getCustomer_id(),"appointment","buyer"), token);
                                                    sendNotif(notification,"accepted");
                                                }
                                            });
                                }
                            }
                        });
            }
        });

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection("User")
                        .document(productModel.getCustomer_id()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                if(error!=null){
                                    return;
                                }
                                if(value.exists()){
                                    String token= value.getString("fcmToken");
                                    FirebaseFirestore.getInstance().collection("Appointments")
                                            .document(productModel.getId())
                                            .update("appointment_status","cancelled")
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    pushNotification notification = new pushNotification(new notificationData("Your request appointment has been cancelled",
                                                            "Cancelled appointment",productModel.getId(),productModel.getCustomer_id(),"appointment","buyer"), token);
                                                    sendNotif(notification,"cancelled");
                                                }
                                            });
                                }
                            }
                        });
            }
        });

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, acquired_service_details.class);
                intent.putExtra("mode", (Serializable) productModel);
                context.startActivity(intent);
            }
        });
    }

    private void sendNotif(pushNotification notification,String from) {
        ApiUtilities.getClient().sendNotification(notification).enqueue(new Callback<pushNotification>() {
            @Override
            public void onResponse(Call<pushNotification> call, Response<pushNotification> response) {
                if(response.isSuccessful()){
                    if(from.equals("accepted")){
                        Toast.makeText(context, "Appointment successfully moved to accepted tab", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(context, "Appointment successfully moved to cancelled tab", Toast.LENGTH_SHORT).show();
                    }
                }else{

                }
            }

            @Override
            public void onFailure(Call<pushNotification> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageRecycler;
        TextView  nameRecycler;
        TextView  descriptionRecycler,numberRecycler;
        TextView button_contact;
        TextView label;
        RelativeLayout relativeLayout;
        LinearLayout buttonLayout,buttonContact,buttonDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            buttonContact = itemView.findViewById(R.id.buttonContact);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonLayout = itemView.findViewById(R.id.buttonLayout);
            numberRecycler = itemView.findViewById(R.id.numberRecycler);
            label = itemView.findViewById(R.id.label);
            imageRecycler = itemView.findViewById(R.id.imageRecycler);
            nameRecycler = itemView.findViewById(R.id.nameRecycler);
            descriptionRecycler = itemView.findViewById(R.id.descriptionRecycler);
            button_contact = itemView.findViewById(R.id.button_contact);

        }
    }
}
