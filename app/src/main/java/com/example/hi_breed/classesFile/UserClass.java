package com.example.hi_breed.classesFile;

import com.google.firebase.Timestamp;

import java.util.List;

public class UserClass {
     String firstName;
    String lastName;
    String middleName;

    String address;
    String zipCode;
    String image;
    List<String> role;
    String backgroundImage;
    String birth;
    String gender;
    String idNo;
    String status;
    Timestamp timestamp;
    public UserClass(){

    }
   public UserClass(String idNo, String first, String middle, String last, String gender, String birth, String address, String zip, String image, String backgroundImage, Timestamp timestamp, String status){
       this.idNo=idNo;
        this.firstName = first;
       this.lastName = last;
       this.middleName = middle;
       this.address = address;
       this.zipCode = zip;
       this.image = image;
       this.backgroundImage = backgroundImage;
       this.birth = birth;
       this.gender = gender;
       this.status = status;
       this.timestamp = timestamp;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getStatus() {
        return status;
    }

    public String getIdNo() {
        return idNo;
    }

    public List<String> getRole() {
        return role;
    }

    public void updateBreeder(String firstName, String middleName , String lastName,
                              String address, String zipCode, String image, String backgroundImage, List<String> role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;

        this.address = address;
        this.zipCode = zipCode;
        this.image = image;
        this.backgroundImage = backgroundImage;
        this.role= role;
    }


    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }



    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage(){
       return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
