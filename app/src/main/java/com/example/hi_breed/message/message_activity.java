package com.example.hi_breed.message;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hi_breed.R;
import com.example.hi_breed.adapter.message_adapter.message_horizontal_adapter;
import com.example.hi_breed.adapter.message_adapter.message_vertical_adapter;
import com.example.hi_breed.classesFile.BaseActivity;
import com.example.hi_breed.classesFile.chat_conversation_class;
import com.example.hi_breed.classesFile.matches_class;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import de.hdodenhof.circleimageview.CircleImageView;

public class message_activity extends BaseActivity {

    private  CircleImageView profileImage;
    private FirebaseUser user;
    private RecyclerView matchProfile_recycler;
    private RecyclerView matchConversation_recycler;
    private message_horizontal_adapter adapter;
    private ImageView backArrowImage;
    private message_vertical_adapter adapter_vertical;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);
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




        backArrowImage = findViewById(R.id.backArrowImage);
        backArrowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message_activity.this.onBackPressed();
            }
        });
        user = FirebaseAuth.getInstance().getCurrentUser();

        profileImage = findViewById(R.id.profileImage);

        matchConversation_recycler = findViewById(R.id.matchConversation_recycler);
        matchConversation_recycler.setLayoutManager(new GridLayoutManager(this,1));
        adapter_vertical = new message_vertical_adapter(this,user.getUid());


        matchProfile_recycler = findViewById(R.id.matchProfile_recycler);
        matchProfile_recycler.setLayoutManager(new LinearLayoutManager(message_activity.this,LinearLayoutManager.HORIZONTAL,false));
        adapter = new message_horizontal_adapter(this,user.getUid());

        FirebaseFirestore.getInstance().collection("User")
                .document(user.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot s = task.getResult();
                            Glide.with( message_activity.this)
                                    .load(s.getString("image"))
                                    .placeholder(R.drawable.noimage)
                                    .error(R.drawable.screen_alert_image_error_border)
                                    .into(profileImage);
                        }
                    }
                });
    }

    private void getConversation() {
        Query query = FirebaseFirestore.getInstance().collection("Chat").whereArrayContains("participants",user.getUid());

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if(error!=null){
                                    return;
                                }
                                if(value!=null){
                                  query.orderBy("latestMessage.timestamp", Query.Direction.DESCENDING)
                                          .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                              @Override
                                              public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                  if(error!=null){
                                                      return;
                                                  }
                                                  if(value!=null){
                                                      adapter_vertical.clearList();
                                                      for(DocumentSnapshot s: value){
                                                          chat_conversation_class a = s.toObject(chat_conversation_class.class);
                                                          adapter_vertical.addMatchDisplay(a);
                                                          matchConversation_recycler.setAdapter(adapter_vertical);
                                                      }
                                                  }
                                              }
                                          });

                                }
                            }
                        });

    }




    @Override
    protected void onResume() {
        super.onResume();
        getConversation();

        getMatches();
    }

    private void getMatches() {
        FirebaseFirestore.getInstance().collection("Matches").whereArrayContains("participants",user.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(error!=null){
                                return;
                            }
                            if(value!=null){
                                    adapter.clearList();
                                for(DocumentSnapshot s: value){
                                    if(s.exists()){
                                        matches_class m = s.toObject(matches_class.class);
                                        adapter.addMatchDisplay(m);
                                        matchProfile_recycler.setAdapter(adapter);
                                    }
                                }

                            }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}