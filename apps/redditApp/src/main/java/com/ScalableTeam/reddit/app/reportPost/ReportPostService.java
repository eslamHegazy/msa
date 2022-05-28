package com.ScalableTeam.reddit.app.reportPost;

import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.arango.Channel;
import com.ScalableTeam.arango.Post;
import com.ScalableTeam.reddit.app.repository.ChannelRepository;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.models.reddit.ReportPostForm;
import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
public class ReportPostService implements MyCommand {

    @Autowired
    private final ChannelRepository channelRepository;
    @Autowired
    private final PostRepository postRepository;
    @Autowired
    GeneralConfig generalConfig;

    @Autowired
    public ReportPostService(ChannelRepository channelRepository, PostRepository postRepository, GeneralConfig generalConfig) {
        this.channelRepository =channelRepository;
        this.postRepository = postRepository;
        this.generalConfig = generalConfig;
    }
    @RabbitListener(queues = "${mq.queues.request.reddit.reportPost}")
    public String listenToRequestQueue(ReportPostForm reportPostForm , Message message) throws Exception {
        String correlationId = message.getMessageProperties().getCorrelationId();
        String indicator = generalConfig.getCommands().get("reportPost");
        log.info(indicator + "Service::reportPost, CorrelationId={}", correlationId);
        return execute(reportPostForm);
    }
    @Override
    @CachePut("postsCache")
    public String execute(Object body) throws Exception {

ReportPostForm request =  (ReportPostForm) body;
        String userId = request.getUserId();
        String postId = request.getPostId();
try {
        Optional<Post> post = postRepository.findById(postId);

        if (!post.isPresent()){
            throw new IllegalStateException("Post with Id: "+ postId + "does not exist in the database");
        }
        String redditId = post.get().getChannelId();
        Optional<Channel> reddit = channelRepository.findById(redditId);

        if(!reddit.isPresent()){
            throw new IllegalStateException("Channel with Id: "+ redditId + "does not exist in the database");
        }

        Channel actualReddit = reddit.get();
        HashMap<String, Boolean> report = new HashMap<String, Boolean>();
        report.put(request.toString(),true);
        try {
            if (actualReddit.getReports()==null){
                actualReddit.setReports(report);
                channelRepository.save(actualReddit);


            }else{
                channelRepository.addReport(redditId, report);

            }
        }
        catch (Exception e){
            throw e;
        }

        return "report added successfully";}
catch (Exception e){
    throw e;
}
    }

    @RabbitListener(queues = "${mq.queues.response.reddit.reportPost}")
    public void receive(String response, Message message) {
        String indicator = generalConfig.getCommands().get("reportPost");
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info(indicator + "Service::CorrelationId: {}, message: {}", correlationId, response);
    }
}
