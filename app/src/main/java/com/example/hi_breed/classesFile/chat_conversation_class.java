package com.example.hi_breed.classesFile;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class chat_conversation_class implements Serializable {
    Map<String,Object> latestMessage;
    List<message_class> messages;
    List<String> participants;
    Timestamp timestamp;
    String matchID;
    public chat_conversation_class(){

    }
    public chat_conversation_class(Map<String,Object>  latestMessage,List<message_class> messages, List<String>participants,String matchID, Timestamp timestamp) {
        this.messages = messages;
        this.latestMessage = latestMessage;
        this.participants = participants;
        this.timestamp = timestamp;
        this.matchID = matchID;
    }

    public Map<String,Object>  getLatestMessage() {
        return latestMessage;
    }

    public String getMatchID() {
        return matchID;
    }

    public List<message_class> getMessages() {
        return messages;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
