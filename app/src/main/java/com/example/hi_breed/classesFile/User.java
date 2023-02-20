package com.example.hi_breed.classesFile;

public class User {
   public String id;
    public  String name;
    public  String token;
    public  String matchID;


    public User(String id, String name, String token,String matchID) {
        this.id = id;
        this.name = name;
        this.token = token;
        this.matchID = matchID;
    }
    public User(){

    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setMatchID(String matchID) {
        this.matchID = matchID;
    }

    public String getMatchID() {
        return matchID;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }
}
