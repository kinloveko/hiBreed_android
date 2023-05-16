package com.example.hi_breed.classesFile;

public class pushNotification {
    notificationData data;
    String to;

    public pushNotification(notificationData data, String to) {
        this.data = data;
        this.to = to;
    }

    public notificationData getData() {
        return data;
    }

    public String getTo() {
        return to;
    }
}
