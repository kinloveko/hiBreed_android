package com.example.hi_breed.userFile.dashboard;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.BaseActivity;
import com.example.hi_breed.loginAndRegistration.Login;
import com.example.hi_breed.screenLoading.LoadingDialog;
import com.example.hi_breed.userFile.home.user_home_fragment;
import com.example.hi_breed.userFile.likes.user_likes_fragment;
import com.example.hi_breed.userFile.notification.user_notification_fragment;
import com.example.hi_breed.userFile.profile.user_profile_fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

public class user_dashboard extends BaseActivity {

    FirebaseUser firebaseUser;
    FirebaseFirestore fireStore;
    BottomNavigationView BreederBottomNavigation;
    Deque<Integer> IntegerDeque = new ArrayDeque<>(4);

    boolean flag = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_dashboard);

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

//Firebase
        fireStore = FirebaseFirestore.getInstance();
        BreederBottomNavigation = findViewById(R.id.BreederBottom_navigation);
        //load home Fragment
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore.getInstance().collection("User").document(firebaseUser.getUid())
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot s = task.getResult();
                            if(s.getString("fcmToken") == null){
                                FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(String s) {
                                        FirebaseFirestore.getInstance().collection("User")
                                                .document(firebaseUser.getUid())
                                                .update("fcmToken",s);
                                    }
                                });
                            }
                        }
                    }
                });

        if(firebaseUser!=null){
            loadFragment(new user_home_fragment());
            //Set home as a default fragment
                readData(fireStore, new OnGetDataListener() {


                    @Override
                    public void onSuccess(Task task) {

                    }

                    @Override
                    public void onStart() {
                        //when starting

                    }

                    @Override
                    public void onFailure() {

                    }
                });
        }
     }//end of onCreate

    @SuppressLint("NonConstantResourceId")
    private Fragment BreederGetFragment(int itemId) {
        switch (itemId){
            case R.id.breeder_homeItem:
                //set checked dashboard fragment
                BreederBottomNavigation.getMenu().getItem(0).setChecked(true);
                //return dashboard fragment
                return new user_home_fragment();
            case R.id.breeder_likesItem:
                //set checked dashboard fragment
                BreederBottomNavigation.getMenu().getItem(1).setChecked(true);
                //return dashboard fragment
                return new user_likes_fragment();
            case R.id.breeder_Order:
                //set checked dashboard fragment
                BreederBottomNavigation.getMenu().getItem(2).setChecked(true);
                //return cart_fragment
                return new user_notification_fragment();
            case R.id.breeder_profileItem:
                //set checked dashboard fragment
                BreederBottomNavigation.getMenu().getItem(3).setChecked(true);
                //return profile_fragment
                return new user_profile_fragment();
        }
        BreederBottomNavigation.getMenu().getItem(0).setChecked(true);
        return new user_home_fragment();
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment,fragment)
                .commit();
    }

        public interface OnGetDataListener {
            //this is for callbacks
            void onSuccess(Task task);
            void onStart();
            void onFailure();
        }

    public void readData(FirebaseFirestore ref, final OnGetDataListener listener) {
        listener.onStart();
       final LoadingDialog loadingDialog = new LoadingDialog(user_dashboard.this);
        loadingDialog.startDialog();
        DocumentReference doc = ref.collection("User").document(firebaseUser.getUid());
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document!=null){
                        List<String> note =(List<String>) document.get("role");
                        if(note!=null) {
                            if (!note.contains(null)) {

                                if (note.contains("Pet Shooter") || note.contains("Pet Breeder") ||note.contains("Veterinarian")|| note.contains("Pet Owner")) {
                                    display("true");
                                    loadingDialog.dismissDialog();
                                }
                                listener.onSuccess(task);
                            } else {
                                loadingDialog.dismissDialog();
                                Toast.makeText(user_dashboard.this, "Role is empty", Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(user_dashboard.this, Login.class));
                                finish();
                            }
                        }
                        else{
                            loadingDialog.dismissDialog();
                            Toast.makeText(user_dashboard.this, "Can't retrieve any data", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(user_dashboard.this, Login.class));
                            finish();
                        }
                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                    Toast.makeText(user_dashboard.this, e.toString(), Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    loadingDialog.dismissDialog();
                    startActivity(new Intent(user_dashboard.this,Login.class));
            }
        });
    }

    private void display(String user){


      if(Objects.equals(user, "true")){

          IntegerDeque.push(R.id.breeder_homeItem);
          BreederBottomNavigation.setVisibility(View.VISIBLE);
          BreederBottomNavigation.setSelectedItemId(R.id.breeder_homeItem);
          BreederBottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
              @Override
              public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                  //get selected item id
                  int id = item.getItemId();

                  //check condition
                  if(IntegerDeque.contains(id)){
                      //when deque list contains selected id
                      //check condition
                      if(id == R.id.breeder_homeItem){
                          //if id is equal to home fragment or tab 1
                          //check condition
                          if(IntegerDeque.size() !=1){
                              //if deque list size is not equal to 1
                              //check condition
                              if(flag){
                                  IntegerDeque.addFirst(R.id.breeder_homeItem);
                                  //set flag to true
                                  flag = false;
                              }
                          }
                      }
                      //Remove selected id from deque list
                      IntegerDeque.remove(id);
                  }
                  //push selected id in deque list
                  IntegerDeque.push(id);
                  //load fragment
                  loadFragment(BreederGetFragment(item.getItemId()));
                  return true;
              }
          });
        }
        else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(user_dashboard.this,Login.class));
            finish();
        }
     }

    @Override
    public void onBackPressed() {
        IntegerDeque.pop();
        //check condition

        if(!IntegerDeque.isEmpty()) {
            //Pop to previous fragment
            //When deque list is not empty
            //load Fragment
            loadFragment(BreederGetFragment(IntegerDeque.peek()));
        }
        else
        {
            //when deque list is empty
            //finish activity
            finish();
        }

    }

}