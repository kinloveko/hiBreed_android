package com.example.hi_breed.adapter.shooterAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hi_breed.R;

import java.util.ArrayList;

public class shooter_proof_RecyclerAdapter extends RecyclerView.Adapter<shooter_proof_RecyclerAdapter.ViewHolder> {


    private final Context context;
    private final ArrayList<Uri> uriArrayList;
    CountOfImagesWhenRemoves countOfImagesWhenRemoves;
    private final itemClickListeners itemClickListeners;

    public shooter_proof_RecyclerAdapter(ArrayList<Uri> uriArrayList,Context context , CountOfImagesWhenRemoves countOfImagesWhenRemoves, itemClickListeners itemClickListeners) {
        this.context = context;
        this.uriArrayList = uriArrayList;
        this.itemClickListeners = itemClickListeners;
        this.countOfImagesWhenRemoves = countOfImagesWhenRemoves;
    }


    @NonNull
    @Override
    public shooter_proof_RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater  = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_single_image,parent,false);

        return new shooter_proof_RecyclerAdapter.ViewHolder(view, countOfImagesWhenRemoves,itemClickListeners);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Glide.with(holder.itemView.getContext())
                .load(uriArrayList.get(position))
                .placeholder(R.drawable.noimage)
                .error(R.drawable.screen_alert_image_error_border)
                .into(holder.imageView);

        holder.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uriArrayList.remove(uriArrayList.get(position));
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,getItemCount());
                countOfImagesWhenRemoves.clicks(uriArrayList.size());
            }
        });
    }


    @Override
    public int getItemCount() {
        return uriArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        CardView deleteImage;
       itemClickListeners itemClickListeners;
        CountOfImagesWhenRemoves countOfImagesWhenRemoves;
        public ViewHolder(@NonNull View itemView, CountOfImagesWhenRemoves countOfImagesWhenRemoves, itemClickListeners itemClickListeners) {
            super(itemView);
            this.countOfImagesWhenRemoves = countOfImagesWhenRemoves;

            imageView = itemView.findViewById(R.id.dropImageViewGallery);
            deleteImage = itemView.findViewById(R.id.deleteCardView);
            this.itemClickListeners = itemClickListeners;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(itemClickListeners !=null){
                itemClickListeners.itemClicks(getAdapterPosition());
            }
        }
    }

    public interface CountOfImagesWhenRemoves{
        void clicks(int size);
    }

    public interface itemClickListeners{
        void itemClicks(int position);
    }
}
