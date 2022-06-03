import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.reddit.RedditApplication;
import com.ScalableTeam.reddit.app.caching.CachingService;
import com.ScalableTeam.reddit.app.post.GetPopularPostsService;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.reddit.app.repository.vote.PostVoteRepository;
import com.ScalableTeam.reddit.app.repository.vote.UserVotePostRepository;
import com.ScalableTeam.reddit.config.PopularityConfig;
import config.TestBeansConfig;
import mocks.PostMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import utils.GetPopularPostsPopulater;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@Import(TestBeansConfig.class)
@SpringBootTest(classes = RedditApplication.class)
public class GetPopularPostsTest {
    @Autowired
    private ApplicationContext context;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CachingService cachingService;
    @Autowired
    private PostVoteRepository postVoteRepository;
    @Autowired
    private UserVotePostRepository userVotePostRepository;
    @Autowired
    private PopularityConfig popularityConfig;

    @Test
    void getPopularPosts() throws Exception {
        //given

        //when
        GetPopularPostsService command = context.getBean(GetPopularPostsService.class);
        String popularPosts = command.execute("");

        //then
        assertTrue(popularPosts.contains("id='" + PostMock.getPost().getId() + "'"));
    }

    @BeforeEach
    public void prep() throws Exception {
        GetPopularPostsPopulater.populate(userRepository, postRepository, popularityConfig, context);
    }

    @AfterEach
    public void clean() {
        GetPopularPostsPopulater.clear(userRepository, postRepository, cachingService,
                userVotePostRepository, postVoteRepository, popularityConfig);
    }
}
