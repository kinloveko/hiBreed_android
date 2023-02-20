package com.example.hi_breed.classesFile;

import android.icu.text.SimpleDateFormat;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.Timestamp;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimestampConverter {
    private static final long DAY_IN_MILLIS = TimeUnit.DAYS.toMillis(1);
    private static final long MONTH_IN_MILLIS = TimeUnit.DAYS.toMillis(30);

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String formatTimestamp(Timestamp timestamp) {
        Date date = timestamp.toDate();
        long now = System.currentTimeMillis();
        long difference = now - date.getTime();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if (difference < DAY_IN_MILLIS) {
            return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(date);
        } else if (difference < MONTH_IN_MILLIS) {
            return new SimpleDateFormat("EEE", Locale.getDefault()).format(date);
        } else {
            return new SimpleDateFormat("MMM dd", Locale.getDefault()).format(date);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String timestamp(Timestamp timestamp) {
        Date date = timestamp.toDate();
        long now = System.currentTimeMillis();
        long difference = now - date.getTime();


        if (difference < 60000) {
            return " seconds ago";
        } else if (difference < 3600000) {
            long minutes = TimeUnit.MILLISECONDS.toMinutes(difference);
            return minutes +" minutes ago";
        } else if (difference < 86400000) {
            long minutes = TimeUnit.MILLISECONDS.toHours(difference);
            return minutes  + " hours ago";
        } else {
            long minutes = TimeUnit.MILLISECONDS.toDays(difference);
            return minutes + " days ago";
        }

    }

}
