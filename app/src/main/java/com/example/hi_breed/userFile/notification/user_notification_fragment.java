package com.example.hi_breed.userFile.notification;

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
import com.example.hi_breed.adapter.notification_adapter.notification_adapter;
import com.example.hi_breed.classesFile.add_to_cart_class;
import com.example.hi_breed.classesFile.notification_class;
import com.example.hi_breed.classesFile.notification_data_class;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class user_notification_fragment extends Fragment implements notification_adapter.ItemCheckedChangeListener{



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    BottomNavigationView BreederBottomNavigation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        BreederBottomNavigation = getActivity().findViewById(R.id.BreederBottom_navigation);
        BreederBottomNavigation.getMenu().getItem(2).setEnabled(false);
        return inflater.inflate(R.layout.owner_notification_fragment, container, false);

    }

    RecyclerView notification_recycler;
    notification_adapter adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        notification_recycler = view.findViewById(R.id.notification_recycler);
        notification_recycler.setLayoutManager(new GridLayoutManager(getActivity(),1));
        adapter = new notification_adapter(getActivity(),this,FirebaseAuth.getInstance().getCurrentUser().getUid());
        getNotification();

    }


    private void getNotification() {
        FirebaseFirestore.getInstance().collection("Notifications")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error!=null){
                            return;
                        }
                       if(value.exists()) {
                           adapter.clearList();
                            notification_class a = value.toObject(notification_class.class);
                           List<notification_data_class> notifications = a.getNotification();

                           // Sort notifications in descending order by timestamp
                           Collections.sort(notifications, new Comparator<notification_data_class>() {
                               @Override
                               public int compare(notification_data_class o1, notification_data_class o2) {
                                   return o2.getTimestamp().compareTo(o1.getTimestamp());
                               }
                           });
                           adapter.add(notifications, value.getString("id"));
                           notification_recycler.setAdapter(adapter);
                        }
                       else{

                       }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        BreederBottomNavigation.getMenu().getItem(2).setEnabled(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        BreederBottomNavigation.getMenu().getItem(2).setEnabled(true);
    }


    @Override
    public void onItemCheckedChanged(add_to_cart_class item, boolean isChecked) {

    }
}