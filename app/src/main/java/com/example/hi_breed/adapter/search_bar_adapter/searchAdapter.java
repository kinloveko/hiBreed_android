package com.example.hi_breed.adapter.search_bar_adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.item;
import com.example.hi_breed.search.search_clicked;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class searchAdapter extends RecyclerView.Adapter<searchAdapter.ViewHolder> {

        Context context;

    private List<item> searchResults;
    private List<item> hold = new ArrayList<>();
    @SuppressLint("NotifyDataSetChanged")
    public void hold(List<item> hold){
        this.hold = hold;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void holdClear(){

        this.hold.clear();
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clearList(){

        this.searchResults.clear();
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addSearch(List<item> searchResults){
        this.searchResults = searchResults;
        notifyDataSetChanged();
    }

    public searchAdapter(Context context) {
       this.context = context;
        this.searchResults = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_display, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

            item result = searchResults.get(i);
            viewHolder.type.setText(result.getCategory());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, search_clicked.class);
                    intent.putExtra("item",result.getCategory());
                    intent.putExtra("parcel", (Serializable) hold);
                    context.startActivity(intent);
                }
            });
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView type;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.type_id);
        }
    }

}


