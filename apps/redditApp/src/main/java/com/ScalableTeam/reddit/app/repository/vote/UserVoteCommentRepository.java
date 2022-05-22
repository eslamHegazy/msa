package com.ScalableTeam.reddit.app.repository.vote;

import com.ScalableTeam.reddit.app.entity.vote.UserVoteComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVoteCommentRepository extends JpaRepository<UserVoteComment, Long> {
    @Procedure(name = "UserVoteComment.upvoteComment")
    String upvoteComment(@Param("in_userId") String userNameId, @Param("in_commentId") String commentId);
    @Procedure(name = "UserVoteComment.downvoteComment")
    String downvoteComment(@Param("in_userId") String userNameId, @Param("in_commentId") String commentId);
}
