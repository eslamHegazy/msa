import com.ScalableTeam.arango.Post;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.reddit.RedditApplication;
import com.ScalableTeam.reddit.app.post.CreatePostService;
import com.ScalableTeam.reddit.app.repository.PostRepository;
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
import utils.CreatePostPopulator;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@Import(TestBeansConfig.class)
@SpringBootTest(classes = RedditApplication.class)
public class CreatePostTest {
    @Autowired
    private ApplicationContext context;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void createPost() throws Exception {
        //given
        Post p = PostMock.getPost();
        //when
        CreatePostService command = context.getBean(CreatePostService.class);
        command.execute(p);
        //then
        assertTrue(postRepository.existsById(p.getId()));
    }

    @BeforeEach
    public void prep() {
        CreatePostPopulator.populate(userRepository);
    }

    @AfterEach
    public void clean() {
        CreatePostPopulator.clear(userRepository, postRepository);
    }
}
