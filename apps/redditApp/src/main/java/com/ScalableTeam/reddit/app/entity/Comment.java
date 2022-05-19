package com.ScalableTeam.reddit.app.entity;
import com.arangodb.springframework.annotation.ArangoId;
import com.arangodb.springframework.annotation.Document;
import lombok.Builder;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Document("comments")
@Builder
public class Comment implements Serializable {
    @Id // db document field: _key
    private String id;

    @ArangoId // db document field: _id
    private String arangoId;
//    private Comment reply;
    private boolean commentOnPost;
    private String commentParentId;
    private String postId;
    private String body;
    private String userNameId;
    private long upvoteCount;
    private long downvoteCount;

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

//    public Comment getReply() {
//        return reply;
//    }
//
//    public void setReply(Comment reply) {
//        this.reply = reply;
//    }

    public boolean isCommentOnPost() {
        return commentOnPost;
    }

    public void setCommentOnPost(boolean commentOnPost) {
        this.commentOnPost = commentOnPost;
    }

    public String getCommentParentId() {
        return commentParentId;
    }

    public void setCommentParentId(String commentParentId) {
        this.commentParentId = commentParentId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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
    public String getUserNameId() {
        return userNameId;
    }

    public void setUserNameId(String userNameId) {
        this.userNameId = userNameId;
    }
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
    @Override
    public String toString(){
        return "comment Id: "+id+"Comment On Post: "+commentOnPost+"Parent Id: "+commentParentId+"userNameId: "+userNameId;
    }
}
