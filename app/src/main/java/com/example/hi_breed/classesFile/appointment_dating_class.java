package com.example.hi_breed.classesFile;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.List;

public class appointment_dating_class implements Serializable, Parcelable {
    List<String> customer_id;
    String id;
    String seller_id;
    String transaction_id;
    String appointment_date;
    String appointment_time;
    String service_price;
    String type;
    String service_id;
    String appointment_status;
    String appointment_end_message;
    Timestamp timestamp,appointment_end_date;
    String cancelled_by;
    String appointment_for;
    Boolean isRated;
    String pet_dating_match_id;

    public appointment_dating_class(){

    }

    public appointment_dating_class(String id,  List<String> customer_id , String seller_id, String transaction_id,
                                    String appointment_date, String appointment_time, String service_price, String service_id,
                                    String appointment_status, String appointment_end_message, String type , String cancelled_by,
                                    Timestamp timestamp, Timestamp appointment_end_date, Boolean isRated, String appointment_for,String pet_dating_match_id) {
        this.appointment_end_date = appointment_end_date;
        this.appointment_end_message = appointment_end_message;
        this.cancelled_by = cancelled_by;
        this.id = id;
        this.type = type;
        this.isRated = isRated;
        this.customer_id = customer_id;
        this.seller_id = seller_id;
        this.transaction_id = transaction_id;
        this.appointment_date = appointment_date;
        this.appointment_time = appointment_time;
        this.service_price = service_price;
        this.service_id = service_id;
        this.appointment_status = appointment_status;
        this.timestamp = timestamp;
        this.appointment_for = appointment_for;
        this.pet_dating_match_id = pet_dating_match_id;
    }

    protected appointment_dating_class(Parcel in) {
        customer_id = in.createStringArrayList();
        id = in.readString();
        seller_id = in.readString();
        transaction_id = in.readString();
        appointment_date = in.readString();
        appointment_time = in.readString();
        service_price = in.readString();
        type = in.readString();
        service_id = in.readString();
        appointment_status = in.readString();
        appointment_end_message = in.readString();
        timestamp = in.readParcelable(Timestamp.class.getClassLoader());
        appointment_end_date = in.readParcelable(Timestamp.class.getClassLoader());
        cancelled_by = in.readString();
        appointment_for = in.readString();
        byte tmpIsRated = in.readByte();
        isRated = tmpIsRated == 0 ? null : tmpIsRated == 1;
        pet_dating_match_id = in.readString();
    }

    public static final Creator<appointment_dating_class> CREATOR = new Creator<appointment_dating_class>() {
        @Override
        public appointment_dating_class createFromParcel(Parcel in) {
            return new appointment_dating_class(in);
        }

        @Override
        public appointment_dating_class[] newArray(int size) {
            return new appointment_dating_class[size];
        }
    };

    public String getPet_dating_match_id() {
        return pet_dating_match_id;
    }

    public List<String> getCustomer_id() {
        return customer_id;
    }

    public String getType() {
        return type;
    }

    public Boolean getRated() {
        return isRated;
    }

    public String getCancelled_by() {
        return cancelled_by;
    }


    public Timestamp getAppointment_end_date() {
        return appointment_end_date;
    }

    public String getAppointment_end_message() {
        return appointment_end_message;
    }


    public String getId() {
        return id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public String getAppointment_date() {
        return appointment_date;
    }

    public String getAppointment_time() {
        return appointment_time;
    }

    public String getService_price() {
        return service_price;
    }

    public String getService_id() {
        return service_id;
    }

    public String getAppointment_status() {
        return appointment_status;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getAppointment_for() {
        return appointment_for;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(customer_id);
        dest.writeString(id);
        dest.writeString(seller_id);
        dest.writeString(transaction_id);
        dest.writeString(appointment_date);
        dest.writeString(appointment_time);
        dest.writeString(service_price);
        dest.writeString(type);
        dest.writeString(service_id);
        dest.writeString(appointment_status);
        dest.writeString(appointment_end_message);
        dest.writeParcelable(timestamp, flags);
        dest.writeParcelable(appointment_end_date, flags);
        dest.writeString(cancelled_by);
        dest.writeString(appointment_for);
        dest.writeByte((byte) (isRated == null ? 0 : isRated ? 1 : 2));
        dest.writeString(pet_dating_match_id);
    }
}
