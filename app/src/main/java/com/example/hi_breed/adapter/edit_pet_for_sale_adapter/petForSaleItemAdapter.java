package com.example.hi_breed.adapter.edit_pet_for_sale_adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.PetSaleClass;
import com.example.hi_breed.Pet.edit_pet_for_sale;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class petForSaleItemAdapter extends RecyclerView.Adapter<petForSaleItemAdapter.ViewHolder> {

        Context context;
    private List<PetSaleClass> list;

    public petForSaleItemAdapter(Context context){
            this.context = context;
            this.list = new ArrayList<>();
    }
    public void clearList(){
        this.list.clear();
        notifyDataSetChanged();
    }
    public void addPetDisplay(PetSaleClass PetSaleClass){
        list.add(PetSaleClass);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_my_pets_layout_for_edit,parent,false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PetSaleClass productModel = list.get(position);
        holder.nameRecycler.setText(productModel.getPet_name());
        holder.roleRecycler.setText("For Sale");
        holder.kennelRecycler.setText(productModel.getPet_gender());
        holder.breedRecycler.setText(productModel.getPet_breed());
        FirebaseFirestore.getInstance().collection("Pet").document(productModel.getId())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @SuppressLint("CheckResult")
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                                ArrayList<String> list = (ArrayList<String>) task.getResult().get("photos");
                                if(list.size() !=0)

                                    Glide.with(holder.itemView.getContext())
                                            .load(list.get(0))
                                            .placeholder(R.drawable.noimage)
                                            .error(R.drawable.screen_alert_image_error_border)
                                            .into(holder.imageRecycler);
                        }
                    }
                });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, edit_pet_for_sale.class);
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
        TextView breedRecycler;
        ImageView imageRecycler;
        TextView  nameRecycler;
        TextView  kennelRecycler;
        TextView roleRecycler;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            breedRecycler = itemView.findViewById(R.id.breedRecycler);
            imageRecycler = itemView.findViewById(R.id.imageRecycler);
            nameRecycler = itemView.findViewById(R.id.nameRecycler);
            kennelRecycler = itemView.findViewById(R.id.kennelRecycler);
            roleRecycler = itemView.findViewById(R.id.roleRecycler);

        }
    }


}
