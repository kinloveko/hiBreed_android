package com.example.hi_breed.adapter;

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

public class breeder_kennel_cert_RecyclerAdapter extends RecyclerView.Adapter<breeder_kennel_cert_RecyclerAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList <Uri> uriArrayList;
    CountOfImagesWhenRemoved countOfImagesWhenRemoved;
    private final itemClickListener itemClickListener;

    public breeder_kennel_cert_RecyclerAdapter(ArrayList<Uri> uriArrayList, Context context, CountOfImagesWhenRemoved countOfImagesWhenRemoved
   , itemClickListener itemClickListener) {
        this.uriArrayList = uriArrayList;
        this.context = context;
        this.countOfImagesWhenRemoved = countOfImagesWhenRemoved;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public breeder_kennel_cert_RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater  = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_single_image,parent,false);

        return new ViewHolder(view, countOfImagesWhenRemoved,itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull breeder_kennel_cert_RecyclerAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

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
                countOfImagesWhenRemoved.clicked(uriArrayList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return uriArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        CardView deleteImage;
        itemClickListener itemClickListener;
        CountOfImagesWhenRemoved countOfImagesWhenRemoved;
        public ViewHolder(@NonNull View itemView, CountOfImagesWhenRemoved countOfImagesWhenRemoved
        ,itemClickListener itemClickListener) {
            super(itemView);
            this.countOfImagesWhenRemoved = countOfImagesWhenRemoved;

            imageView = itemView.findViewById(R.id.dropImageViewGallery);
            deleteImage = itemView.findViewById(R.id.deleteCardView);
            this.itemClickListener = itemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(itemClickListener !=null){
                itemClickListener.itemClick(getAdapterPosition());
            }
        }
    }
    public interface CountOfImagesWhenRemoved{
        void clicked(int size);
    }

    public interface itemClickListener{
        void itemClick(int position);
    }
}
