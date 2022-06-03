import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.models.reddit.CreateChannelForm;
import com.ScalableTeam.reddit.RedditApplication;
import com.ScalableTeam.reddit.app.adminServices.CreateChannelService;
import com.ScalableTeam.reddit.app.repository.ChannelRepository;
import config.TestBeansConfig;
import mocks.CreateChannelFormMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import utils.CreateChannelPopulator;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@Import(TestBeansConfig.class)
@SpringBootTest(classes = RedditApplication.class)
public class CreateChannelTest {
    @Autowired
    private ApplicationContext context;
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void createChannel() throws Exception {
        //given
        CreateChannelForm createChannelForm = CreateChannelFormMock.getCreateChannelForm();
        //when
        CreateChannelService command = context.getBean(CreateChannelService.class);
        command.execute(createChannelForm);
        //then
        assertTrue(channelRepository.existsById(createChannelForm.getChannelNameId()));
    }

    @BeforeEach
    public void prep() {
        CreateChannelPopulator.populate(userRepository);
    }

    @AfterEach
    public void clean() {
        CreateChannelPopulator.clear(userRepository, channelRepository);
    }
}
