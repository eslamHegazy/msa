package com.ScalableTeam.reddit.app.post;

import com.ScalableTeam.reddit.app.entity.Post;
import com.ScalableTeam.reddit.app.entity.vote.PostVote;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.reddit.app.repository.vote.PostVoteRepository;
import com.ScalableTeam.reddit.app.repository.vote.UserVotePostRepository;
import com.ScalableTeam.reddit.app.requestForms.VotePostForm;
import com.ScalableTeam.reddit.app.validation.PostVoteValidation;
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
public class UpvotePostService {
    private final PostRepository postRepository;
    private final UserVotePostRepository userVotePostRepository;
    private final PostVoteRepository postVoteRepository;
    private final GeneralConfig generalConfig;
    private final PostVoteValidation postVoteValidation;

    @Transactional(rollbackFor = {Exception.class})
    @RabbitListener(queues = "${mq.queues.request.reddit.upvotePost}")
    public String execute(VotePostForm votePostForm, Message message) throws Exception {
        String userNameId = votePostForm.getUserNameId();
        String postId = votePostForm.getPostId();
        String correlationId = message.getMessageProperties().getCorrelationId();
        String indicator = generalConfig.getCommands().get("upvotePost");
        log.info(indicator + "Service::Post Id={}, User Id={}, CorrelationId={}", postId, userNameId, correlationId);


        postVoteValidation.validatePostVote(userNameId, postId);
        String responseMessage = userVotePostRepository.upvotePost(userNameId, postId);

        PostVote postVote = postVoteRepository.findById(postId).get();
        Long upvotesCount = postVote.getUpvotes(), downvotesCount = postVote.getDownvotes();
        Post post = postRepository.findById(postId).get();
        post.setUpvoteCount(upvotesCount);
        post.setDownvoteCount(downvotesCount);
        postRepository.save(post);

        // todo: integrate notifications
        return String.format("User %s %s %s", userNameId, responseMessage, postId);
    }

    @RabbitListener(queues = "${mq.queues.response.reddit.upvotePost}")
    public void receive(String response, Message message) {
        String indicator = generalConfig.getCommands().get("upvotePost");
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info(indicator + "Service::CorrelationId: {}, message: {}", correlationId, response);
    }
}
