package com.example.hi_breed.adapter.order_status_for_seller_buyer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.appointment_order_class;
import com.example.hi_breed.order_details_activity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class cancelled_orderAdapter extends RecyclerView.Adapter<cancelled_orderAdapter.ViewHolder> {
        Context context;
    private List<appointment_order_class> list;
    private String userID;
    String from;
    public cancelled_orderAdapter(Context context, String userID,String from){
            this.context = context;
            this.from = from;
            this.userID =userID;
            this.list = new ArrayList<>();
    }

    public void clearList(){
        this.list.clear();
        notifyDataSetChanged();
    }

    public void addServiceDisplay(appointment_order_class appointment_order_class){
        list.add(appointment_order_class);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_status_layout_accepted,parent,false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        appointment_order_class productModel = list.get(position);

        holder.text.setText("View");

        if(from.equals("completed")){
            holder.label.setText("Order completed");
        }
        else if(from.equals("cancelled")){
            holder.label.setText("Order cancelled");
        }
        
        if(productModel.getCustomer_id().equals(userID))
        {

            FirebaseFirestore.getInstance().collection("Shop")
                            .document(productModel.getSeller_id())
                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            holder.name.setText(documentSnapshot.getString("shopName"));
                        }
                    });
            FirebaseFirestore.getInstance().collection("Pet")
                            .document(productModel.getItem_id())
                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if(documentSnapshot.get("photos")!=null){
                                List<String> list = (List<String>) documentSnapshot.get("photos");
                                Picasso.get().load(list.get(0)).placeholder(R.drawable.noimage).into(holder.imageRecycler);

                                holder.breed.setText(documentSnapshot.getString("pet_breed"));
                                holder.gender.setText(documentSnapshot.getString("pet_gender"));
                            }
                        }
                    });
        }
        else if(productModel.getSeller_id().equals(userID)){
            holder.genderImage.setImageResource(R.drawable.icon_call);
            holder.breedImage.setImageResource(R.drawable.ic_address);


            FirebaseFirestore.getInstance().collection("User")
                    .document(productModel.getCustomer_id()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()) {
                                Picasso.get().load(documentSnapshot.getString("image")).placeholder(R.drawable.noimage)
                                        .into(holder.imageRecycler);

                                holder.name.setText(documentSnapshot.getString("firstName") + " "
                                        + documentSnapshot.getString("middleName") + " " +
                                        documentSnapshot.getString("lastName"));
                                holder.breed.setText(documentSnapshot.getString("address"));

                                FirebaseFirestore.getInstance().collection("User")
                                        .document(productModel.getCustomer_id())
                                        .collection("security")
                                        .document("security_doc")
                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    if(documentSnapshot.exists()){
                                                        holder.gender.setText(documentSnapshot.getString("contactNumber"));
                                                    }
                                            }
                                        });
                            }
                        }
                    });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, order_details_activity.class);
                intent.putExtra("mode", (Serializable) productModel);
                context.startActivity(intent);
            }
        });

        holder.buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, order_details_activity.class);
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
        TextView  gender;
        TextView  name,breed,text;
        TextView label;
        LinearLayout  buttonView ;
        ImageView personImage,
        breedImage, genderImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            personImage= itemView.findViewById(R.id.personImage);
            genderImage= itemView.findViewById(R.id.genderImage);
            breedImage= itemView.findViewById(R.id.breedImage);

            buttonView = itemView.findViewById(R.id.buttonView);
            breed = itemView.findViewById(R.id.breed);
            label = itemView.findViewById(R.id.label);
            imageRecycler = itemView.findViewById(R.id.imageRecycler);
            name = itemView.findViewById(R.id.name);
            gender = itemView.findViewById(R.id.gender);
            text = itemView.findViewById(R.id.text);

        }
    }
}
