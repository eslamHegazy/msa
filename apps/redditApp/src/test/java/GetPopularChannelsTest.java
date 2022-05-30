import com.ScalableTeam.arango.Comment;
import com.ScalableTeam.arango.Post;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.models.reddit.VoteCommentForm;
import com.ScalableTeam.models.reddit.VotePostForm;
import com.ScalableTeam.reddit.RedditApplication;
import com.ScalableTeam.reddit.app.caching.CachingService;
import com.ScalableTeam.reddit.app.comment.CommentService;
import com.ScalableTeam.reddit.app.comment.DownvoteCommentService;
import com.ScalableTeam.reddit.app.comment.UpvoteCommentService;
import com.ScalableTeam.reddit.app.popularChannels.GetPopularChannelsService;
import com.ScalableTeam.reddit.app.post.CreatePostService;
import com.ScalableTeam.reddit.app.post.GetPopularPostsService;
import com.ScalableTeam.reddit.app.post.UpvotePostService;
import com.ScalableTeam.reddit.app.readWall.ReadWallService;
import com.ScalableTeam.reddit.app.repository.ChannelRepository;
import com.ScalableTeam.reddit.app.repository.CommentRepository;
import com.ScalableTeam.reddit.app.repository.PostCommentHierarchyRepository;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.reddit.app.repository.vote.*;
import com.ScalableTeam.reddit.config.PopularityConfig;
import config.TestBeansConfig;
import mocks.ChannelMock2;
import mocks.CommentMock;
import mocks.PostMock;
import mocks.UserMock;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import utils.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        GetPopularChannelsService command=context.getBean(GetPopularChannelsService.class);
        String popularChannels =command.execute("");

        //then
        assertTrue(popularChannels.contains( "channelNameId='" + ChannelMock2.getChannelNameId()+"'"));
    }

    @BeforeEach
    public void prep() throws Exception {
        GetPopularChannelsPopulater.populate(userRepository,popularityConfig,context,channelRepository);
    }

    @AfterEach
    public void clean() {
        GetPopularChannelsPopulater.clear(userRepository,cachingService,popularityConfig,redditFollowRepository);
    }
}
