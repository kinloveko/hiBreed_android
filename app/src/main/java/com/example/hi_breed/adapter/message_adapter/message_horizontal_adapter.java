package com.example.hi_breed.adapter.message_adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.matches_class;
import com.example.hi_breed.message.message_conversation_activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class message_horizontal_adapter extends RecyclerView.Adapter<message_horizontal_adapter.ViewHolder> {

     private    Context context;
    private List<matches_class> list;
    private String user;


    public void clearList(){
        this.list.clear();
        notifyDataSetChanged();
    }
    public message_horizontal_adapter(Context context,String user){
            this.context = context;
            this.user = user;
            this.list = new ArrayList<>();
    }
    public void addMatchDisplay(matches_class reply){
        list.add(reply);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_horizontal_layout,parent,false);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull message_horizontal_adapter.ViewHolder holder, int position) {
        String notCurrent;
        matches_class productModel = list.get(position);
        if(!productModel.getParticipants().get(0).equals(user)){
            FirebaseFirestore.getInstance().collection("User")
                    .document(productModel.getParticipants().get(0)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot s = task.getResult();
                                Glide.with(holder.itemView.getContext())
                                        .load(s.getString("image"))
                                        .placeholder(R.drawable.noimage)
                                        .error(R.drawable.screen_alert_image_error_border)
                                        .into(holder.image);
                                holder.text.setText(s.getString("firstName")+" "+s.getString("middleName")+" "+s.getString("lastName"));
                                if(s.get("online")!=null){
                                    if(s.get("online").equals("true"))
                                        holder.avail.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#5CCD08")));
                                    else
                                        holder.avail.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFADB6C4")));
                                }else{

                                }
                            }
                        }
                    });
            notCurrent = productModel.getParticipants().get(0);
        }
        else{
            FirebaseFirestore.getInstance().collection("User")
                    .document(productModel.getParticipants().get(1)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot s = task.getResult();
                                Picasso.get().load(s.getString("image")).into(holder.image);
                                holder.text.setText(s.getString("firstName")+" "+s.getString("middleName")+" "+s.getString("lastName"));
                                if(s.get("online")!=null){
                                    if(s.get("online").equals("true"))
                                        holder.avail.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#5CCD08")));
                                    else
                                        holder.avail.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFADB6C4")));
                                }else{

                                }
                            }
                        }
                });
            notCurrent =productModel.getParticipants().get(1);
        }
        String finalNotCurrent = notCurrent;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, message_conversation_activity.class);
                intent.putExtra("model", (Parcelable) productModel);
                intent.putExtra("notCurrentUser", finalNotCurrent);
                context.startActivity(intent);
            }
        });
        notCurrent="";
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TextView text;
        CircleImageView avail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            text = itemView.findViewById(R.id.text);
            avail = itemView.findViewById(R.id.avail);
        }
    }
}
