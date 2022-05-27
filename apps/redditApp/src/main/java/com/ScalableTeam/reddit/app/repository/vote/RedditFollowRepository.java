package com.ScalableTeam.reddit.app.repository.vote;

import com.ScalableTeam.reddit.app.entity.vote.RedditFollowers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RedditFollowRepository extends JpaRepository<RedditFollowers, String> {
    @Procedure(name = "reddit_followers.followReddit")
    @Transactional
    String followReddit(@Param("in_redditId") String channelNameId);
}
