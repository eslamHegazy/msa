package com.ScalableTeam.reddit.app.moderation;

import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.entity.Channel;
import com.ScalableTeam.reddit.app.repository.ChannelRepository;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.reddit.app.repository.UserRepository;
import com.ScalableTeam.reddit.app.requestForms.ViewReportsForm;
import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.Optional;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
public class ViewReportsService implements MyCommand {

    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GeneralConfig generalConfig;

    private final String serviceName = "viewReports";

    @RabbitListener(queues = "${mq.queues.request.reddit."+serviceName+"}",returnExceptions = "true")
    public String listenToRequestQueue(ViewReportsForm viewReportsForm, Message message) throws Exception {
        String correlationId = message.getMessageProperties().getCorrelationId();
        String indicator = generalConfig.getCommands().get(serviceName);
        log.info(indicator + "Service::View Reports, CorrelationId={}", correlationId);
        return execute(viewReportsForm);
    }


    @Override
    public String execute(Object body) throws Exception {

        try {
            ViewReportsForm request  = (ViewReportsForm) body;
            Optional<Channel> reddit = channelRepository.findById(request.getRedditId());
            if(!reddit.isPresent()){
                throw new IllegalStateException("reddit "+ request.getRedditId()+" not found in DB");
            }

            Channel channel = reddit.get();
            System.out.println(channel.getReports());
            if(channel.getModerators().containsKey(request.getModId())){
                if (channel.getReports()!=null){
                    return channel.getReports().toString();
                }else{
                    return "no reports for this channel";
                }
            }else{
                return "user "+ request.getModId()+" is not a mod of channel "+request.getRedditId();
            }
        }catch(Exception e){
throw  e;
        }

    }
}
