package com.ScalableTeam.reddit.app.entity;
import com.arangodb.springframework.annotation.ArangoId;
import com.arangodb.springframework.annotation.Document;
import org.springframework.data.annotation.Id;

import java.util.HashMap;
import java.util.HashSet;

@Document("channels")
public class Channel {
    @Id // db document field: _key
    private String id;

    @ArangoId // db document field: _id
    private String arangoId;
    private String name;
    private String adminId;
    private HashMap<String,Boolean> moderators;
    private HashMap<String, Boolean> bannedUsers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArangoId() {
        return arangoId;
    }

    public void setArangoId(String arangoId) {
        this.arangoId = arangoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public HashMap<String,Boolean> getModerators() {return moderators;}

    public void setModerators(HashMap<String,Boolean> moderators) {
        this.moderators = moderators;
    }

    public HashMap<String, Boolean> getBannedUsers() {
        return bannedUsers;
    }

    public void setBannedUsers(HashMap<String, Boolean> bannedUsers) {
        this.bannedUsers = bannedUsers;
    }

    @Override
    public String toString() {
        return "Channel [id=" + id + ", name=" + name + ", adminId=" + adminId + moderators.toString()+"]";
    }
}
