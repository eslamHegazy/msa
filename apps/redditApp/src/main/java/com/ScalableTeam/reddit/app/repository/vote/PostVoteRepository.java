package com.ScalableTeam.reddit.app.repository.vote;

import com.ScalableTeam.reddit.app.entity.vote.PostVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostVoteRepository extends JpaRepository<PostVote, String> {
}
