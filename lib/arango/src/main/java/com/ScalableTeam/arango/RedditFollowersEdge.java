package com.ScalableTeam.arango;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import lombok.Builder;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Edge("redditFollowers")
@Builder
public class RedditFollowersEdge implements Serializable {
    @Id
    private String id;
    @From
    private Channel channel;
    @To
    private User user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
