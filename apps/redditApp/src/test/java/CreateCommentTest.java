import com.ScalableTeam.arango.Comment;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.reddit.RedditApplication;
import com.ScalableTeam.reddit.app.comment.CommentService;
import com.ScalableTeam.reddit.app.repository.CommentRepository;
import com.ScalableTeam.reddit.app.repository.PostCommentHierarchyRepository;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import config.TestBeansConfig;
import mocks.CommentMock;
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
import utils.CreateCommentPopulator;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@Import(TestBeansConfig.class)
@SpringBootTest(classes = RedditApplication.class)
public class CreateCommentTest {
    @Autowired
    private ApplicationContext context;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostCommentHierarchyRepository postCommentHierarchyRepository;

    @Test
    void createComment() throws Exception {
        //given
        Comment c = CommentMock.getComment();
        //when
        CommentService command = context.getBean(CommentService.class);
        command.execute(c);
        //then
        assertTrue(commentRepository.existsById(c.getId()));
        assertTrue(postCommentHierarchyRepository.existsById(PostMock.getId() + CommentMock.getId()));
    }

    @BeforeEach
    public void prep() {
        CreateCommentPopulator.populate(userRepository, postRepository);
    }

    @AfterEach
    public void clean() {
        CreateCommentPopulator.clear(userRepository, postRepository, commentRepository, postCommentHierarchyRepository);
    }
}
