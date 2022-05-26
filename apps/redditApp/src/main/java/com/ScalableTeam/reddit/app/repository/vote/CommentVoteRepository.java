package com.ScalableTeam.reddit.app.repository.vote;

import com.ScalableTeam.reddit.app.entity.vote.CommentVote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentVoteRepository extends JpaRepository<CommentVote, String> {
}