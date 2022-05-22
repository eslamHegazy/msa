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
@Table(name = "comment_votes")
public class CommentVote {
    @Id
    private String commentId;
    private Long upvotes;
    private Long downvotes;
}
