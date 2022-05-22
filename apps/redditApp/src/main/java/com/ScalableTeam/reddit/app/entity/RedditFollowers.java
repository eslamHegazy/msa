package com.ScalableTeam.reddit.app.entity;

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
        @NamedStoredProcedureQuery(name = "RedditFollowers.followReddit",
                procedureName = "follow_reddit", parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "in_redditId", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "message", type = String.class)}),
        @NamedStoredProcedureQuery(name = "RedditFollowers.unfollowReddit",
                procedureName = "unfollow_reddit", parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "in_redditId", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "message", type = String.class)})
})
@SequenceGenerator(
        name = "reddit_followers_sequence",
        sequenceName = "reddit_followers_sequence",
        allocationSize = 1
)
public class RedditFollowers {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "reddit_followers_sequence"
    )
    private Long id;
    @Column(name = "redditId")
    private String channelNameId;
    private int followerCount;
}