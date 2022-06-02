package com.ScalableTeam.reddit.app.reportPost;

import com.ScalableTeam.amqp.MessagePublisher;
import com.ScalableTeam.arango.Channel;
import com.ScalableTeam.arango.Post;
import com.ScalableTeam.models.reddit.ReportPostForm;
import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.repository.ChannelRepository;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
@AllArgsConstructor
public class ReportPostService implements MyCommand {

    private ChannelRepository channelRepository;
    private PostRepository postRepository;



    @RabbitListener(queues = "${mq.queues.request.reddit.reportPost}")
    public String listenToRequestQueue(ReportPostForm reportPostForm, Message message, @Header(MessagePublisher.HEADER_COMMAND) String commandName) throws Exception {
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info("Queue Listener::Command={}, CorrelationId={}, Report Post Form={}", commandName, correlationId, reportPostForm);
        return execute(reportPostForm);
    }

    @Override
    @CachePut("postsCache")
    public String execute(Object body) throws Exception {

        ReportPostForm request = (ReportPostForm) body;
        log.info("Service::Report Post Form={}", request);
        String userId = request.getUserId();
        String postId = request.getPostId();
        try {
            Optional<Post> post = postRepository.findById(postId);

            if (!post.isPresent()) {
                throw new IllegalStateException("Post with Id: " + postId + "does not exist in the database");
            }
            String redditId = post.get().getChannelId();
            Optional<Channel> reddit = channelRepository.findById(redditId);

            if (!reddit.isPresent()) {
                throw new IllegalStateException("Channel with Id: " + redditId + "does not exist in the database");
            }

            Channel actualReddit = reddit.get();
            HashMap<String, Boolean> report = new HashMap<String, Boolean>();
            report.put(request.toString(), true);
            try {
                if (actualReddit.getReports() == null) {
                    actualReddit.setReports(report);
                    channelRepository.save(actualReddit);


                } else {
                    channelRepository.addReport(redditId, report);

                }
            } catch (Exception e) {
                throw e;
            }

            return "report added successfully";
        } catch (Exception e) {
            throw e;
        }
    }

    @RabbitListener(queues = "${mq.queues.response.reddit.reportPost}")
    public void receive(String response, Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info("Response Queue Listener::CorrelationId={}, response={}", correlationId, response);
    }
}
