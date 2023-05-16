package com.example.hi_breed.classesFile;

public class EmailPassClass {
    String contactNumber;
    String email;
    String pass;

    public EmailPassClass(){

    }

    public EmailPassClass(String email, String pass, String contactNumber) {
        this.contactNumber = contactNumber;
        this.email = email;
        this.pass = pass;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
