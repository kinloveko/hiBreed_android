package com.example.hi_breed.classesFile;

import com.google.firebase.Timestamp;

public class add_to_cart_class {
    String id;
    String prod_id;
    String prod_category;
    String prod_price;
    String addBy;
    String prod_seller;
    Timestamp timestamp;

    public  add_to_cart_class() {

    }
    public add_to_cart_class(String id, String prod_id, String prod_category, String prod_price,
                             String addBy, String prod_seller,Timestamp timestamp) {
        this.id = id;
        this.prod_id = prod_id;
        this.prod_category = prod_category;
        this.prod_price = prod_price;
        this.addBy = addBy;
        this.prod_seller = prod_seller;
        this.timestamp = timestamp;

    }



    public String getId() {
        return id;
    }

    public String getProd_id() {
        return prod_id;
    }

    public String getProd_category() {
        return prod_category;
    }

    public String getProd_price() {
        return prod_price;
    }

    public String getAddBy() {
        return addBy;
    }

    public String getProd_seller() {
        return prod_seller;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

}
