package com.example.hi_breed.marketplace;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hi_breed.R;
import com.example.hi_breed.adapter.shooterAdapter.m_serviceAdapter;
import com.example.hi_breed.classesFile.service_class;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class m_services extends Fragment {

    RecyclerView services_recycler;
    TextView noOneTextView_pet_sale_card;
    m_serviceAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.m_services_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        noOneTextView_pet_sale_card = view.findViewById(R.id.noOneTextView_pet_sale_card);

        services_recycler = view.findViewById(R.id.services_recycler);
        adapter = new m_serviceAdapter(getContext());
        services_recycler.setLayoutManager(new GridLayoutManager(getContext(),1));
        services_recycler.setAdapter(adapter);
        getServices();

    }

    private void getServices() {
        FirebaseFirestore.getInstance().collection("Services")
                .whereEqualTo("displayFor","Service").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        adapter.clearList();
                        List<DocumentSnapshot> dlist = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot ds:dlist){
                            service_class pet = ds.toObject(service_class.class);
                            adapter.addServiceDisplay(pet);
                        }
                        if(dlist.size() == 0) {
                            services_recycler.setVisibility(View.GONE);
                            noOneTextView_pet_sale_card.setVisibility(View.VISIBLE);
                        }
                        else{
                            services_recycler.setVisibility(View.VISIBLE);
                            noOneTextView_pet_sale_card.setVisibility(View.GONE);
                        }
                    }
                });
    }
}