package com.example.hi_breed.adapter.ask_professional;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.TimeStampClass;
import com.example.hi_breed.classesFile.replyClass;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class askProf_reply_adapter extends RecyclerView.Adapter<askProf_reply_adapter.ViewHolder> {

     private    Context context;
    private List<replyClass> list;
    String user;
    public void clearList(){
        this.list.clear();
        notifyDataSetChanged();

    }

    public askProf_reply_adapter(Context context,String user){
            this.context = context;
            this.user=user;
            this.list = new ArrayList<>();
    }
    public void addPetDisplay(replyClass reply){
        list.add(reply);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ask_reply_layout,parent,false);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull askProf_reply_adapter.ViewHolder holder, int position) {

        replyClass productModel = list.get(position);

        FirebaseFirestore.getInstance().collection("Post")
                .document(productModel.getPostKey()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.getString("userID").equals(productModel.getUserID())){
                                holder.role.setText("Author");
                                holder.image.setImageResource(R.drawable.icon_mic);
                            }
                            else{
                                holder.role.setText("Veterinarian");
                                holder.image.setImageResource(R.drawable.icon_verified);
                            }
                    }
                });

        Instant instant = Instant.ofEpochMilli(productModel.getTimeStamp().toDate().getTime());
        TimeStampClass timeStamp = new TimeStampClass();
        holder.replyEdit.setText(productModel.getContent());
        holder.details_reply.setText(timeStamp.getRelativeTime(instant));
        holder.senderName.setText(productModel.getName());

        FirebaseFirestore.getInstance().collection("User").document(productModel.getUserID())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Picasso.get().load(documentSnapshot.getString("image")).placeholder(R.drawable.noimage)
                                .into(holder.reply_image_user);
                    }
                });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView reply_image_user;
        TextView  senderName,details_reply,role;
        TextInputEditText replyEdit;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            role = itemView.findViewById(R.id.role);
            image = itemView.findViewById(R.id.image);
            reply_image_user = itemView.findViewById(R.id.reply_image_user);
            senderName = itemView.findViewById(R.id.senderName);
            replyEdit = itemView.findViewById(R.id.replyEdit);
            details_reply = itemView.findViewById(R.id.details_reply);
        }
    }
}
