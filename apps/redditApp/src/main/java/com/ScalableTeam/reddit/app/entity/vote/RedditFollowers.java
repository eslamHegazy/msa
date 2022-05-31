package com.ScalableTeam.reddit.app.entity.vote;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reddit_followers")
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(name = "reddit_followers.followReddit",
                procedureName = "follow_reddit", parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "in_redditId", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "numfollowers", type = Integer.class)}),
        @NamedStoredProcedureQuery(name = "reddit_followers.unfollowReddit",
                procedureName = "unfollow_reddit", parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "in_redditId", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "numfollowers", type = Integer.class)})
})
@SequenceGenerator(
        name = "reddit_followers_sequence",
        sequenceName = "reddit_followers_sequence",
        allocationSize = 1
)
public class RedditFollowers {
    @Id
    @Column(name = "redditId")
    private String channelNameId;
    private int followerCount;

    public String getChannelNameId() {
        return channelNameId;
    }

    public void setChannelNameId(String channelNameId) {
        this.channelNameId = channelNameId;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }
}