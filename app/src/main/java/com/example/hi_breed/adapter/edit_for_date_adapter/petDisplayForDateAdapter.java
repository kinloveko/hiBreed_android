package com.example.hi_breed.adapter.edit_for_date_adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.PetDateClass;

import java.util.List;

public class petDisplayForDateAdapter extends ArrayAdapter<PetDateClass> {

    public petDisplayForDateAdapter(Context context, int resource, List<PetDateClass> items) {
        super(context, resource, items);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PetDateClass pet_item = getItem(position);

        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pet_date_swipe_layout,parent,false);
        }
        ImageView image = (ImageView) convertView.findViewById(R.id.item_image);
        TextView name = (TextView) convertView.findViewById(R.id.item_name);
        TextView breed = (TextView) convertView.findViewById(R.id.item_breed);
        TextView address = (TextView) convertView.findViewById(R.id.items_address);
        TextView gender = (TextView) convertView.findViewById(R.id.items_gender);

        gender.setText(pet_item.getPet_gender());
/*        Picasso.get().load(pet_item.getPhotos().get(0)).into(image);*/

        Glide.with(convertView.getContext())
                .load(pet_item.getPhotos().get(0))
                .placeholder(R.drawable.noimage)
                .error(R.drawable.screen_alert_image_error_border)
                .into(image);


        name.setText(pet_item.getPet_name());
        breed.setText(pet_item.getPet_breed());
        address.setText(pet_item.getAddress());
        return convertView;
    }
}
