import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.models.reddit.AssignModeratorsForm;
import com.ScalableTeam.reddit.RedditApplication;
import com.ScalableTeam.reddit.app.adminServices.AssignModeratorsService;
import com.ScalableTeam.reddit.app.repository.ChannelRepository;
import config.TestBeansConfig;
import mocks.AssignModeratorsFormMock;
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
import utils.AssignModeratorsPopulator;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@Import(TestBeansConfig.class)
@SpringBootTest(classes = RedditApplication.class)
public class AssignModeratorsTest {
    @Autowired
    private ApplicationContext context;
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void assignModerator() throws Exception {
        //given
        AssignModeratorsForm assignModeratorsForm = AssignModeratorsFormMock.getAssignModeratorsForm();
        //when
        AssignModeratorsService command = context.getBean(AssignModeratorsService.class);
        command.execute(assignModeratorsForm);
        //then
        assertTrue(channelRepository.findById(ChannelMock2.getChannelNameId()).get().getModerators().get(assignModeratorsForm.getModeratorId()));
    }

    @BeforeEach
    public void prep() {
        AssignModeratorsPopulator.populate(userRepository, channelRepository);
    }

    @AfterEach
    public void clean() {
        AssignModeratorsPopulator.clear(userRepository, channelRepository);
    }
}
