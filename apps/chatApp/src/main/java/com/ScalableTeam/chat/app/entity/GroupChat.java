package com.ScalableTeam.chat.app.entity;

import java.util.ArrayList;
import java.util.List;

public class GroupChat {

    String groupChatId;
    ArrayList<String> userIds;
    ArrayList<Message> messages;
    String name;
    String description;
    String adminId;

    public GroupChat(){

    }

    public GroupChat(String name,
                     String description) {
        this.name = name;
        this.description = description;
    }
    public GroupChat(String name,
                     String description,
                     String adminId) {
        this.name = name;
        this.description = description;
        this.adminId = adminId;
    }

    public GroupChat(String name,
                     String description,
                     String adminId,
                     ArrayList<String> userIds) {
        this.name = name;
        this.description = description;
        this.adminId = adminId;
        this.userIds = userIds;
    }


    public GroupChat(String groupChatId,
                     ArrayList<String> userIds,
                     ArrayList<Message> messages,
                     String name,
                     String description,
                     String adminId) {
        this.groupChatId = groupChatId;
        this.userIds = userIds;
        this.messages = messages;
        this.name = name;
        this.description = description;
        this.adminId = adminId;
    }

    public String getGroupChatId() {
        return groupChatId;
    }

    public void setGroupChatId(String groupChatId) {
        this.groupChatId = groupChatId;
    }

    public ArrayList<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(ArrayList<String> userIds) {
        this.userIds = userIds;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }
}
