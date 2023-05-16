package com.example.hi_breed.classesFile;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.Duration;
import java.time.Instant;


public class TimeStampClass {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public  String getRelativeTime(Instant timestamp) {
        Instant now = Instant.now();
        Duration duration = Duration.between(timestamp, now);

        if (duration.toMillis() < 1000) {
            return "Just now";
        } else if (duration.toMillis() < 60000) {
            return duration.toMillis() / 1000 + " seconds ago";
        } else if (duration.toMillis() < 3600000) {
            return duration.toMinutes() + " minutes ago";
        } else if (duration.toMillis() < 86400000) {
            return duration.toHours() + " hours ago";
        } else {
            return duration.toDays() + " days ago";
        }
    }
}
