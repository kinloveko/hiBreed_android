package com.example.hi_breed.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.TimestampConverter;
import com.example.hi_breed.classesFile.rating_class;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class review_order_adapter extends RecyclerView.Adapter<review_order_adapter.ViewHolder> {

        Context context;
    private List<rating_class> list;

    public review_order_adapter(Context context){
            this.context = context;
            this.list = new ArrayList<>();
    }
    public void clearList(){
        this.list.clear();
        notifyDataSetChanged();
    }
    public void addServiceDisplay(rating_class rating_class){
        list.add(rating_class);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_layout,parent,false);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        rating_class productModel = list.get(position);
        FirebaseFirestore.getInstance().collection("User")
                .document(productModel.getCustomer_id()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            String name = documentSnapshot.getString("firstName")+" "+documentSnapshot.getString("middleName")+" "
                                    +documentSnapshot.getString("lastName");
                            Picasso.get().load(documentSnapshot.getString("image"))
                                            .placeholder(R.drawable.noimage).into(holder.imageView);
                            holder.name.setText(name);

                        }
                    }
                });

        holder.ratingBar.setRating(productModel.getRating());

        if(productModel.getComment().equals("")){
            holder.message.setVisibility(View.GONE);
        }
        else{
            holder.message.setText(productModel.getComment());
        }
        holder.time.setText(TimestampConverter.exactDateTime(productModel.getTimestamp()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,time;
        ImageView imageView;
        RatingBar ratingBar;
        TextView  message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time= itemView.findViewById(R.id.time);
            name = itemView.findViewById(R.id.name);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            imageView = itemView.findViewById(R.id.imageView);
            message = itemView.findViewById(R.id.message);
        }
    }


}
