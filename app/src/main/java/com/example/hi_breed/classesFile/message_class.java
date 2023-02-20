package com.example.hi_breed.classesFile;

import com.google.firebase.Timestamp;

public class message_class {
    String text;
    Timestamp timestamp;
    String sender;
    public message_class(){

    }

    public message_class(String text, Timestamp timestamp, String sender) {
        this.text = text;
        this.timestamp = timestamp;
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getSender() {
        return sender;
    }
}
