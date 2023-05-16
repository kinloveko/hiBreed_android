package com.example.hi_breed.classesFile;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.List;

public class POST implements Parcelable, Serializable {

    private String postKey;
    private String title;
    private String description;
    private List<String> photos;
    private String userID;
    private String image;
    private  Timestamp timeStamp;
    private String displayFor;

    public POST(String title, String description, List<String>photos, String userID, String image, String postKey, Timestamp timeStamp, String displayFor) {
        this.title = title;
        this.description = description;
        this.photos = photos;
        this.userID = userID;
        this.image = image;
        this.postKey = postKey;
        this.timeStamp = timeStamp;
        this.displayFor = displayFor;
    }
    public POST(){

    }

    protected POST(Parcel in) {
        postKey = in.readString();
        title = in.readString();
        description = in.readString();
        photos = in.createStringArrayList();
        userID = in.readString();
        image = in.readString();
        timeStamp = in.readParcelable(Timestamp.class.getClassLoader());
        displayFor = in.readString();
    }

    public static final Creator<POST> CREATOR = new Creator<POST>() {
        @Override
        public POST createFromParcel(Parcel in) {
            return new POST(in);
        }

        @Override
        public POST[] newArray(int size) {
            return new POST[size];
        }
    };

    public String getPostKey() {
        return postKey;
    }

    public String getDisplayFor() {
        return displayFor;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public String getUserID() {
        return userID;
    }

    public String getImage() {
        return image;
    }

    public  Timestamp getTimeStamp() {
        return timeStamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(postKey);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeStringList(photos);
        dest.writeString(userID);
        dest.writeString(image);
        dest.writeParcelable(timeStamp, flags);
        dest.writeString(displayFor);
    }
}
