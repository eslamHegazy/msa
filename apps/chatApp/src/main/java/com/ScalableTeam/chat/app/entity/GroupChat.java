package com.ScalableTeam.chat.app.entity;

import java.util.ArrayList;
import java.util.List;

public class GroupChat {

    String groupChatId;
    List<String> users;
    List<Message> messages;
    String name;
    String description;
    String adminId;

    public GroupChat() {

    }

    public GroupChat(String groupChatId) {
        this.groupChatId = groupChatId;
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
                     List<String> users) {
        this.name = name;
        this.description = description;
        this.adminId = adminId;
        this.users = users;
    }


    public GroupChat(String groupChatId,
                     String name,
                     String description,
                     String adminId,
                     List<String> users
    ) {
        this.groupChatId = groupChatId;
        this.users = users;
        this.name = name;
        this.description = description;
        this.adminId = adminId;
    }

    public GroupChat(String groupChatId,
                     ArrayList<String> users,
                     ArrayList<Message> messages,
                     String name,
                     String description,
                     String adminId) {
        this.groupChatId = groupChatId;
        this.users = users;
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

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    public List<Message> getMessages() {
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

    @Override
    public String toString() {
        return "GroupChat{" +
                "groupChatId='" + groupChatId + '\'' +
                ", users=" + users +
                ", messages=" + messages +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", adminId='" + adminId + '\'' +
                '}';
    }
}
