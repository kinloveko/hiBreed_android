package com.example.hi_breed.shop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class shop_service_fragment extends Fragment {

    RecyclerView service_view;
    m_serviceAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String breeder = getArguments().getString("breeder");
        View view = inflater.inflate(R.layout.shop_service_fragment, container, false);
        service_view = view.findViewById(R.id.service_view);
        service_view.setLayoutManager(new GridLayoutManager(getActivity(),1));
        adapter = new m_serviceAdapter(getActivity());
        service_view.setAdapter(adapter);
        getService(adapter,breeder);


        return view;
    }

    private void getService(m_serviceAdapter adapter,String id) {
        FirebaseFirestore.getInstance().collection("Services")
                .whereEqualTo("shooter_id",id)
                .whereEqualTo("show",true).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(error!=null){
                        return;
                    }
                    if(value!=null){
                        adapter.clearList();
                        List<DocumentSnapshot> list = value.getDocuments();
                        for(DocumentSnapshot s:list){
                            service_class c = s.toObject(service_class.class);
                            adapter.addServiceDisplay(c);
                        }
                    }
                    }
                });


    }
}