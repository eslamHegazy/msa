package com.ScalableTeam.reddit.app.comment;

import com.ScalableTeam.amqp.MessagePublisher;
import com.ScalableTeam.amqp.MessageQueues;
import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.arango.Comment;
import com.ScalableTeam.models.notifications.requests.NotificationSendRequest;
import com.ScalableTeam.models.reddit.VoteCommentForm;
import com.ScalableTeam.reddit.ICommand;
import com.ScalableTeam.reddit.app.entity.vote.CommentVote;
import com.ScalableTeam.reddit.app.repository.CommentRepository;
import com.ScalableTeam.reddit.app.repository.vote.CommentVoteRepository;
import com.ScalableTeam.reddit.app.repository.vote.UserVoteCommentRepository;
import com.ScalableTeam.reddit.app.validation.CommentVoteValidation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
@AllArgsConstructor
public class DownvoteCommentService implements ICommand<VoteCommentForm, String> {
    private final CommentRepository commentRepository;
    private final UserVoteCommentRepository userVoteCommentRepository;
    private final CommentVoteRepository commentVoteRepository;
    private final CommentVoteValidation commentVoteValidation;
    private final RabbitMQProducer rabbitMQProducer;

    @RabbitListener(queues = "${mq.queues.request.reddit.downvoteComment}")
    public String execute(VoteCommentForm voteCommentForm, Message message, @Header(MessagePublisher.HEADER_COMMAND) String commandName) throws Exception {
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info("Queue Listener::Command={}, CorrelationId={}, Vote Comment Form={}", commandName, correlationId, voteCommentForm);
        return execute(voteCommentForm);
    }

    @Transactional(rollbackFor = {Exception.class}, isolation = Isolation.REPEATABLE_READ)
    @Override
    public String execute(VoteCommentForm voteCommentForm) throws Exception {
        log.info("Service::Vote Comment Form={}", voteCommentForm);

        String userNameId = voteCommentForm.getUserNameId();
        String commentId = voteCommentForm.getCommentId();

        commentVoteValidation.validateCommentVote(userNameId, commentId);
        Comment comment = commentRepository.findById(commentId).get();
        Long oldUpvotesCount = comment.getUpvoteCount(), oldDownvotesCount = comment.getDownvoteCount();

        try {
            String responseMessage = userVoteCommentRepository.downvoteComment(userNameId, commentId);

            CommentVote commentVote = commentVoteRepository.findById(commentId).get();
            Long upvotesCount = commentVote.getUpvotes(), downvotesCount = commentVote.getDownvotes();
            comment.setUpvoteCount(upvotesCount);
            comment.setDownvoteCount(downvotesCount);
            commentRepository.save(comment);

            String result = String.format("User %s %s %s", userNameId, responseMessage, commentId);
            rabbitMQProducer.publishAsynchronous(MessageQueues.NOTIFICATIONS, "sendNotificationCommand", new NotificationSendRequest(
                    "Downvote Update on one of your comments",
                    result,
                    userNameId,
                    List.of(comment.getUserNameId())
            ));
            return result;
        } catch (Exception e) {
            comment.setUpvoteCount(oldUpvotesCount);
            comment.setDownvoteCount(oldDownvotesCount);
            commentRepository.save(comment);
            throw e;
        }
    }

    @RabbitListener(queues = "${mq.queues.response.reddit.downvoteComment}")
    public void receive(String response, Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info("Response Queue Listener::CorrelationId={}, response={}", correlationId, response);
    }
}
