package com.ScalableTeam.reddit.app.entity;
import com.arangodb.springframework.annotation.ArangoId;
import com.arangodb.springframework.annotation.Document;
import lombok.Builder;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.HashMap;

@Document("posts")
@Builder
public class Post {
    @Id // db document field: _key
    private String id;

    @ArangoId // db document field: _id
    private String arangoId;
    private String channelId;
    private String userNameId;
    private String title;
    private String body;
    private String photoLink;
    private long upvoteCount;
    private long downvoteCount;
    private Instant time;
    private HashMap<String, Comment> comments;

    private HashMap<String, String> reports; //userId : reason

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

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getUserNameId() {
        return userNameId;
    }

    public void setUserNameId(String userNameId) {
        this.userNameId = userNameId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }

    public long getUpvoteCount() {
        return upvoteCount;
    }

    public void setUpvoteCount(long upvoteCount) {
        this.upvoteCount = upvoteCount;
    }

    public long getDownvoteCount() {
        return downvoteCount;
    }

    public void setDownvoteCount(long downvoteCount) {
        this.downvoteCount = downvoteCount;
    }

    public HashMap<String,Comment> getComments() {
        return comments;
    }

    public void setComments(HashMap<String,Comment> comments) {
        this.comments = comments;
    }
    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public HashMap<String, String> getReports() {
        return reports;
    }

    public void setReports(HashMap<String, String> reports) {
        this.reports = reports;
    }
    @Override
    public String toString(){
        return "Post Id: "+id+" userNameId: "+userNameId;
    }

}
