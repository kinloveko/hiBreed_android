package com.example.hi_breed.marketplace;

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
import com.example.hi_breed.adapter.shooterAdapter.m_serviceAdapter;
import com.example.hi_breed.classesFile.service_class;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class m_service_shooter extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_m_service_shooter, container, false);
    }

    RecyclerView shooter_recycler;
    m_serviceAdapter adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        shooter_recycler = view.findViewById(R.id.shooter_recycler);
        adapter = new m_serviceAdapter(getContext());
        shooter_recycler.setLayoutManager(new GridLayoutManager(getContext(),1));
        shooter_recycler.setAdapter(adapter);
        getServices();
    }



    private void getServices() {
        FirebaseFirestore.getInstance().collection("Services")
                .whereEqualTo("displayFor","Service")
                .whereEqualTo("serviceType","Shooter Service")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        adapter.clearList();
                        List<DocumentSnapshot> dlist = value.getDocuments();
                        for(DocumentSnapshot ds:dlist){
                            service_class pet = ds.toObject(service_class.class);
                            adapter.addServiceDisplay(pet);
                        }
                    }
                });
    }
}