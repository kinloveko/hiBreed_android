package com.example.hi_breed.loginAndRegistration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.EmailPassClass;
import com.example.hi_breed.classesFile.PhotoClass;
import com.example.hi_breed.classesFile.ShopClass;
import com.example.hi_breed.classesFile.UserClass;
import com.example.hi_breed.classesFile.singlePhotoClass;
import com.example.hi_breed.screenLoading.screen_WelcomeToHiBreed;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.droidsonroids.gif.GifImageView;


public class fragment_registration_email_password extends Fragment {

    final Pattern passwordPattern = Pattern.compile("^" +
            "(?=.*[@#$%^&!+=])" +     // at least 1 special character
            "(?=\\S+$)" +            // no white spaces
            ".{5,}" +                // at least 5 characters
            "$");
    final String passError = "Password must contains at least 1 special character[ex.@#$%!^&+],at least 5 characters and no white spaces ";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.registration_email_password, container, false);
    }

    FirebaseAuth mAuth;
    FirebaseFirestore fireStore;
    StorageReference fileRefs;

    ArrayList<Parcelable> uriShooter;
    ArrayList<Parcelable> uriBreeder;
    ArrayList<Uri> uriShooter_converted;
    ArrayList<Uri> uriBreeder_converted;
    ArrayList<String> role;

    String first;
    String middle;
    String last;
    String birth;
    String gender;
    String address;
    String zip;
    String exp;
    String lastTransaction;
    String kennel;

    String vet_image;
    Button submit_application;
    TextInputLayout reg_email,reg_password;
    TextInputEditText reg_emailEdit, reg_passwordEdit;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //Firebase authentication
        mAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();


        //InputLayout
        reg_email = view.findViewById(R.id.reg_email);
        reg_password = view.findViewById(R.id.reg_password);
        //EditText
        reg_emailEdit = view.findViewById(R.id.reg_emailEdit);
        reg_passwordEdit = view.findViewById(R.id.reg_passwordEdit);

        //button
        submit_application = view.findViewById(R.id.submit_application);
        //Text Watcher
        reg_emailEdit.addTextChangedListener(new TextChange(reg_emailEdit));
        reg_passwordEdit.addTextChangedListener(new TextChange(reg_passwordEdit));


        //ArrayList
        role = new ArrayList<>();
        uriShooter_converted = new ArrayList<>();
        uriBreeder_converted = new ArrayList<>();
        //getting all the passed data in this bundle
        Bundle bundle = this.getArguments();
        role = Objects.requireNonNull(bundle).getStringArrayList("roles");

        first = bundle.getString("first");
        middle = bundle.getString("middle");
        last = bundle.getString("last");
        birth = bundle.getString("birth");
        gender = bundle.getString("gender");
        address = bundle.getString("address");
        zip = bundle.getString("zip");

        if(role.contains("Pet Breeder") || role.contains("Pet Shooter")|| role.contains("Veterinarian")){

            if(role.contains("Veterinarian")){
                vet_image = bundle.getString("vet_image");
            }

            if(role.contains("Pet Breeder")){
                kennel = bundle.getString("kennel");
                uriBreeder = bundle.getParcelableArrayList("uriBreeder");

                //converting Parcelable to URI
                for(Parcelable p: uriBreeder){
                       uriBreeder_converted.add(Uri.parse(p.toString()));
                }
            }
            if(role.contains("Pet Shooter")){
                lastTransaction = bundle.getString("lastTransaction");
                uriShooter = bundle.getParcelableArrayList("uriShooter");

                int i = 0;
                //converting Parcelable to URI
                for(Parcelable p: uriShooter){
                    i++;

                    uriShooter_converted.add(Uri.parse(p.toString()));

                }
            }

            exp = bundle.getString("exp");

        }

        submit_application.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadToFirebase();
            }
        });
    }


    private void uploadToFirebase() {

        String email = Objects.requireNonNull(reg_emailEdit.getText()).toString().trim();
        String password = Objects.requireNonNull(reg_passwordEdit.getText()).toString().trim();

        CharSequence emailcs = reg_emailEdit.getText();
        if (email.isEmpty()) {
            reg_email.setHelperText("Please input your email");
            reg_email.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
            reg_emailEdit.requestFocus();
            return;
        }
        else if (!(Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), emailcs))) {
            reg_email.setHelperText("Enter a valid email ex. example@gmail.com");
            reg_email.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
            reg_emailEdit.requestFocus();
            return;
        }
        CharSequence passwordcs = reg_passwordEdit.getText();

        if(password.isEmpty()){
            reg_password.setHelperText("Please input your password");
            reg_password.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
            reg_passwordEdit.requestFocus();

            return;
        }
        else if(password.length()<5){

            reg_password.setHelperText("Password must have 5 characters");
            reg_password.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
            reg_passwordEdit.requestFocus();

            return;
        }
        else if(!(Pattern.matches(passwordPattern.pattern(),passwordcs))){
            reg_password.setHelperText(passError);
            reg_password.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
            reg_passwordEdit.requestFocus();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = View.inflate(getContext(),R.layout.screen_custom_alert,null);
        TextView title = view.findViewById(R.id.screen_custom_alert_title);

        builder.setCancelable(false);
        AppCompatImageView imageViewCompat = view.findViewById(R.id.appCompatImageView);
        imageViewCompat.setImageURI(Uri.parse(String.valueOf(R.drawable.icon_check_grey)));
        title.setVisibility(View.GONE);

        TextView message = view.findViewById(R.id.screen_custom_alert_message);
        message.setVisibility(View.GONE);
        builder.setView(view);
        AlertDialog alert = builder.create();
        alert.show();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

           mAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                        boolean isNewUser = Objects.requireNonNull(task.getResult().getSignInMethods()).isEmpty();

                        if (isNewUser) {


                            mAuth.createUserWithEmailAndPassword(email,password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){


                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                AuthCredential authCredential = EmailAuthProvider
                                                        .getCredential(email,password);
                                                assert user != null;
                                                user.reauthenticate(authCredential);

                                                Uri image = Uri.parse("android.resource://"+ getActivity().getPackageName()+"/"+R.drawable.noimage);
                                                Uri imageCover= Uri.parse("android.resource://"+ getActivity().getPackageName()+"/"+R.drawable.nobackground);
                                                UserClass breederClass= new UserClass(user.getUid(),first,middle,last,gender,birth,address,zip,image.toString(),imageCover.toString(), Timestamp.now(),"pending");
                                                EmailPassClass security = new EmailPassClass(email,password,"");
                                                DocumentReference documentBreeder = fireStore.collection("User").document(user.getUid());
                                                documentBreeder.set(breederClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {



                                                        if(role.contains("Pet Breeder") || role.contains("Pet Shooter")||role.contains("Veterinarian")) {

                                                            if (role.contains("Pet Breeder")) {
                                                                uploadToStorageBreeder(user);
                                                                Map<String,Object> data = new HashMap<>();
                                                                data.put("kennel",kennel);
                                                                documentBreeder.collection("validation").document("validation_doc").set(data,SetOptions.merge());
                                                            }
                                                            if (role.contains("Pet Shooter")) {
                                                                if(uriShooter_converted.size() == 0){
                                                                    Toast.makeText(getContext(), "shooter image is empty", Toast.LENGTH_SHORT).show();
                                                                    return;
                                                                }
                                                                uploadToStorageShooter(user);
                                                                Map<String,Object> data = new HashMap<>();
                                                                data.put("last_transaction",lastTransaction);
                                                                documentBreeder.collection("validation").document("validation_doc").set(data,SetOptions.merge());
                                                            }
                                                            if(role.contains("Veterinarian")){
                                                                uploadToStorageVet(user);
                                                                Map<String,Object> data = new HashMap<>();
                                                                data.put("license_image",vet_image);
                                                                documentBreeder.collection("validation").document("validation_doc").set(data,SetOptions.merge());

                                                            }
                                                            Map<String,Object> data = new HashMap<>();
                                                            data.put("experience",exp);
                                                            documentBreeder.collection("validation").document("validation_doc").set(data,SetOptions.merge());

                                                            //for creation of shop data

                                                            ShopClass shopClass = new ShopClass(last + " Shop", "", gender, birth, image.toString(), imageCover.toString(), user.getUid(),true);
                                                            DocumentReference documentShop = fireStore.collection("Shop").document(user.getUid());
                                                            documentShop.set(shopClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                }
                                                            });

                                                        }

                                                        save_role(user);
                                                        //for security emails and stuffs
                                                        DocumentReference securityRef = fireStore.collection("User").document(user.getUid());
                                                        securityRef.collection("security")
                                                                .document("security_doc").set(security).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        new Handler().postDelayed(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                alert.dismiss();
                                                                                AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                                                                                builder2.setCancelable(false);
                                                                                View view = View.inflate(getActivity(), R.layout.screen_custom_alert, null);
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
                                                                                imageViewCompat.setImageDrawable(getResources().getDrawable(R.drawable.screen_alert_image_valid_borders));
                                                                                //message
                                                                                TextView message = view.findViewById(R.id.screen_custom_alert_message);
                                                                                title.setText("Account Registered");
                                                                                message.setText("Welcome to hiBreed application. You can now showcase all your pets and services online.");
                                                                                //button
                                                                                LinearLayout buttonLayout = view.findViewById(R.id.screen_custom_alert_buttonLayout);
                                                                                buttonLayout.setVisibility(View.VISIBLE);
                                                                                //button cancel,okay
                                                                                MaterialButton cancel, okay;
                                                                                cancel = view.findViewById(R.id.screen_custom_dialog_btn_cancel);
                                                                                okay = view.findViewById(R.id.screen_custom_alert_dialog_btn_done);
                                                                                cancel.setVisibility(View.GONE);
                                                                                builder2.setView(view);
                                                                                AlertDialog alert2 = builder2.create();
                                                                                alert2.show();
                                                                                alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                                                okay.setOnClickListener(new View.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(View v) {
                                                                                        alert2.dismiss();
                                                                                        reg_email.setError("");
                                                                                        reg_password.setError("");
                                                                                        reg_emailEdit.getText().clear();
                                                                                        FirebaseAuth.getInstance().signOut();
                                                                                        startActivity(new Intent(getContext(), Login.class));
                                                                                        getActivity().finish();
                                                                                    }
                                                                                });
                                                                            }
                                                                        }, 2000);
                                                                    }
                                                                });


                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                            else{
                                                Toast.makeText(requireActivity(), "Something went wrong,Please try again!", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getContext(), screen_WelcomeToHiBreed.class));
                                            }
                                        }
                                    });
                        }
                        else
                        {

                            reg_email.setError("Email is already registered. Try another one!");
                            reg_emailEdit.getText().clear();
                            reg_passwordEdit.getText().clear();

                        }

                    }
                });
    }

    private void uploadToStorageVet(FirebaseUser user) {
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build();

        StorageReference reference = FirebaseStorage.getInstance().getReference("User/"+user.getUid()+"/Vet Proof");

        reference.child("license_ID").putFile(Uri.parse(vet_image),metadata).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    reference.child("license_ID").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            singlePhotoClass photoClass = new singlePhotoClass(task.getResult().toString());
                           FirebaseFirestore.getInstance().collection("User")
                                   .document(user.getUid())
                                   .set(photoClass,SetOptions.merge());
                        }
                    });
                }
            }
        });
    }
    private void save_role(FirebaseUser user) {

        DocumentReference def = FirebaseFirestore.getInstance().collection("User")
                .document(user.getUid());

        List<String> roles = new ArrayList<>();
        roles.addAll(role);
        Map<String,Object> data = new HashMap<>();
        data.put("role",roles);
        def.set(data, SetOptions.merge());

    }

    int counterShooter;
    ArrayList<String> savedImageShooter = new ArrayList<>();
    private void uploadToStorageShooter( FirebaseUser user) {
        // Create file metadata including the content type
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build();
        fileRefs = FirebaseStorage.getInstance().getReference("User/"+user.getUid()+"/Shooter Proof/");
        for ( int z=0; z < uriShooter_converted.size(); z++){
            final int last = z;
            Uri uriLast = uriShooter_converted.get(z);
            fileRefs.child(uriLast.getLastPathSegment()).putFile(uriShooter_converted.get(z),metadata).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        fileRefs.child(uriLast.getLastPathSegment()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                counterShooter++;
                                if(task.isSuccessful()){
                                    savedImageShooter.add(task.getResult().toString());
                                }else{
                                    Log.d("ERROR",task.getException().toString());
                                   }
                                if(counterShooter == uriShooter_converted.size()){
                                    saveToFireStoreShooter(user);
                                      }
                            }
                        });
                    }
                }
            });
        }
    }//end of save to storage

    private void saveToFireStoreShooter(FirebaseUser user) {
        DocumentReference def = FirebaseFirestore.getInstance().collection("User")
                .document(user.getUid());

        ArrayList<String> photos = new ArrayList<>();
        photos.addAll(savedImageShooter);
        PhotoClass photo = new PhotoClass(photos);

        def.collection("proof_photo").document("proof_doc").set(photo, SetOptions.merge());
    }//end of save to firebase

    int counterBreeder;
    ArrayList<String> savedImageBreeder = new ArrayList<>();
    private void uploadToStorageBreeder( FirebaseUser user) {
        // Create file metadata including the content type
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build();
       StorageReference fileRef = FirebaseStorage.getInstance().getReference("User/"+user.getUid()+"/Certificate/");

        for ( int z=0; z < uriBreeder_converted.size(); z++){
            final int last = z;
            Uri uriLast = uriBreeder_converted.get(z);
            fileRef.child(uriLast.getLastPathSegment()).putFile(uriBreeder_converted.get(z),metadata).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){

                        fileRef.child(uriLast.getLastPathSegment()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                counterBreeder++;
                                if(task.isSuccessful()){
                                    savedImageBreeder.add(task.getResult().toString());
                                }
                                else{
                                    Toast.makeText(getContext(), "Image cannot uploaded_"+uriBreeder_converted.get(last)+"_"+last, Toast.LENGTH_SHORT).show();
                                }
                                if(counterBreeder == uriBreeder_converted.size()){
                                    saveToFireStoreBreeder(user);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }

    }//end of save to storage

    private void saveToFireStoreBreeder(FirebaseUser user) {
        DocumentReference def = FirebaseFirestore.getInstance().collection("User")
                .document(user.getUid());
        ArrayList<String> photos = new ArrayList<>();
        photos.addAll(savedImageBreeder);
        PhotoClass photo = new PhotoClass(photos);

        def.collection("certificate").document("certificate_doc").set(photo, SetOptions.merge());
    }//end of save to firebase


    private class TextChange implements TextWatcher {


        View view;
        private TextChange (View v) {
            view = v;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onTextChanged(CharSequence s, int i, int i1, int i2) {

            switch (view.getId()) {

                case R.id.reg_emailEdit:
                    if (s != null) {
                        if (Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), s)) {
                            reg_email.setHelperText("Email is valid");
                            reg_email.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#5CCD08")));

                        } else {
                            reg_email.setHelperText("Enter a valid email ex. example@gmail.com");
                            reg_email.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                        }
                    }
                    else {
                        reg_email.setHelperText("Please input your email");
                        reg_email.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    }
                    break;

                case R.id.reg_passwordEdit:
                    if(s!=null){
                        Matcher m = passwordPattern.matcher(s);
                        if(m.matches()) {
                            if (s.length() >= 4) {
                                reg_password.setHelperText("Valid password!");
                                reg_password.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#5CCD08")));
                            }
                        }
                        else{
                            reg_password.setHelperText(passError);
                            reg_password.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                        }
                    }
                    else{
                        reg_password.setHelperText("Please enter a password");
                        reg_password.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    }
                    break;
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };
}