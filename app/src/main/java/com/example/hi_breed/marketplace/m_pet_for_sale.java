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
import com.example.hi_breed.adapter.edit_pet_for_sale_adapter.petDisplayForSaleAdapter;
import com.example.hi_breed.classesFile.PetSaleClass;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class m_pet_for_sale extends Fragment {

    private RecyclerView pet_for_sale_recycler;
    private petDisplayForSaleAdapter adapter;
    private TextView noOneTextView_pet_sale_card;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.m_pet_for_sale_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        pet_for_sale_recycler = view.findViewById(R.id.pet_for_sale_recycler);
        noOneTextView_pet_sale_card = view.findViewById(R.id.noOneTextView_pet_sale_card);

        adapter = new petDisplayForSaleAdapter(getContext());
        pet_for_sale_recycler.setLayoutManager(new GridLayoutManager(getContext(),2));
        pet_for_sale_recycler.setAdapter(adapter);
        getProducts();
    }

    private void getProducts() {
        FirebaseFirestore.getInstance().collection("Pet")
                .whereEqualTo("displayFor","forSale")
                .whereEqualTo("show",true)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            return;
                        }
                        if(value!=null){
                            adapter.clearList();
                            List<DocumentSnapshot> dsList = value.getDocuments();
                            for(DocumentSnapshot ds:dsList){
                                if(ds!=null){

                                    PetSaleClass productModel = ds.toObject(PetSaleClass.class);
                                    adapter.addPetDisplay(productModel);
                                }
                            }
                            if(dsList.size() == 0) {
                                pet_for_sale_recycler.setVisibility(View.GONE);
                                noOneTextView_pet_sale_card.setVisibility(View.VISIBLE);
                            }
                            else{
                                pet_for_sale_recycler.setVisibility(View.VISIBLE);
                                noOneTextView_pet_sale_card.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }
}