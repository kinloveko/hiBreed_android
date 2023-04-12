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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class pet_papers_check_current extends RecyclerView.Adapter<pet_papers_check_current.ViewHolder> {

    private final Context context;
    private final ArrayList <Uri> uriArrayLists;
    CountOfPapersCurrent countPapersCurrent;
    private final itemPaperClickListenerPet itemPaperClick;
    String id;
    String breederID;

    public pet_papers_check_current(ArrayList<Uri> uriArrayList, Context context, CountOfPapersCurrent countPapersCurrent
          , itemPaperClickListenerPet itemPaperClick, String id, String breederID){
        this.uriArrayLists = uriArrayList;
        this.context = context;
        this.countPapersCurrent = countPapersCurrent;
        this.itemPaperClick = itemPaperClick;
        this.id = id;
        this.breederID = breederID;
    }



    @NonNull
    @Override
    public pet_papers_check_current.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater  = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_single_image,parent,false);

        return new ViewHolder(view, countPapersCurrent,itemPaperClick);
    }

    @Override
    public void onBindViewHolder(@NonNull pet_papers_check_current.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Glide.with(holder.itemView.getContext())
                .load(uriArrayLists.get(position))
                .placeholder(R.drawable.noimage)
                .error(R.drawable.screen_alert_image_error_border)
                .into(holder.imageView);
        holder.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uriArrayLists.size() == 1){
                    Toast.makeText(context, "Cannot be deleted papers can't be empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    FirebaseFirestore.getInstance().collection("Pet").document(id)
                            .update("papers", FieldValue.arrayRemove(uriArrayLists.get(position)))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        FirebaseFirestore.getInstance().collection("Shop").document(breederID)
                                                .collection("Pet").document(id)
                                                .update("papers",FieldValue.arrayRemove(uriArrayLists.get(position)))
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            FirebaseFirestore.getInstance().collection("User")
                                                                    .document(breederID)
                                                                    .collection("Pet")
                                                                    .document(id)
                                                                    .update("papers",FieldValue.arrayRemove(uriArrayLists.get(position)))
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if(task.isSuccessful()){
                                                                                uriArrayLists.remove(uriArrayLists.get(position));
                                                                                notifyItemRemoved(position);
                                                                                notifyItemRangeChanged(position,getItemCount());
                                                                                countPapersCurrent.clickPaperCurrent(uriArrayLists.size());
                                                                                Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show();
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
        itemPaperClickListenerPet itemPaperClick;
        CountOfPapersCurrent countPaperCurrent;
        public ViewHolder(@NonNull View itemView, CountOfPapersCurrent countPaperCurrent
                , itemPaperClickListenerPet itemPaperClick) {
            super(itemView);
            this.countPaperCurrent = countPaperCurrent;

            imageView = itemView.findViewById(R.id.dropImageViewGallery);
            deleteImage = itemView.findViewById(R.id.deleteCardView);
            this.itemPaperClick = itemPaperClick;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if(itemPaperClick !=null){
                itemPaperClick.itemPaperClick(getAdapterPosition());
            }
        }
    }

    public interface CountOfPapersCurrent{
        void clickPaperCurrent(int size);
    }

    public interface itemPaperClickListenerPet{
        void itemPaperClick(int position);
    }
}
