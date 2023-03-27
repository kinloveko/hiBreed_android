package com.example.hi_breed.service_status_for_buyer;

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
import com.example.hi_breed.adapter.service_status_for_seller_buyer.accepted_serviceAdapter;
import com.example.hi_breed.classesFile.appointment_class;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;


public class accepted_user_side extends Fragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accepted_user_side, container, false);
    }

    RecyclerView acceptedRecycler;
    accepted_serviceAdapter adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        acceptedRecycler = view.findViewById(R.id.acceptedRecycler);
        acceptedRecycler.setLayoutManager(new GridLayoutManager(getContext(),1));
        adapter = new accepted_serviceAdapter(getContext(), FirebaseAuth.getInstance().getCurrentUser().getUid());
        getPendingAppointment();
    }

    private void getPendingAppointment() {
        FirebaseFirestore.getInstance().collection("Appointments")
                .whereEqualTo("customer_id", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("order_status","accepted").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            return;
                        }

                        if(value!=null){
                            adapter.clearList();

                            for(DocumentSnapshot s: value){
                                appointment_class appointment = s.toObject(appointment_class.class);
                                adapter.addServiceDisplay(appointment);
                            }
                            acceptedRecycler.setAdapter(adapter);
                        }
                    }
                });
    }
}