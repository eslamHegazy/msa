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
import com.ScalableTeam.reddit.app.post.CreatePostService;
import com.ScalableTeam.reddit.app.post.GetPopularPostsService;
import com.ScalableTeam.reddit.app.post.UpvotePostService;
import com.ScalableTeam.reddit.app.readWall.ReadWallService;
import com.ScalableTeam.reddit.app.repository.CommentRepository;
import com.ScalableTeam.reddit.app.repository.PostCommentHierarchyRepository;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.reddit.app.repository.vote.CommentVoteRepository;
import com.ScalableTeam.reddit.app.repository.vote.PostVoteRepository;
import com.ScalableTeam.reddit.app.repository.vote.UserVoteCommentRepository;
import com.ScalableTeam.reddit.app.repository.vote.UserVotePostRepository;
import com.ScalableTeam.reddit.config.PopularityConfig;
import config.TestBeansConfig;
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
        GetPopularPostsService command=context.getBean(GetPopularPostsService.class);
        String popularPosts =command.execute("");

        //then
        assertTrue(popularPosts.contains("id='"+PostMock.getPost().getId()+"'"));
        }

    @BeforeEach
    public void prep() throws Exception {
        GetPopularPostsPopulater.populate(userRepository,postRepository,popularityConfig,context);
    }

    @AfterEach
    public void clean() {
        GetPopularPostsPopulater.clear(userRepository,postRepository,cachingService,
                userVotePostRepository,postVoteRepository,popularityConfig);
    }
}
