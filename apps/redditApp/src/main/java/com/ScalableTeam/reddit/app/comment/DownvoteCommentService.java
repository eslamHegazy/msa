package com.ScalableTeam.reddit.app.comment;

import com.ScalableTeam.reddit.app.entity.Comment;
import com.ScalableTeam.reddit.app.entity.vote.CommentVote;
import com.ScalableTeam.reddit.app.repository.CommentRepository;
import com.ScalableTeam.reddit.app.repository.vote.CommentVoteRepository;
import com.ScalableTeam.reddit.app.repository.vote.UserVoteCommentRepository;
import com.ScalableTeam.reddit.app.requestForms.VoteCommentForm;
import com.ScalableTeam.reddit.app.validation.CommentVoteValidation;
import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
@AllArgsConstructor
public class DownvoteCommentService {
    private final CommentRepository commentRepository;
    private final UserVoteCommentRepository userVoteCommentRepository;
    private final CommentVoteRepository commentVoteRepository;
    private final GeneralConfig generalConfig;
    private final CommentVoteValidation commentVoteValidation;

    @Transactional(rollbackFor = {Exception.class})
    @RabbitListener(queues = "${mq.queues.request.reddit.downvoteComment}")
    public String execute(VoteCommentForm voteCommentForm, Message message) throws Exception {
        String userNameId = voteCommentForm.getUserNameId();
        String commentId = voteCommentForm.getCommentId();
        String correlationId = message.getMessageProperties().getCorrelationId();
        String indicator = generalConfig.getCommands().get("downvoteComment");
        log.info(indicator + "Service::Comment Id={}, User Id={}, correlationId={}", commentId, userNameId, correlationId);

        commentVoteValidation.validateCommentVote(userNameId, commentId);
        String responseMessage = userVoteCommentRepository.downvoteComment(userNameId, commentId);

        CommentVote commentVote = commentVoteRepository.findById(commentId).get();
        Long upvotesCount = commentVote.getUpvotes(), downvotesCount = commentVote.getDownvotes();
        Comment comment = commentRepository.findById(commentId).get();
        comment.setUpvoteCount(upvotesCount);
        comment.setDownvoteCount(downvotesCount);
        commentRepository.save(comment);

        // todo: integrate notifications
        return String.format("User %s %s %s", userNameId, responseMessage, commentId);
    }

    @RabbitListener(queues = "${mq.queues.response.reddit.downvoteComment}")
    public void receive(String response, Message message) {
        String indicator = generalConfig.getCommands().get("downvoteComment");
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info(indicator + "Service::CorrelationId: {}, message: {}", correlationId, response);
    }

}
