package com.example.hi_breed.order_breeder_side;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.hi_breed.R;
import com.example.hi_breed.shop.user_breeder_shop_panel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class order_breeder_side extends AppCompatActivity {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    RelativeLayout toolbarID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_breeder_side);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        }

        toolbarID = findViewById(R.id.toolbarID);
        toolbarID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(order_breeder_side.this, user_breeder_shop_panel.class));
                finish();
            }
        });

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new order_breeder_pending(), "Pending");
        viewPagerAdapter.addFragment(new order_breeder_accepted(), "Accepted");
        viewPagerAdapter.addFragment(new order_breeder_completed(), "Completed");
        viewPagerAdapter.addFragment(new order_breeder_cancelled(), "Cancelled");
        viewPagerAdapter.addFragment(new reviews_shop_fragment(), "Reviews");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        Intent intent = getIntent();
        Log.d("MyApp", "Intent extras: " + intent.getExtras());
        if (intent != null) {
            Log.d("MyApp", "Intent extras: " + intent.toString()); // Add this log
            String selectedTab = intent.getStringExtra("SELECTED_TAB");
            Log.d("MyApp", "SELECTED_TAB_APPOINTMENT: " + selectedTab);

            if (selectedTab != null) {
                if (selectedTab.equals("pending")) {
                    viewPager.setCurrentItem(0, true);
                    return;
                }else
                if (selectedTab.equals("accepted")) {
                    viewPager.setCurrentItem(1, true);
                    return;
                }else
                if (selectedTab.equals("completed")) {
                    viewPager.setCurrentItem(2, true);
                    return;
                }else
                if (selectedTab.equals("cancelled")) {
                    viewPager.setCurrentItem(3, true);
                    return;
                }
                else
                if (selectedTab.equals("reviews")) {
                    viewPager.setCurrentItem(4, true);
                    return;
                }
            }
        } else {
            Log.d("MyApp", "No intent extras found");
        }

    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragments = new ArrayList<>();
        private final List<String> fragmentTitles = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitles.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }
    }

}