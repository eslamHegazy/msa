package com.ScalableTeam.reddit.app.seeders;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;

@Slf4j
@Component
@AllArgsConstructor
public class DataLoader {
    private final UserSeeder userSeeder;
    private final PostSeeder postSeeder;
    private final CommentSeeder commentSeeder;
    private final ChannelSeeder channelSeeder;

    @PostConstruct
    private void loadData() {
//        log.info("Data Loader:----");
//        Set<String> users = userSeeder.seedUsers();
//        Set<String> posts = postSeeder.seedPosts(users);
//        commentSeeder.seedComments(users, posts);
//        channelSeeder.seedChannels();
    }
}
