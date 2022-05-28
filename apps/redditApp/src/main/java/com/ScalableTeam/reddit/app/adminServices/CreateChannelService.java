package com.ScalableTeam.reddit.app.adminServices;

import com.ScalableTeam.arango.Channel;
import com.ScalableTeam.arango.User;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.models.reddit.CreateChannelForm;
import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.repository.ChannelRepository;
import com.ScalableTeam.reddit.config.GeneralConfig;
import com.arangodb.springframework.core.ArangoOperations;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
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
    private final GeneralConfig generalConfig;

    @RabbitListener(queues = "${mq.queues.request.reddit.createChannel}")
    public String listenToRequestQueue(CreateChannelForm createChannelForm, Message message) throws Exception {
        String correlationId = message.getMessageProperties().getCorrelationId();
        String indicator = generalConfig.getCommands().get("createChannel");
        log.info(indicator + "Service::CreateChannel, CorrelationId={}", correlationId);
        return execute(createChannelForm);
    }

    @Override
    public String execute(Object createChannelFormObj) throws Exception {
        log.info(generalConfig.getCommands().get("createChannel") + "Service", createChannelFormObj);
        try {
            CreateChannelForm createChannelForm = (CreateChannelForm) createChannelFormObj;
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
        String indicator = generalConfig.getCommands().get("createChannel");
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info(indicator + "Service::CorrelationId: {}, message: {}", correlationId, response);
    }
}
