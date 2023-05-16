package com.example.hi_breed.adapter.edit_pet_for_sale_adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hi_breed.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class pet_image_check_current extends RecyclerView.Adapter<pet_image_check_current.ViewHolder> {

    private final Context context;
    private final ArrayList <Uri> uriArrayLists;
    CountOfImagesCurrent countCurrent;
    private final itemCurrentClickListenerPet itemCurrentClick;
    String id;
    String breederID;

    public pet_image_check_current(ArrayList<Uri> uriArrayList, Context context, CountOfImagesCurrent countCurrent
          , itemCurrentClickListenerPet itemCurrentClick, String id, String breederID){
        this.uriArrayLists = uriArrayList;
        this.context = context;
        this.countCurrent = countCurrent;
        this.itemCurrentClick = itemCurrentClick;
        this.id = id;
        this.breederID = breederID;
    }


    @NonNull
    @Override
    public pet_image_check_current.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater  = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_single_image,parent,false);

        return new ViewHolder(view, countCurrent,itemCurrentClick);
    }

    @Override
    public void onBindViewHolder(@NonNull pet_image_check_current.ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        Glide.with(holder.itemView.getContext())
                .load(uriArrayLists.get(position))
                .placeholder(R.drawable.noimage)
                .error(R.drawable.screen_alert_image_error_border)
                .into(holder.imageView);
        holder.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uriArrayLists.size() == 1){
                    Toast.makeText(context, "Cannot be deleted image can't be empty", Toast.LENGTH_SHORT).show();
                }else{
                    FirebaseFirestore.getInstance().collection("Pet").document(id)
                            .update("photos", FieldValue.arrayRemove(uriArrayLists.get(position)))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        FirebaseFirestore.getInstance().collection("Shop").document(breederID)
                                                .collection("Pet").document(id)
                                                .update("photos",FieldValue.arrayRemove(uriArrayLists.get(position)))
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            FirebaseFirestore.getInstance().collection("User")
                                                                    .document(breederID)
                                                                    .collection("Pet")
                                                                    .document(id)
                                                                    .update("photos",FieldValue.arrayRemove(uriArrayLists.get(position)))
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if(task.isSuccessful()){
                                                                                StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(uriArrayLists.get(position).toString());
                                                                                imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void unused) {
                                                                                        uriArrayLists.remove(uriArrayLists.get(position));
                                                                                        notifyItemRemoved(position);
                                                                                        notifyItemRangeChanged(position,getItemCount());
                                                                                        countCurrent.clickCurrent(uriArrayLists.size());
                                                                                        Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show();
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
                            });

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return uriArrayLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        CardView deleteImage;
        itemCurrentClickListenerPet itemCurrentClick;
        CountOfImagesCurrent countCurrent;
        public ViewHolder(@NonNull View itemView, CountOfImagesCurrent countCurrent
                , itemCurrentClickListenerPet itemCurrentClick) {
            super(itemView);
            this.countCurrent = countCurrent;

            imageView = itemView.findViewById(R.id.dropImageViewGallery);
            deleteImage = itemView.findViewById(R.id.deleteCardView);
            this.itemCurrentClick = itemCurrentClick;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if(itemCurrentClick !=null){
                itemCurrentClick.itemCurrentClick(getAdapterPosition());
            }
        }
    }

    public interface CountOfImagesCurrent{
        void clickCurrent(int size);
    }

    public interface itemCurrentClickListenerPet{
        void itemCurrentClick(int position);
    }
}
