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
@Table(name = "user_vote_post")
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(name = "UserVotePost.upvotePost",
                procedureName = "upvote_post", parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "in_userId", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "in_postId", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "message", type = String.class)}),
        @NamedStoredProcedureQuery(name = "UserVotePost.downvotePost",
                procedureName = "downvote_post", parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "in_userId", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "in_postId", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "message", type = String.class)})
})
@SequenceGenerator(
        name = "user_vote_post_sequence",
        sequenceName = "user_vote_post_sequence",
        allocationSize = 1
)
public class UserVotePost {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_vote_post_sequence"
    )
    private Long id;
    @Column(name = "userId")
    private String userNameId;
    private String postId;
    private int type;
}