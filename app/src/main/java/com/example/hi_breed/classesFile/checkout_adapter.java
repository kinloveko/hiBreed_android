package com.example.hi_breed.classesFile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hi_breed.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class checkout_adapter extends RecyclerView.Adapter<checkout_adapter.ViewHolder> {

    Context context;
    List<add_to_cart_class> cart;

    public checkout_adapter(Context context){
        this.context = context;
        this.cart = new ArrayList<>();

    }
    public void clearList(){
        this.cart.clear();
        notifyDataSetChanged();
    }
    public void add(add_to_cart_class cart){
        this.cart.add(cart);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_layout,parent,false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder v, int position) {
        add_to_cart_class add = cart.get(position);
        priceFormat format = new priceFormat();


            v.itemValue.setText(format.priceFormatString(add.prod_price));

            FirebaseFirestore.getInstance().collection("Shop")
                            .document(add.getProd_seller())
                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            v.shop_name.setText(documentSnapshot.getString("shopName"));
                        }
                    });
            if(add.getProd_category().equals("forSale")){
                FirebaseFirestore.getInstance().collection("Pet")
                        .document(add.prod_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot s = task.getResult();
                                    ArrayList<String> list = (ArrayList<String>) s.get("photos");
                                    Picasso.get().load(list.get(0)).into(v.imageView);
                                    v.breed .setText(s.getString("pet_gender"));
                                    v.pet_name.setText(s.getString("pet_breed"));
                                }
                            }
                        });
            }
            else{
                FirebaseFirestore.getInstance().collection("Pet")
                        .document(add.getProd_id()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot s = task.getResult();
                                    ArrayList<String> list = (ArrayList<String>) s.get("photos");
                                    Picasso.get().load(list.get(0)).into(v.imageView);
                                    v.pet_name.setText(s.getString("prod_name"));
                                    v.breed.setText(s.getString("prod_category"));
                                }
                            }
                        });
            }
    }

    @Override
    public int getItemCount() {
        return cart.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        ImageView imageView,check;
        TextView pet_name;
        TextView breed;
        TextView itemValue;
        TextView shop_name;
        ImageView delete;
        public ViewHolder(@NonNull View v) {
            super(v);
            check = v.findViewById(R.id.check);
            shop_name = v.findViewById(R.id.shop_name);
             imageView = v.findViewById(R.id.imageView);
            pet_name = v.findViewById(R.id.pet_name);
            breed = v.findViewById(R.id.breed);
            itemValue = v.findViewById(R.id.itemValue);
            delete = v.findViewById(R.id.delete);
        }
    }
}
