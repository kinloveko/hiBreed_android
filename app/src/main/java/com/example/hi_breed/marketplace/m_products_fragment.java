package com.example.hi_breed.marketplace;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hi_breed.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;


public class m_products_fragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.m_products_fragment, container, false);
    }

    ViewPager2 viewPager;
    ViewPagerAdapter adapterV;
    TabLayout tabLayout;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        initView();
    }


    private void initView() {
        adapterV = new ViewPagerAdapter(getChildFragmentManager(),
                getViewLifecycleOwner().getLifecycle());
        adapterV.addFragment(new product_dog_accessories(),"Dog Accessories");
        adapterV.addFragment(new product_medicine(),"Medicine");


        viewPager.setAdapter(adapterV);
        viewPager.setOffscreenPageLimit(1);

        new TabLayoutMediator(tabLayout,viewPager,
                (tab,position)->{
                    tab.setText(adapterV.fragmentTitle.get(position));
                }).attach();

        for(int i =0; i<tabLayout.getTabCount();i++){
            TextView tv = (TextView) LayoutInflater.from(getContext())
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