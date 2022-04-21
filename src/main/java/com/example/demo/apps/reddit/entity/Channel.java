package com.example.demo.apps.reddit.entity;
import com.arangodb.springframework.annotation.ArangoId;
import com.arangodb.springframework.annotation.Document;
import org.springframework.data.annotation.Id;

import java.util.HashSet;

@Document("channels")
public class Channel {
    @Id // db document field: _key
    private String id;

    @ArangoId // db document field: _id
    private String arangoId;
    private String name;
    private String adminId;
    private HashSet<User> moderators;

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

    public HashSet<User> getModerators() {return moderators;}

    public void setModerators(HashSet<User> moderators) {
        this.moderators = moderators;
    }
    @Override
    public String toString() {
        return "Channel [id=" + id + ", name=" + name + ", adminId=" + adminId + moderators.toString()+"]";
    }
}
