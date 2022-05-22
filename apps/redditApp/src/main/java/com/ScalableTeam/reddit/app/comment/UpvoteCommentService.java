package com.ScalableTeam.reddit.app.comment;

import com.ScalableTeam.reddit.app.entity.Comment;
import com.ScalableTeam.reddit.app.entity.vote.CommentVote;
import com.ScalableTeam.reddit.app.repository.CommentRepository;
import com.ScalableTeam.reddit.app.repository.vote.CommentVoteRepository;
import com.ScalableTeam.reddit.app.repository.vote.UserVoteCommentRepository;
import com.ScalableTeam.reddit.app.validation.CommentVoteValidation;
import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
@AllArgsConstructor
public class UpvoteCommentService {
    private final CommentRepository commentRepository;
    private final UserVoteCommentRepository userVoteCommentRepository;
    private final CommentVoteRepository commentVoteRepository;
    private final GeneralConfig generalConfig;
    private final CommentVoteValidation commentVoteValidation;

    @Transactional(rollbackFor = {Exception.class})
    public String execute(Object obj) throws Exception {
        Map<String, Object> attributes = (Map<String, Object>) obj;
        String userNameId = (String) attributes.get("userNameId");
        String commentId = (String) attributes.get("commentId");
        String indicator = generalConfig.getCommands().get("upvoteComment");
        log.info(indicator + "Service::Comment Id={}, User Id={}", commentId, userNameId);

        commentVoteValidation.validateCommentVote(userNameId, commentId);
        String responseMessage = userVoteCommentRepository.upvoteComment(userNameId, commentId);

        CommentVote commentVote = commentVoteRepository.findById(commentId).get();
        Long upvotesCount = commentVote.getUpvotes(), downvotesCount = commentVote.getDownvotes();
        Comment comment = commentRepository.findById(commentId).get();
        comment.setUpvoteCount(upvotesCount);
        comment.setDownvoteCount(downvotesCount);
        commentRepository.save(comment);

        // todo: integrate notifications
        return String.format("User %s %s %s", userNameId, responseMessage, commentId);
    }

}
