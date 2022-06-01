package com.ScalableTeam.chat.app.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PrivateChat {
    public String privateChatId;
    List<String> users;
//    List<Message> messages;

    public PrivateChat() {

    }

    public PrivateChat(String privateChatId) {
        this.privateChatId = privateChatId;
    }

    public PrivateChat(List<String> users) {
        this.users = users;
    }

    public PrivateChat(String privateChatId, ArrayList<String> users) {
        this.privateChatId = privateChatId;
        this.users = users;
//        this.messages = messages;
    }

    public String getPrivateChatId() {
        return privateChatId;
    }

    public void setPrivateChatId(String privateChatId) {
        this.privateChatId = privateChatId;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

//    public List<Message> getMessages() {
//        return messages;
//    }
//
//    public void setMessages(ArrayList<Message> messages) {
//        this.messages = messages;
//    }

    @Override
    public String toString() {
        return "PrivateChat{" +
                "privateChatId='" + privateChatId + '\'' +
                ", users=" + users +
                '}';
    }
}
