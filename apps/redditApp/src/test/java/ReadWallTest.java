import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.reddit.RedditApplication;
import com.ScalableTeam.reddit.app.caching.CachingService;
import com.ScalableTeam.reddit.app.readWall.ReadWallService;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import config.TestBeansConfig;
import mocks.PostMock;
import mocks.UserMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import utils.ReadWallPopulator;

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
        String userNameId = UserMock.getId();
        //when
        ReadWallService command = context.getBean(ReadWallService.class);
        String wall = command.execute(userNameId);
        //then
        assertTrue(wall.contains("id='" + PostMock.getPost().getId() + "'"));
        assertEquals(userRepository.findById(UserMock.getId()).get().getLatestReadPostId(), PostMock.getId());
    }

    @BeforeEach
    public void prep() {
        ReadWallPopulator.populate(userRepository, postRepository);
    }

    @AfterEach
    public void clean() {
        ReadWallPopulator.clear(userRepository, postRepository, cachingService);
    }
}
