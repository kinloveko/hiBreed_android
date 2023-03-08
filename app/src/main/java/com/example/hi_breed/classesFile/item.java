package com.example.hi_breed.classesFile;

import java.io.Serializable;

public class item implements Serializable {
    private String id;
    private String seller_id;
    private String price;
    private String address;
    private String category;
    private boolean show;
    private String type;
    // Add other properties as needed

    public item() {

    }


    public item(String id, String seller_id, String price, String category,String type,String address, boolean show) {
        this.id = id;
        this.seller_id = seller_id;
        this.price = price;
        this.address = address;
        this.type = type;
        this.category = category;
        this.show = show;
    }

    public String getAddress() {
        return address;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public String getPrice() {
        return price;
    }



    public boolean isShow() {
        return show;
    }

    public String getCategory() { return category; }

}