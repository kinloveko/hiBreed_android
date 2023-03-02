package com.example.hi_breed.adapter.search_bar_adapter;

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
import com.example.hi_breed.Pet.pet_for_sale_details;
import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.PetSaleClass;
import com.example.hi_breed.classesFile.item;
import com.example.hi_breed.classesFile.product_class;
import com.example.hi_breed.classesFile.service_class;
import com.example.hi_breed.product.product_details;
import com.example.hi_breed.shooter.shooter_vet_display_details;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class searchClickedAdapter extends RecyclerView.Adapter<searchClickedAdapter.ViewHolder> {
        Context context;

    private List<item> searchResults;

    @SuppressLint("NotifyDataSetChanged")
    public void addSearch(List<item> searchResults){
        this.searchResults = searchResults;
        notifyDataSetChanged();
    }

    public void clearList(){
        this.searchResults.clear();
        notifyDataSetChanged();
    }

    public searchClickedAdapter(Context context) {
       this.context = context;
        this.searchResults = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_display_clicked, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        item result = searchResults.get(i);


        viewHolder.price.setText(result.getPrice());


        viewHolder.type.setText(result.getType());
        FirebaseFirestore.getInstance().collection("Shop")
                .document(result.getSeller_id())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot s = task.getResult();
                            viewHolder.name.setText(s.getString("shopName"));
                        }
                    }
                });

        if(result.getCategory().equals("Shooter Service") || result.getCategory().equals("Veterinarian Service")){

               FirebaseFirestore.getInstance().collection("Services")
                       .document(result.getId())
                       .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                           @Override
                           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot s = task.getResult();
                                    List<String> photos = (List<String>) s.get("photos");
                                    Glide.with(viewHolder.itemView.getContext())
                                            .load(photos.get(0))
                                            .placeholder(R.drawable.noimage)
                                            .into(viewHolder.image);
                                }
                           }
                       });

        }
        else if(result.getCategory().equals("Medicine") ||result.getCategory().equals("Dog Accessories")){
            FirebaseFirestore.getInstance().collection("Pet")
                    .document(result.getId())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot s = task.getResult();
                                List<String> list = (List<String>) s.get("photos");
                                Glide.with(viewHolder.itemView.getContext())
                                        .load(list.get(0))
                                        .placeholder(R.drawable.noimage)
                                        .into(viewHolder.image);
                            }
                        }
                    });
        }
        else{
            FirebaseFirestore.getInstance().collection("Pet")
                    .document(result.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot s = task.getResult();
                                List<String> photos = (List<String>) s.get("photos");
                                Glide.with(viewHolder.itemView.getContext())
                                        .load(photos.get(0))
                                        .placeholder(R.drawable.noimage)
                                        .into(viewHolder.image);
                            }
                        }
                    });
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(result.getCategory().equals("Shooter Service") || result.getCategory().equals("Veterinarian Service")){
                        FirebaseFirestore.getInstance().collection("Services")
                                .document(result.getId())
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            DocumentSnapshot s = task.getResult();
                                            service_class ser = s.toObject(service_class.class);
                                            Intent intent = new Intent(context, shooter_vet_display_details.class);
                                            intent.putExtra("mode", (Serializable) ser);
                                            context.startActivity(intent);

                                        }
                                    }
                                });
                    }
                    else if(result.getCategory().equals("Medicine") || result.getCategory().equals("Dog Accessories")){
                        FirebaseFirestore.getInstance().collection("Pet")
                                .document(result.getId())
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            DocumentSnapshot s = task.getResult();
                                            product_class product = s.toObject(product_class.class);
                                            Intent intent = new Intent(context,   product_details.class);
                                            intent.putExtra("mode", (Serializable) product);
                                            context.startActivity(intent);
                                        }
                                    }
                                });
                    }

                    else{
                        FirebaseFirestore.getInstance().collection("Pet")
                                .document(result.getId())
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            DocumentSnapshot s = task.getResult();
                                            PetSaleClass sale = s.toObject(PetSaleClass.class);
                                            Intent intent = new Intent(context,   pet_for_sale_details.class);
                                            intent.putExtra("mode", (Serializable) sale);
                                            context.startActivity(intent);
                                        }
                                    }
                                });
                    }
                }
            });
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView type;
        TextView price;
        TextView name;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            type = itemView.findViewById(R.id.category);
            price = itemView.findViewById(R.id.price);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
        }
    }

}


