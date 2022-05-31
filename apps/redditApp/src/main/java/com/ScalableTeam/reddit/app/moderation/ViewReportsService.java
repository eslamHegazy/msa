package com.ScalableTeam.reddit.app.moderation;

import com.ScalableTeam.amqp.MessagePublisher;
import com.ScalableTeam.arango.Channel;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.models.reddit.ViewReportsForm;
import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.repository.ChannelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.messaging.handler.annotation.Header;
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

    private final String serviceName = "viewReports";

    @RabbitListener(queues = "${mq.queues.request.reddit." + serviceName + "}", returnExceptions = "true")
    public String listenToRequestQueue(ViewReportsForm viewReportsForm, Message message, @Header(MessagePublisher.HEADER_COMMAND) String commandName) throws Exception {
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info("Queue Listener::Command={}, CorrelationId={}, View Reports Form={}", commandName, correlationId, viewReportsForm);
        return execute(viewReportsForm);
    }


    @Override
    public String execute(Object body) throws Exception {

        try {
            ViewReportsForm request = (ViewReportsForm) body;
            log.info("Service::View Reports Form={}", request);
            Optional<Channel> reddit = channelRepository.findById(request.getRedditId());
            if (!reddit.isPresent()) {
                throw new IllegalStateException("reddit " + request.getRedditId() + " not found in DB");
            }

            Channel channel = reddit.get();
            System.out.println(channel.getReports());
            if (channel.getModerators().containsKey(request.getModId())) {
                if (channel.getReports() != null) {
                    return channel.getReports().toString();
                } else {
                    return "no reports for this channel";
                }
            } else {
                return "user " + request.getModId() + " is not a mod of channel " + request.getRedditId();
            }
        } catch (Exception e) {
            throw e;
        }

    }
}
