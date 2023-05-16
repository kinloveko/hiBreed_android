package com.example.hi_breed.service_status_for_seller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hi_breed.R;
import com.example.hi_breed.adapter.review_order_adapter;
import com.example.hi_breed.classesFile.rating_class;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;


public class reviews_service extends Fragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reviews_service, container, false);
    }

    RecyclerView reviewsRecycler;
    review_order_adapter adapter;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reviewsRecycler = view.findViewById(R.id.reviewsRecycler);
        reviewsRecycler.setLayoutManager(new GridLayoutManager(getContext(),1));
        adapter = new review_order_adapter(getContext());
        getReviews();
    }

    private void getReviews() {

        FirebaseFirestore.getInstance().collection("Reviews")
                .whereEqualTo("seller_id", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("rateFor","Service")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            return;
                        }
                        if(value!=null){
                            adapter.clearList();
                            for(DocumentSnapshot s:value){

                                rating_class rate = s.toObject(rating_class.class);
                                adapter.addServiceDisplay(rate);
                            }
                            reviewsRecycler.setAdapter(adapter);
                        }
                    }
                });
    }
}