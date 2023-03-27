package com.example.hi_breed.order_breeder_side;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hi_breed.R;
import com.example.hi_breed.adapter.order_status_for_seller_buyer.cancelled_orderAdapter;
import com.example.hi_breed.classesFile.appointment_order_class;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class order_breeder_cancelled extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    RecyclerView cancelledRecycler;
    cancelled_orderAdapter adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cancelledRecycler = view.findViewById(R.id.cancelledRecycler);
        cancelledRecycler.setLayoutManager(new GridLayoutManager(getActivity(),1));
        adapter = new cancelled_orderAdapter(getActivity(), FirebaseAuth.getInstance().getCurrentUser().getUid(),"cancelled");
        getCancelled();
    }

    private void getCancelled() {
        FirebaseFirestore.getInstance().collection("Appointments")
                .whereEqualTo("seller_id", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("order_status","cancelled").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            return;
                        }

                        if(value!=null){
                            adapter.clearList();

                            for(DocumentSnapshot s: value){
                                appointment_order_class appointment = s.toObject(appointment_order_class.class);
                                adapter.addServiceDisplay(appointment);
                            }
                            cancelledRecycler.setAdapter(adapter);
                        }
                    }
                });
    }
}