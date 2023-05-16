package com.example.hi_breed.adapter.shooterAdapter;

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
import com.example.hi_breed.classesFile.service_class;
import com.example.hi_breed.service.service_display_details;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class m_serviceAdapter extends RecyclerView.Adapter<m_serviceAdapter.ViewHolder> {
        Context context;
    private List<service_class> list;

    public m_serviceAdapter(Context context){
            this.context = context;
            this.list = new ArrayList<>();
    }
    public void addServiceDisplay(service_class service_class){
        list.add(service_class);
        notifyDataSetChanged();
    }
    public void clearList(){
        this.list.clear();
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.m_services_card_layout,parent,false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        service_class productModel = list.get(position);

        holder.label.setText(productModel.getServiceType());
        FirebaseFirestore.getInstance().collection("User").document(productModel.getShooter_id())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot snapshot = task.getResult();
                            holder.nameRecycler.setText(snapshot.getString("firstName"));
                        }
                    }
                });


        holder.servicePriceRecycler.setText(productModel.getService_fee()+".0");
        holder.descriptionRecycler.setText(productModel.getService_description());
        Glide.with(holder.itemView.getContext())
                .load(productModel.getPhotos().get(0))
                .placeholder(R.drawable.noimage)
                .error(R.drawable.screen_alert_image_error_border)
                .into(holder.imageRecycler);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, service_display_details.class);
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
        TextView servicePriceRecycler;
        ImageView imageRecycler;
        TextView  nameRecycler;
        TextView  descriptionRecycler;
        TextView label;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            servicePriceRecycler = itemView.findViewById(R.id.servicePriceRecycler);
            imageRecycler = itemView.findViewById(R.id.imageRecycler);
            nameRecycler = itemView.findViewById(R.id.nameRecycler);
            descriptionRecycler = itemView.findViewById(R.id.descriptionRecycler);
            label = itemView.findViewById(R.id.label);

        }
    }


}
