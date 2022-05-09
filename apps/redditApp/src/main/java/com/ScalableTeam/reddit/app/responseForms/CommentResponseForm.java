package com.ScalableTeam.reddit.app.responseForms;

import com.ScalableTeam.reddit.app.entity.Comment;

public class CommentResponseForm {
    private String userId;
    private String postId;
    private Comment comment;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
