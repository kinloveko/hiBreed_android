package com.example.hi_breed.ask_a_professional;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
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
import com.example.hi_breed.adapter.ask_professional.edit_post_ask_prof_adapter;
import com.example.hi_breed.classesFile.BaseActivity;
import com.example.hi_breed.classesFile.POST;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class edit_post_ask_prof extends BaseActivity implements edit_post_ask_prof_adapter.CountOfImagesCurrent, edit_post_ask_prof_adapter.itemCurrentClickListenerPet
   {
    RecyclerView service_images_view;
    LinearLayout backLayoutService;
    CardView servicePhotoCardView;
    TextInputEditText serviceNameEdit;
    TextInputEditText serviceDescEdit;
    Button deletePost;
    ImageView serviceNameClearButton,saveNameButton;
    ImageView serviceDescClearButton,saveQuestionButton;
    POST post;
    TextView nameCountID;
    TextView serviceDescCountID;
    ArrayList<Uri> currUri = new ArrayList<>();
    edit_post_ask_prof_adapter adapter;
    String oldTitle="",oldDesc="";
    int pos=0;


       @SuppressLint("NotifyDataSetChanged")
    @Override
       public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post_ask_prof);


        service_images_view = findViewById(R.id.service_images_view);
        //clear and save
        saveNameButton = findViewById(R.id.saveNameButton);
        saveQuestionButton = findViewById(R.id.saveQuestionButton);

        serviceNameClearButton = findViewById(R.id.serviceNameClearButton);
        serviceDescClearButton = findViewById(R.id.serviceDescClearButton);
        //edit text
        serviceNameEdit = findViewById(R.id.serviceNameEdit);
        serviceDescEdit = findViewById(R.id.serviceDescEdit);
        //counter
        nameCountID = findViewById(R.id.nameCountID);
        serviceDescCountID = findViewById(R.id.serviceDescCountID);
        backLayoutService = findViewById(R.id.backLayoutService);
        backLayoutService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_post_ask_prof.this.onBackPressed();
                finish();
            }
        });
        Intent intent = getIntent();
        pos = getIntent().getIntExtra("position", -1);
        post = (POST) intent.getSerializableExtra("mode");
        if(post!=null){
                serviceNameEdit.setText(post.getTitle());
                oldTitle = post.getTitle();
            if(serviceNameEdit.getText().toString() != null || serviceNameEdit.getText().toString() != ""){
                serviceNameClearButton.setVisibility(View.VISIBLE);
            }
                serviceDescEdit.setText(post.getDescription());
                oldDesc = post.getDescription();
            if(serviceDescEdit.getText().toString() != null || serviceDescEdit.getText().toString() != ""){
                serviceDescClearButton.setVisibility(View.VISIBLE);
            }

            if(post.getPhotos()!=null){
                service_images_view.setVisibility(View.VISIBLE);
                for(String i:post.getPhotos()){
                    Uri img = Uri.parse(i);
                    currUri.add(img);
                    adapter = new edit_post_ask_prof_adapter(currUri,getApplicationContext(),this,this,post.getPostKey(),post.getUserID());
                    adapter.notifyDataSetChanged();
                }
            }
        }

        serviceNameClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               serviceNameEdit.getText().clear();
            }
        });

        serviceDescClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceDescEdit.getText().clear();
            }
        });


        //current
        adapter = new edit_post_ask_prof_adapter(currUri,getApplicationContext(),this,this,post.getPostKey(),post.getUserID());
        service_images_view.setLayoutManager(new GridLayoutManager(this, 4));
        service_images_view.setAdapter(adapter);

        servicePhotoCardView = findViewById(R.id.servicePhotoCardView);
        servicePhotoCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePermission();
            }
        });

        if(serviceNameEdit.getText() !=null || serviceNameEdit.getText().equals("")) {
            serviceNameEdit.addTextChangedListener(NameTextEditorWatcher);
        }
        if( serviceDescEdit.getText() !=null ||  serviceDescEdit.getText().equals("")) {
            serviceDescEdit.addTextChangedListener(DescTextEditorWatcher);
        }
        deletePost = findViewById(R.id.deletePost);
        deletePost.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
            @Override
            public void onClick(View v) {

                androidx.appcompat.app.AlertDialog.Builder builder2 = new androidx.appcompat.app.AlertDialog.Builder(edit_post_ask_prof.this);
                builder2.setCancelable(false);
                View view = View.inflate(edit_post_ask_prof.this, R.layout.screen_custom_alert, null);
                //title
                TextView title = view.findViewById(R.id.screen_custom_alert_title);
                //loading text
                TextView loadingText = view.findViewById(R.id.screen_custom_alert_loadingText);
                loadingText.setVisibility(View.GONE);
                //gif
                GifImageView gif = view.findViewById(R.id.screen_custom_alert_gif);
                Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.kawaii_cry);
                gif.setImageURI(uri);
                //header image
                AppCompatImageView imageViewCompat = view.findViewById(R.id.appCompatImageView);
                imageViewCompat.setVisibility(View.VISIBLE);
                imageViewCompat.setImageDrawable(getDrawable(R.drawable.screen_alert_image_error_border));
                //message
                TextView message = view.findViewById(R.id.screen_custom_alert_message);
                title.setText("Are you sure?");
                message.setVisibility(View.GONE);
                //button
                LinearLayout buttonLayout = view.findViewById(R.id.screen_custom_alert_buttonLayout);
                buttonLayout.setVisibility(View.VISIBLE);
                MaterialButton cancel, okay;
                cancel = view.findViewById(R.id.screen_custom_dialog_btn_cancel);
                okay = view.findViewById(R.id.screen_custom_alert_dialog_btn_done);
                okay.setText("Delete");
                okay.setBackgroundColor(Color.parseColor("#F6B75A"));
                okay.setTextColor(Color.WHITE);

                builder2.setView(view);
                androidx.appcompat.app.AlertDialog alert2 = builder2.create();
                alert2.show();
                alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                okay.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
                    @Override
                    public void onClick(View v) {
                        alert2.dismiss();
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(edit_post_ask_prof.this);
                        builder2.setCancelable(false);
                        View view = View.inflate(edit_post_ask_prof.this, R.layout.screen_custom_alert, null);
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
                        title.setText("Deleting your post");
                        message.setVisibility(View.VISIBLE);
                        message.setText("Please wait a bit. We are deleting your post info");
                        builder2.setView(view);
                        AlertDialog alert2 = builder2.create();
                        alert2.show();
                        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        FirebaseFirestore.getInstance().collection("Post")
                                .document(post.getPostKey()).collection("Views")
                                .document("Views_doc")
                                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            FirebaseFirestore.getInstance().collection("Post")
                                                    .document(post.getPostKey())
                                                    .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){
                                                                        final StorageReference storageReference = FirebaseStorage.getInstance().getReference("User/"+post.getUserID()+"/"+"Question/");
                                                                        storageReference.listAll()
                                                                                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                                                                                    @Override
                                                                                    public void onSuccess(ListResult listResult) {
                                                                                        for (StorageReference item : listResult.getItems()) {
                                                                                            item.delete();
                                                                                        }
                                                                                        storageReference.delete();
                                                                                        alert2.show();
                                                                                        startActivity(new Intent(edit_post_ask_prof.this, ask_a_professional.class));
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
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert2.dismiss();

                    }
                });




            }
        });
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
    Uri imageUri;
    ArrayList<Uri> arr = new ArrayList<>();
    int count;
    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables", "NotifyDataSetChanged"})
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null ) {
            // get the image Uri
            imageUri = data.getData();
            // check if the user selected multiple images

            if (data.getClipData() != null) {
                Toast.makeText(this,"Please upload one at a time.",Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                // display the selected image
                service_images_view.setVisibility(View.VISIBLE);
                // do something with the image (e.g. upload to Firebase Storage)
                AlertDialog.Builder builder2 = new AlertDialog.Builder(edit_post_ask_prof.this);
                builder2.setCancelable(false);
                View view = View.inflate(edit_post_ask_prof.this,R.layout.screen_custom_alert,null);
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
                imageViewCompat.setImageDrawable(getDrawable(R.drawable.screen_alert_image_error_border));
                //message
                TextView message = view.findViewById(R.id.screen_custom_alert_message);
                title.setText("Updating your photo");
                message.setVisibility(View.VISIBLE);
                message.setText("Please wait a bit. We are updating your info");
                builder2.setView(view);
                AlertDialog alert2 = builder2.create();
                alert2.show();
                alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                uploadToFirebaseStorage(post.getPostKey(),imageUri,alert2);

            }
        }
        else {
                Toast.makeText(this, "You haven't picked any image", Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadToFirebaseStorage(String postKey, Uri imageUri,AlertDialog alertDialog) {
        String userID="";
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            userID = firebaseUser.getUid();
        }
        String imageName = "Question_Photo_" + postKey + "_" + System.currentTimeMillis();
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference("User/"+userID+"/"+"Question/"+imageName);
        final UploadTask uploadTask = storageReference.putFile(imageUri);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Get the image URL from Firebase Storage
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uris) {
                        String imageURL = uris.toString();
                        // Save the image URL in Firestore
                        FirebaseFirestore.getInstance().collection("Post").document(postKey)
                                .update("photos", FieldValue.arrayUnion(imageURL)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @SuppressLint({"NotifyDataSetChanged", "UseCompatLoadingForDrawables", "SetTextI18n"})
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            alertDialog.dismiss();
                                            AlertDialog.Builder builder2 = new AlertDialog.Builder(edit_post_ask_prof.this);
                                            builder2.setCancelable(false);
                                            View view = View.inflate(edit_post_ask_prof.this,R.layout.screen_custom_alert,null);
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
                                            title.setText("Successfully Updated");
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
                                            AlertDialog alert3 = builder2.create();
                                            alert3.show();
                                            alert3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            okay.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    service_images_view.setVisibility(View.VISIBLE);
                                                    currUri.add(uris);
                                                    adapter.notifyDataSetChanged();
                                                    alert3.dismiss();

                                                }
                                            });
                                        }
                                    }
                                });
                    }
                });
            }
        });
    }

    private final TextWatcher NameTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @SuppressLint("SetTextI18n")
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            nameCountID.setText(s.length() +"/2000");
            if(s.length()!=0){
                serviceNameClearButton.setVisibility(View.VISIBLE);
                serviceNameClearButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        serviceNameEdit .getText().clear();
                    }
                });
                if(oldTitle.equals(serviceNameEdit.getText().toString())){
                    saveNameButton.setVisibility(View.GONE);
                }
                else{
                    saveNameButton.setVisibility(View.VISIBLE);
                    saveNameButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateToFireStore("title",serviceNameEdit.getText().toString());
                            oldTitle = serviceNameEdit.getText().toString();
                            saveNameButton.setVisibility(View.GONE);
                        }
                    });
                }
            }
            else{
                saveNameButton.setVisibility(View.GONE);
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
                if(oldDesc.equals(serviceDescEdit.getText().toString())){
                    saveQuestionButton.setVisibility(View.GONE);
                }
                else{
                    saveQuestionButton.setVisibility(View.VISIBLE);
                    saveQuestionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateToFireStore("description",serviceDescEdit.getText().toString());
                            oldDesc =serviceDescEdit.getText().toString();
                            saveQuestionButton.setVisibility(View.GONE);
                        }
                    });
                }
            }
            else{
                saveQuestionButton.setVisibility(View.GONE);
                serviceDescClearButton.setVisibility(View.GONE);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };

    private void updateToFireStore(String field,String value){

        Map<String,String> m = new HashMap<>();
        m.put(field,value);

        FirebaseFirestore.getInstance().collection("Post")
                .document(post.getPostKey()).set(m, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(edit_post_ask_prof.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
    @Override
    public void clickCurrent(int size) {
        Toast.makeText(this, currUri.size()+": Current", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void itemCurrentClick(int position) {
        Dialog dialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        dialog.setContentView(R.layout.custom_dialog_zoom);

        TextView textView = dialog.findViewById(R.id.text_dialog);
        ImageView imageView = dialog.findViewById(R.id.image_view_dialog);
        Button buttonClose = dialog.findViewById(R.id.button_close_dialog);


        textView.setText("Image"+position);
        Glide.with( this)
                .load(currUri.get(position))
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