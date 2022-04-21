package com.example.demo.apps.reddit.entity;
import com.arangodb.springframework.annotation.ArangoId;
import com.arangodb.springframework.annotation.Document;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

@Document("users")
public class User {
    @Id // db document field: _key
    private String userNameId;

    @ArangoId // db document field: _id
    private String arangoId;
    private String email;
    private String password;
    private String profilePhotoLink;
    private HashSet<String> followedChannels;

    private HashSet<String>followedUsers;

    public HashSet<String> getFollowedChannels() {
        return followedChannels;
    }

    public void setFollowedChannels(HashSet<String> followedChannels) {
        this.followedChannels = followedChannels;
    }

    public HashSet<String> getFollowedUsers() {
        return followedUsers;
    }

    public void setFollowedUsers(HashSet<String> followedUsers) {
        this.followedUsers = followedUsers;
    }


    public String getUserNameId() {
        return userNameId;
    }

    public void setUserNameId(String userNameId) {
        this.userNameId = userNameId;
    }

    public String getArangoId() {
        return arangoId;
    }

    public void setArangoId(String arangoId) {
        this.arangoId = arangoId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePhotoLink() {
        return profilePhotoLink;
    }

    public void setProfilePhotoLink(String profilePhotoLink) {
        this.profilePhotoLink = profilePhotoLink;
    }
}
