package com.ScalableTeam.reddit.app.adminServices;

import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.entity.Channel;
import com.ScalableTeam.reddit.app.entity.User;
import com.ScalableTeam.reddit.app.repository.ChannelRepository;
import com.ScalableTeam.reddit.app.repository.UserRepository;
import com.ScalableTeam.models.reddit.CreateChannelForm;
import com.ScalableTeam.reddit.config.GeneralConfig;
import com.arangodb.springframework.core.ArangoOperations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
public class CreateChannelService implements MyCommand {
    @Autowired
    private ArangoOperations operations;
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GeneralConfig generalConfig;
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
            Optional<User>userOptional=userRepository.findById(createChannelForm.getAdminId());
            if(userOptional.isEmpty()){
                throw new Exception();
            }

            Channel channel=new Channel();
            channel.setChannelNameId(createChannelForm.getChannelNameId());
            channel.setAdminId(createChannelForm.getAdminId());
            HashMap<String,Boolean>moderators=new HashMap<>();
            moderators.put(createChannelForm.getAdminId(),true);
            channel.setModerators(moderators);
            channelRepository.save(channel);

            User channelCreator=userOptional.get();
            HashMap<String,Boolean>followedChannels=new HashMap<>();
            followedChannels.put(channel.getChannelNameId(),true);
            userRepository.updateFollowedChannelsWithID(channelCreator.getUserNameId(),followedChannels);
            return "Channel added Successfully";

        }
        catch (Exception e){
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
