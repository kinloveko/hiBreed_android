package com.example.hi_breed.adapter.product_adapter;

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
import com.example.hi_breed.classesFile.priceFormat;
import com.example.hi_breed.classesFile.product_class;
import com.example.hi_breed.product.product_details;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class productForSaleAdapter extends RecyclerView.Adapter<productForSaleAdapter.ViewHolder> {

        Context context;
    private List<product_class> list;

    public void clearList(){
        this.list.clear();
        notifyDataSetChanged();
    }

    public productForSaleAdapter(Context context){
            this.context = context;
            this.list = new ArrayList<>();
    }
    public void addPetDisplay(product_class product_class){
        list.add(product_class);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_for_sale_layout,parent,false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull productForSaleAdapter.ViewHolder holder, int position) {

        priceFormat format = new priceFormat();
        product_class productModel = list.get(position);
        holder.nameRecycler.setText(productModel.getProd_name());
        if(productModel.getProd_price() !=null)
        holder.priceRecycler.setText(format.priceFormatString(productModel.getProd_price())+".0");



        FirebaseFirestore.getInstance().collection("User")
                        .document(productModel.getVet_id())
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot s = task.getResult();
                            holder.kennelRecycler.setText(s.getString("lastName"));

                        }
                    }
                });

        holder.breedRecycler.setText(productModel.getProd_category());
        FirebaseFirestore.getInstance().collection("Pet").document(productModel.getId())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @SuppressLint("CheckResult")
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                                ArrayList<String> list = (ArrayList<String>) task.getResult().get("photos");
                                if(list.size() != 0){
                                    if(list.get(0) ==null){
                                        holder.imageRecycler.setImageResource(R.drawable.noimage);
                                    }
                                    else{

                                        Glide.with(holder.itemView.getContext())
                                                .load(list.get(0))
                                                .placeholder(R.drawable.noimage)
                                                .error(R.drawable.screen_alert_image_error_border)
                                                .into(holder.imageRecycler);

                                    }
                                }
                                else{
                                    holder.imageRecycler.setImageResource(R.drawable.noimage);
                                }

                        }
                    }
                });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, product_details.class);
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
        TextView priceRecycler;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            breedRecycler = itemView.findViewById(R.id.breedRecycler);
            imageRecycler = itemView.findViewById(R.id.imageRecycler);
            nameRecycler = itemView.findViewById(R.id.nameRecycler);
            kennelRecycler = itemView.findViewById(R.id.kennelRecycler);
            priceRecycler = itemView.findViewById(R.id.priceRecycler);

        }
    }
}
