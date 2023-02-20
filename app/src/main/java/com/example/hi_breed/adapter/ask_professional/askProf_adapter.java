package com.example.hi_breed.adapter.ask_professional;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hi_breed.R;
import com.example.hi_breed.ask_a_professional.ask_question_details;
import com.example.hi_breed.ask_a_professional.edit_post_ask_prof;
import com.example.hi_breed.classesFile.POST;
import com.example.hi_breed.classesFile.TimeStampClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class askProf_adapter extends RecyclerView.Adapter<askProf_adapter.ViewHolder> {

    Context context;
    private List<POST> list;
    private String user;


    public askProf_adapter(Context context){
        this.context = context;
    }

    public askProf_adapter(Context context,String user){
            this.context = context;
            this.user = user;
            this.list = new ArrayList<>();

    }

    @SuppressLint("NotifyDataSetChanged")
    public void clearList(){
        this.list.clear();
        notifyDataSetChanged();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void addPetDisplay(List<POST> postList){
        this.list = postList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ask_prof_layout_post,parent,false);

        return new ViewHolder(view);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull askProf_adapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        POST productModel = list.get(position);

        Instant instant = Instant.ofEpochMilli(productModel.getTimeStamp().toDate().getTime());
        TimeStampClass timeStamp = new TimeStampClass();

        FirebaseFirestore.getInstance().collection("Comments").whereEqualTo("postKey",productModel.getPostKey())
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        int countRep = 0;
                        for(int i = 0;i<list.size();i++){
                            countRep ++;
                        }
                        holder.askReplyRecycler.setText("Reply "+countRep);
                    }
                });

        holder.post_details.setText(timeStamp.getRelativeTime(instant));
        holder.title.setText("Question: "+productModel.getTitle());

        Glide.with(holder.itemView.getContext())
                .load(productModel.getImage())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.screen_alert_image_error_border)
                .into(holder.imageRecycler);

        FirebaseFirestore.getInstance().collection("User").document(productModel.getUserID()).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                     DocumentSnapshot s = task.getResult();
                                     holder.nameRecycler.setText(s.getString("firstName")+" "+s.getString("middleName")+" "+s.getString("lastName"));
                                    }
                             }
                        });

        FirebaseFirestore.getInstance().collection("Post").document(productModel.getPostKey())
                .collection("Views").document("Views_doc").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot s = task.getResult();
                            if(s.get("ids") != null) {
                                List<String> ids = (List<String>) s.get("ids");
                                holder.askViewRecycler.setText("Views "+ids.size());
                            }
                        }
                    }
                });


        List<String> myArray = Arrays.asList(user);
        if(productModel.getUserID().equals(user))
        {
            holder.editQuestion.setVisibility(View.VISIBLE);
            holder.editQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, edit_post_ask_prof.class);
                    intent.putExtra("mode", (Parcelable) productModel);
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                }
            });
        }
        else{
            holder.editQuestion.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseFirestore.getInstance().collection("Post").document(productModel.getPostKey())
                        .collection("Views").document("Views_doc").get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot s = task.getResult();
                                    if(s.get("ids") != null){

                                        List<String> ids = (List<String>) s.get("ids");

                                        if(productModel.getUserID() == user){
                                            return;
                                        }else
                                        if(ids.contains(user)){
                                            return;
                                        }
                                        else{
                                            if(list.size() !=0){
                                                if(productModel.getUserID() == user){
                                                    return;
                                                }else {
                                                    FirebaseFirestore.getInstance().collection("Post").document(productModel.getPostKey())
                                                            .collection("Views").document("Views_doc")
                                                            .update("ids", FieldValue.arrayUnion(user)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {

                                                                    }
                                                                }
                                                            });
                                                }
                                            }


                                        }

                                    }
                                    else{
                                        if(productModel.getUserID() == user){
                                            return;
                                        }else {

                                            FirebaseFirestore.getInstance().collection("Post").document(productModel.getPostKey())
                                                    .collection("Views").document("Views_doc").set(new HashMap<String, Object>() {{
                                                        put("ids", myArray);
                                                    }}, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {

                                                            }
                                                        }
                                                    });

                                        }

                                    }
                                }

                            }
                        });

                Intent intent = new Intent(context, ask_question_details.class);
                intent.putExtra("mode", (Parcelable) productModel);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imageRecycler;
        TextView  nameRecycler;
        TextView  descriptionRecycler;
        TextView askReplyRecycler,
                askViewRecycler,
                post_details;
        ImageView editQuestion;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            editQuestion = itemView.findViewById(R.id.editQuestion);
            imageRecycler = itemView.findViewById(R.id.imageRecycler);
            nameRecycler = itemView.findViewById(R.id.nameRecycler);
            descriptionRecycler = itemView.findViewById(R.id.descriptionRecycler);
            askReplyRecycler = itemView.findViewById(R.id.askReplyRecycler);
            askViewRecycler = itemView.findViewById(R.id.askViewRecycler);
            post_details = itemView.findViewById(R.id.post_details);

        }
    }

}
