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
@Table(name = "user_vote_comment")
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(name = "UserVoteComment.upvoteComment",
                procedureName = "upvote_comment", parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "in_userId", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "in_commentId", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "message", type = String.class)}),
        @NamedStoredProcedureQuery(name = "UserVoteComment.downvoteComment",
                procedureName = "downvote_comment", parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "in_userId", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "in_commentId", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "message", type = String.class)})
})
@SequenceGenerator(
        name = "user_vote_comment_sequence",
        sequenceName = "user_vote_comment_sequence",
        allocationSize = 1
)
public class UserVoteComment {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_vote_comment_sequence"
    )
    private Long id;
    @Column(name = "userId")
    private String userNameId;
    private String commentId;
    private int type;
}