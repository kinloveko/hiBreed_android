package com.example.hi_breed.classesFile;

import com.google.firebase.Timestamp;

public class notification_data_class {
    private String sender;
    private String send_to_id;
    private String message;
    private String title;
    private Timestamp timestamp;
    private String type;
    private String id;

    public notification_data_class(){

    }

    public notification_data_class(String send_to_id,String sender,String title ,String message, Timestamp timestamp, String type, String id) {
        this.send_to_id = send_to_id;
        this.message = message;
        this.timestamp = timestamp;
        this.type = type;
        this.sender = sender;
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getSender() {
        return sender;
    }


    public String getSend_to_id() {
        return send_to_id;
    }

    public String getMessage() {
        return message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }
}
