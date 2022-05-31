import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.models.reddit.VotePostForm;
import com.ScalableTeam.reddit.RedditApplication;
import com.ScalableTeam.reddit.app.post.DownvotePostService;
import com.ScalableTeam.reddit.app.post.UpvotePostService;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.reddit.app.repository.vote.PostVoteRepository;
import com.ScalableTeam.reddit.app.repository.vote.UserVotePostRepository;
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
import utils.VotePostPopulator;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@Import(TestBeansConfig.class)
@SpringBootTest(classes = RedditApplication.class)

// todo: test concurrency and rolling back
public class VotePostCommandTest {

    @Autowired
    private ApplicationContext context;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostVoteRepository postVoteRepository;
    @Autowired
    private UserVotePostRepository userVotePostRepository;

    @BeforeEach
    public void prep() {
        VotePostPopulator.populate(userRepository, postRepository);
    }

    @AfterEach
    public void clean() {
        VotePostPopulator.clear(userRepository, postRepository, userVotePostRepository, postVoteRepository);
    }

    @Test
    void upvote_FirstTime() throws Exception {
        final String voterId = UserMock.getId() + "Voter";
        final String postId = PostMock.getId();
        final VotePostForm votePostForm = VotePostForm
                .builder()
                .postId(postId)
                .userNameId(voterId)
                .build();
        UpvotePostService command = context.getBean(UpvotePostService.class);
        String response = command.execute(votePostForm);
        String expected = String.format("User %s %s %s", voterId, PostMock.upvotePostFirstTime, postId);
        assertEquals(expected, response);
        assertEquals(1, postRepository.findById(postId).get().getUpvoteCount());
        assertEquals(0, postRepository.findById(postId).get().getDownvoteCount());
    }

    @Test
    void upvote_SecondTime() throws Exception {
        final String voterId = UserMock.getId() + "Voter";
        final String postId = PostMock.getId();
        final VotePostForm votePostForm = VotePostForm
                .builder()
                .postId(postId)
                .userNameId(voterId)
                .build();
        UpvotePostService command = context.getBean(UpvotePostService.class);
        String response = command.execute(votePostForm);
        String expected = String.format("User %s %s %s", voterId, PostMock.upvotePostFirstTime, postId);
        assertEquals(expected, response);
        response = command.execute(votePostForm);
        expected = String.format("User %s %s %s", voterId, PostMock.upvotePostSecondTime, postId);
        assertEquals(expected, response);
        assertEquals(0, postRepository.findById(postId).get().getUpvoteCount());
        assertEquals(0, postRepository.findById(postId).get().getDownvoteCount());
    }

    @Test
    void upvote_upvoteThenDownvote() throws Exception {
        final String voterId = UserMock.getId() + "Voter";
        final String postId = PostMock.getId();
        final VotePostForm votePostForm = VotePostForm
                .builder()
                .postId(postId)
                .userNameId(voterId)
                .build();
        DownvotePostService command = context.getBean(DownvotePostService.class);
        UpvotePostService command1 = context.getBean(UpvotePostService.class);
        String response = command.execute(votePostForm);
        response = command1.execute(votePostForm);
        String expected = String.format("User %s %s %s", voterId, PostMock.upvoteAfterDownvote, postId);
        assertEquals(expected, response);
        assertEquals(1, postRepository.findById(postId).get().getUpvoteCount());
        assertEquals(0, postRepository.findById(postId).get().getDownvoteCount());
    }

    @Test
    void downvote_FirstTime() throws Exception {
        final String voterId = UserMock.getId() + "Voter";
        final String postId = PostMock.getId();
        final VotePostForm votePostForm = VotePostForm
                .builder()
                .postId(postId)
                .userNameId(voterId)
                .build();
        DownvotePostService command = context.getBean(DownvotePostService.class);
        String response = command.execute(votePostForm);
        String expected = String.format("User %s %s %s", voterId, PostMock.downvotePostFirstTime, postId);
        assertEquals(expected, response);
        assertEquals(0, postRepository.findById(postId).get().getUpvoteCount());
        assertEquals(1, postRepository.findById(postId).get().getDownvoteCount());
    }

    @Test
    void downvote_SecondTime() throws Exception {
        final String voterId = UserMock.getId() + "Voter";
        final String postId = PostMock.getId();
        final VotePostForm votePostForm = VotePostForm
                .builder()
                .postId(postId)
                .userNameId(voterId)
                .build();
        DownvotePostService command = context.getBean(DownvotePostService.class);
        String response = command.execute(votePostForm);
        String expected = String.format("User %s %s %s", voterId, PostMock.downvotePostFirstTime, postId);
        assertEquals(expected, response);
        response = command.execute(votePostForm);
        expected = String.format("User %s %s %s", voterId, PostMock.downvotePostSecondTime, postId);
        assertEquals(expected, response);
        assertEquals(0, postRepository.findById(postId).get().getUpvoteCount());
        assertEquals(0, postRepository.findById(postId).get().getDownvoteCount());
    }

    @Test
    void downvote_downvoteThenUpvote() throws Exception {
        final String voterId = UserMock.getId() + "Voter";
        final String postId = PostMock.getId();
        final VotePostForm votePostForm = VotePostForm
                .builder()
                .postId(postId)
                .userNameId(voterId)
                .build();
        DownvotePostService command = context.getBean(DownvotePostService.class);
        UpvotePostService command1 = context.getBean(UpvotePostService.class);
        String response = command1.execute(votePostForm);
        response = command.execute(votePostForm);
        String expected = String.format("User %s %s %s", voterId, PostMock.downvoteAfterUpvote, postId);
        assertEquals(expected, response);
        assertEquals(0, postRepository.findById(postId).get().getUpvoteCount());
        assertEquals(1, postRepository.findById(postId).get().getDownvoteCount());
    }
}
