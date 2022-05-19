package com.ScalableTeam.reddit.app.moderation;

import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.entity.Channel;
import com.ScalableTeam.reddit.app.repository.ChannelRepository;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.reddit.app.repository.UserRepository;
import com.ScalableTeam.reddit.app.requestForms.ViewReportsForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.Optional;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
public class ViewReportsService implements MyCommand {

    @Autowired
    private final ChannelRepository channelRepository;
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    public ViewReportsService(ChannelRepository channelRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
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
            if(channel.getModerators().containsKey(request.getModId())){
                if (channel.getReports()!=null){
                    return channel.getReports().toString();
                }
            }
        }catch(Exception e){
    return e.getMessage();
        }

        return "reports:";
    }
}
