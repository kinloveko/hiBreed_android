package com.example.hi_breed.marketplace;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.BaseActivity;
import com.example.hi_breed.classesFile.BreedClass;
import com.example.hi_breed.classesFile.item;
import com.example.hi_breed.classesFile.size_class;
import com.example.hi_breed.shooter.filter_search;
import com.example.hi_breed.search.search_dashboard_home;
import com.example.hi_breed.userFile.cart.add_to_cart;
import com.example.hi_breed.userFile.dashboard.user_dashboard;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class m_market_place_container extends BaseActivity {
    TextView search_searchEditID;
    ImageView cart;
    LinearLayout backLayoutPet;
    ViewPager2 viewPager;
    ViewPagerAdapter adapterV;
    LinearLayout searchLinear;
    TabLayout tabLayout;
    ImageView filter;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.marketplace_activity);



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

        filter = findViewById(R.id.filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFilter();
            }
        });
        search_searchEditID = findViewById(R.id.search_searchEditID);
        search_searchEditID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(m_market_place_container.this, search_dashboard_home.class));
            }
        });
        searchLinear = findViewById(R.id.searchLinear);
        searchLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(m_market_place_container.this, search_dashboard_home.class));
            }
        });

        cart = findViewById(R.id.cart);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(m_market_place_container.this, add_to_cart.class));

            }
        });
        backLayoutPet = findViewById(R.id.backLayoutPet);
        backLayoutPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(m_market_place_container.this, user_dashboard.class);
                // set the new task and clear flags
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
        initView();
    }

    EditText locationEdit,colorEdit,shopEdit;
    RelativeLayout locationFilter,genderLayout,gender_edit_layout,shopEditLayout,
            locationEditLayout,service_vet_layout,
            pet_color_layout,pet_size_layout,
            pet_breed_layout,
            service_shooter_layout,
            breed_edit_layout,
            size_edit_layout,
            color_edit_layout,
            shopFilter,
            productFilter,
            petFilter,
            servicesFilter,
            product_dog_accessories_layout,
            product_medicine_layout,
            applyFilter;

    TextView breedTextView,genderTextView,
    sizeTextView;

    ImageView locationCheck, service_shooter,pet_gender,
            product_dog_accessories,pet_breed,
    pet_size,
            pet_color,
    product_medicine,
    service_vet,
    servicesCheck,
            shopCheck,
            productCheck,
            petCheck;

    int count = 0;
    String category="";
    String color="";
    String breed="";
    String size= "";
    String gender="";
    TextView locationText;
    String shop = "";
    String location = "";
    Query serviceQuery ;
    private void viewFilter() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this,R.layout.filter_layout,null);

        if(view!=null){
            service_vet_layout = view.findViewById(R.id.service_vet_layout);
            service_shooter_layout = view.findViewById(R.id.service_shooter_layout);
            service_shooter = view.findViewById(R.id.service_vet);
            service_vet = view.findViewById(R.id.service_shooter);

            shopEditLayout = view.findViewById(R.id.shopEditLayout);
            shopEdit = view.findViewById(R.id.shopEdit);

            pet_breed = view.findViewById(R.id.pet_breed);
            pet_size = view.findViewById(R.id.pet_size);
            pet_color = view.findViewById(R.id.pet_color);

            breed_edit_layout = view.findViewById(R.id.breed_edit_layout);
            size_edit_layout = view.findViewById(R.id.size_edit_layout);

            color_edit_layout = view.findViewById(R.id.color_edit_layout);
            colorEdit = view.findViewById(R.id.colorEdit);
            pet_color_layout = view.findViewById(R.id.pet_color_layout);

            pet_size_layout = view.findViewById(R.id.pet_size_layout);
            pet_breed_layout = view.findViewById(R.id.pet_breed_layout);

            breedTextView = view.findViewById(R.id.breedTextView);
            sizeTextView = view.findViewById(R.id.sizeTextView);

            genderLayout = view.findViewById(R.id.genderLayout);
            pet_gender = view.findViewById(R.id.pet_gender);
            gender_edit_layout = view.findViewById(R.id.gender_edit_layout);
            genderTextView = view.findViewById(R.id.genderTextView);

            product_dog_accessories_layout = view.findViewById(R.id.product_dog_accessories_layout);
            product_medicine_layout = view.findViewById(R.id.product_medicine_layout);
            product_dog_accessories = view.findViewById(R.id.product_dog_accessories);
            product_medicine = view.findViewById(R.id.product_medicine);

            locationEdit = view.findViewById(R.id.locationEdit);
            locationEditLayout = view.findViewById(R.id.locationEditLayout);
            applyFilter = view.findViewById(R.id.applyFilter);

            locationText = view.findViewById(R.id.locationText);
            locationCheck = view.findViewById(R.id.locationCheck);
            locationFilter = view.findViewById(R.id.locationFilter);

            shopFilter = view.findViewById(R.id.shopFilter);
            shopCheck = view.findViewById(R.id.shopCheck);

            productFilter = view.findViewById(R.id.productFilter);
            productCheck = view.findViewById(R.id.productCheck);

            petCheck = view.findViewById(R.id.petCheck);
            petFilter = view.findViewById(R.id.petFilter);

            servicesFilter = view.findViewById(R.id.servicesFilter);
            servicesCheck = view.findViewById(R.id.servicesCheck);

        }

        builder.setView(view);

        AlertDialog alert2 = builder.create();
        alert2.show();
        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        locationFilter.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                // Change the ImageDrawable of locationCheck
                if(locationCheck.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_circle_check).getConstantState()) {
                    locationCheck.setImageResource(R.drawable.icon_not_check);
                    locationEditLayout.setVisibility(View.GONE);
                    if(!locationEdit.getText().toString().isEmpty())
                    locationEdit.getText().clear();

                } else {
                    locationCheck.setImageResource(R.drawable.icon_circle_check);
                    locationEditLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        shopFilter.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"UseCompatLoadingForDrawables"})
            @Override
            public void onClick(View v) {
                // Change the ImageDrawable of shopCheck
                if(shopCheck.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_circle_check).getConstantState()) {

                    shopCheck.setImageResource(R.drawable.icon_not_check);
                    shopEditLayout.setVisibility(View.GONE);
                    shopEdit.getText().clear();
                    locationFilter.setEnabled(true);
                    locationText.setTextColor(ColorStateList.valueOf(Color.parseColor("#BD101417")));

                } else {
                    servicesCheck.setImageResource(R.drawable.icon_not_check);
                    service_vet_layout.setVisibility(View.GONE);
                    service_shooter_layout.setVisibility(View.GONE);
                    service_vet.setImageResource(R.drawable.icon_check_boxs);
                    service_shooter.setImageResource(R.drawable.icon_check_boxs);

                    shopCheck.setImageResource(R.drawable.icon_circle_check);
                    shopEditLayout.setVisibility(View.VISIBLE);

                    locationFilter.setEnabled(false);
                    locationText.setTextColor(Color.parseColor("#BBEFEFEF"));

                    productCheck.setImageResource(R.drawable.icon_not_check);
                    product_dog_accessories_layout.setVisibility(View.GONE);
                    product_medicine_layout.setVisibility(View.GONE);
                    product_dog_accessories.setImageResource(R.drawable.icon_check_boxs);
                    product_medicine.setImageResource(R.drawable.icon_check_boxs);

                    petCheck.setImageResource(R.drawable.icon_not_check);
                    pet_breed_layout.setVisibility(View.GONE);
                    pet_size_layout.setVisibility(View.GONE);
                    pet_color_layout.setVisibility(View.GONE);
                    pet_breed.setImageResource(R.drawable.icon_check_boxs);
                    pet_size.setImageResource(R.drawable.icon_check_boxs);
                    pet_gender.setImageResource(R.drawable.icon_check_boxs);
                    pet_color.setImageResource(R.drawable.icon_check_boxs);
                    breedTextView.setText("");
                    sizeTextView.setText("");
                    genderTextView.setText("");
                    colorEdit.getText().clear();
                    breed_edit_layout.setVisibility(View.GONE);
                    gender_edit_layout.setVisibility(View.GONE);
                    size_edit_layout.setVisibility(View.GONE);
                    color_edit_layout.setVisibility(View.GONE);
                    genderLayout.setVisibility(View.GONE);
                }
            }
        });
        productFilter.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                // Change the ImageDrawable of productCheck
                if(productCheck.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_circle_check).getConstantState()) {
                    productCheck.setImageResource(R.drawable.icon_not_check);
                    product_dog_accessories_layout.setVisibility(View.GONE);
                    product_medicine_layout.setVisibility(View.GONE);
                    //reset checks
                    product_dog_accessories.setImageResource(R.drawable.icon_check_boxs);
                    product_medicine.setImageResource(R.drawable.icon_check_boxs);

                } else {

                    productCheck.setImageResource(R.drawable.icon_circle_check);
                    product_dog_accessories_layout.setVisibility(View.VISIBLE);
                    product_medicine_layout.setVisibility(View.VISIBLE);
                    product_dog_accessories_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(product_dog_accessories.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_check_boxs).getConstantState()){
                                product_dog_accessories.setImageResource(R.drawable.icon_check_click);
                            }
                            else{
                                product_dog_accessories.setImageResource(R.drawable.icon_check_boxs);
                            }
                        }
                    });

                    product_medicine_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(product_medicine.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_check_boxs).getConstantState()){
                                product_medicine.setImageResource(R.drawable.icon_check_click);
                            }
                            else{
                                product_medicine.setImageResource(R.drawable.icon_check_boxs);
                            }
                        }
                    });
                    servicesCheck.setImageResource(R.drawable.icon_not_check);
                    service_vet_layout.setVisibility(View.GONE);
                    service_shooter_layout.setVisibility(View.GONE);
                    service_vet.setImageResource(R.drawable.icon_check_boxs);
                    service_shooter.setImageResource(R.drawable.icon_check_boxs);

                    shopCheck.setImageResource(R.drawable.icon_not_check);
                    shopEditLayout.setVisibility(View.GONE);
                    shopEdit.getText().clear();
                    petCheck.setImageResource(R.drawable.icon_not_check);
                    pet_breed_layout.setVisibility(View.GONE);
                    pet_size_layout.setVisibility(View.GONE);
                    pet_color_layout.setVisibility(View.GONE);
                    pet_breed.setImageResource(R.drawable.icon_check_boxs);
                    pet_size.setImageResource(R.drawable.icon_check_boxs);
                    pet_gender.setImageResource(R.drawable.icon_check_boxs);
                    pet_color.setImageResource(R.drawable.icon_check_boxs);
                    breedTextView.setText("");
                    sizeTextView.setText("");
                    genderTextView.setText("");
                    colorEdit.getText().clear();
                    breed_edit_layout.setVisibility(View.GONE);
                    gender_edit_layout.setVisibility(View.GONE);
                    size_edit_layout.setVisibility(View.GONE);
                    color_edit_layout.setVisibility(View.GONE);
                    genderLayout.setVisibility(View.GONE);

                }
            }
        });
        petFilter.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                // Change the ImageDrawable of petCheck
                if(petCheck.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_circle_check).getConstantState()) {
                    petCheck.setImageResource(R.drawable.icon_not_check);
                    pet_breed.setImageResource(R.drawable.icon_check_boxs);
                    pet_size.setImageResource(R.drawable.icon_check_boxs);
                    pet_color.setImageResource(R.drawable.icon_check_boxs);
                    pet_gender.setImageResource(R.drawable.icon_check_boxs);
                    breed_edit_layout.setVisibility(View.GONE);
                    size_edit_layout.setVisibility(View.GONE);
                    color_edit_layout.setVisibility(View.GONE);
                    pet_breed_layout.setVisibility(View.GONE);
                    pet_size_layout.setVisibility(View.GONE);
                    gender_edit_layout.setVisibility(View.GONE);
                    pet_color_layout.setVisibility(View.GONE);
                    genderLayout.setVisibility(View.GONE);
                    breedTextView.setText("");
                    sizeTextView.setText("");
                    genderTextView.setText("");
                    colorEdit.getText().clear();
                } else {
                    servicesCheck.setImageResource(R.drawable.icon_not_check);
                    service_vet_layout.setVisibility(View.GONE);
                    service_shooter_layout.setVisibility(View.GONE);
                    service_vet.setImageResource(R.drawable.icon_check_boxs);
                    service_shooter.setImageResource(R.drawable.icon_check_boxs);

                    shopCheck.setImageResource(R.drawable.icon_not_check);
                    shopEditLayout.setVisibility(View.GONE);
                    shopEdit.getText().clear();

                    productCheck.setImageResource(R.drawable.icon_not_check);
                    product_dog_accessories_layout.setVisibility(View.GONE);
                    product_medicine_layout.setVisibility(View.GONE);
                    product_dog_accessories.setImageResource(R.drawable.icon_check_boxs);
                    product_medicine.setImageResource(R.drawable.icon_check_boxs);


                    petCheck.setImageResource(R.drawable.icon_circle_check);
                    pet_breed_layout.setVisibility(View.VISIBLE);
                    pet_size_layout.setVisibility(View.VISIBLE);
                    pet_color_layout.setVisibility(View.VISIBLE);
                    genderLayout.setVisibility(View.VISIBLE);
                    genderLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(pet_gender.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_check_boxs).getConstantState()){
                                pet_gender.setImageResource(R.drawable.icon_check_click);
                                gender_edit_layout.setVisibility(View.VISIBLE);
                                gender_edit_layout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                       genderCategory();
                                    }
                                });
                            }
                            else{
                                genderTextView.setText("");
                                gender_edit_layout.setVisibility(View.GONE);
                                pet_gender.setImageResource(R.drawable.icon_check_boxs);
                            }
                        }
                    });
                    pet_breed_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(pet_breed.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_check_boxs).getConstantState()){
                                pet_breed.setImageResource(R.drawable.icon_check_click);
                                breed_edit_layout.setVisibility(View.VISIBLE);
                                breed_edit_layout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        breedCategory();
                                    }
                                });
                            }
                            else{
                                breedTextView.setText("");
                                breed_edit_layout.setVisibility(View.GONE);
                                pet_breed.setImageResource(R.drawable.icon_check_boxs);
                            }
                        }
                    });
                    pet_size_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(pet_size.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_check_boxs).getConstantState()){
                                pet_size.setImageResource(R.drawable.icon_check_click);
                                size_edit_layout.setVisibility(View.VISIBLE);
                                size_edit_layout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        selectSize();
                                    }
                                });
                            }
                            else{
                                sizeTextView.setText("");
                                size_edit_layout.setVisibility(View.GONE);
                                pet_size.setImageResource(R.drawable.icon_check_boxs);
                            }
                        }
                    });
                    pet_color_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(pet_color.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_check_boxs).getConstantState()){
                                pet_color.setImageResource(R.drawable.icon_check_click);
                                color_edit_layout.setVisibility(View.VISIBLE);

                            }
                            else{
                                colorEdit.getText().clear();
                                color_edit_layout.setVisibility(View.GONE);
                                pet_color.setImageResource(R.drawable.icon_check_boxs);
                            }
                        }
                    });


                }
            }
        });
        servicesFilter.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                // Change the ImageDrawable of all ImageViews that contain R.drawable.icon_check_click
                if(servicesCheck.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_circle_check).getConstantState()) {
                    servicesCheck.setImageResource(R.drawable.icon_not_check);
                    service_vet_layout.setVisibility(View.GONE);
                    service_shooter_layout.setVisibility(View.GONE);
                    //to reset checks
                    service_vet.setImageResource(R.drawable.icon_check_boxs);
                    service_shooter.setImageResource(R.drawable.icon_check_boxs);

                } else {
                    service_vet_layout.setVisibility(View.VISIBLE);
                    service_shooter_layout.setVisibility(View.VISIBLE);

                    service_vet_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(service_vet.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_check_boxs).getConstantState()){
                                service_vet.setImageResource(R.drawable.icon_check_click);
                            }
                            else{
                                service_vet.setImageResource(R.drawable.icon_check_boxs);
                            }
                        }
                    });
                    service_shooter_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(service_shooter.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_check_boxs).getConstantState()){
                                service_shooter.setImageResource(R.drawable.icon_check_click);
                            }
                            else{
                                service_shooter.setImageResource(R.drawable.icon_check_boxs);
                            }
                        }
                    });


                    servicesCheck.setImageResource(R.drawable.icon_circle_check);

                    shopCheck.setImageResource(R.drawable.icon_not_check);
                    shopEditLayout.setVisibility(View.GONE);
                    shopEdit.getText().clear();

                    productCheck.setImageResource(R.drawable.icon_not_check);
                    product_dog_accessories_layout.setVisibility(View.GONE);
                    product_medicine_layout.setVisibility(View.GONE);
                    product_dog_accessories.setImageResource(R.drawable.icon_check_boxs);
                    product_medicine.setImageResource(R.drawable.icon_check_boxs);

                    petCheck.setImageResource(R.drawable.icon_not_check);
                    pet_breed_layout.setVisibility(View.GONE);
                    pet_size_layout.setVisibility(View.GONE);
                    pet_color_layout.setVisibility(View.GONE);
                    pet_breed.setImageResource(R.drawable.icon_check_boxs);
                    pet_size.setImageResource(R.drawable.icon_check_boxs);
                    pet_gender.setImageResource(R.drawable.icon_check_boxs);
                    pet_color.setImageResource(R.drawable.icon_check_boxs);
                    breedTextView.setText("");
                    sizeTextView.setText("");
                    genderTextView.setText("");
                    colorEdit.getText().clear();
                    breed_edit_layout.setVisibility(View.GONE);
                    gender_edit_layout.setVisibility(View.GONE);
                    size_edit_layout.setVisibility(View.GONE);
                    color_edit_layout.setVisibility(View.GONE);
                    genderLayout.setVisibility(View.GONE);

                }
            }
        });
        applyFilter.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                if(servicesCheck.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_not_check).getConstantState() &&
                        petCheck.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_not_check).getConstantState() &&
                        shopCheck.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_not_check).getConstantState() &&
                        locationCheck.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_not_check).getConstantState() &&
                        productCheck.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_not_check).getConstantState()) {
                    Toast.makeText(m_market_place_container.this, "Please select one before clicking apply", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!(servicesCheck.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_not_check).getConstantState())) {
                    category = "Services";
                }
                else if(!(petCheck.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_not_check).getConstantState())){
                    category = "Pet";
                }
                else
                if(!(shopCheck.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_not_check).getConstantState())){
                    category = "Shop";

                } else
                if(!(productCheck.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_not_check).getConstantState())){
                    category = "Products";
                }
                else
                if(!(locationCheck.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_not_check).getConstantState())){
                    category = "Location";
                    location = locationEdit.getText().toString();
                }
                if (!TextUtils.isEmpty(category)) {


                    if (category.equals("Pet")){
                        // Get a reference to the "products" collection in Firestore
                        CollectionReference petRef = FirebaseFirestore.getInstance().collection("Pet");
                        Query petQuery = petRef.whereEqualTo("displayFor", "forSale");
                        if(pet_breed.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_check_boxs).getConstantState()&&
                                pet_size.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_check_boxs).getConstantState() &&
                                pet_color.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_check_boxs).getConstantState() &&
                                pet_gender.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_check_boxs).getConstantState()) {
                            Toast.makeText(m_market_place_container.this, "Please choose one or more of the categories listed below in pets", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else{
                            if(!(pet_breed.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_check_boxs).getConstantState())){

                                if(!breedTextView.getText().toString().equals("")){

                                    breed = breedTextView.getText().toString();
                                    if (breed != null && !breed.isEmpty()) {
                                        petQuery = petQuery.whereEqualTo("pet_breed", breed);
                                    }
                                }
                                else{

                                    breed = "";
                                    Toast.makeText(m_market_place_container.this, "Select breed before clicking apply", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            if(!(pet_size.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_check_boxs).getConstantState())){

                                if(!sizeTextView.getText().toString().equals("")){
                                    size = sizeTextView.getText().toString();
                                    if (size != null && !size.isEmpty()) {
                                        petQuery = petQuery.whereEqualTo("petSize", size);
                                    }
                                }
                                else{
                                    size= "";
                                    Toast.makeText(m_market_place_container.this, "Select size before clicking apply", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }

                            if(!(pet_gender.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_check_boxs).getConstantState())) {
                                if (!genderTextView.getText().toString().equals("")) {
                                    gender = genderTextView.getText().toString();
                                    if (gender != null && !gender.isEmpty()) {
                                        petQuery = petQuery.whereEqualTo("pet_gender", gender);
                                    }
                                }
                                else{
                                    gender = "";
                                    Toast.makeText(m_market_place_container.this, "Enter gender first before clicking apply", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        }

                        petQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<item> items = new ArrayList<>();
                                if(queryDocumentSnapshots!=null){
                                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                    if (list.size() != 0) {
                                        int itemCount = 0;
                                        for (DocumentSnapshot s : list) {

                                            if (!(locationCheck.getDrawable().getConstantState() == ContextCompat.getDrawable(m_market_place_container.this, R.drawable.icon_not_check).getConstantState())) {

                                                if(!TextUtils.isEmpty(locationEdit.getText().toString())){

                                                    if (!s.getString("address").toLowerCase().contains(locationEdit.getText().toString().toLowerCase())) {
                                                        // skip this item if its address does not match the filter
                                                        continue;
                                                    }

                                                    if(!(pet_color.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_check_boxs).getConstantState())){
                                                        if(!colorEdit.getText().toString().equals("")){
                                                            if(s.getString("pet_colorMarkings").toLowerCase().contains(colorEdit.getText().toString().toLowerCase())){

                                                                item item = new item(s.getId(), s.getString("pet_breeder"), s.getString("pet_price")
                                                                        , s.getString("pet_breed"), s.getString("pet_breed"),s.getString("address"), true);
                                                                items.add(item);

                                                                itemCount++;
                                                                // start activity                       if all items have been added
                                                                if (itemCount == list.size()) {
                                                                    gotoNextActivity(items,alert2);
                                                                    break;
                                                                }

                                                            }
                                                        }
                                                        else{
                                                            Toast.makeText(m_market_place_container.this, "Please enter color before clicking apply", Toast.LENGTH_SHORT).show();
                                                            return;
                                                        }
                                                    }

                                                    item item = new item(s.getId(), s.getString("pet_breeder"), s.getString("pet_price")
                                                            , s.getString("pet_breed"), s.getString("pet_breed"),s.getString("address"), true);
                                                    items.add(item);

                                                    itemCount++;
                                                    // start activity                       if all items have been added
                                                    if (itemCount == list.size()) {
                                                        gotoNextActivity(items,alert2);
                                                    }

                                                }else{
                                                    Toast.makeText(m_market_place_container.this, "Please enter a specific location", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                            }
                                            else{
                                                if(!(pet_color.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_check_boxs).getConstantState())){
                                                    if(!colorEdit.getText().toString().equals("")){
                                                        if(s.getString("pet_colorMarkings").toLowerCase().contains(colorEdit.getText().toString().toLowerCase())){

                                                            item item = new item(s.getId(), s.getString("pet_breeder"), s.getString("pet_price")
                                                                    , s.getString("pet_breed"), s.getString("pet_breed"),s.getString("address"), true);
                                                            items.add(item);

                                                            itemCount++;
                                                            // start activity                       if all items have been added
                                                            if (itemCount == list.size()) {
                                                                gotoNextActivity(items,alert2);
                                                                break;
                                                            }

                                                        }
                                                    }
                                                    else{
                                                        Toast.makeText(m_market_place_container.this, "Please enter color before clicking apply", Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }
                                                }else{

                                                    item item = new item(s.getId(), s.getString("pet_breeder"), s.getString("pet_price")
                                                            , s.getString("pet_breed"), s.getString("pet_breed"),s.getString("address"), true);
                                                    items.add(item);

                                                    itemCount++;
                                                    // start activity                       if all items have been added
                                                    if (itemCount == list.size()) {
                                                        gotoNextActivity(items,alert2);
                                                    }
                                                }
                                            }
                                        }
                                        gotoNextActivity(items,alert2);
                                    }
                                    else{
                                        gotoNextActivity(items,alert2);
                                        Toast.makeText(m_market_place_container.this, "No item matched", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                    }
                    else if(category.equals("Services")) {

                        CollectionReference service = FirebaseFirestore.getInstance().collection("Services");
                        serviceQuery = service.whereEqualTo("show",true);

                        boolean isShooterChecked = service_shooter.getDrawable().getConstantState() == ContextCompat.getDrawable(m_market_place_container.this, R.drawable.icon_check_boxs).getConstantState();
                        boolean isVetChecked = service_vet.getDrawable().getConstantState() == ContextCompat.getDrawable(m_market_place_container.this, R.drawable.icon_check_boxs).getConstantState();

                        if (service_shooter.getDrawable().getConstantState() == ContextCompat.getDrawable(m_market_place_container.this, R.drawable.icon_check_boxs).getConstantState() &&
                                service_vet.getDrawable().getConstantState() == ContextCompat.getDrawable(m_market_place_container.this, R.drawable.icon_check_boxs).getConstantState()) {
                            Toast.makeText(m_market_place_container.this, "Please choose one or more of the categories listed below in service.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {
                            if (isVetChecked) {

                                serviceQuery = serviceQuery.whereEqualTo("serviceType", "Veterinarian Service");

                            }
                            if (isShooterChecked) {

                                serviceQuery = serviceQuery.whereEqualTo("serviceType", "Shooter Service");
                            }
                        }

                        serviceQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<item> items = new ArrayList<>();

                                if (queryDocumentSnapshots != null) {
                                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                                    if (list.size() != 0) {
                                        int itemCount = 0;
                                        for (DocumentSnapshot s : queryDocumentSnapshots.getDocuments()) {
                                            if (!(locationCheck.getDrawable().getConstantState() == ContextCompat.getDrawable(m_market_place_container.this, R.drawable.icon_not_check).getConstantState())) {

                                           if(!TextUtils.isEmpty(locationEdit.getText().toString())){
                                               if (!s.getString("address").toLowerCase().contains(locationEdit.getText().toString().toLowerCase())) {
                                                   // skip this item if its address does not match the filter
                                                   continue;
                                               }
                                               // add item to the list
                                               item e = new item(s.getString("id"), s.getString("shooter_id"), s.getString("service_fee"), s.getString("serviceType"), s.getString("name"), s.getString("address"), true);
                                               items.add(e);
                                               itemCount++;
                                               // start activity                       if all items have been added
                                               if (itemCount == list.size()) {
                                                   gotoNextActivity(items,alert2);
                                               }

                                           }else{
                                               Toast.makeText(m_market_place_container.this, "Please enter a specific location", Toast.LENGTH_SHORT).show();
                                               return;
                                           }

                                           }
                                            else{
                                                // add item to the list
                                                item e = new item(s.getString("id"), s.getString("shooter_id"), s.getString("service_fee"), s.getString("serviceType"), s.getString("name"), s.getString("address"), true);
                                                items.add(e);
                                                itemCount++;
                                                // start activity                       if all items have been added
                                                if (itemCount == list.size()) {
                                                    gotoNextActivity(items,alert2);
                                                }
                                            }
                                        }
                                        gotoNextActivity(items,alert2);
                                    }
                                    else {

                                        gotoNextActivity(items,alert2);
                                        Toast.makeText(m_market_place_container.this, "No item matched", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                }
                            }
                        });
                    }
                    else if (category.equals("Shop")){
                        CollectionReference petRef = FirebaseFirestore.getInstance().collection("Shop");
                        Query shopQuery = petRef.whereEqualTo("show", true);
                        if(!shopEdit.getText().toString().equals("")){
                            shop = shopEdit.getText().toString();

                            shopQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    //if the ID is equal to shop name queued get and send to filter_search.class
                                    List<item> items = new ArrayList<>();
                                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                    if(list.size() !=0){
                                        for(DocumentSnapshot s : list){

                                            FirebaseFirestore.getInstance().collection("Search")
                                                    .whereEqualTo("seller_id",s.getString("breeder")).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                            List<DocumentSnapshot> lists = queryDocumentSnapshots.getDocuments();
                                                            if(lists.size() !=0){

                                                                for(DocumentSnapshot a:lists){
                                                                    FirebaseFirestore.getInstance().collection("Shop")
                                                                            .document(a.getString("seller_id"))
                                                                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                @Override
                                                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                    if(documentSnapshot.getString("shopName").toLowerCase().contains(shop.toLowerCase())){
                                                                                        item item = a.toObject(item.class);
                                                                                        items.add(item);
                                                                                        count++;
                                                                                        if(count == lists.size()){
                                                                                            gotoNextActivity(items,alert2);
                                                                                        }
                                                                                    }

                                                                                }
                                                                            });
                                                                }
                                                                gotoNextActivity(items,alert2);
                                                            }
                                                            else{
                                                                gotoNextActivity(items,alert2);
                                                             }
                                                        }
                                                    });
                                        }
                                    }
                                }
                            });
                        }
                        else{
                            shop = "";
                            Toast.makeText(m_market_place_container.this, "Enter Shop name first before clicking apply", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    else if (category.equals("Products")){
                        CollectionReference service = FirebaseFirestore.getInstance().collection("Pet");
                        Query productQuery = service.whereEqualTo("displayFor", "forProducts");

                        if(product_dog_accessories.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_check_boxs).getConstantState() &&
                                product_medicine.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_check_boxs).getConstantState()) {
                            Toast.makeText(m_market_place_container.this, "Please choose one or more of the categories listed below in product.", Toast.LENGTH_SHORT).show();
                        return;
                        }
                        else{
                            boolean isDogAccessoriesChecked = product_dog_accessories.getDrawable().getConstantState() == ContextCompat.getDrawable(m_market_place_container.this, R.drawable.icon_check_boxs).getConstantState();
                            boolean isMedicineChecked = product_medicine.getDrawable().getConstantState() == ContextCompat.getDrawable(m_market_place_container.this, R.drawable.icon_check_boxs).getConstantState();

                            if(isDogAccessoriesChecked){
                                productQuery = productQuery.whereEqualTo("prod_category","Dog Accessories");
                            }
                            if(isMedicineChecked){
                                productQuery = productQuery.whereEqualTo("prod_category", "Medicine");
                            }
                        }

                       productQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                List<item> items = new ArrayList<>();
                                if(queryDocumentSnapshots!=null) {

                                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                        if (list.size() != 0) {
                                            int itemCount = 0;
                                            for (DocumentSnapshot s : list) {

                                                if (!(locationCheck.getDrawable().getConstantState() == ContextCompat.getDrawable(m_market_place_container.this, R.drawable.icon_not_check).getConstantState())) {
                                                  if(!TextUtils.isEmpty(locationEdit.getText().toString())){
                                                        if (!s.getString("address").toLowerCase().contains(locationEdit.getText().toString().toLowerCase())) {
                                                            // skip this item if its address does not match the filter
                                                            continue;
                                                        }
                                                      // add item to the list
                                                      item a = new item(s.getString("id"), s.getString("vet_id"), s.getString("prod_price"),
                                                              s.getString("prod_category"), s.getString("prod_name"),s.getString("address"), true);
                                                      items.add(a);
                                                      itemCount++;

                                                      // start activity                       if all items have been added
                                                      if (itemCount == list.size()) {
                                                          gotoNextActivity(items,alert2);
                                                      }
                                                    }
                                                  else{
                                                      Toast.makeText(m_market_place_container.this, "Please enter a specific location", Toast.LENGTH_SHORT).show();
                                                      return;
                                                  }
                                                }else{
                                                    // add item to the list
                                                    item a = new item(s.getString("id"), s.getString("vet_id"), s.getString("prod_price"),
                                                            s.getString("prod_category"), s.getString("prod_name"),s.getString("address"), true);
                                                    items.add(a);
                                                    itemCount++;

                                                    // start activity                       if all items have been added
                                                    if (itemCount == list.size()) {
                                                        gotoNextActivity(items,alert2);
                                                    }
                                                }
                                            }
                                            gotoNextActivity(items,alert2);
                                        }
                                        else{
                                            gotoNextActivity(items,alert2);
                                        }
                                   }
                               }
                            });
                      }
                    else
                    if(category.equals("Location")){
                        CollectionReference location = FirebaseFirestore.getInstance().collection("Search");
                        Query locationQuery = location.whereEqualTo("show", true);

                        if (!(locationCheck.getDrawable().getConstantState() == ContextCompat.getDrawable(m_market_place_container.this, R.drawable.icon_not_check).getConstantState())) {

                            if(!TextUtils.isEmpty(locationEdit.getText().toString())) {
                                locationQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                        List<item> items = new ArrayList<>();
                                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                        int itemCount = 0;
                                        if(list.size() !=0){

                                            for(DocumentSnapshot s: list){

                                                if (!s.getString("address").toLowerCase().contains(locationEdit.getText().toString().toLowerCase())) {
                                                    // skip this item if its address does not match the filter
                                                    continue;
                                                }
                                                item a = new item(s.getString("id"), s.getString("seller_id"), s.getString("price"),
                                                        s.getString("category"), s.getString("type"),s.getString("address"), true);
                                                items.add(a);
                                                itemCount++;

                                                if(itemCount==list.size()){
                                                    gotoNextActivity(items,alert2);
                                                }

                                            }
                                            gotoNextActivity(items,alert2);
                                        }
                                        else{
                                            Toast.makeText(m_market_place_container.this, "No matched", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                });
                            }
                            else{
                                Toast.makeText(m_market_place_container.this, "Please enter a specific location", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }

                }
            category  = "";
            }
        });
    }

    private void gotoNextActivity(List<item> items,AlertDialog alert2) {
        Intent i = new Intent(m_market_place_container.this, filter_search.class);
        i.putExtra("mode", (Serializable) items);
        startActivity(i);
        alert2.dismiss();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    ListView genderListViews;
    EditText genderEditTexts;
    ArrayAdapter<String> genderArrayAdapters;
    @SuppressLint("UseCompatLoadingForDrawables")
    private void genderCategory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //ListView
        String[] gender = {"Male","Female"};

        View pop = View.inflate(m_market_place_container.this,R.layout.pet_add_search_dialog,null);
        RelativeLayout search_customBreedID =  pop.findViewById(R.id.search_customBreedID);
        search_customBreedID.setVisibility(View.GONE);
        TextInputLayout input = pop.findViewById(R.id.input);
        input.setVisibility(View.GONE);
        genderEditTexts = pop.findViewById(R.id.search_searchEditID);
        genderEditTexts.setVisibility(View.GONE);
        genderListViews = pop.findViewById(R.id.search_breedListView);
        MaterialButton cancel;
        cancel = pop.findViewById(R.id.search_dialog_btn_cancel);
        genderArrayAdapters = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,gender);
        genderListViews.setAdapter(genderArrayAdapters);
        builder.setView(pop);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        if(genderEditTexts.getText()!=null || genderEditTexts.getText().equals("")) {
            genderEditTexts.addTextChangedListener(genderFilterTextWatcher);
        }

        if(genderListViews.getCount() == 0 || genderListViews == null){

            genderListViews.setVisibility(View.GONE);
            genderListViews.setCacheColorHint(Color.TRANSPARENT);
            genderListViews.setBackground(new ColorDrawable(Color.TRANSPARENT));
            genderListViews.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));

        }
        else{

            genderListViews.setBackground(getDrawable(R.drawable.shape));
            genderListViews.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            genderListViews.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params = genderListViews.getLayoutParams();
            params.height = 400;
            genderListViews.setLayoutParams(params);
            genderListViews.requestLayout();

        }

        genderListViews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String holder = genderListViews.getItemAtPosition(position).toString();
                genderTextView.setText(holder);
                Toast.makeText(m_market_place_container.this, holder, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    RelativeLayout addCustoms;
    ListView listViews;
    EditText editTexts;
    ArrayAdapter<String> arrayAdapters;
    int counters = 0;
    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    private void selectSize() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //ListView
       size_class size = new size_class();

        View pop = View.inflate(m_market_place_container.this,R.layout.pet_add_search_dialog,null);
        addCustoms = pop.findViewById(R.id.search_customBreedID);
        addCustoms.setVisibility(View.GONE);
        TextInputLayout input = pop.findViewById(R.id.input);
        input.setVisibility(View.GONE);
        editTexts = pop.findViewById(R.id.search_searchEditID);
        editTexts.setVisibility(View.GONE);
        listViews = pop.findViewById(R.id.search_breedListView);
        MaterialButton cancel;
        cancel = pop.findViewById(R.id.search_dialog_btn_cancel);
        arrayAdapters = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,size.size);
        listViews.setAdapter(arrayAdapters);
        builder.setView(pop);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if(editTexts.getText()!=null || editTexts.getText().equals("")) {
            editTexts.addTextChangedListener(filterTextWatchers);
        }

        if(listViews.getCount() == 0 || listViews == null){
            listViews.setVisibility(View.GONE);
            listViews.setCacheColorHint(Color.TRANSPARENT);
            listViews.setBackground(new ColorDrawable(Color.TRANSPARENT));
            listViews.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        }
        else{
            listViews.setBackground(getDrawable(R.drawable.shape));
            listViews.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            listViews.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params = listViews.getLayoutParams();
            params.height = 400;
            listViews.setLayoutParams(params);
            listViews.requestLayout();
        }

        listViews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String holder = listViews.getItemAtPosition(position).toString();
                sizeTextView.setText(holder);
                Toast.makeText(m_market_place_container.this, holder, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


    RelativeLayout addCustom;
    ListView listView;
    EditText editText;
    ArrayAdapter<String> arrayAdapter;
    int counter = 0;
    private void breedCategory() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //ListView
        BreedClass breed = new BreedClass();

        View pop = View.inflate(m_market_place_container.this,R.layout.pet_add_search_dialog,null);
        addCustom = pop.findViewById(R.id.search_customBreedID);
        editText = pop.findViewById(R.id.search_searchEditID);
        listView = pop.findViewById(R.id.search_breedListView);
        MaterialButton cancel;
        cancel = pop.findViewById(R.id.search_dialog_btn_cancel);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,breed.breed);
        listView.setAdapter(arrayAdapter);
        builder.setView(pop);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if(editText.getText()!=null || editText.getText().equals("")) {
            editText.addTextChangedListener(filterTextWatcher);
        }

        if(listView.getCount() == 0 || listView == null){
            listView.setVisibility(View.GONE);
            listView.setCacheColorHint(Color.TRANSPARENT);
            listView.setBackground(new ColorDrawable(Color.TRANSPARENT));
            listView.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        }
        else{
            listView.setBackground(getDrawable(R.drawable.shape));
            listView.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            listView.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = 400;
            listView.setLayoutParams(params);
            listView.requestLayout();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String holder = listView.getItemAtPosition(position).toString();
                breedTextView.setText(holder);
                Toast.makeText(m_market_place_container.this, holder, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        addCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(m_market_place_container.this);
                View custom = View.inflate(m_market_place_container.this,R.layout.pet_add_custom_breed_dialog,null);
                EditText customEdit;
                MaterialButton cancel,done;
                customEdit = custom.findViewById(R.id.custom_breed_dialog_textin);
                cancel = custom.findViewById(R.id.custom_breed_dialog_btn_cancel);
                done = custom.findViewById(R.id.custom_breed_dialog_btn_done);

                builder.setView(custom);
                android.app.AlertDialog dialogAddCustom = builder.create();
                dialogAddCustom.show();
                dialogAddCustom.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(customEdit.getText().toString() == null || customEdit.getText().toString().equals("")){
                            counter = 0;
                        }
                        else{
                            breedTextView.setText(customEdit.getText().toString());
                            counter=1;
                        }
                        if (counter!=0){
                            Toast.makeText(m_market_place_container.this, customEdit.getText().toString(), Toast.LENGTH_SHORT).show();
                            dialogAddCustom.dismiss();
                        }
                        else{
                            Toast.makeText(m_market_place_container.this, "Cannot make any changes", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogAddCustom.dismiss();
                    }
                });

            }
        });
    }


    private final TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

            arrayAdapter.getFilter().filter(s);

        }
    };
    private final TextWatcher genderFilterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

            genderArrayAdapters.getFilter().filter(s);

        }
    };
    private final TextWatcher filterTextWatchers = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

            arrayAdapters.getFilter().filter(s);

        }
    };


    private void initView() {
        adapterV = new ViewPagerAdapter(this.getSupportFragmentManager(),
                this.getLifecycle());
        adapterV.addFragment(new m_pet_for_sale(),"PETS");
        adapterV.addFragment(new m_products_fragment(),"PRODUCTS");
        adapterV.addFragment(new m_services(),"SERVICES");

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


    class ViewPagerAdapter extends FragmentStateAdapter{
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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(m_market_place_container.this, user_dashboard.class);
        // set the new task and clear flags
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }
}