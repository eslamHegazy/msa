import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.models.reddit.VoteCommentForm;
import com.ScalableTeam.reddit.RedditApplication;
import com.ScalableTeam.reddit.app.comment.DownvoteCommentService;
import com.ScalableTeam.reddit.app.comment.UpvoteCommentService;
import com.ScalableTeam.reddit.app.repository.CommentRepository;
import com.ScalableTeam.reddit.app.repository.vote.CommentVoteRepository;
import com.ScalableTeam.reddit.app.repository.vote.UserVoteCommentRepository;
import config.TestBeansConfig;
import mocks.CommentMock;
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
import utils.VoteCommentPopulator;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@Import(TestBeansConfig.class)
@SpringBootTest(classes = RedditApplication.class)

// todo: test concurrency and rolling back
public class VoteCommentCommandTest {

    @Autowired
    private ApplicationContext context;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentVoteRepository commentVoteRepository;
    @Autowired
    private UserVoteCommentRepository userVoteCommentRepository;

    @BeforeEach
    public void prep() {
        VoteCommentPopulator.populate(userRepository, commentRepository);
    }

    @AfterEach
    public void clean() {
        VoteCommentPopulator.clear(userRepository, commentRepository, userVoteCommentRepository, commentVoteRepository);
    }

    @Test
    void upvote_FirstTime() throws Exception {
        final String voterId = UserMock.getId() + "Voter";
        final String commentId = CommentMock.getId();
        final VoteCommentForm voteCommentForm = VoteCommentForm
                .builder()
                .commentId(commentId)
                .userNameId(voterId)
                .build();
        UpvoteCommentService command = context.getBean(UpvoteCommentService.class);
        String response = command.execute(voteCommentForm);
        String expected = String.format("User %s %s %s", voterId, CommentMock.upvoteCommentFirstTime, commentId);
        assertEquals(expected, response);
        assertEquals(1, commentRepository.findById(commentId).get().getUpvoteCount());
        assertEquals(0, commentRepository.findById(commentId).get().getDownvoteCount());
    }

    @Test
    void upvote_SecondTime() throws Exception {
        final String voterId = UserMock.getId() + "Voter";
        final String commentId = CommentMock.getId();
        final VoteCommentForm voteCommentForm = VoteCommentForm
                .builder()
                .commentId(commentId)
                .userNameId(voterId)
                .build();
        UpvoteCommentService command = context.getBean(UpvoteCommentService.class);
        String response = command.execute(voteCommentForm);
        String expected = String.format("User %s %s %s", voterId, CommentMock.upvoteCommentFirstTime, commentId);
        assertEquals(expected, response);
        assertEquals(1, commentRepository.findById(commentId).get().getUpvoteCount());
        assertEquals(0, commentRepository.findById(commentId).get().getDownvoteCount());
        response = command.execute(voteCommentForm);
        expected = String.format("User %s %s %s", voterId, CommentMock.upvoteCommentSecondTime, commentId);
        assertEquals(expected, response);
        assertEquals(0, commentRepository.findById(commentId).get().getUpvoteCount());
        assertEquals(0, commentRepository.findById(commentId).get().getDownvoteCount());
    }

    @Test
    void upvote_upvoteThenDownvote() throws Exception {
        final String voterId = UserMock.getId() + "Voter";
        final String commentId = CommentMock.getId();
        final VoteCommentForm voteCommentForm = VoteCommentForm
                .builder()
                .commentId(commentId)
                .userNameId(voterId)
                .build();
        UpvoteCommentService command = context.getBean(UpvoteCommentService.class);
        DownvoteCommentService command1 = context.getBean(DownvoteCommentService.class);
        String response = command1.execute(voteCommentForm);
        response = command.execute(voteCommentForm);
        String expected = String.format("User %s %s %s", voterId, CommentMock.upvoteAfterDownvote, commentId);
        assertEquals(expected, response);
        assertEquals(1, commentRepository.findById(commentId).get().getUpvoteCount());
        assertEquals(0, commentRepository.findById(commentId).get().getDownvoteCount());
    }

    @Test
    void downvote_FirstTime() throws Exception {
        final String voterId = UserMock.getId() + "Voter";
        final String commentId = CommentMock.getId();
        final VoteCommentForm voteCommentForm = VoteCommentForm
                .builder()
                .commentId(commentId)
                .userNameId(voterId)
                .build();
        DownvoteCommentService command = context.getBean(DownvoteCommentService.class);
        String response = command.execute(voteCommentForm);
        String expected = String.format("User %s %s %s", voterId, CommentMock.downvoteCommentFirstTime, commentId);
        assertEquals(expected, response);
        assertEquals(0, commentRepository.findById(commentId).get().getUpvoteCount());
        assertEquals(1, commentRepository.findById(commentId).get().getDownvoteCount());
    }

    @Test
    void downvote_SecondTime() throws Exception {
        final String voterId = UserMock.getId() + "Voter";
        final String commentId = CommentMock.getId();
        final VoteCommentForm voteCommentForm = VoteCommentForm
                .builder()
                .commentId(commentId)
                .userNameId(voterId)
                .build();
        DownvoteCommentService command = context.getBean(DownvoteCommentService.class);
        String response = command.execute(voteCommentForm);
        String expected = String.format("User %s %s %s", voterId, CommentMock.downvoteCommentFirstTime, commentId);
        assertEquals(expected, response);
        assertEquals(0, commentRepository.findById(commentId).get().getUpvoteCount());
        assertEquals(1, commentRepository.findById(commentId).get().getDownvoteCount());
        response = command.execute(voteCommentForm);
        expected = String.format("User %s %s %s", voterId, CommentMock.downvoteCommentSecondTime, commentId);
        assertEquals(expected, response);
        assertEquals(0, commentRepository.findById(commentId).get().getUpvoteCount());
        assertEquals(0, commentRepository.findById(commentId).get().getDownvoteCount());
    }

    @Test
    void downvote_downvoteThenUpvote() throws Exception {
        final String voterId = UserMock.getId() + "Voter";
        final String commentId = CommentMock.getId();
        final VoteCommentForm voteCommentForm = VoteCommentForm
                .builder()
                .commentId(commentId)
                .userNameId(voterId)
                .build();
        UpvoteCommentService command = context.getBean(UpvoteCommentService.class);
        DownvoteCommentService command1 = context.getBean(DownvoteCommentService.class);
        String response = command.execute(voteCommentForm);
        response = command1.execute(voteCommentForm);
        String expected = String.format("User %s %s %s", voterId, CommentMock.downvoteAfterUpvote, commentId);
        assertEquals(expected, response);
        assertEquals(0, commentRepository.findById(commentId).get().getUpvoteCount());
        assertEquals(1, commentRepository.findById(commentId).get().getDownvoteCount());
    }
}
