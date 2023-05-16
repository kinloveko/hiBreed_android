package com.example.hi_breed.adapter.ask_professional;

import android.annotation.SuppressLint;
import android.content.Context;
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

public class askSinglePhoto extends RecyclerView.Adapter<askSinglePhoto.ViewHolder> {

    private final Context context;
    private final ArrayList <String> uriArrayList;
    CountOfImagesWhenRemovedPet countOfImagesWhenRemoved;
    private final itemClickListenerPet itemClickListener;

    public askSinglePhoto(ArrayList<String> uriArrayList, Context context, CountOfImagesWhenRemovedPet countOfImagesWhenRemoved
            , itemClickListenerPet itemClickListener){
        this.uriArrayList = uriArrayList;
        this.context = context;
        this.countOfImagesWhenRemoved = countOfImagesWhenRemoved;
        this.itemClickListener = itemClickListener;
    }



    @NonNull
    @Override
    public askSinglePhoto.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater  = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_single_image,parent,false);

        return new ViewHolder(view, countOfImagesWhenRemoved,itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull askSinglePhoto.ViewHolder holder, @SuppressLint("RecyclerView") int position) {


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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        CardView deleteImage;
        itemClickListenerPet itemClickListener;
        CountOfImagesWhenRemovedPet countOfImagesWhenRemoved;

        public ViewHolder(@NonNull View itemView, CountOfImagesWhenRemovedPet countOfImagesWhenRemoved
                , itemClickListenerPet itemClickListener) {
            super(itemView);
            this.countOfImagesWhenRemoved = countOfImagesWhenRemoved;

            imageView = itemView.findViewById(R.id.dropImageViewGallery);
            deleteImage = itemView.findViewById(R.id.deleteCardView);
            deleteImage.setVisibility(View.GONE);
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

    public interface CountOfImagesWhenRemovedPet{
        void clicked(int size);
    }

    public interface itemClickListenerPet{
        void itemClick(int position);
    }

}
