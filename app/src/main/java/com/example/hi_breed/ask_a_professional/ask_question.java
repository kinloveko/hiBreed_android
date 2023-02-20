package com.example.hi_breed.ask_a_professional;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hi_breed.R;
import com.example.hi_breed.adapter.petImagesRecyclerAdapter;
import com.example.hi_breed.classesFile.BaseActivity;
import com.example.hi_breed.classesFile.POST;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

import pl.droidsonroids.gif.GifImageView;

public class ask_question extends BaseActivity implements petImagesRecyclerAdapter.CountOfImagesWhenRemovedPet,
        petImagesRecyclerAdapter.itemClickListenerPet {

    String profile="";

    private petImagesRecyclerAdapter adapter;
    ArrayList<Uri> uri  = new ArrayList<Uri>();
    Uri imageUri;
    RecyclerView service_images_view;

    LinearLayout backLayoutService;
    Button question_add_create_Button;
    CardView servicePhotoCardView;

    TextInputEditText serviceNameEdit;
    TextInputEditText serviceDescEdit;

    ImageView serviceNameClearButton;
    ImageView serviceDescClearButton;

    TextView nameCountID;
    TextView serviceDescCountID;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);



        FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot snapshot = task.getResult();
                            if(snapshot!=null){
                                profile = snapshot.getString("image");
                            }
                        }
                    }
                });

        //Recycler
        service_images_view = findViewById(R.id.service_images_view);
        adapter = new petImagesRecyclerAdapter(uri,getApplicationContext(),this,this);
        service_images_view.setLayoutManager(new GridLayoutManager(this,4));
        service_images_view.setAdapter(adapter);
        //button
        question_add_create_Button = findViewById(R.id.question_add_create_Button);
        question_add_create_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInput();
            }
        });
        //cardView
        servicePhotoCardView = findViewById(R.id.servicePhotoCardView);
        servicePhotoCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePermission();
            }
        });
        //back layout
        backLayoutService = findViewById(R.id.backLayoutService);
        backLayoutService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ask_question.this.onBackPressed();
                finish();
            }
        });

        //edit text
        serviceNameEdit = findViewById(R.id.serviceNameEdit);
        serviceDescEdit = findViewById(R.id.serviceDescEdit);
        //counter
        nameCountID = findViewById(R.id.nameCountID);
        serviceDescCountID = findViewById(R.id.serviceDescCountID);
        //clear
        serviceNameClearButton = findViewById(R.id.serviceNameClearButton);
        serviceDescClearButton = findViewById(R.id.serviceDescClearButton);

        if(serviceNameEdit.getText() !=null || serviceNameEdit.getText().equals("")) {
            serviceNameEdit.addTextChangedListener(NameTextEditorWatcher);
        }
        if( serviceDescEdit.getText() !=null ||  serviceDescEdit.getText().equals("")) {
            serviceDescEdit.addTextChangedListener(DescTextEditorWatcher);
        }
    }
    @SuppressLint("ObsoleteSdkInt")
    private void imagePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        }
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),1);
    }
    private int countOfImages = 0;
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && null != data){

            //this part is for to get multiple images
            if(data.getClipData() != null){

                countOfImages = data.getClipData().getItemCount();
                for(int j = 0; j<countOfImages; j++){

                    if(uri.size() < 5){
                        imageUri = data.getClipData().getItemAt(j).getUri();
                        service_images_view.setVisibility(View.VISIBLE);
                        uri.add(imageUri);

                    }else{
                        Toast.makeText(this,"Not allowed to pick more than 5 images",Toast.LENGTH_SHORT).show();
                    }
                }
                //then notify the adapter
                adapter.notifyDataSetChanged();
            }else{
                //this is for single images
                if(uri.size() <5){
                    imageUri =data.getData();
                    uri.add(imageUri);
                    service_images_view.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(this,"Not allowed to pick more than 5 images",Toast.LENGTH_SHORT).show();
                }
            }
            adapter.notifyDataSetChanged();
        }
        else{
            //this code is for if user not picked any image
            Toast.makeText(this,"You Haven't Pick any image",Toast.LENGTH_SHORT).show();
        }
    }


    int upload=0;
    int counterPhotos=0;
    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    private void checkInput() {
        String name = serviceNameEdit.getText().toString().trim();
        String desc = serviceDescEdit.getText().toString().trim();

        if(name.isEmpty()){
            Toast.makeText(this, "Please input a title for your question", Toast.LENGTH_SHORT).show();
            return;
        }
        if (desc.isEmpty()){
            Toast.makeText(this, "Please input a content in your question", Toast.LENGTH_SHORT).show();
        }
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setCancelable(false);
        View view = View.inflate(this,R.layout.screen_custom_alert,null);
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
        imageViewCompat.setVisibility(View.GONE);
        //message
        TextView message = view.findViewById(R.id.screen_custom_alert_message);
        title.setText("Creating your question");
        message.setVisibility(View.VISIBLE);
        message.setText("Please wait a bit.");
        builder2.setView(view);
        AlertDialog alert2 = builder2.create();
        alert2.show();
        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        String countIDD = UUID.randomUUID().toString();
        String userID="";


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            userID = firebaseUser.getUid();
        }
        ArrayList<String> petPhotos = new ArrayList<>();

        if(uri.size() !=0){
            final StorageReference ImageFolder = FirebaseStorage.getInstance().getReference("User/"+userID+"/"+"Question/");
            for(upload = 0;upload<uri.size();upload++){
                Uri Image = uri.get(upload);
                StorageReference imageName = ImageFolder.child(Image.getLastPathSegment());
                imageName.putFile(Image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uris) {
                                counterPhotos++;
                                String url = String.valueOf(uris);
                                petPhotos.add(url);
                                if(counterPhotos == uri.size()){
                                    POST post = new POST(name,desc,petPhotos,firebaseUser.getUid(),profile,countIDD,Timestamp.now(),"Post");
                                    FirebaseFirestore.getInstance().collection("Post").document(countIDD)
                                            .set(post, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        AlertDialog.Builder builder2 = new AlertDialog.Builder(ask_question.this);
                                                        builder2.setCancelable(false);
                                                        View view = View.inflate(ask_question.this,R.layout.screen_custom_alert,null);
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
                                                        imageViewCompat.setImageDrawable(getDrawable(R.drawable.screen_alert_image_valid_borders));
                                                        //message
                                                        TextView message = view.findViewById(R.id.screen_custom_alert_message);
                                                        title.setText("Successfully created a post");
                                                        message.setVisibility(View.VISIBLE);
                                                        message.setText("Click okay to continue..");
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
                                                                Toast.makeText(ask_question.this, "Successfully Created a post", Toast.LENGTH_SHORT).show();
                                                                startActivity(new Intent(ask_question.this,ask_a_professional.class));
                                                                finish();
                                                            }
                                                        });

                                                    }
                                                }
                                            });
                                }

                            }
                        });
                    }
                });
            }
        }
        else{
            POST post = new POST(name,desc,null,firebaseUser.getUid(),profile,countIDD,Timestamp.now(),"Post");
            FirebaseFirestore.getInstance().collection("Post").document(countIDD)
                    .set(post, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(ask_question.this);
                                builder2.setCancelable(false);
                                View view = View.inflate(ask_question.this,R.layout.screen_custom_alert,null);
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
                                imageViewCompat.setImageDrawable(getDrawable(R.drawable.screen_alert_image_valid_borders));
                                //message
                                TextView message = view.findViewById(R.id.screen_custom_alert_message);
                                title.setText("Successfully created a post");
                                message.setVisibility(View.VISIBLE);
                                message.setText("Click okay to continue..");
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
                                        Toast.makeText(ask_question.this, "Successfully Created a post", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ask_question.this,ask_a_professional.class));
                                        finish();
                                    }
                                });
                            }
                        }
                    });
        }
    }



    private final TextWatcher NameTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @SuppressLint("SetTextI18n")
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            nameCountID.setText(s.length() +"/30");
            if(s.length()!=0){
                serviceNameClearButton.setVisibility(View.VISIBLE);
                serviceNameClearButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        serviceNameEdit .getText().clear();
                    }
                });
            }
            else{
                serviceNameClearButton.setVisibility(View.GONE);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };
    private final TextWatcher DescTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @SuppressLint("SetTextI18n")
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            serviceDescCountID.setText(s.length() +"/2000");
            if(s.length()!=0){
                serviceDescClearButton.setVisibility(View.VISIBLE);
                serviceDescClearButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        serviceDescEdit.getText().clear();
                    }
                });
            }
            else{
                serviceDescClearButton.setVisibility(View.GONE);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    public void clicked(int size) {
        Toast.makeText(this,uri.size()+": selected",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void itemClick(int position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        View dialog = View.inflate(this,R.layout.custom_dialog_zoom,null);

        TextView textView = dialog.findViewById(R.id.text_dialog);
        ImageView imageView = dialog.findViewById(R.id.image_view_dialog);
        Button buttonClose = dialog.findViewById(R.id.button_close_dialog);

        textView.setText("Image"+position);
        Glide.with(this).load(uri.get(position)).into(imageView);

        alert.setView(dialog);
        AlertDialog alertDialog = alert.create();

        alertDialog.show();

        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


    }
}