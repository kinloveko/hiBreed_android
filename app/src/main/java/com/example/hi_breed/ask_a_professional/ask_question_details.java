package com.example.hi_breed.ask_a_professional;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hi_breed.R;
import com.example.hi_breed.adapter.ask_professional.askProf_reply_adapter;
import com.example.hi_breed.adapter.ask_professional.askSinglePhoto;
import com.example.hi_breed.classesFile.BaseActivity;
import com.example.hi_breed.classesFile.POST;
import com.example.hi_breed.classesFile.TimeStampClass;
import com.example.hi_breed.classesFile.replyClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ask_question_details extends BaseActivity implements askSinglePhoto.CountOfImagesWhenRemovedPet, askSinglePhoto.itemClickListenerPet{
    TextView headerName;
    ConstraintLayout reply_layout;
    CircleImageView imageRecycler,reply_image_user;
    TextView title,nameRecycler,time_details,descriptionRecycler;
    TextInputEditText replyEdit;
    ImageView sendReplyImageView,edit_icons;
    RecyclerView list_of_reply,list_of_photos;
    askProf_reply_adapter adapter;
    askSinglePhoto photo_adapter;
    TextView askReplyRecycler,askViewRecycler;
    String image;
    String name;
    String postKey;
    POST post;
    LinearLayout backLayoutService;
    ArrayList<Uri> uri;




    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged", "ResourceType"})
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question_details);



        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        }
        else{
            window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.setStatusBarColor(Color.parseColor("#e28743"));
        }



        backLayoutService = findViewById(R.id.backLayoutService);
        backLayoutService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ask_question_details.this.onBackPressed();
                finish();
            }
        });
        reply_layout = findViewById(R.id.reply_layout);
        imageRecycler = findViewById(R.id.imageRecycler);
        reply_image_user = findViewById(R.id.reply_image_user);
        title = findViewById(R.id.title);
        nameRecycler = findViewById(R.id.nameRecycler);
        time_details = findViewById(R.id.time_details);
        descriptionRecycler = findViewById(R.id.descriptionRecycler);
        replyEdit = findViewById(R.id.replyEdit);
        sendReplyImageView = findViewById(R.id.sendReplyImageView);
        askReplyRecycler = findViewById(R.id.askReplyRecycler);
        askViewRecycler = findViewById(R.id.askViewRecycler);
        headerName = findViewById(R.id.headerName);
        edit_icons = findViewById(R.id.edit_icons);
        //recycler view
        Intent intent = getIntent();
        post= (POST) intent.getSerializableExtra("mode");
        list_of_photos = findViewById(R.id.list_of_photos);

        photo_adapter = new askSinglePhoto((ArrayList<String>) post.getPhotos(),getApplicationContext(),this,this);
        list_of_photos.setAdapter(photo_adapter);


        if(post!=null){

            if(post.getUserID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){

                edit_icons.setVisibility(View.VISIBLE);


                edit_icons.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ask_question_details.this, edit_post_ask_prof.class);
                        intent.putExtra("mode", (Parcelable) post);
                        ask_question_details.this.startActivity(intent);
                    }
                });
            }
            else{
                edit_icons.setVisibility(View.GONE);
            }

            postKey = post.getPostKey();
            if(post.getImage()!=null){
                Picasso.get().load(post.getImage()).into(imageRecycler);
                Glide.with(this)
                        .load(post.getImage())
                        .placeholder(R.drawable.noimage)
                        .error(R.drawable.screen_alert_image_error_border)
                        .into(imageRecycler);
            }
           title.setText("Question: "+post.getTitle());
           descriptionRecycler.setText(post.getDescription());
            Instant instant = Instant.ofEpochMilli(post.getTimeStamp().toDate().getTime());
            TimeStampClass timeStamp = new TimeStampClass();
           time_details.setText(timeStamp.getRelativeTime(instant));

            //photos
            if(post.getPhotos()!=null){
                if(post.getPhotos().size() > 1){
                    list_of_photos.setLayoutManager(new GridLayoutManager(this,2));
                }
                else{
                     list_of_photos.setLayoutManager(new GridLayoutManager(this,1));

                }
            }
           //FOR NAMES
            FirebaseFirestore.getInstance().collection("User").document(post.getUserID()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot s = task.getResult();
                                headerName.setText(s.getString("firstName")+" "+s.getString("middleName")+" "+s.getString("lastName")+" Post");
                               nameRecycler.setText(s.getString("firstName")+" "+s.getString("middleName")+" "+s.getString("lastName"));
                            }
                        }
                    });

        }
        //FOR POST VIEW COUNT
        FirebaseFirestore.getInstance().collection("Post").document(post.getPostKey())
                .collection("Views").document("Views_doc").addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null) return;
                        if(value!=null){
                            if(value.exists()){

                                if(value.get("ids") != null) {
                                    List<String> ids = (List<String>) value.get("ids");
                                    askViewRecycler.setText("Views "+ids.size());
                                }
                            }
                        }
                    }
                });
        //FOR COMMENTS COUNT
        FirebaseFirestore.getInstance().collection("Comments").whereEqualTo("postKey",post.getPostKey())
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if(value!=null){
                                    List<DocumentSnapshot> list = value.getDocuments();
                                    int countRep = 0;
                                    for(int i = 0;i<list.size();i++){
                                        countRep ++;
                                    }
                                    askReplyRecycler.setText("Reply "+countRep);
                                }
                            }
                        });
        //FOR IMAGE OF THE VET
        FirebaseFirestore.getInstance().collection("User")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot s = task.getResult();
                            if(s!=null){
                                image = s.getString("image");
                                name = s.getString("firstName")+" "+s.getString("middleName")+" "+s.getString("lastName");

                                Picasso.get().load(s.getString("image")).into(reply_image_user);
                                Glide.with(ask_question_details.this)
                                        .load(s.getString("image"))
                                        .into(reply_image_user);
                            }
                        }
                    }
                });

        //FOR CHECKING OF ROLES
        FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
               .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot s = task.getResult();
                            if(s.get("role") != null) {

                                List<String> ids = (List<String>) s.get("role");
                                if(ids!=null){
                                    if(ids.contains("Veterinarian") || post.getUserID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){


                                        reply_layout.setVisibility(View.VISIBLE);

                                        sendReplyImageView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if(replyEdit.getText().toString().isEmpty()){
                                                    Toast.makeText(ask_question_details.this, "Add a comment first before clicking the add button.", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                                else{
                                                    String id = UUID.randomUUID().toString();
                                                    replyClass reply = new replyClass(id,replyEdit.getText().toString(),FirebaseAuth.getInstance().getUid(),image,name, Timestamp.now(),post.getPostKey());
                                                    FirebaseFirestore.getInstance().collection("Comments").document(id).set(reply, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                replyEdit.getText().clear();
                                                                Toast.makeText(ask_question_details.this, "Comment added", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                    else{
                                        reply_layout.setVisibility(View.GONE);
                                    }
                                }
                            }
                        }
                    }
                });


        list_of_reply = findViewById(R.id.list_of_reply);
        adapter = new askProf_reply_adapter(this,FirebaseAuth.getInstance().getCurrentUser().getUid());
        list_of_reply.setLayoutManager(new GridLayoutManager(this,1));
        list_of_reply.setAdapter(adapter);
        getReply();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void getReply() {
        FirebaseFirestore.getInstance().collection("Comments").whereEqualTo("postKey",postKey)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {

                            return;
                        }
                        if (value != null && !value.isEmpty()) {
                            adapter.clearList();

                            List<DocumentSnapshot> list = value.getDocuments();
                            if(list!=null){
                                for (DocumentSnapshot s: list){
                                    replyClass rep = s.toObject(replyClass.class);
                                    adapter.addPetDisplay(rep);
                                }
                            }
                        }
                        else
                        {

                        }
                    }
                });
    }

    @Override
    public void clicked(int size) {
       }

    @Override
    public void itemClick(int position) {

        Dialog dialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        dialog.setContentView(R.layout.custom_dialog_zoom);

        TextView textView = dialog.findViewById(R.id.text_dialog);
        ImageView imageView = dialog.findViewById(R.id.image_view_dialog);
        Button buttonClose = dialog.findViewById(R.id.button_close_dialog);


        textView.setText("Image"+position);
        Glide.with(ask_question_details.this)
                .load(post.getPhotos().get(position))
                .placeholder(R.drawable.noimage)
                .error(R.drawable.screen_alert_image_error_border)
                .into(imageView);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}