package com.example.hi_breed.classesFile;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class service_class implements Serializable, Parcelable {
    List<String> availability;
    List<String> schedule;
    private ArrayList<String> photos;
    String service_fee;
    String service_description;
    Timestamp timestamp;
    String address;
    String id;
    String shooter_id;
    String serviceType;
    String displayFor;
    Boolean show;
    String name;
    public service_class(){

    }

    public service_class(String id,String name , String service_description, List<String> schedule, List<String> availability, String address, String service_fee, ArrayList<String> photos, String shooter_id, String serviceType,String displayFor,Timestamp timestamp  ,boolean show) {
        this.id = id;
        this.name = name;
        this.availability=  availability;
        this.schedule =schedule;
        this.service_fee = service_fee;
        this.service_description = service_description;
        this.address = address;
        this.photos = photos;
        this.shooter_id = shooter_id;
        this.serviceType = serviceType;
        this.displayFor = displayFor;
        this.show = show;
        this.timestamp = timestamp;
    }

    protected service_class(Parcel in) {
        availability = in.createStringArrayList();
        schedule = in.createStringArrayList();
        photos = in.createStringArrayList();
        service_fee = in.readString();
        service_description = in.readString();
        timestamp = in.readParcelable(Timestamp.class.getClassLoader());
        address = in.readString();
        id = in.readString();
        shooter_id = in.readString();
        serviceType = in.readString();
        displayFor = in.readString();
        byte tmpShow = in.readByte();
        show = tmpShow == 0 ? null : tmpShow == 1;
        name = in.readString();
    }

    public static final Creator<service_class> CREATOR = new Creator<service_class>() {
        @Override
        public service_class createFromParcel(Parcel in) {
            return new service_class(in);
        }

        @Override
        public service_class[] newArray(int size) {
            return new service_class[size];
        }
    };

    public String getName() {
        return name;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getDisplayFor() {
        return displayFor;
    }

    public Boolean getShow() {
        return show;
    }

    public String getShooter_id() {
        return shooter_id;
    }

    public String getId() {
        return id;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public List<String> getSchedule() {
        return schedule;
    }

    public List<String> getAvailability() {
        return availability;
    }

    public String getService_fee() {
        return service_fee;
    }

    public String getService_description() {
        return service_description;
    }


    public String getAddress() {
        return address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeStringList(availability);
        dest.writeStringList(schedule);
        dest.writeStringList(photos);
        dest.writeString(service_fee);
        dest.writeString(service_description);
        dest.writeParcelable(timestamp, flags);
        dest.writeString(address);
        dest.writeString(id);
        dest.writeString(shooter_id);
        dest.writeString(serviceType);
        dest.writeString(displayFor);
        dest.writeByte((byte) (show == null ? 0 : show ? 1 : 2));
        dest.writeString(name);
    }


}
