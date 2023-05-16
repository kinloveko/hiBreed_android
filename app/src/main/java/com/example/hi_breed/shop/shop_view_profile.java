package com.example.hi_breed.shop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.BaseActivity;
import com.example.hi_breed.classesFile.ShopClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class shop_view_profile extends BaseActivity {


    FirebaseUser firebaseUser;


    TextView viewShopName,viewShopVerified,viewShopReviews,viewShopLink,editShop;
    ImageView imageShopProfile,imageShopBackground;
    LinearLayout backLayoutShop;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_shop_view_profile);



        Window w = getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        imageShopBackground = findViewById(R.id.view_shop_coverImageID);
        imageShopProfile = findViewById(R.id.view_shop_Profile);


        backLayoutShop = findViewById(R.id.backLayoutShop);

        viewShopLink = findViewById(R.id.view_shop_link);
        viewShopName = findViewById(R.id.view_shop_name);
        viewShopVerified = findViewById(R.id.view_shop_verified);
        viewShopReviews = findViewById(R.id.view_shop_reviews);
        editShop = findViewById(R.id.editShop);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseFirestore.getInstance().collection("Shop")
                .document(firebaseUser.getUid())
                        .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            DocumentSnapshot snapshot = task.getResult();
                                            ShopClass shopName = snapshot.toObject(ShopClass.class);
                                            viewShopName.setText(shopName.getShopName());
                                            viewShopLink.setText("hiBreed.ph/"+shopName.getShopName());
                                            viewShopLink.setTextSize(11);
                                            viewShopReviews.setTextSize(11);
                                            if(firebaseUser.isEmailVerified()){
                                                viewShopVerified.setText("Verified");
                                            }
                                            if(shopName.getProfImage().equals("") ||shopName.getProfImage() == null)
                                            Glide.with( shop_view_profile.this)
                                                    .load(R.drawable.noimage)
                                                    .into(imageShopProfile);
                                            else
                                                Glide.with( shop_view_profile.this)
                                                        .load(shopName.getProfImage())
                                                        .into(imageShopProfile);

                                            if(shopName.getBackgroundImage().equals("") || shopName.getBackgroundImage() == null) {
                                                Glide.with( shop_view_profile.this)
                                                        .load(R.drawable.nobackground)
                                                        .into(imageShopBackground);
                                            }
                                            else {
                                                Glide.with( shop_view_profile.this)
                                                        .load(shopName.getBackgroundImage())
                                                        .into(imageShopBackground);
                                            }
                                        }
                                    }
                                });


        editShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(shop_view_profile.this,shop_edit_profile.class));
            }
        });

    backLayoutShop.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            shop_view_profile.this.onBackPressed();
        }
    });


        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        display(FirebaseAuth.getInstance().getCurrentUser().getUid());

    }

    private void display(String id) {
        List<String> fragment = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("Shop")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot snapshot = task.getResult();
                            ShopClass shopName = snapshot.toObject(ShopClass.class);
                            viewShopName.setText(shopName.getShopName());

                            viewShopReviews.setTextSize(11);
                            if(firebaseUser.isEmailVerified()){
                                viewShopVerified.setText("Verified");
                            }
                            Picasso.get().load(shopName.getProfImage()).placeholder(R.drawable.noimage).into(imageShopProfile);
                            Picasso.get().load(shopName.getBackgroundImage()).placeholder(R.drawable.nobackground).into(imageShopBackground);
                        }
                    }
                });

        FirebaseFirestore.getInstance().collection("Pet")
                .whereEqualTo("pet_breeder",id)
                .whereEqualTo("displayFor","forSale")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<DocumentSnapshot> list = task.getResult().getDocuments();
                            if(list.size() !=0){
                                fragment.add("Pet");
                            }
                            initView(id,fragment);
                        }
                    }
                });

        FirebaseFirestore.getInstance().collection("Pet")
                .whereEqualTo("vet_id",id)
                .whereEqualTo("displayFor","forProducts")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<DocumentSnapshot> list = task.getResult().getDocuments();
                            if(list.size() !=0){
                                fragment.add("Products");
                            }
                            initView(id,fragment);
                        }
                    }
                });

        FirebaseFirestore.getInstance().collection("Services")
                .whereEqualTo("shooter_id",id)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<DocumentSnapshot> list = task.getResult().getDocuments();
                            if(list.size() !=0){
                                fragment.add("Shooter");
                            }
                            initView(id,fragment);
                        }

                    }
                });



    }

    TabLayout tabLayout;
    ViewPager2 viewPager;
    ViewPagerAdapter adapterV;
    private String id;
    private void initView(String breeder,List<String> fragmentList) {


        adapterV = new ViewPagerAdapter(this.getSupportFragmentManager(),
                this.getLifecycle());

        Bundle args = new Bundle();
        args.putString("breeder", breeder);

        if(fragmentList.contains("Pet"))
        {
            shop_pet_for_sale_fragment pet  = new shop_pet_for_sale_fragment();
            pet.setArguments(args);
            adapterV.addFragment(pet,"PET FOR SALE");
        }
        if(fragmentList.contains("Products")){
            shop_product_fragment product = new shop_product_fragment();
            product.setArguments(args);
            adapterV.addFragment(product,"PRODUCTS FOR SALE");
        }
        if(fragmentList.contains("Shooter"))
        {
            shop_service_fragment shooter = new shop_service_fragment();
            shooter.setArguments(args);
            adapterV.addFragment(shooter,"SERVICE");
        }
        viewPager.setAdapter(adapterV);
        viewPager.setOffscreenPageLimit(1);
        new TabLayoutMediator(tabLayout,viewPager,
                (tab,position)->{
                    tab.setText(adapterV.fragmentTitle.get(position));
                }).attach();

        for(int i =0; i<tabLayout.getTabCount();i++){
            TextView tv = (TextView) LayoutInflater.from(this)
                    .inflate(R.layout.shop_custom_tab,null);
            tabLayout.getTabAt(i).setCustomView(tv);
        }

    }


    class ViewPagerAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        public void addFragment(Fragment fragment, String title){
            fragmentList.add(fragment);
            fragmentTitle.add(title);
        }


        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }


        @Override
        public int getItemCount() {
            return fragmentList.size();
        }

    }


}