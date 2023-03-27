package com.example.hi_breed.classesFile;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class appointment_order_class implements Serializable, Parcelable {

    String seller_id;
    String customer_id;
    String id;
    String item_id;
    String item_price;
    String order_status;
    Timestamp timestamp;
    String transaction_id;
    String order_cancelled_message;
    Timestamp oder_date_end;
    String type;
    Boolean isRated;
    public appointment_order_class(){

    }

    public appointment_order_class( String id,String seller_id,String customer_id, String item_id, String item_price, String order_status, Timestamp timestamp, String transaction_id, String order_cancelled_message,String type, Timestamp oder_date_end, Boolean isRated) {
        this.customer_id = customer_id;
        this.id = id;
        this.seller_id = seller_id;
        this.item_id = item_id;
        this.item_price = item_price;
        this.order_status = order_status;
        this.timestamp = timestamp;
        this.transaction_id = transaction_id;
        this.type = type;
        this.order_cancelled_message = order_cancelled_message;
        this.oder_date_end = oder_date_end;
        this.isRated = isRated;
    }

    protected appointment_order_class(Parcel in) {
        seller_id = in.readString();
        customer_id = in.readString();
        id = in.readString();
        item_id = in.readString();
        item_price = in.readString();
        order_status = in.readString();
        timestamp = in.readParcelable(Timestamp.class.getClassLoader());
        transaction_id = in.readString();
        order_cancelled_message = in.readString();
        oder_date_end = in.readParcelable(Timestamp.class.getClassLoader());
        type = in.readString();
        byte tmpIsRated = in.readByte();
        isRated = tmpIsRated == 0 ? null : tmpIsRated == 1;
    }

    public static final Creator<appointment_order_class> CREATOR = new Creator<appointment_order_class>() {
        @Override
        public appointment_order_class createFromParcel(Parcel in) {
            return new appointment_order_class(in);
        }

        @Override
        public appointment_order_class[] newArray(int size) {
            return new appointment_order_class[size];
        }
    };

    public String getType() {
        return type;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public String getId() {
        return id;
    }

    public String getItem_id() {
        return item_id;
    }

    public String getItem_price() {
        return item_price;
    }

    public String getOrder_status() {
        return order_status;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public String getOrder_cancelled_message() {
        return order_cancelled_message;
    }

    public Timestamp getOder_date_end() {
        return oder_date_end;
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
        dest.writeString(seller_id);
        dest.writeString(customer_id);
        dest.writeString(id);
        dest.writeString(item_id);
        dest.writeString(item_price);
        dest.writeString(order_status);
        dest.writeParcelable(timestamp, flags);
        dest.writeString(transaction_id);
        dest.writeString(order_cancelled_message);
        dest.writeParcelable(oder_date_end, flags);
        dest.writeString(type);
        dest.writeByte((byte) (isRated == null ? 0 : isRated ? 1 : 2));
    }
}
