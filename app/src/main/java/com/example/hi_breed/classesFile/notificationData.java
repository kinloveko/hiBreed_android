package com.example.hi_breed.classesFile;

public class notificationData {
    String message;
    String title;
    String matchID;
    String notCurrentUser;
    String type;
    String notificationFor;


    public notificationData(){

    }

    public notificationData(String message, String title,String matchID,String notCurrentUser,String type,String notificationFor) {
        this.message = message;
        this.title = title;
        this.matchID = matchID;
        this.notCurrentUser = notCurrentUser;
        this.type = type;
        this.notificationFor = notificationFor;
    }

    public String getNotificationFor() {
        return notificationFor;
    }

    public String getType() {
        return type;
    }

    public String getNotCurrentUser() {
        return notCurrentUser;
    }

    public String getMessage() {
        return message;
    }

    public String getTitle() {
        return title;
    }

    public String getMatchID() {
        return matchID;
    }
}
