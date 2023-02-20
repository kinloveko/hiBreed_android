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

import com.example.hi_breed.Pet.pet_for_sale_details;
import com.example.hi_breed.R;
import com.example.hi_breed.product.product_details;
import com.example.hi_breed.shop.view_breeder_shop;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class addToCart_adapter extends RecyclerView.Adapter<addToCart_adapter.ViewHolder> {

    Context context;
    List<add_to_cart_class> cart;
    private ItemCheckedChangeListener listener;
    Object tag;
    public addToCart_adapter(Context context, ItemCheckedChangeListener listener){
        this.context = context;
        this.cart = new ArrayList<>();
        this.listener = listener;
    }
    public void clearList(){
        this.cart.clear();
        notifyDataSetChanged();
    }
    public void add_to_cart(add_to_cart_class cart){
        this.cart.add(cart);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_to_cart_layout,parent,false);

        return new ViewHolder(view,listener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder v, int position) {
            add_to_cart_class add = cart.get(position);
        priceFormat format = new priceFormat();
            v.itemValue.setText(format.priceFormatString(add.prod_price));

           /* */

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
                                    v.pet_name.setText(s.getString("pet_name"));
                                    v.breed.setText(s.getString("pet_breed"));
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
        v.shop_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, view_breeder_shop.class);
                i.putExtra("breeder",add.getProd_seller());
                context.startActivity(i);
                //hehe
            }
        });
        v.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a) {
                Object tag = v.check.getTag();
                if (tag == null || !((Boolean) tag)) {
                    v.check.setImageResource(R.drawable.icon_check_click);
                    v.check.setTag(true);
                    v.onItemCheckedChanged.onItemCheckedChanged(add, true);
                } else {
                    v.check.setImageResource(R.drawable.icon_check_box);
                    v.check.setTag(false); // set to false instead of true
                    v.onItemCheckedChanged.onItemCheckedChanged(add, false);
                }
            }
        });

            v.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(add.getProd_category().equals("forSale")) {
                        FirebaseFirestore.getInstance().collection("Pet")
                                .document(add.getProd_id())
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
                    else if(add.getProd_category().equals("Medicine") || add.prod_category.equals("Dog Accessories") ){
                        FirebaseFirestore.getInstance().collection("Pet")
                                .document(add.getProd_id())
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

                }
            });

    }

    @Override
    public int getItemCount() {
        return cart.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ItemCheckedChangeListener onItemCheckedChanged;
        ImageView imageView,check;
        TextView pet_name;
        TextView breed;
        TextView itemValue;
        TextView shop_name;
        public ViewHolder(@NonNull View v,  ItemCheckedChangeListener onItemCheckedChanged) {
            super(v);
            check = v.findViewById(R.id.check);
            shop_name = v.findViewById(R.id.shop_name);
             imageView = v.findViewById(R.id.imageView);
            pet_name = v.findViewById(R.id.pet_name);
            breed = v.findViewById(R.id.breed);
            itemValue = v.findViewById(R.id.itemValue);
           this.onItemCheckedChanged = onItemCheckedChanged;

        }
    }


    public interface ItemCheckedChangeListener {
        void onItemCheckedChanged(add_to_cart_class item, boolean isChecked);
    }
}
