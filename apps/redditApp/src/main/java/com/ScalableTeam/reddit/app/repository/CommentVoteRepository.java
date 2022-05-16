package com.ScalableTeam.reddit.app.repository;

import com.ScalableTeam.reddit.app.entity.CommentVote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentVoteRepository extends JpaRepository<CommentVote, String> {
}