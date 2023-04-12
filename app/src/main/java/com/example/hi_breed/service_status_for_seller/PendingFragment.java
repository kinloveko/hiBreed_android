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
import com.example.hi_breed.adapter.service_status_for_seller_buyer.pending_serviceAdapter;
import com.example.hi_breed.classesFile.appointment_class;
import com.example.hi_breed.classesFile.appointment_dating_class;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;


public class PendingFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pending, container, false);
    }


    RecyclerView pendingRecycler;
    pending_serviceAdapter adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pendingRecycler = view.findViewById(R.id.pendingRecycler);
        pendingRecycler.setLayoutManager(new GridLayoutManager(getContext(),1));
        adapter = new pending_serviceAdapter(getContext(),FirebaseAuth.getInstance().getCurrentUser().getUid());
        getPendingAppointment();
    }

    private void getPendingAppointment() {
        FirebaseFirestore.getInstance().collection("Appointments")
                .whereEqualTo("seller_id", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("appointment_status","pending").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(error!=null){
                                return;
                            }

                            if(value!=null){
                                adapter.clearList();

                                for(DocumentSnapshot s: value){
                                    if(s.getString("appointment_for")!=null){
                                        appointment_dating_class appointment = s.toObject(appointment_dating_class.class);
                                        adapter.addDateList(appointment);
                                    }
                                    else{
                                        appointment_class appointment = s.toObject(appointment_class.class);
                                        adapter.addServiceDisplay(appointment);
                                    }
                                }
                                pendingRecycler.setAdapter(adapter);
                            }
                    }
                });
    }
}