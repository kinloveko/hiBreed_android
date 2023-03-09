package com.example.hi_breed.classesFile;

import java.sql.Timestamp;

public class appointment_class {
    String customer_id;
    String id;
    String seller_id;
    String transaction_id;
    String appointment_date;
    String appointment_time;
    String service_price;
    String service_id;
    String appointment_status;
    Timestamp timestamp;

    public appointment_class(String id,String customer_id ,String seller_id, String transaction_id, String appointment_date, String appointment_time, String service_price, String service_id, String appointment_status, Timestamp timestamp) {
        this.id = id;
        this.customer_id = customer_id;
        this.seller_id = seller_id;
        this.transaction_id = transaction_id;
        this.appointment_date = appointment_date;
        this.appointment_time = appointment_time;
        this.service_price = service_price;
        this.service_id = service_id;
        this.appointment_status = appointment_status;
        this.timestamp = timestamp;
    }

    public String getCustomer_id() {
        return customer_id;
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

}
