import com.ScalableTeam.arango.Comment;
import com.ScalableTeam.arango.Post;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.models.reddit.VoteCommentForm;
import com.ScalableTeam.reddit.RedditApplication;
import com.ScalableTeam.reddit.app.caching.CachingService;
import com.ScalableTeam.reddit.app.comment.CommentService;
import com.ScalableTeam.reddit.app.comment.DownvoteCommentService;
import com.ScalableTeam.reddit.app.comment.UpvoteCommentService;
import com.ScalableTeam.reddit.app.post.CreatePostService;
import com.ScalableTeam.reddit.app.readWall.ReadWallService;
import com.ScalableTeam.reddit.app.repository.CommentRepository;
import com.ScalableTeam.reddit.app.repository.PostCommentHierarchyRepository;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.reddit.app.repository.vote.CommentVoteRepository;
import com.ScalableTeam.reddit.app.repository.vote.UserVoteCommentRepository;
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
import utils.CreateCommentPopulator;
import utils.CreatePostPopulator;
import utils.ReadWallPopulator;
import utils.VoteCommentPopulator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@Import(TestBeansConfig.class)
@SpringBootTest(classes = RedditApplication.class)
public class ReadWallTest {
    @Autowired
    private ApplicationContext context;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CachingService cachingService;
    @Test
    void readWall() throws Exception {
        //given
        String userNameId=UserMock.getId();
        //when
        ReadWallService command=context.getBean(ReadWallService.class);
        String wall =command.execute(userNameId);
        //then
        assertTrue(wall.contains("id='"+PostMock.getPost().getId()+"'"));
        assertEquals(userRepository.findById(UserMock.getId()).get().getLatestReadPostId(), PostMock.getId());
    }

    @BeforeEach
    public void prep() {
        ReadWallPopulator.populate(userRepository,postRepository);
    }

    @AfterEach
    public void clean() {
        ReadWallPopulator.clear(userRepository,postRepository,cachingService);
    }
}
