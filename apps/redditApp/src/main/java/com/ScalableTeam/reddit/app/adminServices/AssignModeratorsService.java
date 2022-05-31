package com.ScalableTeam.reddit.app.adminServices;

import com.ScalableTeam.amqp.MessagePublisher;
import com.ScalableTeam.arango.Channel;
import com.ScalableTeam.arango.User;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.models.reddit.AssignModeratorsForm;
import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.repository.ChannelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
public class AssignModeratorsService implements MyCommand {

    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private UserRepository userRepository;

    @RabbitListener(queues = "${mq.queues.request.reddit.assignModerators}")
    public String listenToRequestQueue(AssignModeratorsForm assignModeratorsForm, Message message, @Header(MessagePublisher.HEADER_COMMAND) String commandName) throws Exception {
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info("Queue Listener::Command={}, CorrelationId={}, Assign Moderators Form={}", commandName, correlationId, assignModeratorsForm);
        return execute(assignModeratorsForm);
    }

    @Override
    public String execute(Object assignModeratorsFormObj) throws Exception {
        try {
            AssignModeratorsForm assignModeratorsForm = (AssignModeratorsForm) assignModeratorsFormObj;
            log.info("Service::Assign Moderators Form={}", assignModeratorsForm);
            Optional<Channel> channelOptional = channelRepository.findById(assignModeratorsForm.getChannelNameId());
            Optional<User> adminOptional = userRepository.findById(assignModeratorsForm.getAdminId());
            Optional<User> moderatorOptional = userRepository.findById(assignModeratorsForm.getModeratorId());

            if (channelOptional.isEmpty() || adminOptional.isEmpty() || moderatorOptional.isEmpty()) {
                throw new Exception();
            }
            Channel channel = channelOptional.get();
            User moderator = moderatorOptional.get();
            User admin = adminOptional.get();
            if (channel.getAdminId().compareTo(admin.getUserNameId()) != 0) {
                throw new Exception();
            }
            if (!moderator.getFollowedChannels().containsKey(channel.getChannelNameId())) {
                throw new Exception();
            }

            HashMap<String, Boolean> moderators = new HashMap<>();
            moderators.put(moderator.getUserNameId(), true);
            channelRepository.updateModeratorsWithID(channel.getChannelNameId(), moderators);
            //Continue adding the moderator
            return "Moderator added to Channel Successfully";

        } catch (Exception e) {
            throw new Exception("Error: Couldn't add Channel");
        }
    }

    @RabbitListener(queues = "${mq.queues.response.reddit.assignModerators}")
    public void receive(String response, Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info("Response Queue Listener::CorrelationId={}, response={}", correlationId, response);
    }
}
