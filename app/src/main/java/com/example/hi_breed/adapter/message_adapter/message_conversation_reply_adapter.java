package com.example.hi_breed.adapter.message_adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.TimestampConverter;
import com.example.hi_breed.classesFile.message_class;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class message_conversation_reply_adapter extends RecyclerView.Adapter<message_conversation_reply_adapter.ViewHolder> {

     private    Context context;
    private List<message_class> list;
    private String user;
    public message_conversation_reply_adapter(Context context, String user){
            this.context = context;
            this.user = user;
            this.list = new ArrayList<>();
    }

    public void addMatchDisplay(List<message_class> reply){
        this.list = reply;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_coversation_layout,parent,false);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull message_conversation_reply_adapter.ViewHolder holder, int position) {

        message_class s = list.get(position);

        if(s.getSender().equals(user)){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.image.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_END);
            holder.image.setLayoutParams(params);

            RelativeLayout.LayoutParams rParam = (RelativeLayout.LayoutParams) holder.relative.getLayoutParams();
            rParam.addRule(RelativeLayout.START_OF,R.id.image);
            holder.relative.setLayoutParams(rParam);

            RelativeLayout.LayoutParams textParam = (RelativeLayout.LayoutParams) holder.details_chat.getLayoutParams();
            textParam.addRule(RelativeLayout.START_OF,R.id.image);
            textParam.addRule(RelativeLayout.BELOW,R.id.relative);
            holder.details_chat.setLayoutParams(textParam);



        }
        else{
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.image.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_START);
            holder.image.setLayoutParams(params);

            RelativeLayout.LayoutParams rParam = (RelativeLayout.LayoutParams) holder.relative.getLayoutParams();
            rParam.addRule(RelativeLayout.END_OF,R.id.image);
            holder.relative.setLayoutParams(rParam);




            RelativeLayout.LayoutParams textParam = (RelativeLayout.LayoutParams) holder.details_chat.getLayoutParams();
            textParam.addRule(RelativeLayout.END_OF,R.id.image);
            textParam.addRule(RelativeLayout.BELOW,R.id.relative);
            holder.details_chat.setLayoutParams(textParam);

        }

        String formattedTime = TimestampConverter.formatTimestamp(s.getTimestamp());
        holder.details_chat.setText(formattedTime);
        holder.relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.details_chat.isShown()){
                    holder.details_chat.setVisibility(View.GONE);
                }
                else{
                    holder.details_chat.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.text.setText(s.getText());
        FirebaseFirestore.getInstance().collection("User")
                        .document(s.getSender()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot s = task.getResult();
                            if(s.exists()){
                                Glide.with(holder.itemView.getContext())
                                        .load(s.getString("image"))
                                        .placeholder(R.drawable.noimage)
                                        .error(R.drawable.screen_alert_image_error_border)
                                        .into(holder.image);

                            }
                        }
                    }
                });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TextView text;
        TextView details_chat;
        RelativeLayout relative;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            text = itemView.findViewById(R.id.text);
            details_chat = itemView.findViewById(R.id.details_chat);
            relative = itemView.findViewById(R.id.relative);

        }
    }
}
