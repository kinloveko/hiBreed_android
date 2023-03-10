package com.example.hi_breed.userFile.profile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.hi_breed.R;
import com.example.hi_breed.screenLoading.LoadingDialog;
import com.example.hi_breed.screenLoading.screen_WelcomeToHiBreed;
import com.example.hi_breed.service_status_for_buyer.appointment_user_side;
import com.example.hi_breed.userFile.cart.add_to_cart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class user_profile_fragment extends Fragment {

    int i = -1;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private DocumentReference documentReference;
    String  userID;
    CircleImageView imageView;
    ConstraintLayout editLayout,shoppingConstraint,trackingConstraint;
    ConstraintLayout accountLayout,cardLayout,faqLayout,aboutLayout;
    TextView ownerDisplay;
    TextView label;
    BottomNavigationView BreederBottomNavigation;

    public user_profile_fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        BreederBottomNavigation = getActivity().findViewById(R.id.BreederBottom_navigation);
        BreederBottomNavigation.getMenu().getItem(3).setEnabled(false);
        return inflater.inflate(R.layout.user_profile_fragment, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Variables

        if (isAdded() && getActivity() != null) {

            trackingConstraint = view.findViewById(R.id.trackingConstraint);
            shoppingConstraint = view.findViewById(R.id.shoppingConstraint);
            //images
            imageView = view.findViewById(R.id.profileImage);
            //layout
            editLayout = view.findViewById(R.id.profileIconLayout);
            accountLayout = view.findViewById(R.id.accountLayout);
            cardLayout = view.findViewById(R.id.cardLayout);
            aboutLayout = view.findViewById(R.id.aboutLayout);
            faqLayout = view.findViewById(R.id.faqLayout);

            //firebase
            mAuth = FirebaseAuth.getInstance();
            firebaseUser = mAuth.getCurrentUser();
            firebaseFirestore = FirebaseFirestore.getInstance();

            ownerDisplay = view.findViewById(R.id.userNameTxtView);
            label = view.findViewById(R.id.userLabelTextView);

 /*
    TODO: Getting the info of the user that is currently logged in.
  */


            if (firebaseUser != null) {
                userID = firebaseUser.getUid();
                documentReference = firebaseFirestore.collection("User").document(userID);

                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            getUserBreeder(documentReference);
                        }
                    }
                });

            } else {
                Toast.makeText(this.getActivity().getApplicationContext(), "No current User", Toast.LENGTH_SHORT).show();
            }

            shoppingConstraint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), add_to_cart.class));
                }
            });
//to edit
            editLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(user_profile_fragment.this.getActivity(), user_profile_account_edit.class));
                }
            });

            trackingConstraint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), appointment_user_side.class));
                }
            });
// to save the image


//account settings
            accountLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    transistion();
                }
            });

//signout

            ConstraintLayout signout = view.findViewById(R.id.signOutLayout);
            signout.setOnClickListener(new View.OnClickListener() {
                final LoadingDialog loadingDialog = new LoadingDialog(user_profile_fragment.this.getActivity());

                @Override
                public void onClick(View v) {
                    loadingDialog.startDialog();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
                                @Override
                                public void onSuccess(String s) {
                                    FirebaseFirestore.getInstance().collection("User")
                                            .document(firebaseUser.getUid())
                                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot s = task.getResult();
                                                        if (s.getString("fcmToken") != null) {
                                                            String get = s.getString("fcmToken");
                                                            Map<String, Object> m = new HashMap<>();
                                                            m.put("fcmToken", FieldValue.delete());
                                                            FirebaseFirestore.getInstance().collection("User")
                                                                    .document(firebaseUser.getUid())
                                                                    .update(m).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                FirebaseAuth.getInstance().signOut();
                                                                                Intent intent = new Intent(getActivity(), screen_WelcomeToHiBreed.class);
                                                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                                                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                                startActivity(intent);
                                                                                requireActivity().finish();
                                                                                loadingDialog.dismissDialog();
                                                                            }
                                                                        }
                                                                    });
                                                        }
                                                    }
                                                }
                                            });
                                }
                            });

                        }
                    }, 1500);

                }
            });

    /*
    BACK LAYOUT CODE
    TODO: if the user click the back button. it will be directed to the home page

  */

            LinearLayout backArrowLayout = view.findViewById(R.id.backLayout);
            backArrowLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
        }
        else{
            Toast.makeText(sPlashScreen, "Loading", Toast.LENGTH_SHORT).show();
        }
    }


//retrieve the data in the firebase and display it on profile
    public void getUserBreeder(DocumentReference userID) {


        userID.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot snapshot = task.getResult();
                    String name = snapshot.getString("firstName")+" "
                            +snapshot.getString("middleName")+" "
                            +snapshot.getString("lastName");
                    //for full name
                    ownerDisplay.setText(name);
                    //for image
                    if(snapshot.getString("image") != null)

                        Picasso.get().load(snapshot.getString("image")).into(imageView);
                    else
                        imageView.setImageResource(R.drawable.noimage);

                    List<String> role =(List<String>) snapshot.get("role");
                    String fullName = "";
                    if (role != null) {
                        for(String i : role){
                            if (i.contains("Veterinarian"))
                            fullName += "Veterinarian ";
                            if (i.contains("Pet Owner"))
                            fullName += "Owner ";
                            if (i.contains("Pet Breeder"))
                            fullName += "Breeder ";
                            if (i.contains("Pet Shooter"))
                            fullName += "Shooter ";
                            label.setText(fullName);
                        }
                    }
                }
            }
        });

  }

    final FragmentActivity sPlashScreen = getActivity();

    private void transistion(){
        Thread mSplashThread = new Thread() {
            @Override
            public void run() {

                Intent intent = new Intent();
                intent.setClass(getActivity(), user_profile_account_setting.class);
                getActivity().overridePendingTransition(R.drawable.fade_in, R.drawable.fade_out);
                startActivity(intent);
            }
        };
        mSplashThread.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        BreederBottomNavigation.getMenu().getItem(3).setEnabled(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        BreederBottomNavigation.getMenu().getItem(3).setEnabled(true);

    }
}