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
import com.example.hi_breed.adapter.product_adapter.productForSaleAdapter;
import com.example.hi_breed.classesFile.product_class;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class shop_product_fragment extends Fragment {


    RecyclerView product_view;
    productForSaleAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.shop_product_fragment, container, false);
        String breeder = getArguments().getString("breeder");
        product_view = view.findViewById(R.id.product_view);
        product_view.setLayoutManager(new GridLayoutManager(getActivity(),2));
        adapter = new productForSaleAdapter(getActivity());
        product_view.setAdapter(adapter);
        getProducts(adapter,breeder);
        return view;
    }


    private void getProducts(productForSaleAdapter adapter,String id) {
        FirebaseFirestore.getInstance().collection("Shop").document(id)
                .collection("Product")
                .whereEqualTo("displayFor","forProducts")
                .whereEqualTo("show",true)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            return;
                        }
                        if(value!=null){
                            adapter.clearList();
                            List<DocumentSnapshot> list = value.getDocuments();
                            for(DocumentSnapshot s:list){
                                product_class p = s.toObject(product_class.class);
                                adapter.addPetDisplay(p);
                            }
                        }
                    }
                });


    }
}