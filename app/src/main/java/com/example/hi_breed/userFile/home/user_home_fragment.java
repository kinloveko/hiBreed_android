package com.example.hi_breed.userFile.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.hi_breed.Pet.pet_date_swipe;
import com.example.hi_breed.R;
import com.example.hi_breed.adapter.edit_pet_for_sale_adapter.petDisplayForSaleAdapter;
import com.example.hi_breed.ask_a_professional.ask_a_professional;
import com.example.hi_breed.classesFile.PetSaleClass;
import com.example.hi_breed.classesFile.RecommendedProduct;
import com.example.hi_breed.classesFile.likes_adapter;
import com.example.hi_breed.classesFile.likes_class;
import com.example.hi_breed.marketplace.m_market_place_container;
import com.example.hi_breed.not_verified_activity;
import com.example.hi_breed.search.search_dashboard_home;
import com.example.hi_breed.shop.user_breeder_shop_panel;
import com.example.hi_breed.userFile.profile.user_profile_account_edit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.droidsonroids.gif.GifImageView;

public class user_home_fragment extends Fragment {

    public user_home_fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    BottomNavigationView BreederBottomNavigation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.owner_home_fragment, container, false);

        BreederBottomNavigation = getActivity().findViewById(R.id.BreederBottom_navigation);
        BreederBottomNavigation.getMenu().getItem(0).setEnabled(false);
        return view;
    }

    ImageSlider imageSlider;
    TextView logoName;
    private FirebaseUser firebaseUser;
    FirebaseFirestore fireStore;
    TextView notVerified;
    ImageView imageView;
    CardView profilePictureCard, searchcarddView, addPetCard, petSaleCardView9, findDateCardView9, myServicesCardView9;
    private RecyclerView for_you_recycler;
    private petDisplayForSaleAdapter adapter;
    LinearLayout for_you_layout;
    DocumentReference databaseReference;
    ArrayList<String> role;
    LinearLayout maybeLayout;
    RecyclerView youMayLike_recycler;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        youMayLike_recycler = view.findViewById(R.id.youMayLike_recycler);
        youMayLike_recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapters = new likes_adapter(getContext());
        maybeLayout = view.findViewById(R.id.maybeLayout);
        notVerified = view.findViewById(R.id.notVerified);
        logoName = view.findViewById(R.id.logoName);
        addPetCard = view.findViewById(R.id.addPetView);
        //arraylist
        role = new ArrayList<>();
        //profileCard
        searchcarddView = view.findViewById(R.id.searchcarddView);
        profilePictureCard = view.findViewById(R.id.cardView);
        petSaleCardView9 = view.findViewById(R.id.petSaleCardView9);
        findDateCardView9 = view.findViewById(R.id.findDateCardView9);
        myServicesCardView9 = view.findViewById(R.id.myServicesCardView9);
        myServicesCardView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ask_a_professional.class));
            }
        });
        searchcarddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), search_dashboard_home.class));
            }
        });
        //fireStore
        fireStore = FirebaseFirestore.getInstance();


        //image Slider
        imageSlider = view.findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.slide_third, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slide_first, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slide_second, ScaleTypes.FIT));
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);
        //Linear
        for_you_layout = view.findViewById(R.id.forYou_layout);
        //textview

        //RecyclerView
        for_you_recycler = view.findViewById(R.id.for_you_recycler);
        adapter = new petDisplayForSaleAdapter(getContext());
        for_you_recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        for_you_recycler.setAdapter(adapter);

        getPetForYou();

        //imageView
        imageView = view.findViewById(R.id.profileImage);

        //user
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID = "";
        if (firebaseUser != null) {
            userID = firebaseUser.getUid();
            // calling the user info to display the details about the user.

            databaseReference = fireStore.collection("User").document(userID);
            databaseReference
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().getString("status").equals("pending")) {
                                        notVerified.setVisibility(View.VISIBLE);
                                        notVerified.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                startActivity(new Intent(getContext(), not_verified_activity.class));
                                            }
                                        });
                                    }

                                    ArrayList arrayList = (ArrayList) task.getResult().get("role");
                                    if (arrayList != null) {
                                        role.addAll(arrayList);
                                    }
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(user_home_fragment.this.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
            databaseReference
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                String img = documentSnapshot.getString("image");
                                if (img != null)
                                    Picasso.get().load(img).into(imageView);
                                else
                                    imageView.setImageResource(R.drawable.noimage);
                            }
                        }
                    });
        }


        findDateCardView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (role.contains("Pet Owner") || role.contains("Veterinarian")) {
                    FirebaseFirestore.getInstance().collection("Pet")
                            .whereEqualTo("pet_breeder", firebaseUser.getUid()).whereEqualTo("displayFor", "forDating")
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments();
                                    if (dsList.size() == 0) {
                                        AlertDialog.Builder builder3 = new AlertDialog.Builder(getContext());
                                        View view = View.inflate(getContext(), R.layout.screen_custom_alert, null);
                                        builder3.setCancelable(false);
                                        TextView title = view.findViewById(R.id.screen_custom_alert_title);
                                        TextView loadingText = view.findViewById(R.id.screen_custom_alert_loadingText);
                                        loadingText.setVisibility(View.GONE);
                                        AppCompatImageView imageViewCompat = view.findViewById(R.id.appCompatImageView);
                                        imageViewCompat.setVisibility(View.VISIBLE);
                                        imageViewCompat.setImageDrawable(getResources().getDrawable(R.drawable.screen_alert_image_error_border));
                                        GifImageView gif = view.findViewById(R.id.screen_custom_alert_gif);
                                        gif.setVisibility(View.GONE);
                                        TextView message = view.findViewById(R.id.screen_custom_alert_message);
                                        title.setText("No pets for dating exist");
                                        message.setText("Note: You don't have a profile for your pet.. Please click the GO TO PANEL button and click Create pet profile");
                                        LinearLayout buttonLayout = view.findViewById(R.id.screen_custom_alert_buttonLayout);
                                        buttonLayout.setVisibility(View.VISIBLE);
                                        MaterialButton cancel, okay;
                                        cancel = view.findViewById(R.id.screen_custom_dialog_btn_cancel);
                                        okay = view.findViewById(R.id.screen_custom_alert_dialog_btn_done);
                                        okay.setText("Go to panel");
                                        okay.setBackgroundColor(Color.parseColor("#F6B75A"));
                                        okay.setTextColor(Color.WHITE);
                                        builder3.setView(view);
                                        AlertDialog alert4 = builder3.create();
                                        alert4.show();
                                        alert4.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        cancel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                alert4.dismiss();
                                            }
                                        });
                                        okay.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                alert4.dismiss();
                                                startActivity(new Intent(getContext(), user_breeder_shop_panel.class));
                                                getActivity().finish();
                                            }
                                        });
                                    } else {
                                        startActivity(new Intent(getContext(), pet_date_swipe.class));
                                    }

                                }
                            });


                } else {
                    Toast.makeText(getContext(), "Only a pet owner can access this!", Toast.LENGTH_SHORT).show();
                }


            }
        });
        petSaleCardView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), m_market_place_container.class));
            }
        });
        profilePictureCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), user_profile_account_edit.class));
            }
        });
        addPetCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), user_breeder_shop_panel.class));
            }
        });

        recommendProducts();

    }


    int counterMain=0;
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void recommendProducts() {

        // Get the user's ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance().collection("Likes")
                .whereEqualTo("likedBy",userId).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null) return;
                        if(value!=null){
                            maybeLayout.setVisibility(View.VISIBLE);
                            List<DocumentSnapshot> list = value.getDocuments();
                            List<RecommendedProduct> recommended = new ArrayList<>();
                            Set<String> likedProducts = new HashSet<>(); // create a set to store the liked products

                            for(DocumentSnapshot s: list) {
                                counterMain++;
                                String type = s.getString("category");
                                String prod_id = s.getString("product_id");
                                Log.d("RecommendedLikes",prod_id);
                                likedProducts.add(prod_id); // add the liked products to the set
                                if(type.equals("forSale")){
                                    FirebaseFirestore.getInstance().collection("Pet")
                                            .document(prod_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    if(documentSnapshot.exists()){

                                                        FirebaseFirestore.getInstance().collection("Pet")
                                                                .whereEqualTo("displayFor",type)
                                                                .whereEqualTo("pet_breed",documentSnapshot.getString("pet_breed"))
                                                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                        if(queryDocumentSnapshots!=null){
                                                                            List<DocumentSnapshot> listSub = queryDocumentSnapshots.getDocuments();
                                                                            int counterSub=0;
                                                                            for(DocumentSnapshot s: listSub){
                                                                                counterSub++;
                                                                                if(!likedProducts.contains(s.getString("id")) && !recommended.contains(s.getString("id"))){ // check if the product is already liked or recommended
                                                                                    recommended.add(new RecommendedProduct(s.getString("id"),s.getString("displayFor")));
                                                                                    if(counterSub == listSub.size() && counterMain == list.size()){
                                                                                       displayProductsRecommended(recommended);
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                });

                                                    }
                                                }
                                            });
                                }
                                else if(type.equals("Dog Accessories")||type.equals("Medicine")){
                                    FirebaseFirestore.getInstance().collection("Pet")
                                            .document(prod_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    if(documentSnapshot.exists()){
                                                        FirebaseFirestore.getInstance().collection("Pet")
                                                                .whereEqualTo("prod_category",type)
                                                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                        if(queryDocumentSnapshots!=null){
                                                                            List<DocumentSnapshot> list1 = queryDocumentSnapshots.getDocuments();
                                                                            int counterSub = 0;
                                                                            for(DocumentSnapshot a: list1){
                                                                                counterSub++;
                                                                                if(!likedProducts.contains(a.getString("id")) && !recommended.contains(a.getString("id"))){ // check if the product is already liked or recommended
                                                                                    recommended.add(new RecommendedProduct(a.getString("id"),a.getString("prod_category")));
                                                                                    if(counterSub == list1.size() && counterMain == list.size()){
                                                                                        displayProductsRecommended(recommended);
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                             });
                                                    }
                                                }
                                            });
                                }
                                else if(type.equals("Service")){
                                    FirebaseFirestore.getInstance().collection("Services")
                                            .document(prod_id)
                                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    if(documentSnapshot.exists()){
                                                        FirebaseFirestore.getInstance().collection("Services")
                                                                .whereEqualTo("serviceType",documentSnapshot.getString("serviceType"))
                                                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                        if(!queryDocumentSnapshots.isEmpty()){
                                                                            int counterSub = 0;
                                                                            for(DocumentSnapshot a:queryDocumentSnapshots.getDocuments()){
                                                                                counterSub++;
                                                                                if(!likedProducts.contains(a.getString("id")) && !recommended.contains(a.getString("id"))){ // check if the product is already liked or recommended
                                                                                    recommended.add(new RecommendedProduct(a.getString("id"),a.getString("serviceType")));
                                                                                    if(counterSub == queryDocumentSnapshots.size() && counterMain == list.size()){
                                                                                        displayProductsRecommended(recommended);
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                        else{
                            maybeLayout.setVisibility(View.GONE);
                        }
                    }
                });

    }
    likes_adapter adapters;
    private final Set<String> addedIds = new HashSet<>();
    private void displayProductsRecommended(List<RecommendedProduct> recommended) {
        for (RecommendedProduct r : recommended) {
            if (!addedIds.contains(r.getId())) {
                addedIds.add(r.getId());
                if (r.getType().equals("Veterinarian Service") || r.getType().equals("Shooter Service")) {
                    FirebaseFirestore.getInstance().collection("Services")
                            .document(r.getId())
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        likes_class like = new likes_class(r.getId(), "", r.getId(),
                                                documentSnapshot.getString("shooter_id"),
                                                r.getType(), null);
                                        adapters.add_to_cart(like);
                                    }
                                }
                            });

                }
                else if (r.getType().equals("Dog Accessories") || r.getType().equals("Medicine")){
                    FirebaseFirestore.getInstance().collection("Pet")
                            .document(r.getId())
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if(documentSnapshot.exists()){
                                        likes_class  like = new likes_class(r.getId(),"",r.getId(),
                                                documentSnapshot.getString("vet_id"),
                                                r.getType(),null);
                                        adapters.add_to_cart(like);
                                    }
                                }
                            });
                }
                else if (r.getType().equals("forSale")){
                    FirebaseFirestore.getInstance().collection("Pet")
                            .document(r.getId())
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if(documentSnapshot.exists()){
                                        likes_class  like = new likes_class(r.getId(),"",r.getId(),
                                                documentSnapshot.getString("pet_breeder"),
                                                r.getType(),null);
                                        adapters.add_to_cart(like);
                                    }
                                }
                            });
                }
            }
        }
       youMayLike_recycler.setAdapter(adapters);
    }
    private void getPetForYou() {
      FirebaseFirestore.getInstance().collection("Pet")
                .whereEqualTo("displayFor","forSale")
              .whereEqualTo("show",true)
              .addSnapshotListener(new EventListener<QuerySnapshot>() {
                  @Override
                  public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(error!=null){
                        return;
                    }
                    if(value!=null){
                        adapter.clearList();
                        List<DocumentSnapshot> dsList = value.getDocuments();
                        for(DocumentSnapshot ds:dsList){
                            if(ds!=null){

                                PetSaleClass productModel = ds.toObject(PetSaleClass.class);
                                adapter.addPetDisplay(productModel);
                            }
                        }
                        if(dsList.size() == 0)
                            for_you_layout.setVisibility(View.GONE);
                        else{
                            for_you_layout.setVisibility(View.VISIBLE);
                        }
                    }
                  }
              });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (BreederBottomNavigation != null) {
            BreederBottomNavigation.getMenu().getItem(0).setEnabled(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (BreederBottomNavigation != null) {
            BreederBottomNavigation.getMenu().getItem(0).setEnabled(true);
        }
    }
}
