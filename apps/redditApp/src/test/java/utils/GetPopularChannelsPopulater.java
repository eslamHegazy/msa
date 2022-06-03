package utils;

import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.models.reddit.FollowRedditForm;
import com.ScalableTeam.reddit.app.caching.CachingService;
import com.ScalableTeam.reddit.app.followReddit.FollowRedditService;
import com.ScalableTeam.reddit.app.repository.ChannelRepository;
import com.ScalableTeam.reddit.app.repository.vote.RedditFollowRepository;
import com.ScalableTeam.reddit.config.PopularityConfig;
import mocks.ChannelMock2;
import mocks.UserMock;
import org.springframework.context.ApplicationContext;

public class GetPopularChannelsPopulater {


    public static void populate(UserRepository userRepository, PopularityConfig popularityConfig,
                                ApplicationContext context, ChannelRepository channelRepository) throws Exception {
//        userRepository.save(UserMock.getUserFollowsChannel(PostMock.getChannelId()));
        channelRepository.save(ChannelMock2.getChannelWithAdminAsModerator());
        int popularChannelsFollowThreshold = popularityConfig.getChannelFollowersThreshold();
        for (int i = 0; i < popularChannelsFollowThreshold; i++) {
            String followerId = UserMock.getId() + "Follower" + i;
            userRepository.save(UserMock.getUserWithId(followerId));
            FollowRedditForm followRedditForm = new FollowRedditForm(followerId, ChannelMock2.getChannelNameId());
            FollowRedditService command = context.getBean(FollowRedditService.class);
            command.execute(followRedditForm);
        }

    }

    public static void clear(UserRepository userRepository,
                             CachingService cachingService
            , PopularityConfig popularityConfig, RedditFollowRepository redditFollowRepository) {

        cachingService.removePreviouslyPopularChannel(ChannelMock2.getChannelNameId());
        int popularChannelsFollowThreshold = popularityConfig.getChannelFollowersThreshold();
        for (int i = 0; i < popularChannelsFollowThreshold; i++) {
            String followerId = UserMock.getId() + "Follower" + i;
            userRepository.deleteById(followerId);
        }
        redditFollowRepository.deleteAll();


    }
}
