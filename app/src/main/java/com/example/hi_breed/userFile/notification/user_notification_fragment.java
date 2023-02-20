package com.example.hi_breed.userFile.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hi_breed.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class user_notification_fragment extends Fragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    BottomNavigationView BreederBottomNavigation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        BreederBottomNavigation = getActivity().findViewById(R.id.BreederBottom_navigation);
        BreederBottomNavigation.getMenu().getItem(2).setEnabled(false);
        return inflater.inflate(R.layout.owner_notification_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
        BreederBottomNavigation.getMenu().getItem(2).setEnabled(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        BreederBottomNavigation.getMenu().getItem(2).setEnabled(true);
    }



}