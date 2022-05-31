package com.ScalableTeam.reddit.app.seeders;

import com.ScalableTeam.arango.Channel;
import com.ScalableTeam.reddit.app.repository.ChannelRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@AllArgsConstructor
@Slf4j
public class ChannelSeeder {
    private static final String prefix = "R";
    private static final String adminprefix = "u";

    private final ChannelRepository channelRepository;

    public Set<String> seedChannels() {
        log.info("Seed Channels:-----");
        int channelNum = 0;
        Set<String> channels = new HashSet<>();
        for (int i = 0; i < 20; i++) {
            String id = prefix + channelNum++;
            channels.add(id);
//            Channel channel = Channel.builder()
//                    .channelNameId(id)
//                    .adminId(adminprefix)
////                    .password(password)
////                    .profilePhotoLink(photoLink)
//                    .build();
            Channel channel = new Channel(prefix+i,adminprefix+channelNum);
            channelRepository.save(channel);
        }
        return channels;
    }
}
