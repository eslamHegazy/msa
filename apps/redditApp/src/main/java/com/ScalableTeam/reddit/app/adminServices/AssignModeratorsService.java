package com.ScalableTeam.reddit.app.adminServices;

import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.entity.Channel;
import com.ScalableTeam.reddit.app.entity.User;
import com.ScalableTeam.reddit.app.repository.ChannelRepository;
import com.ScalableTeam.reddit.app.repository.UserRepository;
import com.ScalableTeam.reddit.app.requestForms.AssignModeratorsForm;
import com.ScalableTeam.reddit.app.requestForms.CreateChannelForm;
import com.ScalableTeam.reddit.config.GeneralConfig;
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
public class AssignModeratorsService implements MyCommand {
    @Autowired
    private GeneralConfig generalConfig;
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private UserRepository userRepository;
    @RabbitListener(queues = "${mq.queues.request.reddit.assignModerators}")
    public String listenToRequestQueue(AssignModeratorsForm assignModeratorsForm, Message message) throws Exception {
        String correlationId = message.getMessageProperties().getCorrelationId();
        String indicator = generalConfig.getCommands().get("assignModerators");
        log.info(indicator + "Service::AssignModerators, CorrelationId={}", correlationId);
        return execute(assignModeratorsForm);
    }
    @Override
    public String execute(Object assignModeratorsFormObj) throws Exception {
        log.info(generalConfig.getCommands().get("assignModerators") + "Service", assignModeratorsFormObj);
        try {
            AssignModeratorsForm assignModeratorsForm = (AssignModeratorsForm) assignModeratorsFormObj;
            Optional<Channel> channelOptional = channelRepository.findById(assignModeratorsForm.getChannelNameId());
            Optional<User>adminOptional=userRepository.findById(assignModeratorsForm.getAdminId());
            Optional<User>moderatorOptional=userRepository.findById(assignModeratorsForm.getModeratorId());

            if (channelOptional.isEmpty()||adminOptional.isEmpty()||moderatorOptional.isEmpty()) {
                throw new Exception();
            }
            Channel channel=channelOptional.get();
            User moderator=moderatorOptional.get();
            User admin=adminOptional.get();
            if(channel.getAdminId().compareTo(admin.getUserNameId())!=0){
                throw new Exception();
            }
            if(!moderator.getFollowedChannels().containsKey(channel.getChannelNameId())){
                throw new Exception();
            }

            HashMap<String,Boolean>moderators=new HashMap<>();
            moderators.put(moderator.getUserNameId(),true);
            channelRepository.updateModeratorsWithID(channel.getChannelNameId(),moderators);
            //Continue adding the moderator
            return "Moderator added to Channel Successfully";

        }
        catch (Exception e){
            throw new Exception("Error: Couldn't add Channel");
        }
    }
    @RabbitListener(queues = "${mq.queues.response.reddit.assignModerators}")
    public void receive(String response, Message message) {
        String indicator = generalConfig.getCommands().get("assignModerators");
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info(indicator + "Service::CorrelationId: {}, message: {}", correlationId, response);
    }
}
