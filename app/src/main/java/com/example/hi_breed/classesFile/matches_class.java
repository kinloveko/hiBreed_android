package com.example.hi_breed.classesFile;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.List;

public class matches_class implements Parcelable,Serializable {

    List<String> participants;
    String matchID;
    Boolean show;
    Timestamp time;
    String appointment_id;
    String status;

    public matches_class(){

    }

    public matches_class(List<String> participants, String matchID, Boolean show, Timestamp time,String appointment_id,String status) {
        this.participants = participants;
        this.matchID = matchID;
        this.show = show;
        this.time = time;
        this.appointment_id = appointment_id;
        this.status = status;
    }

    protected matches_class(Parcel in) {
        participants = in.createStringArrayList();
        matchID = in.readString();
        byte tmpShow = in.readByte();
        show = tmpShow == 0 ? null : tmpShow == 1;
        time = in.readParcelable(Timestamp.class.getClassLoader());
        appointment_id = in.readString();
        status = in.readString();
    }

    public static final Creator<matches_class> CREATOR = new Creator<matches_class>() {
        @Override
        public matches_class createFromParcel(Parcel in) {
            return new matches_class(in);
        }

        @Override
        public matches_class[] newArray(int size) {
            return new matches_class[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public String getAppointment_id() {
        return appointment_id;
    }


    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public void setMatchID(String matchID) {
        this.matchID = matchID;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }


    public List<String> getParticipants() {
        return participants;
    }

    public String getMatchID() {
        return matchID;
    }

    public Boolean getShow() {
        return show;
    }

    public Timestamp getTime() {
        return time;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(participants);
        dest.writeString(matchID);
        dest.writeByte((byte) (show == null ? 0 : show ? 1 : 2));
        dest.writeParcelable(time, flags);
        dest.writeString(appointment_id);
        dest.writeString(status);
    }
}
