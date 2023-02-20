package com.example.hi_breed.Pet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.hi_breed.R;
import com.example.hi_breed.adapter.edit_for_date_adapter.petDisplayForDateAdapter;
import com.example.hi_breed.classesFile.BaseActivity;
import com.example.hi_breed.classesFile.PetDateClass;
import com.example.hi_breed.message.message_activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class pet_date_swipe extends BaseActivity {
    private CardView card;
    private TextView noOneTextView_pet_sale_card;
    private petDisplayForDateAdapter arrayAdapter;
    private LinearLayout backLayout;
    private List<PetDateClass> rowItems;
    SwipeFlingAdapterView flingContainer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pet_date_swipe_container);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#000000"));
        }
        card = findViewById(R.id.card);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(pet_date_swipe.this, message_activity.class));
            }
        });

        noOneTextView_pet_sale_card = findViewById(R.id.noOneTextView_pet_sale_card);
        noOneTextView_pet_sale_card.setVisibility(View.VISIBLE);
        backLayout = findViewById(R.id.backLayoutPet);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pet_date_swipe.this.onBackPressed();
                finish();
            }
        });
        getAllPets();
         rowItems = new ArrayList<PetDateClass>();
         arrayAdapter = new petDisplayForDateAdapter(this, R.layout.pet_date_swipe_layout,rowItems);
         flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
         flingContainer.setAdapter(arrayAdapter);
         flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                PetDateClass item = (PetDateClass) dataObject;

                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                // Get the id of the user who made the swipe
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                FirebaseFirestore.getInstance().collection("Swipes")
                        .document(userId)
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot s = task.getResult();
                                    if(s.get("swipe") !=null){
                                        Map<String, Object> swipeData = new HashMap<>();
                                        swipeData.put("swipedUserId", item.getPet_breeder());
                                        swipeData.put("swipeDirection", "left");
                                        FirebaseFirestore.getInstance().collection("Swipes")
                                                .document(userId)
                                                .update("swipe", FieldValue.arrayUnion(swipeData));
                                    }
                                    else{

                                        Map<String, Object> swipe = new HashMap<>();
                                        swipe.put("swipedUserId", item.getPet_breeder());
                                        swipe.put("swipeDirection", "left");
                                        List<Map<String,Object>> hold = new ArrayList<>();
                                        hold.add(swipe);
                                        Map<String,Object> x  = new HashMap<>();
                                        x.put("swipe",hold);
                                        FirebaseFirestore.getInstance().collection("Swipes")
                                                .document(userId)
                                                .set(x,SetOptions.merge());
                                    }
                                }
                            }
                        });
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                PetDateClass item = (PetDateClass) dataObject;
                // Get the id of the user who made the swipe
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                // Add a document to the swipes collection for the swipe
                FirebaseFirestore.getInstance().collection("Swipes")
                                .document(userId)
                                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot s = task.getResult();
                                    if(s.get("swipe") !=null){
                                        Map<String, Object> swipeData = new HashMap<>();
                                        swipeData.put("swipedUserId", item.getPet_breeder());
                                        swipeData.put("swipeDirection", "right");
                                        FirebaseFirestore.getInstance().collection("Swipes")
                                                .document(userId)
                                                .update("swipe", FieldValue.arrayUnion(swipeData)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            isConnection(userId,item);
                                                        }
                                                    }
                                                });
                                    }
                                    else{
                                        Map<String, Object> swipe = new HashMap<>();
                                        swipe.put("swipedUserId", item.getPet_breeder());
                                        swipe.put("swipeDirection", "right");
                                        List<Map<String,Object>> hold = new ArrayList<>();
                                        hold.add(swipe);
                                        Map<String,Object> x  = new HashMap<>();
                                        x.put("swipe",hold);
                                        FirebaseFirestore.getInstance().collection("Swipes")
                                                .document(userId)
                                                .set(x,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            isConnection(userId,item);
                                                        }
                                                    }
                                                });
                                    }
                                }
                            }
                        });
                Toast.makeText(pet_date_swipe.this, "Like", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                if(rowItems.size() == 0){
                    flingContainer.setVisibility(View.GONE);
                    noOneTextView_pet_sale_card.setVisibility(View.VISIBLE);
                }
                else{
                    noOneTextView_pet_sale_card.setVisibility(View.GONE);
                    flingContainer.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(pet_date_swipe.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void isConnection(String userId, PetDateClass item){
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collection("Swipes").document(item.getPet_breeder())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot value = task.getResult();
                            if(value.exists()){

                                List<Map<String, Object>> swipes = (List<Map<String, Object>>) value.get("swipe");
                                for (Map<String, Object> swipe : swipes) {
                                    if (swipe.get("swipedUserId").equals(currentUserId) && swipe.get("swipeDirection").equals("right")) {
                                        // both users swiped right on each other
                                        Map<String, Object> connection = new HashMap<>();
                                        connection.put("matchID", "");
                                        connection.put("participants", Arrays.asList(userId, item.getPet_breeder()));
                                        connection.put("time", Timestamp.now());
                                        connection.put("show", true);
                                        FirebaseFirestore.getInstance().collection("Matches").add(connection)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {

                                                        Map<String, Object> map = new HashMap<>();
                                                        map.put("matchID", documentReference.getId());

                                                        FirebaseFirestore.getInstance().collection("Matches")
                                                                .document(documentReference.getId())
                                                                .set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        Toast.makeText(pet_date_swipe.this, "It's a match!", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                    }
                                                });
                                    }

                                }

                            }
                        }
                    }
                });
    }

    private void getAllPets() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference userSwipes = firestore.collection("Swipes").document(userId);

        firestore.collection("Pet")
                .whereEqualTo("displayFor", "forDating")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error)  {
                        if(error!=null){
                            return;
                        }
                        if(value!=null){

                            List<PetDateClass> allPets = new ArrayList<>();

                            for (DocumentSnapshot petSnapshot : value) {
                                PetDateClass pet = petSnapshot.toObject(PetDateClass.class);
                                if(pet.getPet_breeder().equals(userId))
                                    continue;
                                allPets.add(pet);
                            }

                            userSwipes.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                    if(error!=null){
                                       return;
                                    }
                                        if (value.exists())
                                        {
                                            List<Map<String, Object>> swipes = (List<Map<String, Object>>) value.get("swipe");
                                            List<PetDateClass> notSwipedPets = new ArrayList<>();
                                            for (PetDateClass pet : allPets) {
                                                boolean isSwiped = false;
                                                for (Map<String, Object> swipe : swipes) {
                                                    if (swipe.get("swipedUserId").equals(pet.getPet_breeder())) {
                                                        isSwiped = true;
                                                        break;
                                                    }
                                                }
                                                if (!isSwiped) {
                                                    notSwipedPets.add(pet);
                                                }
                                            }
                                            if (notSwipedPets.isEmpty()) {
                                                // all pets have been swiped, hide the SwipeFlingAdapterView
                                                flingContainer.setVisibility(View.GONE);
                                                noOneTextView_pet_sale_card.setVisibility(View.VISIBLE);
                                            } else {
                                                // show the notSwipedPets in the SwipeFlingAdapterView
                                                rowItems.clear();
                                                rowItems.addAll(notSwipedPets);
                                                arrayAdapter.notifyDataSetChanged();
                                                flingContainer.setVisibility(View.VISIBLE);
                                                noOneTextView_pet_sale_card.setVisibility(View.GONE);
                                            }
                                        } else {
                                            // show all pets in the SwipeFlingAdapterView as this user has no swipes yet

                                            rowItems.addAll(allPets);
                                            arrayAdapter.notifyDataSetChanged();
                                            flingContainer.setVisibility(View.VISIBLE);
                                            noOneTextView_pet_sale_card.setVisibility(View.GONE);
                                        }

                                 }
                            });
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