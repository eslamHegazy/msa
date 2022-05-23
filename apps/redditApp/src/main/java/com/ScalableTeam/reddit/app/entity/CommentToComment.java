package com.ScalableTeam.reddit.app.entity;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import lombok.Builder;

import javax.persistence.Id;
import java.io.Serializable;

@Edge("commentToComment")
@Builder
public class CommentToComment implements Serializable {
    @Id
    private String id;
    @From
    private Comment parentComment;
    @To
    private Comment childComment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Comment getParentComment() {
        return parentComment;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    public Comment getChildComment() {
        return childComment;
    }

    public void setChildComment(Comment childComment) {
        this.childComment = childComment;
    }
}
