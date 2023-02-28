package com.example.hi_breed.checkout;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.BaseActivity;
import com.example.hi_breed.classesFile.add_to_cart_class;
import com.example.hi_breed.classesFile.checkout_adapter;
import com.example.hi_breed.classesFile.priceFormat;
import com.example.hi_breed.current_address;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class checkout_activity extends BaseActivity {
    TextView check_out_name,
             checkout_number,
             checkout_address,
             checkout_zip,
             subTotal,
             totalPayment,
             texts;
    LinearLayout gcashLayout,codLayout,onSiteLayout;
    ImageView circleCod,circleGcash;
    Button place_order;
    checkout_adapter adapter;
    RecyclerView recyclerView;
    LinearLayout backLayoutService;
    RelativeLayout current;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_details);


        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.setStatusBarColor(Color.parseColor("#e28743"));
        }
        gcashLayout = findViewById(R.id.gcashLayout);
        codLayout = findViewById(R.id.codLayout);
        circleCod = findViewById(R.id.circleCod);
        circleGcash = findViewById(R.id.circleGcash);
        place_order = findViewById(R.id.place_order);
        texts = findViewById(R.id.texts);
        onSiteLayout = findViewById(R.id.onSiteLayout);

        current = findViewById(R.id.currentAddress);
        current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(checkout_activity.this,current_address.class);
                i.putExtra("address",checkout_address.getText().toString());
                i.putExtra("zip",checkout_zip.getText().toString());
                i.putExtra("phone",checkout_number.getText().toString());
                startActivity(i);
            }
        });
        backLayoutService = findViewById(R.id.backLayoutService);
        backLayoutService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkout_activity.this.onBackPressed();
                finish();
            }
        });
        check_out_name = findViewById(R.id.check_out_name);
        checkout_number = findViewById(R.id.checkout_number);
        checkout_address = findViewById(R.id.checkout_address);
        checkout_zip = findViewById(R.id.checkout_zip);
        subTotal = findViewById(R.id.subTotal);
        totalPayment = findViewById(R.id.totalPayment);
        recyclerView = findViewById(R.id.checkout_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        adapter = new checkout_adapter(this);
        int total = 0;
        Intent i = getIntent();
        //Using this to upload na order
        List<add_to_cart_class> list = (List<add_to_cart_class>) i.getSerializableExtra("mode");

        for (add_to_cart_class a : list) {
            total += Integer.parseInt(a.getProd_price());
            if(a.getProd_category().equals("forSale")){
                onSiteLayout.setVisibility(View.VISIBLE);
                texts.setVisibility(View.VISIBLE);
            }
            if(a.getProd_category().equals("Medicine") || a.getProd_category().equals("Dog Accessories")){
                gcashLayout.setVisibility(View.VISIBLE);
                codLayout.setVisibility(View.VISIBLE);
            }

            adapter.add(a);
        }
        recyclerView.setAdapter(adapter);
        priceFormat format = new priceFormat();
        subTotal.setText("₱ " + format.priceFormatString(String.valueOf(total)));
        totalPayment.setText("₱ " + format.priceFormatString(String.valueOf(total)));

        FirebaseFirestore.getInstance().collection("User")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot s) {
                        check_out_name.setText(s.getString("firstName") + " " + s.getString("middleName") + " " + s.getString("lastName"));
                        checkout_address.setText(s.getString("address"));
                        checkout_zip.setText(s.getString("zipCode"));
                        checkout_number.setText("09318924806");
                    }
                });


/*          disabling it for future features

        // Set click listener for gcashLayout
        gcashLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current tag value for the check mark image
                Object tag = circleGcash.getTag();
                // If the tag is null or false, the image is not checked
                if (tag == null || !((Boolean) tag)) {
                    // Set the check mark image to the "checked" drawable
                    circleGcash.setImageResource(R.drawable.icon_circle_check);
                    // Set the tag to true to indicate that the image is checked
                    circleGcash.setTag(true);
                    // Set the other check mark image to the "unchecked" drawable
                    circleCod.setImageResource(R.drawable.icon_not_check);
                    // Set the tag to false to indicate that the image is unchecked
                    circleCod.setTag(false);
                } else {
                    // Set the check mark image to the "unchecked" drawable
                    circleGcash.setImageResource(R.drawable.icon_not_check);
                    // Set the tag to false to indicate that the image is unchecked
                    circleGcash.setTag(false);
                }
            }
        });*/

// Set click listener for codLayout
        codLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current tag value for the check mark image
                Object tag = circleCod.getTag();
                // If the tag is null or false, the image is not checked
                if (tag == null || !((Boolean) tag)) {
                    // Set the check mark image to the "checked" drawable
                    circleCod.setImageResource(R.drawable.icon_circle_check);
                    // Set the tag to true to indicate that the image is checked
                    circleCod.setTag(true);
                    // Set the other check mark image to the "unchecked" drawable
                    circleGcash.setImageResource(R.drawable.icon_not_check);
                    // Set the tag to false to indicate that the image is unchecked
                    circleGcash.setTag(false);
                } else {
                    // Set the check mark image to the "unchecked" drawable
                    circleCod.setImageResource(R.drawable.icon_not_check);
                    // Set the tag to false to indicate that the image is unchecked
                    circleCod.setTag(false);
                }
            }
        });
// Set click listener for place_order button
        place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the tag values for the check mark images
                boolean gcashChecked = circleGcash.getTag() != null && (Boolean) circleGcash.getTag();
                boolean codChecked = circleCod.getTag() != null && (Boolean) circleCod.getTag();
                // Check which image is checked and display appropriate message

                if ((gcashLayout.isShown() || codLayout.isShown()) && onSiteLayout.isShown()) {
                    if (gcashChecked) {
                      gotoAnotherActivity();

                    } else if (codChecked) {
                        gotoAnotherActivity();
                    }
                    else{
                        Toast.makeText(checkout_activity.this, "Select a payment", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(gcashLayout.isShown() || codLayout.isShown()){
                    if (gcashChecked) {
                        gotoAnotherActivity();
                    } else if (codChecked) {
                        gotoAnotherActivity();
                    } else{
                        Toast.makeText(checkout_activity.this, "Select a payment", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    //Check from List getSerializable at the tom from their category if petforsale or product.
                    gotoAnotherActivity();
                }
            }
        });
    }

    private void gotoAnotherActivity() {
        Intent i = new Intent(this,checkout_thankyou.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
         i.putExtra("from","Checkout");
        startActivity(i);
        startActivity(i);
        finish();
    }
}