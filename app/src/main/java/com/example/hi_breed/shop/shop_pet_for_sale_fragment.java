package com.example.hi_breed.shop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hi_breed.R;
import com.example.hi_breed.adapter.edit_pet_for_sale_adapter.petDisplayForSaleAdapter;
import com.example.hi_breed.classesFile.PetSaleClass;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class shop_pet_for_sale_fragment extends Fragment {

    RecyclerView petForSale_view;
    petDisplayForSaleAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.shop_pet_for_sale_fragment, container, false);
        String breeder = getArguments().getString("breeder");
        petForSale_view = view.findViewById(R.id.petForSale_view);
        adapter = new petDisplayForSaleAdapter(getContext());
        petForSale_view.setLayoutManager(new GridLayoutManager(getContext(),2));
        petForSale_view.setAdapter(adapter);
        getDisplay(breeder);
        return view;
    }

    private void getDisplay(String id) {

        FirebaseFirestore.getInstance().collection("Shop")
                .document(id).collection("Pet")
                .whereEqualTo("displayFor","forSale")
                .whereEqualTo("show",true)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot s : list) {
                                PetSaleClass sale = s.toObject(PetSaleClass.class);
                                adapter.addPetDisplay(sale);
                            }
                        }
                    }
                });
    }


}