package com.example.hi_breed.classesFile;

import com.google.firebase.Timestamp;

public class likes_class {
    String id;
    String likedBy;
    String product_id;
    String breeder_id;
    Timestamp timestamp;
    String category;

    public likes_class(){

    }
    public likes_class(String id, String likedBy, String product_id, String breeder_id,String category, Timestamp timestamp) {
        this.id = id;
        this.likedBy = likedBy;
        this.product_id = product_id;
        this.breeder_id = breeder_id;
        this.timestamp = timestamp;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public String getId() {
        return id;
    }

    public String getLikedBy() {
        return likedBy;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getBreeder_id() {
        return breeder_id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
