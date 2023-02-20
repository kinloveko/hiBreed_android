package com.example.hi_breed.classesFile;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.List;

public class product_class implements Serializable, Parcelable {

    String id;
    String prod_name;
    String prod_description;
    String prod_treatment;
    String prod_category;
    String prod_brand;
    String prod_expiration;
    String prod_stocks;
    String vet_id;
    String prod_price;
    Boolean show;
    List<String> photos;
    String displayFor;
    com.google.firebase.Timestamp timestamp;
    public product_class(){

    }
    public product_class(String id, String prod_name, String prod_description, String prod_treatment, String prod_category, String prod_brand, String prod_price, String prod_expiration, String prod_stocks, List<String> photos , String vet_id, String displayFor, com.google.firebase.Timestamp timestamp, Boolean show) {
        this.id = id;
        this.prod_name = prod_name;
        this.prod_description = prod_description;
        this.prod_treatment = prod_treatment;
        this.prod_category = prod_category;
        this.prod_brand = prod_brand;
        this.prod_expiration = prod_expiration;
        this.prod_stocks = prod_stocks;
        this.vet_id = vet_id;
        this.prod_price = prod_price;
        this.photos = photos;
        this.show = show;
        this.displayFor = displayFor;
        this.timestamp =timestamp;
    }

    protected product_class(Parcel in) {
        id = in.readString();
        prod_name = in.readString();
        prod_description = in.readString();
        prod_treatment = in.readString();
        prod_category = in.readString();
        prod_brand = in.readString();
        prod_expiration = in.readString();
        prod_stocks = in.readString();
        vet_id = in.readString();
        prod_price = in.readString();
        byte tmpShow = in.readByte();
        show = tmpShow == 0 ? null : tmpShow == 1;
        photos = in.createStringArrayList();
        displayFor = in.readString();
        timestamp = in.readParcelable(Timestamp.class.getClassLoader());
    }

    public static final Creator<product_class> CREATOR = new Creator<product_class>() {
        @Override
        public product_class createFromParcel(Parcel in) {
            return new product_class(in);
        }

        @Override
        public product_class[] newArray(int size) {
            return new product_class[size];
        }
    };

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getDisplayFor() {
        return displayFor;
    }

    public String getProd_price() {
        return prod_price;
    }

    public List<String> getPhotos() {
        return photos;
    }


    public String getId() {
        return id;
    }

    public String getProd_name() {
        return prod_name;
    }

    public String getProd_description() {
        return prod_description;
    }

    public String getProd_treatment() {
        return prod_treatment;
    }

    public String getProd_category() {
        return prod_category;
    }

    public String getProd_brand() {
        return prod_brand;
    }

    public String getProd_expiration() {
        return prod_expiration;
    }

    public String getProd_stocks() {
        return prod_stocks;
    }

    public String getVet_id() {
        return vet_id;
    }

    public Boolean getShow() {
        return show;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(prod_name);
        dest.writeString(prod_description);
        dest.writeString(prod_treatment);
        dest.writeString(prod_category);
        dest.writeString(prod_brand);
        dest.writeString(prod_expiration);
        dest.writeString(prod_stocks);
        dest.writeString(vet_id);
        dest.writeString(prod_price);
        dest.writeByte((byte) (show == null ? 0 : show ? 1 : 2));
        dest.writeStringList(photos);
        dest.writeString(displayFor);
        dest.writeParcelable(timestamp, flags);
    }
}
