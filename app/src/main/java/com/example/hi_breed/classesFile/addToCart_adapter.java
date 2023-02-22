package com.example.hi_breed.classesFile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hi_breed.Pet.pet_for_sale_details;
import com.example.hi_breed.R;
import com.example.hi_breed.product.product_details;
import com.example.hi_breed.shop.view_breeder_shop;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class addToCart_adapter extends RecyclerView.Adapter<addToCart_adapter.ViewHolder> {

    Context context;
    List<add_to_cart_class> cart;
    private ItemCheckedChangeListener listener;

    public addToCart_adapter(Context context, ItemCheckedChangeListener listener){
        this.context = context;
        this.cart = new ArrayList<>();
        this.listener = listener;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_to_cart_layout,parent,false);

        return new ViewHolder(view,listener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder v, int position) {
        add_to_cart_class add = cart.get(position);
        priceFormat format = new priceFormat();

        v.delete.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                builder2.setCancelable(false);
                View view = View.inflate(context,R.layout.screen_custom_alert,null);
                //title
                TextView title = view.findViewById(R.id.screen_custom_alert_title);
                //loading text
                TextView loadingText = view.findViewById(R.id.screen_custom_alert_loadingText);
                loadingText.setVisibility(View.GONE);
                //gif
                GifImageView gif = view.findViewById(R.id.screen_custom_alert_gif);
                gif.setVisibility(View.GONE);
                //header image
                AppCompatImageView imageViewCompat = view.findViewById(R.id.appCompatImageView);
                imageViewCompat.setVisibility(View.VISIBLE);
                imageViewCompat.setImageDrawable(context.getDrawable(R.drawable.screen_alert_image_error_border));
                //message
                TextView message = view.findViewById(R.id.screen_custom_alert_message);
                title.setText("Are you sure?");
                message.setVisibility(View.VISIBLE);
                message.setText("Click okay if you want to delete");
                LinearLayout buttonLayout = view.findViewById(R.id.screen_custom_alert_buttonLayout);
                buttonLayout.setVisibility(View.VISIBLE);
                MaterialButton cancel,okay;
                cancel = view.findViewById(R.id.screen_custom_dialog_btn_cancel);
                cancel.setVisibility(View.VISIBLE);

                okay = view.findViewById(R.id.screen_custom_alert_dialog_btn_done);
                okay.setText("Okay");
                builder2.setView(view);
                AlertDialog alert2 = builder2.create();
                alert2.show();
                alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert2.dismiss();
                    }
                });
                okay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseFirestore.getInstance().collection("Cart")
                                .document(add.getId())
                                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        alert2.dismiss();
                                        AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                                        builder2.setCancelable(false);
                                        View view = View.inflate(context,R.layout.screen_custom_alert,null);
                                        //title
                                        TextView title = view.findViewById(R.id.screen_custom_alert_title);
                                        //loading text
                                        TextView loadingText = view.findViewById(R.id.screen_custom_alert_loadingText);
                                        loadingText.setVisibility(View.GONE);
                                        //gif
                                        GifImageView gif = view.findViewById(R.id.screen_custom_alert_gif);
                                        gif.setVisibility(View.GONE);
                                        //header image
                                        AppCompatImageView imageViewCompat = view.findViewById(R.id.appCompatImageView);
                                        imageViewCompat.setVisibility(View.VISIBLE);
                                        imageViewCompat.setImageDrawable(context.getDrawable(R.drawable.dialog_cart_borders));
                                        //message
                                        TextView message = view.findViewById(R.id.screen_custom_alert_message);
                                        title.setText("Deleted to cart");
                                        message.setVisibility(View.VISIBLE);
                                        message.setText("Successfully Added");
                                        LinearLayout buttonLayout = view.findViewById(R.id.screen_custom_alert_buttonLayout);
                                        buttonLayout.setVisibility(View.VISIBLE);
                                        MaterialButton cancel,okay;
                                        cancel = view.findViewById(R.id.screen_custom_dialog_btn_cancel);
                                        cancel.setVisibility(View.GONE);
                                        okay = view.findViewById(R.id.screen_custom_alert_dialog_btn_done);
                                        okay.setText("Okay");
                                        builder2.setView(view);
                                        AlertDialog alert2 = builder2.create();
                                        alert2.show();
                                        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        okay.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                alert2.dismiss();
                                            }
                                        });
                                    }
                                });


                    }
                });

            }
        });
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

        v.shop_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, view_breeder_shop.class);
                i.putExtra("breeder",add.getProd_seller());
                context.startActivity(i);
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
        ImageView delete;
        public ViewHolder(@NonNull View v,  ItemCheckedChangeListener onItemCheckedChanged) {
            super(v);
            check = v.findViewById(R.id.check);
            shop_name = v.findViewById(R.id.shop_name);
             imageView = v.findViewById(R.id.imageView);
            pet_name = v.findViewById(R.id.pet_name);
            breed = v.findViewById(R.id.breed);
            itemValue = v.findViewById(R.id.itemValue);
            delete = v.findViewById(R.id.delete);
           this.onItemCheckedChanged = onItemCheckedChanged;
        }
    }

    public interface ItemCheckedChangeListener {
        void onItemCheckedChanged(add_to_cart_class item, boolean isChecked);
    }
}
