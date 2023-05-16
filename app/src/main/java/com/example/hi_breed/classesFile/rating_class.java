package com.example.hi_breed.classesFile;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class rating_class implements Serializable, Parcelable {


    String id;
    String customer_id;
    String seller_id;
    String comment;
    float rating;
    String rateShop;
    String service_id;
    String type;
    Timestamp timestamp;
    Boolean isRated;

    public rating_class(){

    }

    public rating_class(String id, String customer_id, String seller_id, String comment, float rating, String rateShop, String service_id, String type, Timestamp timestamp, Boolean isRated) {
        this.id = id;
        this.customer_id = customer_id;
        this.seller_id = seller_id;
        this.comment = comment;
        this.rating = rating;
        this.rateShop = rateShop;
        this.service_id = service_id;
        this.type = type;
        this.timestamp = timestamp;
        this.isRated = isRated;
    }

    protected rating_class(Parcel in) {
        id = in.readString();
        customer_id = in.readString();
        seller_id = in.readString();
        comment = in.readString();
        rating = in.readFloat();
        rateShop = in.readString();
        service_id = in.readString();
        type = in.readString();
        timestamp = in.readParcelable(Timestamp.class.getClassLoader());
        byte tmpIsRated = in.readByte();
        isRated = tmpIsRated == 0 ? null : tmpIsRated == 1;
    }

    public static final Creator<rating_class> CREATOR = new Creator<rating_class>() {
        @Override
        public rating_class createFromParcel(Parcel in) {
            return new rating_class(in);
        }

        @Override
        public rating_class[] newArray(int size) {
            return new rating_class[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public String getComment() {
        return comment;
    }

    public float getRating() {
        return rating;
    }

    public String getRateShop() {
        return rateShop;
    }

    public String getService_id() {
        return service_id;
    }

    public String getType() {
        return type;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public Boolean getRated() {
        return isRated;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(customer_id);
        dest.writeString(seller_id);
        dest.writeString(comment);
        dest.writeFloat(rating);
        dest.writeString(rateShop);
        dest.writeString(service_id);
        dest.writeString(type);
        dest.writeParcelable(timestamp, flags);
        dest.writeByte((byte) (isRated == null ? 0 : isRated ? 1 : 2));
    }
}
