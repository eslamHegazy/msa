package com.ScalableTeam.arango;

import com.arangodb.springframework.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

@Document("posts")
@AllArgsConstructor
@Builder
@NoArgsConstructor
@PersistentIndex(fields = {"_key", "userNameId"})
@PersistentIndex(fields = {"_key", "channelId"})


public class Post implements Serializable {
    @Field("_key")
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
    private Date time;

    @Relations(edges = PostToComment.class, lazy = true, direction = Relations.Direction.OUTBOUND)
    private Collection<Comment> comments;
    private HashMap<String, String> reports; //userId : reason

//    public Post(){}

    public Collection<Comment> getComments() {
        return comments;
    }

    public void setComments(Collection<Comment> comments) {
        this.comments = comments;
    }

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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public HashMap<String, String> getReports() {
        return reports;
    }

    public void setReports(HashMap<String, String> reports) {
        this.reports = reports;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", arangoId='" + arangoId + '\'' +
                ", channelId='" + channelId + '\'' +
                ", userNameId='" + userNameId + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", photoLink='" + photoLink + '\'' +
                ", upvoteCount=" + upvoteCount +
                ", downvoteCount=" + downvoteCount +
                ", time=" + time +
                ", comments=" + comments +
                ", reports=" + reports +
                '}';
    }
}