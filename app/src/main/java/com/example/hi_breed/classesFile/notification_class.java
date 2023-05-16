package com.example.hi_breed.classesFile;

import java.util.List;
import java.util.Map;

public class notification_class {

    private String id;
    private Map<String, Object> latestNotification;
    private List<notification_data_class> notification;

    public notification_class() {
        // Required empty constructor for Firebase Firestore
    }

    public notification_class(String id, Map<String, Object> latestNotification, List<notification_data_class> notification) {
        this.id = id;
        this.latestNotification = latestNotification;
        this.notification = notification;
    }

    // getters and setters for all fields
    public String getId() {
        return id;
    }


    public Map<String, Object> getLatestNotification() {
        return latestNotification;
    }


    public List<notification_data_class> getNotification() {
        return notification;
    }


}
