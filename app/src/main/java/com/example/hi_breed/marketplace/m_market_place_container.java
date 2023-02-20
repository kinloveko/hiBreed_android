package com.example.hi_breed.marketplace;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
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

import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.BaseActivity;
import com.example.hi_breed.search.search_dashboard_home;
import com.example.hi_breed.userFile.cart.add_to_cart;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

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
                m_market_place_container.this.onBackPressed();
            }
        });
        initView();
    }

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

}