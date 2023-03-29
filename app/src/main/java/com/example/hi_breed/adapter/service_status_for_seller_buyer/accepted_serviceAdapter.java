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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hi_breed.R;
import com.example.hi_breed.message.acquired_service_accepted_message;
import com.example.hi_breed.details.acquired_service_details;
import com.example.hi_breed.classesFile.appointment_class;
import com.example.hi_breed.classesFile.matches_class;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class accepted_serviceAdapter extends RecyclerView.Adapter<accepted_serviceAdapter.ViewHolder> {

    Context context;
    private List<appointment_class> list;
    private String userID;

    public accepted_serviceAdapter(Context context, String userID){
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_status_layout_accepted,parent,false);

        return new ViewHolder(view);
    }

    private matches_class match;
    String notCurrentUser;
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        appointment_class productModel = list.get(position);

        if(!productModel.getSeller_id().equals(userID)){
            notCurrentUser = productModel.getSeller_id();
        }
        else if(!productModel.getCustomer_id().equals(userID)){
            notCurrentUser = productModel.getCustomer_id();
        }

        FirebaseFirestore.getInstance().collection("Matches").document(productModel.getId())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        match = documentSnapshot.toObject(matches_class.class);
                    }
                });
        if(productModel.getCustomer_id().equals(userID))
        {

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
        holder.messageID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, acquired_service_accepted_message.class);
                intent.putExtra("model", (Serializable) match);
                context.startActivity(intent);
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
        LinearLayout messageID;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageID = itemView.findViewById(R.id.messageID);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);

            numberRecycler = itemView.findViewById(R.id.numberRecycler);
            label = itemView.findViewById(R.id.label);
            imageRecycler = itemView.findViewById(R.id.imageRecycler);
            nameRecycler = itemView.findViewById(R.id.nameRecycler);
            descriptionRecycler = itemView.findViewById(R.id.descriptionRecycler);
            button_contact = itemView.findViewById(R.id.button_contact);
        }
    }
}
