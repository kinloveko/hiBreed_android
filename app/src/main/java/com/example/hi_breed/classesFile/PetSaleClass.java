package com.example.hi_breed.classesFile;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;

public class PetSaleClass implements Serializable, Parcelable {
    String pet_birthday;
    String pet_breed;
    String id;
    String pet_breeder;
    String petSize;
    ArrayList<String> photos;
    String petKilo;
    String pet_colorMarkings;
    String pet_description;
    String pet_gender;
    String pet_name;
    String pet_price;
    String displayFor;
    ArrayList<String> pet_vaccine;
    ArrayList<String> papers;
    Boolean show;
    String kennel;
    Timestamp timestamp;
    public PetSaleClass(){

    }
    public PetSaleClass(String id,String pet_name, String pet_description, String pet_colorMarkings, String pet_breed,String petSize,String petKilo,ArrayList<String> photos,ArrayList<String> papers, ArrayList<String> pet_vaccine,
                        String pet_gender, String pet_birthday, String pet_price,  String pet_breeder,String kennel,String displayFor,Timestamp timestamp, Boolean show) {
        this.pet_birthday = pet_birthday;
        this.pet_breed = pet_breed;
        this.pet_breeder = pet_breeder;
        this.pet_colorMarkings = pet_colorMarkings;
        this.pet_description = pet_description;
        this.pet_gender = pet_gender;
        this.petSize = petSize;
        this.petKilo = petKilo;
        this.pet_name = pet_name;
        this.pet_price = pet_price;
        this.pet_vaccine = pet_vaccine;
        this.show = show;
        this.kennel = kennel;
        this.id = id;
        this.displayFor = displayFor;
        this.photos = photos;
        this.timestamp = timestamp;
        this.papers = papers;
    }

    protected PetSaleClass(Parcel in) {
        pet_birthday = in.readString();
        pet_breed = in.readString();
        id = in.readString();
        pet_breeder = in.readString();
        petSize = in.readString();
        photos = in.createStringArrayList();
        petKilo = in.readString();
        pet_colorMarkings = in.readString();
        pet_description = in.readString();
        pet_gender = in.readString();
        pet_name = in.readString();
        pet_price = in.readString();
        displayFor = in.readString();
        pet_vaccine = in.createStringArrayList();
        papers = in.createStringArrayList();
        byte tmpShow = in.readByte();
        show = tmpShow == 0 ? null : tmpShow == 1;
        kennel = in.readString();
        timestamp = in.readParcelable(Timestamp.class.getClassLoader());
    }

    public static final Creator<PetSaleClass> CREATOR = new Creator<PetSaleClass>() {
        @Override
        public PetSaleClass createFromParcel(Parcel in) {
            return new PetSaleClass(in);
        }

        @Override
        public PetSaleClass[] newArray(int size) {
            return new PetSaleClass[size];
        }
    };

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public ArrayList<String> getPapers() {
        return papers;
    }

    public void setPapers(ArrayList<String> papers) {
        this.papers = papers;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    public String getKennel() {
        return kennel;
    }

    public void setKennel(String kennel) {
        this.kennel = kennel;
    }

    public String getPetSize() {
        return petSize;
    }

    public void setPetSize(String petSize) {
        this.petSize = petSize;
    }


    public String getPetKilo() {
        return petKilo;
    }

    public void setPetKilo(String petKilo) {
        this.petKilo = petKilo;
    }


    public String getDisplayFor() {
        return displayFor;
    }

    public void setDisplayFor(String displayFor) {
        this.displayFor = displayFor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPet_price() {
        return pet_price;
    }

    public void setPet_price(String pet_price) {
        this.pet_price = pet_price;
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
        dest.writeString(id);
        dest.writeString(pet_breeder);
        dest.writeString(petSize);
        dest.writeStringList(photos);
        dest.writeString(petKilo);
        dest.writeString(pet_colorMarkings);
        dest.writeString(pet_description);
        dest.writeString(pet_gender);
        dest.writeString(pet_name);
        dest.writeString(pet_price);
        dest.writeString(displayFor);
        dest.writeStringList(pet_vaccine);
        dest.writeStringList(papers);
        dest.writeByte((byte) (show == null ? 0 : show ? 1 : 2));
        dest.writeString(kennel);
        dest.writeParcelable(timestamp, flags);
    }
}
