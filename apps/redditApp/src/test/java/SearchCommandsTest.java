import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.reddit.RedditApplication;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.reddit.app.search.SearchByChannelService;
import com.ScalableTeam.reddit.app.search.SearchByTitleService;
import config.TestBeansConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import utils.SearchDataPopulater;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@Import(TestBeansConfig.class)
@SpringBootTest(classes = RedditApplication.class)
public class SearchCommandsTest {

    @Autowired
    private ApplicationContext context;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    public void prep() {
        SearchDataPopulater.populate(userRepository, postRepository);
    }

    @AfterEach
    public void clean() {
        SearchDataPopulater.clear(userRepository, postRepository);
    }

    @Test
    void givenSubStringTitle_whenSearchByTitle_returnAllPosts() throws Exception {
        //given
        final String title = "the";
        //when
        SearchByTitleService searchCommand = context.getBean(SearchByTitleService.class);
        //return
        String posts = searchCommand.execute(title);
        int cnt = posts.split("Post").length;
        assertEquals(8, cnt);
    }

    @Test
    void givenChannelId_whenSearchByChannel_returnAllPosts() throws Exception {
        //given
        final String channelId = "Mock";
        //when
        SearchByChannelService searchCommand = context.getBean(SearchByChannelService.class);
        //return
        String posts = searchCommand.execute(channelId);
        int cnt = posts.split("Post").length;
        assertEquals(12, cnt);
    }

    @Test
    void givenNonExistingTitle_whenSearchByTitle_returnZeroPosts() throws Exception {
        //given
        final String title = "xxxxx";
        //when
        SearchByTitleService searchCommand = context.getBean(SearchByTitleService.class);
        //return
        String posts = searchCommand.execute(title);
        assertEquals("[]", posts);
    }

    @Test
    void givenNonExistingChannelId_whenSearchByChannel_returnZeroPosts() throws Exception {
        //given
        final String channelId = "xxxxx";
        //when
        SearchByChannelService searchCommand = context.getBean(SearchByChannelService.class);
        //return
        String posts = searchCommand.execute(channelId);
        assertEquals("[]", posts);
    }
}
