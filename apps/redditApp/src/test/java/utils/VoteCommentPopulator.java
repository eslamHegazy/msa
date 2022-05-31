package utils;

import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.reddit.app.repository.CommentRepository;
import com.ScalableTeam.reddit.app.repository.vote.CommentVoteRepository;
import com.ScalableTeam.reddit.app.repository.vote.PostVoteRepository;
import com.ScalableTeam.reddit.app.repository.vote.UserVoteCommentRepository;
import com.ScalableTeam.reddit.app.repository.vote.UserVotePostRepository;
import mocks.CommentMock;
import mocks.PostMock;
import mocks.UserMock;

public class VoteCommentPopulator {
    public static void populate(UserRepository userRepository, CommentRepository commentRepository) {
        userRepository.save(UserMock.getUser());
        userRepository.save(UserMock.getUserWithId(UserMock.getId() + "Voter"));
        commentRepository.save(CommentMock.getCommentWithUserNameId(UserMock.getId()));
    }

    public static void clear(UserRepository userRepository, CommentRepository commentRepository, UserVoteCommentRepository userVoteCommentRepository, CommentVoteRepository commentVoteRepository){
        userRepository.delete(UserMock.getUser());
        userRepository.delete(UserMock.getUserWithId(UserMock.getId() + "Voter"));
        commentRepository.delete(CommentMock.getCommentWithUserNameId(UserMock.getId()));
        userVoteCommentRepository.deleteAll();
        commentVoteRepository.deleteAll();
    }
}
