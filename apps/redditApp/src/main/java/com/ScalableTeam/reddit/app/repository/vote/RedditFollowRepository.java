package com.ScalableTeam.reddit.app.repository.vote;

import com.ScalableTeam.reddit.app.entity.vote.RedditFollowers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RedditFollowRepository extends JpaRepository<RedditFollowers, String> {
}
