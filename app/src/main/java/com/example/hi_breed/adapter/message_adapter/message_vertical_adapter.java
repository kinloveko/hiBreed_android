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
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.TimestampConverter;
import com.example.hi_breed.classesFile.chat_conversation_class;
import com.example.hi_breed.classesFile.matches_class;
import com.example.hi_breed.message.message_conversation_activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class message_vertical_adapter extends RecyclerView.Adapter<message_vertical_adapter.ViewHolder> {
    private List<matches_class> match ;
     private    Context context;
    private List<chat_conversation_class> list;
    private String user;

    public message_vertical_adapter(Context context, String user){
            this.context = context;
            this.user = user;
            this.list = new ArrayList<>();
            this.match  = new ArrayList<>();

    }

    public void clearList(){
        this.list.clear();
        notifyDataSetChanged();
    }
    public void addMatchDisplay(chat_conversation_class reply){
        this.list.add(reply);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_conversation_vertical,parent,false);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull message_vertical_adapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String notCurrentUser;
        chat_conversation_class s = list.get(position);

        if(!s.getParticipants().get(0).equals(user)){
            notCurrentUser = s.getParticipants().get(0);
        }
        else{
            notCurrentUser = s.getParticipants().get(1);
        }


        FirebaseFirestore.getInstance().collection("Matches")
                .whereEqualTo("matchID",s.getMatchID())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(value!=null){
                            for(DocumentSnapshot s:value){
                                matches_class m = s.toObject(matches_class.class);
                                match.add(m);
                            }
                        }
                    }
                });

            FirebaseFirestore.getInstance().collection("User")
                    .document(notCurrentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot s = task.getResult();
                                if(s.exists()){
                                    if(s.get("online")!=null){
                                        if(s.get("online").equals("true"))
                                        holder.avail.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#5CCD08")));
                                        else
                                            holder.avail.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFADB6C4")));
                                    }else{

                                    }
                                    Glide.with(holder.itemView.getContext())
                                            .load(s.getString("image"))
                                            .placeholder(R.drawable.noimage)
                                            .error(R.drawable.screen_alert_image_error_border)
                                            .into(holder.image);

                                    holder.name.setText(s.getString("firstName")+" "+s.getString("middleName")+" "+s.getString("lastName"));
                                }
                            }
                        }
                    });

        holder.recentChat.setText(s.getLatestMessage().get("text").toString());
            if(s.getLatestMessage().get("timestamp")!=null) {
                String formattedTime = TimestampConverter.formatTimestamp((Timestamp) s.getLatestMessage().get("timestamp"));
                holder.timestamp.setText(formattedTime);
            }

        String finalNotCurrentUser = notCurrentUser;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, message_conversation_activity.class);
                matches_class s = match.get(position);
                intent.putExtra("model", (Parcelable) s);
                intent.putExtra("notCurrentUser", finalNotCurrentUser);
                context.startActivity(intent);

            }
        });
        notCurrentUser="";
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        CircleImageView image;
        TextView recentChat;
        TextView timestamp;
        CircleImageView avail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avail = itemView.findViewById(R.id.avail);
            image = itemView.findViewById(R.id.image);
            recentChat = itemView.findViewById(R.id.recentChat);
            timestamp = itemView.findViewById(R.id.timestamp);
            name = itemView.findViewById(R.id.name);
        }
    }
}
