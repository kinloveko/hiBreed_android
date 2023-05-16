package com.example.hi_breed.userFile.likes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.likes_adapter;
import com.example.hi_breed.classesFile.likes_class;
import com.example.hi_breed.userFile.cart.add_to_cart;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class user_likes_fragment extends Fragment {
    ImageView cart;
    RecyclerView likes_view;
    BottomNavigationView BreederBottomNavigation;
    likes_adapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.owner_likes_fragment, container, false);

        likes_view = view.findViewById(R.id.likes_view);
        likes_view.setLayoutManager(new GridLayoutManager(getActivity(),2));
        adapter = new likes_adapter(getActivity());
        cart = view.findViewById(R.id.cart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),add_to_cart.class));
            }
        });
        getLikes();
        likes_view.setAdapter(adapter);
        BreederBottomNavigation = getActivity().findViewById(R.id.BreederBottom_navigation);
        BreederBottomNavigation.getMenu().getItem(1).setEnabled(false);
        return view;
    }

    private void getLikes() {
        FirebaseFirestore.getInstance().collection("Likes")
                .whereEqualTo("likedBy", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            return;
                        }
                        if(value!=null){
                            adapter.clearList();
                            for(DocumentSnapshot s: value){
                                likes_class like = s.toObject(likes_class.class);
                                adapter.add_to_cart(like);
                            }
                        }
                    }
                });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (BreederBottomNavigation != null) {
            BreederBottomNavigation.getMenu().getItem(1).setEnabled(false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (BreederBottomNavigation != null) {
            BreederBottomNavigation.getMenu().getItem(1).setEnabled(true);
        }
    }
}