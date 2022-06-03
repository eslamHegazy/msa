import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.reddit.RedditApplication;
import com.ScalableTeam.reddit.app.caching.CachingService;
import com.ScalableTeam.reddit.app.popularChannels.GetPopularChannelsService;
import com.ScalableTeam.reddit.app.repository.ChannelRepository;
import com.ScalableTeam.reddit.app.repository.vote.RedditFollowRepository;
import com.ScalableTeam.reddit.config.PopularityConfig;
import config.TestBeansConfig;
import mocks.ChannelMock2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import utils.GetPopularChannelsPopulater;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@Import(TestBeansConfig.class)
@SpringBootTest(classes = RedditApplication.class)
public class GetPopularChannelsTest {
    @Autowired
    private ApplicationContext context;
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CachingService cachingService;
    @Autowired
    private PopularityConfig popularityConfig;
    @Autowired
    private RedditFollowRepository redditFollowRepository;

    @Test
    void getPopularChannels() throws Exception {
        //given

        //when
        GetPopularChannelsService command = context.getBean(GetPopularChannelsService.class);
        String popularChannels = command.execute("");

        //then
        assertTrue(popularChannels.contains("channelNameId='" + ChannelMock2.getChannelNameId() + "'"));
    }

    @BeforeEach
    public void prep() throws Exception {
        GetPopularChannelsPopulater.populate(userRepository, popularityConfig, context, channelRepository);
    }

    @AfterEach
    public void clean() {
        GetPopularChannelsPopulater.clear(userRepository, cachingService, popularityConfig, redditFollowRepository);
    }
}
