package com.example.hi_breed.classesFile;

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
import com.example.hi_breed.product.product_details;
import com.example.hi_breed.service.service_display_details;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class likes_adapter extends RecyclerView.Adapter<likes_adapter.ViewHolder> {

    Context context;
    List<likes_class> cart;

    public likes_adapter(Context context ){
        this.context = context;
        this.cart = new ArrayList<>();

    }

    public void clearList(){
        this.cart.clear();
        notifyDataSetChanged();
    }

    public void add_to_cart(likes_class cart){
        this.cart.add(cart);
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.likes_recycler_layout,parent,false);

        return new ViewHolder(view) ;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder v, int position) {
            likes_class add = cart.get(position);
            //possible category
            //Medicine
            //forSale
            //Services
            if(add.category.equals("forSale")) {
                FirebaseFirestore.getInstance().collection("Pet")
                        .document(add.product_id)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot s = task.getResult();
                                    List<String> list = (List<String>) s.get("photos");
                                    v.name.setText(s.getString("pet_name"));
                                    v.price.setText(s.getString("pet_price"));
                                    v.category.setText(s.getString("pet_breed"));
                                    if(list!=null)
                                   Picasso.get().load(list.get(0))
                                            .placeholder(R.drawable.noimage)
                                            .into(v.image);
                                }
                            }
                        });

            }
            else if(add.category.equals("Medicine") || add.category.equals("Dog Accessories") ){
                FirebaseFirestore.getInstance().collection("Pet")
                        .document(add.product_id)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot s = task.getResult();

                                    List<String> list = (List<String>) s.get("photos");
                                    if(list!=null)
                                        Glide.with(v.itemView.getContext()).load(list.get(0))
                                            .placeholder(R.drawable.noimage)
                                            .into(v.image);
                                    v.name.setText(s.getString("prod_name"));
                                    v.price.setText(s.getString("prod_price"));
                                    v.category.setText(s.getString("prod_category"));


                                }
                            }
                        });
            }
            else{
                FirebaseFirestore.getInstance().collection("Services")
                        .document(add.product_id)
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot s) {
                                List<String> list = (List<String>) s.get("photos");
                                if(list!=null)
                                    Glide.with(v.itemView.getContext()).load(list.get(0))
                                            .placeholder(R.drawable.noimage)
                                            .into(v.image);
                                v.name.setText(s.getString("serviceType"));
                                v.price.setText(s.getString("service_fee"));
                                v.category.setText(s.getString("displayFor"));
                            }
                        });
            }
            v.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(add.category.equals("forSale")) {
                        FirebaseFirestore.getInstance().collection("Pet")
                                .document(add.product_id)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            DocumentSnapshot s = task.getResult();
                                            PetSaleClass a = s.toObject(PetSaleClass.class);
                                            Intent i = new Intent(context, pet_for_sale_details.class);
                                            i.putExtra("mode", (Serializable) a);
                                            context.startActivity(i);
                                        }
                                    }
                                });

                    }
                    else if(add.category.equals("Medicine") || add.category.equals("Dog Accessories") ){
                        FirebaseFirestore.getInstance().collection("Pet")
                                .document(add.product_id)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            DocumentSnapshot s = task.getResult();
                                            product_class a = s.toObject(product_class.class);
                                            Intent i = new Intent(context, product_details.class);
                                            i.putExtra("mode",(Serializable) a);
                                            context.startActivity(i);
                                        }
                                    }
                                });
                    }
                    else{
                        FirebaseFirestore.getInstance().collection("Services")
                                .document(add.product_id)
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot s) {
                                        service_class a = s.toObject(service_class.class);
                                        Intent i = new Intent(context, service_display_details.class);
                                        i.putExtra("mode",(Serializable) a);
                                        context.startActivity(i);
                                    }
                                });
                    }
                }
            });
    }

    @Override
    public int getItemCount() {
        return cart.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder   {

        TextView category;
        ImageView image;
        TextView  name;
        TextView price;
        public ViewHolder(@NonNull View v ) {
            super(v);
            category = v.findViewById(R.id.category);
            image = v.findViewById(R.id.image);
             name = v.findViewById(R.id.name);
            price = v.findViewById(R.id.price);
        }
    }
}
