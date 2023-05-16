package com.example.hi_breed.order_user_side;

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
import com.example.hi_breed.adapter.order_status_for_seller_buyer.pending_orderAdapter;
import com.example.hi_breed.classesFile.appointment_order_class;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class order_pending_fragment extends Fragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    pending_orderAdapter adapter;
    RecyclerView pendingRecycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_pending, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pendingRecycler = view.findViewById(R.id.pendingRecycler);
        pendingRecycler.setLayoutManager(new GridLayoutManager(getContext(),1));

        adapter = new pending_orderAdapter(getActivity(), FirebaseAuth.getInstance().getCurrentUser().getUid());
        getPending();

    }

    private void getPending() {
        FirebaseFirestore.getInstance().collection("Appointments")
                .whereEqualTo("customer_id", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("order_status","pending").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){

                        }
                        if(value!=null){
                            adapter.clearList();
                            for(DocumentSnapshot s: value){

                                appointment_order_class appointment = s.toObject(appointment_order_class.class);
                                adapter.addServiceDisplay(appointment);

                            }
                            pendingRecycler.setAdapter(adapter);

                        }
                    }
                });

    }
}