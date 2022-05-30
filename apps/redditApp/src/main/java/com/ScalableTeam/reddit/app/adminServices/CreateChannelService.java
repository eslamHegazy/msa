package com.ScalableTeam.reddit.app.adminServices;

import com.ScalableTeam.amqp.MessagePublisher;
import com.ScalableTeam.arango.Channel;
import com.ScalableTeam.arango.User;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.models.reddit.CreateChannelForm;
import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.repository.ChannelRepository;
import com.arangodb.springframework.core.ArangoOperations;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class CreateChannelService implements MyCommand {
    private final ArangoOperations operations;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    @RabbitListener(queues = "${mq.queues.request.reddit.createChannel}")
    public String listenToRequestQueue(CreateChannelForm createChannelForm, Message message, @Header(MessagePublisher.HEADER_COMMAND) String commandName) throws Exception {
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info("Queue Listener::Command={}, CorrelationId={}, Create Channel Form={}", commandName, correlationId, createChannelForm);
        return execute(createChannelForm);
    }

    @Override
    public String execute(Object createChannelFormObj) throws Exception {
        try {
            CreateChannelForm createChannelForm = (CreateChannelForm) createChannelFormObj;
            log.info("Service::Create Channel Form={}", createChannelForm);
            Optional<Channel> channelOptional = channelRepository.findById(createChannelForm.getChannelNameId());
            if (channelOptional.isPresent()) {
                throw new Exception();
            }
            Optional<User> userOptional = userRepository.findById(createChannelForm.getAdminId());
            if (userOptional.isEmpty()) {
                throw new Exception();
            }

            Channel channel = new Channel();
            channel.setChannelNameId(createChannelForm.getChannelNameId());
            channel.setAdminId(createChannelForm.getAdminId());
            HashMap<String, Boolean> moderators = new HashMap<>();
            moderators.put(createChannelForm.getAdminId(), true);
            channel.setModerators(moderators);
            channelRepository.save(channel);

            User channelCreator = userOptional.get();
            HashMap<String, Boolean> followedChannels = new HashMap<>();
            followedChannels.put(channel.getChannelNameId(), true);
            userRepository.updateFollowedChannelsWithID(channelCreator.getUserNameId(), followedChannels);
            return "Channel added Successfully";

        } catch (Exception e) {
            throw new Exception("Error: Couldn't add Channel");
        }
    }

    @RabbitListener(queues = "${mq.queues.response.reddit.createChannel}")
    public void receive(String response, Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info("Response Queue Listener::CorrelationId={}, response={}", correlationId, response);
    }
}
