package com.ScalableTeam.reddit.app.repository;

import com.ScalableTeam.reddit.app.entity.UserVotePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVotePostRepository extends JpaRepository<UserVotePost, Long> {
    @Procedure(name = "UserVotePost.upvotePost")
    String upvotePost(@Param("in_userId") String userNameId, @Param("in_postId") String postId);
    @Procedure(name = "UserVotePost.downvotePost")
    String downvotePost(@Param("in_userId") String userNameId, @Param("in_postId") String postId);
}
