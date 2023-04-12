package com.example.hi_breed.adapter.service_status_for_seller_buyer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.appointment_class;
import com.example.hi_breed.classesFile.appointment_dating_class;
import com.example.hi_breed.details.acquired_service_details;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class cancelled_serviceAdapter extends RecyclerView.Adapter<cancelled_serviceAdapter.ViewHolder> {

    Context context;
    private List<Object> list;
    private String userID;
    private String from;
    public cancelled_serviceAdapter(Context context, String userID,String from){
            this.context = context;
            this.from = from;
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
    public void dateList(appointment_dating_class appointment_class){
        list.add(appointment_class);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_status_layout_cancelled,parent,false);

        return new ViewHolder(view);
    }
    String names="";
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Object obj = list.get(position);
        if (obj instanceof appointment_class) {
            appointment_class productModel = (appointment_class) list.get(position);
            if(from.equals("cancelled")){
                holder.label.setText("Cancelled appointment");
            }
            else if (from.equals("completed")){
                holder.label.setText("Completed appointment");
            }
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
                    Intent intent = new Intent(context, acquired_service_details.class);
                    intent.putExtra("mode", (Serializable) productModel);
                    context.startActivity(intent);
                }
            });

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, acquired_service_details.class);
                    intent.putExtra("from","petDating");
                    intent.putExtra("mode", (Serializable) productModel);
                    context.startActivity(intent);
                }
            });
        }
        else if(obj instanceof appointment_dating_class){
            appointment_dating_class dating_class = (appointment_dating_class) list.get(position);
            if(dating_class!=null) {
                if (from.equals("cancelled")) {
                    holder.label.setText("Cancelled appointment");
                }
                else if (from.equals("completed")) {
                    holder.label.setText("Completed appointment");
                }
                if (dating_class.getCustomer_id().contains(userID)) {
                    FirebaseFirestore.getInstance().collection("Services")
                            .document(dating_class.getService_id())
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    if (documentSnapshot.get("photos") != null) {
                                        List<String> list = (List<String>) documentSnapshot.get("photos");
                                        Picasso.get().load(list.get(0)).placeholder(R.drawable.noimage).into(holder.imageRecycler);
                                        holder.nameRecycler.setText(documentSnapshot.getString("name"));
                                        FirebaseFirestore.getInstance().collection("User").document(dating_class.getSeller_id()).collection("security")
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
                else {
                    List<Bitmap> bitmaps = new ArrayList<>();
                    if (dating_class.getCustomer_id() != null) {
                        String separator = " & ";
                        for (int i = 0; i < dating_class.getCustomer_id().size(); i++) {
                            int finalI = i;
                            Log.d("Number",String.valueOf(finalI));
                            FirebaseFirestore.getInstance().collection("User").document(dating_class.getCustomer_id().get(i))
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot snapshot = task.getResult();
                                                if(snapshot.exists()){
                                                    String imageUrl = snapshot.getString("image");
                                                    if (imageUrl != null) {
                                                        Picasso.get().load(imageUrl).into(new Target() {
                                                            @Override
                                                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                                bitmaps.add(bitmap);
                                                                if (bitmaps.size() == 2) {
                                                                    // Merge the two bitmaps and set the merged bitmap to your ImageView
                                                                    Bitmap mergedBitmap = mergeBitmaps(bitmaps.get(0), bitmaps.get(1));
                                                                    holder.imageRecycler.setImageBitmap(mergedBitmap);
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
                                                    String name = snapshot.getString("firstName");
                                                    if (names.isEmpty()) {
                                                        names += name;
                                                    } else {
                                                        names += separator + name;
                                                    }
                                                    holder.nameRecycler.setText(names);
                                                    holder.numberRecycler.setText("Pet dating");
                                                    holder.callImage.setImageResource(R.drawable.icon_dog_breed);
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                }
                holder.descriptionRecycler.setText(dating_class.getAppointment_date()+" @ "+dating_class.getAppointment_time());
                holder.messageID.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, acquired_service_details.class);
                        intent.putExtra("mode", (Serializable) dating_class);
                        intent.putExtra("from","petDating");
                        context.startActivity(intent);
                    }
                });
                holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, acquired_service_details.class);
                        intent.putExtra("mode", (Serializable) dating_class);
                        intent.putExtra("from","petDating");
                        context.startActivity(intent);
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
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageRecycler,callImage;
        TextView  nameRecycler;
        TextView  descriptionRecycler,numberRecycler;
        TextView button_contact;
        TextView label;
        RelativeLayout relativeLayout;
        LinearLayout messageID;

        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            callImage = itemView.findViewById(R.id.callImage);
            cardView = itemView.findViewById(R.id.card);
            messageID = itemView.findViewById(R.id.cancelled_id);
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
