package com.example.hi_breed.Pet;

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
import com.example.hi_breed.adapter.edit_for_date_adapter.petForDateItemAdapter;
import com.example.hi_breed.classesFile.PetDateClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class pet_my_pet_for_date_fragment extends Fragment {

    petForDateItemAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.my_pet_pet_for_date_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView;

        recyclerView = view.findViewById(R.id.for_you_recycler_my_pet_for_date);
        adapter = new petForDateItemAdapter(requireContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);
        getPetForDate();

    }

    private void getPetForDate() {
        FirebaseFirestore.getInstance().collection("Pet").whereEqualTo("displayFor","forDating")
                .whereEqualTo("pet_breeder", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }
                        if (value != null) {
                            adapter.clearList();
                            List<DocumentSnapshot> list = value.getDocuments();
                            for(DocumentSnapshot s:list){
                                PetDateClass pet = s.toObject(PetDateClass.class);
                                adapter.addPetDisplay(pet);
                            }

                        }
                    }
                });
    }
}

