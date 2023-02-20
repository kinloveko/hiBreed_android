package com.example.hi_breed.classesFile;

import com.google.firebase.Timestamp;

public class replyClass {
    private String id, content,userID,image,name,postKey;
    private Timestamp timeStamp;

    public  replyClass(){

    }
    public replyClass(String id,String content, String userID, String image, String name, Timestamp timeStamp,String postKey) {
        this.content = content;
        this.userID = userID;
        this.image = image;
        this.name = name;
        this.timeStamp = timeStamp;
        this.id = id;
        this.postKey = postKey;
    }

    public String getPostKey() {
        return postKey;
    }

    public String getContent() {
        return content;
    }

    public String getUserID() {
        return userID;
    }

    public String getImage() {
        return image;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }
}
