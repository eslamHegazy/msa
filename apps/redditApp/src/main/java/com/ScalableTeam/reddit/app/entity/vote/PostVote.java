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
@Table(name = "post_votes")
public class PostVote {
    @Id
    private String postId;
    private Long upvotes;
    private Long downvotes;
}
