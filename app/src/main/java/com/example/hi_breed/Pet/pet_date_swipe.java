package com.example.hi_breed.Pet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import com.example.hi_breed.R;
import com.example.hi_breed.adapter.edit_for_date_adapter.petDisplayForDateAdapter;
import com.example.hi_breed.classesFile.ApiUtilities;
import com.example.hi_breed.classesFile.BaseActivity;
import com.example.hi_breed.classesFile.PetDateClass;
import com.example.hi_breed.classesFile.matches_class;
import com.example.hi_breed.classesFile.notificationData;
import com.example.hi_breed.classesFile.pushNotification;
import com.example.hi_breed.message.message_activity;
import com.example.hi_breed.message.message_conversation_activity;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
        arrayAdapter = new petDisplayForDateAdapter(this, R.layout.pet_date_swipe_layout, rowItems);
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
                                if (task.isSuccessful()) {
                                    DocumentSnapshot s = task.getResult();
                                    if (s.get("swipe") != null) {
                                        Map<String, Object> swipeData = new HashMap<>();
                                        swipeData.put("swipedUserId", item.getPet_breeder());
                                        swipeData.put("swipeDirection", "left");
                                        swipeData.put("pet_id", item.getId());
                                        swipeData.put("pet_breed", item.getPet_breed());
                                        FirebaseFirestore.getInstance().collection("Swipes")
                                                .document(userId)
                                                .update("swipe", FieldValue.arrayUnion(swipeData));
                                    } else {

                                        Map<String, Object> swipe = new HashMap<>();
                                        swipe.put("swipedUserId", item.getPet_breeder());
                                        swipe.put("swipeDirection", "left");
                                        swipe.put("pet_id", item.getId());
                                        swipe.put("pet_breed", item.getPet_breed());
                                        List<Map<String, Object>> hold = new ArrayList<>();
                                        hold.add(swipe);
                                        Map<String, Object> x = new HashMap<>();
                                        x.put("swipe", hold);
                                        FirebaseFirestore.getInstance().collection("Swipes")
                                                .document(userId)
                                                .set(x, SetOptions.merge());
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
                                if (task.isSuccessful()) {
                                    DocumentSnapshot s = task.getResult();
                                    if (s.get("swipe") != null) {
                                        Map<String, Object> swipeData = new HashMap<>();
                                        swipeData.put("swipedUserId", item.getPet_breeder());
                                        swipeData.put("swipeDirection", "right");
                                        swipeData.put("pet_id", item.getId());
                                        swipeData.put("pet_breed", item.getPet_breed());
                                        FirebaseFirestore.getInstance().collection("Swipes")
                                                .document(userId)
                                                .update("swipe", FieldValue.arrayUnion(swipeData)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            isConnection(userId, item);
                                                        }
                                                    }
                                                });
                                    } else {
                                        Map<String, Object> swipe = new HashMap<>();
                                        swipe.put("swipedUserId", item.getPet_breeder());
                                        swipe.put("swipeDirection", "right");
                                        swipe.put("pet_id", item.getId());
                                        swipe.put("pet_breed", item.getPet_breed());
                                        List<Map<String, Object>> hold = new ArrayList<>();
                                        hold.add(swipe);
                                        Map<String, Object> x = new HashMap<>();
                                        x.put("swipe", hold);
                                        FirebaseFirestore.getInstance().collection("Swipes")
                                                .document(userId)
                                                .set(x, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            isConnection(userId, item);
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
                if (rowItems.size() == 0) {
                    flingContainer.setVisibility(View.GONE);
                    noOneTextView_pet_sale_card.setVisibility(View.VISIBLE);
                } else {
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

    private void isConnection(String userId, PetDateClass item) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance().collection("Swipes").document(item.getPet_breeder())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot value = task.getResult();
                            if (value.exists()) {

                                List<Map<String, Object>> swipes = (List<Map<String, Object>>) value.get("swipe");
                                for (Map<String, Object> swipe : swipes) {
                                    if (swipe.get("swipedUserId").equals(currentUserId) && swipe.get("swipeDirection").equals("right")) {

                                        // both users swiped right on each other
                                        Map<String, Object> connection = new HashMap<>();
                                        connection.put("matchID", "");
                                        connection.put("participants", Arrays.asList(userId, item.getPet_breeder()));
                                        connection.put("pets_participants", Arrays.asList(item.getId(), swipe.get("pet_id")));
                                        connection.put("time", Timestamp.now());
                                        connection.put("matchFor", "forDating");
                                        connection.put("show", true);
                                        Log.d("match", "its a match");
                                        FirebaseFirestore.getInstance().collection("Matches").add(connection)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference a) {

                                                        Map<String, Object> map = new HashMap<>();
                                                        map.put("matchID", a.getId());
                                                        Log.d("matchID", a.getId());
                                                        FirebaseFirestore.getInstance().collection("Matches")
                                                                .document(a.getId())
                                                                .set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        Log.d("matchID", "successful");
                                                                        matchNotification(a.getId(), item);
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

    private void matchNotification(String id, PetDateClass item) {

        FirebaseFirestore.getInstance().collection("User")
                .document(item.getPet_breeder()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String token = documentSnapshot.getString("fcmToken");

                        Map<String, Object> map = new HashMap<>();
                        map.put("send_to_id", item.getPet_breeder());
                        map.put("message", "You can now message to each other to plan a date for your dogs");
                        map.put("title", "You have a match");
                        map.put("timestamp", Timestamp.now());
                        map.put("type", "matchDating");
                        map.put("id", id);

                        Map<String, Object> maps = new HashMap<>();

                        maps.put("latestNotification", map);
                        maps.put("notification", Arrays.asList(map));

                        FirebaseFirestore.getInstance().collection("Notifications").document(item.getPet_breeder())
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {

                                            FirebaseFirestore.getInstance().collection("Notifications")
                                                    .document(item.getPet_breeder())
                                                    .update("latestNotification", map, "notification", FieldValue.arrayUnion(map))
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            pushNotification notification = new pushNotification(new notificationData("You have a matched! You can now talk to each other",
                                                                    "Match notification", id, item.getPet_breeder(), "matchNotification", "", ""), token);
                                                            sendNotif(notification);
                                                        }
                                                    });
                                        } else {

                                            FirebaseFirestore.getInstance().collection("Notifications")
                                                    .document(item.getPet_breeder())
                                                    .set(maps)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            pushNotification notification = new pushNotification(new notificationData("You have a matched! You can now talk to each other",
                                                                    "Match notification", id, item.getPet_breeder(), "matchNotification", "", ""), token);
                                                            sendNotif(notification);
                                                        }
                                                    });

                                        }
                                    }
                                });

                    }
                });

        AlertDialog.Builder builder2 = new AlertDialog.Builder(pet_date_swipe.this);
        View view = View.inflate(pet_date_swipe.this, R.layout.matched_layout, null);
        Button sendMessageButton, keepScrollingButton;
        TextView match_message = view.findViewById(R.id.match_message);
        sendMessageButton = view.findViewById(R.id.sendMessageButton);
        keepScrollingButton = view.findViewById(R.id.keepScrollingButton);

        FirebaseFirestore.getInstance().collection("User")
                .document(item.getPet_breeder())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            match_message.setText("You and " + documentSnapshot.getString("firstName") + " both liked the adorable pups you've displayed. Why not start messaging now and plan a doggy playdate? Your furry friends will love it! Woof woof!");
                        }
                    }
                });

        builder2.setView(view);
        AlertDialog alert2 = builder2.create();
        alert2.show();
        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection("Matches").document(id)
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    matches_class match = documentSnapshot.toObject(matches_class.class);
                                    Intent i = new Intent(pet_date_swipe.this, message_conversation_activity.class);
                                    i.putExtra("model", (Serializable) match);
                                    startActivity(i);
                                    alert2.dismiss();
                                }
                            }
                        });

            }
        });
        keepScrollingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert2.dismiss();
            }
        });

        Map<String, Object> map = new HashMap<>();
        map.put("send_to_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
        map.put("message", "You can now message to each other to plan a date for your dogs");
        map.put("title", "You have a match");
        map.put("timestamp", Timestamp.now());
        map.put("type", "matchDating");
        map.put("id", id);

        Map<String, Object> maps = new HashMap<>();

        maps.put("latestNotification", map);
        maps.put("notification", Arrays.asList(map));

        FirebaseFirestore.getInstance().collection("Notifications").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

                            FirebaseFirestore.getInstance().collection("Notifications")
                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .update("latestNotification", map, "notification", FieldValue.arrayUnion(map))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    });
                        } else {

                            FirebaseFirestore.getInstance().collection("Notifications")
                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .set(maps)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    });

                        }
                    }
                });

    }

    private void sendNotif(pushNotification notification) {
        ApiUtilities.getClient().sendNotification(notification).enqueue(new Callback<pushNotification>() {
            @Override
            public void onResponse(Call<pushNotification> call, Response<pushNotification> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(pet_date_swipe.this, "You have a matched!", Toast.LENGTH_SHORT).show();
                    Log.d("Match Notification", "its a match");
                } else {
                    Log.d("Match Notification", "Failed");
                }
            }

            @Override
            public void onFailure(Call<pushNotification> call, Throwable t) {
                Toast.makeText(pet_date_swipe.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

/*
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
    }*/
   private  FirebaseFirestore firestore = FirebaseFirestore.getInstance();
   private  String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
   private  DocumentReference userSwipes = firestore.collection("Swipes").document(userId);

    private void getAllPets() {

        firestore.collection("Pet").whereEqualTo("displayFor","forDating")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }
                        if (value != null) {
                            int count = 0;
                            List<PetDateClass> allPets = new ArrayList<>();
                            for (DocumentSnapshot d : value.getDocuments()) {
                                count++;
                                PetDateClass pet = d.toObject(PetDateClass.class);
                                if (pet.getPet_breeder().equals(userId))
                                    continue;
                                allPets.add(pet);
                                Log.d("petNames",pet.getPet_name()+":"+pet.getPet_breed());
                                if(value.size() == count){
                                    callSwipeFilter(allPets,value);
                                }
                            }
                        }
                    }
                });
    }

    private void callSwipeFilter(List<PetDateClass> allPets,QuerySnapshot value) {

        userSwipes.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(DocumentSnapshot d) {
                if(d.exists()){
                    List<Map<String, Object>> swipes = (List<Map<String, Object>>) d.get("swipe");
                    Toast.makeText(pet_date_swipe.this, String.valueOf(allPets.size()), Toast.LENGTH_SHORT).show();
                    if (swipes != null) {
                        // Get a list of all the pet breeds the user has swiped left
                        List<String> dislikedBreeds = new ArrayList<>();
                        for (Map<String, Object> swipe : swipes) {
                            String direction = (String) swipe.get("swipeDirection");
                            String breed = (String) swipe.get("pet_breed");
                            if (direction.equals("left")) {
                                dislikedBreeds.add(breed);
                            }
                        }
// Filter out the pets with the disliked breeds
                        List<PetDateClass> filteredPets = new ArrayList<>();
                        List<PetDateClass> removedPets = new ArrayList<>();
                        for (PetDateClass pet : allPets) {
                            if (!dislikedBreeds.contains(pet.getPet_breed())) {
                                filteredPets.add(pet);
                                Log.d("petNames1:", pet.getPet_name() + ":" + pet.getPet_breed());
                            } else {
                                removedPets.add(pet);
                            }
                        }
                        filteredPets.addAll(removedPets); // Add the removed pets to the end of the list

// Find the breed the user has swiped right the most

                        Map<String, Integer> breedCount = new HashMap<>();
                        for (Map<String, Object> swipe : swipes) {
                            String direction = (String) swipe.get("swipeDirection");
                            String breed = (String) swipe.get("pet_breed");
                            if (direction.equals("right")) {
                                int count = breedCount.getOrDefault(breed, 0);
                                breedCount.put(breed, count + 1);
                            }
                        }
                        String recommendedBreed = null;
                        int maxCount = 0;
                        for (String breed : breedCount.keySet()) {
                            int count = breedCount.get(breed);
                            if (count > maxCount) {
                                recommendedBreed = breed;
                                maxCount = count;
                            }
                        }

// Display the recommended pets to the user
                        displayRecommendedPets(filteredPets, recommendedBreed);
                    }
                }
                else{
                    // Display all pets to the user

                    displayAllPets();
                }
            }
        });

    }

    private void displayAllPets() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference userSwipes = firestore.collection("Swipes").document(userId);
        // Get all pets from Firestore and display them
        firestore.collection("Pet").whereEqualTo("displayFor","forDating").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {

                    List<PetDateClass> allPets = new ArrayList<>();

                    for (DocumentSnapshot petSnapshot : value) {
                        PetDateClass pet = petSnapshot.toObject(PetDateClass.class);
                        if (pet.getPet_breeder().equals(userId))
                            continue;
                        allPets.add(pet);
                    }
                    userSwipes.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                return;
                            }
                            if (value.exists()) {
                                List<Map<String, Object>> swipes = (List<Map<String, Object>>) value.get("swipe");
                                List<PetDateClass> notSwipedPets = new ArrayList<>();
                                for (PetDateClass pet : allPets) {
                                    for (Map<String, Object> swipe : swipes) {
                                        if (swipe.get("swipedUserId").equals(pet.getPet_breeder())) {
                                         continue;
                                        }
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

    private void displayRecommendedPets(List<PetDateClass> pets, String recommendedBreed) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference userSwipes = firestore.collection("Swipes").document(userId);
        Log.d("petNames:","breed:"+recommendedBreed);

        List<PetDateClass> recommendedPets = new ArrayList<>();
        for (PetDateClass pet : pets) {
            if (recommendedBreed != null && !recommendedBreed.isEmpty()) {
                // Add only pets with a matching breed to the recommendedPets list
                if (pet.getPet_breed().equals(recommendedBreed)) {
                    if (pet.getPet_breeder().equals(userId)) {
                        continue;
                    }
                    recommendedPets.add(pet);
                    Log.d("petNames", pet.getPet_name() + ":" + pet.getPet_breed());
                }
            } else {
                // Add only pets with a non-null and non-empty breed to the recommendedPets list
                if (pet.getPet_breeder().equals(userId)) {
                    continue;
                }
                recommendedPets.add(pet);
                Log.d("petNames", pet.getPet_name() + ":" + pet.getPet_breed());
            }
        }
        userSwipes.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    return;
                }
                if (value.exists()) {
                    Log.d("petNames:","im here in user swipes");

                    List<Map<String, Object>> swipes = (List<Map<String, Object>>) value.get("swipe");
                    List<PetDateClass> notSwipedPets = new ArrayList<>();

                    for (PetDateClass pet : recommendedPets) {
                        boolean isSwiped = false;
                        for (Map<String, Object> swipe : swipes) {
                            if (swipe.get("swipedUserId").equals(pet.getPet_breeder())) {
                                isSwiped = true;
                                break;
                            }
                        }
                        if (!isSwiped) {
                            notSwipedPets.add(pet);
                            Log.d("petNames:",notSwipedPets.toString());
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
                    rowItems.addAll(recommendedPets);
                    arrayAdapter.notifyDataSetChanged();
                    flingContainer.setVisibility(View.VISIBLE);
                    noOneTextView_pet_sale_card.setVisibility(View.GONE);
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
