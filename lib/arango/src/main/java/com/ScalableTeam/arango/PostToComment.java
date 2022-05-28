package com.ScalableTeam.arango;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import lombok.Builder;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Edge("postToComment")
@Builder
public class PostToComment implements Serializable {
    @Id
    private String id;
    @From
    private Post post;
    @To
    private Comment comment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
