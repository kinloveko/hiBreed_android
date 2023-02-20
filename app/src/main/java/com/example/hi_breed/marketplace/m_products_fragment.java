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
import com.example.hi_breed.adapter.product_adapter.productForSaleAdapter;
import com.example.hi_breed.classesFile.product_class;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class m_products_fragment extends Fragment {
    private RecyclerView products_recycler;
    private productForSaleAdapter adapter;
    private TextView noOneTextView_pet_sale_card;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.m_products_fragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        products_recycler = view.findViewById(R.id.products_recycler);
        adapter = new productForSaleAdapter(getContext());
        products_recycler.setLayoutManager(new GridLayoutManager(getContext(),2));
        products_recycler.setAdapter(adapter);
        getProducts();
    }

    private void getProducts() {
     FirebaseFirestore.getInstance().collection("Pet")
             .whereEqualTo("displayFor","forProducts").addSnapshotListener(new EventListener<QuerySnapshot>() {
                 @Override
                 public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null)
                        {
                            return;
                        }
                        if(value!=null){
                            adapter.clearList();
                            List<DocumentSnapshot> list = value.getDocuments();
                            for(DocumentSnapshot s: list){
                                product_class p = s.toObject(product_class.class);
                                adapter.addPetDisplay(p);
                            }
                        }
                 }
             });

    }
}