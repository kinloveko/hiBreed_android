package com.example.hi_breed.classesFile;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;

public class PetDateClass implements Serializable, Parcelable {
    String pet_birthday,pet_breed,pet_breeder,pet_colorMarkings,pet_description,pet_gender,pet_name,displayFor;
    ArrayList<String> pet_vaccine;
    Boolean show;
    String id;
    String petSize;
    String petKilo;
    ArrayList<String> photos;
    String address;
    Timestamp timestamp;
    public PetDateClass(){

    }

    public PetDateClass(String id, String pet_name, String pet_description, String pet_colorMarkings,
                        String pet_breed, String petSize, String petKilo, ArrayList<String> photos, ArrayList<String> pet_vaccine,
                        String pet_gender, String pet_birthday, String address , String pet_breeder, String displayFor, Timestamp timestamp, Boolean show) {
        this.pet_birthday = pet_birthday;
        this.address = address;
        this.pet_breed = pet_breed;
        this.pet_breeder = pet_breeder;
        this.pet_colorMarkings = pet_colorMarkings;
        this.pet_description = pet_description;
        this.pet_gender = pet_gender;
        this.petKilo =petKilo;
        this.petSize = petSize;
        this.pet_name = pet_name;
        this.pet_vaccine = pet_vaccine;
        this.displayFor = displayFor;
        this.show = show;
        this.id = id;
        this.photos = photos;
        this.timestamp = timestamp;
    }

    protected PetDateClass(Parcel in) {
        pet_birthday = in.readString();
        pet_breed = in.readString();
        pet_breeder = in.readString();
        pet_colorMarkings = in.readString();
        pet_description = in.readString();
        pet_gender = in.readString();
        pet_name = in.readString();
        displayFor = in.readString();
        pet_vaccine = in.createStringArrayList();
        byte tmpShow = in.readByte();
        show = tmpShow == 0 ? null : tmpShow == 1;
        id = in.readString();
        petSize = in.readString();
        petKilo = in.readString();
        photos = in.createStringArrayList();
        address = in.readString();
        timestamp = in.readParcelable(Timestamp.class.getClassLoader());
    }

    public static final Creator<PetDateClass> CREATOR = new Creator<PetDateClass>() {
        @Override
        public PetDateClass createFromParcel(Parcel in) {
            return new PetDateClass(in);
        }

        @Override
        public PetDateClass[] newArray(int size) {
            return new PetDateClass[size];
        }
    };

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getAddress() {
        return address;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }


    public String getPetKilo() {
        return petKilo;
    }

    public String getPetSize() {
        return petSize;
    }

    public void setPetKilo(String petKilo) {
        this.petKilo = petKilo;
    }

    public void setPetSize(String petSize) {
        this.petSize = petSize;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayFor() {
        return displayFor;
    }

    public void setDisplayFor(String displayFor) {
        this.displayFor = displayFor;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public Boolean getShow() {
        return show;
    }

    public String getPet_birthday() {
        return pet_birthday;
    }

    public void setPet_birthday(String pet_birthday) {
        this.pet_birthday = pet_birthday;
    }

    public String getPet_breed() {
        return pet_breed;
    }

    public void setPet_breed(String pet_breed) {
        this.pet_breed = pet_breed;
    }

    public String getPet_breeder() {
        return pet_breeder;
    }

    public void setPet_breeder(String pet_breeder) {
        this.pet_breeder = pet_breeder;
    }

    public String getPet_colorMarkings() {
        return pet_colorMarkings;
    }

    public void setPet_colorMarkings(String pet_colorMarkings) {
        this.pet_colorMarkings = pet_colorMarkings;
    }

    public String getPet_description() {
        return pet_description;
    }

    public void setPet_description(String pet_description) {
        this.pet_description = pet_description;
    }

    public String getPet_gender() {
        return pet_gender;
    }

    public void setPet_gender(String pet_gender) {
        this.pet_gender = pet_gender;
    }


    public String getPet_name() {
        return pet_name;
    }

    public void setPet_name(String pet_name) {
        this.pet_name = pet_name;
    }


    public ArrayList<String> getPet_vaccine() {
        return pet_vaccine;
    }

    public void setPet_vaccine(ArrayList<String> pet_vaccine) {
        this.pet_vaccine = pet_vaccine;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pet_birthday);
        dest.writeString(pet_breed);
        dest.writeString(pet_breeder);
        dest.writeString(pet_colorMarkings);
        dest.writeString(pet_description);
        dest.writeString(pet_gender);
        dest.writeString(pet_name);
        dest.writeString(displayFor);
        dest.writeStringList(pet_vaccine);
        dest.writeByte((byte) (show == null ? 0 : show ? 1 : 2));
        dest.writeString(id);
        dest.writeString(petSize);
        dest.writeString(petKilo);
        dest.writeStringList(photos);
        dest.writeString(address);
        dest.writeParcelable(timestamp, flags);
    }
}
