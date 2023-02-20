package com.example.hi_breed.userFile.cart;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.BaseActivity;
import com.example.hi_breed.classesFile.addToCart_adapter;
import com.example.hi_breed.classesFile.add_to_cart_class;
import com.example.hi_breed.classesFile.priceFormat;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class add_to_cart extends BaseActivity implements addToCart_adapter.ItemCheckedChangeListener {

    LinearLayout toolbarID;
    RecyclerView cart_recycler_view;
    CheckBox allCheckBox;
    TextView add_to_cart_total_value;
    Button add_to_cart_check_out_button;
    addToCart_adapter adapter;
    int count;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_add_to_cart);

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

        //adapter
        adapter = new addToCart_adapter(add_to_cart.this,this);
        //recycler
        cart_recycler_view = findViewById(R.id.cart_recycler_view);
        cart_recycler_view.setLayoutManager(new LinearLayoutManager(add_to_cart.this,LinearLayoutManager.VERTICAL,false));
        cart_recycler_view.setAdapter(adapter);
        getAddToCart();
        //CheckBox

        allCheckBox = findViewById(R.id.allCheckBox);
        //TextView
        add_to_cart_total_value = findViewById(R.id.add_to_cart_total_value);
        //Button
        add_to_cart_check_out_button =findViewById(R.id.add_to_cart_check_out_button);
        //back
        toolbarID = findViewById(R.id.backLayoutPet);
        toolbarID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_to_cart.this.onBackPressed();
            }
        });
    }


    ArrayList<add_to_cart_class> addToCart = new ArrayList<>();
    private void getAddToCart() {
        FirebaseFirestore.getInstance().collection("Cart")
                .whereEqualTo("addBy", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for(DocumentSnapshot s:queryDocumentSnapshots){
                            add_to_cart_class cart = s.toObject(add_to_cart_class.class);
                            adapter.add_to_cart(cart);
                            addToCart.add(cart);
                        }

                    }
                });

    }
    int total;
    int countCheck;
    Set<String> checkedItems = new HashSet<>();

    @SuppressLint("SetTextI18n")
    @Override
    public void onItemCheckedChanged(add_to_cart_class item, boolean isChecked) {

            FirebaseFirestore.getInstance().collection("Pet")
                    .document(item.getProd_id())
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                return;
                            }
                            if (value != null) {
                                if (value.getString("pet_price") != null) {
                                    String p = value.getString("pet_price");
                                    int price = Integer.parseInt(p);
                                    if (isChecked) {
                                        checkedItems.add(item.getProd_id());
                                        total += price;
                                        countCheck++;
                                    } else {
                                        if (checkedItems.contains(item.getProd_id())) {
                                            checkedItems.remove(item.getProd_id());
                                            total -= price;
                                            countCheck--;
                                        }
                                    }

                                }
                                else{
                                    String p = value.getString("prod_price");
                                    int price = Integer.parseInt(p);
                                    if (isChecked) {
                                        checkedItems.add(item.getProd_id());
                                        total += price;
                                        countCheck++;
                                    } else {
                                        if (checkedItems.contains(item.getProd_id())) {
                                            checkedItems.remove(item.getProd_id());
                                            total -= price;
                                            countCheck--;
                                        }
                                    }
                                }

                                priceFormat format = new priceFormat();
                                add_to_cart_check_out_button.setText("CHECKOUT "+"("+countCheck+")");
                                add_to_cart_total_value.setText(format.priceFormatString(String.valueOf(total)));
                            }
                        }
                    });


    }
}