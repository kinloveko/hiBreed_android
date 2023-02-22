package com.example.hi_breed.classesFile;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.io.Serializable;
public class add_to_cart_class implements Serializable, Parcelable {
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


    protected add_to_cart_class(Parcel in) {
        id = in.readString();
        prod_id = in.readString();
        prod_category = in.readString();
        prod_price = in.readString();
        addBy = in.readString();
        prod_seller = in.readString();
        timestamp = in.readParcelable(Timestamp.class.getClassLoader());
    }

    public static final Creator<add_to_cart_class> CREATOR = new Creator<add_to_cart_class>() {
        @Override
        public add_to_cart_class createFromParcel(Parcel in) {
            return new add_to_cart_class(in);
        }

        @Override
        public add_to_cart_class[] newArray(int size) {
            return new add_to_cart_class[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(prod_id);
        dest.writeString(prod_category);
        dest.writeString(prod_price);
        dest.writeString(addBy);
        dest.writeString(prod_seller);
        dest.writeParcelable(timestamp, flags);
    }
}
